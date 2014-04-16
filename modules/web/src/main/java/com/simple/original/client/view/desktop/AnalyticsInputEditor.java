package com.simple.original.client.view.desktop;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.web.bindery.requestfactory.gwt.client.HasRequestContext;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.UIComplexInputModelProxy;
import com.simple.original.client.proxy.UIDateInputModelProxy;
import com.simple.original.client.proxy.UIUserInputModelProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.widgets.AnimationBuilder;
import com.simple.original.client.view.widgets.ValueListBox;

/**
 * @author chinshaw
 * 
 */
public class AnalyticsInputEditor extends Composite implements ValueAwareEditor<List<AnalyticsOperationInputProxy>>, HasRequestContext<List<AnalyticsOperationInputProxy>> {

	interface BaseInputEditor<T extends AnalyticsOperationInputProxy> extends IsWidget {
		public SimpleBeanEditorDriver<T, ?> getDriver();
	}

	abstract class BaseInputPanel<T extends AnalyticsOperationInputProxy> extends Composite implements BaseInputEditor<T> {

		private final int index;

		public BaseInputPanel(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

	}

	/**
	 * Editor driver for the script inputs. This will give us the ability to
	 * flush the driver to get the ScriptInputs from the view.
	 * 
	 * @author chinshaw
	 */
	interface ScriptInputDriver extends SimpleBeanEditorDriver<List<AnalyticsOperationInputProxy>, AnalyticsInputEditor> {
	}

	@UiTemplate("UserInputExec.ui.xml")
	public interface UiUserInputBinder extends UiBinder<Widget, StringInputEditor> {
	}

	public interface StringInputEditorDriver extends SimpleBeanEditorDriver<UIUserInputModelProxy, StringInputEditor> {
	}

	/**
	 * String input class. This will allow the user to select a string input.
	 * 
	 * @author chinshaw
	 */
	public class StringInputEditor extends BaseInputPanel<UIUserInputModelProxy> implements ValueAwareEditor<UIUserInputModelProxy> {

		@UiField
		FlowPanel valueContainer;

		@UiField
		LabelElement inputLabel;

		private StringInputEditorDriver driver = GWT.create(StringInputEditorDriver.class);

		TakesValueEditor<String> value;

		private UIUserInputModelProxy model = null;

		public StringInputEditor(int index) {
			super(index);
			initWidget(GWT.<UiUserInputBinder> create(UiUserInputBinder.class).createAndBindUi(this));
			driver.initialize(this);
		}

		private Widget createEditor(UIUserInputModelProxy input) {
			Widget valueWidget = null;
			if (input.getDefinedInputs().size() > 1) {
				ValueListBox<String> valueListBox = new ValueListBox<String>(new AbstractRenderer<String>() {

					@Override
					public String render(String object) {
						return (object == null) ? "Select..." : object;
					}
				});
				valueListBox.setAcceptableValues(input.getDefinedInputs());
				valueWidget = valueListBox;
				value = valueListBox.asEditor();
			} else {
				TextBox textBox = new TextBox();
				valueWidget = textBox;
				value = textBox.asEditor();
			}

			valueContainer.add(valueWidget);
			return valueWidget;
		}

		@Override
		public SimpleBeanEditorDriver<UIUserInputModelProxy, ?> getDriver() {
			return driver;
		}

		@Override
		public void setDelegate(EditorDelegate<UIUserInputModelProxy> delegate) {
		}

		@Override
		public void flush() {
			this.model.setDefinedInputs(null);
			this.model.setValue(value.getValue());
		}

		@Override
		public void onPropertyChange(String... paths) {
		}

		@Override
		public void setValue(UIUserInputModelProxy model) {
			this.model = model;
			valueContainer.add(createEditor(model));
			inputLabel.setInnerText(model.getDisplayName());
		}
	}

	/**
	 * This is our UI Complex input editor
	 * 
	 * @author chinshaw
	 * 
	 */
	@UiTemplate("ComplexInputEditorExec.ui.xml")
	public interface UiComplexInputBinder extends UiBinder<Widget, ComplexInputEditor> {
	}

	public interface ComplexInputEditorDriver extends SimpleBeanEditorDriver<UIComplexInputModelProxy, ComplexInputEditor> {
	}

	/**
	 * This editor is a little different in that it has to add inputs to it's
	 * list of inputs We are using it as a editor even though the inputs will
	 * not be persisted. This will append the inputs to the list of inputs in
	 * the object in order to send to the server.
	 * 
	 * @author chinshaw
	 */
	public class ComplexInputEditor extends BaseInputPanel<UIComplexInputModelProxy> implements ValueAwareEditor<UIComplexInputModelProxy>,
			HasRequestContext<UIComplexInputModelProxy> {

		@Editor.Ignore
		@UiField
		AnalyticsInputEditor inputs;

		@UiField
		LabelElement inputLabel;

		private UIComplexInputModelProxy model;

		private ComplexInputEditorDriver driver = GWT.create(ComplexInputEditorDriver.class);

		private RequestContext context;

		public ComplexInputEditor(int index) {
			super(index);
			initWidget(GWT.<UiComplexInputBinder> create(UiComplexInputBinder.class).createAndBindUi(this));
			driver.initialize(this);
		}

		@UiHandler("add")
		void onAddComplexInput(ClickEvent event) {
			UIComplexInputModelProxy input = context.create(UIComplexInputModelProxy.class);
			input.setInputs(new ArrayList<AnalyticsOperationInputProxy>());

			// Since request factory does not have clone we have to do this. :(
			for (AnalyticsOperationInputProxy subInput : model.getInputs()) {

				if (subInput instanceof UIUserInputModelProxy) {
					UIUserInputModelProxy newInput = context.create(UIUserInputModelProxy.class);
					newInput.setInputName(subInput.getInputName());
					newInput.setDisplayName(subInput.getDisplayName());
					newInput.setValue(((UIUserInputModelProxy) subInput).getValue());
					newInput.setDefinedInputs(((UIUserInputModelProxy) subInput).getDefinedInputs());
					inputs.addInput(newInput);
				} else if (subInput instanceof UIDateInputModelProxy) {
					UIDateInputModelProxy newInput = context.create(UIDateInputModelProxy.class);
					newInput.setInputName(subInput.getInputName());
					newInput.setDisplayName(subInput.getDisplayName());
					newInput.setValue(((UIDateInputModelProxy) subInput).getValue());
					inputs.addInput(newInput);
				}
			}

			inputs.getContainerElement().appendChild(Document.get().createBRElement());
		}

		@Override
		public Widget asWidget() {
			return this;
		}

		@Override
		public SimpleBeanEditorDriver<UIComplexInputModelProxy, ?> getDriver() {
			return driver;
		}

		@Override
		public void flush() {
			this.model.getInputs().clear();
			for (BaseInputEditor<? extends AnalyticsOperationInputProxy> editor : inputs.getEditors()) {
				this.model.getInputs().add(editor.getDriver().flush());
			}
		}

		@Override
		public void setValue(UIComplexInputModelProxy model) {
			this.model = model;
			inputLabel.setInnerText(model.getDisplayName());
		}

		@Override
		public void setRequestContext(RequestContext context) {
			this.context = context;
		}

		/**
		 * Not implemented
		 */
		@Override
		public void setDelegate(EditorDelegate<UIComplexInputModelProxy> delegate) {
		}

		/**
		 * Not implemented
		 */
		@Override
		public void onPropertyChange(String... paths) {
		}

	}

	/**
	 * Template for UIBinder that is used when rendering the UIDateInputEditor
	 * 
	 * @author chinshaw
	 */
	@UiTemplate("DateInputEditorExec.ui.xml")
	public interface UiDateInputBinder extends UiBinder<Widget, UIDateInputEditor> {
	}

	/**
	 * Driver interface for constructing the date editor driver.
	 * 
	 * @author chinshaw
	 */
	public interface UIDateInputEditorDriver extends SimpleBeanEditorDriver<UIDateInputModelProxy, UIDateInputEditor> {
	}

	/**
	 * Date input editor which allows a user to add a date to the arguments of a
	 * script.
	 * 
	 * @author chinshaw
	 */
	public class UIDateInputEditor extends BaseInputPanel<UIDateInputModelProxy> implements ValueAwareEditor<UIDateInputModelProxy> {

		/**
		 * The default vaue datebox that the user can used to set a date.
		 */
		@UiField(provided = true)
		DateBox value;

		/**
		 * Label that will be updated based on the input display name.
		 */
		@UiField
		LabelElement inputLabel;

		/**
		 * The driver for editing.
		 */
		private final UIDateInputEditorDriver driver = GWT.create(UIDateInputEditorDriver.class);

		/**
		 * The date input model proxy that will be updated when running.
		 */
		private UIDateInputModelProxy model;

		private DateTimeFormat dateFormatter = DateTimeFormat.getFormat("MM/d/yy");

		/**
		 * Default constructor takes an index for indexing the input in the
		 * list. The index is not really used but is there for future use.
		 * 
		 * @param index
		 */
		public UIDateInputEditor(int index) {
			super(index);
			value = new DateBox();
			DefaultFormat dateFormat = new DefaultFormat(dateFormatter);

			value.setFormat(dateFormat);
			initWidget(GWT.<UiDateInputBinder> create(UiDateInputBinder.class).createAndBindUi(this));
			driver.initialize(this);
		}

		/**
		 * Return the driver for this editing the UIDateInputModelProxy.
		 */
		@Override
		public SimpleBeanEditorDriver<UIDateInputModelProxy, ?> getDriver() {
			return driver;
		}

		/**
		 * This will set the value back onto the model when called.
		 */
		@Override
		public void flush() {
			// Date date = dateFormatter.format(defaultValue.getValue(),
			// TimeZone.createTimeZone("UTC"));
			this.model.setValue(value.getValue());
		}

		/**
		 * Setter for the date input model proxy that we will be editing.
		 */
		@Override
		public void setValue(UIDateInputModelProxy model) {
			this.model = model;
			this.inputLabel.setInnerText(model.getDisplayName());
		}

		/**
		 * Not implemented
		 */
		@Override
		public void setDelegate(EditorDelegate<UIDateInputModelProxy> delegate) {
		}

		/**
		 * Not implemented
		 */
		@Override
		public void onPropertyChange(String... paths) {
		}
	}

	/**
	 * Panel that holds the inputs
	 */
	private FlowPanel container = new FlowPanel();

	/**
	 * The request context that is main used by the complex inputs editor.
	 */
	private RequestContext context;

	private List<AnalyticsOperationInputProxy> values;

	private List<BaseInputEditor<? extends AnalyticsOperationInputProxy>> editors = new ArrayList<BaseInputEditor<? extends AnalyticsOperationInputProxy>>();

	SimpleBeanEditorDriver<List<AnalyticsOperationInputProxy>, AnalyticsInputEditor> driver = GWT.create(ScriptInputDriver.class);

	/**
	 * Default constructor that takes a reference to the resources file. This is
	 * is also a UIConstructor so you can access it from a ui.xml template using
	 * the {@code <d:AnalyticsInputEditor resources="\ resources\} />" }
	 */
	@UiConstructor
	public AnalyticsInputEditor(Resources resources) {
		initWidget(container);
		driver.initialize(this);
	}

	/**
	 * Not implemented.
	 */
	@Override
	public void onPropertyChange(String... paths) {
	}

	/**
	 * Not implemented
	 */
	@Override
	public void setDelegate(EditorDelegate<List<AnalyticsOperationInputProxy>> delegate) {
	}

	@Override
	public void setValue(List<AnalyticsOperationInputProxy> values) {
	    // If values is empty we atleast need to create a simple backing.
		if (values == null) {
		    values = new ArrayList<AnalyticsOperationInputProxy>();
		}
		
		// Setting the backing inputs that will be returned to the editor.
		this.values = values;
		
		// Clear the inputs container so that we can add our new values. This is
		// just in case we are reusing the editor.
		container.clear();
		// Clear all editors.
		editors.clear();

		if (values != null) {
			for (AnalyticsOperationInputProxy input : values) {
				addInput(input);
			}
		}
	}

	/**
	 * Used to add an input to the view. This also adds it to the list of
	 * editors.
	 * 
	 * @param input
	 */
	protected void addInput(AnalyticsOperationInputProxy input) {
		BaseInputEditor<? extends AnalyticsOperationInputProxy> editor = null;
		if (input instanceof UIUserInputModelProxy) {
			editor = createStringEditor(editors.size(), (UIUserInputModelProxy) input);
		} else if (input instanceof UIDateInputModelProxy) {
			editor = createDateEditor(editors.size(), (UIDateInputModelProxy) input);
		} else if (input instanceof UIComplexInputModelProxy) {
			UIComplexInputModelProxy complexInput = (UIComplexInputModelProxy) input;
			editor = createComplexEditor(editors.size(), complexInput);
		} else {
			throw new RuntimeException("Editor type was not implemented " + input);
		}

		editors.add(editor);
		AnimationBuilder builder = AnimationBuilder.create(editor.asWidget().getElement()).addFadeInAnimation().addSlideDownAnimation();
		container.add(editor);
		builder.run(500);
	}

	public SimpleBeanEditorDriver<List<AnalyticsOperationInputProxy>, AnalyticsInputEditor> getInputsEditorDriver() {
		return driver;
	}

	public BaseInputEditor<? extends AnalyticsOperationInputProxy> createComplexEditor(int index, UIComplexInputModelProxy input) {
		ComplexInputEditor editor = new ComplexInputEditor(index);
		editor.setRequestContext(context);
		editor.getDriver().edit(input);
		return editor;
	}

	public BaseInputEditor<? extends AnalyticsOperationInputProxy> createStringEditor(int index, UIUserInputModelProxy input) {
		StringInputEditor editor = new StringInputEditor(index);
		editor.getDriver().edit(input);
		return editor;
	}

	public BaseInputEditor<? extends AnalyticsOperationInputProxy> createDateEditor(int index, UIDateInputModelProxy input) {
		UIDateInputEditor editor = new UIDateInputEditor(index);
		editor.getDriver().edit(input);
		return editor;
	}

	@Override
	public void setRequestContext(RequestContext context) {
		this.context = context;
	}

	@Override
	public void flush() {
		values.clear();

		for (BaseInputEditor<? extends AnalyticsOperationInputProxy> editor : editors) {
			AnalyticsOperationInputProxy input = editor.getDriver().flush();
			values.add(input);
		}
	}
	
	private Element getContainerElement() {
		return container.getElement();
	}

	private List<BaseInputEditor<?>> getEditors() {
		return editors;
	}
	
	/**
	 * Removes multiple inputs at one time. This will also removing their cordinating editor along
	 * with the analytics operation inputs.
	 * @param inputs
	 */
	public void removeInputs(List<AnalyticsOperationInputProxy> inputs) {
	    values.removeAll(inputs);
	}
	
	/**
	 * Remove a single input from the editor. This will remove it's cordinating editor along with
	 * the value being removed.
	 * @param input
	 */
	public void removeInput(AnalyticsOperationInputProxy input) {
	    values.remove(input);
	}

	/**
	 * This method will recursively call add input 
	 * @param inputs
	 */
    public void addInputs(List<AnalyticsOperationInputProxy> inputs) {
        for (AnalyticsOperationInputProxy input : inputs) {
            addInput(input);
        }
    }
}