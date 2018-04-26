package face.com.zdl.cctools;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 89667 on 2018/3/5.
 * 农历日期工具类
 * <p>
 * <p>
 * 1、不允许复的日期风格。例如：yyyy-MM-dd和yyyy-M-d，表现出的风格是相同的。只有当两个日期风格含有不同的字符时，才会看成是不相同的日期风格。例如：yyyy-MM-dd和yyyy-M-d EEE。当含有重复的日期风格时，可以通过isShowOnly=true来区分，isShowOnly=true表示该风格只是“格式化Date类型的日期”用，而不用作“自动判断String类型的日期”。
 * 2、日期必须含有完整年份信息。例如：MM-dd。没有年份的话，判断MM-dd是不准确的，因为无法识别出闰年（2-29）。其实MM-dd等类似的风格，我们日常习惯上，将其看作是“今年的M月d日”，而SimpleDateFormat中的parse方法中默认的年份为1970年。
 * 3、添加顺序为：由简到繁。目的在于2012-12和2012-12-1是等价的，虽然日期风格不一样，但默认会看成是一样的且以DateStyle匹配到的最后一个为主。因此最好将详细的日期风格写在后面。
 */

public class DateUtil2 {

    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();

    private static final Object object = new Object();

    /**
     * 获取SimpleDateFormat
     *
     * @param pattern 日期格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    private static SimpleDateFormat getDateFormat(String pattern) throws RuntimeException {
        SimpleDateFormat dateFormat = threadLocal.get();
        if (dateFormat == null) {
            synchronized (object) {
                if (dateFormat == null) {
                    dateFormat = new SimpleDateFormat(pattern);
                    dateFormat.setLenient(false);
                    threadLocal.set(dateFormat);
                }
            }
        }
        dateFormat.applyPattern(pattern);
        return dateFormat;
    }

    /**
     * 获取日期中的某数值。如获取月份
     *
     * @param date     日期
     * @param dateType 日期格式
     * @return 数值
     */
    private static int getInteger(Date date, int dateType) {
        int num = 0;
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
            num = calendar.get(dateType);
        }
        return num;
    }

    /**
     * 增加日期中某类型的某数值。如增加日期
     *
     * @param date     日期字符串
     * @param dateType 类型
     * @param amount   数值
     * @return 计算后日期字符串
     */
    private static String addInteger(String date, int dateType, int amount) {
        String dateString = null;
        DateStyle dateStyle = getDateStyle(date);
        if (dateStyle != null) {
            Date myDate = StringToDate(date, dateStyle);
            myDate = addInteger(myDate, dateType, amount);
            dateString = DateToString(myDate, dateStyle);
        }
        return dateString;
    }

    /**
     * 增加日期中某类型的某数值。如增加日期
     *
     * @param date     日期
     * @param dateType 类型
     * @param amount   数值
     * @return 计算后日期
     */
    private static Date addInteger(Date date, int dateType, int amount) {
        Date myDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(dateType, amount);
            myDate = calendar.getTime();
        }
        return myDate;
    }

    /**
     * 获取精确的日期
     *
     * @param timestamps 时间long集合
     * @return 日期
     */
    private static Date getAccurateDate(List<Long> timestamps) {
        Date date = null;
        long timestamp = 0;
        Map<Long, long[]> map = new HashMap<Long, long[]>();
        List<Long> absoluteValues = new ArrayList<Long>();

        if (timestamps != null && timestamps.size() > 0) {
            if (timestamps.size() > 1) {
                for (int i = 0; i < timestamps.size(); i++) {
                    for (int j = i + 1; j < timestamps.size(); j++) {
                        long absoluteValue = Math.abs(timestamps.get(i) - timestamps.get(j));
                        absoluteValues.add(absoluteValue);
                        long[] timestampTmp = {timestamps.get(i), timestamps.get(j)};
                        map.put(absoluteValue, timestampTmp);
                    }
                }

                // 有可能有相等的情况。如2012-11和2012-11-01。时间戳是相等的。此时minAbsoluteValue为0
                // 因此不能将minAbsoluteValue取默认值0
                long minAbsoluteValue = -1;
                if (!absoluteValues.isEmpty()) {
                    minAbsoluteValue = absoluteValues.get(0);
                    for (int i = 1; i < absoluteValues.size(); i++) {
                        if (minAbsoluteValue > absoluteValues.get(i)) {
                            minAbsoluteValue = absoluteValues.get(i);
                        }
                    }
                }

                if (minAbsoluteValue != -1) {
                    long[] timestampsLastTmp = map.get(minAbsoluteValue);

                    long dateOne = timestampsLastTmp[0];
                    long dateTwo = timestampsLastTmp[1];
                    if (absoluteValues.size() > 1) {
                        timestamp = Math.abs(dateOne) > Math.abs(dateTwo) ? dateOne : dateTwo;
                    }
                }
            } else {
                timestamp = timestamps.get(0);
            }
        }

        if (timestamp != 0) {
            date = new Date(timestamp);
        }
        return date;
    }

    /**
     * 判断字符串是否为日期字符串
     *
     * @param date 日期字符串
     * @return true or false
     */
    public static boolean isDate(String date) {
        boolean isDate = false;
        if (date != null) {
            if (getDateStyle(date) != null) {
                isDate = true;
            }
        }
        return isDate;
    }

    /**
     * 获取日期字符串的日期风格。失敗返回null。
     *
     * @param date 日期字符串
     * @return 日期风格
     */
    public static DateStyle getDateStyle(String date) {
        DateStyle dateStyle = null;
        Map<Long, DateStyle> map = new HashMap<Long, DateStyle>();
        List<Long> timestamps = new ArrayList<Long>();
        for (DateStyle style : DateStyle.values()) {
            if (style.isShowOnly()) {
                continue;
            }
            Date dateTmp = null;
            if (date != null) {
                try {
                    ParsePosition pos = new ParsePosition(0);
                    dateTmp = getDateFormat(style.getValue()).parse(date, pos);
                    if (pos.getIndex() != date.length()) {
                        dateTmp = null;
                    }
                } catch (Exception e) {
                }
            }
            if (dateTmp != null) {
                timestamps.add(dateTmp.getTime());
                map.put(dateTmp.getTime(), style);
            }
        }
        Date accurateDate = getAccurateDate(timestamps);
        if (accurateDate != null) {
            dateStyle = map.get(accurateDate.getTime());
        }
        return dateStyle;
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date 日期字符串
     * @return 日期
     */
    public static Date StringToDate(String date) {
        DateStyle dateStyle = getDateStyle(date);
        return StringToDate(date, dateStyle);
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date    日期字符串
     * @param pattern 日期格式
     * @return 日期
     */
    public static Date StringToDate(String date, String pattern) {
        Date myDate = null;
        if (date != null) {
            try {
                myDate = getDateFormat(pattern).parse(date);
            } catch (Exception e) {
            }
        }
        return myDate;
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date      日期字符串
     * @param dateStyle 日期风格
     * @return 日期
     */
    public static Date StringToDate(String date, DateStyle dateStyle) {
        Date myDate = null;
        if (dateStyle != null) {
            myDate = StringToDate(date, dateStyle.getValue());
        }
        return myDate;
    }

    /**
     * 将日期转化为日期字符串。失败返回null。
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return 日期字符串
     */
    public static String DateToString(Date date, String pattern) {
        String dateString = null;
        if (date != null) {
            try {
                dateString = getDateFormat(pattern).format(date);
            } catch (Exception e) {
            }
        }
        return dateString;
    }

    /**
     * 将日期转化为日期字符串。失败返回null。
     *
     * @param date      日期
     * @param dateStyle 日期风格
     * @return 日期字符串
     */
    public static String DateToString(Date date, DateStyle dateStyle) {
        String dateString = null;
        if (dateStyle != null) {
            dateString = DateToString(date, dateStyle.getValue());
        }
        return dateString;
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     *
     * @param date       旧日期字符串
     * @param newPattern 新日期格式
     * @return 新日期字符串
     */
    public static String StringToString(String date, String newPattern) {
        DateStyle oldDateStyle = getDateStyle(date);
        return StringToString(date, oldDateStyle, newPattern);
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     *
     * @param date         旧日期字符串
     * @param newDateStyle 新日期风格
     * @return 新日期字符串
     */
    public static String StringToString(String date, DateStyle newDateStyle) {
        DateStyle oldDateStyle = getDateStyle(date);
        return StringToString(date, oldDateStyle, newDateStyle);
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     *
     * @param date        旧日期字符串
     * @param olddPattern 旧日期格式
     * @param newPattern  新日期格式
     * @return 新日期字符串
     */
    public static String StringToString(String date, String olddPattern, String newPattern) {
        return DateToString(StringToDate(date, olddPattern), newPattern);
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     *
     * @param date         旧日期字符串
     * @param olddDteStyle 旧日期风格
     * @param newParttern  新日期格式
     * @return 新日期字符串
     */
    public static String StringToString(String date, DateStyle olddDteStyle, String newParttern) {
        String dateString = null;
        if (olddDteStyle != null) {
            dateString = StringToString(date, olddDteStyle.getValue(), newParttern);
        }
        return dateString;
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     *
     * @param date         旧日期字符串
     * @param olddPattern  旧日期格式
     * @param newDateStyle 新日期风格
     * @return 新日期字符串
     */
    public static String StringToString(String date, String olddPattern, DateStyle newDateStyle) {
        String dateString = null;
        if (newDateStyle != null) {
            dateString = StringToString(date, olddPattern, newDateStyle.getValue());
        }
        return dateString;
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     *
     * @param date         旧日期字符串
     * @param olddDteStyle 旧日期风格
     * @param newDateStyle 新日期风格
     * @return 新日期字符串
     */
    public static String StringToString(String date, DateStyle olddDteStyle, DateStyle newDateStyle) {
        String dateString = null;
        if (olddDteStyle != null && newDateStyle != null) {
            dateString = StringToString(date, olddDteStyle.getValue(), newDateStyle.getValue());
        }
        return dateString;
    }

    /**
     * 增加日期的年份。失败返回null。
     *
     * @param date       日期
     * @param yearAmount 增加数量。可为负数
     * @return 增加年份后的日期字符串
     */
    public static String addYear(String date, int yearAmount) {
        return addInteger(date, Calendar.YEAR, yearAmount);
    }

    /**
     * 增加日期的年份。失败返回null。
     *
     * @param date       日期
     * @param yearAmount 增加数量。可为负数
     * @return 增加年份后的日期
     */
    public static Date addYear(Date date, int yearAmount) {
        return addInteger(date, Calendar.YEAR, yearAmount);
    }

    /**
     * 增加日期的月份。失败返回null。
     *
     * @param date        日期
     * @param monthAmount 增加数量。可为负数
     * @return 增加月份后的日期字符串
     */
    public static String addMonth(String date, int monthAmount) {
        return addInteger(date, Calendar.MONTH, monthAmount);
    }

    /**
     * 增加日期的月份。失败返回null。
     *
     * @param date        日期
     * @param monthAmount 增加数量。可为负数
     * @return 增加月份后的日期
     */
    public static Date addMonth(Date date, int monthAmount) {
        return addInteger(date, Calendar.MONTH, monthAmount);
    }

    /**
     * 增加日期的天数。失败返回null。
     *
     * @param date      日期字符串
     * @param dayAmount 增加数量。可为负数
     * @return 增加天数后的日期字符串
     */
    public static String addDay(String date, int dayAmount) {
        return addInteger(date, Calendar.DATE, dayAmount);
    }

    /**
     * 增加日期的天数。失败返回null。
     *
     * @param date      日期
     * @param dayAmount 增加数量。可为负数
     * @return 增加天数后的日期
     */
    public static Date addDay(Date date, int dayAmount) {
        return addInteger(date, Calendar.DATE, dayAmount);
    }

    /**
     * 增加日期的小时。失败返回null。
     *
     * @param date       日期字符串
     * @param hourAmount 增加数量。可为负数
     * @return 增加小时后的日期字符串
     */
    public static String addHour(String date, int hourAmount) {
        return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
    }

    /**
     * 增加日期的小时。失败返回null。
     *
     * @param date       日期
     * @param hourAmount 增加数量。可为负数
     * @return 增加小时后的日期
     */
    public static Date addHour(Date date, int hourAmount) {
        return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
    }

    /**
     * 增加日期的分钟。失败返回null。
     *
     * @param date         日期字符串
     * @param minuteAmount 增加数量。可为负数
     * @return 增加分钟后的日期字符串
     */
    public static String addMinute(String date, int minuteAmount) {
        return addInteger(date, Calendar.MINUTE, minuteAmount);
    }

    /**
     * 增加日期的分钟。失败返回null。
     *
     * @param date         日期
     * @param minuteAmount 增加数量。可为负数
     * @return 增加分钟后的日期
     */
    public static Date addMinute(Date date, int minuteAmount) {
        return addInteger(date, Calendar.MINUTE, minuteAmount);
    }

    /**
     * 增加日期的秒钟。失败返回null。
     *
     * @param date         日期字符串
     * @param secondAmount 增加数量。可为负数
     * @return 增加秒钟后的日期字符串
     */
    public static String addSecond(String date, int secondAmount) {
        return addInteger(date, Calendar.SECOND, secondAmount);
    }

    /**
     * 增加日期的秒钟。失败返回null。
     *
     * @param date         日期
     * @param secondAmount 增加数量。可为负数
     * @return 增加秒钟后的日期
     */
    public static Date addSecond(Date date, int secondAmount) {
        return addInteger(date, Calendar.SECOND, secondAmount);
    }

    /**
     * 获取日期的年份。失败返回0。
     *
     * @param date 日期字符串
     * @return 年份
     */
    public static int getYear(String date) {
        return getYear(StringToDate(date));
    }

    /**
     * 获取日期的年份。失败返回0。
     *
     * @param date 日期
     * @return 年份
     */
    public static int getYear(Date date) {
        return getInteger(date, Calendar.YEAR);
    }

    /**
     * 获取日期的月份。失败返回0。
     *
     * @param date 日期字符串
     * @return 月份
     */
    public static int getMonth(String date) {
        return getMonth(StringToDate(date));
    }

    /**
     * 获取日期的月份。失败返回0。
     *
     * @param date 日期
     * @return 月份
     */
    public static int getMonth(Date date) {
        return getInteger(date, Calendar.MONTH) + 1;
    }

    /**
     * 获取日期的天数。失败返回0。
     *
     * @param date 日期字符串
     * @return 天
     */
    public static int getDay(String date) {
        return getDay(StringToDate(date));
    }

    /**
     * 获取日期的天数。失败返回0。
     *
     * @param date 日期
     * @return 天
     */
    public static int getDay(Date date) {
        return getInteger(date, Calendar.DATE);
    }

    /**
     * 获取日期的小时。失败返回0。
     *
     * @param date 日期字符串
     * @return 小时
     */
    public static int getHour(String date) {
        return getHour(StringToDate(date));
    }

    /**
     * 获取日期的小时。失败返回0。
     *
     * @param date 日期
     * @return 小时
     */
    public static int getHour(Date date) {
        return getInteger(date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取日期的分钟。失败返回0。
     *
     * @param date 日期字符串
     * @return 分钟
     */
    public static int getMinute(String date) {
        return getMinute(StringToDate(date));
    }

    /**
     * 获取日期的分钟。失败返回0。
     *
     * @param date 日期
     * @return 分钟
     */
    public static int getMinute(Date date) {
        return getInteger(date, Calendar.MINUTE);
    }

    /**
     * 获取日期的秒钟。失败返回0。
     *
     * @param date 日期字符串
     * @return 秒钟
     */
    public static int getSecond(String date) {
        return getSecond(StringToDate(date));
    }

    /**
     * 获取日期的秒钟。失败返回0。
     *
     * @param date 日期
     * @return 秒钟
     */
    public static int getSecond(Date date) {
        return getInteger(date, Calendar.SECOND);
    }

    /**
     * 获取日期 。默认yyyy-MM-dd格式。失败返回null。
     *
     * @param date 日期字符串
     * @return 日期
     */
    public static String getDate(String date) {
        return StringToString(date, DateStyle.YYYY_MM_DD);
    }

    /**
     * 获取日期。默认yyyy-MM-dd格式。失败返回null。
     *
     * @param date 日期
     * @return 日期
     */
    public static String getDate(Date date) {
        return DateToString(date, DateStyle.YYYY_MM_DD);
    }

    /**
     * 获取日期的时间。默认HH:mm:ss格式。失败返回null。
     *
     * @param date 日期字符串
     * @return 时间
     */
    public static String getTime(String date) {
        return StringToString(date, DateStyle.HH_MM_SS);
    }

    /**
     * 获取日期的时间。默认HH:mm:ss格式。失败返回null。
     *
     * @param date 日期
     * @return 时间
     */
    public static String getTime(Date date) {
        return DateToString(date, DateStyle.HH_MM_SS);
    }

    /**
     * 获取日期的星期。失败返回null。
     *
     * @param date 日期字符串
     * @return 星期
     */
    public static Week getWeek(String date) {
        Week week = null;
        DateStyle dateStyle = getDateStyle(date);
        if (dateStyle != null) {
            Date myDate = StringToDate(date, dateStyle);
            week = getWeek(myDate);
        }
        return week;
    }

    /**
     * 获取日期的星期。失败返回null。
     *
     * @param date 日期
     * @return 星期
     */
    public static Week getWeek(Date date) {
        Week week = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekNumber = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (weekNumber) {
            case 0:
                week = Week.SUNDAY;
                break;
            case 1:
                week = Week.MONDAY;
                break;
            case 2:
                week = Week.TUESDAY;
                break;
            case 3:
                week = Week.WEDNESDAY;
                break;
            case 4:
                week = Week.THURSDAY;
                break;
            case 5:
                week = Week.FRIDAY;
                break;
            case 6:
                week = Week.SATURDAY;
                break;
        }
        return week;
    }

    /**
     * 获取两个日期相差的天数
     *
     * @param date      日期字符串
     * @param otherDate 另一个日期字符串
     * @return 相差天数。如果失败则返回-1
     */
    public static int getIntervalDays(String date, String otherDate) {
        return getIntervalDays(StringToDate(date), StringToDate(otherDate));
    }

    /**
     * @param date      日期
     * @param otherDate 另一个日期
     * @return 相差天数。如果失败则返回-1
     */
    public static int getIntervalDays(Date date, Date otherDate) {
        int num = -1;
        Date dateTmp = DateUtil2.StringToDate(DateUtil2.getDate(date), DateStyle.YYYY_MM_DD);
        Date otherDateTmp = DateUtil2.StringToDate(DateUtil2.getDate(otherDate), DateStyle.YYYY_MM_DD);
        if (dateTmp != null && otherDateTmp != null) {
            long time = Math.abs(dateTmp.getTime() - otherDateTmp.getTime());
            num = (int) (time / (24 * 60 * 60 * 1000));
        }
        return num;
    }


    /**
     * 获取简单农历对象
     *
     * @param date 日期字符串
     * @return 简单农历对象
     */
    public static SimpleLunarCalendar getSimpleLunarCalendar(String date) {
        return new SimpleLunarCalendar(DateUtil2.StringToDate(date));
    }

    /**
     * 获取简单农历对象
     *
     * @param date 日期
     * @return 简单农历对象
     */
    public static SimpleLunarCalendar getSimpleLunarCalendar(Date date) {
        return new SimpleLunarCalendar(date);
    }


//#######################      DateStyle类         ################################


//#######################           Week类               #############################


    enum Week {

        MONDAY("星期一", "Monday", "Mon.", 1),
        TUESDAY("星期二", "Tuesday", "Tues.", 2),
        WEDNESDAY("星期三", "Wednesday", "Wed.", 3),
        THURSDAY("星期四", "Thursday", "Thur.", 4),
        FRIDAY("星期五", "Friday", "Fri.", 5),
        SATURDAY("星期六", "Saturday", "Sat.", 6),
        SUNDAY("星期日", "Sunday", "Sun.", 7);

        String name_cn;
        String name_en;
        String name_enShort;
        int number;

        Week(String name_cn, String name_en, String name_enShort, int number) {
            this.name_cn = name_cn;
            this.name_en = name_en;
            this.name_enShort = name_enShort;
            this.number = number;
        }

        public String getChineseName() {
            return name_cn;
        }

        public String getName() {
            return name_en;
        }

        public String getShortName() {
            return name_enShort;
        }

        public int getNumber() {
            return number;
        }
    }


//#######################        SimpleLunarCalendar       ###############################

//        适用的年份为：1900年——2099年，我去掉了不精准的部分（如天干地支），目前该简单农历只能满足显示阴历信息，适合需要较为简单的用户适用。另外若想支持的年限范围更广，则需要修改lunarInfo（农历年数据表），并做适当的调整（默认农历年）即可。


    public static class SimpleLunarCalendar {

        /**
         * 最小时间1900-1-31
         */
        private final static long minTimeInMillis = -2206425952001L;
        /**
         * 最大时间2099-12-31
         */
        private final static long maxTimeInMillis = 4102416000000L;
        /**
         * 农历年数据表(1900-2099年)<br>
         * <br>
         * 每个农历年用16进制来表示，解析时转为2进制<br>
         * 前12位分别表示12个农历月份的大小月，1是大月，0是小月<br>
         * 最后4位表示闰月，转为十进制后即为闰月值，例如0110，则为闰6月
         */
        private final static int[] lunarInfo = {0x4bd8, 0x4ae0, 0xa570, 0x54d5, 0xd260, 0xd950, 0x5554, 0x56af, 0x9ad0, 0x55d2, 0x4ae0, 0xa5b6, 0xa4d0, 0xd250, 0xd295, 0xb54f, 0xd6a0, 0xada2, 0x95b0,
                0x4977, 0x497f, 0xa4b0, 0xb4b5, 0x6a50, 0x6d40, 0xab54, 0x2b6f, 0x9570, 0x52f2, 0x4970, 0x6566, 0xd4a0, 0xea50, 0x6a95, 0x5adf, 0x2b60, 0x86e3, 0x92ef, 0xc8d7, 0xc95f, 0xd4a0, 0xd8a6,
                0xb55f, 0x56a0, 0xa5b4, 0x25df, 0x92d0, 0xd2b2, 0xa950, 0xb557, 0x6ca0, 0xb550, 0x5355, 0x4daf, 0xa5b0, 0x4573, 0x52bf, 0xa9a8, 0xe950, 0x6aa0, 0xaea6, 0xab50, 0x4b60, 0xaae4, 0xa570,
                0x5260, 0xf263, 0xd950, 0x5b57, 0x56a0, 0x96d0, 0x4dd5, 0x4ad0, 0xa4d0, 0xd4d4, 0xd250, 0xd558, 0xb540, 0xb6a0, 0x95a6, 0x95bf, 0x49b0, 0xa974, 0xa4b0, 0xb27a, 0x6a50, 0x6d40, 0xaf46,
                0xab60, 0x9570, 0x4af5, 0x4970, 0x64b0, 0x74a3, 0xea50, 0x6b58, 0x5ac0, 0xab60, 0x96d5, 0x92e0, 0xc960, 0xd954, 0xd4a0, 0xda50, 0x7552, 0x56a0, 0xabb7, 0x25d0, 0x92d0, 0xcab5, 0xa950,
                0xb4a0, 0xbaa4, 0xad50, 0x55d9, 0x4ba0, 0xa5b0, 0x5176, 0x52bf, 0xa930, 0x7954, 0x6aa0, 0xad50, 0x5b52, 0x4b60, 0xa6e6, 0xa4e0, 0xd260, 0xea65, 0xd530, 0x5aa0, 0x76a3, 0x96d0, 0x4afb,
                0x4ad0, 0xa4d0, 0xd0b6, 0xd25f, 0xd520, 0xdd45, 0xb5a0, 0x56d0, 0x55b2, 0x49b0, 0xa577, 0xa4b0, 0xaa50, 0xb255, 0x6d2f, 0xada0, 0x4b63, 0x937f, 0x49f8, 0x4970, 0x64b0, 0x68a6, 0xea5f,
                0x6b20, 0xa6c4, 0xaaef, 0x92e0, 0xd2e3, 0xc960, 0xd557, 0xd4a0, 0xda50, 0x5d55, 0x56a0, 0xa6d0, 0x55d4, 0x52d0, 0xa9b8, 0xa950, 0xb4a0, 0xb6a6, 0xad50, 0x55a0, 0xaba4, 0xa5b0, 0x52b0,
                0xb273, 0x6930, 0x7337, 0x6aa0, 0xad50, 0x4b55, 0x4b6f, 0xa570, 0x54e4, 0xd260, 0xe968, 0xd520, 0xdaa0, 0x6aa6, 0x56df, 0x4ae0, 0xa9d4, 0xa4d0, 0xd150, 0xf252, 0xd520};
        /**
         * 十二生肖
         */
        private final static String[] Animals = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
        /**
         * 农历中文字符串一
         */
        private final static String[] lunarString1 = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        /**
         * 农历中文字符串二
         */
        private final static String[] lunarString2 = {"初", "十", "廿", "卅", "正", "腊", "冬", "闰"};
        /**
         * 农历年
         */
        private int lunarYear;
        /**
         * 农历月
         */
        private int lunarMonth;
        /**
         * 农历日
         */
        private int lunarDay;
        /**
         * 是否是闰月
         */
        private boolean isLeap;
        /**
         * 是否是闰年
         */
        private boolean isLeapYear;
        /**
         * 某农历月的最大天数
         */
        private int maxDayInMonth = 29;

        /**
         * 通过 TimeInMillis 构建农历信息
         *
         * @param TimeInMillis
         */
        public SimpleLunarCalendar(long TimeInMillis) {
            this.init(TimeInMillis);
        }

        /**
         * 通过 Date 对象构建农历信息
         *
         * @param date 指定日期对象
         */
        public SimpleLunarCalendar(Date date) {
            if (date == null)
                date = new Date();
            this.init(date.getTime());
        }

        /**
         * 农历初始化
         *
         * @param timeInMillis 时间毫秒数
         */
        private void init(long timeInMillis) {
            if (timeInMillis > minTimeInMillis && timeInMillis < maxTimeInMillis) {
                // 以农历为1900年正月一日的1900-1-31作为起始日期
                Calendar baseDate = new GregorianCalendar(1900, 0, 31);
                // 距离起始日期间隔的总天数
                long offset = (timeInMillis - baseDate.getTimeInMillis()) / 86400000;
                // 默认农历年为1900年，且由此开始推算农历年份
                this.lunarYear = 1900;
                int daysInLunarYear = SimpleLunarCalendar.getLunarYearDays(this.lunarYear);
                // 递减每个农历年的总天数，确定农历年份
                while (this.lunarYear < 2100 && offset >= daysInLunarYear) {
                    offset -= daysInLunarYear;
                    daysInLunarYear = SimpleLunarCalendar.getLunarYearDays(++this.lunarYear);
                }
                // 获取该农历年的闰月月份
                int leapMonth = SimpleLunarCalendar.getLunarLeapMonth(this.lunarYear);
                // 没有闰月则不是闰年
                this.isLeapYear = leapMonth > 0;

                // 默认农历月为正月，且由此开始推荐农历月
                int lunarMonth = 1;
                // 是否递减农历月
                boolean isDecrease = true;
                boolean isLeap = false;
                int daysInLunarMonth = 0;
                // 递减每个农历月的总天数，确定农历月份
                while (lunarMonth < 13 && offset > 0) {
                    if (isLeap && !isDecrease) {
                        // 该农历年闰月的总天数
                        daysInLunarMonth = SimpleLunarCalendar.getLunarLeapDays(this.lunarYear);
                        isDecrease = true;
                    } else {
                        // 该农历年正常农历月份的天数
                        daysInLunarMonth = SimpleLunarCalendar.getLunarMonthDays(this.lunarYear, lunarMonth);
                    }
                    if (offset < daysInLunarMonth) {
                        break;
                    }
                    offset -= daysInLunarMonth;

                    // 如果农历月是闰月，则不递增农历月份
                    if (leapMonth == lunarMonth && isLeap == false) {
                        isDecrease = false;
                        isLeap = true;
                    } else {
                        lunarMonth++;
                    }
                }
                // 如果daysInLunarMonth为0则说明默认农历月即为返回的农历月
                this.maxDayInMonth = daysInLunarMonth != 0 ? daysInLunarMonth : SimpleLunarCalendar.getLunarMonthDays(this.lunarYear, lunarMonth);
                this.lunarMonth = lunarMonth;
                this.isLeap = (lunarMonth == leapMonth && isLeap);
                this.lunarDay = (int) offset + 1;
            }
        }

        /**
         * 获取某农历年的总天数
         *
         * @param lunarYear 农历年份
         * @return 该农历年的总天数
         */
        private static int getLunarYearDays(int lunarYear) {
            // 按小月计算,农历年最少有12 * 29 = 348天
            int daysInLunarYear = 348;

            // 遍历前12位
            for (int i = 0x8000; i > 0x8; i >>= 1) {
                // 每个大月累加一天
                daysInLunarYear += ((SimpleLunarCalendar.lunarInfo[lunarYear - 1900] & i) != 0) ? 1 : 0;
            }
            // 加上闰月天数
            daysInLunarYear += SimpleLunarCalendar.getLunarLeapDays(lunarYear);

            return daysInLunarYear;
        }

        /**
         * 获取某农历年闰月的总天数
         *
         * @param lunarYear 农历年份
         * @return 该农历年闰月的总天数，没有闰月返回0
         */
        private static int getLunarLeapDays(int lunarYear) {
            // 下一年最后4bit为1111,返回30(大月)
            // 下一年最后4bit不为1111,返回29(小月)
            // 若该年没有闰月,返回0
            return SimpleLunarCalendar.getLunarLeapMonth(lunarYear) > 0 ? ((SimpleLunarCalendar.lunarInfo[lunarYear - 1899] & 0xf) == 0xf ? 30 : 29) : 0;
        }

        /**
         * 获取某农历年闰月月份
         *
         * @param lunarYear 农历年份
         * @return 该农历年闰月的月份，没有闰月返回0
         */
        private static int getLunarLeapMonth(int lunarYear) {
            // 匹配后4位
            int leapMonth = SimpleLunarCalendar.lunarInfo[lunarYear - 1900] & 0xf;
            // 若最后4位全为1或全为0,表示没闰
            leapMonth = (leapMonth == 0xf ? 0 : leapMonth);
            return leapMonth;
        }

        /**
         * 获取某农历年某农历月份的总天数
         *
         * @param lunarYear  农历年份
         * @param lunarMonth 农历月份
         * @return 该农历年该农历月的总天数
         */
        private static int getLunarMonthDays(int lunarYear, int lunarMonth) {
            // 匹配前12位代表的相应农历月份的大小月，大月30天，小月29天
            int daysInLunarMonth = ((SimpleLunarCalendar.lunarInfo[lunarYear - 1900] & (0x10000 >> lunarMonth)) != 0) ? 30 : 29;
            return daysInLunarMonth;
        }

        /**
         * 返回指定数字的农历年份表示字符串
         *
         * @param lunarYear 农历年份(数字,0为甲子)
         * @return 农历年份字符串
         */
        private static String getLunarYearString(int lunarYear) {
            String lunarYearString = "";
            String year = String.valueOf(lunarYear);
            for (int i = 0; i < year.length(); i++) {
                char yearChar = year.charAt(i);
                int index = Integer.parseInt(String.valueOf(yearChar));
                lunarYearString += lunarString1[index];
            }
            return lunarYearString;
        }

        /**
         * 返回指定数字的农历月份表示字符串
         *
         * @param lunarMonth 农历月份(数字)
         * @return 农历月份字符串 (例:正)
         */
        private static String getLunarMonthString(int lunarMonth) {
            String lunarMonthString = "";
            if (lunarMonth == 1) {
                lunarMonthString = SimpleLunarCalendar.lunarString2[4];
            } else {
                if (lunarMonth > 9)
                    lunarMonthString += SimpleLunarCalendar.lunarString2[1];
                if (lunarMonth % 10 > 0)
                    lunarMonthString += SimpleLunarCalendar.lunarString1[lunarMonth % 10];
            }
            return lunarMonthString;
        }

        /**
         * 返回指定数字的农历日表示字符串
         *
         * @param lunarDay 农历日(数字)
         * @return 农历日字符串 (例: 廿一)
         */
        private static String getLunarDayString(int lunarDay) {
            if (lunarDay < 1 || lunarDay > 30)
                return "";
            int i1 = lunarDay / 10;
            int i2 = lunarDay % 10;
            String c1 = SimpleLunarCalendar.lunarString2[i1];
            String c2 = SimpleLunarCalendar.lunarString1[i2];
            if (lunarDay < 11)
                c1 = SimpleLunarCalendar.lunarString2[0];
            if (i2 == 0)
                c2 = SimpleLunarCalendar.lunarString2[1];
            return c1 + c2;
        }

        /**
         * 取农历年生肖
         *
         * @return 农历年生肖(例:龙)
         */
        public String getAnimalString() {
            if (lunarYear == 0)
                return null;
            return SimpleLunarCalendar.Animals[(this.lunarYear - 4) % 12];
        }

        /**
         * 返回农历日期字符串
         *
         * @return 农历日期字符串
         */
        public String getDayString() {
            if (lunarDay == 0)
                return null;
            return SimpleLunarCalendar.getLunarDayString(this.lunarDay);
        }

        /**
         * 返回农历日期字符串
         *
         * @return 农历日期字符串
         */
        public String getMonthString() {
            if (lunarMonth == 0)
                return null;
            return (this.isLeap() ? "闰" : "") + SimpleLunarCalendar.getLunarMonthString(this.lunarMonth);
        }

        /**
         * 返回农历日期字符串
         *
         * @return 农历日期字符串
         */
        public String getYearString() {
            if (lunarYear == 0)
                return null;
            return SimpleLunarCalendar.getLunarYearString(this.lunarYear);
        }

        /**
         * 返回农历表示字符串
         *
         * @return 农历字符串(例:甲子年正月初三)
         */
        public String getDateString() {
            if (lunarYear == 0)
                return null;
            return this.getYearString() + "年" + this.getMonthString() + "月" + this.getDayString() + "日";
        }

        /**
         * 农历年是否是闰月
         *
         * @return 农历年是否是闰月
         */
        public boolean isLeap() {
            return isLeap;
        }

        /**
         * 农历年是否是闰年
         *
         * @return 农历年是否是闰年
         */
        public boolean isLeapYear() {
            return isLeapYear;
        }

        /**
         * 当前农历月是否是大月
         *
         * @return 当前农历月是大月
         */
        public boolean isBigMonth() {
            return this.getMaxDayInMonth() > 29;
        }

        /**
         * 当前农历月有多少天
         *
         * @return 天数
         */
        public int getMaxDayInMonth() {
            if (lunarYear == 0)
                return 0;
            return this.maxDayInMonth;
        }

        /**
         * 农历日期
         *
         * @return 农历日期
         */
        public int getDay() {
            return lunarDay;
        }

        /**
         * 农历月份
         *
         * @return 农历月份
         */
        public int getMonth() {
            return lunarMonth;
        }

        /**
         * 农历年份
         *
         * @return 农历年份
         */
        public int getYear() {
            return lunarYear;
        }
    }


    public enum DateStyle {
        YYYY_MM("yyyy-MM", false),
        YYYY_MM_DD("yyyy-MM-dd", false),
        YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm", false),
        YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss", false),

        YYYY_MM_EN("yyyy/MM", false),
        YYYY_MM_DD_EN("yyyy/MM/dd", false),
        YYYY_MM_DD_HH_MM_EN("yyyy/MM/dd HH:mm", false),
        YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss", false),

        YYYY_MM_CN("yyyy年MM月", false),
        YYYY_MM_DD_CN("yyyy年MM月dd日", false),
        YYYY_MM_DD_HH_MM_CN("yyyy年MM月dd日 HH:mm", false),
        YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss", false),

        HH_MM("HH:mm", true),
        HH_MM_SS("HH:mm:ss", true),

        MM_DD("MM-dd", true),
        MM_DD_HH_MM("MM-dd HH:mm", true),
        MM_DD_HH_MM_SS("MM-dd HH:mm:ss", true),

        MM_DD_EN("MM/dd", true),
        MM_DD_HH_MM_EN("MM/dd HH:mm", true),
        MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss", true),

        MM_DD_CN("MM月dd日", true),
        MM_DD_HH_MM_CN("MM月dd日 HH:mm", true),
        MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss", true);

        private String value;

        private boolean isShowOnly;

        DateStyle(String value, boolean isShowOnly) {
            this.value = value;
            this.isShowOnly = isShowOnly;
        }

        public String getValue() {
            return value;
        }

        public boolean isShowOnly() {
            return isShowOnly;
        }

    }

}

