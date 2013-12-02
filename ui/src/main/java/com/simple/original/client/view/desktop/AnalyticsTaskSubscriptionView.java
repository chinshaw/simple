package com.simple.original.client.view.desktop;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.adapters.HasDataEditor;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IAnalyticsTaskSubscriptionView;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * @author hemantha
 *
 */
public class AnalyticsTaskSubscriptionView extends AbstractView implements IAnalyticsTaskSubscriptionView, Editor<AnalyticsTaskProxy> {

	/**
	 * Reporting task editor driver.
	 */
	interface ReportingTaskEditorDriver extends RequestFactoryEditorDriver<AnalyticsTaskProxy, AnalyticsTaskSubscriptionView> {
	}
	
	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	@UiTemplate("ReportSubscriptionView.ui.xml")
	public interface Binder extends	UiBinder<Widget, AnalyticsTaskSubscriptionView> {
	}
	
	@UiField
	HTMLPanel reportSubscriptionPanel;
	
	
	/**
	 * Name of the Reporting task
	 */
	@UiField
	TextBox name;
	
	/**
	 * TextArea for Reporting task Description
	 */
	@UiField
	TextArea description;
	
	/**
	 * CellList used to show all the users mailids
	 */
	@Ignore
	@UiField(provided = true)
	CellList<PersonProxy> usersList = new CellList<PersonProxy>(
			new AbstractCell<PersonProxy>() {
				@Override
				public void render(
						com.google.gwt.cell.client.Cell.Context context,
						PersonProxy value, SafeHtmlBuilder sb) {
					AbstractCell<String> c = new EditTextCell();
					c.render(context, value.getEmail(), sb);
				}
	
			});
	
	/**
	 * Button to move the selected users to subscribers list 
	 */
	@UiField
	Button addUser;
	
	/**
	 * Button to remove the selected subscribers from subscribers list to available virtual factory users list
	 */
	@UiField
	Button removeUser;
	
	/**
	 * Button to save the changes
	 */
	@UiField
	Button save;
	
	/**
	 * Button to cancel the changes to the screen
	 */
	@UiField
	Button cancel;
	
	/**
	 * ErrorPanle to show the errors
	 */
	@UiField
	ErrorPanel errorPanel;
	
	/**
	 * CellList used to show all the users
	 */
	@UiField(provided = true)
	CellList<PersonProxy> subscribersList = new CellList<PersonProxy>(
			new AbstractCell<PersonProxy>() {
				@Override
				public void render(
						com.google.gwt.cell.client.Cell.Context context,
						PersonProxy value, SafeHtmlBuilder sb) {
					AbstractCell<String> c = new EditTextCell();
					c.render(context, value.getEmail(), sb);
				}
	
			});
	
	/**
	 * ListEditor associated to subscribers CellList
	 */
	@Path("subscribers")
	ListEditor<PersonProxy, LeafValueEditor<PersonProxy>> subscribersEditor = HasDataEditor.of(subscribersList);
	
	/**
	 * Presenter.
	 */
	private Presenter presenter;
	
	/**
	 * This is the script editor driver that will allow you to edit a Reporting  task.
	 */
	private final ReportingTaskEditorDriver driver = GWT.create(ReportingTaskEditorDriver.class);
	
	/**
	 * Required default constructor
	 * @param resources
	 */
	@Inject
	public AnalyticsTaskSubscriptionView(Resources resources) {
		super(resources);
		
		usersList.setPageSize(5);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		driver.initialize(this);
		name.setEnabled(false);
		description.setEnabled(false);
		setEnabledSaveButton(false);
		setEnabledCancelButton(false);
	}
	
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@UiHandler("save")
	void onSave(ClickEvent clickEvent) {
		presenter.onSave();
	}
	
	@UiHandler("cancel")
	void onCancel(ClickEvent clickEvent) {
		presenter.onCancel();
	}
	
	@UiHandler("addUser")
	void onAddVFUsers(ClickEvent clickEvent) {
		presenter.onAddVirtualFactoryUsers();
	}
	
	@UiHandler("removeUser")
	void onRemoveUsers(ClickEvent clickEvent) {
		presenter.onRemoveUsers();
	}
	
	/* (non-Javadoc)
	 * @see com.simple.original.client.view.ReportSubscriptionView#getUsersList()
	 */
	@Override
	public CellList<PersonProxy> getUsersList() {
		return usersList;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.simple.original.client.view.desktop.ViewImpl#getErrorPanel()
	 */
    @Override
    public ErrorPanel getErrorPanel() {
        return errorPanel;
    }
	
	/* (non-Javadoc)
	 * @see com.simple.original.client.view.ReportSubscriptionView#getSubsribersList()
	 */
	@Override
	public AbstractHasData<PersonProxy> getSubsribersList() {
		return subscribersList;
	}
	
	/* (non-Javadoc)
	 * @see com.simple.original.client.view.ReportSubscriptionView#getSubscribersEditor()
	 */
	@Ignore
	@Override
	public ListEditor<PersonProxy, LeafValueEditor<PersonProxy>> getSubscribersEditor() {
		return subscribersEditor;
	}

	/* (non-Javadoc)
	 * @see com.simple.original.client.view.desktop.ViewImpl#reset()
	 */
	@Override
	public void reset() {
		errorPanel.clear();
		name.setEnabled(false);
		description.setEnabled(false);
		setEnabledSaveButton(false);
		setEnabledCancelButton(false);
	}

	@Override
	public void setEnabledSaveButton(boolean value) {
		save.setEnabled(value);
	}

	@Override
	public void setEnabledCancelButton(boolean value) {
		cancel.setEnabled(value);
	}
	
}
