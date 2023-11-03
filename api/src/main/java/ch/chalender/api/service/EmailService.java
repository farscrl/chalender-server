package ch.chalender.api.service;

import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventLookup;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface EmailService {
    public void sendAccountConfirmationEmail(String emailAddress, String name, String confirmationCode) throws MessagingException, UnsupportedEncodingException;
    public void sendPasswordResetEmail(String emailAddress, String name, String confirmationCode) throws MessagingException, UnsupportedEncodingException;

    public void sendEventPublishedEmail(String emailAddress, String name, Event event, String comment) throws MessagingException, UnsupportedEncodingException;
    public void sendEventUpdateAcceptedEmail(String emailAddress, String name, Event event, String comment) throws MessagingException, UnsupportedEncodingException;
    public void sendEventRefusedEmail(String emailAddress, String name, Event event, String comment) throws MessagingException, UnsupportedEncodingException;
    public void sendEventUpdateRefusedEmail(String emailAddress, String name, Event event, String comment) throws MessagingException, UnsupportedEncodingException;

    public void sendEmailSubscriptionInstant(String emailAddress, String userName, String subscriptionName, Event event) throws MessagingException, UnsupportedEncodingException;
    public void sendEmailSubscriptionWeekly(String emailAddress, String userName, String subscriptionName, List<EventLookup> events) throws MessagingException, UnsupportedEncodingException;
}
