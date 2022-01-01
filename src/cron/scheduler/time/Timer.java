package cron.scheduler.time;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.SortedSet;

public class Timer {
    private final SortedSet<Month> months;
    private final SortedSet<Day> days;
    private final SortedSet<Hour> hours;
    private final SortedSet<Minute> minutes;

    public Timer(SortedSet<Minute> minutes, SortedSet<Hour> hours, SortedSet<Day> days, SortedSet<Month> months) {
        this.months = months;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
    }

    /**
     * Calculates the remaining minutes to reach the nearest execution time
     *
     * @return remaining minutes
     * @throws Exception if any time unit can't get created from the current time
     */
    public Integer getNextExecutionTime() throws Exception {
        LocalDateTime currentTime = LocalDateTime.now();
        Minute currentMinute = new Minute(currentTime.getMinute());
        Hour currentHour = new Hour(currentTime.getHour());
        Day currentDay = new Day(currentTime.getDayOfMonth());
        Month currentMonth = new Month(currentTime.getMonthValue());

        Time time = new Time(currentMonth, currentDay, currentHour, currentMinute);
        return time.remainingMinutesToReach(getNearestTime(time));
    }

    /**
     * Get the nearest execution time from available collections of available months,days,hours and minutes
     * @param time the reference time to get the nearest from [current time]
     * @return nearest avaliable execution time
     */
    private Time getNearestTime(Time time) {
        Month month = getNearestUnit(time.month, this.months);
        if (!Objects.equals(month.getValue(), time.month.getValue()))
            return new Time(month, this.days.first(), this.hours.first(), this.minutes.first());
        Day day = getNearestUnit(time.day, this.days);
        if (!Objects.equals(day.getValue(), time.day.getValue()))
            return new Time(month, day, this.hours.first(), this.minutes.first());
        Hour hour = getNearestUnit(time.hour, this.hours);
        if (!Objects.equals(hour.getValue(), time.hour.getValue()))
            return new Time(month, day, hour, this.minutes.first());
        Minute minute = getNearestUnit(time.minute, this.minutes);

        //Corner case if the next minute will be in the next hour
        if (minute.getValue() < time.minute.getValue()) {
            hour.increaseVal(1);
            hour = getNearestUnit(hour, this.hours);
        }
        return new Time(month, day, hour, minute);
    }

    /**
     * calculates the nearest next available time unit from a sorted set of units
     * to the given unit
     * ie for months sorted set [2,5,8,9]
     * if month is 4 then 5 should be returned
     * if month is 10 then 2 should be returned
     */
    public <T extends TimeUnit> T getNearestUnit(T unit, SortedSet<T> units) {
        //tail set is a sorted set contains all elements greater than the given unit
        SortedSet<T> tailSet = units.tailSet(unit);
        if (tailSet.size() == 0) return units.first();
        return tailSet.first();
    }
}
