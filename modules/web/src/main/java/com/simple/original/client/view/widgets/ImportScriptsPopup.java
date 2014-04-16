package com.simple.original.client.view.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ImportScriptsPopup extends PopupPanel {
	
	private final FormPanel form = new FormPanel();
	private final FileUpload upload = new FileUpload();
	
	public ImportScriptsPopup() {
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.setAction(GWT.getHostPageBaseURL() + "/analyticsTaskRestore");

		VerticalPanel panel = new VerticalPanel();
		panel.setHeight("60px");
		panel.setWidth("100px");
		panel.setBorderWidth(1);

		upload.setName("uploadFileElement");

		panel.add(new Label("Import scripts from file"));
		panel.add(upload);

		Button submit = new Button("submit");
		submit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				form.submit();
			}
		});

		panel.add(submit);
		form.add(panel);

		form.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {

			}

		});
		
		setGlassEnabled(true);
		setAutoHideEnabled(true);
		add(form);
	}	
}
