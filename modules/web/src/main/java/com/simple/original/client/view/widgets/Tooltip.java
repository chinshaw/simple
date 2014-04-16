package com.simple.original.client.view.widgets;

/*
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

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.Widget;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.resources.ResourcesFactory;

public class Tooltip implements DynamicTooltip {

	// private static final TooltipResources res =
	// GWT.create(TooltipResources.class);

	public static enum TooltipPosition {
		CURSOR, BELOW_LEFT, BELOW_CENTER, BELOW_RIGHT, ABOVE_LEFT, ABOVE_CENTER, ABOVE_RIGHT, LEFT_TOP, LEFT_MIDDLE, LEFT_BOTTOM, RIGHT_TOP, RIGHT_MIDDLE, RIGHT_BOTTOM
	}

	public static interface SetTooltipCallback {
		void onShow(Widget target, DynamicTooltip tooltip);
	}

	private SetTooltipCallback callback;

	private final Map<Widget, List<HandlerRegistration>> widgetHandlerRegistrations = new IdentityHashMap<Widget, List<HandlerRegistration>>();
	private final PopupPanel panel = new PopupPanel();
	private HandlerRegistration moveHandlerRegistration;

	private int mouseX, mouseY;
	private Widget widget;

	final Timer timer = new Timer() {

		@Override
		public void run() {

			if (position == TooltipPosition.CURSOR) {
				moveHandlerRegistration.removeHandler();
			}

			if (callback != null) {
				callback.onShow(widget, Tooltip.this);
			} else {
				showTooltip();
			}
		}
	};

	private final MouseOutHandler mouseOutHandler = new MouseOutHandler() {

		@Override
		public void onMouseOut(MouseOutEvent event) {
			timer.cancel();

			if (panel.isShowing() && isMouseOutOfWidget(event, widget) && isMouseOutOfWidget(event, panel)) {
				panel.hide();
			}

		}
	};

	private boolean isMouseOutOfWidget(MouseEvent<?> event, Widget widget) {
		int x = event.getClientX();
		int y = event.getClientY();
		return x <= widget.getAbsoluteLeft() || x >= widget.getAbsoluteLeft() + widget.getOffsetWidth() || y <= widget.getAbsoluteTop()
				|| y >= widget.getAbsoluteTop() + widget.getOffsetHeight();
	}

	private final MouseOverHandler handler = new MouseOverHandler() {

		@Override
		public void onMouseOver(final MouseOverEvent event) {

			if (widget == event.getSource() && panel.isShowing()) {
				return;
			}

			widget = (Widget) event.getSource();

			if (position == TooltipPosition.CURSOR) {
				mouseX = event.getNativeEvent().getClientX();
				mouseY = event.getNativeEvent().getClientY();

				moveHandlerRegistration = widget.addDomHandler(new MouseMoveHandler() {

					@Override
					public void onMouseMove(MouseMoveEvent event) {
						mouseX = event.getClientX();
						mouseY = event.getClientY();
					}
				}, MouseMoveEvent.getType());
			}

			timer.schedule(400);

		}
	};

	private TooltipPosition position = TooltipPosition.CURSOR;
	
	private static Resources resources = ResourcesFactory.getResources();

	public Tooltip() {
		panel.addDomHandler(mouseOutHandler, MouseOutEvent.getType());
		panel.setStyleName(resources.style().toolTip());
	}

	public Tooltip(IsWidget content) {
		this();
		panel.setWidget(content);
	}

	public Tooltip setContent(IsWidget content) {
		panel.setWidget(content);
		return this;
	}

	public Tooltip setText(String text) {
		panel.setWidget(new Label(text));
		return this;
	}

	public Tooltip setHtml(SafeHtml html) {
		panel.setWidget(new HTML(html));
		return this;
	}

	public Tooltip setPosition(TooltipPosition position) {
		this.position = position;
		return this;
	}

	public Tooltip attachTo(Widget widget) {
		HandlerRegistration mouseOverHandlerRegistration = widget.addDomHandler(handler, MouseOverEvent.getType());
		HandlerRegistration mouseOutHandlerRegistration = widget.addDomHandler(mouseOutHandler, MouseOutEvent.getType());
		widgetHandlerRegistrations.put(widget, Arrays.asList(mouseOverHandlerRegistration, mouseOutHandlerRegistration));
		return this;
	}

	public Tooltip setCallback(SetTooltipCallback callback) {
		this.callback = callback;
		return this;
	}

	public Tooltip setStyleName(String style) {
		panel.setStyleName(style);
		return this;
	}

	public Tooltip addStyleName(String style) {
		panel.addStyleName(style);
		return this;
	}

	private void showTooltip() {
		panel.setPopupPositionAndShow(new PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) {

				int x = 0, y = 0;

				final int widgetX = widget.getAbsoluteLeft();
				final int widgetY = widget.getAbsoluteTop();
				final int widgetWidth = widget.getOffsetWidth();
				final int widgetHeight = widget.getOffsetHeight();

				switch (position) {
				case CURSOR:
					x = mouseX;
					y = mouseY;
					break;
				case ABOVE_CENTER:
					x = widgetX + (widgetWidth - offsetWidth) / 2;
					y = widgetY - offsetHeight + 1;
					break;
				case ABOVE_LEFT:
					x = widgetX;
					y = widgetY - offsetHeight + 1;
					break;
				case ABOVE_RIGHT:
					x = widgetX + widgetWidth - offsetWidth;
					y = widgetY - offsetHeight + 1;
					break;
				case BELOW_CENTER:
					x = widgetX + (widgetWidth - offsetWidth) / 2;
					y = widgetY + widgetHeight - 1;
					break;
				case BELOW_LEFT:
					x = widgetX;
					y = widgetY + widgetHeight - 1;
					break;
				case BELOW_RIGHT:
					x = widgetX + widgetWidth - offsetWidth;
					y = widgetY + widgetHeight - 1;
					break;
				case LEFT_TOP:
					x = widgetX - offsetWidth + 1;
					y = widgetY;
					break;
				case LEFT_MIDDLE:
					x = widgetX - offsetWidth + 1;
					y = widgetY + (widgetHeight - offsetHeight) / 2;
					break;
				case LEFT_BOTTOM:
					x = widgetX - offsetWidth + 1;
					y = widgetY + widgetHeight - offsetHeight;
					break;
				case RIGHT_TOP:
					x = widgetX + widgetWidth - 1;
					y = widgetY;
					break;
				case RIGHT_MIDDLE:
					x = widgetX + widgetWidth - 1;
					y = widgetY + (widgetHeight - offsetHeight) / 2;
					break;
				case RIGHT_BOTTOM:
					x = widgetX + widgetWidth - 1;
					y = widgetY + widgetHeight - offsetHeight;
					break;
				}

				panel.setPopupPosition(x, y);
			}
		});
	}

	@Override
	public void showHTML(SafeHtml html) {
		if (panel.getWidget() instanceof HTML) {
			((HTML) panel.getWidget()).setHTML(html);
		} else {
			panel.setWidget(new HTML(html));
		}
		showTooltip();
	}

	@Override
	public void showWidget(IsWidget widget) {
		panel.setWidget(widget);
		showTooltip();
	}

}
