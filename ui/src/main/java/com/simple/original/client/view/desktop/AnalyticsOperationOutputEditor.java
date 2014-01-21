package com.simple.original.client.view.desktop;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.simple.original.api.analytics.IAnalyticsOperationOutput;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.desktop.AnalyticsOperationOutputEditor.OutputEditor;
import com.simple.original.client.view.widgets.AnimationBuilder;
import com.simple.original.client.view.widgets.AnimationBuilder.AnimationCompletionHandler;
import com.simple.original.client.view.widgets.EnumEditor;

public class AnalyticsOperationOutputEditor extends Composite implements
		IsEditor<ListEditor<AnalyticsOperationOutputProxy, OutputEditor>> {

	@UiTemplate("AnalyticsOperationOutputEditor.ui.xml")
	public interface OutputEditorBinder extends UiBinder<Widget, OutputEditor> {
	}

	class OutputEditor extends Composite implements
			Editor<AnalyticsOperationOutputProxy> {

		@UiField(provided = true)
		EnumEditor<IAnalyticsOperationOutput.Type> outputType = new EnumEditor<IAnalyticsOperationOutput.Type>(
				IAnalyticsOperationOutput.Type.class);

		@UiField
		TextBox name;

		@UiField
		Button removeInput;

		private int index;

		/**
		 * Default constructor takes an index of the Editor objects location.
		 * The index is used to delete this object.
		 * 
		 * @param index
		 */
		public OutputEditor(int index) {
			initWidget(GWT
					.<OutputEditorBinder> create(OutputEditorBinder.class)
					.createAndBindUi(this));
			this.index = index;
		}

		@UiHandler("removeInput")
		void removeOutput(ClickEvent clickEvent) {
			editor.getList().remove(index);
			List<OutputEditor> editors = editor.getEditors();
			// reorder
			for (int i = index, j = editors.size(); i < j; i++) {
				(editors.get(i)).setIndex(i);
			}
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

	/**
	 * This is our editor used to edit our ScriptInputProxy classes.
	 */
	private final ListEditor<AnalyticsOperationOutputProxy, OutputEditor> editor;
	/**
	 * This is our container that holds our output editor widgets.
	 */
	private final FlowPanel container = new FlowPanel();

	public AnalyticsOperationOutputEditor(final Resources resources) {
		initWidget(container);

		editor = ListEditor.of(new EditorSource<OutputEditor>() {

			public List<OutputEditor> create(int count, int index) {
				container.clear();
				List<OutputEditor> toReturn = new ArrayList<OutputEditor>(count);
				for (int i = 0; i < count; i++) {
					toReturn.add(create(index + i));
				}
				return toReturn;
			}

			@Override
			public void dispose(final OutputEditor editor) {
				AnimationCompletionHandler completionHandler = new AnimationCompletionHandler() {
					@Override
					public void onAnimationsComplete() {
						editor.removeFromParent();
					}
				};
				AnimationBuilder.create(editor.getElement())
						.addFadeOutAnimation().addSlideUpAnimation()
						.addCompletionHandler(completionHandler).run(500);
			}

			@Override
			public OutputEditor create(final int index) {
				final OutputEditor outputEditor = new OutputEditor(index);
				AnimationBuilder builder = AnimationBuilder
						.create(outputEditor.getElement()).addFadeInAnimation()
						.addSlideDownAnimation();
				container.insert(outputEditor, index);
				builder.run(500);
				return outputEditor;
			}
		});
	}

	@Override
	public ListEditor<AnalyticsOperationOutputProxy, OutputEditor> asEditor() {
		return editor;
	}
}
