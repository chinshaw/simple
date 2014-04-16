package com.simple.original.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.DataGrid;
import com.simple.original.client.dashboard.AbstractDashboardWidget;
import com.simple.original.client.dashboard.designer.WidgetPropertiesPanel;
import com.simple.original.client.view.widgets.ProgressWidget;

public interface Resources extends ClientBundle, CellTable.Resources,
		CellList.Resources, DataGrid.Resources {

	public static final Resources INSTANCE = GWT.create(Resources.class);

	public interface Style extends CssResource, ProgressWidget.Style {
		
		public String add();

		public String buttonCollection();

		public String contentContainer();

		public String content();

		public String contentBlock();

		public String dashboardExampleTable();
		
		public String dateSlider();
		
		public String delete();

		public String draggableLabel();
		
		public String dropDownTabPanelContainer();

		public String dropDownTabPanel();

		public String dropDownTabPanelTab();

		public String dropDownTabPanelTabInner();

		public String dropDownTabPanelTabSelected();

		public String dropDownTabPanelTabInnerLabel();

		public String dropDownTabPanelTabBar();
		

		/**
		 * This is the style for the Analytics Task Editor Defined Inputs.
		 * 
		 * @return
		 */
		public String definedInputsContainer();

		public String errorMessage();

		public String errorPanel();


		public String gaugeWidget();

		public String gaugeLabel();

		public String header();
		
		public String search();
		
		public String tableSearch();

		public String textButton();

		public String topPanel();
		
		/**
		 * This is the header at the top just below the title menu bar.
		 * @return
		 */
		public String topHeader();
		
		public String updating();

		public String updatingImage();


		public String modalCloseButton();

		public String notificationPanel();

		public String loggingPanel();

		public String loggingMessages();


		public String spinBox();

		public String staticPlotWidget();

		public String titleTextCell();

		public String widgetSelectorPanel();

		public String linkableWidget();

		public String constraintErrorLabel();

		public String violationInfo();

		public String title();

		public String toolTip();



	

		public String historySliderPanelDropDownLabel();



		public String widgetInspector();


		public String popupContextMenu();

		public String actionPanel();

		public String actionPanelButton();

		public String navBox();

		/**
		 * This is an element that has the ability to be selected.
		 * 
		 * @return
		 */
		public String selectionContext();

		public String designerGaugeWidget();

		public String modalDialogBox();

		public String selectorWidget();

		public String widgetsPanel();

		public String designer();

		public String dndPlaceHolder();

		public String splitLayoutPanel();

		/**
		 * This is the link for the {@link WidgetPropertiesPanel}
		 * 
		 * @return
		 */
		public String widgetPropertiesPanel();

		/**
		 * Style for class {@link AbstractDashboardWidget}
		 * 
		 * @return
		 */
		public String abstractDashboardWidget();

		public String iconCell();
	}

	/**
	 * These are the obfuscated styles.
	 * 
	 * @return
	 */
	@NotStrict
	@Source({ "default.css" })
	public Style style();

	@Source("cellListStyle.css")
	public CellList.Style cellListStyle();

	@Source("images/alertSmall.png")
	public ImageResource alertSmall();

	/**
	 * The background used for selected items.
	 */
	@Source("images/alertSmall.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
	ImageResource cellListSelectedBackground();

	/**
	 * Panel open icon.
	 * 
	 * <br />
	 * <b>Example</b> <br />
	 * <img src="images/expandablePanelOpen.gif" />
	 * 
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
	
	@ImageOptions()
	@Source("images/down_arrow_select.jpg")
	public ImageResource downArrowSelect();

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
	 * <b>Example</b> <br />
	 * <img src="images/modalClose.png" />
	 * 
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
	
	@Source("images/person_icon_small.png")
	ImageResource personIcon();

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
	 * 
	 * @return ImageResource
	 */
	@Source("images/full_screen.png")
	ImageResource fullScreen();

	/**
	 * Edit properties icon
	 * 
	 * <br />
	 * Example <img src="images/edit_properties_16.png" />
	 * 
	 * @return ImageResource
	 */
	@Source("images/edit_properties_16.png")
	ImageResource editProperties16();

	/**
	 * Edit properties icon
	 * 
	 * <br />
	 * Example <img src="images/edit_properties_32.png" />
	 * 
	 * @return ImageResource
	 */
	@Source("images/edit_properties_32.png")
	ImageResource editProperties32();

	/**
	 * Delete Icon
	 * 
	 * <br />
	 * <b>Example</b> <br />
	 * <img src="images/delete.png" />
	 * 
	 * @return ImageResource
	 */
	@Source("images/delete.png")
	public ImageResource delete();

	/**
	 * Edit properties icon
	 * 
	 * <br />
	 * Example <img src="images/edit_properties_16.png" />
	 * 
	 * @return ImageResource
	 */
	@Source("images/delete_16.png")
	ImageResource delete16();

	/**
	 * Edit properties icon
	 * 
	 * <br />
	 * Example <img src="images/delete.png" />
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
	 * @return ImageResource
	 */
	@Source("images/timepicker_PM_small.png")
	ImageResource timePickerPMSmall();

	/**
	 * Warning image. Size will be 15x15
	 * 
	 * Example <br />
	 * <img src="images/red-x.png" />
	 * 
	 * @return
	 */
	@Source("images/critical.png")
	ImageResource widgetViolationSmallIcon();
}
