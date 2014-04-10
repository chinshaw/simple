package com.simple.original.client.activity;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.api.exceptions.SimpleException;
import com.simple.api.orchestrator.IAnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.original.client.place.AnalyticsOperationPlace;
import com.simple.original.client.place.AnalyticsOperationsPlace;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.proxy.DataProviderInputProxy;
import com.simple.original.client.proxy.RAnalyticsOperationProxy;
import com.simple.original.client.service.DaoRequestFactory.AnalyticsOperationRequest;
import com.simple.original.client.service.event.JmsEvent;
import com.simple.original.client.service.event.jms.StompEventService;
import com.simple.original.client.view.IOperationBuilderView;
import com.simple.original.client.view.IOperationBuilderView.Presenter;
import com.simple.original.client.view.desktop.OperationExecutionView;

public class AnalyticsOperationBuilderActivity extends AbstractActivity<AnalyticsOperationPlace, IOperationBuilderView> implements
		Presenter, JmsEvent.Handler {

	private static final Logger logger = Logger.getLogger(AnalyticsOperationBuilderActivity.class.getName());

	/**
	 * This is the request context for the
	 */
	private AnalyticsOperationRequest context;

	private StompEventService stompClient;

	@Inject
	public AnalyticsOperationBuilderActivity(IOperationBuilderView view) {
		super(view);
	}

	@Override
	protected void bindToView() {
		GWT.log("Adding event handler");
		eventBus().addHandler(JmsEvent.TYPE, new JmsEvent.Handler() {
			
			@Override
			public void onJmsEvent(JmsEvent event) {
				Window.alert("JMS Event " + event.getMessage().getMessage());
				
			}
		});

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

	private void doRegistration() {
		GWT.log("StompClient is " + stompClient);
	}

	/**
	 * Creates a context and then uses the context to create an
	 * {@link RAnalyticsOperation} Next it will call editOperation to do the
	 * actual editing on the view.
	 */
	private void createAndEditOperation() {
		RAnalyticsOperationProxy operation = context.create(RAnalyticsOperationProxy.class);
		operation.setDataProviders(new ArrayList<DataProviderInputProxy>());
		operation.setInputs(new ArrayList<AnalyticsOperationInputProxy>());
		operation.setOutputs(new ArrayList<AnalyticsOperationOutputProxy>());
		edit(operation);
	}

	private void findAndEditOperation(Long operationId) {
		dao().createAnalyticsOperationRequest().find(operationId).with("*").fire(new Receiver<AnalyticsOperationProxy>() {

			@Override
			public void onSuccess(AnalyticsOperationProxy operation) {
				if (!(operation instanceof RAnalyticsOperationProxy)) {
					throw new RuntimeException("Operation is not of type R");
				}

				RAnalyticsOperationProxy editable = (RAnalyticsOperationProxy) context.edit(operation);
				edit(editable);

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
		logger.fine("Editing operation " + operation);
		display.getEditorDriver().edit(operation);
	}

	/**
	 * validate unique name and save/update editing proxy
	 */
	@Override
	public void onSave() {
		AnalyticsOperationProxy operation = display.getEditorDriver().flush();
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
					GWT.log("Constraint violation " + violation.getPropertyPath() + violation.getMessage());
					display.showError(violation.getPropertyPath() + " " + violation.getMessage());
				}
			}
		}).fire();
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
	 * This is a little quirky behind the scenes because we have to clone the
	 * proxy to another context. This is because the edit context owns the bean
	 * and it will throw an exception if we try to send it to another context to
	 * execute the operation. We use proxy utils to clone the proxy to another
	 * context.
	 * 
	 * 
	 * 
	 */
	@Override
	public void onTest() {
		RAnalyticsOperationProxy operation = display.getEditorDriver().flush();

		OperationExecutionActivity execActivity = new OperationExecutionActivity(new OperationExecutionView(eventBus(), resources()));
		execActivity.start(display.getExecutionContainer(), (EventBus) eventBus());

		RAnalyticsOperation rOperation = new RAnalyticsOperation(operation.getName());
		rOperation.setCode(operation.getCode());
		execActivity.edit(rOperation);

		if (display.getExecutionContainerSize() == 0) {
			display.setExecutionContainerSize(400);
		}

		/*
		 * OperationRequest testRequest = service().operationRequest();
		 * RAnalyticsOperationProxy clone = ProxyUtils.cloneProxyToNewContext(
		 * RAnalyticsOperationProxy.class, operation, testRequest);
		 * 
		 * testRequest.executeOperation(clone).fire();
		 */
	}

	@Override
	public void onJmsEvent(JmsEvent event) {
		Window.alert("GOt event " + event.getMessage().getMessage());
		
	}
}