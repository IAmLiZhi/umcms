package com.uoumei.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Tom
 */
public class DateUtils {
	private StringBuffer buffer = new StringBuffer();
	private static String ZERO = "0";
	private static DateUtils date;
	public static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat format1 = new SimpleDateFormat(
			"yyyyMMdd HH:mm:ss");
	public static SimpleDateFormat common_format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dayFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static SimpleDateFormat timeFormat = new SimpleDateFormat(
			"HH:mm:ss");
	public static String COMMON_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
	public static String COMMON_FORMAT_SHORT = "yyyy-MM-dd";
	public static String COMMON_FORMAT_MONTH = "yyyy-MM";
	public static String COMMON_FORMAT_MONTH_STR = "MM";
	public static String COMMON_FORMAT_YEAR= "yyyy";
	public static String COMMON_FORMAT_MERGE_SHORT="yyyyMMdd";
	public static String COMMON_FORMAT_MERGE_STR="yyyyMMdd HH:mm:ss";
	public static ThreadLocal<SimpleDateFormat> dayFormatThreadLocal = new ThreadLocal<SimpleDateFormat>();  //格式为yyyy-MM-dd
	public static ThreadLocal<SimpleDateFormat> commonFormatThreadLocal = new ThreadLocal<SimpleDateFormat>();  //格式为yyyy-MM-dd HH:mm:ss
	public static ThreadLocal<SimpleDateFormat> timeFormatThreadLocal = new ThreadLocal<SimpleDateFormat>();  //格式为HH:mm:ss
	public static ThreadLocal<SimpleDateFormat> formatThreadLocal = new ThreadLocal<SimpleDateFormat>();  //格式为yyyyMMdd
	public static ThreadLocal<SimpleDateFormat> format1ThreadLocal = new ThreadLocal<SimpleDateFormat>();  //格式为yyyyMMdd HH:mm:ss
	

	public String getNowString() {
		Calendar calendar = getCalendar();
		buffer.delete(0, buffer.capacity());
		buffer.append(getYear(calendar));

		if (getMonth(calendar) < 10) {
			buffer.append(ZERO);
		}
		buffer.append(getMonth(calendar));

		if (getDate(calendar) < 10) {
			buffer.append(ZERO);
		}
		buffer.append(getDate(calendar));
		if (getHour(calendar) < 10) {
			buffer.append(ZERO);
		}
		buffer.append(getHour(calendar));
		if (getMinute(calendar) < 10) {
			buffer.append(ZERO);
		}
		buffer.append(getMinute(calendar));
		if (getSecond(calendar) < 10) {
			buffer.append(ZERO);
		}
		buffer.append(getSecond(calendar));
		return buffer.toString();
	}

	private static int getDateField(Date date, int field) {
		Calendar c = getCalendar();
		c.setTime(date);
		return c.get(field);
	}

	public static int getYearsBetweenDate(Date begin, Date end) {
		int bYear = getDateField(begin, Calendar.YEAR);
		int eYear = getDateField(end, Calendar.YEAR);
		return eYear - bYear;
	}

	public static int getMonthsBetweenDate(Date begin, Date end) {
		int bMonth = getDateField(begin, Calendar.MONTH);
		int eMonth = getDateField(end, Calendar.MONTH);
		return eMonth - bMonth;
	}

	public static int getWeeksBetweenDate(Date begin, Date end) {
		int bWeek = getDateField(begin, Calendar.WEEK_OF_YEAR);
		int eWeek = getDateField(end, Calendar.WEEK_OF_YEAR);
		return eWeek - bWeek;
	}

	public static int getDaysBetweenDate(Date begin, Date end) {
		return (int) ((end.getTime()-begin.getTime())/(1000 * 60 * 60 * 24));
	}
	
	

	/**
	 * 获取date年后的amount年的第一天的开始时间
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficYearStart(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, amount);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return getStartDate(cal.getTime());
	}

	/**
	 * 获取date年后的amount年的最后一天的终止时间
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficYearEnd(Date date, int amount) {
		Date temp = getStartDate(getSpecficYearStart(date, amount + 1));
		Calendar cal = Calendar.getInstance();
		cal.setTime(temp);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return getFinallyDate(cal.getTime());
	}

	/**
	 * 获取date月后的amount月的第一天的开始时间
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficMonthStart(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, amount);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return getStartDate(cal.getTime());
	}

	/**
	 * 获取当前自然月后的amount月的最后一天的终止时间
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficMonthEnd(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getSpecficMonthStart(date, amount + 1));
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return getFinallyDate(cal.getTime());
	}

	/**
	 * 获取date周后的第amount周的开始时间（这里星期一为一周的开始）
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficWeekStart(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY); /* 设置一周的第一天为星期一 */
		cal.add(Calendar.WEEK_OF_MONTH, amount);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return getStartDate(cal.getTime());
	}

	/**
	 * 获取date周后的第amount周的最后时间（这里星期日为一周的最后一天）
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficWeekEnd(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY); /* 设置一周的第一天为星期一 */
		cal.add(Calendar.WEEK_OF_MONTH, amount);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return getFinallyDate(cal.getTime());
	}

	public static Date getSpecficDateStart(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, amount);
		return getStartDate(cal.getTime());
	}
	
	public static Date getSpecficDateEnd(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, amount);
		return getFinallyDate(cal.getTime());
	}

	/**
	 * 得到指定日期的一天的的最后时刻23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFinallyDate(Date date) {
		SimpleDateFormat sdf = formatThreadLocal.get();
		if (sdf == null ){
			sdf = new SimpleDateFormat(COMMON_FORMAT_MERGE_SHORT);  
			formatThreadLocal.set(sdf);
		}
		String temp = sdf.format(date);
		temp += " 23:59:59";

		try {
			sdf = format1ThreadLocal.get();
			if (sdf == null ){
				sdf = new SimpleDateFormat(COMMON_FORMAT_MERGE_STR);  
				format1ThreadLocal.set(sdf);
			}
			return sdf.parse(temp);
		} catch (Exception e) {
			return new Date();
		}
	}

	/**
	 * 得到指定日期的一天的开始时刻00:00:00
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartDate(Date date) {
		SimpleDateFormat sdf = formatThreadLocal.get();
		if (sdf == null ){
			sdf = new SimpleDateFormat(COMMON_FORMAT_MERGE_SHORT);  
			formatThreadLocal.set(sdf);
		}
		
		String temp = sdf.format(date);
		temp += " 00:00:00";

		try {
			sdf = format1ThreadLocal.get();
			if (sdf == null ){
				sdf = new SimpleDateFormat(COMMON_FORMAT_MERGE_STR);  
				format1ThreadLocal.set(sdf);
			}
			return sdf.parse(temp);
		} catch (Exception e) {
			return new Date();
		}
	}
	
	public static Date getMinuteBeforeTime(Date date, int minute) {
		Date beforeTime = new Date(date.getTime() - 60000*minute); //minute分钟前的时间
		return beforeTime;
	}

	/**
	 * 
	 * @param date
	 *            指定比较日期
	 * @param compareDate
	 * @return
	 */
	public static boolean isInDate(Date date, Date compareDate) {
		int compare=compareDate.compareTo(date);
		if ((compareDate.after(getStartDate(date))
				&& compareDate.before(getFinallyDate(date)))||compare==0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 返回 "11/2这种格式日期"
	 * @return
	 */
	public static String getMonthDayStr(Date d) {
		Calendar calendar=getCalendar();
		calendar.setTime(d);
		return getMonth(calendar)+"/"+getDate(calendar);
	}
	/**
	 * 返回 "当月天数"
	 * @return
	 */
	public static int getMonthDayNum(Date d){
		Calendar calendar=getCalendar();
		calendar.setTime(d);
		calendar.set(Calendar.DATE, 1);//把日期设置为当月第一天  
		calendar.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
	    int maxDate = calendar.get(Calendar.DATE);  
	    return maxDate;  
	}
	
	public static Double getDiffMinuteTwoDate(Date begin, Date end) {
		return (end.getTime() - begin.getTime()) / 1000 / 60.0;
	}
	
	public static Integer getDiffIntMinuteTwoDate(Date begin, Date end) {
		return (int) ((end.getTime() - begin.getTime()) / 1000 / 60);
	}
	
	public static Double getDiffHourTwoDate(Date begin, Date end) {
		return getDiffMinuteTwoDate(begin, end)/60;
	}
	
	public static Integer getDiffIntHourTwoDate(Date begin, Date end) {
		return  getDiffHourTwoDate(begin, end).intValue();
	}
	
	/**
	 * 获取两个时间的差值秒
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Integer getSecondBetweenDate(Date d1,Date d2){
		Long second=(d2.getTime()-d1.getTime())/1000;
		return second.intValue();
	}
	

	private static int getYear(Calendar calendar) {
		return calendar.get(Calendar.YEAR);
	}

	private static int getMonth(Calendar calendar) {
		return calendar.get(Calendar.MONDAY) + 1;
	}

	private static int getDate(Calendar calendar) {
		return calendar.get(Calendar.DATE);
	}

	private static int getHour(Calendar calendar) {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	private static int getMinute(Calendar calendar) {
		return calendar.get(Calendar.MINUTE);
	}

	private static int getSecond(Calendar calendar) {
		return calendar.get(Calendar.SECOND);
	}

	private static Calendar getCalendar() {
		return Calendar.getInstance();
	}
	
	public static Date parseDayStrToDate(String dateStr){
		try {
			SimpleDateFormat sdf = dayFormatThreadLocal.get();
			if (sdf == null ){
				sdf = new SimpleDateFormat(COMMON_FORMAT_SHORT);  
				dayFormatThreadLocal.set(sdf);
			}
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			return new Date();
		}
	}
	
	public static String parseDateToTimeStr(Date  date){
		SimpleDateFormat sdf = commonFormatThreadLocal.get();
		if (sdf == null ){
			sdf = new SimpleDateFormat(COMMON_FORMAT_STR);  
			commonFormatThreadLocal.set(sdf);
		}
		return sdf.format(date);
	}
	
	public static String parseDateToDateStr(Date  date){
		SimpleDateFormat sdf = dayFormatThreadLocal.get();
		if (sdf == null ){
			sdf = new SimpleDateFormat(COMMON_FORMAT_SHORT);  
			dayFormatThreadLocal.set(sdf);
		}
		return sdf.format(date);
	}
	
	public static String parseToShortTimeStr(Date  date){
		return timeFormat.format(date);
	}


	public static DateUtils getDateInstance() {
		if (date == null) {
			date = new DateUtils();
		}
		return date;
	}
	


	public static void main(String args[]) {
		//System.out.println(getDaysBetweenDate(getSpecficMonthStart(Calendar.getInstance().getTime(), 0), new Date()));
		//System.out.println(getSpecficDateStart(new Date(), -28));
		//System.out.println(getSpecficYearStart(Calendar.getInstance().getTime(), 0));
		//System.out.println(getSpecficYearEnd(Calendar.getInstance().getTime(), 0));
		//System.out.println(getSpecficDateStart(Calendar.getInstance().getTime(), -1));
		//System.out.println(getSpecficDateEnd(Calendar.getInstance().getTime(), -1));
		for(int i=0;i<100;i++){
			ThreadTest my = new ThreadTest();
			new Thread(my).start();
		}
	}
}

class ThreadTest implements Runnable{
	public ThreadTest() {
		  
    }
	@Override
	public void run() {
		Date now=Calendar.getInstance().getTime();
		Date to=DateUtils.getSpecficWeekEnd(now, 3);
		String d=DateUtils.parseToShortTimeStr(to);
	}
}

