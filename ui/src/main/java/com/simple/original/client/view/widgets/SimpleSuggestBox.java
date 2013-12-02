package com.simple.original.client.view.widgets;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;

public class SimpleSuggestBox extends TextBox implements KeyUpHandler {
	
	public interface SearchOracle {
		void search(String search);
	}

	private SearchOracle oracle;

	public SimpleSuggestBox() {
		addKeyUpHandler(this);
	}

	public SimpleSuggestBox(SearchOracle oracle) {
		this();
		this.oracle = oracle;
	}

	public void onKeyUp(KeyUpEvent event) {
		refreshSuggestions();
	}

	protected void refreshSuggestions() {
		oracle.search(this.getText());
	}

	public void setSearchOracle(SearchOracle oracle) {
		this.oracle = oracle;
	}

	public SearchOracle getSearchOracle() {
		return oracle;
	}
}
