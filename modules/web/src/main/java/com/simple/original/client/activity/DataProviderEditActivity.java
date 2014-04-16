package com.simple.original.client.activity;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.dataprovider.HttpDataProvider;
import com.simple.original.client.place.DataProviderEditPlace;
import com.simple.original.client.service.rest.DataProviderResource;
import com.simple.original.client.view.IDataProviderEditView;

public class DataProviderEditActivity extends AbstractActivity<DataProviderEditPlace, IDataProviderEditView> implements IDataProviderEditView.Presenter {

	@Inject
	public DataProviderEditActivity(IDataProviderEditView view) {
		super(view);
	}

	@Override
	protected void bindToView() {
		display.setPresenter(this);
		createAndEdit();
	}

	private void createAndEdit() {
		HttpDataProvider httpDataProvider = new HttpDataProvider();
		display.getEditorDriver().edit(httpDataProvider);
	}

	
	@Override
	public void onSave() {
		DataProvider dp = getDisplay().getEditorDriver().flush();
		DataProviderResource.Util.get().create(dp, new MethodCallback<DataProvider>() {
			
			@Override
			public void onSuccess(Method method, DataProvider response) {
				Window.alert("Success " + response);
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert("Failed" + exception);
			}
		});
	}
}