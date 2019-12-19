package com.weisen.www.code.yjf.login.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;

import io.micrometer.core.instrument.util.StringUtils;

public class TimeUtil {

	// 一分钟
	private static final long ONE_MINUTE = 60000L; // 单位:毫秒

	// 一小时
	private static final long ONE_HOUR = 3600000L;

	// 一天
	private static final long ONE_DAY = 86400000L;

//		// 一周
//		private static final long ONE_WEEK = 604800000L;

//		// 一年
//		private static final long ONE_YEAR = 31557600000L;

	private static final String ONE_SECOND_AGO = "秒前";

	private static final String ONE_MINUTE_AGO = "分钟前";

	private static final String ONE_HOUR_AGO = "个小时前";

	private static final String ONE_DAY_AGO = "天前";

//		private static final String ONE_MONTH_AGO = "个月前";

//		private static final String ONE_YEAR_AGO = "年前";

	private static int TIME_ZONE = 0;

	static {
		Calendar cal = Calendar.getInstance();
		int offset = cal.get(Calendar.ZONE_OFFSET);
		cal.add(Calendar.MILLISECOND, -offset);
		long timeStampUTC = cal.getTimeInMillis();
		long timeStamp = System.currentTimeMillis();
		TIME_ZONE = (int) (timeStamp - timeStampUTC) / (1000 * 3600);
	}

	public static Instant now() {
		return Instant.now().plus(TIME_ZONE, ChronoUnit.HOURS);
	}

	public static boolean isToday(Instant instant) {
		Instant truncatedInstant = instant.truncatedTo(ChronoUnit.DAYS);
		Instant todayInstant = Instant.now().truncatedTo(ChronoUnit.DAYS);
		return truncatedInstant.equals(todayInstant);
	}

	public static boolean isBefore(Instant instant, long offset, TemporalUnit unit) {
		return instant.plus(offset, unit).isAfter(Instant.now());
	}

	/**
	 * 返回一个当前时间
	 * 
	 * @return
	 */
	public static String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	/**
	 * 返回一个时间节点
	 * 
	 * @return
	 */
	public static String getDateTimes() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return format.format(new Date());
	}

	/**
	 * 将字符串转换为时间
	 * 
	 * @param timeStr
	 * @return
	 */
	public static Date formatDate(String timeStr) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(timeStr);
			return date;
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取当前时间
	 *
	 * @param //args
	 */
	public static String getNowTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		String lastMonth = dft.format(cal.getTime());
		return lastMonth;
	}

	/**
	 * 判断当天是否为本月第一天
	 *
	 * @return
	 */
	public static boolean isFirstDayOfMonth() {
		boolean flag = false;
		Calendar calendar = Calendar.getInstance();
		int today = calendar.get(calendar.DAY_OF_MONTH);
		if (1 == today) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 获取当前月份最后一天
	 *
	 * // * @param date
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static String getMaxMonthDate() {
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		// calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return dft.format(calendar.getTime());
	}

	/**
	 *
	 * 描述:获取下一个月的第一天.
	 *
	 * @return
	 */
	public static String getPerFirstDayOfMonth() {
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return dft.format(calendar.getTime());
	}

	/**
	 *
	 * 描述:获取上个月的最后一天.
	 *
	 * @return
	 */
	public static String getLastMaxMonthDate() {
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return dft.format(calendar.getTime());
	}

	/**
	 * 获取上一个月
	 *
	 * @return
	 */
	public static String getLastMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(cal.MONTH, -1);
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMM");
		String lastMonth = dft.format(cal.getTime());
		return lastMonth;
	}

	/**
	 *
	 * 描述:获取下一个月.
	 *
	 * @return
	 */
	public static String getPreMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(cal.MONTH, 1);
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMM");
		String preMonth = dft.format(cal.getTime());
		return preMonth;
	}

//    // 是否是最后一天
//    public static boolean isLastDayOfMonth() {
//        boolean flag = false;
//        if (StringUtils.isNotBlank(getNowTime()) && StringUtils.isNotBlank(getMaxMonthDate()) && StringUtils.equals(getNowTime(), getMaxMonthDate())) { // getMaxMonthDate().equals(getNowTime())
//            flag = true;
//        }
//        return flag;
//    }

	/**
	 * 获取任意时间的下一个月 描述:<描述函数实现的功能>.
	 * 
	 * @param repeatDate
	 * @return
	 */
	public static String getPreMonth(String repeatDate) {
		String lastMonth = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
		int year = Integer.parseInt(repeatDate.substring(0, 4));
		String monthsString = repeatDate.substring(4, 6);
		int month;
		if ("0".equals(monthsString.substring(0, 1))) {
			month = Integer.parseInt(monthsString.substring(1, 2));
		} else {
			month = Integer.parseInt(monthsString.substring(0, 2));
		}
		cal.set(year, month, Calendar.DATE);
		lastMonth = dft.format(cal.getTime());
		return lastMonth;
	}

	/**
	 * 获取任意时间的上一个月 描述:<描述函数实现的功能>.
	 * 
	 * @param repeatDate
	 * @return
	 */
	public static String getLastMonth(String repeatDate) {
		String lastMonth = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
		int year = Integer.parseInt(repeatDate.substring(0, 4));
		String monthsString = repeatDate.substring(4, 6);
		int month;
		if ("0".equals(monthsString.substring(0, 1))) {
			month = Integer.parseInt(monthsString.substring(1, 2));
		} else {
			month = Integer.parseInt(monthsString.substring(0, 2));
		}
		cal.set(year, month - 2, Calendar.DATE);
		lastMonth = dft.format(cal.getTime());
		return lastMonth;
	}

	/**
	 * 获取任意时间的月的最后一天 描述:<描述函数实现的功能>.
	 * 
	 * @param repeatDate
	 * @return
	 */
	private static String getMaxMonthDate(String repeatDate) {
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		try {
			if (StringUtils.isNotBlank(repeatDate) && !"null".equals(repeatDate)) {
				calendar.setTime(dft.parse(repeatDate));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return dft.format(calendar.getTime());
	}

	/**
	 * 获取任意时间的月第一天 描述:<描述函数实现的功能>.
	 * 
	 * @param repeatDate
	 * @return
	 */
	private static String getMinMonthDate(String repeatDate) {
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		try {
			if (StringUtils.isNotBlank(repeatDate) && !"null".equals(repeatDate)) {
				calendar.setTime(dft.parse(repeatDate));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return dft.format(calendar.getTime());
	}

	/**
	 * 不论是当前时间，还是历史时间 皆是时间点的前天 repeatDate 任意时间
	 */
	public static String getModify2DaysAgo(String repeatDate) {
		Calendar cal = Calendar.getInstance();
		String daysAgo = "";
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		if (repeatDate == null || "".equals(repeatDate)) {
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 2);

		} else {
			int year = Integer.parseInt(repeatDate.substring(0, 4));
			String monthsString = repeatDate.substring(4, 6);
			int month;
			if ("0".equals(monthsString.substring(0, 1))) {
				month = Integer.parseInt(monthsString.substring(1, 2));
			} else {
				month = Integer.parseInt(monthsString.substring(0, 2));
			}
			String dateString = repeatDate.substring(6, 8);
			int date;
			if ("0".equals(dateString.subSequence(0, 1))) {
				date = Integer.parseInt(dateString.substring(1, 2));
			} else {
				date = Integer.parseInt(dateString.substring(0, 2));
			}
			cal.set(year, month - 1, date - 1);
			System.out.println(dft.format(cal.getTime()));
		}
		daysAgo = dft.format(cal.getTime());
		return daysAgo;
	}

	/**
	 * 不论是当前时间，还是历史时间 皆是时间点的T-N天 repeatDate 任意时间 param 数字 可以表示前几天
	 */
	public static String getModifyNumDaysAgo(String repeatDate, int param) {
		Calendar cal = Calendar.getInstance();
		String daysAgo = "";
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		if (repeatDate == null || "".equals(repeatDate)) {
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - param);

		} else {
			int year = Integer.parseInt(repeatDate.substring(0, 4));
			String monthsString = repeatDate.substring(4, 6);
			int month;
			if ("0".equals(monthsString.substring(0, 1))) {
				month = Integer.parseInt(monthsString.substring(1, 2));
			} else {
				month = Integer.parseInt(monthsString.substring(0, 2));
			}
			String dateString = repeatDate.substring(6, 8);
			int date;
			if ("0".equals(dateString.subSequence(0, 1))) {
				date = Integer.parseInt(dateString.substring(1, 2));
			} else {
				date = Integer.parseInt(dateString.substring(0, 2));
			}
			cal.set(year, month - 1, date - param + 1);
			System.out.println(dft.format(cal.getTime()));
		}
		daysAgo = dft.format(cal.getTime());
		return daysAgo;
	}

	/**
	 * @author LuoJinShui 将传入时间与当前时间进行对比,是否今天\昨天\同一年
	 */
	public static String getTime(Date date) {
		boolean sameYear = false;
		String todySDF = "HH:mm";
		String todyDay = "今天";
		String yesterDaySDF = "昨天";
		String otherSDF = "MM-dd HH:mm";
		String otherYearSDF = "yyyy-MM-dd HH:mm";
		SimpleDateFormat sfd = null;
		String time = "";
		Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);
		Date now = new Date();
		Calendar todayCalendar = Calendar.getInstance();
		todayCalendar.setTime(now);
		todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
		todayCalendar.set(Calendar.MINUTE, 0);

		if (dateCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
			sameYear = true;
		} else {
			sameYear = false;
		}

		if (dateCalendar.after(todayCalendar)) {// 判断是不是今天
			sfd = new SimpleDateFormat(todySDF);
			time = todyDay + " " + sfd.format(date);
			return time;
		} else {
			todayCalendar.add(Calendar.DATE, -1);
			if (dateCalendar.after(todayCalendar)) {// 判断是不是昨天
				sfd = new SimpleDateFormat(todySDF);
				time = yesterDaySDF + " " + sfd.format(date);
				return time;
			}
		}

		if (sameYear) {
			sfd = new SimpleDateFormat(otherSDF);
			time = sfd.format(date);
		} else {
			sfd = new SimpleDateFormat(otherYearSDF);
			time = sfd.format(date);
		}

		return time;
	}

	/**
	 * 根据当前时间返回相对应参数
	 * 
	 * @author LuoJinShui
	 * @param date
	 */
	public static String getFormat(Date date) {

		long delta = new Date().getTime() - date.getTime();

		// 当前时间没有超过一分钟,返回秒数
		if (delta < 1L * ONE_MINUTE) {

			long seconds = toSeconds(delta);

			return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;

		}

		// 当前时间没有超过一小时,返回分钟
		if (delta < 45L * ONE_MINUTE) {

			long minutes = toMinutes(delta);

			return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;

		}

		// 当前时间没有超过24小时,返回几个小时前
		if (delta < 24L * ONE_HOUR) {

			long hours = toHours(delta);

			return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;

		}

		// 当前时间超过24小时,返回昨天
		if (delta < 48L * ONE_HOUR) {

			return "昨天";

		}

		// 当前时间超过48小时,返回几天前
		if (delta < 30L * ONE_DAY) {

			long days = toDays(delta);

			return (days <= 0 ? 1 : days) + ONE_DAY_AGO;

		}

		// 当前时间没有超过12个月,返回几个月前.
		// 否则返回几年前
//		if (delta < 12L * 4L * ONE_WEEK) {
//
//			long months = toMonths(delta);
//
//			return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
//
//		} 
		else {

			long days = toDays(delta);

			return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
		}

	}

	// 秒数
	private static long toSeconds(long date) {

		return date / 1000L;

	}

	// 分钟
	private static long toMinutes(long date) {

		return toSeconds(date) / 60L;

	}

	// 小时
	private static long toHours(long date) {

		return toMinutes(date) / 60L;

	}

	// 天数
	private static long toDays(long date) {

		return toHours(date) / 24L;

	}

	// 月份
//	private static long toMonths(long date) {
//
//		return toDays(date) / 30L;
//
//	}
}
