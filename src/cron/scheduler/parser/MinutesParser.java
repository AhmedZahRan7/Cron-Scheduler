package cron.scheduler.parser;

import cron.scheduler.time.Minute;

public class MinutesParser extends FieldParser<Minute> {

    public MinutesParser() {
        super(Utilities.MINUTE_REGEX, Minute.class);
    }

}
