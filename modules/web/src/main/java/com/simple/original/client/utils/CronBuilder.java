package com.simple.original.client.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class CronBuilder {
	
	
	public interface CronHandler {
		void onCronUpdated(String cronText);
	}
	
	/**
	 * 
	 * From http://www.quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger
	 * 
	 * CronTrigger Tutorial Introduction
	 * 
	 * cron is a UNIX tool that has been around for a long time, so its
	 * scheduling capabilities are powerful and proven. The CronTrigger class is
	 * based on the scheduling capabilities of cron.
	 * 
	 * CronTrigger uses "cron expressions", which are able to create firing
	 * schedules such as: "At 8:00am every Monday through Friday" or
	 * "At 1:30am every last Friday of the month".
	 * 
	 * Cron expressions are powerful, but can be pretty confusing. This tutorial
	 * aims to take some of the mystery out of creating a cron expression,
	 * giving users a resource which they can visit before having to ask in a
	 * forum or mailing list. Format
	 * 
	 * A cron expression is a string comprised of 6 or 7 fields separated by
	 * white space. Fields can contain any of the allowed values, along with
	 * various combinations of the allowed special characters for that field.
	 * The fields are as follows: Field Name Mandatory Allowed Values Allowed
	 * Special Characters Seconds YES 0-59 , - * / Minutes YES 0-59 , - * /
	 * Hours YES 0-23 , - * / Day of month YES 1-31 , - * ? / L W Month YES 1-12
	 * or JAN-DEC , - * / Day of week YES 1-7 or SUN-SAT , - * ? / L # Year NO
	 * empty, 1970-2099 , - * /
	 * 
	 * So cron expressions can be as simple as this: * * * * ? *
	 * 
	 * or more complex, like this: 0/5 14,18,3-39,52 * ? JAN,MAR,SEP MON-FRI
	 * 2002-2010 Special characters
	 * 
	 * ("all values") - used to select all values within a field. For example,
	 * "" in the minute field means *"every minute".
	 * 
	 * ? ("no specific value") - useful when you need to specify something in
	 * one of the two fields in which the character is allowed, but not the
	 * other. For example, if I want my trigger to fire on a particular day of
	 * the month (say, the 10th), but don't care what day of the week that
	 * happens to be, I would put "10" in the day-of-month field, and "?" in the
	 * day-of-week field. See the examples below for clarification.
	 * 
	 * - - used to specify ranges. For example, "10-12" in the hour field means
	 * "the hours 10, 11 and 12".
	 * 
	 * , - used to specify additional values. For example, "MON,WED,FRI" in the
	 * day-of-week field means "the days Monday, Wednesday, and Friday".
	 * 
	 * / - used to specify increments. For example, "0/15" in the seconds field
	 * means "the seconds 0, 15, 30, and 45". And "5/15" in the seconds field
	 * means "the seconds 5, 20, 35, and 50". You can also specify '/' after the
	 * '' character - in this case '' is equivalent to having '0' before the
	 * '/'. '1/3' in the day-of-month field means
	 * "fire every 3 days starting on the first day of the month".
	 * 
	 * L ("last") - has different meaning in each of the two fields in which it
	 * is allowed. For example, the value "L" in the day-of-month field means
	 * "the last day of the month" - day 31 for January, day 28 for February on
	 * non-leap years. If used in the day-of-week field by itself, it simply
	 * means "7" or "SAT". But if used in the day-of-week field after another
	 * value, it means "the last xxx day of the month" - for example "6L" means
	 * "the last friday of the month". When using the 'L' option, it is
	 * important not to specify lists, or ranges of values, as you'll get
	 * confusing results.
	 * 
	 * W ("weekday") - used to specify the weekday (Monday-Friday) nearest the
	 * given day. As an example, if you were to specify "15W" as the value for
	 * the day-of-month field, the meaning is:
	 * "the nearest weekday to the 15th of the month". So if the 15th is a
	 * Saturday, the trigger will fire on Friday the 14th. If the 15th is a
	 * Sunday, the trigger will fire on Monday the 16th. If the 15th is a
	 * Tuesday, then it will fire on Tuesday the 15th. However if you specify
	 * "1W" as the value for day-of-month, and the 1st is a Saturday, the
	 * trigger will fire on Monday the 3rd, as it will not 'jump' over the
	 * boundary of a month's days. The 'W' character can only be specified when
	 * the day-of-month is a single day, not a range or list of days.
	 * 
	 * The 'L' and 'W' characters can also be combined in the day-of-month field
	 * to yield 'LW', which translates to *"last weekday of the month"*.
	 * 
	 * # - used to specify "the nth" XXX day of the month. For example, the
	 * value of "6#3" in the day-of-week field means
	 * "the third Friday of the month" (day 6 = Friday and "#3" = the 3rd one in
	 * the month). Other examples: "2#1" = the first Monday of the month and
	 * "4#5" = the fifth Wednesday of the month. Note that if you specify "#5"
	 * and there is not 5 of the given day-of-week in the month, then no firing
	 * will occur that month.
	 * 
	 * The legal characters and the names of months and days of the week are not
	 * case sensitive. MON is the same as mon.
	 * 
	 * Examples
	 * 
	 * Here are some full examples: Expression Meaning 0 0 12 * * ? Fire at 12pm
	 * (noon) every day 0 15 10 ? * * Fire at 10:15am every day 0 15 10 * * ?
	 * Fire at 10:15am every day 0 15 10 * * ? * Fire at 10:15am every day 0 15
	 * 10 * * ? 2005 Fire at 10:15am every day during the year 2005 0 * 14 * * ?
	 * Fire every minute starting at 2pm and ending at 2:59pm, every day 0 0/5
	 * 14 * * ? Fire every 5 minutes starting at 2pm and ending at 2:55pm, every
	 * day 0 0/5 14,18 * * ? Fire every 5 minutes starting at 2pm and ending at
	 * 2:55pm, AND fire every 5 minutes starting at 6pm and ending at 6:55pm,
	 * every day 0 0-5 14 * * ? Fire every minute starting at 2pm and ending at
	 * 2:05pm, every day 0 10,44 14 ? 3 WED Fire at 2:10pm and at 2:44pm every
	 * Wednesday in the month of March. 0 15 10 ? * MON-FRI Fire at 10:15am
	 * every Monday, Tuesday, Wednesday, Thursday and Friday 0 15 10 15 * ? Fire
	 * at 10:15am on the 15th day of every month 0 15 10 L * ? Fire at 10:15am
	 * on the last day of every month 0 15 10 ? * 6L Fire at 10:15am on the last
	 * Friday of every month 0 15 10 ? * 6L Fire at 10:15am on the last Friday
	 * of every month 0 15 10 ? * 6L 2002-2005 Fire at 10:15am on every last
	 * friday of every month during the years 2002, 2003, 2004 and 2005 0 15 10
	 * ? * 6#3 Fire at 10:15am on the third Friday of every month 0 0 12 1/5 * ?
	 * Fire at 12pm (noon) every 5 days every month, starting on the first day
	 * of the month. 0 11 11 11 11 ? Fire every November 11th at 11:11am.
	 * 
	 * Pay attention to the effects of '?' and '*' in the day-of-week and
	 * day-of-month fields!
	 * 
	 * Notes
	 * 
	 * Support for specifying both a day-of-week and a day-of-month value is not
	 * complete (you must currently use the '?' character in one of these
	 * fields). Be careful when setting fire times between mid-night and 1:00 AM
	 * - "daylight savings" can cause a skip or a repeat depending on whether
	 * the time moves back or jumps forward.
	 */
    protected static final int ALL_SPEC_INT = 99; // '*'
    protected static final int NO_SPEC_INT = 98; // '?'
    protected static final Integer ALL_SPEC = Integer.valueOf(ALL_SPEC_INT);
    protected static final Integer NO_SPEC = Integer.valueOf(NO_SPEC_INT);
    
   
    
    public List<CronHandler> handlers = new ArrayList<CronHandler>();
	
	public enum OCCURRENCE {
		HOURLY, DAILY, MONTHLY,
	}

	public enum DAY {
		SUNDAY("SUN"), MONDAY("MON"), TUESDAY("TUE"), WEDNESDAY("WED"), THURSDAY(
				"THU"), FRIDAY("FRI"), SATURDAY("SAT");

		private String value;

		DAY(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	private SortedSet<DAY> days = new TreeSet<DAY>();

	private OCCURRENCE occurrence = OCCURRENCE.HOURLY;

	private String day = "01";

	private int hour = 0;

	private int minute = 0;

	public String build() {
		
	    //String cronExpression = String.format("0 %d %d %d * ?", minute, hour, dayOfMonth);
		String cronText = getTime() + " " + getRecurrence();

		return cronText;

	}

	public String getDaysOfWeek() {
		String cronText;
		int daysCount = days.size();

		if (daysCount < 7 && daysCount > 0) {
			cronText = "";
			for (Iterator<DAY> iter = days.iterator(); iter.hasNext();) {
				cronText += iter.next().getValue();
				if (iter.hasNext()) {
					cronText += ",";
				}
			}
		} else if (daysCount == 7) {
			cronText = "*";
		} else {
			cronText = "?";
		}

		return cronText;
	}

	public CronBuilder enableDayOfWeek(DAY day) {
		return setDayOfWeek(day, true);
	}

	public CronBuilder disableDayOfWeek(DAY day) {
		return setDayOfWeek(day, false);
	}

	public CronBuilder setDayOfWeek(DAY day, boolean bool) {
		bool = (bool) ? days.add(day) : days.remove(day);
		return this;
	}

	public String getRecurrence() {
		String cronText = "";

		if (occurrence == OCCURRENCE.HOURLY) {
			cronText = "? * " + getDaysOfWeek();
		} else if (occurrence == OCCURRENCE.DAILY) {
			cronText = "? * " + getDaysOfWeek();
		} else if (occurrence == OCCURRENCE.MONTHLY) {
			//cronText = "L * ?";
			cronText = "" + day + " * ?";
		}

		return cronText;
	}

	public String getTime() {
		String cronText = "0 0 *"; // Every hour
		if (occurrence == OCCURRENCE.HOURLY) {
			cronText = "0 "+ minute + " * ";
		} else if (occurrence == OCCURRENCE.DAILY) {
			cronText = "0 "+ minute + " " + hour;
		}
		return cronText;
	}

	public CronBuilder setOccurrence(OCCURRENCE occurrence) {
		this.occurrence = occurrence;
		updateCronTab();
		return this;
	}
	
	public String getDay() {
		return day;
	}

	public CronBuilder setDay(String day) {
		this.day = day;
		updateCronTab();
		return this;
	}

	public int getHour() {
		return hour;
	}

	public CronBuilder setHour(int hour) {
		this.hour = hour;
		updateCronTab();
		return this;
	}

	public int getMinute() {
		return minute;
	}

	public CronBuilder setMinute(int minute) {
		this.minute = minute;
		updateCronTab();
		return this;
	}
	
	
	private void updateCronTab() {
		for (CronHandler handler : handlers) {
			handler.onCronUpdated(build());
		}
	}
	
	public void addCronHandler(CronHandler handler) {
		this.handlers.add(handler);
	}
	
	public void removeCronHandler(CronHandler handler) {
		this.handlers.remove(handler);
	}
}
