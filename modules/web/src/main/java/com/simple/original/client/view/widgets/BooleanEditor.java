package com.simple.original.client.view.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.user.client.ui.ListBox;

public class BooleanEditor extends ListBox implements LeafValueEditor<Boolean> {

    public BooleanEditor() {
        addItem("True");
        addItem("False");
    }

    @Override
    public void setValue(Boolean value) {
        if (value == null) {
            setSelectedIndex(1); // set it to false
        } else {
            setSelectedIndex(value ? 0 : 1);
        }
    }

    @Override
    public Boolean getValue() {
        Boolean value = (getSelectedIndex() == 0) ? true : false;
        return value;
    }
}
