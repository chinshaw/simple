package com.simple.original.client.view.desktop;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.proxy.MonitorAlertProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IFactoryAlertsView;
import com.simple.original.client.view.widgets.ErrorPanel;

public class FactoryAlertsView extends AbstractView implements IFactoryAlertsView {

	/**
	 * Page size for the number of alerts showing on table.
	 */
	private final int PAGE_SIZE = 15;

	/**
	 * Text size to enable CellPreviewHandler.
	 */
	private final int TEXT_SIZE = 50;

	@UiTemplate("FactoryAlertsView.ui.xml")
	public interface Binder extends UiBinder<Widget, FactoryAlertsView> {
	}

	/**
	 * PagerResources
	 */
	SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
	/**
	 * FastforwardRows size to the simplepager constructor
	 */
	private static int DEFAULT_FAST_FORWARD_ROWS = 1000;

	/**
	 * List box for displaying available search options.
	 */
	@UiField
	ListBox searchListBox;

	/**
	 * Text box for displaying the list of alerts based on search.
	 */
	@UiField
	TextBox searchQueryInput;

	/**
	 * Search Button.
	 */
	@UiField
	Button searcButon;

	/**
	 * Error panel for displaying errors.
	 */
	@UiField
	ErrorPanel errorPanel;

	/**
	 * Table containing the list of Factory alerts.
	 */
	@UiField
	CellTable<MonitorAlertProxy> factoryAlertTable;

	/**
	 * Pagination for displaying the list of factory alerts.
	 */
	@UiField
	SimplePager pager;

	/**
	 * Presenter is an interface for AlertDefintionView
	 */
	private Presenter presenter;

	@Inject
	public FactoryAlertsView(EventBus eventBus, Resources resources) {
		super(resources);
		pager = new SimplePager();
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		setupSimplePager();
		// Adding the data in SearchList Box.
		searchListBox.addItem("Alert Name");
		searchListBox.addItem("Alert Description");
		searchListBox.addItem("Factory");
		searchListBox.addItem("QUIX ID");
		searchListBox.addItem("Open Date");
		searchListBox.addItem("Close Date");
		searchListBox.addItem("State");

		alertDescriptionHandler();
	}

	/**
	 * Mouse over event Listener for alertDescription field
	 */
	public void alertDescriptionHandler() {
		factoryAlertTable.addCellPreviewHandler(new Handler<MonitorAlertProxy>() {
			PopupPanel popupPanel = new PopupPanel(true);
			TextArea textArea = new TextArea();

			@Override
			public void onCellPreview(CellPreviewEvent<MonitorAlertProxy> event) {
				if (event.getColumn() == 1) {
					if ("mouseover".equals(event.getNativeEvent().getType())) {
						String message = factoryAlertTable.getVisibleItem(event.getIndex() - factoryAlertTable.getPageStart()).getAlertDefinition().getDescription();
						if (message.length() > TEXT_SIZE) {
							textArea.setVisible(true);
							// textArea.setHeight("4em");
							textArea.setWidth("30em");
							textArea.setText(message);
							popupPanel.add(textArea);
							Widget source = (Widget) event.getSource();
							int x = source.getAbsoluteLeft() + event.getColumn() * 100;
							int y = source.getAbsoluteTop() + (event.getIndex() - factoryAlertTable.getPageStart() + 1) * 33;

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
	 * This method is called in FactoryAlertViewImpl for displaying of
	 * pagination
	 */
	public void setupSimplePager() {
		pager.setDisplay(factoryAlertTable);
	}

	@Override
	public ErrorPanel getErrorPanel() {
		return errorPanel;
	}

	@Override
	public void reset() {
		errorPanel.clear();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/**
	 * UiFactory constructor for the simplepager.
	 * 
	 * @return
	 */
	@UiFactory
	SimplePager createSimplePager() {
		// Constructor changed for enabling the lastpage icon in pagination
		final SimplePager factoryAlertsPager = new SimplePager(TextLocation.LEFT, pagerResources, false, DEFAULT_FAST_FORWARD_ROWS, true);
		return factoryAlertsPager;

	}

	/**
	 * This is used to draw the cell table.
	 * 
	 * @return CellTable for reports
	 */
	@UiFactory
	CellTable<MonitorAlertProxy> createTable() {
		final CellTable<MonitorAlertProxy> monitorAlertTable = new CellTable<MonitorAlertProxy>(PAGE_SIZE, getResources());
		monitorAlertTable.setEmptyTableWidget(new Label("No Factory Alerts found"));
		
		AbstractCell<String> criticalityCell = new AbstractCell<String>() {

			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<img src=\"" + getResources().iconStatusCritical().getSafeUri().asString() + "\" />" );
				sb.appendHtmlConstant("<span style=\"margin-left:5px\">Critical Alert</span>");
			}
		};
		
		Column<MonitorAlertProxy, String> criticalityColumn = new Column<MonitorAlertProxy, String>(criticalityCell) {

			@Override
			public String getValue(MonitorAlertProxy object) {
				return "";
			}
		};
		
		
		ClickableTextCell alertNameCell = new ClickableTextCell() {
			protected void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
				if (value != null) {
					sb.appendHtmlConstant("<a>");
					sb.append(value);
					sb.appendHtmlConstant("</a>");
				}
			}
		};

		Column<MonitorAlertProxy, String> nameColumn = new Column<MonitorAlertProxy, String>(alertNameCell) {
			@Override
			public String getValue(MonitorAlertProxy alert) {
				return alert.getAlertDefinition().getName();
			}
		};
		
		nameColumn.setFieldUpdater(new FieldUpdater<MonitorAlertProxy, String>() {

			@Override
			public void update(int index, MonitorAlertProxy alert, String value) {
				presenter.onAlertDetailClicked(alert.getId());
			}
		});
		
		nameColumn.setSortable(true);
		

		
		// Alert description column
		Column<MonitorAlertProxy, String> alertDesc = new Column<MonitorAlertProxy, String>(new TextCell()) {

			@Override
			public String getValue(MonitorAlertProxy alert) {
				String desc = alert.getAlertDefinition().getDescription();
				if (desc != null && desc.length() > TEXT_SIZE) {
					return desc.substring(0, TEXT_SIZE);
				}
				return desc;
			}
		};

		alertDesc.setSortable(true);
		
		// Dashboard link column
		Column<MonitorAlertProxy, String> dashboardColumn = new Column<MonitorAlertProxy, String>(alertNameCell) {

			@Override
			public String getValue(MonitorAlertProxy alert) {
				return "View Dashboard";
			}
			
		};
		
		
		dashboardColumn.setFieldUpdater(new FieldUpdater<MonitorAlertProxy, String>() {

			@Override
			public void update(int index, MonitorAlertProxy alert, String value) {
				presenter.onDashboardClicked(alert.getTaskExecution().getId());
			}
		});

		
		// QUIX ID column
		Column<MonitorAlertProxy, String> quixId = new Column<MonitorAlertProxy, String>(alertNameCell) {
			@Override
			public String getValue(MonitorAlertProxy alert) {
				if (alert.getQuixId() != null)
					return alert.getQuixId();
				else
					return "";
			}
		};

		quixId.setFieldUpdater(new FieldUpdater<MonitorAlertProxy, String>() {

			@Override
			public void update(int index, MonitorAlertProxy alert, String value) {
				presenter.onQuixIdClicked(alert);
			}
		});

		quixId.setSortable(true);
		

		// Open Date column
		Column<MonitorAlertProxy, String> openDate = new Column<MonitorAlertProxy, String>(new TextCell()) {

			@Override
			public String getValue(MonitorAlertProxy alert) {
				DateTimeFormat format = DateTimeFormat.getFormat("yyyy-MM-dd");
				return format.format(alert.getCreationDate());
			}
		};
		openDate.setSortable(true);
		

		// Closed Date column
		Column<MonitorAlertProxy, String> closeDate = new Column<MonitorAlertProxy, String>(new TextCell()) {

			@Override
			public String getValue(MonitorAlertProxy alert) {
				DateTimeFormat format = DateTimeFormat.getFormat("yyyy-MM-dd");
				if (alert.getClosedDate() != null) {
					return format.format(alert.getClosedDate());
				} else {
					return "N/A";
				}
			}
		};
		closeDate.setSortable(true);
		

		// State column
		Column<MonitorAlertProxy, String> state = new Column<MonitorAlertProxy, String>(new TextCell()) {

			@Override
			public String getValue(MonitorAlertProxy alert) {
				return alert.getStatus();
			}
		};

		state.setSortable(true);
		
		
		// Add the columns
		monitorAlertTable.addColumn(criticalityColumn, "Alert Criticality", "");
		monitorAlertTable.addColumn(nameColumn, "Alert Name", "");
		monitorAlertTable.addColumn(alertDesc, "Alert Description", "");
		monitorAlertTable.addColumn(dashboardColumn, "Dashboard", "");
		monitorAlertTable.addColumn(quixId, "QuixId", "");
		
		monitorAlertTable.addColumn(openDate, "Open Date", "");
		monitorAlertTable.addColumn(closeDate, "Close Date", "");
		monitorAlertTable.addColumn(state, "State", "");

		
		
		return monitorAlertTable;
	}

	@Override
	public TextBox getSearchText() {
		return searchQueryInput;
	}

	/**
	 * Return the alerts Cell Table.
	 */
	@Override
	public CellTable<MonitorAlertProxy> getFactoryAlertTable() {
		return factoryAlertTable;
	}

	@Override
	public ListBox searchListBox() {
		return searchListBox;
	}

	@Override
	public int getColumnIndex(Column<MonitorAlertProxy, ?> column) {
		//
		return this.factoryAlertTable.getColumnIndex(column);
	}

	/**
	 * getter for searcButon button
	 */
	@Override
	public Button getSearch() {
		return searcButon;
	}

	/**
	 * Method for implementing sort functionality
	 */
	@Override
	public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler) {
		return factoryAlertTable.addColumnSortHandler(handler);
	}

	@Override
	public SimplePager getPager() {
		return this.pager;
	}

}
