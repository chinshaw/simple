package com.simple.original.client.view.desktop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.UIComplexInputModelProxy;
import com.simple.original.client.proxy.UIDateInputModelProxy;
import com.simple.original.client.proxy.UIUserInputModelProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IOperationBuilderView.AnalyticsTaskInputType;
import com.simple.original.client.view.widgets.AnimationBuilder;
import com.simple.original.client.view.widgets.AnimationBuilder.AnimationCompletionHandler;

public class InputsEditorWorking extends AbstractInputsEditor {

	private static final Logger logger = Logger.getLogger("InputsEditorWorking");
	
    public abstract class BaseInputEditor<T> extends Composite implements BaseEditor<T> {

        private int index;
        
        public BaseInputEditor(int index) {
            this.index = index;
        }

        @UiField
        @Editor.Ignore
        TextBox inputType;

        @UiField
        TextBox inputName;

        @UiField
        TextBox displayName;

        @UiField
        Button removeInput;

        @UiHandler("removeInput")
        void removeInput(ClickEvent event) {
            removeUserInput(index);
        }

        public void setIndex(int index) {
            this.index = index;
        }

		@Override
		public Editor<String> displayName() {
			return displayName.asEditor();
		}

		@Override
		public Editor<String> inputName() {
			return inputName.asEditor();
		}
    }	

    @UiTemplate("UIDateInputEditor.ui.xml")
    public interface UiDateInputBinder extends UiBinder<Widget, UIDateInputEditor> {
    }
    
    public class UIDateInputEditor extends BaseInputEditor<UIDateInputModelProxy> implements DateEditor {
    	
        @UiField(provided=true)
        DateBox value;

        @UiField
        DivElement inputValueContainer;
        
        private DateEditorDriver driver = GWT.create(DateEditorDriver.class);

        public UIDateInputEditor(int index) {
            super(index);
            value = new DateBox();
            DefaultFormat dateFormat = new DefaultFormat(DateTimeFormat.getFormat("MM/d/yy"));
            value.setFormat(dateFormat);
            initWidget(GWT.<UiDateInputBinder> create(UiDateInputBinder.class).createAndBindUi(this));
            inputType.setValue(AnalyticsTaskInputType.DATE_INPUT.getDisplayValue());
            driver.initialize(this);
        }

		@Override
		public Editor<Date> value() {
			return value.asEditor();
		}
		
		public SimpleBeanEditorDriver<UIDateInputModelProxy, ?> getDriver() {
			return driver;
		}
    }

    @UiTemplate("UIUserInputEditor.ui.xml")
    public interface UiUserInputBinder extends UiBinder<Widget, UIUserInputEditor> {
    }
    
    public class UIUserInputEditor extends BaseInputEditor<UIUserInputModelProxy> implements StringEditor {

        @UiField
        TextBox value;

        @UiField
        DivElement inputValueContainer;

        @UiField
        DefinedInputsEditor definedInputs;
        
        private final StringEditorDriver driver = GWT.create(StringEditorDriver.class);

        public UIUserInputEditor(int index) {
            super(index);
            initWidget(GWT.<UiUserInputBinder> create(UiUserInputBinder.class).createAndBindUi(this));
            inputType.setValue(AnalyticsTaskInputType.TEXT_INPUT.getDisplayValue());
            driver.initialize(this);
        }

		@Override
		public Editor<String> value() {
			return value.asEditor();
		}

		@Override
		public Editor<List<String>> definedInputs() {
			return definedInputs;
		}

		@Override
		public SimpleBeanEditorDriver<UIUserInputModelProxy, ?> getDriver() {
			return driver;
		}
    }
    
    /**
     * This is our UI Complex input editor
     * 
     * @author chinshaw
     * 
     */
    @UiTemplate("UIComplexInputEditor.ui.xml")
    public interface UiComplexInputBinder extends UiBinder<Widget, UIComplexInputEditor> {
    }

    public class UIComplexInputEditor extends BaseInputEditor<UIComplexInputModelProxy> implements ComplexEditor {

    	@UiField
        InputsEditorWorking inputs;
        
    	private final ComplexEditorDriver driver = GWT.create(ComplexEditorDriver.class);
    	
        public UIComplexInputEditor(int index) {
            super(index);
            initWidget(GWT.<UiComplexInputBinder> create(UiComplexInputBinder.class).createAndBindUi(this));
            inputType.setValue(AnalyticsTaskInputType.COMPLEX_INPUT.getDisplayValue());
            inputs.setRequestContext(context);
            driver.initialize(this);
        }

		@Override
		public Editor<List<AnalyticsOperationInputProxy>> inputs() {
			return inputs;
		}

		@Override
		public SimpleBeanEditorDriver<UIComplexInputModelProxy, ?> getDriver() {
			return driver;
		}
    }
    

    /**
     * The is the defined inputs editor which is used to add defined inputs to a ui string input.
     * This will allow the user to make a multi select when running a task.
     * @author chinshaw
     *
     */
    public static class DefinedInputsEditor extends Composite implements ValueAwareEditor<List<String>> {

        private List<String> backing;
        private FlowPanel container = new FlowPanel();
        private List<LeafValueEditor<String>> holder = new ArrayList<LeafValueEditor<String>>();
        private TextBox addDefinedInput = new TextBox();

        public DefinedInputsEditor() {
            initWidget(container);
            container.add(addDefinedInput);
            addDefinedInput.addKeyUpHandler(new KeyUpHandler() {

                @Override
                public void onKeyUp(KeyUpEvent event) {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                        String value = addDefinedInput.getValue();
                        if (!value.isEmpty()) {
                            addInput(addDefinedInput.getValue());
                            addDefinedInput.setValue("");
                        }
                    }
                }
            });
        }

        @Override
        public void setDelegate(EditorDelegate<List<String>> delegate) {
        }

        @Override
        public void flush() {
            this.backing.clear();
            for (LeafValueEditor<String> value : holder) {
                this.backing.add(value.getValue());
            }
        }

        @Override
        public void onPropertyChange(String... paths) {
        }

        @Override
        public void setValue(List<String> values) {
            this.backing = values;

            for (String value : backing) {
                addInput(value);
            }
        }

        private void addInput(String value) {
            final FlowPanel flowPanel = new FlowPanel();
            final Label label = new Label(value);

            Button delete = new Button();
            delete.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    container.remove(flowPanel);
                    holder.remove(label.asEditor());
                }
            });

            flowPanel.add(label);
            flowPanel.add(delete);
            
            holder.add(label.asEditor());

            container.add(flowPanel);
        }
    }


    @UiTemplate("AnalyticsInputsView.ui.xml")
    public interface Binder extends UiBinder<Widget, InputsEditorWorking> {
    }

    /**
     * Selectable list box that allows user to add input to the view.
     */
    @Ignore
    @UiField
    public ListBox availableInputTypes;

    /**
     * Container that holds the inputs.
     */
    @UiField
    FlowPanel container;
    
    private RequestContext context;

    @UiConstructor
    public InputsEditorWorking(Resources resources) {
    	super(resources);
        initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

        
        AnalyticsTaskInputType[] userInputs = AnalyticsTaskInputType.values();

        for (AnalyticsTaskInputType input : userInputs) {
            availableInputTypes.addItem(input.getDisplayValue(), input.name());
        }
    }

    private void onAddInput() {
        AnalyticsTaskInputType type = AnalyticsTaskInputType.valueOf(availableInputTypes.getValue(availableInputTypes.getSelectedIndex()));
        AnalyticsOperationInputProxy input = null;

        switch (type) {
        case TEXT_INPUT:
            input = context.create(UIUserInputModelProxy.class);
            ((UIUserInputModelProxy) input).setDefinedInputs(new ArrayList<String>());
            addInput(input);
            break;
        case DATE_INPUT:
            input = context.create(UIDateInputModelProxy.class);
            addInput(input);
            break;
        case COMPLEX_INPUT:
            input = context.create(UIComplexInputModelProxy.class);
            addInput(input);
            break;
        default:
            throw new RuntimeException("Tried to add unhandled type of ui input to view: " + type.name());
        }
    }

    /**
     * UiHandler that handles the click event from the ui to add a user input.
     * @param event
     */
    @UiHandler("addInput")
    void addUserInput(ClickEvent event) {
        onAddInput();
    }

    /**
     * Removes the selected input from the view, this will also remove the input from the editors and drivers.
     * It also has to reorder the editors so that we don't get a null pointer if you delete a value in the middle of the list and then
     * delete one at the end.
     * @param index
     */
    private void removeUserInput(final int index) {
        AnimationBuilder.create(editors.get(index).asWidget().getElement()).addFadeOutAnimation().addSlideUpAnimation().addCompletionHandler(new AnimationCompletionHandler() {

            @Override
            public void onAnimationsComplete() {
                editors.get(index).asWidget().removeFromParent();
                //container.remove(index);
                editors.remove(index);
                // reorder the editors in the list so that the next revove removes the
                // correct value.
                for (int i = index, j = editors.size(); i < j; i++) {
                    BaseEditor<?> editor = editors.get(i);
                    editor.setIndex(i);
                }
            }
        }).run(500);
    }



    /**
     * Not implemented.
     */
    @Override
    public void onPropertyChange(String... paths) {}

    /**
     * Not implemented
     */
    @Override
    public void setDelegate(EditorDelegate<List<AnalyticsOperationInputProxy>> delegate) {}

    @Override
    public void setValue(List<AnalyticsOperationInputProxy> values) {
        // Setting the backing inputs that will be returned to the editor.
        this.inputs = values;
        // Clear the inputs container so that we can add our new values. This is
        // just in case we are reusing the editor.
        container.clear();
        
        // Clear the editors so that we don't have any extra laying around.
        // This was causing a bug where editors inputs were being duplicated.
        editors.clear();
        
        for (AnalyticsOperationInputProxy input : inputs) {
            addInput(input);
        }
    }

    /**
     * Used to add an input to the view. This also adds it to the list of editors.
     * @param input
     */
    private void addInput(AnalyticsOperationInputProxy input) {
        BaseEditor<? extends AnalyticsOperationInputProxy> editor = null;
        if (input instanceof UIUserInputModelProxy) {
            editor = initStringEditor((UIUserInputModelProxy) input);
        } else if (input instanceof UIDateInputModelProxy) {
            editor = initDateEditor((UIDateInputModelProxy) input);
        } else if (input instanceof UIComplexInputModelProxy) {
            editor = initComplexEditor((UIComplexInputModelProxy) input);
        } else {
            throw new RuntimeException("Editor type was not implemented " + input);
        }
        
        editors.add(editor);
        
        AnimationBuilder builder = AnimationBuilder.create(editor.asWidget().getElement()).addFadeInAnimation().addSlideDownAnimation();
        container.add(editor);
        builder.run(500);
    }


	@Override
	public ComplexEditor createComplexEditor(int index) {
		return new UIComplexInputEditor(index);
	}

	@Override
	public StringEditor createStringEditor(int index) {
		return new UIUserInputEditor(index);
	}

	@Override
	public DateEditor createDateEditor(int index) {
		return new UIDateInputEditor(index);
	}

    /**
     * Our request context.
     */
    @Override
    public void setRequestContext(RequestContext context) {
    	logger.info("Request context is " + context);
        this.context = context;
    }
    
}