package com.project.notif.mail.validation;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :15/10/2021 - 9:30 AM
 */
public class IncompleteEmailException extends RuntimeException {
    private static final long serialVersionUID = -5356669884453957632L;

    public IncompleteEmailException(String message) {
        super(message);
    }
}
