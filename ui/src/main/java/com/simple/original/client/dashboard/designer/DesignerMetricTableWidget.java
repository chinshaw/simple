package com.simple.original.client.dashboard.designer;

import com.google.gwt.resources.client.ImageResource;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.TableWidget;
import com.simple.original.client.dashboard.model.ITableWidgetModel;
import com.simple.original.client.resources.Resources;

public class DesignerMetricTableWidget extends DesignerWidget<TableWidget, ITableWidgetModel, TableEditor> {
 
    
    public DesignerMetricTableWidget(EventBus eventBus, Resources resources, TableEditor editor) {
        super(eventBus, new TableWidget(eventBus, resources), editor);
        initContextHandlers();
        initDraggable();
        
        enableEditableHeaders();
    }
    
    private void enableEditableHeaders() {
        
    }

	@Override
	public void setModel(ITableWidgetModel model) {
		getWrappedWidget().setModel(model);
		
	}

	@Override
	public void setTitle(String title) {
		getWrappedWidget().setTitle(title);
	}

	@Override
	public ImageResource getSelectorIcon() {
		// TODO Auto-generated method stub
		return null;
	}
    
    /*
    private void initializeColumns(List<String> headers) {
        // Clear any columns if there are any.
        while (table.getColumnCount() > 0) {
            table.removeColumn(0);
        }

        for (int i = 0; i < headers.size(); i++) {
            final int index = i; // inner method needs final value
            String columnName = headers.get(i);
            table.addColumn(new MericColumn(i), new MetricTableHeader(columnName, new ValueUpdater<String>() {
                @Override
                public void update(String value) {
                    //getModel().getHeaders().set(index, value);
                }
            }));
        }
    }
    */
    
}