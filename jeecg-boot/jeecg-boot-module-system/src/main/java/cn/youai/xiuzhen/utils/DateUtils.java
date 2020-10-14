package cn.youai.xiuzhen.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.joda.time.Period;
import org.joda.time.*;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期处理
 *
 * @author luopeihuan
 * @date 2018年11月2日 下午14:53:33
 */
public final class DateUtils {

    private static long offsetSeconds = 0;
    // 每天5点设置为游戏开始时间
    private static final int GAME_START_HOUR_OF_DAY = 5;

    private DateUtils() {
    }

    /**
     * 修改服务器时间相对值
     *
     * @param timeOffset 相对时间（秒）
     */
    public static void setOffsetSeconds(long timeOffset) {
        if (timeOffset < 0) {
            throw new RuntimeException("Cannot set timeOffset with negative value:" + timeOffset);
        }
        offsetSeconds = timeOffset;
    }

    public static long getOffsetSeconds() {
        return offsetSeconds;
    }

    public static final long MINUTE_MILLIS = 60L * 1000L;

    public static int toSecond(long time) {
        return (int) (time / 1000L);
    }

    public static Date now() {
        if (offsetSeconds == 0) {
            return new Date();
        }
        return addSeconds(new Date(), (int) offsetSeconds);
    }

    public static long currentTimeMillis() {
        return now().getTime();
    }

    public static LocalDateTime currentDateTime() {
        return LocalDateTime.now().plusSeconds(offsetSeconds);
    }

    public static LocalDate currentDate() {
        return currentDateTime().toLocalDate();
    }

    public static LocalTime currentTime() {
        return currentDateTime().toLocalTime();
    }

    /**
     * 仅保留日期，去掉时分秒毫秒
     */
    public static Date dateOnly(Date date) {
        return new java.sql.Date(startTimeOfDate(date).getTime());
    }

    public static Date todayDate() {
        return new java.sql.Date(startTimeOfDate(now()).getTime());
    }

    public static Date todayGameDate() {
        return gameDate(now());
    }

    public static Date gameDate(Date date) {
        DateTime dateTime = dateTimeOfGameDate(date);
        return dateTime.toDate();
    }

    private static DateTime dateTimeOfGameDate(Date date) {
        DateTime dateTime = new DateTime(date);
        // 每天5点前归为上一天
        if (dateTime.getHourOfDay() < GAME_START_HOUR_OF_DAY) {
            return dateTime.minusDays(1).withTimeAtStartOfDay();
        } else {
            // 5点之后归为新的一天
            return dateTime.withTimeAtStartOfDay();
        }
    }

    public static Date gameStartTimeOfDate(Date date) {
        return dateTimeOfGameDate(date).withTime(GAME_START_HOUR_OF_DAY, 0, 0, 0).toDate();
    }

    public static Date gameEndTimeOfDate(Date date) {
        // 游戏日结束时间：第二天的5点
        return dateTimeOfGameDate(date).withTime(GAME_START_HOUR_OF_DAY, 0, 0, 0)
                .plusDays(1).minusMillis(1).toDate();
    }

    public static Date gameDate(LocalDateTime date) {
        LocalDateTime ldt = localDateTimeOfGameDate(date);
        return toDate(ldt);
    }

    private static LocalDateTime localDateTimeOfGameDate(LocalDateTime date) {
        // 每天5点前归为上一天
        if (date.getHour() < GAME_START_HOUR_OF_DAY) {
            return date.minusDays(1).truncatedTo(ChronoUnit.DAYS);
        } else {
            // 5点之后归为新的一天
            return date.truncatedTo(ChronoUnit.DAYS);
        }
    }

    public static LocalDateTime gameStartTimeOfDate(LocalDateTime date) {
        return localDateTimeOfGameDate(date).with(LocalTime.of(GAME_START_HOUR_OF_DAY, 0, 0, 0));
    }

    public static LocalDateTime gameEndTimeOfDate(LocalDateTime date) {
        // 游戏日结束时间：第二天的5点
        return gameStartTimeOfDate(date).plusDays(1).minusNanos(1);
    }

    public static int dayOfWeak(Date date) {
        return new DateTime(date).getDayOfWeek();
    }

    public static int todayOfWeak() {
        return dayOfWeak(now());
    }

    public static String today() {
        return DateUtil.format(now(), DatePattern.PURE_DATE_PATTERN);
    }

    public static long unixTimestamp(Date date) {
        if (date == null) {
            return 0;
        }
        return date.getTime() / 1000;
    }

    public static long unixTimestamp(long time) {
        return time / 1000;
    }

    public static Date toDate(LocalTime localTime) {
        return toDate(localTime.atDate(currentDate()));
    }

    /**
     * java.time.LocalDateTime --> java.util.Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    // java.util.Date --> java.time.LocalDateTime
    public static LocalDateTime toLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    // java.util.Date --> java.time.LocalDate
    public static LocalDate toLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return instant.atZone(zone).toLocalDate();
    }

    // java.util.Date --> java.time.LocalTime
    public static LocalTime toLocalTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return instant.atZone(zone).toLocalTime();
    }


    /**
     * 获取 UTC 时差
     */
    public static int getUTCHourOffset() {
        return getHourOffset(getTimeZone());
    }

    public static TimeZone getTimeZone() {
        return TimeZone.getTimeZone(ZoneId.systemDefault());
    }

    public static int getHourOffset(TimeZone timeZone) {
        return timeZone.getOffset(System.currentTimeMillis()) / 1000 / 60 / 60;
    }

    /**
     * 计算日期相差天数 end - start， 如果 end < start，返回负数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static int daysBetween(Date start, Date end) {
        return Days.daysBetween(new DateTime(start), new DateTime(end)).getDays();
    }

    /**
     * 计算日期相差天数 end - start， 如果 end < start，返回负数
     * 不足24小时，按1天算
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static int daysBetweenNatural(Date start, Date end) {
        return Days.daysBetween(new DateTime(start).withTimeAtStartOfDay(), new DateTime(end).withTimeAtStartOfDay()).getDays();
    }

    /**
     * 获取小时
     *
     * @param date
     * @return
     */
    public static int hourOfDay(Date date) {
        return new DateTime(date).hourOfDay().get();
    }

    /**
     * 判断是否过期
     *
     * @param createTime 开始日期
     * @param days       有效天数
     * @return 是否已过期
     */
    public static boolean isExpired(Date createTime, int days) {
        Date expiredDate = addDays(createTime, days);
        return expiredDate.before(DateUtils.now());
    }

    /**
     * 剩余天数，不足24小时按1天算
     *
     * @param createTime 开始日期
     * @param days       有效天数
     * @return 剩余天数
     */
    public static int remainDays(Date createTime, int days) {
        return Days.daysBetween(DateTime.now().withTimeAtStartOfDay(), new DateTime(createTime).plusDays(days).withTimeAtStartOfDay()).getDays();
    }

    /**
     * 判断两个日期是是同一天
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static boolean isSameDay(Date start, Date end) {
        return new DateTime(start).withTimeAtStartOfDay().isEqual(new DateTime(end).withTimeAtStartOfDay());
    }

    public static boolean isNotSameDay(Date start, Date end) {
        return !isSameDay(start, end);
    }

    /**
     * 计算日期相差小时数 end - start， 如果 end < start，返回负数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static int hoursBetween(Date start, Date end) {
        return Hours.hoursBetween(new DateTime(start), new DateTime(end)).getHours();
    }

    /**
     * 计算日期相差分钟数 end - start， 如果 end < start，返回负数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static int minutesBetween(Date start, Date end) {
        return Minutes.minutesBetween(new DateTime(start), new DateTime(end)).getMinutes();
    }

    /**
     * 计算日期相差秒数 end - start， 如果 end < start，返回负数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 秒
     */
    public static long secondsBetween(Date start, Date end) {
        return secondsBetween(toLocalDateTime(start), toLocalDateTime(end));
    }

    public static long secondsBetween(Temporal start, Temporal end) {
        return Duration.between(start, end).getSeconds();
    }

    public static long millisecondsBetween(Date start, Date end) {
        return end.getTime() - start.getTime();
    }

    /**
     * 获取时间间隔的描述 xx年xx月xx天xx时xx分xx秒
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static String timeBetween(Date start, Date end) {
        DateTime startDate = new DateTime(start);
        DateTime endDate = new DateTime(end);
        Period period = new Period(startDate, endDate);
        PeriodFormatter formatter = new PeriodFormatterBuilder().appendYears().appendSuffix("年").appendMonths()
                .appendSuffix("月").appendDays().appendSuffix("天").appendHours().appendSuffix("时").appendMinutes()
                .appendSuffix("分").appendSeconds().appendSuffix("秒").toFormatter();
        return formatter.print(period);
    }

    /**
     * 起始时间
     *
     * @param date 日期
     * @return 0点时间
     */
    public static Date startTimeOfDate(Date date) {
        return new DateTime(date).withTimeAtStartOfDay().toDate();
    }

    /**
     * 结束时间
     *
     * @param date 日期
     * @return 结束时间
     */
    public static Date endTimeOfDate(Date date) {
        return new DateTime(date).millisOfDay().withMaximumValue().toDate();
    }

    public static String formatDateTimeStr(Date date) {
        return DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parseDate(String date) {
        return DateUtil.parse(date);
    }

    /**
     * 解析 时间（HH:mm:ss）为日期
     *
     * @param date yyyy-MM-dd 格式的字符串
     * @param time HH:mm:ss 格式的时间
     * @return Date
     */
    public static Date parseOnlyTime(String date, String time) {
        return DateUtils.parseDate(date + " " + time.trim());
    }

    public static String formatDateZhCN(Date date) {
        return DateUtil.format(date, "yyyy年MM月dd日");
    }

    public static String formatDateNumStr(Date date) {
        return DateUtil.format(date, DatePattern.PURE_DATETIME_PATTERN);
    }

    public static String formatDate(Date date, String pattern) {
        return DateUtil.format(date, pattern);
    }

    /**
     * 获取整点时间
     *
     * @param hour
     * @return
     */
    public static long getOfDayIntHourTime(int hour) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * 增加秒数
     *
     * @param date    日期
     * @param seconds 秒数
     * @return
     */
    public static Date addSeconds(Date date, int seconds) {
        return new DateTime(date).plusSeconds(seconds).toDate();
    }

    public static Date addMillSeconds(Date date, int millSeconds) {
        return new DateTime(date).plusMillis(millSeconds).toDate();
    }


    /**
     * 添加天数
     *
     * @param date 日期
     * @param days 天数
     * @return
     */
    public static Date addDays(Date date, int days) {
        return new DateTime(date).plusDays(days).toDate();
    }


    /**
     * 一天的开始和结束
     */
    public static Date[] todayStartAndEnd() {
        return dateStartAndEnd(now());
    }

    public static Date[] dateStartAndEnd(Date date) {
        Date[] dates = new Date[2];
        dates[0] = DateUtils.startTimeOfDate(date);
        dates[1] = DateUtils.endTimeOfDate(date);
        return dates;
    }

    public static Date[] dateStartAndEndOffset5(Date date) {
        return dateStartAndEndOffset(date, 5);
    }

    public static Date[] dateStartAndEndOffset(Date date, int hour) {
        if (null == date || hour <= 0) {
            return todayStartAndEnd();
        }

        LocalDateTime ldt = toLocalDateTime(date);
        if (ldt.getHour() < hour) {
            ldt = ldt.plusDays(-1);
        }

        Date[] dates = new Date[2];
        dates[0] = toDate(ldt.toLocalDate().atTime(hour, 0));
        dates[1] = toDate(ldt.plusDays(1).toLocalDate().atTime(hour, 0).plusNanos(-1));
        return dates;
    }

    /**
     * 下一周的指定周天
     *
     * @param week 周几
     * @return 日期零点
     */
    public static Date startOfWeekWithWeeks(int week) {
        DateTime dateTime = new DateTime(now());
        return dateTime.dayOfWeek().withMinimumValue().plusWeeks(week).withTimeAtStartOfDay().toDate();
    }

    /**
     * 下个月的几号日期时间点
     *
     * @param day 几号
     * @return 日期时间
     */
    public static Date zeroNextMonthFirstDay(int day) {
        DateTime dateTime = new DateTime(now());
        return dateTime.dayOfMonth().withMinimumValue().plusMonths(day).withTimeAtStartOfDay().toDate();
    }

    /**
     * 当天的整点时间
     *
     * @param hour 整点
     * @param plus -1 昨天 0 今天 1 明天
     * @return 整点日期
     */
    public static Date hourToday(int plus, int hour) {
        DateTime currentDateTime = new DateTime(now());
        return currentDateTime.plusDays(plus).withTimeAtStartOfDay().withHourOfDay(hour).toDate();
    }


    public static boolean isTimeBetween(LocalTime startTime, LocalTime endTime) {
        return isTimeBetween(DateUtils.currentTime(), startTime, endTime);
    }

    public static boolean isTimeBetween(LocalTime targetTime, LocalTime startTime, LocalTime endTime) {
        return targetTime.isAfter(startTime) && targetTime.isBefore(endTime);
    }

    public static boolean isDateBetween(Date date, Date start, Date end) {
        return date.after(start) && date.before(end);
    }

    public static long subMillisOfLocalTime(LocalTime targetLocalTime) {
        LocalTime localTime = DateUtils.currentTime();
        LocalDate localDate = DateUtils.currentDate();
        LocalDate targetLocalDate = localTime.isBefore(targetLocalTime) ? localDate : localDate.plusDays(1);
        return Duration.between(localDate.atTime(localTime), targetLocalDate.atTime(targetLocalTime)).toMillis();
    }

    public static void main(String[] args) {
        Date date = DateUtils.parseDate("2020-07-13 03:55:35");
        System.out.println("now:" + date);
        System.out.println("start of day:" + DateUtils.formatDate(startTimeOfDate(date), "yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println("end of day:" + DateUtils.formatDate(endTimeOfDate(date), "yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println("game day:" + DateUtils.formatDate(gameDate(date), "yyyy-MM-dd"));
        System.out.println("game start of day:" + DateUtils.formatDate(gameStartTimeOfDate(date), "yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println("game end of day:" + DateUtils.formatDate(gameEndTimeOfDate(date), "yyyy-MM-dd HH:mm:ss.SSS"));

    }
}
