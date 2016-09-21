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
package org.jboss.hal.client.deployment;

import javax.inject.Inject;

import elemental.dom.Element;
import org.jboss.gwt.elemento.core.Elements;
import org.jboss.hal.ballroom.Tabs;
import org.jboss.hal.core.modelbrowser.ModelBrowser;
import org.jboss.hal.core.mvp.PatternFlyViewImpl;
import org.jboss.hal.dmr.model.ResourceAddress;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.resources.Resources;

/**
 * @author Harald Pehl
 */
public class DeploymentDetailView extends PatternFlyViewImpl implements DeploymentDetailPresenter.MyView {

    private final Tabs tabs;
    private final ModelBrowser modelBrowser;

    @Inject
    public DeploymentDetailView(ModelBrowser modelBrowser, Resources resources) {
        this.modelBrowser = modelBrowser;

        Element element = new Elements.Builder().p().textContent(Names.NYI).end().build();

        tabs = new Tabs();
        tabs.add(Ids.DEPLOYMENT_DETAIL_RESOURCES_TAB, Names.MANAGEMENT_MODEL, modelBrowser.asElements());
        tabs.add(Ids.DEPLOYMENT_DETAIL_CONTENT_TAB, resources.constants().content(), element);
        initElement(tabs);
    }

    @Override
    public void setRoot(final ResourceAddress root) {
        modelBrowser.setRoot(root, false);
    }
}