package cron.scheduler.parser;

import cron.scheduler.time.Day;

public class DaysParser extends FieldParser<Day> {

    public DaysParser() {
        super(Utilities.DAY_REGEX, Day.class);
    }
}
