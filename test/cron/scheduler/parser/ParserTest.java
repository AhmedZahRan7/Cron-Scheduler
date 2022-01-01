package cron.scheduler.parser;

import cron.scheduler.time.TimeUnit;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class ParserTest {
    private void testAboveMax(FieldParser<TimeUnit> parser, String expression) {
        try {
            System.out.println(parser.getPossibilitiesSet(expression));
            Assert.fail("Didn't throw exception");
        } catch (Exception ignored) {
        }
    }

    private void testSingleVale(FieldParser<TimeUnit> parser, String expression) throws Exception {
        SortedSet<TimeUnit> set = parser.getPossibilitiesSet(expression);
        int setSize = set.size();
        if (setSize != 1) Assert.fail("Invalid set number");
        if (set.first().getValue() != Integer.parseInt(expression)) Assert.fail("Invalid assign");

    }

    private void testAsterisk(FieldParser<TimeUnit> parser) throws Exception {
        SortedSet<TimeUnit> set = parser.getPossibilitiesSet("*");
        int setSize = set.size();
        if (setSize != parser.getMax() - parser.getMin() + 1) Assert.fail("Invalid set number");
        for (int i = parser.getMin(); i <= parser.getMax(); i++) {
            if (i != set.first().getValue()) Assert.fail("Invalid assign");
            set.remove(set.first());
        }
    }

    private void testRange(FieldParser<TimeUnit> parser, Integer from, Integer to) throws Exception {
        SortedSet<TimeUnit> set = parser.getPossibilitiesSet(from.toString() + "-" + to.toString());
        int setSize = set.size();
        if (setSize != to - from + 1) Assert.fail("Invalid set number");
        for (int i = from; i <= to; i++) {
            if (i != set.first().getValue()) Assert.fail("Invalid assign");
            set.remove(set.first());
        }
    }

    private void testStep(FieldParser<TimeUnit> parser, Integer from, Integer step) throws Exception {
        SortedSet<TimeUnit> set = parser.getPossibilitiesSet(from.toString() + "/" + step.toString());
        for (int i = from; i <= parser.getMax(); i += step) {
            if (i != set.first().getValue()) Assert.fail("Invalid assign");
            set.remove(set.first());
        }

    }

    private void testList(FieldParser<TimeUnit> parser, ArrayList<Integer> list) throws Exception {

        SortedSet<TimeUnit> set = parser.getPossibilitiesSet(list.stream().map(String::valueOf).collect(Collectors.joining(",")));
        Collections.sort(list);
        if (list.size() != set.size()) Assert.fail("Invalid set number");
        for (Integer num : list) {
            if (num.intValue() != set.first().getValue()) Assert.fail("Invalid assign");
            set.remove(set.first());
        }
    }

    private void complexTest(FieldParser<TimeUnit> parser, String expression, ArrayList<Integer> expected) throws Exception {
        SortedSet<TimeUnit> set = parser.getPossibilitiesSet(expression);
        if (expected.size() != set.size()) Assert.fail("Invalid set number");
        for (Integer num : expected) {
            if (num.intValue() != set.first().getValue()) Assert.fail("Invalid assign");
            set.remove(set.first());
        }

    }

    /**
     * Minutes tests
     */
    @Test
    public void minuteExpressionTest() throws Exception {
        FieldParser minutesParser = new MinutesParser();
        testAboveMax(minutesParser, "60");
        testSingleVale(minutesParser, "12");
        testAsterisk(minutesParser);
        testRange(minutesParser, 10, 32);
        testStep(minutesParser, 7, 13);
        testList(minutesParser, new ArrayList<>(Arrays.asList(15, 20, 3, 2, 8)));
        complexTest(minutesParser, "12-38/8", new ArrayList<>(Arrays.asList(12, 20, 28, 36)));
    }

    @Test
    public void hoursExpressionTest() throws Exception {
        FieldParser hoursParser = new HoursParser();
        testAboveMax(hoursParser, "25");
        testSingleVale(hoursParser, "20");
        testAsterisk(hoursParser);
        testRange(hoursParser, 10, 18);
        testStep(hoursParser, 7, 2);
        testList(hoursParser, new ArrayList<>(Arrays.asList(15, 20, 3, 2, 8)));
        complexTest(hoursParser, "0-15/3", new ArrayList<>(Arrays.asList(0, 3, 6, 9, 12, 15)));
    }

    @Test
    public void daysExpressionTest() throws Exception {
        FieldParser daysParser = new DaysParser();
        testAboveMax(daysParser, "35");
        testSingleVale(daysParser, "30");
        testAsterisk(daysParser);
        testRange(daysParser, 10, 29);
        testStep(daysParser, 5, 2);
        testList(daysParser, new ArrayList<>(Arrays.asList(15, 20, 3, 26, 8)));
        complexTest(daysParser, "2-31/15", new ArrayList<>(Arrays.asList(2, 17)));
    }

    @Test
    public void monthExpressionTest() throws Exception {
        FieldParser monthParser = new MonthsParser();
        testAboveMax(monthParser, "15");
        testSingleVale(monthParser, "11");
        testAsterisk(monthParser);
        testRange(monthParser, 2, 12);
        testStep(monthParser, 1, 4);
        testList(monthParser, new ArrayList<>(Arrays.asList(5, 10, 3, 2, 8)));
        complexTest(monthParser, "1-10/3", new ArrayList<>(Arrays.asList(1, 4, 7, 10)));
    }
}
