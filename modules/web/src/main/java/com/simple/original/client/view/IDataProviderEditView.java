/**
 * 
 */
package com.simple.original.client.view;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.simple.domain.model.dataprovider.HttpDataProvider;

/**
 * @author chinshaw
 * 
 */
public interface IDataProviderEditView extends IView {

	public interface Presenter {

		void onSave();

	}

	void setPresenter(Presenter presenter);

	SimpleBeanEditorDriver<HttpDataProvider, ?> getEditorDriver();
}
