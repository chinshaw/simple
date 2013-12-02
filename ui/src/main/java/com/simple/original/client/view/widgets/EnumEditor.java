package com.simple.original.client.view.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.ui.ListBox;

public class EnumEditor<T extends Enum<T>> extends ListBox implements LeafValueEditor<T> {

    public EnumEditor() {
    }

    private Class<T> clazz;
    private Map<T, Integer> index = new HashMap<T, Integer>();

    public EnumEditor(Class<T> e) {
        super();
        this.clazz = e;
        int idx = 0;
        for (T t : e.getEnumConstants()) {
            this.addItem(t.toString());
            index.put(t, idx);
            idx++;
        }
    }

    @Override
    public void setValue(T value) {
        if (value == null) {
            setSelectedIndex(-1);
        } else {
            setSelectedIndex(index.get(value));
        }
        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), this);
    }

    @Override
    public T getValue() {
        int idx = getSelectedIndex();
        if (idx == -1)
            return null;
        else {
            return Enum.valueOf(clazz, getItemText(idx));
        }
    }
}
