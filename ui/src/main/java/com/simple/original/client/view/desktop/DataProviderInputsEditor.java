package com.simple.original.client.view.desktop;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.client.HasRequestContext;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.simple.original.api.analytics.IDataProviderInput;
import com.simple.original.client.proxy.DataProviderInputProxy;
import com.simple.original.client.view.widgets.EnumEditor;

public class DataProviderInputsEditor extends Composite implements
		IsEditor<ListEditor<DataProviderInputProxy, DataProviderInputEditor>> {

	private static final Logger logger = Logger
			.getLogger(DataProviderInputsEditor.class.getName());

	interface Binder extends UiBinder<Widget, DataProviderInputsEditor> {
	}

	private class DPEditorSource extends EditorSource<DataProviderInputEditor> {

		@Override
		public DataProviderInputEditor create(final int index) {
			final DataProviderInputEditor subEditor = new DataProviderInputEditor(
					index);
			dataProviderInputEditors.add(subEditor.asWidget());
			subEditor.addDeleteHandler(new EditorDeleteEvent.Handler() {

				@Override
				public void onEditorDeleted(EditorDeleteEvent event) {
					remove(index);
				}
			});
			return subEditor;
		}

		@Override
		public void dispose(DataProviderInputEditor subEditor) {
			subEditor.removeFromParent();
		}

		@Override
		public void setIndex(DataProviderInputEditor subEditor, int index) {
			dataProviderInputEditors.insert(subEditor, index);
		}
	}

	@UiField
	FlowPanel dataProviderInputEditors;

	@UiField(provided = true)
	EnumEditor<IDataProviderInput.Type> type = new EnumEditor<IDataProviderInput.Type>(
			IDataProviderInput.Type.class);

	private final ListEditor<DataProviderInputProxy, DataProviderInputEditor> editor;

	private RequestContext context;

	public DataProviderInputsEditor() {
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		editor = ListEditor.of(new DPEditorSource());
	}

	@Override
	public ListEditor<DataProviderInputProxy, DataProviderInputEditor> asEditor() {
		return editor;
	}

	private void remove(final int index) {
		editor.getList().remove(index);
	}

	@UiHandler("add")
	void onAddDataProviderInput(ClickEvent click) {
		add();
	}

	private void add() {
		DataProviderInputProxy proxy = context
				.create(DataProviderInputProxy.class);
		proxy.setType(type.getValue());
		editor.getList().add(proxy);
	}

	public void setRequestContext(RequestContext context) {
		this.context = context;
	}
}