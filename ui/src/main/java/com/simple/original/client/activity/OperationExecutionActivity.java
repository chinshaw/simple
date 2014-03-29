package com.simple.original.client.activity;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.shared.GWT;
import com.google.inject.Inject;
import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.dataprovider.DBDataProvider;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.dataprovider.HttpDataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.domain.model.ui.DataProviderInput;
import com.simple.original.client.place.OperationExecutionPlace;
import com.simple.original.client.service.rest.OrchestratorService;
import com.simple.original.client.view.IOperationExecutionView;

public class OperationExecutionActivity extends AbstractActivity<OperationExecutionPlace, IOperationExecutionView> implements IOperationExecutionView.Presenter {

	
	private AnalyticsOperation operation = null;
	private List<DataProvider> dataProviders = new ArrayList<DataProvider>();
	
	@Inject
	public OperationExecutionActivity(IOperationExecutionView display) {
		super(display);
	}

	@Override
	protected void bindToView() {
		display.setPresenter(this);
		
	}
	
	public void edit(AnalyticsOperation operation) {
		this.operation = operation;
		for (DataProviderInput dip : operation.getDataProviders()) {
			addDataProvider(dip);
		}
		
		for (AnalyticsOperationInput aoip : operation.getInputs()) {
			display.addOperationInput(aoip);
		}
		
	}
	
	private void addDataProvider(DataProviderInput dip) {
		DataProvider dp = null;
		switch (dip.getType()) {
		case DB:
			dp = new DBDataProvider();
			break;
		case FILE:
			break;
		case WEB:
			dp = new HttpDataProvider();
		default:
			break;	
		}
		
		if (dp == null) {
			throw new RuntimeException("DataProvider had unknown type" + dip.getType());
		}
		
		dataProviders.add(dp);
		display.addDataProvider(dp);
	}
	

	@Override
	public void onExecute() {
		job.setOperation(operation);
		job.setDataProviders(dataProviders);
		
		OrchestratorService.Util.get().executeOperation(job, new MethodCallback<Void>() {
			
			@Override
			public void onSuccess(Method method, Void response) {
				GWT.log("Success");
				
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				GWT.log("Failed" + exception);
			}
		});
	}

}
