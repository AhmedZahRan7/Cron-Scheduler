package cron.scheduler.job;

import cron.scheduler.parser.DaysParser;
import cron.scheduler.parser.HoursParser;
import cron.scheduler.parser.MinutesParser;
import cron.scheduler.parser.MonthsParser;
import cron.scheduler.time.Timer;

class CronExpressionParser {

    public static Timer parse(String expression) throws Exception {
        String[] fields = expression.trim().split("\\s");
        if (fields.length < 4) throw new Exception("Invalid number of arguments");
        return new Timer(
                new MinutesParser().getPossibilitiesSet(fields[0]),
                new HoursParser().getPossibilitiesSet(fields[1]),
                new DaysParser().getPossibilitiesSet(fields[2]),
                new MonthsParser().getPossibilitiesSet(fields[3])
        );
    }
}
