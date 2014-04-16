package com.simple.original.client.view.desktop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.client.HasRequestContext;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.UIDateInputModelProxy;
import com.simple.original.client.proxy.UIUserInputModelProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.desktop.CompositeAnalyticsOperationInputEditor.BaseInputEditor;
import com.simple.original.client.view.desktop.InputsEditor.UIDateInputEditor;
import com.simple.original.client.view.widgets.PolymorphicListEditor;
import com.simple.original.client.view.widgets.ListEditorWrapper.EditorProvider;

/**
 * This is a pretty complicated class but it really has to be because of all the
 * different input pieces it handles. This class is in charge of Edting script
 * input's along with Default values. Since GWT does not have a nice way of
 * editing a complicated list of objects here we are.
 * 
 * @author chinshaw
 * 
 */
public class CompositeAnalyticsOperationInputEditor extends Composite implements  IsEditor<PolymorphicListEditor<AnalyticsOperationInputProxy, BaseInputEditor>>, HasRequestContext<AnalyticsOperationInputProxy>  {

    /**
     * This is the presenter used for adding inputs and removing inputs.
     * 
     * @author chinshaw
     * 
     */
    public interface Presenter {
        void onAddInput();
    }

    @UiTemplate("UIUserInputEditor.ui.xml")
    public interface UiUserInputBinder extends UiBinder<Widget, UIUserInputEditor> {
    }

    public static interface BaseInputEditor extends ValueAwareEditor<AnalyticsOperationInputProxy> {

        ValueBoxBase<String> displayName();

        ValueBoxBase<String> inputName();

        Widget asWidget();
    }

    public class UIUserInputEditor extends Composite implements BaseInputEditor {

        @UiField
        @Editor.Ignore
        TextBox inputType;

        @UiField
        @Path("required")
        CheckBox isRequired;

        @UiField
        TextBox inputName;

        @UiField
        TextBox value;

        @UiField
        TextBox displayName;

        @UiField
        Button removeInput;

        @UiField
        DivElement inputValueContainer;

        // @UiField(provided = true)
        // DefinedInputEditor definedInputs = new DefinedInputEditor();

        private UIUserInputModelProxy modelValue;

        public UIUserInputEditor(AnalyticsOperationInputProxy value) {
            initWidget(GWT.<UiUserInputBinder> create(UiUserInputBinder.class).createAndBindUi(this));
            isRequired.setValue(false);
        }

        @UiHandler("removeInput")
        void removeInput(ClickEvent event) {
            asEditor().getList().remove(modelValue);
        }

        @Override
        public ValueBoxBase<String> displayName() {
            return displayName;
        }

        @Override
        public Widget asWidget() {
            return this;
        }

        @Override
        public ValueBoxBase<String> inputName() {
            return inputName;
        }

        @Override
        public void flush() {
            // TODO
        }

        @Override
        public void onPropertyChange(String... paths) {}

        @Override
        public void setValue(AnalyticsOperationInputProxy modelValue) {
            this.modelValue = (UIUserInputModelProxy) modelValue;

        }

        @Override
        public void setDelegate(EditorDelegate<AnalyticsOperationInputProxy> delegate) {

        }
    }

    public static class DefinedInputEditor extends Composite implements IsEditor<TakesValueEditor<String>>, TakesValue<String> {

        private TakesValueEditor<String> editor;
        private String currentValue;

        private Label label = new Label();
        private FlowPanel flowPanel = new FlowPanel();
        PushButton delete;

        public DefinedInputEditor() {
            initWidget(flowPanel);

            delete = new PushButton(new Image(resources.minusSmall()));

            flowPanel.add(label);
            flowPanel.add(delete);
        }

        @Override
        public TakesValueEditor<String> asEditor() {
            if (editor == null) {
                editor = TakesValueEditor.of(this);
            }
            return editor;
        }

        @Override
        public void setValue(String value) {
            label.setText(value);
            this.currentValue = value;
        }

        @Override
        public String getValue() {
            return currentValue;
        }

        public HasClickHandlers getDeleteButton() {
            return delete;
        }
    }

    public static class DefinedInputsEditor extends Composite implements IsEditor<ListEditor<String, TakesValueEditor<String>>> {

        private ListEditor<String, TakesValueEditor<String>> editor;
        private FlowPanel container = new FlowPanel();

        public DefinedInputsEditor() {
            initWidget(container);

            editor = ListEditor.of(new EditorSource<TakesValueEditor<String>>() {

                public List<TakesValueEditor<String>> create(int count, int index) {
                    container.clear();
                    List<TakesValueEditor<String>> toReturn = new ArrayList<TakesValueEditor<String>>();
                    for (int i = 0; i < count; i++) {
                        TakesValueEditor<String> editor = create(index + i);
                        toReturn.add(editor);
                    }
                    return toReturn;
                }

                @Override
                public TakesValueEditor<String> create(int index) {
                    final DefinedInputEditor definedInputEditor = new DefinedInputEditor();
                    definedInputEditor.getDeleteButton().addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            editor.getList().remove(definedInputEditor.getValue());
                            container.remove(definedInputEditor);
                        }

                    });

                    container.insert(definedInputEditor, index);
                    return definedInputEditor.asEditor();
                }
            });
        }

        @Override
        public ListEditor<String, TakesValueEditor<String>> asEditor() {
            return editor;
        }
    }

    /**
     * Work around hack to get the polymorphic bean editor.
     * 
     * @author chinshaw
     */
    static interface UIUserInputDriver extends SimpleBeanEditorDriver<UIUserInputModelProxy, BaseInputEditor> {
    }

    /**
     * This is our polymorphic editor provider, it's purpose is to create and
     * editor based on a specific type.
     */
    EditorProvider<AnalyticsOperationInputProxy, BaseInputEditor> editorProvider = new EditorProvider<AnalyticsOperationInputProxy, BaseInputEditor>() {

        
        @Override
        public BaseInputEditor create(AnalyticsOperationInputProxy value) {
            BaseInputEditor editor = null;
            if (value instanceof UIUserInputModelProxy) {
                editor = new UIUserInputEditor(value);

                // This is a temporary hack to help flush the value back.
                container.add(editor.asWidget());
            }
            return editor;
        }

        @Override
        public void setIndex(BaseInputEditor e, int i) {
            container.insert(((BaseInputEditor) e).asWidget(), i);
        }

        @Override
        public void dispose(BaseInputEditor subEditor) {
            container.remove(((BaseInputEditor) subEditor).asWidget());
        }

        @Override
        public Collection<? extends BaseInputEditor> create(List<AnalyticsOperationInputProxy> values) {
            List<BaseInputEditor> toReturn = new ArrayList<BaseInputEditor>(values.size());
            for (AnalyticsOperationInputProxy value : values) {
                toReturn.add(create(value));
            }
            return toReturn;
        }

        @Override
        public BaseInputEditor createBaseEditor() {
            return new UIUserInputEditor(null);
        }

        @Override
        public SimpleBeanEditorDriver<AnalyticsOperationInputProxy, BaseInputEditor> getEditorDriver(AnalyticsOperationInputProxy value) {
            if (value instanceof UIUserInputModelProxy) {
                return GWT.create(UIUserInputEditor.class);
            } else if ( value instanceof UIDateInputModelProxy) {
                return GWT.create(UIDateInputEditor.class);
            }
            return null;
        }

    };

    /**
     * This is the container for the ScriptInput Widgets
     */
    private FlowPanel container = new FlowPanel();

    /**
     * Our required presenter to handle adding a new ScriptInputProxy class to
     * the view.
     */
    private final Presenter presenter;
    /**
     * Resources needed for drawing images.
     */
    private static Resources resources;

    private PolymorphicListEditor<AnalyticsOperationInputProxy, BaseInputEditor> listEditor = new PolymorphicListEditor<AnalyticsOperationInputProxy, BaseInputEditor>(
            editorProvider);

    public CompositeAnalyticsOperationInputEditor(Presenter presenter, Resources res) {
        this.presenter = presenter;
        resources = res;
        initWidget(container);
    }

    /**
     * This is called from the InputEditor class to allow a presenter to add a
     * new ScriptInputProxy class to the ScriptEditor's List of proxies.
     */
    protected void onAddInput() {
        presenter.onAddInput();
    }

    @Override
    public PolymorphicListEditor<AnalyticsOperationInputProxy, BaseInputEditor> asEditor() {
        return listEditor;
    }

    @Override
    public void setRequestContext(RequestContext ctx) {
        // TODO Auto-generated method stub
        
    }
}