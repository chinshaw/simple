package com.simple.original.client.view.widgets;

import java.util.List;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.editor.ui.client.adapters.ValueBoxEditor;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.ValueListBox;

/**
 * This is a copy of the original ValueBoxEditorDecorator in the gwt source The
 * reason we are not using it is because it did not support laying out the error
 * panel in a different location.
 * 
 * 
 * A simple decorator to display leaf widgets with an error message.
 * <p>
 * <h3>Use in UiBinder Templates</h3>
 * <p>
 * The decorator may have exactly one ValueBoxBase added though an
 * <code>&lt;e:valuebox></code> child tag.
 * <p>
 * For example:
 * 
 * <pre>
 * &#064;UiField
 * ValueBoxEditorDecorator&lt;String&gt; name;
 * </pre>
 * 
 * <pre>
 * &lt;e:ValueBoxEditorDecorator ui:field='name'>
 *   &lt;e:valuebox>
 *     &lt;g:TextBox />
 *   &lt;/e:valuebox>
 * &lt;/e:ValueBoxEditorDecorator>
 * </pre>
 * 
 * @param <T>
 *            the type of data being edited
 */

public class ValueListBoxEditorDecorator<T> extends Composite implements HasEditorErrors<T>, IsEditor<TakesValueEditor<T>> {

    /**
     * The location of the text relative to the paging buttons.
     */
    public static enum ErrorPanelLocation {
        LEFT, RIGHT;
    }

    SimplePanel contents = new SimplePanel();

    @Ignore
    Label errorLabel = new Label();

    HorizontalPanel layout = new HorizontalPanel();

    private TakesValueEditor<T> editor;

    /**
     * Constructs a ValueBoxEditorDecorator.
     */
    @UiConstructor
    public ValueListBoxEditorDecorator(ErrorPanelLocation errorLocation) {
        initWidget(layout);
        setStyleName("gwt-ValueBoxEditorDecorator");
        errorLabel.setStyleName("gwt-ValueBoxEditorDecorator-error");
        errorLabel.getElement().getStyle().setDisplay(Display.NONE);

        if (errorLocation == ErrorPanelLocation.RIGHT) {
            layout.add(contents);
            layout.add(errorLabel);
        } else {
            layout.add(errorLabel);
            layout.add(contents);
        }
    }

    /**
     * Constructs a ValueBoxEditorDecorator using a {@link ValueBoxBase} widget
     * and a {@link ValueBoxEditor} editor.
     * 
     * @param widget
     *            the widget
     * @param editor
     *            the editor
     */
    public ValueListBoxEditorDecorator(ValueListBox<T> widget, TakesValueEditor<T> editor) {
        this(ErrorPanelLocation.RIGHT);
        contents.add(widget);
        this.editor = editor;
    }

    /**
     * Returns the associated {@link ValueBoxEditor}.
     * 
     * @return a {@link ValueBoxEditor} instance
     * @see #setEditor(ValueBoxEditor)
     */
    public TakesValueEditor<T> asEditor() {
        return editor;
    }

    /**
     * Sets the associated {@link ValueBoxEditor}.
     * 
     * @param editor
     *            a {@link ValueBoxEditor} instance
     * @see #asEditor()
     */
    public void setEditor(ValueBoxEditor<T> editor) {
        this.editor = editor;
    }

    /**
     * Set the widget that the EditorPanel will display. This method will
     * automatically call {@link #setEditor}.
     * 
     * @param widget
     *            a {@link ValueBoxBase} widget
     */
    @UiChild(limit = 1, tagname = "valuebox")
    public void setValueBox(ValueBoxBase<T> widget) {
        contents.add(widget);
        setEditor(widget.asEditor());
    }

    public void clearErrors() {
        errorLabel.setText("");
        errorLabel.getElement().getStyle().setDisplay(Display.NONE);
    }

    /**
     * The default implementation will display, but not consume, received errors
     * whose {@link EditorError#getEditor() getEditor()} method returns the
     * Editor passed into {@link #setEditor}.
     * 
     * @param errors
     *            a List of {@link EditorError} instances
     */
    public void showErrors(List<EditorError> errors) {
        StringBuilder sb = new StringBuilder();
        for (EditorError error : errors) {
            if (error.getEditor().equals(editor)) {
                sb.append("\n").append(error.getMessage());
            }
        }

        if (sb.length() == 0) {
            clearErrors();
            return;
        }

        errorLabel.setText(sb.substring(1));
        errorLabel.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
    }
}
