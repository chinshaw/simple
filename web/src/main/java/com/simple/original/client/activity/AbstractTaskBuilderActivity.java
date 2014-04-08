package com.simple.original.client.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.client.place.AnalyticsTaskBuilderPlace;
import com.simple.original.client.place.AnalyticsTasksPlace;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.service.DaoRequestFactory.DaoRequest;
import com.simple.original.client.view.IAnalyticsTaskDesignerView;

public abstract class AbstractTaskBuilderActivity<E extends AnalyticsTaskProxy, R extends DaoRequest<E>> extends
		AbstractActivity<AnalyticsTaskBuilderPlace, IAnalyticsTaskDesignerView> {
	
	private static final Logger logger = Logger.getLogger(AbstractTaskBuilderActivity.class.getName());
	
	private final Receiver<Long> SAVE_RECEIVER = new Receiver<Long>() {

		@Override
		public void onFailure(ServerFailure error) {
			display.setEnabledSaveButton(true);
			display.setEnabledCancelButton(true);
			display.showError(error.getMessage());
			super.onFailure(error);
		}

		public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
			display.setEnabledSaveButton(true);
			display.setEnabledCancelButton(true);
			display.getEditorDriver().setConstraintViolations(violations);
			display.scrollToTop();
		}

		@Override
		public void onSuccess(Long response) {
			placeController().goTo(getSavePlace());
			display.setEnabledSaveButton(true);
			display.setEnabledCancelButton(true);
			
		}
		
	};
	

	protected Long dashboardId;
	
	/**
	 * Default constructor
	 * 
	 * @param place
	 * @param clientFactory
	 * @param display
	 */
	@Inject
	public AbstractTaskBuilderActivity(IAnalyticsTaskDesignerView display) {
		super(display);
	}

	/**
	 * Create a new task. This will initialize any new lists.
	 * 
	 * @param context
	 *            The context to use to create the task.
	 * @param clazz
	 *            The class type to be created.
	 * @return
	 */
	protected E create(R context, Class<E> clazz) {
		E editableAnalyticsTask = context.create(clazz);


		List<AnalyticsOperationProxy> analyticsOperations = new ArrayList<AnalyticsOperationProxy>();

		editableAnalyticsTask.setAnalyticsOperations(analyticsOperations);

		return editableAnalyticsTask;
	}



	/**
	 * validate unique name and save/update editing proxy
	 */
	public void onSaveChanges(String name, boolean publicFlag) {
		save(new AnalyticsTasksPlace());
	}

	/**
	 * Save the task.
	 */
	protected void save(final Place place) {
		display.setEnabledSaveButton(false);
		display.setEnabledCancelButton(false);
		RequestFactoryEditorDriver<? super AnalyticsTaskProxy, ?> driver = display.getEditorDriver();

		RequestContext request = driver.flush();
		request.fire();
	}

	/**
	 * Copy the task by the id. This will also call edit and flush to the ui.
	 * 
	 * @param taskId
	 *            The task id to copy, can be either a ReportTask or a
	 *            AnalyticsTaskId.
	 */
	protected void doCopy(Long taskId) {
		getContext().copy(place().getAnalyticsTaskId()).with("*").fire(new Receiver<E>() {

			@Override
			public void onSuccess(final E clone) {
				doEdit(getContext(), clone);
			}

			@Override
			public void onFailure(final ServerFailure error) {
				display.showError("Unable to retrieve task details to copy : " + error.getMessage());
				super.onFailure(error);
			}
		});
	}
	
	/**
	 * Call to edit the task and update the dashboards.
	 * 
	 * @param context
	 * @param task
	 */
	protected void doEdit(R context, E task) {
		RequestFactoryEditorDriver<? super AnalyticsTaskProxy, ?> driver = display.getEditorDriver();
		E editable = context.edit(task);
		

		context.save(editable).with(driver.getPaths()).to(SAVE_RECEIVER);
		
		driver.edit(editable, context);

		display.setEnabledSaveButton(true);
		display.setEnabledCancelButton(true);
		
		if (task.getDashboard() != null) {
			dashboardId = task.getDashboard().getId();
			display.setDashboardName(task.getDashboard().getName());
		} else {
			display.setDashboardName("No dashbaord");
		}
	}

	/**
	 * Retrieve the context from the child class.
	 * 
	 * @return
	 */
	public abstract R getContext();

	/**
	 * The place to go to when a task is saved.
	 * 
	 * @return
	 */
	protected abstract Place getSavePlace();

	/**
	 * Call to cancel the edit or save.
	 */
	@Override
	public void onCancel() {
		placeController().goTo(getSavePlace());
	}
}