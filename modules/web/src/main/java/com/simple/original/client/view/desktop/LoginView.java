package com.simple.original.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.ILoginView;
import com.simple.original.client.view.widgets.DefaultPasswordBox;
import com.simple.original.client.view.widgets.DefaultTextBox;
import com.simple.original.client.view.widgets.ErrorPanel;

public class LoginView extends AbstractView implements ILoginView {

	@UiField
	ErrorPanel errorPanel;

	@UiField
	DefaultTextBox username;

	@UiField
	DefaultPasswordBox password;

	@UiField
	Button signin;

	private Presenter presenter;

	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	@UiTemplate("LoginView.ui.xml")
	public interface Binder extends UiBinder<Widget, LoginView> {
	}

	@Inject
	public LoginView(EventBus eventBus, Resources resources) {
		super(resources);

		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

		signin.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.doAuth(username.getValue(), password.getValue());
			}
		});

		
	}

	@Override
	public ErrorPanel getErrorPanel() {
		return errorPanel;
	}

	@Override
	public void reset() {
		errorPanel.clear();
		clearFields();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void clearFields() {
		username.setText("");
		password.setText("");
	}
}
