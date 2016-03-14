package io.nowave.api.service;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;

public class SendGridEmailService implements EmailService {

    private final Log logger = LogFactory.getLog(getClass());

    private final SendGrid sendgrid;

    public SendGridEmailService(String apiKey) {

        logger.info("Initializing EmailServiceWithSendGrid...");
        sendgrid = new SendGrid(apiKey);
    }

    @Override
    public void sendEmail(Set<String> to, String from, String subject, String htmlBody) {
        SendGrid.Email email = new SendGrid.Email();

        email.setTo(to.toArray(new String[to.size()]));
        email.setFrom(from);
        email.setSubject(subject);
        email.setHtml(htmlBody);

        try {
            sendgrid.send(email).getMessage();

        } catch (SendGridException e) {
            logger.error("Error sending email through SendGrid", e);
        }
    }
}
