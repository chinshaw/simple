package com.simple.original.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.DataGrid;
import com.simple.original.client.view.widgets.ProgressWidget;



public interface Resources extends ClientBundle, CellTable.Resources, CellList.Resources, DataGrid.Resources {
    
	public static final Resources INSTANCE = GWT.create(Resources.class);
	
	
    public interface Style extends CssResource, ProgressWidget.Style {

        public String button();
        
        public String buttonCollection();

        public String contentPanel();

        public String menuPanel();

        public String header();

        public String updatingImage();

        public String content();

        public String tabbedPanel();

        public String tabbedPanelLabel();

        public String tableSearch();

        public String textButton();

        public String topPanel();

        public String updating();
        
    	public String dashboardExampleTable();

        /**
         * This is the style for the Analytics Task Editor Defined Inputs.
         * 
         * @return
         */
        public String definedInputsContainer();
        
        public String errorMessage();
        
        public String errorPanel();
        
        public String modalCloseButton();

        public String notificationPanel();

        public String loggingPanel();
        
        public String menuItem();

        public String loggingMessages();

        public String alertNotificationPanel();

        public String alertNotificationLabel();

        public String alertNotificationCloseButton();
        
        public String spinBox();

        public String widgetSelectorPanel();

        public String titleTextCell();

        public String gaugeWidget();
        
        public String gaugeLabel();
        
        public String topHeader();
        
		public String linkableWidget();

        public String constraintErrorLabel();

        public String loginView();
        
		public String violationInfo();

		public String toolTip();

		public String staticPlotWidget();

		public String dateSlider();

		public String historySliderPanelDropDownLabel();

		public String dropDownTabPanelContainer();
		
		public String dropDownTabPanel();

		public String dropDownTabPanelTab();
		
		public String dropDownTabPanelTabInner();
		
		public String dropDownTabPanelTabSelected();

		public String dropDownTabPanelTabInnerLabel();

		public String dropDownTabPanelTabBar();

		public String widgetInspector();

		public String draggableLabel();

		public String popupContextMenu();

        public String actionPanel();

        public String actionPanelButton();

        /**
         * This is an element that has the ability to be selected.
         * @return
         */
        public String selectionContext();

        public String designerGaugeWidget();

		public String modalDialogBox();

		public String dropMenu();
		
		public String dropMenuPopup();
		
		public String dropMenuHorizontal();
		
		public String dropMenuVertical();
		
		public String submitBtn();

		public String selectorWidget();
		
		public String widgetsPanel();
		
		public String designer();

		public String placeHolder();

		public String widgetPropertiesPanel();
		
    }


    /**
     * These are the obfuscated styles.
     * 
     * @return
     */
    @Source({"default.css"})
    public Style style();

    @Source("images/alertSmall.png")
    public ImageResource alertSmall();


    /**
     * Panel open icon.
     * 
     * <br />
     * <b>Example</b>
     * <br />
     * <img src="images/expandablePanelOpen.gif" />
     * @return ImageResource 
     */
    @Source("images/expandablePanelOpen.gif")
    public ImageResource expandablePanelOpen();

    @Source("images/expandablePanelClosed.gif")
    public ImageResource expandablePanelClosed();

    @Source("images/updating.gif")
    public ImageResource updating();

    @Source("images/loading.gif")
    public ImageResource loading();

    @Source("images/calendarIcon.gif")
    public ImageResource calendarIcon();

    @Source("images/configuration.png")
    @ImageOptions(height = 30, width = 30)
    public ImageResource configuration();

    @Source("images/configuration.png")
    @ImageOptions(height = 18, width = 18)
    public ImageResource configurationSmall();

    @ImageOptions(height = 15, width = 15)
    @Source("images/critical_small.png")
    public ImageResource iconStatusCritical();

    @ImageOptions(height = 15, width = 15)
    @Source("images/warning_small.png")
    public ImageResource iconStatusMajor();

    @ImageOptions(height = 15, width = 15)
    @Source("images/minor_small.png")
    public ImageResource iconStatusMinor();

    
    @Source("images/curvedArrow.png")
    @ImageOptions(height = 25, width = 25)
    public ImageResource curvedArrow();

    @Source("images/up.png")
    public ImageResource up();

    @Source("images/down.png")
    public ImageResource down();

    @Source("images/plus.png")
    public ImageResource plus();

    @Source("images/plus.png")
    @ImageOptions(height = 16, width = 16)
    public ImageResource plusSmall();

    @Source("images/minus.png")
    public ImageResource minus();

    /**
     * Close icon for modal boxes, like popup panels
     * 
     * <br />
     * <b>Example</b>
     * <br />
     * <img src="images/modalClose.png" />
     * @return ImageResource 
     */
    @Source("images/modalClose.png")
    public ImageResource modalClose();

    @Source("images/minus.png")
    @ImageOptions(height = 16, width = 16)
    public ImageResource minusSmall();

    @Source("images/gauge-selector-bw.png")
    ImageResource gaugeWidgetSelector();
    
    @Source("images/table-selector-bw.png")
    ImageResource tableWidgetSelector();
    
    @Source("images/panel.jpg")
    ImageResource panelWidgetSelector();
    
    @Source("images/minusCircle.png")
    ImageResource minusCircle();
    
    @Source("images/curved-arrow.png")
    ImageResource largeCurvedArrow();
    
    @Source("images/widgetSelectorChart-icon.png")
    ImageResource widgetSelectorChartIcon();
    
    @Source("images/chart_bar.png")
    ImageResource barChartIcon();

   	@Source("images/up1.png")
    ImageResource spinBoxUp();

    @Source("images/down1.png")
    ImageResource spinBoxDown();

    @Source("images/timepicker_AM.png")
	ImageResource timePickerAM();

    @Source("images/timepicker_PM.png")
	ImageResource timePickerPM();

    @Source("images/timepicker_AM_small.png")
	ImageResource timePickerAMSmall();
    
    
    /**
     * Full screen icon.
     * 
     * <br />
     * Example
     * 
     * <img src="images/full_screen.png" />
     * @return ImageResource 
     */
    @Source("images/full_screen.png")
    ImageResource fullScreen();
    
    /**
     * Edit properties icon
     * 
     * <br />
     * Example
     * <img src="images/edit_properties_16.png" />
     * @return ImageResource 
     */
    @Source("images/edit_properties_16.png")
    ImageResource editProperties16();
    
    /**
     * Edit properties icon
     * 
     * <br />
     * Example
     * <img src="images/edit_properties_32.png" />
     * @return ImageResource 
     */
    @Source("images/edit_properties_32.png")
    ImageResource editProperties32();
    
    /**
     * Delete Icon
     * 
     * <br />
     * <b>Example</b>
     * <br />
     * <img src="images/delete.png" />
     * @return ImageResource 
     */
    @Source("images/delete.png")
    public ImageResource delete();
    
    /**
     * Edit properties icon
     * 
     * <br />
     * Example
     * <img src="images/edit_properties_16.png" />
     * @return ImageResource 
     */
    @Source("images/delete_16.png")
    ImageResource delete16();
    
    /**
     * Edit properties icon
     * 
     * <br />
     * Example
     * <img src="images/delete.png" />
     * @return ImageResource 
     */
    @Source("images/delete_32.png")
    ImageResource delete32();
    
    /**
     * Small green indicator button
     * 
     * <br />
     * Example
     * 
     * <img src="images/GreenButton_Small.png" />
     * @return ImageResource 
     */
    @Source("images/GreenButton_Small.png")
    ImageResource greenButtonSmall();
    
    /**
     * Small red indicator button
     * 
     * <br />
     * Example
     * 
     * <img src="images/RedButton_Small.png" />
     * @return ImageResource 
     */
    @Source("images/RedButton_Small.png")
    ImageResource redButtonSmall();
    
    /**
     * Small yellow indicator button
     * 
     * <br />
     * Example
     * 
     * <img src="images/YellowButton_Small.png" />
     * @return ImageResource 
     */
    @Source("images/YellowButton_Small.png")
    ImageResource yellowButtonSmall();

    /**
     * Time Picker image for simple calendar
     * 
     * <br />
     * Example
     * 
     * <img src="images/timepicker_PM_small.png" />
     * @return ImageResource 
     */
    @Source("images/timepicker_PM_small.png")
	ImageResource timePickerPMSmall();
    
    /**
     * Warning image. Size will be 15x15
     * 
     * Example
     * <br />
     * <img src="images/red-x.png" />
     * @return
     */
    @Source("images/critical.png")
    ImageResource widgetViolationSmallIcon();
}
