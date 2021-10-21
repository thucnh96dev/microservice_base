package com.project.notif.core;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :15/10/2021 - 10:17 AM
 */
@Service
public class LocalQueueManager {
    // Instance
    private static LocalQueueManager instance;

    public static synchronized LocalQueueManager getInstance() {
        if (instance == null) {
            instance = new LocalQueueManager();
        }
        return instance;
    }

    // Queue email notification
    private static Queue<Map<String, Object>> emailQueue;
    private static Queue<Map<String, Object>> smsQueue;

    private LocalQueueManager() {

        emailQueue = new LinkedList<>();
        smsQueue = new LinkedList<>();
        //Load all queue in DB
        loadData();
    }


    public synchronized Boolean IsMailQueueEmpty() {
        if (emailQueue.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized Boolean IsSmsQueueEmpty() {
        if (smsQueue.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    // For Email notification queue
    public synchronized Map<String, Object> getMailQueue() {
        return emailQueue.remove();
    }

    // For Email notification queue
    public synchronized Map<String, Object> getSmsQueue() {
        return smsQueue.remove();
    }

    public synchronized void addMailQueue(Map<String, Object> obj) {
        emailQueue.add(obj);
    }

    public synchronized void addSmsQueue(Map<String, Object> obj) {
        smsQueue.add(obj);
    }

    private void loadData() {
        //
    }
}
