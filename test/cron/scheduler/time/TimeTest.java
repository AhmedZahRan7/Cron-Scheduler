package cron.scheduler.time;

import org.junit.Assert;
import org.junit.Test;

public class TimeTest {

    private int generateRand(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }

    void increaseTimeUnit(TimeUnit unit) {
        Integer value = generateRand(1, unit.getMax() - unit.getValue());
        Integer before = unit.getValue();
        unit.increaseVal(value);
        int expected = before + value;
        int found = unit.getValue();
        if (expected != found)
            Assert.fail("Invalid increasing\nExpected " + expected + "\nFound " + found);
    }

    void increaseAboveMax(TimeUnit unit) {
        Integer value = generateRand(unit.getMax() - unit.getValue() + 1, unit.getMax());
        Integer before = unit.getValue();
        unit.increaseVal(value);
        int expected = Math.max(unit.getMin(), (before + value) % (unit.getMax() + 1));
        int found = unit.getValue();
        if (expected != found)
            Assert.fail("Invalid increasing\nExpected " + expected + "\nFound " + found);
    }

    void decreaseTimeUnit(TimeUnit unit) {
        Integer value = generateRand(1, unit.getValue());
        Integer before = unit.getValue();
        unit.decreaseVal(value);
        int expected = before - value;
        int found = unit.getValue();
        if (expected != found)
            Assert.fail("Invalid increasing\nExpected " + expected + "\nFound " + found);
    }

    void decreaseBelowMin(TimeUnit unit) {
        Integer value = generateRand(unit.getValue() + 1, unit.getMax());
        Integer before = unit.getValue();
        unit.decreaseVal(value);
        int expected = (before - value + unit.getMax() + 1) % (unit.getMax() + 1);
        int found = unit.getValue();
        if (expected != found)
            Assert.fail("Invalid increasing\nExpected " + expected + "\nFound " + found);
    }

    @Test
    public void testIncreasingValue() throws Exception {
        increaseTimeUnit(new Month(generateRand(1, 12)));
        increaseAboveMax(new Month(generateRand(1, 12)));
        decreaseTimeUnit(new Month(generateRand(1, 12)));
        decreaseBelowMin(new Month(generateRand(1, 12)));

        increaseTimeUnit(new Day(generateRand(1, 31)));
        increaseAboveMax(new Day(generateRand(1, 31)));
        decreaseTimeUnit(new Day(generateRand(1, 31)));
        decreaseBelowMin(new Day(generateRand(1, 31)));

        increaseTimeUnit(new Hour(generateRand(0, 23)));
        increaseAboveMax(new Hour(generateRand(0, 23)));
        decreaseTimeUnit(new Hour(generateRand(0, 23)));
        decreaseBelowMin(new Hour(generateRand(0, 23)));

        increaseTimeUnit(new Minute(generateRand(0, 59)));
        increaseAboveMax(new Minute(generateRand(0, 59)));
        decreaseTimeUnit(new Minute(generateRand(0, 59)));
        decreaseBelowMin(new Minute(generateRand(0, 59)));
    }

    @Test
    public void testRemainingTime() throws Exception {
        int found;
        int expected;
        Time time = new Time(
                new Month(5),
                new Day(5),
                new Hour(5),
                new Minute(5)
        );
        found = time.remainingMinutesToReach(new Time(
                new Month(5),
                new Day(5),
                new Hour(5),
                new Minute(5)
        ));
        expected = 0;
        if (expected != found)
            Assert.fail("Invalid remaining time\nExpected " + expected + "\nFound " + found);

        found = time.remainingMinutesToReach(new Time(
                new Month(5),
                new Day(5),
                new Hour(5),
                new Minute(30)
        ));
        expected = 25;
        if (expected != found)
            Assert.fail("Invalid remaining time\nExpected " + expected + "\nFound " + found);

        found = time.remainingMinutesToReach(new Time(
                new Month(5),
                new Day(5),
                new Hour(6),
                new Minute(0)
        ));
        expected = 55;
        if (expected != found)
            Assert.fail("Invalid remaining time\nExpected " + expected + "\nFound " + found);
    }
}
