package com.simple.original.client.dashboard.designer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.api.analytics.Criticality;
import com.simple.original.client.dashboard.designer.GaugeModelRangesEditor.GaugeModelRangeEditor;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel.IGaugeModelRange;
import com.simple.original.client.proxy.NumberRangeProxy;
import com.simple.original.client.view.widgets.ColorPickerEditor;

public class GaugeModelRangesEditor extends Composite implements IsEditor<ListEditor<IGaugeModelRange, GaugeModelRangeEditor>> {

    public interface RangesBinder extends UiBinder<Widget, GaugeModelRangesEditor> {
    }

    @UiTemplate("GaugeModelRangeEditor.ui.xml")
    public interface RangeBinder extends UiBinder<Widget, GaugeModelRangeEditor> {
    }

    public class GaugeModelRangeEditor extends Composite implements ValueAwareEditor<IGaugeModelRange> {

        @Path("color")
        @UiField
        ColorPickerEditor colorPicker;

        @UiField
        DoubleBox minimum;

        @UiField
        DoubleBox maximum;

        @UiField
        Image remove;

        private IGaugeModelRange value;

        public GaugeModelRangeEditor(int index) {
            initWidget(GWT.<RangeBinder> create(RangeBinder.class).createAndBindUi(this));
            initEventHandlers();
        }

        private void initEventHandlers() {

            colorPicker.addSelectionHandler(new SelectionHandler<String>() {

                @Override
                public void onSelection(SelectionEvent<String> event) {
                    value.setColor(colorPicker.asEditor().getValue());
                    modelUpdated();
                }
            });

            minimum.addChangeHandler(new ChangeHandler() {

                @Override
                public void onChange(ChangeEvent event) {
                    value.setMinimum(minimum.getValue());
                    modelUpdated();
                }
            });

            maximum.addChangeHandler(new ChangeHandler() {

                @Override
                public void onChange(ChangeEvent event) {
                    value.setMaximum(maximum.getValue());
                    modelUpdated();
                }
            });
        }

        @UiHandler("remove")
        void onRemove(ClickEvent click) {
            removeRange(value);
        }

        @Override
        public void setDelegate(EditorDelegate<IGaugeModelRange> delegate) {
        }

        @Override
        public void flush() {
            value.setMaximum(maximum.getValue());
            value.setMinimum(minimum.getValue());
            value.setColor(colorPicker.asEditor().getValue());
        }

        @Override
        public void onPropertyChange(String... paths) {
        }

        @Override
        public void setValue(IGaugeModelRange value) {
            this.value = value;
            minimum.setValue(value.getMinimum());
            maximum.setValue(value.getMaximum());
            colorPicker.asEditor().setValue(value.getColor());
            // modelUpdated();
        }
    }

    private class GaugeWidgetRangeEditorSource extends EditorSource<GaugeModelRangeEditor> {

        @Override
        public List<GaugeModelRangeEditor> create(int count, int index) {
            ranges.clear();
            List<GaugeModelRangeEditor> toReturn = new ArrayList<GaugeModelRangeEditor>(count);
            for (int i = 0; i < count; i++) {
                toReturn.add(create(index + i));
            }
            return toReturn;
        }

        @Override
        public GaugeModelRangeEditor create(int index) {
            GaugeModelRangeEditor editor = new GaugeModelRangeEditor(index);
            ranges.insert(editor, index);
            return editor;
        }

        public void dispose(GaugeModelRangeEditor subEditor) {
            subEditor.removeFromParent();
            subEditor = null;
        }

        /**
         * Re-order a sub-Editor. The default implementation is a no-op.
         * 
         * @param editor
         *            an {@link Editor} of type E
         * @param index
         *            the index of the Editor
         */
        public void setIndex(GaugeModelRangeEditor editor, int index) {
            ranges.insert(editor, index);
        }
    }

    
    /**
     * Prive logger.
     */
    private static final Logger logger = Logger.getLogger(GaugeModelRangesEditor.class.getName());
    
    /**
     * This is our list editor which is a collection of editors.
     */
    private static ListEditor<IGaugeModelRange, GaugeModelRangeEditor> editor;

    @UiField
    FlowPanel ranges;

    private final EventBus eventBus;

    public GaugeModelRangesEditor(EventBus eventBus) {
        this.eventBus = eventBus;
        initWidget(GWT.<RangesBinder> create(RangesBinder.class).createAndBindUi(this));
        editor = ListEditor.of(new GaugeWidgetRangeEditorSource());
        // editor.createEditorForTraversal();
    }

    @UiHandler("addRange")
    void onAddRange(ClickEvent click) {
    	/*
    	IGaugeModelRange range = context.create(IGaugeModelRange.class);
        range.setMinimum(0d);
        range.setMaximum(0d);
        range.setColor("red");

        editor.getList().add(range);
        modelUpdated();
        */
    }

    private void addRange(NumberRangeProxy rangeProxy) {
    	/*
    	IGaugeModelRange range = context.create(IGaugeModelRange.class);
        range.setMinimum(rangeProxy.getMinimum());
        range.setMaximum(rangeProxy.getMaximum());
        range.setColor(getCritilityColor(rangeProxy.getCriticality()));

        editor.getList().add(range);
        modelUpdated();
        */
    }

    private void modelUpdated() {
        editor.flush();
    }

    @Override
    public ListEditor<IGaugeModelRange, GaugeModelRangeEditor> asEditor() {
        return editor;
    }

    private void removeRange(IGaugeModelRange range) {
        editor.getList().remove(range);
        modelUpdated();
    }


    public void setRanges(List<NumberRangeProxy> ranges) {
        if (ranges == null) {
            logger.fine("Metric had not ranges");
            return;
        }
        
        if (editor.getList() == null) {
            editor.setValue(new ArrayList<IGaugeModelRange>());
        }
        editor.getList().clear(); 
        
        for (NumberRangeProxy range : ranges) {
            addRange(range);
        }
    }

    public static String getCritilityColor(Criticality criticality) {
        if (criticality == null) {
            return "grey";
        }

        switch (criticality) {
        case NORMAL:
            return "green";
        case WARNING:
            return "yellow";
        case CRITICAL:
            return "red";
        default:
            return "grey";
        }
    }
}