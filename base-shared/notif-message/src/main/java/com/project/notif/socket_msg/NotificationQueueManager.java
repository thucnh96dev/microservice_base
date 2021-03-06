package com.project.notif.socket_msg;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :15/10/2021 - 9:44 AM
 */
@Service
public class NotificationQueueManager {
    // Instance
    private static NotificationQueueManager instance;

    public static synchronized NotificationQueueManager getInstance() {
        if (instance == null) {
            instance = new NotificationQueueManager();
        }
        return instance;
    }

    // Encode queue Message
    private static Queue<NotificationMessage> messages;
    private static Queue<NotificationMessage> messageSystemNotify;

    private NotificationQueueManager() {
        messages = new LinkedList<>();
        messageSystemNotify = new LinkedList<>();
    }

    /**
     * Get the QueueMessage
     *
     * @return QueueMessage
     */
    public synchronized NotificationMessage getMessage() {
        return messages.remove();
    }

    public synchronized NotificationMessage getMessageSystemNotify() {
        return messageSystemNotify.remove();
    }

    /**
     * Add QueueMessage to list message storage
     *
     * @param queueMessage
     */
    public synchronized void addMessage(NotificationMessage queueMessage) {
        System.out.println("addMessage notify");
        messages.add(queueMessage);
    }

    public synchronized void addMessageSystemNotify(NotificationMessage queueMessage) {
        System.out.println("addMessage SystemNotify");
        messageSystemNotify.add(queueMessage);
    }

    /**
     * @return
     */
    public synchronized Boolean IsQueueEmpty() {
        if (messages.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized Boolean IsQueueSystemNotifyEmpty() {
        if (messageSystemNotify.size() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
