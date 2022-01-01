package cron.scheduler;

import cron.scheduler.job.Job;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scheduler implements Runnable {
    private final PriorityQueue<Job> jobsQueue;
    private final HashMap<String, Job> idMap;
    private final ExecutorService pool;
    private static Scheduler singleton;
    private final Logger logger;

    private Scheduler() {
        this.jobsQueue = new PriorityQueue<>();
        this.idMap = new HashMap<>();
        this.pool = Executors.newCachedThreadPool();
        this.logger = Logger.getLogger(Scheduler.class.getName());
        new Thread(this).start();
    }

    /**
     * Only one instance of the scheduler should be created.
     * create the instance if not exist.
     * the method should synchronize called to avoid create multiple instances.
     *
     * @return singleton instance
     */
    public static synchronized Scheduler getInstance() {
        if (singleton == null) singleton = new Scheduler();
        return singleton;
    }

    /**
     * Create a job from a given ID, cronExpression to schedule this job and the runnable object of the job.
     *
     * @param ID             job id
     * @param cronExpression string expression on the form Min Hour Day Month
     * @param function       runnable object
     * @return true if added successfully, false if not.
     */
    public boolean addJob(String ID, String cronExpression, Runnable function) {
        synchronized (idMap) {
            if (idMap.containsKey(ID)) {
                logger.log(Level.WARNING, "A job with same ID:" + ID + " already exists");
                return false;
            }
            try {
                Job job = new Job(ID, cronExpression, function);
                idMap.put(ID, job);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
                return false;
            }
            return true;
        }
    }

    /**
     * Start scheduling a job from the existing jobs.
     *
     * @param jobID ID of the job to get started.
     * @return true if started successfully, false if not.
     */
    public boolean startJob(String jobID) {
        if (!idMap.containsKey(jobID)) {
            logger.log(Level.WARNING, "Job with ID:" + jobID + " not exists");
            return false;
        }
        Job job = idMap.get(jobID);
        synchronized (jobsQueue) {
            if (jobsQueue.contains(job)) return false;
            jobsQueue.add(job);
            return true;
        }
    }

    public void startAll() {
        for (String id : this.idMap.keySet()) startJob(id);
    }

    /**
     * Remove a job from the existing jobs [and stop it first].
     *
     * @param jobID ID of the job to be removed.
     * @return true if removed successfully, false if not.
     */
    public boolean removeJob(String jobID) {
        synchronized (idMap) {
            if (!idMap.containsKey(jobID)) {
                logger.log(Level.WARNING, "Job with ID:" + jobID + " not exists");
                return false;
            }
            stopJob(jobID);
            idMap.remove(jobID);
            return true;
        }
    }

    public void removeAll() {
        stopAll();
        this.idMap.clear();
    }

    /**
     * Temporary stop a job from execution [can be resumed using startJob]
     *
     * @param jobID ID of the job to be stopped.
     * @return true if stopped successfully, false if not.
     */
    public boolean stopJob(String jobID) {
        if (!idMap.containsKey(jobID)) {
            logger.log(Level.WARNING, "Job with ID:" + jobID + " not exists");
            return false;
        }
        synchronized (jobsQueue) {
            jobsQueue.remove(idMap.get(jobID));
        }
        return true;
    }

    public void stopAll() {
        this.jobsQueue.clear();
    }

    /**
     * Reschedule a job for another cron expression
     *
     * @param jobID          id of the job to change the schedule
     * @param cronExpression the new crone expression to extract the schedule
     * @return true if successfully changed, false otherwise
     */
    public boolean changeJobSchedule(String jobID, String cronExpression) {
        if (!idMap.containsKey(jobID)) {
            logger.log(Level.WARNING, "Job with ID:" + jobID + " not exists");
            return false;
        }
        try {
            idMap.get(jobID).changeSchedule(cronExpression);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return false;
    }

    /**
     * change the ID of a job.
     *
     * @param jobID id of the job to change the id
     * @param newID the new id to be assigned
     * @return true if successfully changed, false otherwise
     */
    public boolean changeID(String jobID, String newID) {
        synchronized (idMap) {
            if (!idMap.containsKey(jobID)) {
                logger.log(Level.WARNING, "Job with ID:" + jobID + " not exists");
                return false;
            }
            Job job = idMap.get(jobID);
            idMap.remove(jobID);
            job.setID(newID);
            idMap.put(newID, job);
            return true;
        }
    }

    /**
     * change the function of a job.
     *
     * @param jobID    id of the job to change the id
     * @param function the new runnable to be assigned to the job
     * @return true if successfully changed, false otherwise
     */
    public boolean changeFunction(String jobID, Runnable function) {
        if (!idMap.containsKey(jobID)) {
            logger.log(Level.WARNING, "Job with ID:" + jobID + " not exists");
            return false;
        }
        idMap.get(jobID).setFunction(function);
        return true;
    }

    public int numOfTotalJobs() {
        return idMap.size();
    }

    public int numOfScheduledJobs() {
        return jobsQueue.size();
    }


    @Override
    public void run() {
        while (true) {
            try {
                //wait till the beginning of the next minute
                long millisWithinSecond = System.currentTimeMillis() % 1000;
                long secondsWithinMinute = LocalDateTime.MAX.getSecond() - LocalDateTime.now().getSecond();
                Thread.sleep((1000 - millisWithinSecond) + secondsWithinMinute * 1000);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
            runNextJob();
        }
    }

    private void runNextJob() {
        if (jobsQueue.isEmpty()) return;
        //get all jobs of next time = 0
        ArrayList<Job> jobsToInvoke = new ArrayList<>();
        updateTheQ();
        while (!jobsQueue.isEmpty() && jobsQueue.peek().getNextTime() == 0) {
            Job job = jobsQueue.poll();
            jobsToInvoke.add(job);
        }
        for (Job job : jobsToInvoke) {
            logger.log(Level.INFO, "Executing job with ID: " + job.getID());
            this.pool.submit(job);
            jobsQueue.add(job);
        }
    }

    /**
     * re-sort the priority queue
     */
    private void updateTheQ() {
        if (this.jobsQueue.isEmpty()) return;
        this.jobsQueue.add(this.jobsQueue.poll());
    }
}
