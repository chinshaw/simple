/**
 * 
 */
package com.simple.original.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IWelcomeView;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * @author chinshaw
 * 
 */
public class WelcomeView extends AbstractView implements IWelcomeView {

	public interface Binder extends UiBinder<Widget, WelcomeView> {
	}

	private Presenter presenter;

	/**
	 * @param resources
	 */
	public WelcomeView(Resources resources) {
		super(resources);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param eventBus
	 * @param resources
	 */
	@Inject
	public WelcomeView(EventBus eventBus, Resources resources) {
		super(eventBus, resources);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.desktop.ViewImpl#getErrorPanel()
	 */
	@Override
	protected ErrorPanel getErrorPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.desktop.ViewImpl#reset()
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@UiHandler("dashboards")
	void onDashboardsSelected(ClickEvent click) {
		presenter.onDashboardsSelected();
	}

	@UiHandler("dataproviders")
	void onDataProvidersSelected(ClickEvent click) {
		presenter.onDataProvidersSelected();
	}

	@UiHandler("operations")
	void onOperationsSelected(ClickEvent click) {
		presenter.onOperationsSelected();
	}

	@UiHandler("dashboards")
	void onExecuteTaskSelected(ClickEvent click) {
		presenter.onExecuteTaskSelected();
	}

	@UiHandler("hadoop")
	void onDocumentationSelected(ClickEvent click) {
		presenter.onDocumentationSelected();
	}

	@UiHandler("hadoop")
	void onHadoopSelected(ClickEvent click) {
		presenter.onHadoopSelected();
	}
}
