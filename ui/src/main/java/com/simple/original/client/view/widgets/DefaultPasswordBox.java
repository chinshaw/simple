package com.simple.original.client.view.widgets;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.PasswordTextBox;

/**
 * A text box that displays default text.
 */
public class DefaultPasswordBox extends PasswordTextBox {
  
    /**
     * The text color used when the box is disabled and empty.
     */
    private static final String TEXTBOX_DISABLED_COLOR = "#aaaaaa";

    private final String defaultText;
    
    public @UiConstructor DefaultPasswordBox(final String defaultText) {
      this.defaultText = defaultText;
      resetDefaultText();

      // Add focus and blur handlers.
      addFocusHandler(new FocusHandler() {
        public void onFocus(FocusEvent event) {
          getElement().getStyle().clearColor();
          if (defaultText.equals(getText())) {
            setText("");
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

    public String getDefaultText() {
      return defaultText;
    }

    /**
     * Reset the text box to the default text.
     */
    public void resetDefaultText() {
      setText(defaultText);
      getElement().getStyle().setColor(TEXTBOX_DISABLED_COLOR);
    }
  }
