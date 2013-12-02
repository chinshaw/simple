package com.simple.original.client.view.desktop;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.adapters.HasDataEditor;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;
import com.simple.original.client.proxy.AnalyticsTaskMonitorProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IAlertDefinitionEditView;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * @author nallaraj
 * @description Implementation class for AlertDefintionView
 */
public class AlertDefinitionEditView extends AbstractView implements IAlertDefinitionEditView, Editor<AnalyticsTaskMonitorProxy> {

	private static final Logger logger = Logger.getLogger(AlertDefinitionEditView.class.getName());

	/**
	 * This is my alert definition editor driver.
	 */
	interface AlertDefinitionAddDriver extends RequestFactoryEditorDriver<AnalyticsTaskMonitorProxy, AlertDefinitionEditView> {
	}

	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	@UiTemplate("AlertDefinitionEditView.ui.xml")
	public interface Binder extends UiBinder<Widget, AlertDefinitionEditView> {
	}

	@UiField
	HTMLPanel alertSubscriptionPanel;

	@Path("public")
	@UiField
	CheckBox isPublic;

	/**
	 * Name of the Alert
	 */
	@UiField
	TextBox name;

	/**
	 * TextArea for Alert Description
	 */
	@UiField
	TextArea description;

	/**
	 * CheckBox for alert Active status
	 */
	@UiField
	CheckBox alertStatus;

	/**
	 * Frequency of the analytics task
	 */
	@Ignore
	@UiField
	TextBox frequency;
	
	@UiField 
	CheckBox quixEnabled;

	/**
	 * Start time of scheduled analytics task
	 */
	@Ignore
	@UiField
	TextBox startTime;

	/**
	 * ValueListBox to show all the analytics tasks
	 */
	@Path("analyticsTask")
	@UiField(provided = true)
	ValueListBox<AnalyticsTaskProxy> analyticsTask = new ValueListBox<AnalyticsTaskProxy>(new AbstractRenderer<AnalyticsTaskProxy>() {
		@Override
		public String render(AnalyticsTaskProxy task) {
			return task == null ? "Select a task" : task.getName();
		}
	});

	/**
	 * ValueListBox with all the Outputs of the selected analytics task.
	 */
	@Path("output")
	@UiField(provided = true)
	ValueListBox<AnalyticsOperationOutputProxy> taskMetrics = new ValueListBox<AnalyticsOperationOutputProxy>(new AbstractRenderer<AnalyticsOperationOutputProxy>() {
		@Override
		public String render(AnalyticsOperationOutputProxy metric) {
			return metric == null ? "Select a Metric" : metric.getName();
		}
	});

	/**
	 * This is the description of the selected analytics task
	 */
	@Ignore
	@UiField
	TextArea taskDescription;

	@Ignore
	@UiField(provided = true)
	CellList<String> unsubscribedUserList = new CellList<String>(new AbstractCell<String>() {
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
			AbstractCell<String> c = new EditTextCell();
			c.render(context, value, sb);
		}
	});

	/**
	 * Button to move the selected users to subscribers list
	 */
	@UiField
	Button addUser;

	/**
	 * Button to remove the selected subscribers from subscribers list to
	 * available the users list
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
	CellList<PersonProxy> subscribersList = new CellList<PersonProxy>(new AbstractCell<PersonProxy>() {
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, PersonProxy value, SafeHtmlBuilder sb) {
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
	 * This is the script editor driver that will allow you to edit alert
	 * definition
	 */
	private final AlertDefinitionAddDriver driver = GWT.create(AlertDefinitionAddDriver.class);

	/**
	 * Required default constructor
	 * 
	 * @param resources
	 */
	@Inject
	public AlertDefinitionEditView(Resources resources) {
		super(resources);

		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		linkedTaskListChangeHandler();
		setVisibleAlertSubscriptionPanel(false);
		setEnabledPublic(false);
		setEnabledSaveButton(false);
		setEnabledCancelButton(false);
		
		isPublic.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.onSelectOfPublicAcessCheckBox(isPublic.getValue());
			}
		});
		driver.initialize(this);
	}

	/**
	 * Handler for Analytics task change event
	 */
	public void linkedTaskListChangeHandler() {

		analyticsTask.addValueChangeHandler(new ValueChangeHandler<AnalyticsTaskProxy>() {
			@Override
			public void onValueChange(ValueChangeEvent<AnalyticsTaskProxy> event) {
				if (event != null && event.getValue() != null) {
					taskDescription.setValue(event.getValue().getDescription());
					logger.info("selected task id is " + event.getValue().getId());
					presenter.onAnalyticsTaskSelect(event.getValue());
				} else {
					taskDescription.setValue(null);
					taskMetrics.setValue(null);
					taskMetrics.setAcceptableValues(null);
				}
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.client.view.AlertDefinitionEditView#setPresenter(com.simple.original.
	 * client.view.AlertDefinitionEditView.Presenter)
	 */
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
		presenter.onAddUnsubscribedUsers();
	}

	@UiHandler("removeUser")
	void onRemoveUsers(ClickEvent clickEvent) {
		presenter.onRemoveUsers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.AlertDefinitionEditView#getUsersList()
	 */
	@Override
	public CellList<String> getUnsubscribedUserList() {
		return unsubscribedUserList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.client.view.AlertDefinitionEditView#setTaskDescription(java
	 * .lang.String)
	 */
	@Override
	public void setTaskDescription(String description) {
		taskDescription.setValue(description);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.desktop.ViewImpl#getErrorPanel()
	 */
	@Override
	public ErrorPanel getErrorPanel() {
		return errorPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.AlertDefinitionEditView#getEditorDriver()
	 */
	@Override
	public RequestFactoryEditorDriver<AnalyticsTaskMonitorProxy, ?> getEditorDriver() {
		return driver;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.client.view.AlertDefinitionEditView#setAcceptableTasks(java
	 * .util.List)
	 */
	@Override
	public void setAcceptableTasks(List<AnalyticsTaskProxy> tasks) {
		analyticsTask.setAcceptableValues(tasks);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.AlertDefinitionEditView#getSubsribersList()
	 */
	@Override
	public AbstractHasData<PersonProxy> getSubsribersList() {
		return subscribersList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.AlertDefinitionEditView#getSubscribersEditor()
	 */
	@Ignore
	@Override
	public ListEditor<PersonProxy, LeafValueEditor<PersonProxy>> getSubscribersEditor() {
		return subscribersEditor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.desktop.ViewImpl#reset()
	 */
	@Override
	public void reset() {
		errorPanel.clear();
		setEnabledSaveButton(false);
		setEnabledCancelButton(false);
		taskDescription.setValue(null);
		//analyticsTask.setValue(null);
		//taskMetrics.setValue(null);
		//setAcceptableOutputs(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.AlertDefinitionEditView#getAnalyticsListBox()
	 */
	@Ignore
	@Override
	public ValueListBox<AnalyticsTaskProxy> getAnalyticsListBox() {
		return analyticsTask;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.AlertDefinitionEditView#getTaskDescription()
	 */
	@Ignore
	@Override
	public TextArea getTaskDescription() {
		return taskDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.client.view.AlertDefinitionEditView#getTaskMetricsListBox()
	 */
	@Ignore
	@Override
	public ValueListBox<AnalyticsOperationOutputProxy> getTaskMetricsListBox() {
		return taskMetrics;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.client.view.AlertDefinitionEditView#setAcceptableMetrics(java
	 * .util.List)
	 */
	@Override
	public void setAcceptableOutputs(List<AnalyticsOperationOutputProxy> metrics) {
		taskMetrics.setAcceptableValues(metrics);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.AlertDefinitionEditView#getSaveButton()
	 */
	@Override
	public Button getSaveButton() {
		return save;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.AlertDefinitionEditView#getAlertNameBox()
	 */
	@Ignore
	@Override
	public TextBox getAlertNameBox() {
		return name;
	}

	@Ignore
	@Override
	public void setEnabledPublic(Boolean value) {
		isPublic.setEnabled(value);
	}

	@Ignore
	@Override
	public void setVisibleAlertSubscriptionPanel(Boolean value) {
		alertSubscriptionPanel.setVisible(value);
	}

	@Ignore
	@Override
	public boolean getIsPublicValue() {
		return isPublic.getValue();
	}

	@Override
	public void setEnabledSaveButton(Boolean value) {
		save.setEnabled(value);
	}

	@Override
	public void setEnabledCancelButton(Boolean value) {
		cancel.setEnabled(value);
	}

}
