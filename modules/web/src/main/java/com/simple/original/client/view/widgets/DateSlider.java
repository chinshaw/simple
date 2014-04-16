package com.simple.original.client.view.widgets;

import static com.google.gwt.query.client.GQuery.$;
import static gwtquery.plugins.ui.Ui.Ui;
import gwtquery.plugins.ui.widgets.Slider;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public abstract class DateSlider extends Composite {
	
	private FlowPanel container = new FlowPanel();
	
	private List<Date> dates = null;
	DivElement dateSlider = DOM.createDiv().cast();
	InputElement date = DOM.createInputText().cast();
	
	private Slider slider = null;
	
	public DateSlider() {
		initWidget(container);

		date.getStyle().setBorderStyle(BorderStyle.NONE);
		
		container.getElement().appendChild(date);
		container.getElement().appendChild(dateSlider);
		
		slider = $(dateSlider).as(Ui).slider().bind(Slider.Event.slide, new Function() {
		      @Override
		      public boolean f(Event e, Object data) {
		        Slider.Event slideEvent = ((JavaScriptObject) data).cast();
		        $(date).val(getFormattedDate(slideEvent.intValue()));
		        //$(date).val("" + slideEvent.intValue());
		        return false;
		      }
		    }).bind(Slider.Event.stop, new Function() {
			      @Override
			      public boolean f(Event e, Object data) {
			        Slider.Event stopEvent = ((JavaScriptObject) data).cast();
			        
			        onDateValueChange(dates.get(stopEvent.intValue()));
			        return false;
			      }
		    });
	}
	
	
	public void setDateRange(final List<Date> dates) {
		this.dates = dates;
		
		slider.option(Slider.Options.create().min(0).max(dates.size()).value(dates.size() -1));
		$(date).val(getFormattedDate(dates.size() -1));
	}
	
	
	private String getFormattedDate(int dateIndex) {
	    if (dates == null || dateIndex >= dates.size()) {
	        return "No History Found";
	    }
		return dates.get(dateIndex).toString();
	}
	
	/**
	 * This will be implemented by the creator to get the date change event.
	 * @param date The date that was last selected when the user lets go of the mouse button.
	 */
	public abstract void onDateValueChange(Date date);
}
