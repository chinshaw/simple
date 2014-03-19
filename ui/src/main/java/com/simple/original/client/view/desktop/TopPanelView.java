package com.simple.original.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.place.shared.Place;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.place.AnalyticsOperationsPlace;
import com.simple.original.client.place.AnalyticsTaskExecPlace;
import com.simple.original.client.place.AnalyticsTaskSchedulerPlace;
import com.simple.original.client.place.AnalyticsTasksPlace;
import com.simple.original.client.place.DashboardsPlace;
import com.simple.original.client.place.DataProviderEditPlace;
import com.simple.original.client.place.PlaceController;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.ITopPanelView;
import com.simple.original.client.view.widgets.ErrorPanel;

public class TopPanelView extends AbstractView implements ITopPanelView {

	class MenuPlaceItemCommand implements ScheduledCommand {

		private Place place;
		public MenuPlaceItemCommand(Place place) {
			this.place = place;
		}
		
		@Override
		public void execute() {
			placeController.goTo(place);
		}
	}

	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	@UiTemplate("TopPanelView.ui.xml")
	public interface Binder extends UiBinder<Widget, TopPanelView> {
	}
	
	@UiField
	MenuItem dashboards;
	
	@UiField
	MenuItem dataProviders;

	@UiField
	MenuItem favorites;

	@UiField
	MenuItem operations;

	@UiField
	MenuItem tasks;

	@UiField
	MenuItem scheduling;
	
	@UiField 
	MenuItem runTask;
	
	@UiField
	DivElement username;
	
	@Inject
	PlaceController placeController;
	
	

	@Inject
	public TopPanelView(EventBus eventBus, Resources resources) {
		super(eventBus, resources);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		addStyleName(resources.style().topPanel());
		
		dataProviders.setScheduledCommand(new MenuPlaceItemCommand(new DataProviderEditPlace()));
		dashboards.setScheduledCommand(new MenuPlaceItemCommand(new DashboardsPlace()));
		favorites.setScheduledCommand(new MenuPlaceItemCommand(null));
		operations.setScheduledCommand(new MenuPlaceItemCommand(new AnalyticsOperationsPlace()));
		tasks.setScheduledCommand(new MenuPlaceItemCommand(new AnalyticsTasksPlace()));
		scheduling.setScheduledCommand(new MenuPlaceItemCommand( new AnalyticsTaskSchedulerPlace()));
		runTask.setScheduledCommand(new MenuPlaceItemCommand(new AnalyticsTaskExecPlace()));
		
	}

	@Override
	protected ErrorPanel getErrorPanel() {
		return null;
	}

	@Override
	public void reset() {
	}

	public void setPresenter(Presenter presenter) {
	}

	public void setFullName(String name) {
		username.setInnerText(name);
	}
}
