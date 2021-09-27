package cn.codeben.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xuben
 */
public class DateUtil {

    private static String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static String DATE_FORMAT_PATTERN_SHORT = "yyyy-MM-dd";

    public static String getNowStr(){
        return new SimpleDateFormat(DATE_FORMAT_PATTERN).format(getNowDate());
    }

    public static Date getNowDate(){
        return new Date();
    }
}
