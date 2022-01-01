package cron.scheduler.time;

public class Day extends TimeUnit {

    public Day(Integer value) throws Exception {
        super(value);
    }

    @Override
    public Integer getMin() {
        return 1;
    }

    @Override
    public Integer getMax() {
        return 31;
    }
}
