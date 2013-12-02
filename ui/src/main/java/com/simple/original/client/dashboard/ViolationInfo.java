package com.simple.original.client.dashboard;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.simple.original.client.proxy.ViolationProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.resources.ResourcesFactory;
import com.simple.original.client.view.widgets.Tooltip;
import com.simple.original.client.view.widgets.Tooltip.TooltipPosition;

public class ViolationInfo extends Composite  {
	
	static class ViolationEditor extends AbstractCell<ViolationProxy> {
		
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, ViolationProxy value, SafeHtmlBuilder sb) {
		      sb.appendHtmlConstant("<table><tr><td>");
		      sb.appendHtmlConstant("Rule: " + ((value.getRuleName() == null) ? "N/A" : value.getRuleName()));
		      sb.appendHtmlConstant("</td><td>");
		      sb.appendEscaped("Severity: " + ((value.getSeverity() == null) ? "N/A" : value.getSeverity()) );
		      sb.appendHtmlConstant("</td><td>");
		      sb.appendEscaped("Subgroup: " + ((value.getSubgroup() == null) ? "N/A" : value.getSubgroup()) );
		      sb.appendHtmlConstant("</td><td>");
		      sb.appendEscaped("Detail: " + ((value.getDetail() == null) ? "N/A" : value.getDetail()) );
		      sb.appendHtmlConstant("</td></tr></table>");
		}
	}
	
	private FlowPanel container = new FlowPanel();
	
	//private CellList<ViolationProxy> cellList = new CellList<ViolationProxy>(new ViolationEditor());
	
	private CellTable<ViolationProxy> cellTable;
	
	private Resources resources = ResourcesFactory.getResources();
	
	public ViolationInfo() {
		initWidget(container);
		cellTable = new CellTable<ViolationProxy>();
		
		container.setStyleName(resources.style().violationInfo());
		container.setVisible(false);
		
		Image img = new Image(resources.widgetViolationSmallIcon());
		container.add(img);
		
		
		Tooltip tt = new Tooltip(cellTable);
		tt.setPosition(TooltipPosition.RIGHT_TOP);
		tt.attachTo(img);
		
		initTable();
	}
	
	private void initTable() {
		//Column<ViolationProxy, String> col = new Column<ViolationProxy, String>(new TextCell()
		
		cellTable.addColumn(new Column<ViolationProxy, String>(new TextCell()) {

			@Override
			public String getValue(ViolationProxy violation) {
				return (violation.getRuleName() == null) ? "N/A" : "" + violation.getRuleName();
			}
			
		}, "Rule");
		
		cellTable.addColumn(new Column<ViolationProxy, String>(new TextCell()) {

			@Override
			public String getValue(ViolationProxy violation) {
				return (violation.getSeverity() == null) ? "N/A" : "" + violation.getSeverity();
			}
			
		}, "Severity");
		
		cellTable.addColumn(new Column<ViolationProxy, String>(new TextCell()) {

			@Override
			public String getValue(ViolationProxy violation) {
				return (violation.getSubgroup() == null) ? "N/A" : "" + violation.getSubgroup();
			}
			
		}, "SubGroup");
		cellTable.addColumn(new Column<ViolationProxy, String>(new TextCell()) {

			@Override
			public String getValue(ViolationProxy violation) {
				return (violation.getDetail() == null) ? "N/A" : "" + violation.getDetail();
			}
			
		}, "Detail");
	}
	
	public void setViolations(List<ViolationProxy> violations) {
		if (violations != null) {
			cellTable.setRowData(violations);
			
			if (violations.size() > 0) {
				container.setVisible(true);
			}
		}
	}
}