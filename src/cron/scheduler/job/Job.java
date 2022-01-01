package cron.scheduler.job;

import cron.scheduler.time.Timer;

/**
 * Job is the basic unit of the scheduler.
 * It's runnable to get run in a separate thread.
 * It's comparable by its next execution time.
 */
public class Job implements Comparable<Job>, Runnable {
    private Runnable function;
    private Timer timer;
    private String ID;

    public Job(String ID, String cronExpression, Runnable function) throws Exception {
        this.timer = CronExpressionParser.parse(cronExpression);
        this.ID = ID;
        this.function = function;

    }

    public void changeSchedule(String cronExpression) throws Exception {
        this.timer = CronExpressionParser.parse(cronExpression);
    }

    public void setFunction(Runnable function) {
        this.function = function;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public Integer getNextTime() {
        try {
            return this.timer.getNextExecutionTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int compareTo(Job job) {
        return this.getNextTime() - job.getNextTime();
    }

    @Override
    public void run() {
        execute();
    }
    public void execute() {
        function.run();
    }

}
