package com.simple.original.client.view.widgets;

import java.util.List;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.google.gwt.view.client.SelectionModel;
import com.simple.original.client.proxy.DatastoreObjectProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.service.DaoBaseDataProvider;
import com.simple.original.client.service.DaoBaseDataProvider.HasBaseDataProvider;

public class SmartTable<T extends DatastoreObjectProxy> extends Composite implements HasData<T>, HasBaseDataProvider<T> {

    public class SmartPager extends SimplePager {
    }

    private final FlowPanel layout = new FlowPanel();
    private final CellTable<T> cellTable;
    private final SimplePager pager;
    private final DefaultTextBox search;

    @UiConstructor
    public SmartTable(int pageSize, Resources resources) {
        initWidget(layout);

        ProvidesKey<T> keyProvider = new ProvidesKey<T>() {

            @Override
            public Long getKey(T item) {
                return item.getId();
            }
        };
        
        cellTable = new CellTable<T>(pageSize, resources, keyProvider);
        search = new DefaultTextBox("search");
        search.addStyleName(resources.style().tableSearch());
        layout.add(search);

        pager = new SimplePager(TextLocation.LEFT);
        pager.setDisplay(cellTable);

        layout.add(cellTable);
        layout.add(pager);
    }

    public SmartTable(int pageSize, Resources resources, DaoBaseDataProvider<T> dataProvider) {
        this(pageSize, resources);
        setDataProvider(dataProvider);
    }

    public void setDataProvider(final DaoBaseDataProvider<T> dataProvider) {
        dataProvider.addDataDisplay(cellTable);
        search.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                dataProvider.search(search.getText());
            }
        });
    }

    @Override
    public HandlerRegistration addRangeChangeHandler(Handler handler) {
        return cellTable.addRangeChangeHandler(handler);
    }

    @Override
    public HandlerRegistration addRowCountChangeHandler(com.google.gwt.view.client.RowCountChangeEvent.Handler handler) {
        return cellTable.addRowCountChangeHandler(handler);
    }

    @Override
    public int getRowCount() {
        return cellTable.getRowCount();
    }

    @Override
    public Range getVisibleRange() {
        return cellTable.getVisibleRange();
    }

    @Override
    public boolean isRowCountExact() {
        return cellTable.isRowCountExact();
    }

    @Override
    public void setRowCount(int count) {
        cellTable.setRowCount(count);
    }

    @Override
    public void setRowCount(int count, boolean isExact) {
        cellTable.setRowCount(count, isExact);

    }

    @Override
    public void setVisibleRange(int start, int length) {
        cellTable.setVisibleRange(start, length);
    }

    @Override
    public void setVisibleRange(Range range) {
        cellTable.setVisibleRange(range);
    }

    @Override
    public HandlerRegistration addCellPreviewHandler(com.google.gwt.view.client.CellPreviewEvent.Handler<T> handler) {
        return cellTable.addCellPreviewHandler(handler);
    }

    @Override
    public SelectionModel<? super T> getSelectionModel() {
        return cellTable.getSelectionModel();
    }

    @Override
    public T getVisibleItem(int indexOnPage) {
        return cellTable.getVisibleItem(indexOnPage);
    }

    @Override
    public int getVisibleItemCount() {
        return cellTable.getVisibleItemCount();
    }

    @Override
    public Iterable<T> getVisibleItems() {
        return cellTable.getVisibleItems();
    }

    @Override
    public void setRowData(int start, List<? extends T> values) {
        cellTable.setRowData(start, values);
    }

    @Override
    public void setSelectionModel(SelectionModel<? super T> selectionModel) {
        cellTable.setSelectionModel(selectionModel);
    }

    @Override
    public void setVisibleRangeAndClearData(Range range, boolean forceRangeChangeEvent) {
        cellTable.setVisibleRangeAndClearData(range, forceRangeChangeEvent);
    }
    
    
    public void addColumn(Column<T, ?> col) {
        cellTable.addColumn(col);
    }


    /**
     * Adds a column to the end of the table with an associated {@link SafeHtml}
     * header.
     * 
     * @param col
     *            the column to be added
     * @param headerHtml
     *            the associated header text, as safe HTML
     */
    public void addColumn(Column<T, ?> col, SafeHtml headerHtml) {
        cellTable.addColumn(col, headerHtml);
    }

    public void addColumn(Column<T, ?> col, String headerString) {
        cellTable.addColumn(col, headerString);
    }
    

    public void addColumn(Column<T, ?> col, String headerString, String footerString) {
        cellTable.addColumn(col, headerString, footerString);
    }

    public void addColumn(Column<T, ?> col, Header<?> header) {
        cellTable.addColumn(col, header);
    }

    public int getColumnIndex(Column<T, ?> column) {
        return cellTable.getColumnIndex(column);
    }

    public void setColumnWidth(Column<T, ?> col, String width) {
        cellTable.setColumnWidth(col, width);
    }

    public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler) {
        return cellTable.addColumnSortHandler(handler);
    }
    //To remove the Search Layout from Alert Subscription Screen
    public void removeSearch(){
    	layout.remove(search);
    }

    public void setEmptyTableWidget(Widget widget) {
        cellTable.setEmptyTableWidget(widget);
    }
}