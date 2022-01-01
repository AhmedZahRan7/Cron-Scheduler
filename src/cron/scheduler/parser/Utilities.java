package cron.scheduler.parser;

public class Utilities {
    public static String MINUTE_REGEX = "[1-5]?[0-9]";
    public static String HOUR_REGEX = "(([0-1]?[0-9])|(2[0-3]))";
    public static String DAY_REGEX = "(([1-9])|([1-2][0-9])|(3[0-1]))";
    public static String MONTH_REGEX = "(([1-9])|(1[0-2]))";
}
