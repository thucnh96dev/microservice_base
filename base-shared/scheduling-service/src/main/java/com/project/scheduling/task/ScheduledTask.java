package com.project.scheduling.task;

import java.util.concurrent.ScheduledFuture;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :21/10/2021 - 11:53 AM
 */
public class ScheduledTask {
    public volatile ScheduledFuture<?> future;

    /**
     *
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
