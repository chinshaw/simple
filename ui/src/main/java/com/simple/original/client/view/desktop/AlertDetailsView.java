package com.simple.original.client.view.desktop;

import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.simple.original.client.proxy.AlertViolationDataProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IAlertDetailsView;
import com.simple.original.client.view.widgets.ErrorPanel;

public class AlertDetailsView extends AbstractView implements IAlertDetailsView {

	private int PAGE_SIZE = 25;

	/**
	 * Error panel for displaying errors.
	 */
	@UiField
	ErrorPanel errorPanel;

	/**
	 * Table containing the list of scripts.
	 */
	@UiField
	CellTable<AlertViolationDataProxy> alertDetailsTable;

	/**
	 * TextBox for displaying Alert Status.
	 */
	@UiField
	TextBox resolutionTextBox;

	/**
	 * FlowPanel for adding Charts to display.
	 */
	@UiField
	FlowPanel addImage;

	private Presenter presenter;

	@Inject
	public AlertDetailsView(Resources resources) {
		super(resources);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
	}

	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	@UiTemplate("AlertDetailsView.ui.xml")
	public interface Binder extends UiBinder<Widget, AlertDetailsView> {

	}

	@Override
	protected ErrorPanel getErrorPanel() {
		return errorPanel;
	}

	@UiFactory
	CellTable<AlertViolationDataProxy> createTable() {
		final CellTable<AlertViolationDataProxy> alertDetailsTable = new CellTable<AlertViolationDataProxy>(PAGE_SIZE, getResources());

		Column<AlertViolationDataProxy, String> nameColumn = new Column<AlertViolationDataProxy, String>(new TextCell()) {

			@Override
			public String getValue(AlertViolationDataProxy alertDetail) {
				return alertDetail.getRuleName();
			}
		};
		alertDetailsTable.addColumn(nameColumn, "Rule Name");

		Column<AlertViolationDataProxy, String> chartStatistics = new Column<AlertViolationDataProxy, String>(new TextCell()) {

			@Override
			public String getValue(AlertViolationDataProxy alertDetail) {
				return alertDetail.getChartStatistics();
			}
		};
		alertDetailsTable.addColumn(chartStatistics, "Chart Statistics");

		Column<AlertViolationDataProxy, String> startDateColumn = new Column<AlertViolationDataProxy, String>(new TextCell()) {

			@Override
			public String getValue(AlertViolationDataProxy alertDetail) {
				return alertDetail.getStartDate();
			}
		};

		alertDetailsTable.addColumn(startDateColumn, "Start Time");

		Column<AlertViolationDataProxy, String> center = new Column<AlertViolationDataProxy, String>(new TextCell()) {

			@Override
			public String getValue(AlertViolationDataProxy alertDetail) {
				return alertDetail.getPCenter();
			}
		};
		alertDetailsTable.addColumn(center, "Center");

		Column<AlertViolationDataProxy, String> UCL = new Column<AlertViolationDataProxy, String>(new TextCell()) {

			@Override
			public String getValue(AlertViolationDataProxy alertDetail) {
				return alertDetail.getUCL();
			}
		};
		alertDetailsTable.addColumn(UCL, "UCL");

		Column<AlertViolationDataProxy, String> LCL = new Column<AlertViolationDataProxy, String>(new TextCell()) {

			@Override
			public String getValue(AlertViolationDataProxy alertDetail) {
				return alertDetail.getLCL();
			}
		};

		alertDetailsTable.addColumn(LCL, "LCL");
		return alertDetailsTable;
	}

	/**
	 * Return the CellTable.
	 */
	@Override
	public CellTable<AlertViolationDataProxy> getAlertDetailsTable() {
		alertDetailsTable.setEmptyTableWidget(new Label("No alerts found"));
		return alertDetailsTable;
	}

	@Override
	public void reset() {
		addImage.clear();
		errorPanel.clear();
	}

	@Override
	public void setImage(List<String> imagePaths) {
		for (int var1 = 0; var1 < imagePaths.size(); var1++) {
			Image ChartName1 = new Image();
			ChartName1.setUrl(imagePaths.get(var1));
			addImage.add(ChartName1);
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setResolutionTextBox(String resolution) {
		resolutionTextBox.setValue(resolution);
	}
}