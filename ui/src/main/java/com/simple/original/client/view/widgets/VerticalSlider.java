package com.simple.original.client.view.widgets;

/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RequiresResize;

/**
 * A widget that allows the user to select a value within a range of possible
 * values using a sliding bar that responds to mouse events.
 * 
 * <h3>Keyboard Events</h3>
 * <p>
 * SliderBarVertical listens for the following key events. Holding down a key
 * will repeat the action until the key is released.
 * <ul class='css'>
 * <li>up arrow - shift up one step</li>
 * <li>down arrow - shift down one step</li>
 * <li>ctrl+up arrow - jump up 10% of the distance</li>
 * <li>ctrl+down arrow - jump down 10% of the distance</li>
 * <li>page up - shift up one page</li>
 * <li>page down - shift down one page</li>
 * <li>ctrl+page up - shift up 10% of the distance</li>
 * <li>ctrl+page down - shift down 10% of the distance</li>
 * <li>ctrl+mouse down - page 10% of the distance</li>
 * <li>home - jump to min value</li>
 * <li>end - jump to max value</li>
 * <li>space - jump to middle value</li>
 * </ul>
 * </p>
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.gwt-SliderBarVertical-shell { primary style }</li>
 * <li>.gwt-SliderBarVertical-shell-focused { primary style when focused }</li>
 * <li>.gwt-SliderBarVertical-shell gwt-SliderBarVertical-line { the line that
 * the knob moves along }</li>
 * <li>.gwt-SliderBarVertical-shell gwt-SliderBarVertical-line-sliding { the
 * line that the knob moves along when sliding }</li>
 * <li>.gwt-SliderBarVertical-shell .gwt-SliderBarVertical-knob { the sliding
 * knob }</li>
 * <li>.gwt-SliderBarVertical-shell .gwt-SliderBarVertical-knob-sliding { the
 * sliding knob when sliding }</li>
 * </ul>
 */
public class VerticalSlider extends FocusPanel implements RequiresResize, HasValue<Double>, HasValueChangeHandlers<Double> {

	/**
	 * A {@link ClientBundle} that provides images for {@link SliderBarVertical}
	 * .
	 */
	public interface SliderBarVerticalImages extends ClientBundle {
		/**
		 * The image bundle.
		 */
		SliderBarVerticalImages INSTANCE = GWT.create(SliderBarVerticalImages.class);

		/**
		 * An image used for the sliding knob.
		 * 
		 * @return a prototype of this image
		 */
		@Source("../../resources/images/sliderVertical.gif")
		ImageResource slider();

		/**
		 * An image used for the sliding knob.
		 * 
		 * @return a prototype of this image
		 */
		@Source("../../resources/images/sliderDisabledVertical.gif")
		ImageResource sliderDisabled();

		/**
		 * An image used for the sliding knob while sliding.
		 * 
		 * @return a prototype of this image
		 */
		@Source("../../resources/images/sliderSlidingVertical.gif")
		ImageResource sliderSliding();

		/**
		 * The Css.
		 * 
		 * @return the resource
		 */
		@NotStrict
		@Source("SliderBarVertical.css")
		CssResource sliderBarCss();
	}

	/**
	 * The timer used to continue to shift the knob as the user holds down one
	 * of the left/right arrow keys. Only IE auto-repeats, so we just keep
	 * catching the events.
	 */
	private class KeyTimer extends Timer {
		/**
		 * A bit indicating that this is the first run.
		 */
		private boolean firstRun = true;

		/**
		 * The delay between shifts, which shortens as the user holds down the
		 * button.
		 */
		private final int repeatDelay = 30;

		/**
		 * A bit indicating whether we are shifting to a higher or lower value.
		 */
		private boolean shiftUp = false;

		/**
		 * The number of steps to shift with each press.
		 */
		private int multiplier = 1;

		/**
		 * This method will be called when a timer fires. Override it to
		 * implement the timer's logic.
		 */
		@Override
		public void run() {

			double newPos = 0;
			boolean stop = false;
			// Slide the slider bar
			if (shiftUp) {
				newPos = curValue - multiplier * stepSize;
			} else {
				newPos = curValue + multiplier * stepSize;
			}

			// Check if we are paging while holding mouse down
			// and make sure will not overshoot original mouse down position.
			if (pagingMouse && shiftUp && mouseDownPos > newPos) {
				stop = true;
				setCurrentValue(mouseDownPos);
			} else if (pagingMouse && !shiftUp && mouseDownPos < newPos) {
				stop = true;
				setCurrentValue(mouseDownPos);
			}
			if (!stop) {
				if (firstRun) {
					firstRun = false;
					startSliding(true, false);
				}

				// Slide the slider bar
				setCurrentValue(newPos);

				// Repeat this timer until cancelled by keyup event
				schedule(repeatDelay);
			}
		}

		/**
		 * Schedules a timer to elapse in the future.
		 * 
		 * @param delayMillis
		 *            how long to wait before the timer elapses, in milliseconds
		 * @param shiftUp2
		 *            whether to shift up or not
		 * @param multiplier2
		 *            the number of steps to shift
		 */
		public void schedule(final int delayMillis, final boolean shiftUp2, final int multiplier2) {
			firstRun = true;
			this.shiftUp = shiftUp2;
			this.multiplier = multiplier2;
			super.schedule(delayMillis);
		}
	}

	/**
	 * The space bar key code.
	 */
	private static final int SPACEBAR = 32;

	/**
	 * Used in the multiplier.
	 */
	private static final double PERCENTAGE = 10;

	private static final int INITIALDELAY = 400;

	private static final int DEFAULTPAGESIZE = 25;

	private static final int PAGINGMULTIPLIER = 1000;

	/**
	 * The current value.
	 */
	private double curValue;

	/**
	 * The knob that slides across the line.
	 */
	private final Image knobImage = new Image();

	/**
	 * The timer used to continue to shift the knob if the user holds down a
	 * key.
	 */
	private final KeyTimer keyTimer = new KeyTimer();

	/**
	 * The line that the knob moves over.
	 */
	private final Element lineElement;

	/**
	 * The offset between the edge of the shell and the line.
	 */
	private int lineTopOffset = 0;

	/**
	 * The maximum slider value.
	 */
	private double maxValue;

	/**
	 * The minimum slider value.
	 */
	private double minValue;

	/**
	 * The number of labels to show.
	 */
	private final int numLabels = 0;

	/**
	 * The number of tick marks to show.
	 */
	private final int numTicks = 0;

	/**
	 * A bit indicating whether or not we are currently sliding the slider bar
	 * due to keyboard events.
	 */
	private boolean slidingKeyboard = false;

	/**
	 * A bit indicating whether or not we are currently sliding the slider bar
	 * due to mouse events.
	 */
	private boolean slidingMouse = false;

	/**
	 * A bit indicating whether or not the slider is enabled.
	 */
	private boolean enabled = true;

	/**
	 * The images used with the sliding bar.
	 */
	private final SliderBarVerticalImages images;

	/**
	 * The size of the increments between knob positions.
	 */
	private double stepSize;

	/**
	 * A flag for the when the mouse button is held down when away from the
	 * slider.
	 */
	private boolean pagingMouse;

	/**
	 * The position of the first mouse down. Used for paging.
	 */
	private double mouseDownPos;

	/**
	 * The page size value.
	 */
	private int pageSize;

	/**
	 * An empty constructor to support UI binder.
	 */
	public VerticalSlider() {
		this(1, 2, DEFAULTPAGESIZE);
	}

	/**
	 * Create a slider bar.
	 * 
	 * @param minValue2
	 *            the minimum value in the range
	 * @param maxValue2
	 *            the maximum value in the range
	 * @param pageSize2
	 *            the page size
	 */
	public VerticalSlider(final double minValue2, final double maxValue2, final int pageSize2) {
		this(minValue2, maxValue2, SliderBarVerticalImages.INSTANCE, pageSize2);
	}

	/**
	 * Create a slider bar.
	 * 
	 * @param minValue2
	 *            the minimum value in the range
	 * @param maxValue2
	 *            the maximum value in the range
	 * @param images2
	 *            the images to use for the slider
	 * @param pageSize2
	 *            the page size
	 */
	public VerticalSlider(final double minValue2, final double maxValue2, final SliderBarVerticalImages images2, final int pageSize2) {
		super();
		images2.sliderBarCss().ensureInjected();
		this.minValue = minValue2;
		this.maxValue = maxValue2;
		this.images = images2;
		this.pageSize = pageSize2;

		// Create the outer shell
		DOM.setStyleAttribute(getElement(), "position", "relative");
		setStyleName("gwt-SliderBarVertical-shell");

		// Create the line
		lineElement = DOM.createDiv();
		DOM.appendChild(getElement(), lineElement);
		DOM.setStyleAttribute(lineElement, "position", "absolute");
		DOM.setElementProperty(lineElement, "className", "gwt-SliderBarVertical-line");

		// Create the knob
		knobImage.setResource(images.slider());
		final Element knobElement = knobImage.getElement();
		DOM.appendChild(getElement(), knobElement);
		DOM.setStyleAttribute(knobElement, "position", "absolute");
		DOM.setElementProperty(knobElement, "className", "gwt-SliderBarVertical-knob");

		sinkEvents(Event.MOUSEEVENTS | Event.KEYEVENTS | Event.FOCUSEVENTS);

		// workaround to render properly when parent Widget does not
		// implement ProvidesResize since DOM doesn't provide element
		// height and width until onModuleLoad() finishes.
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				onResize();
			}
		});
	}

	/**
	 * The value change handler.
	 * 
	 * @param handler
	 *            the handler
	 * @return the handle
	 */
	@Override
	public final HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Double> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * Return the current value.
	 * 
	 * @return the current value
	 */
	public final double getCurrentValue() {
		return curValue;
	}

	/**
	 * Return the max value.
	 * 
	 * @return the max value
	 */
	public final double getMaxValue() {
		return maxValue;
	}

	/**
	 * Return the minimum value.
	 * 
	 * @return the minimum value
	 */
	public final double getMinValue() {
		return minValue;
	}

	/**
	 * Return the number of labels.
	 * 
	 * @return the number of labels
	 */
	public final int getNumLabels() {
		return numLabels;
	}

	/**
	 * Return the number of ticks.
	 * 
	 * @return the number of ticks
	 */
	public final int getNumTicks() {
		return numTicks;
	}

	/**
	 * Return the step size.
	 * 
	 * @return the step size
	 */
	public final double getStepSize() {
		return stepSize;
	}

	/**
	 * Return the total range between the minimum and maximum values.
	 * 
	 * @return the total range
	 */
	public final double getTotalRange() {
		if (minValue > maxValue) {
			return 0;
		} else {
			return maxValue - minValue;
		}
	}

	@Override
	public final Double getValue() {
		return curValue;
	}

	/**
	 * @return Gets whether this widget is enabled
	 */
	public final boolean isEnabled() {
		return enabled;
	}

	/**
	 * Listen for events that will move the knob.
	 * 
	 * @param event
	 *            the event that occurred
	 */
	@Override
	public final void onBrowserEvent(final Event event) {
		super.onBrowserEvent(event);
		if (enabled) {
			switch (DOM.eventGetType(event)) {
			// Unhighlight and cancel keyboard events
			case Event.ONBLUR:
				keyTimer.cancel();
				if (slidingMouse) {
					DOM.releaseCapture(getElement());
					slidingMouse = false;
					slideKnob(event);
					stopSliding(true, true);
				} else if (slidingKeyboard) {
					slidingKeyboard = false;
					stopSliding(true, true);
				}
				unhighlight();
				break;

			// Highlight on focus
			case Event.ONFOCUS:
				highlight();
				break;

			// Mousewheel events
			case Event.ONMOUSEWHEEL:
				final int velocityY = event.getMouseWheelVelocityY();
				event.preventDefault();
				if (velocityY > 0) {
					shiftDown(1);
				} else {
					shiftUp(1);
				}
				break;

			// Shift left or right on key press
			case Event.ONKEYDOWN:
				if (!slidingKeyboard) {
					int multiplier = 1;
					if (event.getCtrlKey()) {
						multiplier = (int) (getTotalRange() / stepSize / PERCENTAGE);
					}

					switch (event.getKeyCode()) {
					case KeyCodes.KEY_HOME:
						event.preventDefault();;
						setCurrentValue(minValue);
						break;
					case KeyCodes.KEY_END:
						event.preventDefault();
						setCurrentValue(maxValue);
						break;
					case KeyCodes.KEY_PAGEUP:
						event.preventDefault();
						slidingKeyboard = true;
						startSliding(false, true);
						shiftUp(pageSize);
						keyTimer.schedule(INITIALDELAY, true, pageSize);
						break;
					case KeyCodes.KEY_PAGEDOWN:
						event.preventDefault();
						slidingKeyboard = true;
						startSliding(false, true);
						shiftDown(pageSize);
						keyTimer.schedule(INITIALDELAY, false, pageSize);
						break;
					case KeyCodes.KEY_UP:
						event.preventDefault();
						slidingKeyboard = true;
						startSliding(false, true);
						shiftUp(multiplier);
						keyTimer.schedule(INITIALDELAY, true, multiplier);
						break;
					case KeyCodes.KEY_DOWN:
						event.preventDefault();
						slidingKeyboard = true;
						startSliding(false, true);
						shiftDown(multiplier);
						keyTimer.schedule(INITIALDELAY, false, multiplier);
						break;
					case SPACEBAR:
						event.preventDefault();
						setCurrentValue(minValue + getTotalRange() / 2);
						break;
					default:
					}
				}
				break;
			// Stop shifting on key up
			case Event.ONKEYUP:
				keyTimer.cancel();
				if (slidingKeyboard) {
					slidingKeyboard = false;
					stopSliding(true, true);
				}
				break;

			// Mouse Events
			case Event.ONMOUSEDOWN:
				setFocus(true);
				if (sliderClicked(event)) {
					slidingMouse = true;
					DOM.setCapture(getElement());
					startSliding(true, true);
					DOM.eventPreventDefault(event);
					slideKnob(event);
				} else {
					pagingMouse = true;
					int multiplier = 1;
					if (DOM.eventGetCtrlKey(event)) {
						multiplier = (int) (getTotalRange() / stepSize / PERCENTAGE);
					}
					DOM.setCapture(getElement());
					startSliding(true, true);
					DOM.eventPreventDefault(event);
					pageKnob(event, multiplier);
				}
				break;
			case Event.ONMOUSEUP:
				if (slidingMouse) {
					DOM.releaseCapture(getElement());
					slidingMouse = false;
					slideKnob(event);
					stopSliding(true, true);
				} else if (pagingMouse) {
					DOM.releaseCapture(getElement());
					pagingMouse = false;
					keyTimer.cancel();
					stopSliding(true, true);
				}
				break;
			case Event.ONMOUSEMOVE:
				if (slidingMouse) {
					slideKnob(event);
				}
				break;
			default:
			}
		}
	}

	/**
	 * Checks whether the slider was clicked or not.
	 * 
	 * @param event
	 *            the mouse event
	 * @return whether the slider was clicked
	 */
	private boolean sliderClicked(final Event event) {
		boolean sliderClicked = false;

		if (DOM.eventGetTarget(event).equals(knobImage.getElement())) {
			sliderClicked = true;
		}

		return sliderClicked;
	}

	/**
	 * This method is called when the dimensions of the parent element change.
	 * Subclasses should override this method as needed.
	 * 
	 * @param width
	 *            the new client width of the element
	 * @param height
	 *            the new client height of the element
	 */
	public final void onResize(final int width, final int height) {
		// Center the line in the shell
		final int lineHeight = lineElement.getOffsetHeight();
		lineTopOffset = height / 2 - lineHeight / 2;
		DOM.setStyleAttribute(lineElement, "top", lineTopOffset + "px");

		// Draw the other components
		drawKnob();
	}

	/**
	 * Redraw the progress bar when something changes the layout.
	 */
	public final void redraw() {
		if (isAttached()) {
			final int width = getElement().getClientWidth();
			final int height = getElement().getClientHeight();
			onResize(width, height);
		}
	}

	/**
	 * Set the current value and fire the onValueChange event.
	 * 
	 * @param curValue2
	 *            the current value
	 */
	public final void setCurrentValue(final double curValue2) {
		setCurrentValue(curValue2, true);
	}

	/**
	 * Set the current value and optionally fire the onValueChange event.
	 * 
	 * @param curValue2
	 *            the current value
	 * @param fireEvent
	 *            fire the onValue change event if true
	 */
	public final void setCurrentValue(final double curValue2, final boolean fireEvent) {
		// Confine the value to the range
		this.curValue = Math.max(minValue, Math.min(maxValue, curValue2));
		final double remainder = (this.curValue - minValue) % stepSize;
		this.curValue -= remainder;

		// Go to next step if more than halfway there
		if (remainder > stepSize / 2 && this.curValue + stepSize <= maxValue) {
			this.curValue += stepSize;
		}

		// Redraw the knob
		drawKnob();

		// Fire the ValueChangeEvent
		if (fireEvent) {
			ValueChangeEvent.fire(this, this.curValue);
		}
	}

	/**
	 * Sets whether this widget is enabled.
	 * 
	 * @param enabled2
	 *            true to enable the widget, false to disable it
	 */
	public final void setEnabled(final boolean enabled2) {
		this.enabled = enabled2;
		if (enabled) {
			knobImage.setResource(images.slider());
			lineElement.setPropertyString("className", "gwt-SliderBarVertical-line");
		} else {
			knobImage.setResource(images.sliderDisabled());
			lineElement.setPropertyString("className", "gwt-SliderBarVertical-line gwt-SliderBarVertical-line-disabled");
		}
		redraw();
	}

	/**
	 * Set the max value.
	 * 
	 * @param maxValue2
	 *            the max value
	 */
	public final void setMaxValue(final double maxValue2) {
		this.maxValue = maxValue2;
		resetCurrentValue();
	}

	/**
	 * Set the minimum value.
	 * 
	 * @param minValue2
	 *            the current value
	 */
	public final void setMinValue(final double minValue2) {
		this.minValue = minValue2;
		resetCurrentValue();
	}

	/**
	 * Set the step size.
	 * 
	 * @param stepSize2
	 *            the current value
	 */
	public final void setStepSize(final double stepSize2) {
		this.stepSize = stepSize2;
		resetCurrentValue();
	}

	/**
	 * Set the value of the slider in real terms.
	 * 
	 * @param value
	 *            the value
	 */
	@Override
	public final void setValue(final Double value) {
		setCurrentValue(value, false);
	}

	/**
	 * Set the value of the slider in real terms.
	 * 
	 * @param value
	 *            the value
	 * @param fireEvent
	 *            whether to fire an event
	 */
	@Override
	public final void setValue(final Double value, final boolean fireEvent) {
		setCurrentValue(value, fireEvent);
	}

	/**
	 * Shift to the left (smaller value).
	 * 
	 * @param numSteps
	 *            the number of steps to shift
	 */
	public final void shiftUp(final int numSteps) {
		setCurrentValue(getCurrentValue() - numSteps * stepSize);
	}

	/**
	 * Shift to the right (greater value).
	 * 
	 * @param numSteps
	 *            the number of steps to shift
	 */
	public final void shiftDown(final int numSteps) {
		setCurrentValue(getCurrentValue() + numSteps * stepSize);
	}

	/**
	 * Get the percentage of the knob's position relative to the size of the
	 * line. The return value will be between 0.0 and 1.0.
	 * 
	 * @return the current percent complete
	 */
	protected final double getKnobPercent() {
		// If we have no range
		if (maxValue <= minValue) {
			return 0;
		}

		// Calculate the relative progress
		final double percent = (curValue - minValue) / (maxValue - minValue);
		return Math.max(0.0, Math.min(1.0, percent));
	}

	/**
	 * This method is called immediately after a widget becomes attached to the
	 * browser's document.
	 */
	@Override
	protected final void onLoad() {
		// Reset the position attribute of the parent element
		getElement().getStyle().setPosition(Position.RELATIVE);
	}

	/**
	 * Draw the knob where it is supposed to be relative to the line.
	 */
	private void drawKnob() {
		// Abort if not attached
		if (!isAttached()) {
			return;
		}

		// Move the knob to the correct position
		final Element knobElement = knobImage.getElement();
		final int lineHeight = lineElement.getOffsetHeight();
		final int knobHeight = knobElement.getOffsetHeight();
		int knobTopOffset = (int) (lineTopOffset + getKnobPercent() * lineHeight - knobHeight / 2);
		knobTopOffset = Math.min(knobTopOffset, lineTopOffset + lineHeight - knobHeight / 2 - 1);
		DOM.setStyleAttribute(knobElement, "top", knobTopOffset + "px");
	}

	/**
	 * Highlight this widget.
	 */
	private void highlight() {
		final String styleName = getStylePrimaryName();
		DOM.setElementProperty(getElement(), "className", styleName + " " + styleName + "-focused");
	}

	/**
	 * Reset the progress to constrain the progress to the current range and
	 * redraw the knob as needed.
	 */
	private void resetCurrentValue() {
		setCurrentValue(getCurrentValue());
	}

	/**
	 * Slide the knob to a new location.
	 * 
	 * @param event
	 *            the mouse event
	 */
	private void slideKnob(final Event event) {
		final int y = event.getClientY();
		if (y > 0) {
			final int lineHeight = lineElement.getOffsetHeight();
			final int lineTop = lineElement.getAbsoluteTop();
			final double percent = (double) (y - lineTop) / lineHeight * 1.0;
			setCurrentValue(getTotalRange() * percent + minValue, true);
		}
	}

	/**
	 * Perform a page action.
	 * 
	 * @param event
	 *            the event
	 * @param multiplier
	 *            the multiplier
	 */
	private void pageKnob(final Event event, final int multiplier) {
		final int y = event.getClientY();
		if (y > 0) {
			mouseDownPos = getMousePositionValue(y);
			int change = pageSize;
			if (multiplier != 1) {
				change = multiplier;
			}
			if (y < getKnobPosition()) {
				shiftUp(change);
				keyTimer.schedule(INITIALDELAY, true, change);
			} else {
				shiftDown(change);
				keyTimer.schedule(INITIALDELAY, false, change);
			}
		}
	}

	/**
	 * Calculate the mouse's position in real terms.
	 * 
	 * @param y
	 *            the position in coordinates
	 * @return the position
	 */
	private double getMousePositionValue(final int y) {
		final int lineHeight = lineElement.getOffsetHeight();
		final int lineTop = lineElement.getAbsoluteTop();
		final double percent = (double) (y - lineTop) / lineHeight * 1.0;
		return getTotalRange() * percent + minValue;
	}

	/**
	 * Calculate the knob's position in coordinates.
	 * 
	 * @return the knob's position
	 */
	private int getKnobPosition() {
		final Element knobElement = knobImage.getElement();
		return knobElement.getAbsoluteTop();
	}

	/**
	 * Start sliding the knob.
	 * 
	 * @param highlight
	 *            true to change the style
	 * @param fireEvent
	 *            true to fire the event
	 */
	private void startSliding(final boolean highlight, final boolean fireEvent) {
		if (highlight) {
			lineElement.setPropertyString("className", "gwt-SliderBarVertical-line gwt-SliderBarVertical-line-sliding");
			knobImage.getElement().setPropertyString("className", "gwt-SliderBarVertical-knob gwt-SliderBarVertical-knob-sliding");
			knobImage.setResource(images.sliderSliding());
		}
	}

	/**
	 * Stop sliding the knob.
	 * 
	 * @param unhighlight
	 *            true to change the style
	 * @param fireEvent
	 *            true to fire the event
	 */
	private void stopSliding(final boolean unhighlight, final boolean fireEvent) {
		if (unhighlight) {
			lineElement.setPropertyString("className", "gwt-SliderBarVertical-line");

			knobImage.getElement().setPropertyString("className", "gwt-SliderBarVertical-knob");
			knobImage.setResource(images.slider());
		}
	}

	/**
	 * Unhighlight this widget.
	 */
	private void unhighlight() {
		getElement().setPropertyString("className", getStylePrimaryName());
	}

	/**
	 * Handle the resize event.
	 */
	@Override
	public final void onResize() {
		redraw();
	}
}
