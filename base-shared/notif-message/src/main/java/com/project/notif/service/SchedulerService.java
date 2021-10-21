package com.project.notif.service;

import com.project.notif.tracelogged.EventLogManager;
import com.project.notif.worker.EmailNotificationWorker;
import com.project.notif.worker.PushNotificationWorker;
import com.project.notif.worker.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :15/10/2021 - 10:31 AM
 */
@Service
public class SchedulerService {

    @Autowired
    private ApplicationContext appContext;
    private static ThreadPoolTaskExecutor taskExecutor;


    @Autowired
    private EmailNotificationWorker emailNotificationWorker;

    @Autowired
    private PushNotificationWorker notificationWorker;

    @Scheduled(fixedDelay = 1000)// 1 second
    public void doJobSendEmailNitification() {
        //System.out.println("********* Start doJobRegisterLicense *****");
        doJob(emailNotificationWorker);
    }


    @Scheduled(fixedDelay = 1000)// 1 second
    public void pushNotification() {
        //System.out.println("********* Start doJobRegisterLicense *****");
        doJob(notificationWorker);
    }

    private void doJob(Worker jobWorker) {
        try {
            //System.out.println("Job worker " + jobWorker.getJobName() + " Runing ...");
            if (taskExecutor == null) {
                taskExecutor = (ThreadPoolTaskExecutor) appContext.getBean("executorWithPoolSizeRange");
            }
            // Get thread pool size available
            int corePoolSize = taskExecutor.getCorePoolSize();
            // Check message queue is available for encode
            if (!jobWorker.isQueueEmpty()) {
                // Check thread can create in Thread pool
                if (taskExecutor.getActiveCount() < corePoolSize) {
                    if (jobWorker.getJobType().equals(Worker.JobType.SINGLE)) {
                        jobWorker.doWork();
                    } else {
                        // There're free Thread
                        for (int i = 0; i < Math.min(corePoolSize - taskExecutor.getActiveCount(), corePoolSize); i++) {
                            jobWorker.doWork();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            EventLogManager.getInstance().error(ex);
        }
    }

}
