package com.simple.original.client.view.desktop;

import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.api.analytics.ITaskExecution;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.utils.CronBuilder;
import com.simple.original.client.utils.CronBuilder.DAY;
import com.simple.original.client.utils.CronBuilder.OCCURRENCE;
import com.simple.original.client.view.IAnalyticsTaskSchedulerView;
import com.simple.original.client.view.widgets.DaySelector;
import com.simple.original.client.view.widgets.EnumEditor;
import com.simple.original.client.view.widgets.ErrorPanel;
import com.simple.original.client.view.widgets.MinuteSelector;
import com.simple.original.client.view.widgets.SmallTimePicker;

public class AnalyticsTaskSchedulerView extends AbstractView implements
		IAnalyticsTaskSchedulerView, CronBuilder.CronHandler {

	private static final Logger logger = Logger
			.getLogger(AnalyticsTaskSchedulerView.class.getName());
	
	@UiTemplate("AnalyticsTaskSchedulerView.ui.xml")
	public interface Binder extends
			UiBinder<Widget, AnalyticsTaskSchedulerView> {
	}

	@UiField
	ErrorPanel errorPanel;

	@UiField
	SpanElement analyticsTaskName;
	
	@UiField DivElement 
	timeSelectorWrapper;
	
	@UiField
	DivElement daySelectorWrapper;
	
	@UiField
	DaySelector daySelector;
	
	@UiField(provided = true)
	SmallTimePicker timeSelector;
	
	@UiField
	DivElement minuteSelectorWrapper;
	
	@UiField
	MinuteSelector minuteSelector;

	@UiField(provided=true)
	EnumEditor<OCCURRENCE> occurrence = new EnumEditor<OCCURRENCE>(OCCURRENCE.class);
	
	@UiField
	TextBox scheduleName;

	@UiField
	CheckBox sunday;

	@UiField
	CheckBox monday;
	
	@UiField
	CheckBox tuesday;
	
	@UiField
	CheckBox wednesday;
	
	@UiField
	CheckBox thursday;
	
	@UiField
	CheckBox friday;
	
	@UiField
	CheckBox saturday;

	@UiField
	TextBox cronText;
	
	@Ignore
	@UiField
	DateBox schedulerDateTime;

	@Ignore
	@UiField
	Image calendarImage;

	@UiField
	TableElement dayChooser;
	
	
	private CronBuilder cronBuilder = new CronBuilder();
	
	private ValueChangeHandler<Boolean> dayHandler = new ValueChangeHandler<Boolean>() {

		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {

			CheckBox sender = (CheckBox) event.getSource();
			
			if (sender == sunday) {
				cronBuilder.setDayOfWeek(DAY.SUNDAY, sender.getValue());
			} else if (sender == monday) {
				cronBuilder.setDayOfWeek(DAY.MONDAY, sender.getValue());
			} else if (sender == tuesday) {
				cronBuilder.setDayOfWeek(DAY.TUESDAY, sender.getValue());
			} else if (sender == wednesday) {
				cronBuilder.setDayOfWeek(DAY.WEDNESDAY, sender.getValue());
			} else if (sender == thursday) {
				cronBuilder.setDayOfWeek(DAY.THURSDAY, sender.getValue());
			} else if (sender == friday) {
				cronBuilder.setDayOfWeek(DAY.FRIDAY, sender.getValue());
			} else if (sender == saturday) {
				cronBuilder.setDayOfWeek(DAY.SATURDAY, sender.getValue());
			}
		}
	};

	/*
	private ValueChangeHandler<Date> dateHandler = new ValueChangeHandler<Date>() {

		@Override
		public void onValueChange(ValueChangeEvent<Date> event) {
			
			Date date = event.getValue();
	        if (date.before(new Date())) {
	         schedulerDateTime.setValue(new Date());
	        }
			cronBuilder.setDay(DateTimeFormat.getFormat("dd").format(event.getValue()));
			logger.info("day is "+DateTimeFormat.getFormat("dd").format(event.getValue())+" month is "+DateTimeFormat.getFormat("MM").format(event.getValue()));
			cronBuilder.setMonth(DateTimeFormat.getFormat("MM").format(event.getValue()));
			cronBuilder.setDay(DateTimeFormat.getFormat("EEE").format(event.getValue()));
		}
	};
	*/

	private Presenter presenter;

	@Inject
	public AnalyticsTaskSchedulerView(EventBus eventBus, Resources resources) {
		super(eventBus, resources);
		
		cronBuilder.addCronHandler(this);
		
		timeSelector = new SmallTimePicker(new Date(), true);
		
		timeSelector.addValueChangeHandler(new ValueChangeHandler<Long>() {

			@Override
			public void onValueChange(ValueChangeEvent<Long> event) {
				cronBuilder.setMinute(timeSelector.getMinutesValue());
				cronBuilder.setHour(timeSelector.getHoursValue());
				logger.info("Setting hours to be " +timeSelector.getHoursValue());
			}
			
		});

		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

		schedulerDateTime.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM\\dd\\yyyy")));

		sunday.addValueChangeHandler(dayHandler);
		monday.addValueChangeHandler(dayHandler);
		tuesday.addValueChangeHandler(dayHandler);
		wednesday.addValueChangeHandler(dayHandler);
		thursday.addValueChangeHandler(dayHandler);
		friday.addValueChangeHandler(dayHandler);
		saturday.addValueChangeHandler(dayHandler);

		//schedulerDateTime.setValue(new Date());
		//schedulerDateTime.addValueChangeHandler(dateHandler);

		calendarImage.setResource(resources.calendarIcon());
		calendarImage.setStyleName("imageIcon", true);
        calendarImage.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (schedulerDateTime.isDatePickerShowing()) {
                	schedulerDateTime.hideDatePicker();
                } else {
                	schedulerDateTime.showDatePicker();
                }
            }
        });
        
		checkAllDays();

		occurrence.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				OCCURRENCE value = occurrence.getValue();
				cronBuilder.setOccurrence(value);
				
				if (value == OCCURRENCE.HOURLY) {
					showDaySelector(false);
					showMinuteSelector(true);
					showTimeSelector(false);
					showDayOfWeekSelector(true);
				} else if ( value == OCCURRENCE.DAILY) {
					showDayOfWeekSelector(true);
					showDaySelector(false);
					showTimeSelector(true);
					showMinuteSelector(false);
				} else if (value == OCCURRENCE.MONTHLY) {
					showDayOfWeekSelector(false);
					showMinuteSelector(false);
					showTimeSelector(false);
					showDaySelector(true);
				} 
			}
		});
		
		occurrence.setValue(OCCURRENCE.HOURLY);
		
		
		daySelector.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				cronBuilder.setDay(daySelector.getValue());
			}
		});
		
		
		minuteSelector.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				cronBuilder.setMinute(minuteSelector.getValue());
			}
		});
	}

	@Override
	protected ErrorPanel getErrorPanel() {
	    return errorPanel;
	}

	@Override
	public void reset() {
		errorPanel.clear();
		scheduleName.setValue("");
		checkAllDays();
	}

	@Override
	public void setAnalyticsTaskName(String name) {
		analyticsTaskName.setInnerHTML(name);
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@UiHandler("schedule")
	void onSchedule(ClickEvent event) {
	    if (scheduleName.getValue().length() > 5) {
	        presenter.onScheduleAnalyticsTask(cronText.getValue());
	    } else {
	        showError("Schedule name must be between 5 and 40 characters");
	    }
	}
	
	private void checkAllDays() {
		sunday.setValue(true, true);
		monday.setValue(true, true);
		tuesday.setValue(true, true);
		wednesday.setValue(true, true);
		thursday.setValue(true, true);
		friday.setValue(true, true);
		saturday.setValue(true, true);
	}

	@Override
	public String getScheduleName() {
		return scheduleName.getValue();
	}
	
	private void showDaySelector(boolean show) {
		if (show) {
			daySelectorWrapper.getStyle().setDisplay(Display.BLOCK);
		} else {
			daySelectorWrapper.getStyle().setDisplay(Display.NONE);
		}
	}
	
	private void showMinuteSelector(boolean show) {
		if (show) {
			minuteSelectorWrapper.getStyle().setDisplay(Display.BLOCK);
		} else {
			minuteSelectorWrapper.getStyle().setDisplay(Display.NONE);
		}
		
	}
	
	private void showTimeSelector(boolean show) {
		if (show) {
			timeSelectorWrapper.getStyle().setDisplay(Display.BLOCK);
		} else {
			timeSelectorWrapper.getStyle().setDisplay(Display.NONE);
		}
	}
	
	private void showDayOfWeekSelector(boolean show) {
		if (show) {
			dayChooser.getStyle().setDisplay(Display.BLOCK);
		} else {
			dayChooser.getStyle().setDisplay(Display.NONE);
		}
	}

    @Override
    public void setTriggerState(String response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public HasData<ITaskExecution> getHistoryDisplay() {
        return null;
    }

	@Override
	@Ignore
	public DateBox getScheduleStartDate() {
		return schedulerDateTime;
	}

	@Override
	public void onCronUpdated(String cronText) {
		this.cronText.setValue(cronText);
	}
	
	@UiHandler("cancel")
	void onCancel(ClickEvent event) {
		presenter.onCancel();
	}
}