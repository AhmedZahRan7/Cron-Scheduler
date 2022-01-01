package cron.scheduler.time;

public abstract class TimeUnit implements Comparable<TimeUnit> {
    private Integer value;

    public TimeUnit(Integer value) throws Exception {
        if (value > getMax() || value < getMin()) throw new Exception("Out of boundaries");
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public abstract Integer getMin();

    public abstract Integer getMax();

    public void increaseVal(Integer value) {
        this.value = ((this.value + value) % (getMax() + 1));
        this.value = Math.max(this.value, getMin());

    }

    public void decreaseVal(Integer value) {
        this.value = ((this.value - value + getMax() + 1) % (getMax() + 1));
    }

    @Override
    public int compareTo(TimeUnit timeUnit) {
        return this.value - timeUnit.getValue();
    }
}
