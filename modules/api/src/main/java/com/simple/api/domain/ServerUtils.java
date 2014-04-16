package com.simple.api.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ServerUtils {

    /**
     * Thanks to http://www.mkyong.com/java/how-to-detect-os-in-java-
     * systemgetpropertyosname/
     * 
     * @author chinshaw
     * 
     */
    static class HostType {

        static String os = System.getProperty("os.name").toLowerCase();

        public static boolean isMac() {
            return (os.indexOf("mac") >= 0);
        }

        public static boolean isWindows() {
            return (os.indexOf("win") >= 0);
        }

        public static boolean isUnix() {
            return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
        }

    }

    /**
     * Helper method to generate current time in GMT
     * 
     * @return Date
     * @throws ParseException 
     */
    public static Date getCurrentGmtTime() throws ParseException {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

        // Local time zone
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

        Date gmtTime = null;
        // Time in GMT

        gmtTime = dateFormatLocal.parse(dateFormatGmt.format(new Date()));

        return gmtTime;
    }
}
