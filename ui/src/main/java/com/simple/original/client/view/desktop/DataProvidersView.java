/**
 * 
 */
package com.simple.original.client.view.desktop;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.dataprovider.HttpDataProvider;
import com.simple.original.client.proxy.SqlConnectionProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IDataProvidersView;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * @author chinshaw
 * 
 */
public class DataProvidersView extends AbstractView implements IDataProvidersView {

	class DataProviderCell extends AbstractCell<DataProvider> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, DataProvider value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value.getVariableName());
		}
	};

	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	@UiTemplate("DataProvidersView.ui.xml")
	public interface Binder extends UiBinder<Widget, DataProvidersView> {
	}

	private MultiSelectionModel<SqlConnectionProxy> selectionModel;

	private Presenter presenter;

	@UiField(provided = true)
	CellList<DataProvider> dataProviders;

	@UiField(provided = true)
	SimplePager pager = new SimplePager(TextLocation.RIGHT);

	/**
	 * @param eventBus
	 * @param resources
	 */
	@Inject
	public DataProvidersView(EventBus eventBus, Resources resources) {
		super(eventBus, resources);
		dataProviders = new CellList<DataProvider>(new DataProviderCell());
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.client.view.DataProvidersView#setPresenter(com.simple
	 * .original.client .view.DataProvidersView.Presenter)
	 */
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.desktop.ViewImpl#getErrorPanel()
	 */
	@Override
	protected ErrorPanel getErrorPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.client.view.desktop.ViewImpl#reset()
	 */
	@Override
	public void reset() {
	}

	@UiHandler("add")
	void onAdd(ClickEvent click) {
		presenter.onAddDataProvider();
	}

}
