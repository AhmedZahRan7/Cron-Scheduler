package cron.scheduler.parser;

import cron.scheduler.time.Hour;

public class HoursParser extends FieldParser<Hour> {

    public HoursParser() {
        super(Utilities.HOUR_REGEX, Hour.class);
    }
}
