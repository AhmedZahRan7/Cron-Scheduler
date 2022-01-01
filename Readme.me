# Cron Scheduler
## description
Cron-Scheduler is a way to run functions at specific times.
##### Cron expression syntax
The expression should be on the form
```
[minutes] [hours] [days] [months]
```
| Field | Range | Example |Description |
| --- | --- | --- | --- |
|```minutes```|0 -59|5 * * *|The job is initiated at minute 5 each hour in all days of all months.|
|```hours```|0-23|0 3-7 * *|The job is initiated at minute 0 of hours in range 3-7 in all days of all months.|
|```days```|1-31|0 0 1-20/2 *|The job is initiated at minute 0 of hours 0 in odd days in range 1-20 of all months|
|```months```|1-12|0 0 1 5,7,11|The job is initiated at minute 0 of hours 0 in day 1 in months 5, 7 and 11|

##### Assumptions
* All months are 31 days.
* Day-of-week field is ignored

## Implementation details
![alt text](https://i.ibb.co/TqMSdRS/UML-class.png)
* Storing jobs inside the schedule is done though a hashmap ID -> Job to access jobs through its ID fast.
* The schedule itself runs as a separate thread that periodically checks the head of the priority queue [each minute].
```java
while (true) {
    sleepTillNewMinute();
    runNextJob();
}
runNextJob(){
    //the priority is sorted according to the jobs.nextExecutionTime
    while(first element in the priority queue has nextExecutionTime == 0) invoke(this job)
}
```
* Job invocation is done through submitting this job to the pool of threads to run all jobs in parallel, so instead of firing a new thread for a new job where the machine can contain so many threads at a time and thread creation and deletion is expensive, we will submit the job to the pool of thread which can have a bound on maximum number of threads to create.

## Example usage snippet
#### API usage example
```java
public static void main(String[] args) {
    Scheduler scheduler = Scheduler.getInstance();
    scheduler.addJob("1", "0-59/2 * * * ", () -> System.out.println("JOB 1"));
    scheduler.addJob("2", "* * * *", () -> System.out.println("JOB 2"));
    scheduler.addJob("2", "* * * *", () -> System.out.println("JOB 2"));
    scheduler.addJob("3", "100 * * *", () -> System.out.println("JOB 3"));
    scheduler.startJob("1");
    scheduler.startJob("2");
    scheduler.startAll();
    scheduler.stopAll();
    scheduler.removeJob("2");
    scheduler.removeAll();
}
```
#### Running jobs example
```java
public static void main(String[] args) {
    Scheduler scheduler = Scheduler.getInstance();
    scheduler.addJob("1", "0-59/2 * * * ", () -> System.out.println("JOB 1"));
    scheduler.addJob("2", "* * * *", () -> System.out.println("JOB 2"));
    scheduler.addJob("3", "14-16,18 * * *", () -> System.out.println("JOB 3"));
    scheduler.startAll();
}
```
##### Console-Output
```
Jan 01, 2022 6:14:00 PM cron.scheduler.Scheduler runNextJob
INFO: Executing job with ID: 3
Jan 01, 2022 6:14:00 PM cron.scheduler.Scheduler runNextJob
INFO: Executing job with ID: 2
Jan 01, 2022 6:14:00 PM cron.scheduler.Scheduler runNextJob
INFO: Executing job with ID: 1
JOB 3
JOB 2
JOB 1
Jan 01, 2022 6:15:00 PM cron.scheduler.Scheduler runNextJob
INFO: Executing job with ID: 2
Jan 01, 2022 6:15:00 PM cron.scheduler.Scheduler runNextJob
INFO: Executing job with ID: 3
JOB 2
JOB 3
Jan 01, 2022 6:16:00 PM cron.scheduler.Scheduler runNextJob
INFO: Executing job with ID: 3
Jan 01, 2022 6:16:00 PM cron.scheduler.Scheduler runNextJob
INFO: Executing job with ID: 2
Jan 01, 2022 6:16:00 PM cron.scheduler.Scheduler runNextJob
INFO: Executing job with ID: 1
JOB 3
JOB 2
JOB 1
Jan 01, 2022 6:17:00 PM cron.scheduler.Scheduler runNextJob
INFO: Executing job with ID: 2
JOB 2
Jan 01, 2022 6:18:00 PM cron.scheduler.Scheduler runNextJob
INFO: Executing job with ID: 3
Jan 01, 2022 6:18:00 PM cron.scheduler.Scheduler runNextJob
INFO: Executing job with ID: 2
Jan 01, 2022 6:18:00 PM cron.scheduler.Scheduler runNextJob
INFO: Executing job with ID: 1
JOB 3
JOB 2
JOB 1
Jan 01, 2022 6:19:00 PM cron.scheduler.Scheduler runNextJob
INFO: Executing job with ID: 2
JOB 2
Jan 01, 2022 6:20:00 PM cron.scheduler.Scheduler runNextJob
INFO: Executing job with ID: 1
Jan 01, 2022 6:20:00 PM cron.scheduler.Scheduler runNextJob
INFO: Executing job with ID: 2
JOB 1
JOB 2
```
## Future improvements
* Adding seconds and years field.
* Handling the real number of days in a month [currently we consider all months are 31 days].
* Monitoring the thread running time and interrupt if exceed a threshold.
* Recovering from system crashes.