$wnd.hal.runAsyncCallback15("defineClass(985, 1, {1:1});\n_.com$gwtplatform$mvp$client$HandlerContainerImpl_automaticBind_methodInjection__________ = function com$gwtplatform$mvp$client$HandlerContainerImpl_automaticBind_methodInjection__________(invokee, _0){\n  invokee.automaticBind(_0);\n}\n;\nfunction $clinit_AddressTemplates_2(){\n  $clinit_AddressTemplates_2 = emptyMethod;\n  DEPLOYMENTSCANNER_ADDRESS = '/{selected.profile}/subsystem=deployment-scanner' + '/scanner=*';\n  DEPLOYMENTSCANNER_SUBSYSTEM_TEMPLATE = of_14('/{selected.profile}/subsystem=deployment-scanner');\n  DEPLOYMENTSCANNER_TEMPLATE = of_14('/{selected.profile}/subsystem=deployment-scanner/scanner=*');\n}\n\nvar DEPLOYMENTSCANNER_ADDRESS, DEPLOYMENTSCANNER_SUBSYSTEM_TEMPLATE, DEPLOYMENTSCANNER_TEMPLATE;\nfunction $clinit_DeploymentScannerPresenter(){\n  $clinit_DeploymentScannerPresenter = emptyMethod;\n  $clinit_MbuiPresenter();\n}\n\nfunction DeploymentScannerPresenter(eventBus, view, proxy, finder, finderPathFactory, environment, statementContext, dispatcher){\n  $clinit_DeploymentScannerPresenter();\n  MbuiPresenter.call(this, eventBus, view, proxy, finder);\n  this.$init_920();\n  this.finderPathFactory = finderPathFactory;\n  this.environment = environment;\n  this.statementContext = statementContext;\n  this.dispatcher = dispatcher;\n}\n\ndefineClass(413, 106, {58:1, 49:1, 1:1, 33:1, 30:1, 413:1, 106:1, 47:1, 101:1}, DeploymentScannerPresenter);\n_.$init_920 = function $init_920(){\n}\n;\n_.lambda$0_55 = function lambda$0_102(result_0){\n  $clinit_DeploymentScannerPresenter();\n  {\n    castTo(this.getView(), 749).updateScanners(asNamedNodes(failSafePropertyList(result_0, ($clinit_AddressTemplates_2() , DEPLOYMENTSCANNER_TEMPLATE).lastKey())));\n  }\n}\n;\n_.finderPath = function finderPath_4(){\n  return this.finderPathFactory.subsystemPath('deployment-scanner');\n}\n;\n_.onBind = function onBind_7(){\n  getClassPrototype(89).onBind.call(this);\n  castTo(this.getView(), 749).setPresenter(this);\n}\n;\n_.reload_0 = function reload_3(){\n  var operation;\n  operation = (new Operation$Builder('read-resource', ($clinit_AddressTemplates_2() , DEPLOYMENTSCANNER_SUBSYSTEM_TEMPLATE).resolve_3(this.statementContext, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), {4:1, 1:1, 5:1, 6:1}, 2, 6, [])))).param('recursive-depth', 2).build_13();\n  this.dispatcher.execute_12(operation, new DeploymentScannerPresenter$lambda$0$Type(this));\n}\n;\nvar Lorg_jboss_hal_client_configuration_subsystem_deploymentscanner_DeploymentScannerPresenter_2_classLit = createForClass('org.jboss.hal.client.configuration.subsystem.deploymentscanner', 'DeploymentScannerPresenter', 413, Lorg_jboss_hal_core_mbui_MbuiPresenter_2_classLit);\nfunction $clinit_DeploymentScannerPresenter$MyView(){\n  $clinit_DeploymentScannerPresenter$MyView = emptyMethod;\n}\n\nvar Lorg_jboss_hal_client_configuration_subsystem_deploymentscanner_DeploymentScannerPresenter$MyView_2_classLit = createForInterface('org.jboss.hal.client.configuration.subsystem.deploymentscanner', 'DeploymentScannerPresenter/MyView');\nfunction $clinit_DeploymentScannerPresenter$lambda$0$Type(){\n  $clinit_DeploymentScannerPresenter$lambda$0$Type = emptyMethod;\n}\n\nfunction DeploymentScannerPresenter$lambda$0$Type($$outer_0){\n  $clinit_DeploymentScannerPresenter$lambda$0$Type();\n  this.$$outer_0 = $$outer_0;\n}\n\ndefineClass(1190, 1, {1:1, 24:1}, DeploymentScannerPresenter$lambda$0$Type);\n_.onSuccess_0 = function onSuccess_87(arg0){\n  this.$$outer_0.lambda$0_55(arg0);\n}\n;\nvar Lorg_jboss_hal_client_configuration_subsystem_deploymentscanner_DeploymentScannerPresenter$lambda$0$Type_2_classLit = createForClass('org.jboss.hal.client.configuration.subsystem.deploymentscanner', 'DeploymentScannerPresenter/lambda$0$Type', 1190, Ljava_lang_Object_2_classLit);\nfunction $clinit_DeploymentScannerView(){\n  $clinit_DeploymentScannerView = emptyMethod;\n  $clinit_MbuiViewImpl();\n}\n\nfunction DeploymentScannerView(mbuiContext){\n  $clinit_DeploymentScannerView();\n  MbuiViewImpl.call(this, mbuiContext);\n  this.$init_923();\n}\n\nfunction create_20(mbuiContext){\n  $clinit_DeploymentScannerView();\n  return new Mbui_DeploymentScannerView(mbuiContext);\n}\n\ndefineClass(2734, 162, {1:1, 33:1, 30:1, 749:1, 59:1, 57:1});\n_.$init_923 = function $init_923(){\n}\n;\n_.updateScanners = function updateScanners(items){\n  $refresh_1($add_0(this.deploymentscannerTable.api_0().clear(), items), ($clinit_Api$RefreshMode() , RESET_0));\n  this.deploymentscannerForm.clear_0();\n}\n;\nvar Lorg_jboss_hal_client_configuration_subsystem_deploymentscanner_DeploymentScannerView_2_classLit = createForClass('org.jboss.hal.client.configuration.subsystem.deploymentscanner', 'DeploymentScannerView', 2734, Lorg_jboss_hal_core_mbui_MbuiViewImpl_2_classLit);\nfunction $clinit_Mbui_DeploymentScannerView(){\n  $clinit_Mbui_DeploymentScannerView = emptyMethod;\n  $clinit_DeploymentScannerView();\n}\n\nfunction Mbui_DeploymentScannerView(mbuiContext){\n  $clinit_Mbui_DeploymentScannerView();\n  var deploymentscannerTableOptions, layoutBuilder, metadata7Template, root;\n  DeploymentScannerView.call(this, mbuiContext);\n  this.$init_924();\n  metadata7Template = of_14('/{selected.profile}/subsystem=deployment-scanner/scanner=*');\n  this.metadata7 = castTo(mbuiContext.metadataRegistry_1().lookup_1(metadata7Template), 27);\n  this.handlebarElements = new HashMap;\n  this.deploymentscannerForm = (new ModelNodeForm$Builder('deployment-scanner-form', this.metadata7)).onSave_0(new Mbui_DeploymentScannerView$lambda$0$Type(this, metadata7Template, mbuiContext)).build_12();\n  deploymentscannerTableOptions = castTo(castTo(castTo((new ModelNodeTable$Builder(this.metadata7)).button_5(mbuiContext.tableButtonFactory_0().add_30(build_17('deployment-scanner-table', stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), {4:1, 1:1, 5:1, 6:1}, 2, 6, ['add'])), 'Scanner', metadata7Template, new Mbui_DeploymentScannerView$lambda$1$Type(this))), 16).button_5(mbuiContext.tableButtonFactory_0().remove_16('Scanner', metadata7Template, new Mbui_DeploymentScannerView$lambda$2$Type, new Mbui_DeploymentScannerView$lambda$3$Type(this))), 16).column_8('name', makeLambdaFunction(Mbui_DeploymentScannerView$lambda$4$Type.prototype.render_1, Mbui_DeploymentScannerView$lambda$4$Type, [])), 16).build_7();\n  this.deploymentscannerTable = new ModelNodeTable('deployment-scanner-table', deploymentscannerTableOptions);\n  this.registerAttachable(this.deploymentscannerTable, stampJavaTypeInfo(getClassLiteralForArray(Lorg_jboss_hal_ballroom_Attachable_2_classLit, 1), {4:1, 1:1, 5:1}, 12, 0, []));\n  this.registerAttachable(this.deploymentscannerForm, stampJavaTypeInfo(getClassLiteralForArray(Lorg_jboss_hal_ballroom_Attachable_2_classLit, 1), {4:1, 1:1, 5:1}, 12, 0, []));\n  layoutBuilder = castTo(castTo(castTo(castTo(castTo(castTo(castTo(castTo((new LayoutBuilder).row_0().column_2().div_0(), 9).innerHtml(fromSafeConstant('<h1>Scanners<\\/h1><p>{{metadata7.getDescription().getDescription()}}<\\/p>')), 9).rememberAs('html19'), 9).end(), 9).add_12(this.deploymentscannerTable), 9).add_12(this.deploymentscannerForm), 9).end(), 9).end(), 9);\n  this.handlebarElements.put_0('html19', layoutBuilder.referenceFor('html19'));\n  root = layoutBuilder.build_2();\n  this.initElement(root);\n}\n\nfunction lambda$2_44(api_0){\n  $clinit_Mbui_DeploymentScannerView();\n  return castTo($selectedRow(api_0), 22).getName();\n}\n\nfunction lambda$4_24(cell_0, type_1, row_2, meta_3){\n  $clinit_Mbui_DeploymentScannerView();\n  return row_2.getName();\n}\n\ndefineClass(2829, 2734, {1:1, 33:1, 30:1, 749:1, 59:1, 57:1}, Mbui_DeploymentScannerView);\n_.$init_924 = function $init_924(){\n}\n;\n_.lambda$0_56 = function lambda$0_103(metadata7Template_1, mbuiContext_2, form_2, changedValues_3){\n  $clinit_Mbui_DeploymentScannerView();\n  var name_0;\n  {\n    name_0 = castTo(form_2.getModel(), 22).getName();\n    this.saveForm(changedValues_3, metadata7Template_1.resolve_3(mbuiContext_2.statementContext_4(), stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), {4:1, 1:1, 5:1, 6:1}, 2, 6, [name_0])), 'Deployment Scanner', name_0);\n  }\n}\n;\n_.lambda$1_30 = function lambda$1_56(){\n  $clinit_Mbui_DeploymentScannerView();\n  castTo(this.presenter, 413).reload_0();\n}\n;\n_.lambda$3_15 = function lambda$3_32(){\n  $clinit_Mbui_DeploymentScannerView();\n  castTo(this.presenter, 413).reload_0();\n}\n;\n_.attach = function attach_14(){\n  getClassPrototype(73).attach.call(this);\n  $bindForm(this.deploymentscannerTable.api_0(), this.deploymentscannerForm);\n  replaceHandlebar(castToJso(this.handlebarElements.get_2('html19')), '{{metadata7.getDescription().getDescription()}}', valueOf_29(this.metadata7.getDescription_0().getDescription()));\n}\n;\nvar Lorg_jboss_hal_client_configuration_subsystem_deploymentscanner_Mbui_1DeploymentScannerView_2_classLit = createForClass('org.jboss.hal.client.configuration.subsystem.deploymentscanner', 'Mbui_DeploymentScannerView', 2829, Lorg_jboss_hal_client_configuration_subsystem_deploymentscanner_DeploymentScannerView_2_classLit);\nfunction $clinit_Mbui_DeploymentScannerView$lambda$0$Type(){\n  $clinit_Mbui_DeploymentScannerView$lambda$0$Type = emptyMethod;\n}\n\nfunction Mbui_DeploymentScannerView$lambda$0$Type($$outer_0, metadata7Template_1, mbuiContext_2){\n  $clinit_Mbui_DeploymentScannerView$lambda$0$Type();\n  this.$$outer_0 = $$outer_0;\n  this.metadata7Template_1 = metadata7Template_1;\n  this.mbuiContext_2 = mbuiContext_2;\n}\n\ndefineClass(2830, 1, {1:1}, Mbui_DeploymentScannerView$lambda$0$Type);\n_.onSave = function onSave_11(arg0, arg1){\n  this.$$outer_0.lambda$0_56(this.metadata7Template_1, this.mbuiContext_2, arg0, arg1);\n}\n;\nvar Lorg_jboss_hal_client_configuration_subsystem_deploymentscanner_Mbui_1DeploymentScannerView$lambda$0$Type_2_classLit = createForClass('org.jboss.hal.client.configuration.subsystem.deploymentscanner', 'Mbui_DeploymentScannerView/lambda$0$Type', 2830, Ljava_lang_Object_2_classLit);\nfunction $clinit_Mbui_DeploymentScannerView$lambda$1$Type(){\n  $clinit_Mbui_DeploymentScannerView$lambda$1$Type = emptyMethod;\n}\n\nfunction Mbui_DeploymentScannerView$lambda$1$Type($$outer_0){\n  $clinit_Mbui_DeploymentScannerView$lambda$1$Type();\n  this.$$outer_0 = $$outer_0;\n}\n\ndefineClass(2831, 1, {1:1}, Mbui_DeploymentScannerView$lambda$1$Type);\n_.execute_0 = function execute_96(){\n  this.$$outer_0.lambda$1_30();\n}\n;\nvar Lorg_jboss_hal_client_configuration_subsystem_deploymentscanner_Mbui_1DeploymentScannerView$lambda$1$Type_2_classLit = createForClass('org.jboss.hal.client.configuration.subsystem.deploymentscanner', 'Mbui_DeploymentScannerView/lambda$1$Type', 2831, Ljava_lang_Object_2_classLit);\nfunction $clinit_Mbui_DeploymentScannerView$lambda$2$Type(){\n  $clinit_Mbui_DeploymentScannerView$lambda$2$Type = emptyMethod;\n}\n\nfunction Mbui_DeploymentScannerView$lambda$2$Type(){\n  $clinit_Mbui_DeploymentScannerView$lambda$2$Type();\n}\n\ndefineClass(2832, 1, {1:1}, Mbui_DeploymentScannerView$lambda$2$Type);\n_.apply_2 = function apply_89(arg0){\n  return lambda$2_44(arg0);\n}\n;\nvar Lorg_jboss_hal_client_configuration_subsystem_deploymentscanner_Mbui_1DeploymentScannerView$lambda$2$Type_2_classLit = createForClass('org.jboss.hal.client.configuration.subsystem.deploymentscanner', 'Mbui_DeploymentScannerView/lambda$2$Type', 2832, Ljava_lang_Object_2_classLit);\nfunction $clinit_Mbui_DeploymentScannerView$lambda$3$Type(){\n  $clinit_Mbui_DeploymentScannerView$lambda$3$Type = emptyMethod;\n}\n\nfunction Mbui_DeploymentScannerView$lambda$3$Type($$outer_0){\n  $clinit_Mbui_DeploymentScannerView$lambda$3$Type();\n  this.$$outer_0 = $$outer_0;\n}\n\ndefineClass(2833, 1, {1:1}, Mbui_DeploymentScannerView$lambda$3$Type);\n_.execute_0 = function execute_97(){\n  this.$$outer_0.lambda$3_15();\n}\n;\nvar Lorg_jboss_hal_client_configuration_subsystem_deploymentscanner_Mbui_1DeploymentScannerView$lambda$3$Type_2_classLit = createForClass('org.jboss.hal.client.configuration.subsystem.deploymentscanner', 'Mbui_DeploymentScannerView/lambda$3$Type', 2833, Ljava_lang_Object_2_classLit);\nfunction $clinit_Mbui_DeploymentScannerView$lambda$4$Type(){\n  $clinit_Mbui_DeploymentScannerView$lambda$4$Type = emptyMethod;\n}\n\nfunction Mbui_DeploymentScannerView$lambda$4$Type(){\n  $clinit_Mbui_DeploymentScannerView$lambda$4$Type();\n}\n\ndefineClass(4161, $wnd.Function, {1:1}, Mbui_DeploymentScannerView$lambda$4$Type);\n_.render_1 = function render_24(arg0, arg1, arg2, arg3){\n  return lambda$4_24(arg0, arg1, arg2, arg3);\n}\n;\nfunction $clinit_Mbui_DeploymentScannerView_Provider(){\n  $clinit_Mbui_DeploymentScannerView_Provider = emptyMethod;\n  $clinit_Object();\n}\n\nfunction Mbui_DeploymentScannerView_Provider(mbuiContext){\n  $clinit_Mbui_DeploymentScannerView_Provider();\n  Object_0.call(this);\n  this.$init_925();\n  this.mbuiContext = mbuiContext;\n}\n\ndefineClass(2733, 1, {1:1, 64:1}, Mbui_DeploymentScannerView_Provider);\n_.$init_925 = function $init_925(){\n}\n;\n_.get_8 = function get_123(){\n  return this.get_29();\n}\n;\n_.get_29 = function get_124(){\n  return create_20(this.mbuiContext);\n}\n;\nvar Lorg_jboss_hal_client_configuration_subsystem_deploymentscanner_Mbui_1DeploymentScannerView_1Provider_2_classLit = createForClass('org.jboss.hal.client.configuration.subsystem.deploymentscanner', 'Mbui_DeploymentScannerView_Provider', 2733, Ljava_lang_Object_2_classLit);\ndefineClass(1029, 1, {1:1});\n_.get_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$MyView$_annotation$$none$$ = function get_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$MyView$_annotation$$none$$(){\n  var result;\n  if (isNull_0(this.singleton_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$MyView$_annotation$$none$$)) {\n    result = this.get_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$Mbui_DeploymentScannerView_Provider$_annotation$$none$$().get_29();\n    this.singleton_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$MyView$_annotation$$none$$ = result;\n  }\n  return this.singleton_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$MyView$_annotation$$none$$;\n}\n;\n_.get_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$_annotation$$none$$ = function get_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$_annotation$$none$$(){\n  var result;\n  if (isNull_0(this.singleton_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$_annotation$$none$$)) {\n    result = this.org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter_org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter_methodInjection(this.injector.getFragment_com_google_web_bindery_event_shared().get_Key$type$com$google$web$bindery$event$shared$EventBus$_annotation$$none$$(), this.get_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$MyView$_annotation$$none$$(), this.get_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$MyProxy$_annotation$$none$$(), this.injector.getFragment_org_jboss_hal_core_finder().get_Key$type$org$jboss$hal$core$finder$Finder$_annotation$$none$$(), this.injector.getFragment_org_jboss_hal_core_finder().get_Key$type$org$jboss$hal$core$finder$FinderPathFactory$_annotation$$none$$(), this.injector.getFragment_org_jboss_hal_config().get_Key$type$org$jboss$hal$config$Environment$_annotation$$none$$(), this.injector.getFragment_org_jboss_hal_meta().get_Key$type$org$jboss$hal$meta$StatementContext$_annotation$$none$$(), this.injector.getFragment_org_jboss_hal_dmr_dispatch().get_Key$type$org$jboss$hal$dmr$dispatch$Dispatcher$_annotation$$none$$());\n    this.memberInject_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$_annotation$$none$$(result);\n    this.singleton_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$_annotation$$none$$ = result;\n  }\n  return this.singleton_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$_annotation$$none$$;\n}\n;\n_.get_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$Mbui_DeploymentScannerView_Provider$_annotation$$none$$ = function get_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$Mbui_DeploymentScannerView_Provider$_annotation$$none$$(){\n  var result;\n  result = this.org$jboss$hal$client$configuration$subsystem$deploymentscanner$Mbui_DeploymentScannerView_Provider_org$jboss$hal$client$configuration$subsystem$deploymentscanner$Mbui_DeploymentScannerView_Provider_methodInjection(this.injector.getFragment_org_jboss_hal_core_mbui().get_Key$type$org$jboss$hal$core$mbui$MbuiContext$_annotation$$none$$());\n  this.memberInject_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$Mbui_DeploymentScannerView_Provider$_annotation$$none$$(result);\n  return result;\n}\n;\n_.memberInject_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$_annotation$$none$$ = function memberInject_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$_annotation$$none$$(injectee){\n  this.injector.getFragment_com_gwtplatform_mvp_client().com$gwtplatform$mvp$client$HandlerContainerImpl_automaticBind_methodInjection__________(injectee, this.injector.getFragment_com_gwtplatform_mvp_client().get_Key$type$com$gwtplatform$mvp$client$AutobindDisable$_annotation$$none$$());\n}\n;\n_.memberInject_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$Mbui_DeploymentScannerView_Provider$_annotation$$none$$ = function memberInject_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$Mbui_DeploymentScannerView_Provider$_annotation$$none$$(injectee){\n}\n;\n_.org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter_org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter_methodInjection = function org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter_org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter_methodInjection(_0, _1, _2, _3, _4, _5, _6, _7){\n  return new DeploymentScannerPresenter(_0, _1, _2, _3, _4, _5, _6, _7);\n}\n;\n_.org$jboss$hal$client$configuration$subsystem$deploymentscanner$Mbui_DeploymentScannerView_Provider_org$jboss$hal$client$configuration$subsystem$deploymentscanner$Mbui_DeploymentScannerView_Provider_methodInjection = function org$jboss$hal$client$configuration$subsystem$deploymentscanner$Mbui_DeploymentScannerView_Provider_org$jboss$hal$client$configuration$subsystem$deploymentscanner$Mbui_DeploymentScannerView_Provider_methodInjection(_0){\n  return new Mbui_DeploymentScannerView_Provider(_0);\n}\n;\ndefineClass(1031, 1, {60:1, 1:1});\n_.onSuccess_1 = function onSuccess_88(){\n  this.val$callback2.onSuccess_0(this.this$11.this$01.get_Key$type$org$jboss$hal$client$configuration$subsystem$deploymentscanner$DeploymentScannerPresenter$_annotation$$none$$());\n}\n;\n$entry(onLoad)(15);\n\n//# sourceURL=hal-15.js\n")