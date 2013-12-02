package com.simple.original.client.activity;

import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.client.place.AnalyticsTasksPlace;
import com.simple.original.client.place.CreateEditOperationBuilderPlace;
import com.simple.original.client.place.DashboardDesignerPlace;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.service.DaoRequestFactory.AnalyticsTaskRequest;
import com.simple.original.client.service.SearchableRequest;
import com.simple.original.client.view.IAnalyticsTaskDesignerView;

public class AnalyticsTaskBuilderActivity extends AbstractTaskBuilderActivity<AnalyticsTaskProxy, AnalyticsTaskRequest> implements IAnalyticsTaskDesignerView.Presenter {

	@Inject
	public AnalyticsTaskBuilderActivity(IAnalyticsTaskDesignerView view) {
		super(view);
	}

	/**
	 * This function will bind the activity to the view.
	 */
	@Override
	protected void bindToView() {
		display.setPresenter(this);

		Long taskId = place().getAnalyticsTaskId();
		doCreateEdit(taskId);
	}

	protected void doCreateEdit(Long taskId) {
		AnalyticsTaskProxy taskToEdit = null;
		if (taskId == null) {
			AnalyticsTaskRequest context = getContext();
			taskToEdit = create(context, AnalyticsTaskProxy.class);
			doEdit(context, taskToEdit);
		} else {

			// Lets get the analytics task from RequestFactory and edit it.
			dao().createAnalyticsTaskRequest().find(taskId).with(AnalyticsTaskProxy.EDIT_PROPERTIES).fire(new Receiver<AnalyticsTaskProxy>() {

				@Override
				public void onSuccess(AnalyticsTaskProxy analyticsTask) {
					doEdit(getContext(), analyticsTask);
				}

				@Override
				public void onFailure(ServerFailure error) {
					display.showError("Unable to retrieve task details to Edit : " + error.getMessage());
					super.onFailure(error);
				}
			});
		}
	}

	@Override
	public AnalyticsTaskRequest getContext() {
		return dao().createAnalyticsTaskRequest();
	}

	@Override
	protected Place getSavePlace() {
		return new AnalyticsTasksPlace();
	}

	@Override
	public void onEditOperation(AnalyticsOperationProxy value) {
		placeController().goTo(new CreateEditOperationBuilderPlace(value.getId()));
	}

	@Override
	public Request<AnalyticsOperationProxy> fetchAnalyticsOperation(Long id) {
		return dao().createAnalyticsOperationRequest().find(id);
	}

	@Override
	public SearchableRequest<AnalyticsOperationProxy> createSearchRequest() {
		return dao().createAnalyticsOperationRequest();
	}

	@Override
	public Request<List<AnalyticsOperationInputProxy>> getInputsForOperation(Long operationId) {
		return dao().createAnalyticsOperationRequest().listInputs(operationId);
	}

	@Override
	public void onEditDashboard() {
		placeController().goTo(new DashboardDesignerPlace(dashboardId));
	}
}
