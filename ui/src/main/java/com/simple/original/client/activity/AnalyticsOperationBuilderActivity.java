package com.simple.original.client.activity;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.original.api.analytics.IAnalyticsOperationOutput;
import com.simple.original.api.exceptions.SimpleException;
import com.simple.original.client.place.AnalyticsOperationPlace;
import com.simple.original.client.place.AnalyticsOperationsPlace;
import com.simple.original.client.proxy.AnalyticsOperationDataProviderProxy;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.proxy.RAnalyticsOperationProxy;
import com.simple.original.client.service.DaoRequestFactory.AnalyticsOperationRequest;
import com.simple.original.client.view.IOperationBuilderView;
import com.simple.original.client.view.IOperationBuilderView.Presenter;

public class AnalyticsOperationBuilderActivity extends AbstractActivity<AnalyticsOperationPlace, IOperationBuilderView> implements Presenter {

	private static final Logger logger = Logger.getLogger(AnalyticsOperationBuilderActivity.class.getName());

	/**
	 * This is the request context for the
	 */
	private AnalyticsOperationRequest context;

	@Inject
	public AnalyticsOperationBuilderActivity(IOperationBuilderView view) {
		super(view);
	}

	@Override
	protected void bindToView() {
		display.setPresenter(this);
		context = dao().createAnalyticsOperationRequest();

		final Long operationId = place().getAnalyticsOperationId();

		logger.finest("Operation Id to edit is " + operationId);
		if (operationId == null) {
			createAndEditOperation();
		} else {
			findAndEditOperation(operationId);
		}
	}

	/**
	 * Creates a context and then uses the context to create an
	 * {@link RAnalyticsOperation} Next it will call editOperation to do the
	 * actual editing on the view.
	 */
	private void createAndEditOperation() {
		logger.fine("Creating new operation to edit");
		RAnalyticsOperationProxy operation = context.create(RAnalyticsOperationProxy.class);
		operation.setInputs(new ArrayList<AnalyticsOperationInputProxy>());
		operation.setOutputs(new ArrayList<AnalyticsOperationOutputProxy>());
		edit(operation);
	}

	private void findAndEditOperation(Long operationId) {
		dao().createAnalyticsOperationRequest().find(operationId).with("*").fire(new Receiver<AnalyticsOperationProxy>() {

			@Override
			public void onSuccess(AnalyticsOperationProxy operation) {
				edit((RAnalyticsOperationProxy) operation);
			}
		});
	}

	/**
	 * This builds the RequestContext for the editor driver and calls edit to
	 * the display.
	 * 
	 * @param operation
	 */
	private void edit(RAnalyticsOperationProxy operation) {

		// add a request to save the operation when it is done being edited.
		context.save(operation).to(new Receiver<Long>() {

			public void onFailure(ServerFailure error) {
				display.showError(error.getMessage());
				logger.info("error occured" + error.getMessage());
				super.onFailure(error);
			}

			@Override
			public void onSuccess(Long response) {
				placeController().goTo(new AnalyticsOperationsPlace());
			}

			@Override
			public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
				for (ConstraintViolation<?> violation : violations) {
					display.showError(violation.getPropertyPath() + " " + violation.getMessage());
				}
			}
		});

		logger.fine("Editing operation " + operation);
		display.getEditorDriver().edit(operation, context);
	}

	/**
	 * validate unique name and save/update editing proxy
	 */
	@Override
	public void onSave(String name, boolean publicFlag) {
		Window.alert("DOING SAVE");
		display.getErrorPanel().clear();
		final RequestContext flushedContext = display.getEditorDriver().flush();
		flushedContext.fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				GWT.log("GOT SUCCESSSSS");

			}

		});
	}

	@Override
	public void onCancelAnalytics() {
		placeController().goBackOr(new AnalyticsOperationsPlace());
	}

	/**
	 * Really here more for legacy, this used to have to figure out the output
	 * class type and creat it that way but that is no longer needed.
	 */
	@Override
	public AnalyticsOperationOutputProxy createOutput(IAnalyticsOperationOutput.Type outputType) throws SimpleException {
		logger.fine("Cretaing output with type " + outputType.name());

		AnalyticsOperationOutputProxy output = context.create(AnalyticsOperationOutputProxy.class);
		output.setOutputType(outputType);

		return output;
	}

	@Override
	public Enum<?>[] getAvailableOutputTypes() {
		return IAnalyticsOperationOutput.Type.values();
	}

	/**
	 * Used to create a data provider from the current context.
	 */
	@Override
	public AnalyticsOperationDataProviderProxy createDataProvider() {
		AnalyticsOperationDataProviderProxy dataProvider = context.create(AnalyticsOperationDataProviderProxy.class);
		return dataProvider;
	}

	@Override
	public void onTestScript() {
		
	}
}