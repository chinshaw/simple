package com.simple.original.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.simple.api.orchestrator.IDataProviderInput;
import com.simple.original.client.proxy.DataProviderInputProxy;
import com.simple.original.client.view.widgets.EnumEditor;

public class DataProviderInputEditor extends Composite implements Editor<DataProviderInputProxy> {

	interface Binder extends UiBinder<Widget, DataProviderInputEditor>{}
	
	@UiField(provided = true)
	EnumEditor<IDataProviderInput.Type> type = new EnumEditor<IDataProviderInput.Type>(
			IDataProviderInput.Type.class);
	
	@UiField
	TextArea description;
	
	
	public DataProviderInputEditor(int index) {
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
	}
	
	public final HandlerRegistration addDeleteHandler(EditorDeleteEvent.Handler handler) {
		return addHandler(handler, EditorDeleteEvent.TYPE);
	}
	
	@UiHandler("delete")
	void onDeleteClicked(ClickEvent click) {
		fireEvent(new EditorDeleteEvent(this));
	}

}
