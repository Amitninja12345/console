/*
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.hal.processor.mbui;

import java.beans.Introspector;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.google.auto.service.AutoService;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import org.jboss.auto.AbstractProcessor;
import org.jboss.hal.ballroom.VerticalNavigation;
import org.jboss.hal.ballroom.form.Form;
import org.jboss.hal.ballroom.table.DataTable;
import org.jboss.hal.core.mvp.MbuiViewImpl;
import org.jboss.hal.processor.TemplateNames;
import org.jboss.hal.processor.TypeSimplifier;
import org.jboss.hal.spi.MbuiElement;
import org.jboss.hal.spi.MbuiView;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import static org.jboss.hal.processor.mbui.ElementType.DataTable;
import static org.jboss.hal.processor.mbui.ElementType.Form;
import static org.jboss.hal.processor.mbui.ElementType.VerticalNavigation;

/**
 * @author Harald Pehl
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("org.jboss.hal.spi.MbuiView")
@SuppressWarnings({"HardCodedStringLiteral", "DuplicateStringLiteralInspection"})
public class MbuiViewProcessor extends AbstractProcessor {

    private static final String TEMPLATE = "MbuiView.ftl";
    private static final Escaper JAVA_STRING_ESCAPER = Escapers.builder()
            .addEscape('"', "\\\"")
            .addEscape('\n', "")
            .addEscape('\r', "")
            .build();

    private XPathFactory xpath;

    public MbuiViewProcessor() {
        this(MbuiViewProcessor.class, TemplateNames.TEMPLATES);
    }

    protected MbuiViewProcessor(final Class resourceLoaderClass, final String templates) {
        super(resourceLoaderClass, templates);
    }

    @Override
    protected boolean onProcess(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        for (Element e : roundEnv.getElementsAnnotatedWith(MbuiView.class)) {
            TypeElement type = (TypeElement) e;
            MbuiView mbuiView = type.getAnnotation(MbuiView.class);
            validateType(type, mbuiView);
            processType(type, mbuiView);
        }
        return false;
    }


    // ------------------------------------------------------ general validation

    private void validateType(final TypeElement type, final MbuiView mbuiView) {
        if (mbuiView == null) {
            // This shouldn't happen unless the compilation environment is buggy,
            // but it has happened in the past and can crash the compiler.
            error(type, "Annotation processor for @%s was invoked with a type that does not have that " +
                    "annotation; this is probably a compiler bug", MbuiView.class.getSimpleName());
        }
        if (type.getKind() != ElementKind.CLASS) {
            error(type, "@%s only applies to classes", MbuiView.class.getSimpleName());
        }
        if (!isAssignable(type, MbuiViewImpl.class)) {
            error(type, "Missing base class %s", MbuiViewImpl.class.getSimpleName());
        }
        if (ancestorIsMbuiView(type)) {
            error(type, "One @%s class may not extend another", MbuiView.class.getSimpleName());
        }
        if (isAssignable(type, Annotation.class)) {
            error(type, "@%s may not be used to implement an annotation interface", MbuiView.class.getSimpleName());
        }
    }

    private boolean ancestorIsMbuiView(TypeElement type) {
        while (true) {
            TypeMirror parentMirror = type.getSuperclass();
            if (parentMirror.getKind() == TypeKind.NONE) {
                return false;
            }
            TypeElement parentElement = (TypeElement) typeUtils.asElement(parentMirror);
            if (parentElement.getAnnotation(MbuiView.class) != null) {
                return true;
            }
            type = parentElement;
        }
    }

    private boolean isAssignable(TypeElement subType, Class<?> baseType) {
        return isAssignable(subType.asType(), baseType);
    }

    private boolean isAssignable(TypeMirror subType, Class<?> baseType) {
        return isAssignable(subType, getTypeMirror(baseType));
    }

    private boolean isAssignable(TypeMirror subType, TypeMirror baseType) {
        return typeUtils.isAssignable(typeUtils.erasure(subType), typeUtils.erasure(baseType));
    }

    private TypeMirror getTypeMirror(Class<?> c) {
        return processingEnv.getElementUtils().getTypeElement(c.getName()).asType();
    }


    // ------------------------------------------------------ processing

    protected void processType(final TypeElement type, final MbuiView mbuiView) {
        String subclass = TypeSimplifier.simpleNameOf(generatedClassName(type, "Mbui_", ""));
        String createMethod = verifyCreateMethod(type);
        MbuiViewContext context = new MbuiViewContext(TypeSimplifier.packageNameOf(type),
                TypeSimplifier.classNameOf(type), subclass, createMethod);

        // parse and validate the MBUI XML
        xpath = XPathFactory.instance();
        Document document = parseXml(type, mbuiView);
        validateDocument(type, document);

        // first process the metadata elements
        processMetadata(type, document, context);

        // then find and verify all @MbuiElement members
        processMbuiElements(type, document, context);
        processCrossReferences(document, context);

        // init parameters and abstract properties
        processAbstractProperties(type, context);

        // find and verify all @PostConstruct methods
        processPostConstruct(type, context);

        // generate code
        code(TEMPLATE, context.getPackage(), context.getSubclass(),
                () -> ImmutableMap.of("context", context));
        info("Generated MBUI view implementation [%s] for [%s]", context.getSubclass(), context.getBase());
    }

    String generatedClassName(TypeElement type, String prefix, String suffix) {
        String name = type.getSimpleName().toString();
        while (type.getEnclosingElement() instanceof TypeElement) {
            type = (TypeElement) type.getEnclosingElement();
            name = type.getSimpleName() + "_" + name;
        }
        String pkg = TypeSimplifier.packageNameOf(type);
        String dot = pkg.isEmpty() ? "" : ".";
        return pkg + dot + prefix + name + suffix;
    }

    String verifyCreateMethod(TypeElement type) {
        Optional<ExecutableElement> createMethod = ElementFilter.methodsIn(type.getEnclosedElements())
                .stream()
                .filter(method -> method.getModifiers().contains(Modifier.STATIC) &&
                        method.getReturnType().equals(type.asType()))
                .findAny();
        if (createMethod.isPresent()) {
            return createMethod.get().getSimpleName().toString();
        } else {
            error(type, "@%s needs to define one static method which returns an %s instance",
                    MbuiView.class.getSimpleName(), type.getSimpleName());
            return null;
        }
    }


    // ------------------------------------------------------ XML processing

    private Document parseXml(final TypeElement type, final MbuiView mbuiView) {
        String mbuiXml = Strings.isNullOrEmpty(mbuiView.value())
                ? type.getSimpleName().toString() + ".xml"
                : mbuiView.value();
        String fq = TypeSimplifier.packageNameOf(type).replace('.', '/') + File.separator + mbuiXml;

        try {
            FileObject file = processingEnv.getFiler().getResource(StandardLocation.CLASS_PATH, "", fq);
            return new SAXBuilder().build(file.openReader(true));

        } catch (IOException e) {
            error(type, "Cannot find MBUI XML \"%s\". " +
                    "Please make sure the file exists and resides in the source path.", fq);
        } catch (JDOMException e) {
            error(type, "Cannot parse MBUI XML \"%s\". Please verify that the file contains valid XML.", fq);
        }
        return null;
    }

    private void validateDocument(final TypeElement type, final Document document) {
        // verify root element
        org.jdom2.Element root = document.getRootElement();
        if (!root.getName().equals(XmlTags.VIEW)) {
            error(type, "Invalid root element in MBUI XML. Allowed: \"%s\", found: \"%s\".", XmlTags.VIEW,
                    root.getName());
        }

        // verify first child
        List<org.jdom2.Element> children = root.getChildren();
        if (children.isEmpty()) {
            error(type, "No children found in MBUI XML.");
        } else if (children.size() > 1) {
            error(type, "Only one child allowed in MBUI XML.");
        }
        org.jdom2.Element child = children.get(0);
        if (!(child.getName().equals(XmlTags.VERTICAL_NAVIGATION) || child.getName().equals(XmlTags.METADATA))) {
            error(type, "Invalid child of root element in MBUI XML. Allowed: \"%s\" or \"%s\", found: \"%s\".",
                    XmlTags.VERTICAL_NAVIGATION, XmlTags.METADATA, child.getName());
        }
    }

    private void processMetadata(final TypeElement type, final Document document, final MbuiViewContext context) {
        XPathExpression<org.jdom2.Element> expression = xpath.compile("//metadata", Filters.element());
        for (org.jdom2.Element element : expression.evaluate(document)) {
            String template = element.getAttributeValue("address");
            if (template == null) {
                error(type, "Missing address attribute in metadata element \"%s\"", xmlAsString(element));
            } else {
                context.addMetadata(template);
            }
        }
    }


    // ------------------------------------------------------ process @MbuiElement

    private void processMbuiElements(final TypeElement type, final Document document, final MbuiViewContext context) {
        ElementFilter.fieldsIn(type.getEnclosedElements()).stream()
                .filter(field -> MoreElements.isAnnotationPresent(field, MbuiElement.class))
                .forEach(field -> {

                    // verify the field
                    if (field.getModifiers().contains(Modifier.PRIVATE)) {
                        error(field, "@%s member must not be private", MbuiElement.class.getSimpleName());
                    }
                    if (field.getModifiers().contains(Modifier.STATIC)) {
                        error(field, "@%s member must not be static", MbuiElement.class.getSimpleName());
                    }

                    // verify the selector
                    String selector = getSelector(field);
                    org.jdom2.Element element = verifySelector(selector, field, document);

                    // delegate to specific processors based on element type
                    ElementType elementType = getMbuiElementType(field.asType());
                    if (elementType == null) {
                        error(field, "Unsupported type %s. Please choose one of %s", field.asType(),
                                EnumSet.allOf(ElementType.class));
                    } else {
                        switch (elementType) {
                            case VerticalNavigation:
                                processVerticalNavigation(field, element, selector, context);
                                break;
                            case DataTable:
                                processDataTables(field, element, selector, context);
                                break;
                            case Form:
                                processForms(field, element, selector, context);
                                break;
                        }
                    }
                });
    }

    private String getSelector(Element element) {
        String selector = null;

        //noinspection Guava
        com.google.common.base.Optional<AnnotationMirror> annotationMirror = MoreElements
                .getAnnotationMirror(element, MbuiElement.class);
        if (annotationMirror.isPresent()) {
            Map<? extends ExecutableElement, ? extends AnnotationValue> values = elementUtils
                    .getElementValuesWithDefaults(annotationMirror.get());
            if (!values.isEmpty()) {
                selector = String.valueOf(values.values().iterator().next().getValue());
            }
        }
        return Strings.emptyToNull(selector) == null ? element.getSimpleName().toString() : selector;
    }

    private org.jdom2.Element verifySelector(String selector, Element element, Document document) {
        XPathExpression<org.jdom2.Element> expression = xpath.compile("//*[@id='" + selector + "']", Filters.element());
        List<org.jdom2.Element> elements = expression.evaluate(document);
        if (elements.isEmpty()) {
            error(element,
                    "Cannot find a matching element in the MBUI XML with id \"%s\".", selector);
        } else if (elements.size() > 1) {
            error(element,
                    "Found %d matching elements in the MBUI XML with id \"%s\". Id must be unique.",
                    elements.size(), selector);
        }
        return elements.get(0);
    }

    private ElementType getMbuiElementType(TypeMirror dataElementType) {
        if (isAssignable(dataElementType, VerticalNavigation.class)) {
            return VerticalNavigation;
        } else if (isAssignable(dataElementType, Form.class)) {
            return Form;
        } else if (isAssignable(dataElementType, DataTable.class)) {
            return DataTable;
        } else {
            return null;
        }
    }


    // ------------------------------------------------------ process navigation, tables and forms

    private void processVerticalNavigation(final VariableElement field, final org.jdom2.Element element,
            final String selector, final MbuiViewContext context) {
        VerticalNavigationInfo navigationInfo = new VerticalNavigationInfo(field.getSimpleName().toString(), selector);
        context.setVerticalNavigation(navigationInfo);

        XPathExpression<org.jdom2.Element> expression = xpath.compile("item", Filters.element());
        expression.evaluate(element).forEach(itemElement -> navigationInfo.addItem(createItem(field, itemElement, 0)));
    }

    private VerticalNavigationInfo.Item createItem(final VariableElement field, org.jdom2.Element element, int level) {
        String id = element.getAttributeValue("id");
        String title = element.getAttributeValue("title");
        String icon = element.getAttributeValue("icon");

        if (id == null) {
            error(field, "Invalid item \"%s\" in vertical-navigation: id is mandatory.", xmlAsString(element));
        }
        if (title == null) {
            error(field, "Invalid item \"%s\" in vertical-navigation: title is mandatory.", xmlAsString(element));
        }
        VerticalNavigationInfo.Item item = new VerticalNavigationInfo.Item(id, title, icon);

        List<org.jdom2.Element> subItems = element.getChildren("sub-item");
        if (!subItems.isEmpty()) {
            if (level > 0) {
                error(field, "Invalid nesting in vertical-navigation: sub items cannot have sub items.");
            }
            subItems.forEach(subItemElement -> item.addSubItem(createItem(field, subItemElement, level + 1)));

        } else {
            org.jdom2.Element contentElement = element;
            if (element.getChild(XmlTags.METADATA) != null) {
                contentElement = element.getChild(XmlTags.METADATA);
            }
            StringBuilder html = new StringBuilder();
            for (org.jdom2.Element childElement : contentElement.getChildren()) {
                if (XmlTags.TABLE.equals(childElement.getName()) || XmlTags.FORM.equals(childElement.getName())) {
                    if (html.length() != 0) {
                        item.addContent(new VerticalNavigationInfo.Html(html.toString()));
                        html.setLength(0);
                    }
                    item.addContent(new VerticalNavigationInfo.Reference(childElement.getAttributeValue("id")));

                } else {
                    // do not directly add the html, but collect it until a table or form is about to be processed
                    html.append(JAVA_STRING_ESCAPER.escape(xmlAsString(childElement)));
                }
            }
        }
        return item;
    }

    private void processDataTables(final VariableElement field, final org.jdom2.Element element, final String selector,
            final MbuiViewContext context) {
        MetadataInfo metadata = findMetadata(field, element, context);
        DataTableInfo tableInfo = new DataTableInfo(field.getSimpleName().toString(), selector, getTypeParameter(field),
                metadata);
        context.addDataTableInfo(tableInfo);

        org.jdom2.Element columnsContainer = element.getChild("columns");
        if (columnsContainer != null) {
            for (org.jdom2.Element columnElement : columnsContainer.getChildren("column")) {
                String name = columnElement.getAttributeValue("name");
                String title = columnElement.getAttributeValue("title");
                String value = columnElement.getAttributeValue("value");

                if (name == null) {
                    error(field, "Invalid column \"%s\" in data-table#%s: name is mandatory.",
                            xmlAsString(columnElement), selector);
                }
                if (value != null) {
                    if (!Handlebars.isExpression(value)) {
                        error(field,
                                "Invalid column \"%s\" in data-table#%s: value has to be an expression.",
                                xmlAsString(columnElement), selector);
                    }
                    if (title == null) {
                        error(field,
                                "Invalid column \"%s\" in data-table#%s: if value is given, title is mandatory.",
                                xmlAsString(columnElement), selector);
                    }
                }
                DataTableInfo.Column column = new DataTableInfo.Column(name, title, value);
                tableInfo.addColumn(column);
            }
        }
    }

    private void processForms(final VariableElement field, final org.jdom2.Element element, final String selector,
            final MbuiViewContext context) {
        MetadataInfo metadata = findMetadata(field, element, context);
        FormInfo formInfo = new FormInfo(field.getSimpleName().toString(), selector, getTypeParameter(field), metadata);
        context.addFormInfo(formInfo);

        org.jdom2.Element attributesContainer = element.getChild("attributes");
        if (attributesContainer != null) {
            for (org.jdom2.Element attributeElement : attributesContainer.getChildren("attribute")) {
                String name = attributeElement.getAttributeValue("name");
                if (name == null) {
                    error(field, "Invalid attribute \"%s\" in form#%s: name is mandatory.",
                            xmlAsString(attributeElement), selector);
                }

                FormInfo.Attribute attribute = new FormInfo.Attribute(name);
                org.jdom2.Element suggestHandler = attributeElement.getChild("suggest-handler");
                if (suggestHandler != null) {
                    org.jdom2.Element templatesContainer = suggestHandler.getChild("templates");
                    if (templatesContainer != null) {
                        for (org.jdom2.Element templateElement : templatesContainer.getChildren("template")) {
                            String address = templateElement.getAttributeValue("address");
                            attribute.addSuggestHandlerTemplate(address);
                        }
                    }
                }
                formInfo.addAttribute(attribute);
            }
        }
    }

    private MetadataInfo findMetadata(final VariableElement field, final org.jdom2.Element element,
            final MbuiViewContext context) {
        MetadataInfo metadataInfo = null;
        XPathExpression<org.jdom2.Element> expression = xpath.compile("ancestor::metadata", Filters.element());
        org.jdom2.Element metadataElement = expression.evaluateFirst(element);
        if (metadataElement == null) {
            error(field, "Missing metadata ancestor for %s#%s. Please make sure the there's a <%s/> ancestor element.",
                    element.getName(), element.getAttributeValue("id"), XmlTags.METADATA);
        } else {
            metadataInfo = context.getMetadataInfo(metadataElement.getAttributeValue("address"));
            if (metadataInfo == null) {
                error(field, "No metadata found for %s#%s. Please make sure there's a <%s/> ancestor element.",
                        element.getName(), element.getAttributeValue("id"), XmlTags.METADATA);
            }
        }
        return metadataInfo;
    }

    private String getTypeParameter(final VariableElement field) {
        String typeArgument = "org.jboss.hal.dmr.ModelNode";
        DeclaredType declaredType = MoreTypes.asDeclared(field.asType());
        List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
        if (!typeArguments.isEmpty()) {
            typeArgument = MoreTypes.asTypeElement(typeUtils, typeArguments.get(0)).getQualifiedName().toString();
        }
        return typeArgument;
    }

    private void processCrossReferences(final Document document, final MbuiViewContext context) {
        // table-form bindings
        XPathExpression<org.jdom2.Element> expression = xpath.compile("//table[@form-ref]", Filters.element());
        for (org.jdom2.Element element : expression.evaluate(document)) {
            DataTableInfo tableInfo = context.getElement(element.getAttributeValue("id"));
            FormInfo formInfo = context.getElement(element.getAttributeValue("form-ref"));
            if (tableInfo != null && formInfo != null) {
                tableInfo.setFormRef(formInfo);
            }
        }

        VerticalNavigationInfo navigation = context.getVerticalNavigation();
        if (navigation != null) {
            resolveItemReferences(navigation, "//item//table", document, context);
            resolveItemReferences(navigation, "//item//form", document, context);
        }
    }

    private void resolveItemReferences(final VerticalNavigationInfo navigation, final String xpath,
            final Document document, final MbuiViewContext context) {

        XPathExpression<org.jdom2.Element> expression = this.xpath.compile(xpath, Filters.element());
        for (org.jdom2.Element element : expression.evaluate(document)) {
            String id = element.getAttributeValue("id");
            MbuiElementInfo elementInfo = context.getElement(id);
            if (elementInfo != null) {
                // find parent (sub)item
                XPathExpression<org.jdom2.Element> parentItemExpression = this.xpath.compile("ancestor::sub-item",
                        Filters.element());
                org.jdom2.Element parentItemElement = parentItemExpression.evaluateFirst(element);
                if (parentItemElement == null) {
                    parentItemExpression = this.xpath.compile("ancestor::item", Filters.element());
                    parentItemElement = parentItemExpression.evaluateFirst(element);
                }
                VerticalNavigationInfo.Item parentItem = navigation.getItem(parentItemElement.getAttributeValue("id"));
                VerticalNavigationInfo.Reference reference = parentItem.findReference(id);
                if (reference != null) {
                    reference.setReference(elementInfo.getName());
                }
            }
        }
    }


    // ------------------------------------------------------ abstract properties

    void processAbstractProperties(final TypeElement type, final MbuiViewContext context) {
        ElementFilter.methodsIn(type.getEnclosedElements()).stream()
                .filter(method -> method.getModifiers().contains(Modifier.ABSTRACT))
                .forEach(method -> {

                    // verify method
                    if (method.getReturnType().getKind() == TypeKind.VOID) {
                        error(method, "Abstract propertiers in a @%s class must not return void",
                                MbuiView.class.getSimpleName());
                    }
                    if (!method.getParameters().isEmpty()) {
                        error(method, "Abstract properties in a @%s class must not have parameters",
                                MbuiView.class.getSimpleName());
                    }

                    String typeName = TypeSimplifier.simpleTypeName(method.getReturnType());
                    String methodName = method.getSimpleName().toString();
                    String fieldName = (isGetter(method)) ? nameWithoutPrefix(methodName) : methodName;
                    String modifier = getModifier(method);
                    context.addAbstractProperty(new AbstractPropertyInfo(typeName, fieldName, methodName, modifier));
                });
    }

    private boolean isGetter(ExecutableElement method) {
        String name = method.getSimpleName().toString();
        boolean get = name.startsWith("get") && !name.equals("get");
        boolean is = name.startsWith("is") && !name.equals("is")
                && method.getReturnType().getKind() == TypeKind.BOOLEAN;
        return get || is;
    }

    private String nameWithoutPrefix(String name) {
        String withoutPrefix;
        if (name.startsWith("get") && !name.equals("get")) {
            withoutPrefix = name.substring(3);
        } else {
            assert name.startsWith("is");
            withoutPrefix = name.substring(2);
        }
        return Introspector.decapitalize(withoutPrefix);
    }

    private String getModifier(final ExecutableElement method) {
        String modifier = null;
        Set<Modifier> modifiers = method.getModifiers();
        if (modifiers.contains(Modifier.PUBLIC)) {
            modifier = "public";
        } else if (modifiers.contains(Modifier.PROTECTED)) {
            modifier = "protected";
        }
        return modifier;
    }


    // ------------------------------------------------------ process @PostConstruct

    private void processPostConstruct(TypeElement type, final MbuiViewContext context) {
        ElementFilter.methodsIn(type.getEnclosedElements()).stream()
                .filter(method -> MoreElements.isAnnotationPresent(method, PostConstruct.class))
                .forEach(method -> {

                    // verify method
                    if (method.getModifiers().contains(Modifier.PRIVATE)) {
                        error(method, "@%s method must not be private", PostConstruct.class.getSimpleName());
                    }
                    if (method.getModifiers().contains(Modifier.STATIC)) {
                        error(method, "@%s method must not be static", PostConstruct.class.getSimpleName());
                    }
                    if (!method.getReturnType().equals(typeUtils.getNoType(TypeKind.VOID))) {
                        error(method, "@%s method must return void", PostConstruct.class.getSimpleName());
                    }
                    if (!method.getParameters().isEmpty()) {
                        error(method, "@%s method must not have parameters",
                                PostConstruct.class.getSimpleName());
                    }

                    context.addPostConstruct(new PostConstructInfo(method.getSimpleName().toString()));
                });

        if (context.getPostConstructs().size() > 1) {
            warning(type, "%d methods annotated with @%s found. Order is not guaranteed!",
                    context.getPostConstructs().size(), PostConstruct.class.getSimpleName());
        }
    }


    // ------------------------------------------------------ helper methods

    private String xmlAsString(org.jdom2.Element element) {
        String asString;
        StringWriter writer = new StringWriter();
        try {
            new XMLOutputter(Format.getCompactFormat()).output(element, writer);
            asString = writer.toString();
        } catch (IOException e) {
            asString = "<" + element + "/>";
        }
        return asString;
    }
}
