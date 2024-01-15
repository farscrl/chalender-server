package ch.chalender.api.service;

import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventLookup;
import ch.chalender.api.model.NoticeBoardItem;
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
    void sendNoticeBoardPublishedEmail(String emailAddress, String name, NoticeBoardItem item, String comment) throws MessagingException, UnsupportedEncodingException;
    void sendNoticeBoardUpdateAcceptedEmail(String emailAddress, String name, NoticeBoardItem item, String comment) throws MessagingException, UnsupportedEncodingException;
    void sendNoticeBoardRefusedEmail(String emailAddress, String name, NoticeBoardItem item, String comment) throws MessagingException, UnsupportedEncodingException;
    void sendNoticeBoardUpdateRefusedEmail(String emailAddress, String name, NoticeBoardItem item, String comment) throws MessagingException, UnsupportedEncodingException;
    void sendEventSubscriptionInstant(String emailAddress, String userName, String subscriptionName, Event event, String subscriptionId) throws MessagingException, UnsupportedEncodingException;
    void sendEventSubscriptionWeekly(String emailAddress, String userName, String subscriptionName, List<EventLookup> events, String subscriptionId) throws MessagingException, UnsupportedEncodingException;
    void sendNoticesBoardSubscriptionInstant(String emailAddress, String userName, String subscriptionName, NoticeBoardItem item, String subscriptionId) throws MessagingException, UnsupportedEncodingException;
    void sendNoticesBoardSubscriptionWeekly(String emailAddress, String userName, String subscriptionName, List<NoticeBoardItem> items, String subscriptionId) throws MessagingException, UnsupportedEncodingException;
    void sendEventModeratorEmail(Event event) throws MessagingException, UnsupportedEncodingException;
    void sendNoticeBoardModeratorEmail(NoticeBoardItem item) throws MessagingException, UnsupportedEncodingException;
}
