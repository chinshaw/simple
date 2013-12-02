package com.simple.original.client.view.desktop;

import java.util.logging.Logger;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.simple.original.client.proxy.DataProviderProxy;
import com.simple.original.client.proxy.RDataProviderProxy;
import com.simple.original.client.proxy.SqlDataProviderProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.desktop.DataProviderListEditor.DataProviderEditorWidget;

public class DataProviderListEditor extends Composite implements IsEditor<ListEditor<DataProviderProxy, DataProviderEditorWidget>> {

    private static final Logger logger = Logger.getLogger(DataProviderListEditor.class.getName());

    private class DataProviderSource extends EditorSource<DataProviderEditorWidget> {

        @Override
        public DataProviderEditorWidget create(int index) {
            DataProviderEditorWidget editor = new DataProviderEditorWidget();
            container.insert(editor, index);

            return editor;
        }

        /**
         * Call this to remove an editor from the view.
         */
        @Override
        public void dispose(DataProviderEditorWidget editor) {
            editor.removeFromParent();
        }

        public void setIndex(DataProviderEditorWidget editor, int index) {
            container.insert(editor, index);
        }

    }

    public class DataProviderEditorWidget extends Composite implements ValueAwareEditor<DataProviderProxy> {

        private DataProviderProxy value = null;
        private FlowPanel container = new FlowPanel();

        @Path("variableName")
        private TextBox variableName = new TextBox();

        private final Button removeButton = new Button("remove");
        
        @Editor.Ignore
        private TextArea sqlText = null;

        @Editor.Ignore
        private TextArea rCommandText = null;
        
        
        public DataProviderEditorWidget() {
            initWidget(container);
            removeButton.setStyleName(resources.style().textButton());
            
            container.add(removeButton);
            container.add(new Label("Variable Name"));
            container.add(variableName);
            
            // Add a click handler to remove this object from the list when the delete button is clicked.
            removeButton.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                   DataProviderListEditor.this.asEditor().getList().remove(value); 
                }
            });
        }

        @Override
        public void setDelegate(EditorDelegate<DataProviderProxy> delegate) {
        }

        @Override
        public void flush() {
            value.setVariableName(variableName.getValue());
            if (value instanceof SqlDataProviderProxy) {
                flushSqlDataProvider();
            }
            if (value instanceof RDataProviderProxy) {
                flushRDataProvider();
            }
        }

        @Override
        public void onPropertyChange(String... paths) {

        }

        @Override
        public void setValue(DataProviderProxy value) {
            this.value = value;

            variableName.setValue(this.value.getVariableName());
            if (value instanceof SqlDataProviderProxy) {
                editSqlDataProvider();
            }

            if (value instanceof RDataProviderProxy) {
                logger.info("calling flush on R data provider proxy");
                editRDataProvider();
            }
        }

        void flushSqlDataProvider() {

            if (sqlText != null) {
                ((SqlDataProviderProxy) value).setSqlStatement(sqlText.getValue());
            }
        }

        void flushRDataProvider() {
            if (rCommandText != null) {
                ((RDataProviderProxy) value).setRCommand(rCommandText.getValue());
            }
        }

        void editSqlDataProvider() {
            SqlDataProviderProxy sqlDataProvider = (SqlDataProviderProxy) value;
            if (sqlText == null) {
                container.add(new Label("Sql Text"));
                sqlText = new TextArea();
                sqlText.setHeight("10em");
                sqlText.setWidth("80em");
                container.add(sqlText);
            }
            sqlText.setValue(sqlDataProvider.getSqlStatement());
        }

        void editRDataProvider() {
            RDataProviderProxy rProvider = (RDataProviderProxy) value;
            if (rCommandText == null) {
                rCommandText = new TextArea();
                rCommandText.setHeight("10em");
                rCommandText.setWidth("80em");
                container.add(new Label("R Command Text"));
                container.add(rCommandText);
            }
            rCommandText.setValue(rProvider.getRCommand());
        }
    }

    /**
     * This is the parent container panel that contains all other editors
     */
    private final FlowPanel container = new FlowPanel();
    private final ListEditor<DataProviderProxy, DataProviderEditorWidget> editor = ListEditor.of(new DataProviderSource());
    private final Resources resources;
    
    /**
     * Default constructor takes an index of the Editor objects location. The
     * index is used to delete this object.
     * 
     * @param index
     */
    public DataProviderListEditor(Resources resources) {
        initWidget(container);
        this.resources = resources;

    }

    @Override
    public ListEditor<DataProviderProxy, DataProviderEditorWidget> asEditor() {
        return editor;
    }
}
