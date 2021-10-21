package com.project.notif.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :15/10/2021 - 10:32 AM
 */
@Component
public class PushNotificationWorker extends Worker {
    @Autowired
    private SimpMessagingTemplate template;

    private static final String WEBSOCKET_TOPIC = "/topic/notification";
    private static final String WEBSOCKET_SYSTEM_NOTIFICATION = "/topic/systemnotify";

    @Async
    @Override
    public void doWork() {
        if (!NotificationQueueManager.getInstance().IsQueueEmpty()) {
            Object message = NotificationQueueManager.getInstance().getMessage();
            System.out.println("PushNotificationWorker run ...");
            template.convertAndSend(WEBSOCKET_TOPIC, message);
        }
        if (!NotificationQueueManager.getInstance().IsQueueSystemNotifyEmpty()) {
            Object messObject = NotificationQueueManager.getInstance().getMessageSystemNotify();
            template.convertAndSend(WEBSOCKET_SYSTEM_NOTIFICATION, messObject);
        }
    }

    private String jobName = "PushNotificationWorker";

    @Override
    public String getJobName() {
        return this.jobName;
    }

    @Override
    public void setJobName(String name) {
        this.jobName = name;
    }

    @Override
    public synchronized Boolean isQueueEmpty() {
        return false;
    }

    @Override
    public JobType getJobType() {
        return JobType.MULTIPLE;
    }
}
