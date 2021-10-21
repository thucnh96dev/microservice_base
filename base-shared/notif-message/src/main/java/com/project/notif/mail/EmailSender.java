package com.project.notif.mail;

import com.project.notif.AbstractMessageService;
import com.project.notif.mail.transport.EmailTransportConfiguration;
import com.project.notif.tracelogged.EventLogManager;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :15/10/2021 - 10:13 AM
 */
@Component("EmailSender")
public class EmailSender implements AbstractMessageService {
    //private String SmtpHost="localhost";
    private String smtpHost = "";
    private int smtpPort = 25;
    private Boolean isSmtps = false;

    @Override
    public Object sendMessage(Map<String, Object> params) {
        boolean status = true;
        try {
            EmailTransportConfiguration.configure(smtpHost, smtpPort, isSmtps);
            String mailto = (String) params.get("mailto");
            String Subject = (String) params.get("mailto");
            String body = (String) params.get("mailto");
            // Send email
            new EmailMessage()
                    .from("no-reply@mydomain.com")
                    .to(mailto)
                    .withSubject(Subject)
                    .withBody(body)
                    .send();
        } catch (Exception e) {
            EventLogManager.getInstance().error("Send mail error: " + e.getMessage());
            status = false;
        }
        return status;
    }
}
