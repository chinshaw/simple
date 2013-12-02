package com.simple.original.client.view;

import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.simple.original.client.proxy.AlertViolationDataProxy;
public interface IAlertDetailsView extends IView {

	public interface Presenter {
		
	}

	public CellTable<AlertViolationDataProxy> getAlertDetailsTable();

	void setPresenter(Presenter presenter);

	/**
	 * method is called to add charts to flowPanel to display by taking list of chart paths.
	 * @param response
	 */
	void setImage(List<String> response);

	/**
	 * uset to set status of an alert in the textBox.
	 * @param resolution
	 */
	void setResolutionTextBox(String resolution);
}
