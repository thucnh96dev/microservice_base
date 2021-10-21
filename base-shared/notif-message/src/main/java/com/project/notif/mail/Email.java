package com.project.notif.mail;

import java.util.Set;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :15/10/2021 - 9:26 AM
 */
public interface Email {

    String getFromAddress();

    Set<String> getToAddresses();

    Set<String> getCcAddresses();

    Set<String> getBccAddresses();

    Set<String> getAttachments();

    String getSubject();

    String getBody();
}
