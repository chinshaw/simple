package com.simple.original.client.view.widgets;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.editor.client.CompositeEditor;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.adapters.ListEditor;

/**
 * Synchronizes a list of objects and a list of Editors.
 * 
 * @param <T>
 *            the type of data being edited
 * @param <E>
 *            the type of Editor
 */
public class ListEditorWrapper<T, E extends Editor<T>> extends AbstractList<T> {

    public interface EditorProvider<T, E extends Editor<T>> {
        E create(T value);
        
        E createBaseEditor();

        void setIndex(E e, int i);

        void dispose(E subEditor);
        
        // This is a hack to fix the gwt editor inheritence problem. Should go away later on.
        SimpleBeanEditorDriver<T, E> getEditorDriver(T t);

        Collection<? extends E> create(List<T> values);
    }
    
    private final List<T> backing;
    private final CompositeEditor.EditorChain<T, E> chain;
    private final List<E> editors;
    private final EditorProvider<T, E> editorSource;
    private final List<T> workingCopy;
    private List<SimpleBeanEditorDriver<T, ? extends E>> drivers;
    
    private static final Logger logger = Logger.getLogger(ListEditorWrapper.class.getName());
    
    public ListEditorWrapper(List<T> backing, CompositeEditor.EditorChain<T, E> chain, EditorProvider<T,E> editorSource) {
        this.backing = backing;
        this.chain = chain;
        this.editorSource = editorSource;
        workingCopy = new ArrayList<T>(backing);
        drivers = new ArrayList<SimpleBeanEditorDriver<T, ? extends E>>(backing.size());
        editors = new ArrayList<E>(backing.size());
    }

    @Override
    public void add(int index, T element) {
        logger.info("Adding object to listWrapper " + element);
        workingCopy.add(index, element);
        E subEditor = editorSource.create(element);
        editors.add(index, subEditor);
        for (int i = index + 1, j = editors.size(); i < j; i++) {
            editorSource.setIndex(editors.get(i), i);
        }
        
        chain.attach(element, subEditor);
    }

    @Override
    public T get(int index) {
        return workingCopy.get(index);
    }

    @Override
    public T remove(int index) {
        T toReturn = workingCopy.remove(index);
        E subEditor = editors.remove(index);
        editorSource.dispose(subEditor);
        for (int i = index, j = editors.size(); i < j; i++) {
            editorSource.setIndex(editors.get(i), i); 
        }
        chain.detach(subEditor);
        return toReturn;
    }

    @Override
    public T set(int index, T element) {
        T toReturn = workingCopy.set(index, element);
        chain.attach(element, editors.get(index));
        return toReturn;
    }

    @Override
    public int size() {
        return workingCopy.size();
    }

    /**
     * Must be called after construction. This is a two-phase initialization so
     * that ListEditor can assign its list field before any sub-editors might
     * call {@link ListEditor#getList()}
     */
    void attach() {
        editors.addAll(editorSource.create(workingCopy));
        for (int i = 0, j = workingCopy.size(); i < j; i++) {
            SimpleBeanEditorDriver<T, E> driver = editorSource.getEditorDriver(workingCopy.get(i));
            driver.initialize(editors.get(i));
            driver.edit(workingCopy.get(i));
            drivers.add(driver);
            //chain.attach(workingCopy.get(i), editors.get(i));
        }
    }


    void detach() {
        for (int i = 0, j = editors.size(); i < j; i++) {
            drivers.remove(drivers.get(i));
            //chain.detach(editors.get(i));
            editorSource.dispose(editors.get(i));
        }
    }

    void flush() { 
        for (int i = 0, j = workingCopy.size(); i < j; i++) {
            SimpleBeanEditorDriver<T, ? extends E> driver = drivers.get(i);
            T value = driver.flush();
            
            // Use of object-identity intentional
            if (workingCopy.get(i) != value) {
                workingCopy.set(i, value);
            }
        }
        drivers.clear();
        backing.clear();
        backing.addAll(workingCopy);
    }

    /**
     * For testing only.
     */
    List<? extends E> getEditors() {
        return editors;
    }
}