package com.simple.original.client.view.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.simple.original.client.proxy.MetricDoubleProxy;
import com.simple.original.client.proxy.MetricIntegerProxy;
import com.simple.original.client.proxy.MetricProxy;
import com.simple.original.client.proxy.MetricStringProxy;

public class MetricTableCell extends AbstractCell<MetricProxy> {

    /**
     * This is the click delegate that will be attached to the onclick event.
     */
    private Delegate<Long> delegate;

    /**
     * Default constructor, enables click handling.
     */
    public MetricTableCell() {
        super("click", "keydown");
    }

    /**
     * Default consturctor that will add the click handler delegate.
     * 
     * @param delegate
     *            click handler delegate used to add callback that will pass the
     *            Metrics Id that was clicked.
     */
    public MetricTableCell(Delegate<Long> delegate) {
        this();
        this.delegate = delegate;
    }

    /**
     * Render the MetricCell, this will look at the type of {@link MetricProxy}
     * that this object is and it will render the cell appropriately based on
     * the values in the cell. For example a {@link MetricDoubleProxy} cell has
     * a low, mid, and high range. The render function takes this into account
     * and will color code the cell according to where the metric value falls
     * into the range. A {@link MetricStringProxy} will be simply treated as a
     * string.
     */
    @Override
    public void render(Context context, MetricProxy value, SafeHtmlBuilder sb) {
        // Values should typically always be higher than this value.
        if (value instanceof MetricDoubleProxy) {
            MetricDoubleProxy metric = (MetricDoubleProxy) value;

            // Test to see if it is in high range.
            /*
             * NumberRangeProxy range = metric.getHighRange(); if (range !=
             * null) { Double min = range.getMinimum(); logger.info("Min is " +
             * min + " max is " + metric.getDoubleValue() + " value is " +
             * metric.getDoubleValue()); if (min != null &&
             * metric.getDoubleValue() > min) { sb.appendHtmlConstant(
             * "<div style=\"background: rgba(255, 0, 0, 0.5); height: 100%; width: 8em; \">"
             * + metric.getDoubleValue() + "</div>"); return; } }
             * 
             * // Test to see if the value is in the middle range. range =
             * metric.getMidRange(); if (range != null) { Double min =
             * range.getMinimum(); Double max = range.getMaximum();
             * 
             * if (min != null && max != null && metric.getDoubleValue() > min
             * && metric.getDoubleValue() < max) { sb.appendHtmlConstant(
             * "<div style=\" background: rgba(255, 255, 0, 0.5); height: 100%; width: 8em; \">"
             * + metric.getDoubleValue() + "</div>"); return; } }
             */

            // Else this porridge is just right.
            sb.appendHtmlConstant("<div style=\"color:333; width: 8em;\">" + metric.getValue() + "</div>");

        } else if (value instanceof MetricIntegerProxy) {
            MetricIntegerProxy metric = (MetricIntegerProxy) value;
            sb.appendHtmlConstant("<div style=\"color:333; width: 8em;\">" + metric.getValue() + "</div>");
        } else if (value instanceof MetricStringProxy) {
            MetricStringProxy metric = (MetricStringProxy) value;
            sb.appendHtmlConstant("<div style=\"width:8em;\">" + metric.getValue() + "</div>");
        }
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, MetricProxy value, NativeEvent event, ValueUpdater<MetricProxy> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        if ("click".equals(event.getType())) {
            EventTarget eventTarget = event.getEventTarget();
            if (!Element.is(eventTarget)) {
                return;
            }
            if (parent.getFirstChildElement().isOrHasChild(Element.as(eventTarget))) {
                // Ignore clicks that occur outside of the main element.
                onEnterKeyDown(context, parent, value, event, valueUpdater);
            }
        }
    }

    /**
     * Setter for the delagate
     * 
     * @param delegate
     */
    public void setDelegate(Delegate<Long> delegate) {
        this.delegate = delegate;
    }

    /**
     * Called when the cell is clicked and will make a callback to the delegate
     * with the id of the metric.
     */
    @Override
    protected void onEnterKeyDown(Context context, Element parent, MetricProxy value, NativeEvent event, ValueUpdater<MetricProxy> valueUpdater) {
        delegate.execute((Long) context.getKey());
    }
}