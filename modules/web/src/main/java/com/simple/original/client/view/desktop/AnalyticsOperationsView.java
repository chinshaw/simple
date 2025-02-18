package com.simple.original.client.view.desktop;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.api.domain.GroupMembership;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.proxy.JavaAnalyticsOperationProxy;
import com.simple.original.client.proxy.RAnalyticsOperationProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IOperationsView;
import com.simple.original.client.view.widgets.ErrorPanel;

public class AnalyticsOperationsView extends AbstractView implements IOperationsView {

	class OperationCell extends AbstractCell<AnalyticsOperationProxy> {

		private Resources resources;

		public OperationCell(Resources resources) {
			super("click");
			this.resources = resources;
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, AnalyticsOperationProxy value, SafeHtmlBuilder sb) {
			SafeHtml name = SafeHtmlUtils.fromString(value.getName());
			SafeHtml html = new SafeHtmlBuilder().appendHtmlConstant("<div class=\"" + resources.style().iconCell() + "\"><div class=\"graphic\"><span style=\"font-style:italic\">f(x)</span></div><div class=\"text\">")
					.append(name).appendHtmlConstant("</div>").toSafeHtml();
			sb.append(html);
		}

		public void onBrowserEvent(Context context, Element parent, AnalyticsOperationProxy value, NativeEvent event, ValueUpdater<AnalyticsOperationProxy> valueUpdater) {

			super.onBrowserEvent(context, parent, value, event, valueUpdater);
			if ("click".equals(event.getType())) {
				EventTarget eventTarget = event.getEventTarget();
				if (!Element.is(eventTarget)) {
					return;
				}

				presenter.onOperationSelected(value);

				/*
				 * if
				 * (Element.as(eventTarget).getId().equalsIgnoreCase("remove"))
				 * { onRemoveCell(value); }
				 * 
				 * if (Element.as(eventTarget).getId().equalsIgnoreCase("edit"))
				 * { onEditOperation(value); }
				 */
			}
		}
	}

	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	@UiTemplate("AnalyticsOperationsView.ui.xml")
	public interface Binder extends UiBinder<Widget, AnalyticsOperationsView> {
	}

	/**
	 * Page size for the number of Analytics Operation showing on table.
	 */
	private final int PAGE_SIZE = 15;

	private final String appendText = " Operations";

	/**
	 * FastforwardRows size to the simplepager constructor
	 */
	private static int DEFAULT_FAST_FORWARD_ROWS = 1000;

	/**
	 * PagerResources
	 */
	SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);

	/**
	 * To display the error message.
	 */
	@UiField
	ErrorPanel errorPanel;

	/**
	 * Text box to enter the search criteria.
	 */
	@UiField
	TextBox searchQueryInput;

	/**
	 * To perform the search.
	 */
	@UiField
	Button searchResults;
	
	SplitLayoutPanel sp;

	/**
	 * To add New analytics Operation.
	 */
	@UiField
	Button addAnalytics;

	/**
	 * Table containing the list of Analytics Operation.
	 */
	@UiField(provided = true)
	CellList<AnalyticsOperationProxy> operationsList;

	/**
	 * Pagination for displaying the list of Analytics operations.
	 */
	@UiField
	SimplePager pager;

	/**
	 * Presenter interface for AnalyticsView.
	 */
	private Presenter presenter;

	@Inject
	public AnalyticsOperationsView(EventBus eventBus, Resources resources) {
		super(resources);
		pager = new SimplePager();

		operationsList = new CellList<AnalyticsOperationProxy>(new OperationCell(resources), resources);

		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

		pager.setDisplay(operationsList);
	}

	/**
	 * This method is called to set view for Admin/user in bind to view .
	 */
	@Override
	public void setUserView(GroupMembership userGroup) {

	}

	/**
	 * This method is used to handle change of list item selected in the drop
	 * down listBox
	 */
	public void addOperationListHandler() {
	}

	/**
	 * This method is used to set View for Admin and User
	 */
	private void setView() {

	}

	/**
	 * To set the Presenter.
	 */
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
		final SimplePager pager = new SimplePager(TextLocation.LEFT, pagerResources, false, DEFAULT_FAST_FORWARD_ROWS, true);
		return pager;
	}

	/**
	 * This is used to draw the CellTable.
	 * 
	 * @return CellTable for Analytics Operation.
	 */
	@UiFactory
	CellTable<AnalyticsOperationProxy> createTable() {
		final CellTable<AnalyticsOperationProxy> analyticsTable = new CellTable<AnalyticsOperationProxy>(PAGE_SIZE, getResources());
		analyticsTable.setEmptyTableWidget(new Label("No analytics operation found"));
		CheckboxCell ch = new CheckboxCell(true, true);
		Column<AnalyticsOperationProxy, Boolean> selectionColumn = new Column<AnalyticsOperationProxy, Boolean>(ch) {

			@Override
			public Boolean getValue(AnalyticsOperationProxy object) {
				return analyticsTable.getSelectionModel().isSelected(object);
			}
		};
		analyticsTable.setColumnWidth(selectionColumn, "3em");

		selectionColumn.setFieldUpdater(new FieldUpdater<AnalyticsOperationProxy, Boolean>() {
			public void update(int index, AnalyticsOperationProxy object, Boolean value) {
				analyticsTable.getSelectionModel().setSelected(object, value);
			}
		});

		// Select All
		CheckboxCell selectAll = new CheckboxCell(true, true) {
			@Override
			public void onBrowserEvent(Context context, Element parent, Boolean value, NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
				InputElement input = parent.getFirstChild().cast();
				Boolean isChecked = input.isChecked();
				presenter.onSelectAll(isChecked);
			}
		};

		analyticsTable.addColumn(selectionColumn, new Header<Boolean>(selectAll) {
			@Override
			public Boolean getValue() {
				return false;
			}
		});

		// Operation ID
		Column<AnalyticsOperationProxy, Number> idColumn = new Column<AnalyticsOperationProxy, Number>(new NumberCell(NumberFormat.getFormat("#"))) {

			@Override
			public Number getValue(AnalyticsOperationProxy object) {
				return object.getId();
			}
		};
		analyticsTable.addColumn(idColumn, "Operation ID");

		// Operation Name
		Column<AnalyticsOperationProxy, String> nameColumn = new Column<AnalyticsOperationProxy, String>(new TextCell()) {

			@Override
			public String getValue(AnalyticsOperationProxy analytics) {
				return analytics.getName();
			}

		};

		nameColumn.setSortable(true);
		analyticsTable.addColumn(nameColumn, "Operation Name", "");

		// Operation Description
		Column<AnalyticsOperationProxy, String> descriptionColumn = new Column<AnalyticsOperationProxy, String>(new TextCell()) {

			@Override
			public String getValue(AnalyticsOperationProxy analytics) {
				return analytics.getDescription();
			}
		};
		descriptionColumn.setSortable(true);
		analyticsTable.addColumn(descriptionColumn, "Operation Description", "");

		// Operation Type
		Column<AnalyticsOperationProxy, String> typeColumn = new Column<AnalyticsOperationProxy, String>(new TextCell()) {

			@Override
			public String getValue(AnalyticsOperationProxy analytics) {
				String type = "";
				if (analytics instanceof RAnalyticsOperationProxy) {
					type = "R Script";
				} else if (analytics instanceof JavaAnalyticsOperationProxy) {
					type = "Java Analytics";
				}
				return type;
			}
		};
		typeColumn.setSortable(true);
		analyticsTable.addColumn(typeColumn, "Operation Type", "");

		// Owner
		Column<AnalyticsOperationProxy, String> ownerColumn = new Column<AnalyticsOperationProxy, String>(new TextCell()) {

			@Override
			public String getValue(AnalyticsOperationProxy analytics) {
				return (analytics.getOwner() != null) ? analytics.getOwner().getName() : "Unknown User";
			}
		};

		ownerColumn.setSortable(true);
		analyticsTable.addColumn(ownerColumn, "Owner", "");

		// Visibility
		Column<AnalyticsOperationProxy, String> visibilityColumn = new Column<AnalyticsOperationProxy, String>(new TextCell()) {

			@Override
			public String getValue(AnalyticsOperationProxy analytics) {
				if (analytics.getPublicAccessible()) {
					return "Public";
				} else {
					return "Private";
				}
			}
		};
		visibilityColumn.setSortable(true);
		analyticsTable.addColumn(visibilityColumn, "Visibility", "");

		return analyticsTable;
	}

	/**
	 * Event fired on the click of Search Button.
	 * 
	 * @param clickEvent
	 */
	@UiHandler("searchResults")
	void searcResults(ClickEvent clickEvent) {
		errorPanel.clear();
		presenter.onClickSearch();
	}

	/**
	 * Event fired on the click of New Button.
	 * 
	 * @param clickEvent
	 */
	@UiHandler("addAnalytics")
	void onAddOperations(ClickEvent clickEvent) {
		errorPanel.clear();
		presenter.onAddAnalytics();
	}

	@Override
	public ErrorPanel getErrorPanel() {
		return errorPanel;
	}

	/**
	 * Function used to reset the display. Clear out any handlers and such.
	 */
	@Override
	public void reset() {
		errorPanel.clear();
		searchQueryInput.setText("");
	}

	@Override
	public TextBox getSearchText() {
		return searchQueryInput;
	}

	@Override
	public HasData<AnalyticsOperationProxy> getOperationsList() {
		return operationsList;
	}

}
