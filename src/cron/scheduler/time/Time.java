package cron.scheduler.time;

class Time {
    Month month;
    Day day;
    Hour hour;
    Minute minute;

    public Time(Month month, Day day, Hour hour, Minute minute) {
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * example
     * MM:HH DD/MM
     * next    55:12 12/10
     * current 44:15 15/11
     * result = 11 min + (-3+24) hour + (-4+31) day + 0 month
     * <p>
     * next    33:16 12/8
     * current 44:15 15/9
     * result = (-11+60) min + (0) hour + (-3+31) day + (-2%12 = 10) month
     *
     * @param nextExecution time for command next execution
     * @return remaining minutes to run the command
     */
    public Integer remainingMinutesToReach(Time nextExecution) {
        int minutes = (nextExecution.minute.getValue() - this.minute.getValue());
        if (minutes < 0) {
            minutes += 60;
            nextExecution.hour.decreaseVal(1);
        }
        int hours = (nextExecution.hour.getValue() - this.hour.getValue());
        if (hours < 0) {
            hours += 24;
            nextExecution.day.decreaseVal(1);
        }
        int days = (nextExecution.day.getValue() - this.day.getValue());
        if (days < 0) {
            days += 31;
            nextExecution.month.decreaseVal(1);
        }
        int months = nextExecution.month.getValue() - this.month.getValue();
        return minutes + hours * 60 + days * 24 * 60 + months * 31 * 24 * 60;
    }
}
