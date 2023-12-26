package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.model.request.EmailRequest;

public interface EmailService {

    void sendEmail(EmailRequest emailRequest);
}