package cron.scheduler.parser;

import cron.scheduler.time.TimeUnit;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Field parser validates that the expression matches the right regex
 * and calculates the set of possibilities of the given expression
 * <p>
 * min is the minimum value can be assigned to this field ie 0 for minutes 1 for months
 * max is the maximum value can be assigned to this field ie 59 for minutes 12 for months
 */
abstract class FieldParser<T extends TimeUnit> {
    private final String generalRegex;
    private final Class<T> cls;

    protected FieldParser(String unitRegex, Class<T> cls) {
        this.generalRegex = buildGeneralRegex(unitRegex);
        this.cls = cls;
    }

    /**
     * calculates the set of possibilities of the given expression
     * eg: for hour field and expression * -> Possibilities Set = [0,1,2,...23]
     * eg: for minute field and expression 2,12-16,0/15 -> Possibilities Set = [0,2,12,13,14,15,16,30,45]
     * eg: for day field and expression 3-15/5 -> Possibilities Set = [3,8,13]
     *
     * @return sorted set for all possibilities
     */
    public SortedSet<T> getPossibilitiesSet(String expression) throws Exception {
        if (!checkValidExpression(expression))
            throw new Exception("Invalid expression: " + expression + " when parsing " + cls.getName());
        return buildPossibilities(expression);
    }

    public Integer getMin() {
        try {
            return cls.getConstructor(Integer.class).newInstance(1).getMin();
        } catch (Exception ignored) {
        }
        return null;
    }

    public Integer getMax() {
        try {
            return cls.getConstructor(Integer.class).newInstance(1).getMax();
        } catch (Exception ignored) {
        }
        return null;
    }

    private boolean checkValidExpression(String expression) {
        return Pattern.matches(generalRegex, expression);
    }

    /**
     * general regex should be on the form ^\*|(R(-R)?(/R)?(,R(-R)?(/R)?))$
     * where R is the single unit regex
     *
     * @param singleUnitRegex single value that can be assigned to this field
     *                        ie [1-5]?[0-9] for minutes [0,1,...59]
     *                        so the general regex must be
     *                        ^\*|[1-5]?[0-9](-[1-5]?[0-9])?(/[1-5]?[0-9])?(,[1-5]?[0-9](-[1-5]?[0-9])?(/[1-5]?[0-9])?)*$
     * @return the general regex
     */
    private String buildGeneralRegex(String singleUnitRegex) {
        final String rangeRegex = singleUnitRegex + "(-" + singleUnitRegex + ")?";
        final String stepRegex = rangeRegex + "(/" + singleUnitRegex + ")?";
        final String listRegex = stepRegex + "(," + stepRegex + ")*";
        String allRangeRegex = "\\*";
        return "^" + allRangeRegex + "|" + "(" + listRegex + ")" + "$";
    }

    /**
     * split the given expression by ',' and concatenate the generated possible values
     *
     * @param expression expression written by the usr
     * @return sorted set of all possible values according to the given expression
     */
    private SortedSet<T> buildPossibilities(String expression) throws Exception {
        SortedSet<T> set = new TreeSet<>();
        if (expression.equals("*")) {
            set.addAll(getRange(getMin(), getMax(), 1));
            return set;
        }
        String[] list = expression.split(",");
        for (String interval : list) {
            set.addAll(getValues(interval));
        }
        return set;
    }

    private ArrayList<T> getValues(String interval) throws Exception {
        int step = 1;
        int slashIdx = interval.indexOf('/');
        //the step option is specified
        if (slashIdx != -1) {
            step = Integer.parseInt(interval.substring(slashIdx + 1));
            interval = interval.substring(0, slashIdx);
        }
        int minusIdx = interval.indexOf('-');
        //if only single unit in the regex
        if (minusIdx == -1) {
            if (slashIdx == -1) {
                T child = cls.getConstructor(Integer.class).newInstance(Integer.parseInt(interval));
                return new ArrayList<>(List.of(child));
            } else return getRange(Integer.parseInt(interval), getMax(), step);
        }
        int from = Integer.parseInt(interval.substring(0, minusIdx));
        int to = Integer.parseInt(interval.substring(minusIdx + 1));
        if (from < getMin() || to > getMax())
            throw new Error(
                    "Out of boundaries\ngiven " + from + "while minimum is " + getMin() +
                    "\ngiven " + to + "while maximum is " + getMax() +
                    "for class " + cls.getName()
            );

        return getRange(from, to, step);
    }

    private ArrayList<T> getRange(Integer from, Integer to, Integer step) throws Exception {
        ArrayList<T> values = new ArrayList<>();
        if (from > to) return values;
        for (Integer i = from; i <= to; i += step) {
            T child = cls.getConstructor(Integer.class).newInstance(i);
            values.add(child);
        }
        return values;
    }
}
