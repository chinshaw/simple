package com.simple.original.client.view.widgets;

import java.util.List;

import com.google.gwt.editor.client.CompositeEditor;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDelegate;
import com.simple.original.client.view.widgets.ListEditorWrapper.EditorProvider;

public class PolymorphicListEditor<T,  E extends Editor<T>> implements CompositeEditor<List<T>, T, E>{

    private CompositeEditor.EditorChain<T, E> chain;
    private ListEditorWrapper<T, E> listWrapper;
    private EditorProvider<T, E> editorProvider;
  
    
    public PolymorphicListEditor(EditorProvider<T,E> editorProvider) {
        this.editorProvider = editorProvider;
    }
    
    @Override
    public void flush() {
        listWrapper.flush();
    }

    @Override
    public void onPropertyChange(String... paths) {}
    
    
    @Override
    public void setValue(List<T> value) {
        if (listWrapper != null) {
            // Having entire value reset, so dump the wrapper gracefully
            listWrapper.detach();
        }
        if (value == null) {
            listWrapper = null;
        } else {
            listWrapper = new ListEditorWrapper<T, E>(value, chain, editorProvider);
            listWrapper.attach();
        }
    }


    @Override
    public void setDelegate(EditorDelegate<List<T>> delegate) {}

    @Override
    public String getPathElement(E subEditor) {
        return "[" + listWrapper.getEditors().indexOf(subEditor) + "]";
    }

    @Override
    public void setEditorChain(com.google.gwt.editor.client.CompositeEditor.EditorChain<T, E> chain) {
        this.chain = chain;
    }

    @Override
    public E createEditorForTraversal() {
        E toReturn = editorProvider.createBaseEditor();
        editorProvider.dispose(toReturn);
        return toReturn;
    }
    
    public List<T> getList() {
        return listWrapper;
    }
}
