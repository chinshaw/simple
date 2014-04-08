package com.simple.original.client.dashboard;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.events.WidgetModelChangedEvent;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel;
import com.simple.original.client.dashboard.model.IWidgetModel;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.widgets.BubbleGauge;
import com.simple.original.client.view.widgets.GWTGauge;
import com.simple.original.client.view.widgets.Gauge;

public class GaugeWidget extends AbstractDashboardWidget<IGaugeWidgetModel> implements IGaugeWidget {

	private final FlowPanel container = new FlowPanel();

	private Object gaugeDataKey;

	private Gauge.Options options;

	final PopupPanel menuPanel = new PopupPanel();

	private final ViolationInfo violationInfo = new ViolationInfo();

	final Label titleLabel = new Label();

	final MenuBar popupMenuBar = new MenuBar(true);

	private BubbleGauge gauge;

	private boolean fastDraw = true;

	//@Inject
	public GaugeWidget(final EventBus eventBus, Resources resources, IGaugeWidgetModel model) {
		super(eventBus, resources);
		initWidget(container);
		this.model = model;
		container.setStyleName(resources.style().gaugeWidget());
		container.getElement().getStyle().setZIndex(251);
		titleLabel.setStyleName(resources.style().gaugeLabel());
		// container.addDomHandler(widgetSelectedHandler, ClickEvent.getType());
		container.add(titleLabel);

		options = new GWTGauge.Options();
		options.setHeight(110);
		options.setWidth(110);

		gauge = new BubbleGauge(options);
		container.add(gauge);
		container.add(violationInfo);
		addDomHandler(widgetSelectedHandler, ClickEvent.getType());
	}

	/**
	 * Adds a submenu to the popup menu on the gauge. This will allow you to
	 * drill down to a more specific metric. We wrap the command so that we can
	 * call local logic when a menu option is selected.
	 * 
	 * @param optionName
	 *            String name representation of the menu item.
	 * @param onSelected
	 *            Command to execute when this menu option is selected.
	 * 
	 *            public void addMenuOption(String optionName, Command
	 *            onSelected) { MenuItem menuItem = new MenuItem(optionName,
	 *            true, new MenuCommand(onSelected));
	 *            popupMenuBar.addItem(menuItem); }
	 */

	public void setGaugeDataKey(Object gaugeDataKey) {
		this.gaugeDataKey = gaugeDataKey;
	}

	public void setTitle(String title) {
		this.titleLabel.setText(title);
		super.setTitle(title);
	}

	public Object getGaugeDataKey() {
		return gaugeDataKey;
	}

	@Override
	public String getContext() {
		return "";
	}

	@Override
	public void onWidgetModelChanged(WidgetModelChangedEvent event) {
		IWidgetModel eventModel = event.getWidgetModel();
		if (eventModel == model) {
			setModel((IGaugeWidgetModel) eventModel);
		}
	}

	@Override
	@Inject
	public void setModel(IGaugeWidgetModel model) {
		super.setModel(model);
		/*
		 * // If the metric is null then we will set the value to 0. if
		 * (model.getMetric() != null && model.getMetric().getValue() != null) {
		 * value = model.getMetric().getValue();
		 * violationInfo.setViolations(model.getMetric().getViolations()); }
		 */
		titleLabel.setText(model.getTitle());
		gauge.setTitle(model.getDescription());
		options.setRanges(model.getRanges());
		gauge.setValue((double) 0);
		gauge.setOptions(options, false);
	}

	@Override
	public Number getValue() {
		return gauge.getValue();
	}

	public void setValue(Number value) {
		gauge.setValue(value, fastDraw);
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	protected boolean isFastDraw() {
		return fastDraw;
	}

	public void setFastDraw(boolean fastDraw) {
		this.fastDraw = fastDraw;
	}

	@Override
	public ImageResource getSelectorIcon() {
		return resources.gaugeWidgetSelector();
	}
}