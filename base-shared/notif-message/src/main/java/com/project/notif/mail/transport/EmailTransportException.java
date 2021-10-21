package com.project.notif.mail.transport;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :15/10/2021 - 9:23 AM
 */
public class EmailTransportException extends RuntimeException {

    private static final long serialVersionUID = 959435017940801299L;

    public EmailTransportException(String message, Exception cause) {
        super(message, cause);
    }
}
