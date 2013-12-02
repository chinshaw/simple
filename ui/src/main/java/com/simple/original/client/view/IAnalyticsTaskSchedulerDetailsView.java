package com.simple.original.client.view;

import com.google.gwt.editor.client.Editor;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.simple.original.client.proxy.QuartzTriggerProxy;

public interface IAnalyticsTaskSchedulerDetailsView extends IAnalyticsTaskSchedulerView, Editor<QuartzTriggerProxy> {

    RequestFactoryEditorDriver<QuartzTriggerProxy, ?> getEditorDriver();

}
