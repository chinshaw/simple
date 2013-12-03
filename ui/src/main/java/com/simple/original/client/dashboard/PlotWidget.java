package com.simple.original.client.dashboard;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.events.WidgetContextEvent;
import com.simple.original.client.dashboard.events.WidgetModelChangedEvent;
import com.simple.original.client.dashboard.model.IPlotWidgetModel;
import com.simple.original.client.dashboard.model.IWidgetModel;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.widgets.LinkableWidget;
import com.simple.original.client.view.widgets.ScalableImage;

public class PlotWidget extends AbstractDashboardWidget<IPlotWidgetModel> implements IDashboardWidget<IPlotWidgetModel>, LinkableWidget, WidgetModelChangedEvent.Handler,
		ContextMenuHandler {

	public static final String EMPTY_PLOT_DESCRIPTION = "No metric was selected for this plot";

	/**
	 * This is the container that wraps the entire widget.
	 */
	public FlowPanel container = new FlowPanel();
	/**
	 * This is the violation info widget that will show when there are
	 * violations attaced to this Plot.
	 */
	private ViolationInfo violationInfo = new ViolationInfo();

	/**
	 * This is the holder for the chart image that will be shown from the
	 * metric.
	 */
	private Image chartImage = new Image();

	private Image fullscreen = new Image(resources.fullScreen());

	@Inject
	public PlotWidget(final EventBus eventBus, final Resources resources) {
		super(eventBus, resources);
		initWidget(container);

		addDomHandler(this, ContextMenuEvent.getType());

		fullscreen.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showFullScreen();

			}
		});

		fullscreen.getElement().getStyle().setFloat(Style.Float.RIGHT);
		container.setStyleName(resources.style().staticPlotWidget());
		addDomHandler(widgetSelectedHandler, ClickEvent.getType());
	}

	@Override
	public String getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setChartUrl(String url) {
		chartImage.setUrl(url);
	}

	public String getChartUrl() {
		return chartImage.getUrl();
	}

	@Override
	public Widget asWidget() {
		return this;
	}
	
	@Override
	public void setModel(IPlotWidgetModel model) {
		this.model = model;
		container.clear();
	}

	@Override
	public void onLoad() {
		super.onLoad();
		handlers.add(eventBus.addHandler(WidgetModelChangedEvent.TYPE, this));
	}

	@Override
	public void onUnload() {
		super.onUnload();
		handlers.cleanup();
	}

	private void noChartSelected() {
		HTML placeHolder = new HTML("<div style=\"min-height:200px;text-align:center;\"><h1 style=\"padding-top:90px; font-size:200%;\">" + EMPTY_PLOT_DESCRIPTION + "</h1></div>");
		container.add(placeHolder);
	}

	private void showFullScreen() {
		PopupPanel pp = new PopupPanel(true);
		pp.setGlassEnabled(true);
		pp.setAnimationEnabled(true);

		Image popupImage = new Image();
		popupImage.setUrl(getChartUrl());
		ScalableImage scaleableImage = new ScalableImage(popupImage);

		pp.setWidget(scaleableImage);
		pp.center();
	}

	@Override
	public void onContextMenu(ContextMenuEvent event) {
		event.preventDefault();
		event.stopPropagation();

		showMenu(event.getNativeEvent());
	}

	private void showMenu(NativeEvent event) {
		eventBus.fireEvent(new WidgetContextEvent(this, event.getClientX(), event.getClientY()));
	}

	@Override
	public void onWidgetModelChanged(WidgetModelChangedEvent event) {
		IWidgetModel eventModel = event.getWidgetModel();
	}

	@Override
	public ImageResource getSelectorIcon() {
		return resources.barChartIcon();
	}
}
