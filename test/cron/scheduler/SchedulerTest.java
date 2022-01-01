package cron.scheduler;

import org.junit.Assert;
import org.junit.Test;

public class SchedulerTest {

    @Test
    public void testSchedulerAPI(){
        Scheduler scheduler = Scheduler.getInstance();
        Assert.assertTrue(scheduler.addJob("Job1","* * * *",()->{}));
        Assert.assertTrue(scheduler.addJob("Job2","* * * *",()->{}));
        Assert.assertTrue(scheduler.addJob("Job3","* * * *",()->{}));

        Assert.assertTrue(scheduler.changeJobSchedule("Job3","7 * * *"));

        Assert.assertTrue(scheduler.changeFunction("Job3",()->{
            System.out.println("Changed");
        }));

        Assert.assertTrue(scheduler.changeID("Job3","Job4"));
        Assert.assertFalse(scheduler.changeID("Job3","Job4"));

        Assert.assertFalse(scheduler.addJob("Job4","* * * *",()->{}));
        Assert.assertFalse(scheduler.changeJobSchedule("Job4","80 * * *"));

        Assert.assertEquals(3,scheduler.numOfTotalJobs());
        Assert.assertEquals(0,scheduler.numOfScheduledJobs());

        Assert.assertTrue(scheduler.startJob("Job1"));
        Assert.assertFalse(scheduler.startJob("Job1"));
        scheduler.startJob("Job2");

        Assert.assertEquals(3,scheduler.numOfTotalJobs());
        Assert.assertEquals(2,scheduler.numOfScheduledJobs());

        scheduler.startAll();
        Assert.assertEquals(3,scheduler.numOfScheduledJobs());

        Assert.assertFalse(scheduler.stopJob("Rand"));
        Assert.assertTrue(scheduler.stopJob("Job1"));

        Assert.assertEquals(3,scheduler.numOfTotalJobs());
        Assert.assertEquals(2,scheduler.numOfScheduledJobs());

        Assert.assertTrue(scheduler.removeJob("Job1"));

        Assert.assertEquals(2,scheduler.numOfTotalJobs());
        Assert.assertEquals(2,scheduler.numOfScheduledJobs());

        scheduler.removeAll();
        Assert.assertEquals(0,scheduler.numOfTotalJobs());
        Assert.assertEquals(0,scheduler.numOfScheduledJobs());
    }
}
