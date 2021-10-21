package com.project.notif.worker;

import org.springframework.stereotype.Component;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :15/10/2021 - 10:11 AM
 */
@Component
public abstract class Worker {

    public abstract JobType getJobType();

    public abstract String getJobName();

    public abstract void setJobName(String name);

    public abstract Boolean isQueueEmpty();

    public abstract void doWork();

    public static enum JobType {
        // Allow run single thread for each schedule
        SINGLE,
        // Allow run multi-thread for each schedule & manage by thread pool size
        MULTIPLE
    }
}
