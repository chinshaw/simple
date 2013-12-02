package com.simple.original.client.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.api.analytics.IAnalyticsOperationOutput;
import com.simple.original.api.analytics.IPerson;
import com.simple.original.api.exceptions.SimpleException;
import com.simple.original.client.place.AnalyticsOperationsPlace;
import com.simple.original.client.place.ApplicationPlace;
import com.simple.original.client.place.CreateEditOperationBuilderPlace;
import com.simple.original.client.proxy.AnalyticsOperationDataProviderProxy;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.proxy.AnalyticsOperationProxy.OperationType;
import com.simple.original.client.proxy.JavaAnalyticsOperationProxy;
import com.simple.original.client.proxy.RAnalyticsOperationProxy;
import com.simple.original.client.service.DaoRequestFactory.AnalyticsOperationRequest;
import com.simple.original.client.view.IOperationBuilderView;
import com.simple.original.client.view.IOperationBuilderView.Presenter;


public class AnalyticsOperationBuilderActivity extends AbstractActivity<ApplicationPlace, IOperationBuilderView> implements Presenter {

    private static final Logger logger = Logger.getLogger(AnalyticsOperationBuilderActivity.class.getName());

    /**
     * This is our request context for the operation.
     */
    private AnalyticsOperationRequest requestContext = null;

    private RequestFactoryEditorDriver<RAnalyticsOperationProxy, ?> driver = null;

    // private String operationName = null;

    /** Constants for Edit and Copy actions */
    private enum OperationActionType {
        CREATE, EDIT, COPY;
    }

    private Long analyticsOperationId = null;
    private OperationActionType actionType;

    @Inject
    public AnalyticsOperationBuilderActivity(IOperationBuilderView view) {
        super(view);
       
    }

    @Override
    protected void bindToView() {
    	 if (((CreateEditOperationBuilderPlace) place()).getAnalyticsOperationId() != null) {
             actionType = OperationActionType.EDIT;
             analyticsOperationId = ((CreateEditOperationBuilderPlace) place()).getAnalyticsOperationId();
         } else {
             actionType = OperationActionType.CREATE;
         }
    	
        driver = display.getEditorDriver();

        display.setPresenter(this);

        // If the id is not null then lets go ahead
        if (analyticsOperationId != null && analyticsOperationId != 0) {
            if (OperationActionType.EDIT.equals(actionType)) {
                GWT.log("DOING EDIT");
                dao().createAnalyticsOperationRequest().find(analyticsOperationId).with(AnalyticsOperationProxy.EDIT_PROPERTIES)
                        .with(RAnalyticsOperationProxy.EDIT_PROPERTIES).fire(new Receiver<AnalyticsOperationProxy>() {

                            @Override
                            public void onSuccess(AnalyticsOperationProxy analyticsOperation) {
                                // operationName = analyticsOperation.getName();
                                display.getIsPublic().setEnabled(false);
                                requestContext = dao().createAnalyticsOperationRequest();
                                if (analyticsOperation instanceof RAnalyticsOperationProxy) {
                                    editAnalyticsOperation((RAnalyticsOperationProxy) analyticsOperation);
                                } else if (analyticsOperation instanceof JavaAnalyticsOperationProxy) {
                                    editAnalyticsOperation((JavaAnalyticsOperationProxy) analyticsOperation);
                                } else {
                                    throw new RuntimeException("Got a type that we did not expect");
                                }
                            }
                        });
            } else if (OperationActionType.COPY.equals(actionType)) {
                requestContext = dao().createAnalyticsOperationRequest();
                requestContext.copy(analyticsOperationId).with(AnalyticsOperationProxy.EDIT_PROPERTIES).with(RAnalyticsOperationProxy.EDIT_PROPERTIES)
                        .fire(new Receiver<AnalyticsOperationProxy>() {
                            @Override
                            public void onSuccess(AnalyticsOperationProxy copiedOperation) {
                                requestContext = dao().createAnalyticsOperationRequest();
                                copiedOperation = requestContext.edit(copiedOperation);
                                requestContext.save(copiedOperation).with(driver.getPaths());
                                ((RequestFactoryEditorDriver<RAnalyticsOperationProxy, ?>) driver).edit((RAnalyticsOperationProxy) copiedOperation, requestContext);
                            }
                        });
            }
        } else {
            // To disable the isPublic check box for User View.
            if (currentPerson().getRoles().contains(IPerson.Role.ADMIN)) {
                display.getIsPublic().setEnabled(true);
            }
            // AnalyticsOperationId is null so we will let the view select a
            // type and create new one.
            requestContext = dao().createAnalyticsOperationRequest();
            createAndEditOperation();
        }
    }

    private void createAndEditOperation() {
        RAnalyticsOperationProxy analyticsOperation = requestContext.create(RAnalyticsOperationProxy.class);
        analyticsOperation.setInputs(new ArrayList<AnalyticsOperationInputProxy>());
        analyticsOperation.setOutputs(new ArrayList<AnalyticsOperationOutputProxy>());
        editAnalyticsOperation(analyticsOperation);
    }

    private void editAnalyticsOperation(RAnalyticsOperationProxy analyticsOperation) {
        if (requestContext == null) {
            throw new RuntimeException("Request context must be initialized before calling edit");
        }
        requestContext.save(analyticsOperation).with(driver.getPaths());
        driver.edit(analyticsOperation, requestContext);
    }

    private void editAnalyticsOperation(JavaAnalyticsOperationProxy analyticsOperation) {
        requestContext.save(analyticsOperation).with(driver.getPaths());
        // TODO this has been refactored and the JavaAnalytics stuff isn't done
        // yet.
        // ((RequestFactoryEditorDriver<JavaAnalyticsOperationProxy, ?>)
        // driver).edit(analyticsOperation, requestContext);
    }

    /**
     * validate unique name and save/update editing proxy
     */
    @Override
    public void onSave(String name, boolean publicFlag) {
        display.getErrorPanel().clear();
        saveOperation();
    }

    /**
     * save editing proxy
     */
    private void saveOperation() {
        final AnalyticsOperationRequest request = (AnalyticsOperationRequest) driver.flush();

        if (driver.hasErrors()) {
            List<EditorError> errors = driver.getErrors();
            for (EditorError error : errors) {
                logger.info("Errors occurred in script editor" + error.getMessage());
            }
            return;
        }

        request.fire(new Receiver<Void>() {
            public void onFailure(ServerFailure error) {
                display.showError(error.getMessage());
                logger.info("error occured" + error.getMessage());
                super.onFailure(error);
            }

            @Override
            public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
                for (ConstraintViolation<?> voilation : violations) {
                    display.showError(voilation.getPropertyPath() + " " + voilation.getMessage());
                }
            }

            @Override
            public void onSuccess(Void response) {
                placeController().goTo(new AnalyticsOperationsPlace());
            }
        });
    }

    /**
     * Not used for now
     */
    @Override
    public void onAnalyticsTypeUpdated(OperationType value) {
        // Reset the context to a new request and throw away the old one.
        requestContext = dao().createAnalyticsOperationRequest();
        switch (value) {
        case R_Analytics:
            // User has selected the R_Analytics type so we will create a new
            // object and fill in new lists
            // for the inputs and outputs.
            createAndEditOperation();
            break;
        case Java:
            editAnalyticsOperation(requestContext.create(JavaAnalyticsOperationProxy.class));
            break;
        default:
            throw new RuntimeException("Not supported right now");
        }
    }

    @Override
    public void onCancelAnalytics() {
        Place previousPlace = placeController().getPreviousPlace();
        placeController().goTo(previousPlace);
    }

    /**
     * Really here more for legacy, this used to have to figure out the output
     * class type and creat it that way but that is no longer needed.
     */
    @Override
    public AnalyticsOperationOutputProxy createOutput(IAnalyticsOperationOutput.Type outputType) throws SimpleException {
        AnalyticsOperationOutputProxy output = requestContext.create(AnalyticsOperationOutputProxy.class);
        output.setOutputType(outputType);
        logger.fine("Cretaing output with type " + outputType.name());
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
        AnalyticsOperationDataProviderProxy dataProvider = requestContext.create(AnalyticsOperationDataProviderProxy.class);
        return dataProvider;
    }
}