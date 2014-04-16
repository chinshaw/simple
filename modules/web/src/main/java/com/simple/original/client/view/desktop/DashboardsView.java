/**
 * 
 */
package com.simple.original.client.view.desktop;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.proxy.DashboardProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IDashboardsView;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * @author chinshaw
 * 
 */
public class DashboardsView extends AbstractView implements IDashboardsView {

	interface Binder extends UiBinder<Widget, DashboardsView>{}

	
	static class DashboardCell extends AbstractCell<DashboardProxy> {
		
		// Not used
		interface Templates extends SafeHtmlTemplates {
			@SafeHtmlTemplates.Template("<div class=\"ext-dashboard-cell\"> class=\"ext- style=\"height:40;width:40\">{0}</div>")
			SafeHtml cell(SafeHtml value);
		}
		
		private Resources resources;
		
		public DashboardCell(Resources resources) {
			this.resources = resources;
		}


		/**
		 * Create a singleton instance of the templates used to render the cell.
		 */
		
		//static Templates templates = GWT.create(Templates.class);

		@Override
		public void render(Context context, DashboardProxy value, SafeHtmlBuilder sb) {
			/*
			 * Always do a null check on the value. Cell widgets can pass null
			 * to cells if the underlying data contains a null, or if the data
			 * arrives out of order.
			 */
			if (value == null) {
				return;
			}

			SafeHtml name = SafeHtmlUtils.fromString(value.getName());
			// Use the template to create the Cell's html.
			//SafeHtml rendered = templates.cell(name);
			SafeHtml html = new SafeHtmlBuilder().appendHtmlConstant(
					"<div class=\"" + resources.style().iconCell() + "\"><div class=\"graphic\">D</div><div class=\"text\">").append(name).appendHtmlConstant(
		            "</div>").toSafeHtml();
			sb.append(html);
		}
	}
	
	private Presenter presenter;
	
	
	
	@UiField(provided = true)
	CellList<DashboardProxy> dashboardsList;

	
	
	/**
	 * @param eventBus
	 * @param resources
	 */
	@Inject
	public DashboardsView(EventBus eventBus, Resources resources) {
		super(eventBus, resources);
		dashboardsList = new CellList<DashboardProxy>(new DashboardCell(resources));
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.client.view.IDashboardsView#setPresenter(com.simple
	 * .original.client.view.IDashboardsView.Presenter)
	 */
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.client.view.IDashboardsView#getDashboardDataProvider
	 * ()
	 */
	@Override
	public HasData<DashboardProxy> getDashboardDataProvider() {
		return dashboardsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.desktop.AbstractView#getErrorPanel()
	 */
	@Override
	protected ErrorPanel getErrorPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.desktop.AbstractView#reset()
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}
	

	/**
	 * Click handler for the new dashboard button.
	 * @param click
	 */
	@UiHandler("newDashboard")
	void onNewDashboard(ClickEvent click) {
		presenter.onNewDashboard();
	}
	
}
