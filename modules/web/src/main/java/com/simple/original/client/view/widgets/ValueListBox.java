package com.simple.original.client.view.widgets;

import java.util.List;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.view.client.ProvidesKey;

public class ValueListBox<T> extends com.google.gwt.user.client.ui.ValueListBox<T> implements HasEditorErrors<T> {

    private Element errorLabel = DOM.createDiv();
    
    public ValueListBox(Renderer<T> renderer) {
        super(renderer);
     }
    
    public ValueListBox(Renderer<T> renderer, ProvidesKey<T> keyProvider) {
        super(renderer, keyProvider);
     
    }

    /**
     * This is an ugly hack to get constraint violations working for now.
     * 
     */
    @Override
    public void showErrors(List<EditorError> errors) {
        getElement().getParentElement().appendChild(errorLabel);
        
        StringBuilder sb = new StringBuilder();
        for (EditorError error : errors) {
          if (error.getEditor().equals(this.asEditor())) {
            sb.append("\n").append(error.getMessage());
          }
        }

        if (sb.length() == 0) {
          errorLabel.setInnerText("");
          errorLabel.getStyle().setDisplay(Display.NONE);
          return;
        }

        errorLabel.setInnerText(sb.substring(1));
        errorLabel.getStyle().setDisplay(Display.INLINE_BLOCK);
    }
}
