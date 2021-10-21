package com.project.notif.mail;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :15/10/2021 - 9:27 AM
 */
public interface EmailBuilder {
    EmailBuilder from(String address);

    EmailBuilder to(String... addresses);

    EmailBuilder cc(String... addresses);

    EmailBuilder bcc(String... addresses);

    EmailBuilder withSubject(String subject);

    EmailBuilder withBody(String body);

    EmailBuilder withAttachment(String... attachments);

    void send();
}
