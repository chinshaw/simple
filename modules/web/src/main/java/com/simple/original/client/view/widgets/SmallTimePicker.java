package com.simple.original.client.view.widgets;

import java.util.Date;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.resources.ResourcesFactory;

/**
 * Same as the TimeBox, but uses different styles to show an smaller widget.
 * 
 * @author Carlos Tasada
 *
 */
public class SmallTimePicker extends TimePicker {
    
    private static Resources resources = ResourcesFactory.getResources();
    
	private static final String STYLE_TIMEPICKER = "timePicker";
	private static final String STYLE_TIMEPICKER_ENTRY = "timePickerEntrySmall";
	private static final String STYLE_TIMEPICKER_READONLY = "timePickerReadOnlySmall";
	private static final ImageResource IMG_TIMEPICKER_AM = resources.timePickerAMSmall();
	private static final ImageResource IMG_TIMEPICKER_PM = resources.timePickerPMSmall();
	
	public SmallTimePicker(Date time) {
		this(time, false);
	}

	public SmallTimePicker(Date time, boolean useAMPM) {
		this(time, TIME_PRECISION.QUARTER_HOUR, useAMPM);
	}
	
	public SmallTimePicker(Date time, TIME_PRECISION precision, boolean useAMPM) {
		super(time, precision, useAMPM);
	}

	protected String getStyleTimePickerEntry() {
		return STYLE_TIMEPICKER_ENTRY;
	}

	protected String getStyleTimePicker() {
		return STYLE_TIMEPICKER;
	}

	protected String getStyleTimePickerReadOnly() {
		return STYLE_TIMEPICKER_READONLY;
	}

	protected Image getStyleTimePickerAM() {
		return new Image(IMG_TIMEPICKER_AM);
	}

	protected Image getStyleTimePickerPM() {
		return new Image(IMG_TIMEPICKER_PM);
	}
}
