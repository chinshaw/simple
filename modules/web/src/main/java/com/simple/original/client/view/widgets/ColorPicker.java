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


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

public class ColorPicker extends Widget implements HasSelectionHandlers<String> {

	private static final ColorResources res = GWT.create(ColorResources.class);

	public ColorPicker() {
		this(res.defaultColors().getText());
	}

	public ColorPicker(String paletteJson) {

		res.style().ensureInjected();

		StringBuilder html = new StringBuilder("<table><tbody>");

		JSONArray rows = (JSONArray) JSONParser.parseStrict(paletteJson);
		for (int i = 0; i < rows.size(); i++) {
			JSONArray row = (JSONArray) rows.get(i);
			html.append("<tr>");
			for (int j = 0; j < row.size(); j++) {
				String color = ((JSONString) row.get(j)).stringValue();
				html.append("<td><div style='background-color:").append(color).append("'></div></td>");
			}
			html.append("</tr>");
		}
		html.append("</tbody></table>");

		Element element = DOM.createSpan();
		element.addClassName(res.style().colorPicker());
		element.setInnerHTML(html.toString());
		setElement(element);

		sinkEvents(Event.ONCLICK);
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		if (event.getTypeInt() == Event.ONCLICK) {
			Element target = event.getEventTarget().cast();
			if (target.getTagName().equals("DIV")) {
				String color = target.getStyle().getBackgroundColor();
				SelectionEvent.fire(this, color);
			}
		}
	}

	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<String> handler) {
		return addHandler(handler, SelectionEvent.getType());
	}
}
