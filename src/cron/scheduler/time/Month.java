package cron.scheduler.time;

public class Month extends TimeUnit{

    public Month(Integer value) throws Exception {
        super(value);
    }

    @Override
    public Integer getMin() {
        return 1;
    }

    @Override
    public Integer getMax() {
        return 12;
    }
}
