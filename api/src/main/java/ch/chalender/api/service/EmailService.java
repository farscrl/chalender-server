package ch.chalender.api.service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailService {
    public void sendAccountConfirmationEmail(String emailAddress, String name, String confirmationCode) throws MessagingException, UnsupportedEncodingException;
    public void sendPasswordResetEmail(String emailAddress, String name, String confirmationCode) throws MessagingException, UnsupportedEncodingException;
}
