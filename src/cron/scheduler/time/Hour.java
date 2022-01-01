package cron.scheduler.time;

public class Hour extends TimeUnit {

    public Hour(Integer value) throws Exception {
        super(value);
    }

    @Override
    public Integer getMin() {
        return 0;
    }

    @Override
    public Integer getMax() {
        return 23;
    }
}
