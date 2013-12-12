package com.simple.original.client.activity;

import java.util.logging.Logger;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.inject.Inject;
import com.simple.original.api.domain.SortOrder;
import com.simple.original.client.place.MonitoringTaskPlace;
import com.simple.original.client.place.WelcomePlace;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.service.DaoBaseDataProvider;
import com.simple.original.client.service.DaoRequestFactory.DaoRequest;
import com.simple.original.client.view.IMonitoringTaskLimitView;

public class MonitoringTaskLimitActivity extends AbstractActivity<MonitoringTaskPlace, IMonitoringTaskLimitView>	implements IMonitoringTaskLimitView.Presenter {

	/*
	 * Logger instance
	 */
	private static final Logger logger = Logger.getLogger(MonitoringTaskLimitActivity.class.getName());
	/**
	 * Holds the AppConfigurationsRequest
	 */

	/**
	 * Constructor of MonitoringTaskLimitActivity
	 * 
	 * @param place
	 * @param clientFactory
	 */
	@Inject
	public MonitoringTaskLimitActivity(IMonitoringTaskLimitView view) {
		super(view);
	}
	
	private class MonitoringTaskDataProvider extends DaoBaseDataProvider<PersonProxy> implements ColumnSortEvent.Handler{

		private String sortColumn = null;
		private SortOrder sortOrder = SortOrder.DESCENDING;
		private final String NAME_COLUMN = "name";
		private final String EMAIL_COLUMN = "email";
		private final String TASKLIMIT_COLUMN = "taskLimitValue";
		/**
		 * This method is used to sort the user exception list onclick of the column header.
		 */
		@Override
		public void onColumnSort(ColumnSortEvent event) {

			if (event.getColumn().isSortable()) {
				@SuppressWarnings("unchecked")
				int columnIndex = display.getColumnIndex((Column<PersonProxy, ?>) event.getColumn());

				switch (columnIndex) {
				case 0:
					sortColumn = NAME_COLUMN;
					sortOrder = (event.isSortAscending()) ? SortOrder.ASCENDING	: SortOrder.DESCENDING;
					break;
				case 1:
					sortColumn = EMAIL_COLUMN;
					sortOrder = (event.isSortAscending()) ? SortOrder.ASCENDING	: SortOrder.DESCENDING;
					break;
				case 2:
					sortColumn = TASKLIMIT_COLUMN;
					sortOrder = (event.isSortAscending()) ? SortOrder.ASCENDING	: SortOrder.DESCENDING;	
				}
				logger.info("Selected sort : " + columnIndex + "-" + sortOrder.asJPQL());
				display.getUserTable().setVisibleRangeAndClearData(display.getUserTable().getVisibleRange(), true);
			}
		}

		@Override
		public String[] getWithProperties() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public DaoRequest<PersonProxy> getRequestProvider() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	/**
	 * This is the asynchronous monitoring task data provider used to fetch data
	 * and update the editor when needed.
	 */
	protected MonitoringTaskDataProvider monitoringTaskDataProvider = new MonitoringTaskDataProvider();

	/**
	 * Bind view widgets to activity.
	 */
	@Override
	protected void bindToView() {
		display.setPresenter(this);
		display.addColumnSortHandler(monitoringTaskDataProvider);
		getMonitoringTaskLimit();
		monitoringTaskDataProvider.addDataDisplay(display.getUserTable());
	}

	/**
	 * This method is used to fetch the default task limit value
	 */
	private void getMonitoringTaskLimit(){
		
	}


	/**
	 * This method is called when onSave button is clicked
	 */
	public void onSave() {
		String defaultTaskLimitValue = display.getDefaultTaskLimitValue().trim();
		logger.info("defaultTaskLimitValue onsave()" + defaultTaskLimitValue);		
		if (!defaultTaskLimitValue.matches("[0-9]*")) {
			display.showError("Enter only numeric values");
		}else if (display.getDefaultTaskLimitValue().equals("")) {
			display.showError(" DefaultTasklimitValue cannot be empty ");
		}else if(Integer.parseInt(defaultTaskLimitValue) < 1){
			display.showError("DefaultTaskLimitValue cannot be less than 1");
		}else {
			display.getErrorPanel().clear();
			display.setEnabledSaveButton(false);
			/*
			RequestFactoryEditorDriver<AppConfigurationsProxy, ?> driver = display.getEditorDriver();
			AppConfigurationsRequest request = (AppConfigurationsRequest) driver.flush();
			if (driver.hasErrors()) {
				List<EditorError> errors = driver.getErrors();
				for (EditorError error : errors) {
					logger.info("Errors occurred in NewMonitoringTaskLimitActivity editor"+ error.getMessage());
				}
				return;
			}
			request.fire(new Receiver<Void>() {

				public void onFailure(ServerFailure error) {
					display.setEnabledSaveButton(true);
					display.showError(error.getMessage());
					logger.info("error occured" + error.getMessage());
					super.onFailure(error);
				}

				@Override
				public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
					for (ConstraintViolation<?> voilation : violations) {
						display.showError(voilation.getPropertyPath() + " "+ voilation.getMessage());
					}
					display.setEnabledSaveButton(true);
				}

				@Override
				public void onSuccess(Void response) {
					getMonitoringTaskLimit();
					// Global Status Window	
					NotificationEvent ne = new NotificationEvent("Default Task Limit Value Saved Successfully");
					eventBus.fireEvent(ne);
					display.getUserTable().setVisibleRangeAndClearData(display.getUserTable().getVisibleRange(), true);
					display.setEnabledSaveButton(true);
				}
			});
			*/
		}
	}

	/**
	 * saves users private tasks limit in database
	 */
	@Override
	public void onSaveUserTaskLimit() {
		display.setEnabledSaveTaskLimitButton(false);
		/* This is wrong !! WTF
		personRequestContext.saveUserTaskLimit(personlist).fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void response) {
				personRequestContext = clientFactory.daoRequestFactory().personRequest();
				logger.info(" MonitoringTasksActivity->onSaveTaskLimit -> Saved Successfully");
				display.getUserTable().setVisibleRangeAndClearData(display.getUserTable().getVisibleRange(), true);
				display.setEnabledSaveTaskLimitButton(true);
			}
			public void onFailure(ServerFailure error) {
				logger.info(" MonitoringTasksActivity->onSaveTaskLimit -> Unable to Save Tasks "+ error.toString());
				display.setEnabledSaveTaskLimitButton(true);
				display.showError("Unable to update Person ",error);
			}
		});
		*/
	}

	@Override
	public void onNewTaskLimitUpdate(PersonProxy object, String value) {
	    throw new RuntimeException("FIX ME to use permissions service");
	    
	}
	
	@Override
	public void onCancel() {
		placeController().goBackOr(new WelcomePlace());
	}
}
