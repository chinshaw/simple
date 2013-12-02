package com.simple.original.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IEmailNotificationView;
import com.simple.original.client.view.widgets.ErrorPanel;

public class EmailNotificationView extends AbstractView implements IEmailNotificationView {
	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	@UiTemplate("EmailNotification.ui.xml")
	public interface Binder extends UiBinder<Widget, EmailNotificationView> {
	}

	/**
	 * Error panel for displaying errors.
	 */
	@UiField
	ErrorPanel errorPanel;

	/**
	 * Content Panel.
	 */
	@UiField
	FlowPanel contentPanel;

	/**
	 * TextBox.
	 */
	@UiField
	TextBox alertName;

	/**
	 * TextArea.
	 */
	@UiField
	TextArea alertSubscriptionList;

	/**
	 * TextArea.
	 */
	@UiField
	TextArea messageBody;

	/**
	 * Button
	 */
	@UiField
	Button send;

	/**
	 * Button
	 */
	@UiField
	Button cancel;

	/**
	 * This is used to draw the Smart table.
	 */
	@Inject
	public EmailNotificationView(Resources resources) {
		super(resources);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		send.setEnabled(false);
	}

	/**
	 * Function used to reset the display. Clear out any handlers and such.
	 */
	@Override
	public void reset() {
		errorPanel.clear();
		send.setEnabled(false);
	}

	/**
	 * getter for displaying error message in errorpanel
	 */
	@Override
	public ErrorPanel getErrorPanel() {
		return errorPanel;
	}

	/**
	 * setter for presenter
	 */
	@Override
	public void setPresenter(Presenter presenter) {
	}

	/**
	 * getter for displaying contentpanel
	 */
	@Override
	public HasWidgets getContentPanel() {
		return contentPanel;
	}

	/**
	 * getter for displaying subject
	 */
	@Override
	public TextBox getSubject() {
		return alertName;
	}

	/**
	 * getter for displaying alertSubscriptionList
	 */
	@Override
	public TextArea getAlertSubscriptionList() {
		return alertSubscriptionList;
	}

	/**
	 * getter for displaying messageBody
	 */
	@Override
	public TextArea getDescription() {
		return messageBody;
	}

	/**
	 * getter for send button
	 */
	@Override
	public Button getSendNotification() {
		return send;
	}

	/**
	 * getter for cancel button
	 */
	@Override
	public HasClickHandlers getCancelNotification() {
		return cancel;
	}

	@Override
	public void setCancelEnabled(boolean enableFlag) {
		// TODO Auto-generated method stub
		cancel.setEnabled(enableFlag);
	}
}
