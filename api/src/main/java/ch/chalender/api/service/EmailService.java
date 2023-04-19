package ch.chalender.api.service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailService {
    public void sendAccountConfirmationEmail(String emailAddress) throws MessagingException, UnsupportedEncodingException;
}
