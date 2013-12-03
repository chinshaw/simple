package com.simple.original.client.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.simple.original.client.dashboard.TableWidget.MetricRowProxy;
import com.simple.original.client.dashboard.events.WidgetModelChangedEvent;
import com.simple.original.client.dashboard.events.WidgetSelectedEvent;
import com.simple.original.client.dashboard.model.ITableWidgetModel;
import com.simple.original.client.dashboard.model.IWidgetModel;
import com.simple.original.client.overlays.MetricMatrixOverlay;
import com.simple.original.client.proxy.MetricDoubleProxy;
import com.simple.original.client.proxy.MetricProxy;
import com.simple.original.client.proxy.MetricStringProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.widgets.LinkableWidget;

/**
 * This is a table represenation for dashboard that represents a table. It is a
 * fairly complex class because of the need to do do page and represent a table
 * of metrics.
 * 
 * @author chinshaw
 */
public class TableWidget extends AbstractDashboardWidget<ITableWidgetModel> implements HasData<MetricRowProxy>, LinkableWidget, IInspectable,
        WidgetModelChangedEvent.Handler {

    public interface MetricRowProxy {
        public List<MetricCellProxy> getCells();
    }

    /**
     * This is the proxy that represents our metric cell from the
     * {@link com.simple.domain.dashboard.TableWidget.MetricCell}.
     * 
     * @author chinshaw
     */
    public static interface MetricCellProxy extends ValueProxy {

        public Long getMetricId();

        public String getValue();
    }

    /**
     * Dynamic metric column that aids in rendering an indexed column. This is
     * used to map a column using a list of entities rather than a single entity
     * type.
     * 
     * @author chinshaw
     */
    private class MericColumn extends Column<MetricRowProxy, MetricCellProxy> {
        int index;

        public MericColumn(int index) {
            super(new MetricTableCell());
            this.index = index;
        }

        @Override
        public MetricCellProxy getValue(MetricRowProxy row) {
            MetricCellProxy metricCell = null;
            try {
                // TODO why row null occassionally.
                if (row != null && row.getCells() != null) {
                    metricCell = row.getCells().get(index);
                }
            } catch (IndexOutOfBoundsException e) {
                // Don't do anything with this. It means we have gone past our
                // total number of cells.
            }
            return metricCell;
        }
    }

    /**
     * This is our MetricCell that overrides the onEnterKeyDown event in
     * MetricCell.
     */
    private class MetricTableCell extends AbstractCell<MetricCellProxy> {

        /**
         * Default constructor, enables click handling.
         */
        public MetricTableCell() {
            super("click", "keydown");
        }

        /**
         * Render the MetricCell, this will look at the type of
         * {@link MetricProxy} that this object is and it will render the cell
         * appropriately based on the values in the cell. For example a
         * {@link MetricDoubleProxy} cell has a low, mid, and high range. The
         * render function takes this into account and will color code the cell
         * according to where the metric value falls into the range. A
         * {@link MetricStringProxy} will be simply treated as a string.
         */
        @Override
        public void render(Context context, MetricCellProxy metricCell, SafeHtmlBuilder sb) {
            // Values should typically always be higher than this value.
            if (metricCell == null) {
                sb.appendHtmlConstant("<div style=\"color:333; width: 8em;\">Loading data...</div>");
            } else {
                sb.appendHtmlConstant("<div style=\"color:333; width: 8em;\">" + metricCell.getValue() + "</div>");
            }
        }

        @Override
        public void onBrowserEvent(Context context, Element parent, MetricCellProxy value, NativeEvent event, ValueUpdater<MetricCellProxy> valueUpdater) {
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
            if ("click".equals(event.getType())) {
                EventTarget eventTarget = event.getEventTarget();
                if (!Element.is(eventTarget)) {
                    return;
                }
                if (parent.getFirstChildElement().isOrHasChild(Element.as(eventTarget))) {
                    // Ignore clicks that occur outside of the main element.
                    onEnterKeyDown(context, parent, value, event, valueUpdater);
                }
            }
        }

        /**
         * Called when the cell is clicked and will make a callback to the
         * delegate with the id of the metric.
         */
        @Override
        protected void onEnterKeyDown(Context context, Element parent, MetricCellProxy value, NativeEvent event, ValueUpdater<MetricCellProxy> valueUpdater) {
            eventBus.fireEvent(new WidgetSelectedEvent(TableWidget.this));
        }
    }

    /**
     * This is the uibinder and it will use the view.DefaultView.ui.xml
     * template.
     */
    @UiTemplate("DashboardMetricTable.ui.xml")
    public interface Binder extends UiBinder<Widget, TableWidget> {
    }

    /**
     * Our logger for this class.
     */
    private static final Logger logger = Logger.getLogger("DashboardMetricTable");

    /**
     * Our table instance from ui binder.
     */
    @UiField(provided = true)
    CellTable<MetricRowProxy> table;

    /**
     * Simple pager instance used to page between results.
     */
    @UiField(provided = true)
    SimplePager pager;

    /**
     * This will hold the name of our table right above the table.
     */
    @UiField
    SpanElement tableTitle;

    @UiField
    AbsolutePanel container;

    @UiField
    ViolationInfo violationInfo;

    private final ListDataProvider<MetricRowProxy> dataProvider = new ListDataProvider<TableWidget.MetricRowProxy>();

    /**
     * This is the context for the selectedCell;
     */
    private String selectedCellContext;

    /**
     * Table constructor.
     * 
     * @param resources
     *            The resources used to style the table.
     * 
     */
    @Inject
    public TableWidget(final EventBus eventBus, Resources resources) {
        super(eventBus, resources);

        table = new CellTable<MetricRowProxy>(10, resources);
        pager = new SimplePager(TextLocation.LEFT);

        initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
    }

    public void reset() {
        while (table.getColumnCount() > 0) {
            table.removeColumn(0);
        }
    }

    public AbstractDataProvider<MetricRowProxy> getDataProvider() {
        return dataProvider;
    }

    public void addColumn(MericColumn column, String header) {
        table.addColumn(column, header);
    }

    public void addColumn(MericColumn column, String header, String footer) {
        table.addColumn(column, header, footer);
    }

    @Override
    public String getContext() {
        return selectedCellContext;
    }

    @Override
    public HandlerRegistration addRangeChangeHandler(Handler handler) {
        return table.addRangeChangeHandler(handler);
    }

    @Override
    public HandlerRegistration addRowCountChangeHandler(com.google.gwt.view.client.RowCountChangeEvent.Handler handler) {
        return table.addRowCountChangeHandler(handler);
    }

    @Override
    public int getRowCount() {
        return table.getRowCount();
    }

    @Override
    public Range getVisibleRange() {
        return table.getVisibleRange();
    }

    @Override
    public boolean isRowCountExact() {
        return table.isRowCountExact();
    }

    @Override
    public void setRowCount(int count) {
        table.setRowCount(count);
    }

    @Override
    public void setRowCount(int count, boolean isExact) {
        table.setRowCount(count, isExact);
    }

    @Override
    public void setVisibleRange(int start, int length) {
        table.setVisibleRange(start, length);
    }

    @Override
    public void setVisibleRange(Range range) {
        table.setVisibleRange(range);
    }

    @Override
    public HandlerRegistration addCellPreviewHandler(com.google.gwt.view.client.CellPreviewEvent.Handler<MetricRowProxy> handler) {
        return table.addCellPreviewHandler(handler);
    }

    @Override
    public SelectionModel<? super MetricRowProxy> getSelectionModel() {
        return table.getSelectionModel();
    }

    @Override
    public MetricRowProxy getVisibleItem(int indexOnPage) {
        return table.getVisibleItem(indexOnPage);
    }

    @Override
    public int getVisibleItemCount() {
        return table.getVisibleItemCount();
    }

    @Override
    public Iterable<MetricRowProxy> getVisibleItems() {
        return table.getVisibleItems();
    }

    @Override
    public void setRowData(int start, List<? extends MetricRowProxy> values) {
        table.setRowData(start, values);
    }

    @Override
    public void setSelectionModel(SelectionModel<? super MetricRowProxy> selectionModel) {
        table.setSelectionModel(selectionModel);
    }

    @Override
    public void setVisibleRangeAndClearData(Range range, boolean forceRangeChangeEvent) {
        table.setVisibleRangeAndClearData(range, forceRangeChangeEvent);
    }

    @UiHandler("export")
    void onExport(ClickEvent event) {
        //String query = "rf/secure/analyticsExport?" + AnalyticsExport.EXPORT_TYPE + "=" + AnalyticsExport.ExportType.EXCEL + "&" + AnalyticsExport.EXPORT_ID + "="
        //        + getModel().getMetric().getId().toString();
        //Window.open(GWT.getHostPageBaseURL() + query, "_blank", "enabled");
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        // TODO Auto-generated method stub
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    private void initializeColumns(List<String> headers) {
        // Clear any columns if there are any.
        while (table.getColumnCount() > 0) {
            table.removeColumn(0);
        }

        for (int i = 0; i < headers.size(); i++) {
            String columnName = headers.get(i);
            table.addColumn(new MericColumn(i), columnName);
        }
    }

    @Override
    public void setModel(ITableWidgetModel model) {
        this.model = model;
        
//        if (model.getMetricId() != null && model.getMetric().getJsonUrl() != null) {
//            doConfigureTable(model.getMetric().getJsonUrl());
//        } else {
//            doEmptyTable();
//        }
    }

    private void doConfigureTable(String jsonFileName) {
    	if (jsonFileName == null) {
    		throw new IllegalArgumentException("While trying to configure table the table's data file was not specified");
    	}
    	
    	final String url = "/public/tables/" + jsonFileName;
        
    	
    	try {
            new RequestBuilder(RequestBuilder.GET, url).sendRequest("", new RequestCallback() {
                @Override
                public void onResponseReceived(Request req, Response resp) {
                	if (resp.getText() == null) {
                		
                	}
                    MetricMatrixOverlay matrix = JsonUtils.safeEval(resp.getText());
                    doRenderTable(matrix);
                }

                @Override
                public void onError(Request res, Throwable throwable) {
                    logger.log(Level.SEVERE, "Unable to retreive table data ", throwable);
                }
            });
        } catch (RequestException e) {
        	// This won't do anything
           // e.printStackTrace();
        }
    }

    private void doEmptyTable() {
        for (int i = 0; i < 10; i++) {
            table.addColumn(new Column<MetricRowProxy, String>(new ImageCell()) {

                @Override
                public String getValue(MetricRowProxy object) {
                    return resources.alertSmall().getSafeUri().asString();
                }
            }, "Column " + i);
        }

        List<MetricRowProxy> rows = new ArrayList<MetricRowProxy>();
        for (int i = 0; i < 10; i++) {
            final MetricRowProxy row = new MetricRowProxy() {

                @Override
                public List<MetricCellProxy> getCells() {
                    List<MetricCellProxy> cells = new ArrayList<MetricCellProxy>();
                    for (int i = 0; i < 10; i++) {
                        MetricCellProxy cell = new MetricCellProxy() {

                            @Override
                            public Long getMetricId() {
                                return null;
                            }

                            @Override
                            public String getValue() {
                                return "Provided at Runtime";
                            }
                        };
                        cells.add(cell);
                    }
                    return cells;
                }
            };
            rows.add(row);
        }
        table.setRowData(0, rows);
    }

    private void doRenderTable(MetricMatrixOverlay matrix) {
        dataProvider.addDataDisplay(table);
        List<String> headers = matrix.getHeaders();

        initializeColumns(headers);

        dataProvider.setList(matrix.getRows());
        tableTitle.setInnerText(model.getTitle());
        pager.setDisplay(table);
    }

    private void clearTable() {
        while (table.getColumnCount() > 0) {
            table.removeColumn(0);
        }
    }

    /**
     * Will set the model from the event if the model matches the current model.
     */
    @Override
    public void onWidgetModelChanged(WidgetModelChangedEvent event) {
        IWidgetModel eventModel = event.getWidgetModel();
        if (eventModel == model) {
            setModel((ITableWidgetModel) eventModel);
        }
    }

	@Override
	public ImageResource getSelectorIcon() {
		return resources.tableWidgetSelector();
	}
}