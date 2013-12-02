package com.simple.original.client.activity;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.client.events.NotificationEvent;
import com.simple.original.client.place.DataProvidersPlace;
import com.simple.original.client.proxy.SqlConnectionProxy;
import com.simple.original.client.service.DaoBaseDataProvider;
import com.simple.original.client.service.DaoRequestFactory.DaoRequest;
import com.simple.original.client.service.DaoRequestFactory.DataProviderRequest;
import com.simple.original.client.service.ServiceRequestFactory.SqlDataProviderRequest;
import com.simple.original.client.view.IDataProvidersView;

public class DataProvidersActivity extends AbstractActivity<DataProvidersPlace, IDataProvidersView> implements IDataProvidersView.Presenter {

	private class DataProvider extends DaoBaseDataProvider<SqlConnectionProxy> {

		@Override
		public String[] getWithProperties() {
			return new String[] {};
		}

		@Override
		public DaoRequest<SqlConnectionProxy> getRequestProvider() {
			return dao().dataProviderRequest();
		}
	}

	private RequestFactoryEditorDriver<SqlConnectionProxy, ?> driver;

	@Inject
	public DataProvidersActivity(IDataProvidersView view) {
		super(view);
	}

	@Override
	protected void bindToView() {
		display.setPresenter(this);

		dao().dataProviderRequest().getAvailableSqlDrivers().fire(new Receiver<List<String>>() {

			@Override
			public void onSuccess(List<String> drivers) {
				display.setAvailableDrivers(drivers);
			}
		});

		DataProvider tableDataProvider = new DataProvider();
		tableDataProvider.addDataDisplay(display.getConfiguredDataProviders());
	}

	private void createAndEdit() {
		driver = display.getEditorDriver();
		DataProviderRequest saveContext = dao().dataProviderRequest();
		SqlConnectionProxy dataProvider = saveContext.create(SqlConnectionProxy.class);
		saveContext.save(dataProvider);
		driver.edit(dataProvider, saveContext);
	}

	@Override
	public void onSave() {
		RequestContext context = driver.flush();
		context.fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				eventBus().fireEvent(new NotificationEvent("Data provider saved"));
			}

			@Override
			public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
				driver.setConstraintViolations(violations);
			}
		});
	}

	@Override
	public void onTestConnections(Set<SqlConnectionProxy> selectedSet) {
		SqlDataProviderRequest testRequest = service().sqlDataProviderRequest();

		GWT.log("Selected count is " + selectedSet.size());
		for (final SqlConnectionProxy driver : selectedSet) {
			testRequest.testConnection(driver).to(new Receiver<Boolean>() {

				@Override
				public void onSuccess(Boolean response) {
					Window.alert("Status for " + driver.getName() + " is " + response);
				}

				public void onFailure(ServerFailure failure) {
					Window.alert("Failure is " + failure.getMessage());
				}

			});
		}

		testRequest.fire();
	}
}