package io.nowave.api.service;

import java.util.Set;

public interface EmailService {

    void sendEmail(Set<String> to, String from, String subject, String htmlBody);
}
