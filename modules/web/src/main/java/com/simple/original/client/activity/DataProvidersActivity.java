package com.simple.original.client.activity;

import java.util.Set;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.simple.original.client.place.DataProviderEditPlace;
import com.simple.original.client.proxy.SqlConnectionProxy;
import com.simple.original.client.view.IDataProvidersView;

public class DataProvidersActivity extends AbstractActivity<DataProviderEditPlace, IDataProvidersView> implements IDataProvidersView.Presenter {



	private RequestFactoryEditorDriver<SqlConnectionProxy, ?> driver;

	@Inject
	public DataProvidersActivity(IDataProvidersView view) {
		super(view);
	}

	@Override
	protected void bindToView() {
		display.setPresenter(this);
	}

	private void createAndEdit() {
	
	}


	@Override
	public void onTestConnections(Set<SqlConnectionProxy> selectedSet) {
	}

	@Override
	public void onAddDataProvider() {
		placeController().goTo(new DataProviderEditPlace());
	}
}