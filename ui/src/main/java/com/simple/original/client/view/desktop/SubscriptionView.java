package com.simple.original.client.view.desktop;

import java.util.logging.Logger;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.proxy.SubscriptionProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.ISubscriptionView;
import com.simple.original.client.view.widgets.ErrorPanel;

public class SubscriptionView extends AbstractView implements ISubscriptionView {

	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	@UiTemplate("SubscriptionView.ui.xml")
	public interface Binder extends UiBinder<Widget, SubscriptionView> {
	}

	/**
	 * Page size for the number of scripts showing on table.
	 */
	private final int PAGE_SIZE = 15;

	/**
	 * Text size to enable CellPreviewHandler.
	 */
	private final int TEXT_SIZE = 100;

	/**
	 * PagerResources
	 */
	SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
	/**
	 * FastforwardRows size to the simplepager constructor
	 */
	private static int DEFAULT_FAST_FORWARD_ROWS = 1000;
	/**
	 * Save Subscriptions button.
	 */
	@UiField
	Button saveSubscriptions;

	/**
	 * Cancel Subscriptions button.
	 */
	@UiField
	Button cancelSubscriptions;

	/**
	 * Error panel for displaying errors.
	 */
	@UiField
	ErrorPanel errorPanel;

	/**
	 * Table containing the list of reports/alerts based on current place.
	 */
	@UiField
	CellTable<SubscriptionProxy> subscriptionsTable;

	/**
	 * SimplePager for paging support on report/alert subscription table.
	 */
	@UiField
	SimplePager subscriptionPager;

	/**
	 * Content Panel.
	 */
	@UiField
	FlowPanel contentPanel;

	/**
	 * Presenter.
	 */
	private Presenter presenter;

	@Inject
	public SubscriptionView(EventBus eventBus, Resources resources) {
		super(resources);
		subscriptionPager = new SimplePager();
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		setupSimplePager();
		descriptionPreviewHandler();
	}

	/**
	 * Mouse over event Listener for description field
	 */
	private void descriptionPreviewHandler() {

		subscriptionsTable.addCellPreviewHandler(new Handler<SubscriptionProxy>() {
			PopupPanel popupPanel = new PopupPanel(true);
			TextArea textArea = new TextArea();

			@Override
			public void onCellPreview(CellPreviewEvent<SubscriptionProxy> event) {
				if (event.getColumn() == 1) {
					if ("mouseover".equals(event.getNativeEvent().getType())) {
						String message = subscriptionsTable.getVisibleItem(event.getIndex() - subscriptionsTable.getPageStart()).getDescription();
						if (message != null && message.length() > TEXT_SIZE) {
							textArea.setVisible(true);
							// textArea.setHeight("4em");
							textArea.setWidth("30em");
							textArea.setText(message);
							popupPanel.add(textArea);
							Widget source = (Widget) event.getSource();
							int x = source.getAbsoluteLeft() + event.getColumn() * 100;
							int y = source.getAbsoluteTop() + (event.getIndex() - subscriptionsTable.getPageStart() + 1) * 33;

							popupPanel.setPopupPosition(x, y);
							popupPanel.show();
						}
					}
					if ("mouseout".equals(event.getNativeEvent().getType())) {
						popupPanel.clear();
					}
				}
			}
		});

	}

	/**
	 * This method is called for displaying the pagination
	 */
	public void setupSimplePager() {
		subscriptionPager.setDisplay(subscriptionsTable);
	}

	@UiFactory
	SimplePager createSimplePager() {
		final SimplePager subscriptionPager = new SimplePager(TextLocation.LEFT, pagerResources, false, DEFAULT_FAST_FORWARD_ROWS, true);
		return subscriptionPager;
	}

	@UiFactory
	CellTable<SubscriptionProxy> createTable() {
		final CellTable<SubscriptionProxy> subscriptionsTable = new CellTable<SubscriptionProxy>(PAGE_SIZE, getResources());
		subscriptionsTable.setEmptyTableWidget(new Label("No Subscriptions found"));

		CheckboxCell cb = new CheckboxCell(true, true);

		// Name Column
		Column<SubscriptionProxy, String> nameColumn = new Column<SubscriptionProxy, String>(new TextCell()) {
			@Override
			public String getValue(SubscriptionProxy subscriptionProxy) {
				return subscriptionProxy.getName();
			}
		};
		nameColumn.setSortable(false);
		subscriptionsTable.addColumn(nameColumn, "Name");

		// Description Column
		Column<SubscriptionProxy, String> description = new Column<SubscriptionProxy, String>(new TextCell()) {
			@Override
			public String getValue(SubscriptionProxy subscriptionProxy) {
				if (subscriptionProxy.getDescription() != null && subscriptionProxy.getDescription().length() > TEXT_SIZE) {
					return subscriptionProxy.getDescription().substring(0, TEXT_SIZE);
				}
				return subscriptionProxy.getDescription();
			}
		};
		description.setSortable(true);
		subscriptionsTable.addColumn(description, "Description");

		// Subscribe Column
		Column<SubscriptionProxy, Boolean> selectorColumn = new Column<SubscriptionProxy, Boolean>(cb) {
			@Override
			public Boolean getValue(SubscriptionProxy subscriptionProxy) {
				return subscriptionProxy.isSubscribed();
			}
		};
		selectorColumn.setFieldUpdater(new FieldUpdater<SubscriptionProxy, Boolean>() {
			public void update(int index, SubscriptionProxy object, Boolean value) {
				Logger.getLogger("SubscriptionView").info("INDEX is " + index + "value=" + value);
				presenter.onCheckBoxClicked(object, value, index);
			}
		});
		subscriptionsTable.addColumn(selectorColumn, "Subscribe");
		subscriptionsTable.setColumnWidth(selectorColumn, "5em");
		return subscriptionsTable;
	}

	@UiHandler("saveSubscriptions")
	void onSaveReportSubscriptions(ClickEvent clickEvent) {
		errorPanel.clear();
		presenter.onSaveSubscriptions();
	}

	@UiHandler("cancelSubscriptions")
	void onCancelReportSubscriptions(ClickEvent clickEvent) {
		errorPanel.clear();
		presenter.onCancelSubscriptions();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public HasWidgets getContentPanel() {
		return contentPanel;
	}

	@Override
	public HasData<SubscriptionProxy> getSubscriptionsTable() {
		return subscriptionsTable;
	}

	@Override
	public Button getCancelSubscriptions() {
		return cancelSubscriptions;
	}

	@Override
	public Button getSaveSubscriptions() {
		return saveSubscriptions;
	}

	@Override
	public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler) {
		return subscriptionsTable.addColumnSortHandler(handler);
	}

	@Override
	public int getColumnIndex(Column<SubscriptionProxy, ?> column) {
		return 0;
	}

	@Override
	public ErrorPanel getErrorPanel() {
		return errorPanel;
	}

	@Override
	public void reset() {
		errorPanel.clear();
	}

}
