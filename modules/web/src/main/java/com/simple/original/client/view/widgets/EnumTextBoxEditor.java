package com.simple.original.client.view.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;

public class EnumTextBoxEditor<T extends Enum<T>> extends Composite implements LeafValueEditor<T> {

    private TextBox textBox = new TextBox();
    private Class<T> clazz;

    public EnumTextBoxEditor() {
        super();
        initWidget(textBox);
    }

    public EnumTextBoxEditor(Class<T> e) {
        super();
        this.clazz = e;
        initWidget(textBox);
    }

    @Override
    public void setValue(T value) {
        textBox.setValue(value.name());
    }

    @Override
    public T getValue() {
        return Enum.valueOf(clazz, textBox.getValue());
    }
    
    public void setReadOnly(boolean readOnly) {
        textBox.setReadOnly(readOnly);
    }
    
    public boolean getReadOnly() {
        return textBox.isReadOnly();
    }
}
