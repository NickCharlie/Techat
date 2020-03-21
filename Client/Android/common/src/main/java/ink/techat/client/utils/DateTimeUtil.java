package ink.techat.client.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 * @author NickCharlie
 */
public class DateTimeUtil {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("MM-dd", Locale.ENGLISH);

    /**
     * 获取简单的时间字符串
     * @param date Date
     * @return String
     */
    public static String getSampleDate(Date date) {
        return FORMAT.format(date);
    }
}
