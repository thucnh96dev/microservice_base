package com.project.notif.worker;

import com.project.notif.core.LocalQueueManager;
import com.project.notif.mail.EmailSender;
import com.project.notif.tracelogged.EventLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :15/10/2021 - 10:12 AM
 */
@Component
public class EmailNotificationWorker extends Worker {
    //static Logger logger = Logger.getLogger("service");
    @Autowired
    @Qualifier("EmailSender")
    private EmailSender emailSender;

    @Async
    @Override
    public void doWork() {
        if (!LocalQueueManager.getInstance().IsMailQueueEmpty()) {
            // EventLogManager.getInstance().info("doWork send notification email");
            Map<String, Object> request = LocalQueueManager.getInstance().getMailQueue();
//            String emailAddress=(String)request.get("mail_address");
//            String subject=(String)request.get("subject");
//            String body=(String)request.get("body");
            EventLogManager.getInstance().info("EmailNotificationWorker Send email ");
            emailSender.sendMessage(request);
        }
    }

    private String jobName = "EmailNotificationWorker";

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
        return LocalQueueManager.getInstance().IsMailQueueEmpty();
    }

    @Override
    public JobType getJobType() {
        return JobType.MULTIPLE;
    }

}
