package com.simple.original.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.domain.model.dataprovider.HttpDataProvider;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IDataProviderEditView;
import com.simple.original.client.view.widgets.ErrorPanel;

public class DataProviderEditView extends AbstractView implements IDataProviderEditView, Editor<HttpDataProvider> {

	
	interface EditorDriver extends SimpleBeanEditorDriver<HttpDataProvider, DataProviderEditView>{}
	
	interface Binder extends UiBinder<Widget, DataProviderEditView>{}
	

	@UiField
	TextBox url;
	
	
	private Presenter presenter;
	
	private EditorDriver editorDriver = GWT.create(EditorDriver.class);
	
	
	/**
	 * @param eventBus
	 * @param resources
	 */
	@Inject
	public DataProviderEditView(EventBus eventBus, Resources resources) {
		super(eventBus, resources);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		editorDriver.initialize(this);
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public SimpleBeanEditorDriver<HttpDataProvider, ?> getEditorDriver() {
		return editorDriver;
	}

	@Override
	protected ErrorPanel getErrorPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}
	
	@UiHandler("save")
	void onSave(ClickEvent click) {
		presenter.onSave();
	}

}
