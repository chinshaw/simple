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
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.simple.original.client.proxy.AnalyticsOperationDataProviderProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.desktop.DataProviderOperationEditor.AnalyticsOperationDataProviderEditor;
import com.simple.original.client.view.widgets.AnimationBuilder;
import com.simple.original.client.view.widgets.AnimationBuilder.AnimationCompletionHandler;

public class DataProviderOperationEditor extends Composite implements IsEditor<ListEditor<AnalyticsOperationDataProviderProxy, AnalyticsOperationDataProviderEditor>> {

    /**
     * This is the uibinder and it will use the view.DefaultView.ui.xml
     * template.
     */
    @UiTemplate("AnalyticsOperationDataProviderEditor.ui.xml")
    public interface Binder extends UiBinder<Widget, AnalyticsOperationDataProviderEditor> {
    }

    /**
     * Editor that will edit a single data provider. Editor widgets must be
     * scoped to package level. This will flush values for varaible name and
     * description for a single data provider proxy.
     * 
     * @author chinshaw
     */
    public class AnalyticsOperationDataProviderEditor extends Composite implements Editor<AnalyticsOperationDataProviderProxy> {

        /**
         * R required variable name. Don't make me private.
         */
        @Path("variableName")
        @UiField
        TextBox variableName;

        /**
         * Description of the data provider. Don't make me private.
         */
        @Path("description")
        @UiField
        TextArea description;

        @UiField
        Button remove;

        private final int index;

        public AnalyticsOperationDataProviderEditor(final int index) {
            this.index = index;
            initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
        }

        @UiHandler("remove")
        void remove(ClickEvent event) {
            onRemove(index);
        }
    }

    /**
     * This is our list editor which is a collection of editors.
     */
    private static ListEditor<AnalyticsOperationDataProviderProxy, AnalyticsOperationDataProviderEditor> editor;
    /**
     * This is our container that holds our output editor widgets.
     */
    private final FlowPanel container = new FlowPanel();

    protected final Resources resources;

    @UiConstructor
    public DataProviderOperationEditor(final Resources resources) {
        this.resources = resources;
        initWidget(container);
        editor = ListEditor.of(new EditorSource<AnalyticsOperationDataProviderEditor>() {

            @Override
            public List<AnalyticsOperationDataProviderEditor> create(int count, int index) {
                container.clear();
                List<AnalyticsOperationDataProviderEditor> toReturn = new ArrayList<AnalyticsOperationDataProviderEditor>(count);
                for (int i = 0; i < count; i++) {
                    toReturn.add(create(index + i));
                }
                return toReturn;
            }

            /**
             * Call this to remove an editor from the view.
             */
            @Override
            public void dispose(final AnalyticsOperationDataProviderEditor editor) {
                AnimationCompletionHandler completionHandler = new AnimationCompletionHandler() {

                    @Override
                    public void onAnimationsComplete() {
                        editor.removeFromParent();
                    }
                };

                AnimationBuilder.create(editor.getElement()).addFadeOutAnimation().addSlideUpAnimation().addCompletionHandler(completionHandler).run(500);
            }

            @Override
            public AnalyticsOperationDataProviderEditor create(final int index) {
                AnalyticsOperationDataProviderEditor editor = new AnalyticsOperationDataProviderEditor(index);
                AnimationBuilder builder = AnimationBuilder.create(editor.getElement()).addFadeInAnimation().addSlideDownAnimation();
                container.insert(editor, index);
                builder.run(500);
                return editor;
            }
        });
    }

    protected void onRemove(int index) {
        editor.getList().remove(index);
    }

    @Override
    public ListEditor<AnalyticsOperationDataProviderProxy, AnalyticsOperationDataProviderEditor> asEditor() {
        return editor;
    }
}