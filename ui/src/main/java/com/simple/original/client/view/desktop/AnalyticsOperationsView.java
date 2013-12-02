package com.simple.original.client.view.desktop;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.api.domain.GroupMembership;
import com.simple.original.api.domain.RecordFecthType;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.proxy.JavaAnalyticsOperationProxy;
import com.simple.original.client.proxy.RAnalyticsOperationProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IOperationsView;
import com.simple.original.client.view.widgets.ErrorPanel;


public class AnalyticsOperationsView extends AbstractView implements IOperationsView {

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
	SimplePager.Resources pagerResources = 	GWT.create(SimplePager.Resources.class);
	
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
     * List of available Operations for Admin view.
     */
    @UiField
    ListBox operationsList = new ListBox();
    
    /**
     * To perform the search.
     */
    @UiField
    Button searchResults; 
    
    /**
     * To add New analytics Operation.
     */
    @UiField
    Button addAnalytics;   
   
    /**
     * To Delete Operation.
     */
	@UiField
    Button deleteOperations;
    
	/**
	 * To Copy Operation.
	 */
    @UiField
    Button copyOperations;
    
    /**
	 * To Edit Operation.
	 */
    @UiField
    Button editOperations;
    
   	/**
     * Table containing the list of Analytics Operation.
     */
    @UiField
    CellTable<AnalyticsOperationProxy> analyticsTableData; 

	/**
     * Pagination for displaying the list of Analytics operations.
     */
    @UiField
    SimplePager pager;    
    
    /**
     * Presenter interface for AnalyticsView.
     */
    private Presenter presenter;

    private GroupMembership userRole;

    @Inject
    public AnalyticsOperationsView(EventBus eventBus, Resources resources) {
        super(resources);
        pager = new SimplePager();

        initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));        
        pager.setDisplay(analyticsTableData);
    }


    /**
     * This method is called to set view for Admin/user in bind to view .
     */
    @Override
    public void setUserView(GroupMembership userGroup){
    	userRole = userGroup;
    	operationsList.clear();
	    if(userRole == GroupMembership.ADMINISTRATOR ){
	    	for(RecordFecthType type : RecordFecthType.values()){
	    		operationsList.addItem(type.getDescription()+appendText, type.name());
	    	}
	    	operationsList.setSelectedIndex(RecordFecthType.ALL_RECORDS.ordinal());
	    }else{
	    	operationsList.addItem(RecordFecthType.PUBLIC_RECORDS.getDescription()+appendText, RecordFecthType.PUBLIC_RECORDS.name());
	    	operationsList.addItem(RecordFecthType.MY_RECORDS.getDescription()+appendText, RecordFecthType.MY_RECORDS.name());
			operationsList.setSelectedIndex(1);
	    }
    	setView();
	    addOperationListHandler();
    }

    /**
	 * This method is used to handle change of list item selected in the drop down listBox
	 */
    public void addOperationListHandler() {
    	operationsList.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
            	errorPanel.clear();
            	searchQueryInput.setText("");
            	setView();
               presenter.onOperationSelected( operationsList.getValue(operationsList.getSelectedIndex()));
            }
        });
	}

    /**
     * This method is used to set View for Admin and User
     */
    private void setView(){
    	String operationSelected = operationsList.getValue(operationsList.getSelectedIndex()); 
    	if(userRole == GroupMembership.USER){// User View
			if(operationSelected.equals(RecordFecthType.PUBLIC_RECORDS.name())){
        		setEnabledAddButton(false);

        	}else{
        		setEnabledAddButton(true);
        	}
    	}
    	disableCopyEditDeleteButtons();		
    }

		/**
	 * To set the Presenter.
	 */
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }


    /**
     * UiFactory constructor for the simplepager. 
     * @return
     */
    @UiFactory
    SimplePager createSimplePager() {
    	//Constructor changed for enabling the lastpage icon in pagination
    	final SimplePager pager = new SimplePager(TextLocation.LEFT, pagerResources, false, DEFAULT_FAST_FORWARD_ROWS, true);
        return pager;
    }

    /**
     * This is used to draw the CellTable.
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
            	presenter.onCheckBoxSelected(object,value);
                analyticsTable.getSelectionModel().setSelected(object, value);
            }
        });

        //Select All
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
        
        //Operation Name 
        Column<AnalyticsOperationProxy, String> nameColumn = new Column<AnalyticsOperationProxy, String>(new TextCell()) {

            @Override
            public String getValue(AnalyticsOperationProxy analytics) {
                return analytics.getName();
            }

        };
        
        nameColumn.setSortable(true);
        analyticsTable.addColumn(nameColumn, "Operation Name", "");

        //Operation Description
        Column<AnalyticsOperationProxy, String> descriptionColumn = new Column<AnalyticsOperationProxy, String>(new TextCell()) {

            @Override
            public String getValue(AnalyticsOperationProxy analytics) {
                return analytics.getDescription();
            }
        };
        descriptionColumn.setSortable(true);
        analyticsTable.addColumn(descriptionColumn, "Operation Description", "");

        //Operation Type
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
       

        //Owner
        Column<AnalyticsOperationProxy, String> ownerColumn = new Column<AnalyticsOperationProxy, String>(new TextCell()) {

            @Override
            public String getValue(AnalyticsOperationProxy analytics) {
            	return  (analytics.getOwner() != null) ? analytics.getOwner().getName() : "Unknown User";
            }
        };

        ownerColumn.setSortable(true);
        analyticsTable.addColumn(ownerColumn, "Owner", "");
        
        // Visibility
        Column<AnalyticsOperationProxy, String> visibilityColumn = new Column<AnalyticsOperationProxy, String>(new TextCell()) {

            @Override
            public String getValue(AnalyticsOperationProxy analytics) {
            	if (analytics.isPublic()) {
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
     * @param clickEvent
     */
    @UiHandler("searchResults")
    void searcResults(ClickEvent clickEvent) {
    	disableCopyEditDeleteButtons();
    	errorPanel.clear();
        presenter.onClickSearch();
    }

    /**
     * Event fired on the click of New Button.
     * @param clickEvent
     */
    @UiHandler("addAnalytics")
    void onAddOperations(ClickEvent clickEvent) {
    	errorPanel.clear();
        presenter.onAddAnalytics();
    }

    /**
     * Event fired on the click of Delete Button.
     * @param clickEvent
     */
    @UiHandler("deleteOperations")
    void onDeleteOperations(ClickEvent clickEvent) {
    	errorPanel.clear();
    	presenter.onDeleteAnalyticsOperations();
    }

	/**
     * Event fired onclick of Copy Operation button.
     * @param clickEvent
     */
	@UiHandler("copyOperations")
    void onCopyOperations(ClickEvent clickEvent) {
		errorPanel.clear();
    	presenter.onCopyAnalytics();
    }

    /**
     * Event fired onclick of Edit Operation button.
     * @param clickEvent
     */
	@UiHandler("editOperations")
    void onEditOperations(ClickEvent clickEvent) {
		errorPanel.clear();
    	presenter.onEditAnalytics();
    }

    /**
     * Method to set visibility of Delete Analytics Operation button
     */
	@Override
    public void  setEnabledDeleteButton(boolean value) {
		if(userRole !=  null && 
				operationsList.getSelectedIndex() >= 0 && 
				userRole == GroupMembership.USER &&
				operationsList.getValue(operationsList.getSelectedIndex()).equals(RecordFecthType.PUBLIC_RECORDS.name()) ){
			deleteOperations.setEnabled(false);
		}else{
			deleteOperations.setEnabled(value);
		}
    	
	}
    
    /**
     * Method to set visibility of New Analytics Operation button
     */
	@Override
    public void  setEnabledAddButton(boolean value) {
    	addAnalytics.setEnabled(value);
	}
    
    /**
     * Method to set visibility of Copy Analytics Operation button
     */
	@Override
    public void  setEnabledCopyButton(boolean value) {
    	copyOperations.setEnabled(value);
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
		operationsList.clear();
		disableCopyEditDeleteButtons();
		setEnabledAddButton(true);
    }

    @Override
   	public TextBox getSearchText() {
   		return searchQueryInput;
   	}

    @Override
    public HasData<AnalyticsOperationProxy> getAnalyticsTable() {
        return analyticsTableData;
    }

    @Override
    public int getColumnIndex(Column<AnalyticsOperationProxy, ?> column) {
    	return this.analyticsTableData.getColumnIndex(column);
    }
    
	@Override
	public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler) {
		return analyticsTableData.addColumnSortHandler(handler);  
	}

    @Override
	public CellTable<AnalyticsOperationProxy> getAnalyticsTableData() {
		return analyticsTableData;
	}
    /**
     * Method to set visibility of  Edit Analytics Operations button
     */
	@Override
    public void  setEnabledEditButton(boolean enable) {
		editOperations.setEnabled(enable);
	}
	
	public void disableCopyEditDeleteButtons() {
		setEnabledCopyButton(false);
		setEnabledEditButton(false);
    	setEnabledDeleteButton(false);
	}
	
}
