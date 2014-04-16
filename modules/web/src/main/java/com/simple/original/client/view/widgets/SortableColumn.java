package com.simple.original.client.view.widgets;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.user.cellview.client.Column;

public abstract class SortableColumn<T, C> extends Column<T, C>{
    
    
    public SortableColumn(Cell<C> cell) {
        super(cell);
        setSortable(true);
    }

    public abstract String getColumnSortKey();
}