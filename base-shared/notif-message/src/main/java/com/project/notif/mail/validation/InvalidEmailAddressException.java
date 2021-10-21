package com.project.notif.mail.validation;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :15/10/2021 - 9:30 AM
 */
public class InvalidEmailAddressException extends RuntimeException {

    private static final long serialVersionUID = 7521502257697884074L;

    public InvalidEmailAddressException(String message) {
        super(message);
    }
}
