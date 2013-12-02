package com.simple.original.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.simple.original.client.view.widgets.ErrorPanel;

public interface IEmailNotificationView extends IView {
    public interface Presenter {
    }
    void setPresenter(Presenter presenter);

	HasWidgets getContentPanel();

	TextBox getSubject();
	
	TextArea getAlertSubscriptionList();
	
	TextArea getDescription();
	
	Button getSendNotification();
	
	HasClickHandlers getCancelNotification();
	
	ErrorPanel getErrorPanel();

	void setCancelEnabled(boolean b);

}
