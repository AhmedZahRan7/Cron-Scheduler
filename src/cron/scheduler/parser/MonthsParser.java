package cron.scheduler.parser;

import cron.scheduler.time.Month;

public class MonthsParser extends FieldParser<Month> {

    public MonthsParser() {
        super(Utilities.MONTH_REGEX, Month.class);
    }
}
