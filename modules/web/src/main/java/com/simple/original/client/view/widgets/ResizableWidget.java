package com.simple.original.client.view.widgets;

import com.google.gwt.user.client.Element;

public interface ResizableWidget {
	/**
	 * Get the widget's element.
	 */
	public abstract Element getElement();

	/**
	 * Check if this widget is attached to the page.
	 * 
	 * @return true if the widget is attached to the page
	 */
	public abstract boolean isAttached();

	/**
	 * This method is called when the dimensions of the parent element change.
	 * Subclasses should override this method as needed.
	 * 
	 * @param width
	 *            the new client width of the element
	 * @param height
	 *            the new client height of the element
	 */
	public abstract void onResize(int width, int height);
}