package com.simple.original.client.view.desktop;

import java.util.logging.Logger;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.inject.Inject;
import com.simple.original.api.domain.GroupMembership;
import com.simple.original.api.domain.RecordFecthType;
import com.simple.original.client.proxy.AnalyticsTaskMonitorProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IAlertDefinitionView;
import com.simple.original.client.view.widgets.ErrorPanel;

public class AlertDefinitionView extends AbstractView implements IAlertDefinitionView {

	/**
     * This is a simple cell that allows you to link using an a tag.
     * 
     * @author chinshaw
     * 
     * @param <C>
     */
	public static class LinkCell<C> extends ActionCell<C> {

		public LinkCell(SafeHtml message, Delegate<C> delegate) {
            super(message, delegate);
        }

    }

	/**
     * This is the uibinder and it will use the view.DefaultView.ui.xml
     * template.
     */
	@UiTemplate("AlertDefinitionView.ui.xml")
	public interface Binder extends UiBinder<Widget, AlertDefinitionView> {
    }

    /**
     * Page size for the number of alerts showing on table.
     */
	private final int PAGE_SIZE = 15;
	
	/**
     * Text size to enable CellPreviewHandler.
     */	
	private final int TEXT_SIZE = 80;
	
	/**
	 * PagerResources
	 */
	SimplePager.Resources pagerResources = 
	    		GWT.create(SimplePager.Resources.class);
	/**
	 * FastforwardRows size to the simplepager constructor
	 */
	private static int DEFAULT_FAST_FORWARD_ROWS = 1000;
	
    /**
     * Add alert button.
     */
    @UiField
    Button newAlert;

    /**
     * Used to delete selected reports.
     */
    @UiField
    Button deleteSelectedAlerts;


	/**
     * Error panel for displaying errors.
     */
    @UiField
    ErrorPanel errorPanel;

    /**
     * Table containing the list of alerts.
     */
    @UiField
    CellTable<AnalyticsTaskMonitorProxy> alertsTable;

    
    /**
     * Pagination for displaying the list of alerts.
     */
    @UiField
    SimplePager pager;
    
    /**
     * Textbox for displaying the list of alerts based on search.
     */
    @UiField
    TextBox searchQueryInput;
    
    /**
     * button to list alerts based on search Query.
     */
    @UiField
    Button searchAlerts;
    
    @UiField
    ListBox recordList;
    
    /**
     * Edit alert button.
     */
    @UiField
    Button editAlert;

    /**
     * Used to Notify selected reports.
     */
    @UiField
    Button notifyAlerts;
    
    /**
     * Presenter is an interface for AlertDefintionView
     */
    private Presenter presenter;
    
    /** Constants to use for displaying celltable column and button names*/
    private static final String SELECT = "Select";
    private static final String ALERT_NAME = "Alert Name";
    private static final String ALERT_DESC = "Alert Description";
    private static final String OWNER = "Owner";
    
    static final String appendText = " Alerts ";

    GroupMembership userGroup =  null;
    /**
     * AlertDefinitionViewImpl implementation method
     * @param resources
     */
    @Inject
    public AlertDefinitionView(Resources resources) {    	
    	super(resources);
    	pager = new SimplePager();
        initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
        setupSimplePager();
        
		deleteSelectedAlerts.setEnabled(false);
		alertDescriptionHandler();
    }
    
    @Override
    public void setUserView(GroupMembership group){
    	this.userGroup = group;
    	if(userGroup == GroupMembership.ADMINISTRATOR ){
            for(RecordFecthType type : RecordFecthType.values()){
            	recordList.addItem(type.getDescription()+appendText, type.name());
            }
    		recordList.setSelectedIndex(RecordFecthType.ALL_RECORDS.ordinal());
    		recordList.setVisible(true);
        }else{
        	recordList.setVisible(false);
        }
    	disableButtons();
    	adminListChangeHandler();
    }
    
       
    private void adminListChangeHandler(){
		recordList.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
            	disableButtons();
            	setupSimplePager();
            	searchQueryInput.setText("");
               presenter.onAdminListSelected(recordList.getValue(recordList.getSelectedIndex()));
            }
        });
    }
    
    /**
     * Mouse over event Listener for alertDescription field
     */
    public void alertDescriptionHandler(){
        alertsTable.addCellPreviewHandler(new Handler<AnalyticsTaskMonitorProxy>(){
			PopupPanel popupPanel = new PopupPanel(true);
			TextArea textArea= new TextArea();
			@Override
			public void onCellPreview(CellPreviewEvent<AnalyticsTaskMonitorProxy> event) {
				if(event.getColumn()==2 ){
		            if ("mouseover".equals(event.getNativeEvent().getType())) {
		            	String message = alertsTable.getVisibleItem(event.getIndex()-alertsTable.getPageStart()).getDescription();
		            	if(message != null && message.length()> TEXT_SIZE) {
		            		textArea.setVisible(true);
	//		            	textArea.setHeight("4em");
			            	textArea.setWidth("30em");
			            	textArea.setText(message);
			            	popupPanel.add(textArea);
				            Widget source = (Widget) event.getSource(); 
				            int x = source.getAbsoluteLeft() +event.getColumn()*100;
			 		        int y = source.getAbsoluteTop() +(event.getIndex()-alertsTable.getPageStart()+1)* 33; 
			 		            
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
     * This method is called in AlertDefinitionViewImpl for displaying of pagination
     */
    public void setupSimplePager() {
    	pager.setDisplay(alertsTable);
    }
    
    @Override
    public ErrorPanel getErrorPanel() {
        return errorPanel;
    }

    /**
     * UiFactory constructor for the simplepager.
     * 
     * @return
     */
    @UiFactory
    SimplePager createSimplePager() {
    	//Constructor changed for enabling the lastpage icon in pagination
    	final SimplePager alertsPager = new SimplePager(TextLocation.LEFT, pagerResources, false, DEFAULT_FAST_FORWARD_ROWS, true);
        return alertsPager;
    }

    /**
     * This is used to draw the cell table.
     * 
     * @return CellTable for reports
     */
    @UiFactory
    CellTable<AnalyticsTaskMonitorProxy> createTable() {
        final CellTable<AnalyticsTaskMonitorProxy> alertsTable = new CellTable<AnalyticsTaskMonitorProxy>(PAGE_SIZE, getResources());
        alertsTable.setEmptyTableWidget(new Label("No Alerts found")) ;
        
        //check box cell for selecting of alerts
        CheckboxCell cb = new CheckboxCell(true, true);
        Column<AnalyticsTaskMonitorProxy, Boolean> selectorColumn = new Column<AnalyticsTaskMonitorProxy, Boolean>(cb) {

            @Override
            public Boolean getValue(AnalyticsTaskMonitorProxy alert) {
                return alertsTable.getSelectionModel().isSelected(alert);
            }
        };
        alertsTable.setColumnWidth(selectorColumn, "5em");
        selectorColumn.setFieldUpdater(new FieldUpdater<AnalyticsTaskMonitorProxy, Boolean>() {
            public void update(int index, AnalyticsTaskMonitorProxy object, Boolean value) {
                Logger.getLogger("AlertDefinitionViewImpl").info("Index is " + index);             
				presenter.onCheckBoxClicked(object,value);
                alertsTable.getSelectionModel().setSelected(object, value);
            }
        });

        selectorColumn.setSortable(false);
        alertsTable.addColumn(selectorColumn,SELECT);


        // alert name column
        Column<AnalyticsTaskMonitorProxy, String> nameColumn = new Column<AnalyticsTaskMonitorProxy, String>(new TextCell()) {

            @Override
            public String getValue(AnalyticsTaskMonitorProxy alert) {
                return alert.getName();
            }
        };
        
        nameColumn.setSortable(true);
        alertsTable.addColumn(nameColumn, ALERT_NAME);

        // alert description column
        Column<AnalyticsTaskMonitorProxy, String> alertDesc = new Column<AnalyticsTaskMonitorProxy, String>(new TextCell()) {
        	
            @Override
            public String getValue(AnalyticsTaskMonitorProxy alert) {
            	if(alert.getDescription() != null && alert.getDescription().length() > TEXT_SIZE){
            		return alert.getDescription().substring(0, TEXT_SIZE);
            	}
            	return alert.getDescription();
            }
        };

        alertDesc.setSortable(true);
        alertsTable.addColumn(alertDesc, ALERT_DESC);

        Column<AnalyticsTaskMonitorProxy, String> ownerColumn = new Column<AnalyticsTaskMonitorProxy, String>(new TextCell()) {        	
            @Override
            public String getValue(AnalyticsTaskMonitorProxy alert) {
            	if(alert.isPublic()){
            		return "Public";
            	}else{
            		return  (alert.getOwner() != null) ? alert.getOwner().getName() : "Unknown User";
            	}
            }
        };
        ownerColumn.setSortable(true);
        alertsTable.addColumn(ownerColumn, OWNER);

        return alertsTable;
    }



    /**
     * Return the alerts CellTable.
     */
    @Override
    public CellTable<AnalyticsTaskMonitorProxy> getAlertsTable() {
    	alertsTable.setEmptyTableWidget(new Label("No alerts found"));
        return alertsTable;
    }

    /**
     * Function used to reset the display. Clear out any handlers and such.
     */
    @Override
    public void reset() {
    	errorPanel.clear();
    	recordList.clear();
    	disableButtons();
    	searchQueryInput.setText("");
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Event fired onclick of searchAlerts button
     */
    @UiHandler("searchAlerts")
    void searchAlerts(ClickEvent clickEvent) {
    	disableButtons();
    	searchQueryInput.setText(searchQueryInput.getText().trim());
		alertsTable.setVisibleRange(0, PAGE_SIZE);
        alertsTable.setVisibleRangeAndClearData(alertsTable.getVisibleRange(), true);
    }
    
    /**
     * Event fired onclick of deleteSelectedAlerts button
     */
    @UiHandler("deleteSelectedAlerts")
    void onDeleteReports(ClickEvent clickEvent) {
        presenter.onDeleteAlerts();
    }

    /**
     * Event fired onclick of newAlert button
     */
    @UiHandler("newAlert")
    void onNewReport(ClickEvent clickEvent) {
        presenter.onNewAlert();
    }
    
    /**
     * Event fired onclick of newAlert button
     */
    @UiHandler("editAlert")
    void onEditReport(ClickEvent clickEvent) {
        presenter.onEditAlert();
    }
    
    /**
     * Event fired onclick of newAlert button
     */
    @UiHandler("notifyAlerts")
    void onNotifyReport(ClickEvent clickEvent) {
        presenter.onNotifyAlert();
    }
    
 
    /**
     * getter for deleteSelectedAlerts button
     */
    public Button getSelectedAlertsToDelete() {
 		return deleteSelectedAlerts;
 	}

    /**
     * setter for deleteSelectedAlerts button
     */
 	public void setSelectedAlertsToDelete(Button deleteSelectedAlerts) {
 		this.deleteSelectedAlerts = deleteSelectedAlerts;
 	}

 	/**
     * getter for searchQueryInput textbox
     */
	public TextBox getSearchQueryInput() {
		return searchQueryInput;
	}
 	 	
	/**
     * Method for implementing sort functionality
     */
    public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler) {
    	return alertsTable.addColumnSortHandler(handler);        
    }
    
    /**
     * Method for getting the column index
     */
    public int getColumnIndex(Column<AnalyticsTaskMonitorProxy, ?> column) {
    	return this.alertsTable.getColumnIndex(column);
    }
	
    
    @Override
	public void setEnabledDeleteButton(boolean value) {		
			deleteSelectedAlerts.setEnabled(value);				
	}
       

	@Override
	public void setEnabledNotifyButton(boolean value) {
		if(userGroup !=  null && userGroup == GroupMembership.USER ){
			notifyAlerts.setEnabled(false);
		}else{
			notifyAlerts.setEnabled(value);
		}
	}

	@Override
	public void setEnabledEditButton(boolean value) {
		editAlert.setEnabled(value);
	}
	
	public void disableButtons(){
		setEnabledDeleteButton(false);
    	setEnabledEditButton(false);
    	setEnabledNotifyButton(false);
	}


}
