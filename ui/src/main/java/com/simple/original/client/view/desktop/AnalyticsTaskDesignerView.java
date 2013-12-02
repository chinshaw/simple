package com.simple.original.client.view.desktop;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.simple.original.api.domain.GroupMembership;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.proxy.DataProviderProxy;
import com.simple.original.client.proxy.DataProviderProxy.DataProviderType;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.service.OperationSuggestOracle;
import com.simple.original.client.service.OperationSuggestOracle.OperationSuggestion;
import com.simple.original.client.view.IAnalyticsTaskDesignerView;
import com.simple.original.client.view.widgets.EditableDNDCellList;
import com.simple.original.client.view.widgets.ErrorPanel;
import com.simple.original.client.view.widgets.ValueBoxEditorDecorator;

public class AnalyticsTaskDesignerView extends AbstractView implements IAnalyticsTaskDesignerView, Editor<AnalyticsTaskProxy> {

	private static final Logger logger = Logger.getLogger(AnalyticsTaskDesignerView.class.getName());

	/**
	 * This is my user editor driver.
	 * 
	 * @author chinshaw
	 */
	interface AnalyticsTaskBuilderDriver extends RequestFactoryEditorDriver<AnalyticsTaskProxy, AnalyticsTaskDesignerView> {
	}

	/**
	 * UiBinder implementation.
	 * 
	 * @author chinshaw
	 * 
	 */
	@UiTemplate("AnalyticsTaskBuilderView.ui.xml")
	interface Binder extends UiBinder<Widget, AnalyticsTaskDesignerView> {
	}

	/**
	 * Logger implementation.
	 */
	// private static final Logger logger = Logger.getLogger("ScriptEditor");

	@Ignore
	@UiField
	ErrorPanel errorPanel;

	@Path("public")
	@UiField
	CheckBox isPublic;

	@UiField
	HTMLPanel contentPanel;

	/*
	 * @UiField(provided = true) AnalyticsInputEditor taskInputsEditor;
	 * 
	 * 
	 * @UiField(provided = true) SmallTimePicker timeSelector;
	 */

	/**
	 * Name component for the name of the analytics operation. This also
	 * implements {@link HasEditorErrors so it can show constraint violations
	 * when an error occurs.
	 */
	@UiField
	ValueBoxEditorDecorator<String> name;

	/**
	 * Description component that edits analytics operation description. This
	 * also implements {@link HasEditorErrors} so it can show constraint
	 * violations when an error occurs
	 */
	@UiField
	ValueBoxEditorDecorator<String> description;

	/**
	 * Editor for the data provider object contained in the
	 * {@link AnalyticsTaskProxy}.
	 */
	@UiField
	@Path("dataProviders")
	DataProviderListEditor dataProviderEditor;

	@UiField(provided = true)
	@Path("analyticsOperations")
	EditableDNDCellList<AnalyticsOperationProxy> analyticsChainEditor;

	private final OperationSuggestOracle suggestOracle;

	@Ignore
	@UiField(provided = true)
	SuggestBox operationSuggest;

	@Ignore
	@UiField
	ListBox availableDataProviderTypes;

	@UiField
	LabelElement dashboardName;

	/*
	 * 
	 * @Override public String render(DashboardModelProxy dashboard) { return
	 * dashboard == null ? "No default dashboard" : dashboard.getName(); } },
	 * new ProvidesKey<DashboardModelProxy>() {
	 * 
	 * @Override public Object getKey(DashboardModelProxy item) { return (item
	 * == null) ? null : item.getId(); } });
	 */

	@UiField
	Button addDataProvider;

	@UiField
	Button addAnalytics;

	/**
	 * Button to submit the changes.
	 */
	@UiField
	Button saveTask;

	@UiField
	Button cancel;

	@Ignore
	private Presenter presenter;

	GroupMembership userRole;

	/**
	 * This is the script editor driver that will allow you to edit a analytics
	 * task.
	 */
	private final AnalyticsTaskBuilderDriver driver = GWT.create(AnalyticsTaskBuilderDriver.class);

	/**
	 * Default constructor.
	 * 
	 * @param user
	 *            User to edit.
	 * @param clientFactory
	 *            {@link ClientFactory} implementation.
	 */
	@Inject
	public AnalyticsTaskDesignerView(Resources resources) {
		super(resources);

		// timeSelector = new SmallTimePicker(new Date());
		// taskInputsEditor = new AnalyticsInputEditor(resources);

		suggestOracle = new OperationSuggestOracle();
		operationSuggest = new SuggestBox(suggestOracle);
		operationSuggest.setLimit(15);

		analyticsChainEditor = new EditableDNDCellList<AnalyticsOperationProxy>(new OperationCell() {
			@Override
			public void onRemoveCell(AnalyticsOperationProxy operation) {
				onRemoveOperation(operation);

			}

			@Override
			public void onEditOperation(AnalyticsOperationProxy value) {
				presenter.onEditOperation(value);
			}
		});

		// Max number of entities in the cell list will be 99.
		analyticsChainEditor.setMaxListSize(99);

		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		driver.initialize(this);
		setEnabledSaveButton(false);

		// default checkbox should be disabled
		setEnabledPublicCheckBox(true);
	}

	@Override
	protected ErrorPanel getErrorPanel() {
		return errorPanel;
	}

	@Override
	public void reset() {
		saveTask.setEnabled(false);
		errorPanel.clear();
		setEnabledPublicCheckBox(false);
		isPublic.setValue(false);
		availableDataProviderTypes.clear();
		setEnabledSaveButton(false);
		name.clearErrors();
		description.clearErrors();
	}

	@Override
	public void setEnabledSaveButton(boolean enabled) {
		saveTask.setEnabled(enabled);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		bindToPresenter();
	}

	private void bindToPresenter() {

		Enum<?>[] types = presenter.getDataProviderTypes();

		for (Enum<?> type : types) {
			availableDataProviderTypes.addItem(type.name());
		}

		suggestOracle.setSearchProvider(presenter);
	}

	@UiHandler("cancel")
	void onCancel(ClickEvent clickEvent) {
		errorPanel.clear();
		presenter.onCancel();
	}

	@UiHandler("saveTask")
	void handleClick(ClickEvent clickEvent) {
		errorPanel.clear();
		presenter.onSaveChanges(name.asEditor().getValue(), isPublic.getValue());
	}

	@Override
	public RequestFactoryEditorDriver<AnalyticsTaskProxy, ?> getEditorDriver() {
		return driver;
	}

	public void onAddDataProvider() {
		DataProviderType type = DataProviderType.valueOf(availableDataProviderTypes.getValue(availableDataProviderTypes.getSelectedIndex()));
		List<DataProviderProxy> dataProviders = dataProviderEditor.asEditor().getList();
		dataProviders.add(presenter.createDataProvider(type));
	}

	@Editor.Ignore
	@UiFactory
	DataProviderListEditor dataProviderEditor() {
		return new DataProviderListEditor(getResources());
	}

	@UiHandler("addAnalytics")
	protected void onAddOperationClicked(ClickEvent click) {
		onAddOperation();
	}

	private void onAddOperation() {
		OperationSuggestion suggestion = suggestOracle.getSuggestionByName(operationSuggest.getValue());
		if (suggestion == null || suggestion.getOperation() == null) {
			logger.severe("Operation was null in current selctions  ");
		}

		analyticsChainEditor.asEditor().getList().add(suggestion.getOperation());
		operationSuggest.setValue("");

		/*
		 * presenter.getInputsForOperation(suggestion.getOperation().getId()).fire
		 * (new Receiver<List<AnalyticsOperationInputProxy>>() {
		 * 
		 * @Override public void onSuccess(List<AnalyticsOperationInputProxy>
		 * response) { taskInputsEditor.addInputs(response); } });
		 */
	}

	protected void onRemoveOperation(AnalyticsOperationProxy operation) {
		// taskInputsEditor.removeInputs(operation.getInputs());
		analyticsChainEditor.asEditor().getList().remove(operation);
	}


	public void setEnabledPublicCheckBox(Boolean value) {
		isPublic.setEnabled(value);
	}

	@Override
	public void setDashboardName(String dashboardName) {
		this.dashboardName.setInnerText(dashboardName);
	}

	@Override
	public void setEnabledCancelButton(boolean value) {
		cancel.setEnabled(value);
	}

	@Override
	public void scrollToTop() {
		contentPanel.getElement().getParentElement().setScrollTop(0);
	}
	
	@UiHandler("addDataProvider")
	void addDataProvider(ClickEvent event) {
		onAddDataProvider();
	}
	
	@UiHandler("editDashboard")
	void onEditDashboard(ClickEvent event) {
		presenter.onEditDashboard();
	}
}
