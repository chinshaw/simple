package com.simple.original.client.view.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class TextLinkCell<C> extends AbstractCell<C> {

	private final SafeHtml html;
	private final Delegate<C> delegate;

	/**
	 * Construct a new {@link ActionCell}.
	 * 
	 * @param message
	 *            the message to display on the button
	 * @param delegate
	 *            the delegate that will handle events
	 */
	public TextLinkCell(SafeHtml message, Delegate<C> delegate) {
		super("click", "keydown");
		this.delegate = delegate;
		this.html = new SafeHtmlBuilder()
				.appendHtmlConstant("<button type=\"button\" tabindex=\"-1\">")
				.append(message).appendHtmlConstant("</button>").toSafeHtml();
	}

	/**
	 * Construct a new {@link ActionCell} with a text String that does not
	 * contain HTML markup.
	 * 
	 * @param text
	 *            the text to display on the button
	 * @param delegate
	 *            the delegate that will handle events
	 */
	public TextLinkCell(String text, Delegate<C> delegate) {
		this(SafeHtmlUtils.fromString(text), delegate);
	}

	  @Override
	  public void render(Context context, C value, SafeHtmlBuilder sb) {
	    sb.append(html);
	  }

	@Override
	public void onBrowserEvent(Context context, Element parent, C value,
			NativeEvent event, ValueUpdater<C> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		if ("click".equals(event.getType())) {
			EventTarget eventTarget = event.getEventTarget();
			if (!Element.is(eventTarget)) {
				return;
			}
			if (parent.getFirstChildElement().isOrHasChild(
					Element.as(eventTarget))) {
				// Ignore clicks that occur outside of the main element.
				onEnterKeyDown(context, parent, value, event, valueUpdater);
			}
		}
	}

	@Override
	protected void onEnterKeyDown(Context context, Element parent, C value,
			NativeEvent event, ValueUpdater<C> valueUpdater) {
		delegate.execute(value);
	}
}
