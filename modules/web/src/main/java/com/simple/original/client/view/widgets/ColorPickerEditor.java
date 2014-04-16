package com.simple.original.client.view.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class ColorPickerEditor extends Composite implements IsEditor<TakesValueEditor<String>> {
	
	public class SelectableColorBox extends Composite implements TakesValue<String> {
		private String color;
		
		protected SimplePanel colorBox = new SimplePanel();
		
		private Style style = colorBox.getElement().getStyle();
		
		public SelectableColorBox() {
			initWidget(colorBox);
			
			style.setHeight(12, Unit.PX);
			style.setWidth(12, Unit.PX);
			
			style.setBorderWidth(1, Unit.PX);
			style.setBorderColor("#CCCCCC");
			style.setBackgroundColor("green");
			
			colorBox.setHeight("16px");
			colorBox.setWidth("16px");
			
			colorBox.addDomHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					colorPickerContainer.setPopupPositionAndShow(new PositionCallback() {

						@Override
						public void setPosition(int offsetWidth, int offsetHeight) {
							
							final int widgetX = colorBox.getAbsoluteLeft();
							final int widgetY = colorBox.getAbsoluteTop();
							final int widgetWidth = colorBox.getOffsetWidth();
							//final int widgetHeight = colorBox.getOffsetHeight();
							
							int x = widgetX + widgetWidth - 1;
							int y = widgetY;
							
							colorPickerContainer.setPopupPosition(x, y);	
						}
					});
				}
				
			}, ClickEvent.getType());
		}
		
		@Override
		public void setValue(String color) {
			if (color == null) {
				color = "red";
			}
			
			this.color = color;
			style.setBackgroundColor(color);
		}

		@Override
		public String getValue() {
			return color;
		}
	}
	
	private TakesValueEditor<String> selectedColor;
	
	private FlowPanel container = new FlowPanel();
	
	private SelectableColorBox selectableColorBox = new SelectableColorBox();
	
	private PopupPanel colorPickerContainer = new PopupPanel();
	private ColorPicker colorPicker = new ColorPicker();
	
	public ColorPickerEditor() {
		initWidget(container);
		container.add(selectableColorBox);
		
		selectedColor = TakesValueEditor.of(selectableColorBox);
		colorPickerContainer.setWidget(colorPicker);
		
		colorPicker.addSelectionHandler(new SelectionHandler<String>() {

			@Override
			public void onSelection(SelectionEvent<String> event) {
				selectableColorBox.setValue(event.getSelectedItem());
				colorPickerContainer.hide();
			}
		});
	}
	
	@Override
	public TakesValueEditor<String> asEditor() {
		return selectedColor;
	}
	
	public HandlerRegistration addSelectionHandler(SelectionHandler<String> handler) {
		return colorPicker.addSelectionHandler(handler);
	}
}