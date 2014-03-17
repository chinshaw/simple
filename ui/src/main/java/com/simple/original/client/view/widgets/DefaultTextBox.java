package com.simple.original.client.view.widgets;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.TextBox;

/**
 * A text box that displays default text.
 */
public class DefaultTextBox extends TextBox {

	private final String defaultText;

	public @UiConstructor
	DefaultTextBox(final String defaultText) {
		this.defaultText = defaultText;
		resetDefaultText();

		// Add focus and blur handlers.
		addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				getElement().getStyle().clearColor();
				if (defaultText.equals(getValue())) {
					setValue("");
				}
			}
		});
		addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				if ("".equals(getText())) {
					resetDefaultText();
				}
			}
		});
	}
	public void setText(String text) {
		setValue(text);
	}

	public void setValue(String text) {
		if (text == null || text.isEmpty()) {
			resetDefaultText();
		} else {
			super.setText(text);
			removeStyleName("disabled");
		}
	}

	public String getDefaultText() {
		return defaultText;
	}

	/**
	 * Reset the text box to the default text.
	 */
	public void resetDefaultText() {
		setText(defaultText);
		addStyleName("disabled");
	}
}
