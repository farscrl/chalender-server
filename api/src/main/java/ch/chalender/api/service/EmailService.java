package ch.chalender.api.service;

import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventLookup;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface EmailService {
    void sendAccountConfirmationEmail(String emailAddress, String name, String confirmationCode) throws MessagingException, UnsupportedEncodingException;
    void sendPasswordResetEmail(String emailAddress, String name, String confirmationCode) throws MessagingException, UnsupportedEncodingException;
    void sendEventPublishedEmail(String emailAddress, String name, Event event, String comment) throws MessagingException, UnsupportedEncodingException;
    void sendEventUpdateAcceptedEmail(String emailAddress, String name, Event event, String comment) throws MessagingException, UnsupportedEncodingException;
    void sendEventRefusedEmail(String emailAddress, String name, Event event, String comment) throws MessagingException, UnsupportedEncodingException;
    void sendEventUpdateRefusedEmail(String emailAddress, String name, Event event, String comment) throws MessagingException, UnsupportedEncodingException;
    void sendEmailSubscriptionInstant(String emailAddress, String userName, String subscriptionName, Event event, String subscriptionId) throws MessagingException, UnsupportedEncodingException;
    void sendEmailSubscriptionWeekly(String emailAddress, String userName, String subscriptionName, List<EventLookup> events, String subscriptionId) throws MessagingException, UnsupportedEncodingException;
}
