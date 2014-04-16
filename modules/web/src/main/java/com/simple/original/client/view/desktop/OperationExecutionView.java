/**
 * 
 */
package com.simple.original.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IOperationExecutionView;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * @author chris
 *
 */
public class OperationExecutionView extends AbstractView implements IOperationExecutionView {

	interface Binder extends UiBinder<Widget, OperationExecutionView>{}
	
	
	private Presenter presenter;
	
	
    /**
     * @param eventBus
     * @param resources
     */
    @Inject
    public OperationExecutionView(EventBus eventBus, Resources resources) {
        super(eventBus, resources);
        initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
    }
	
	/* (non-Javadoc)
	 * @see com.simple.original.client.view.IOperationExecutionView#setPresenter(com.simple.original.client.view.IOperationExecutionView.Presenter)
	 */
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/* (non-Javadoc)
	 * @see com.simple.original.client.view.desktop.AbstractView#getErrorPanel()
	 */
	@Override
	protected ErrorPanel getErrorPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see com.simple.original.client.view.desktop.AbstractView#reset()
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addDataProvider(DataProvider dataProvider) {
		
	}

	@Override
	public void addOperationInput(AnalyticsOperationInput input) {
		
	}

	
	@UiHandler("execute")
	void onExecute(ClickEvent click) {
		presenter.onExecute();
	}
}
