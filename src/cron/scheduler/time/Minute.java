package cron.scheduler.time;

public class Minute extends TimeUnit {

    public Minute(Integer value) throws Exception {
        super(value);
    }

    @Override
    public Integer getMin() {
        return 0;
    }

    @Override
    public Integer getMax() {
        return 59;
    }
}
