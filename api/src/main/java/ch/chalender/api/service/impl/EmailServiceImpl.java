package ch.chalender.api.service.impl;

import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventLookup;
import ch.chalender.api.model.EventOccurrence;
import ch.chalender.api.model.NoticeBoardItem;
import ch.chalender.api.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class EmailServiceImpl implements EmailService {
    private static final String LOGO_PATH = "templates/images/logo.jpg";
    private static final String PNG_MIME = "image/png";
    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    @Value("${chalender.appUrl}")
    private String appUrl;

    @Value("${chalender.moderatorEmail}")
    private String[] moderatorEmails;

    String mailFrom = "no-reply@chalender.ch";
    String mailFromName = "chalender.ch";

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendAccountConfirmationEmail(String emailAddress, String name, String confirmationCode) throws MessagingException, UnsupportedEncodingException {
        String confirmationUrl = appUrl + "/user/confirm-email?code=" + confirmationCode;

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(emailAddress);
        email.setSubject("[chalender.ch] " + "Confermar tia adressa dad e-mail");
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", emailAddress);
        ctx.setVariable("name", name);
        ctx.setVariable("logo", LOGO_PATH);
        ctx.setVariable("url", confirmationUrl);

        final String textContent = this.templateEngine.process("email-user/confirm-email.txt", ctx);
        final String htmlContent = this.templateEngine.process("email-user/confirm-email.html", ctx);

        email.setText(textContent, htmlContent);

        ClassPathResource clr = new ClassPathResource(LOGO_PATH);

        email.addInline("logo", clr, PNG_MIME);

        mailSender.send(mimeMessage);
    }

    @Override
    public void sendPasswordResetEmail(String emailAddress, String name, String passwordResetToken) throws MessagingException, UnsupportedEncodingException {
        String passwordResetUrl = appUrl + "/user/confirm-password?token=" + passwordResetToken;

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(emailAddress);
        email.setSubject("[chalender.ch] " + "Redefinir tes pled-clav");
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", emailAddress);
        ctx.setVariable("name", name);
        ctx.setVariable("logo", LOGO_PATH);
        ctx.setVariable("url", passwordResetUrl);

        final String textContent = this.templateEngine.process("email-user/reset-password-email.txt", ctx);
        final String htmlContent = this.templateEngine.process("email-user/reset-password-email.html", ctx);

        email.setText(textContent, htmlContent);

        ClassPathResource clr = new ClassPathResource(LOGO_PATH);

        email.addInline("logo", clr, PNG_MIME);

        mailSender.send(mimeMessage);
    }

    @Override
    public void sendEventPublishedEmail(String emailAddress, String name, Event event, String comment) throws MessagingException, UnsupportedEncodingException {
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String subject = "Occurrenza publitgada: «" + event.getCurrentlyPublished().getTitle() + "»";

        email.setTo(emailAddress);
        email.setSubject("[chalender.ch] " + subject);
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        String eventUrl = appUrl + "/" + event.getId();

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", emailAddress);
        ctx.setVariable("name", name);
        ctx.setVariable("logo", LOGO_PATH);
        ctx.setVariable("eventUrl", eventUrl);
        ctx.setVariable("comment", comment);
        ctx.setVariable("mainLink", appUrl);
        ctx.setVariable("subject", subject);

        final String textContent = this.templateEngine.process("email-moderation-event/event-published.txt", ctx);
        final String htmlContent = this.templateEngine.process("email-moderation-event/event-published.html", ctx);

        email.setText(textContent, htmlContent);

        ClassPathResource clr = new ClassPathResource(LOGO_PATH);

        email.addInline("logo", clr, PNG_MIME);

        mailSender.send(mimeMessage);
    }

    @Override
    public void sendEventUpdateAcceptedEmail(String emailAddress, String name, Event event, String comment) throws MessagingException, UnsupportedEncodingException {
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String subject = "Acceptà l’actualisaziun da l’occurrenza «" + event.getCurrentlyPublished().getTitle() + "»";

        email.setTo(emailAddress);
        email.setSubject("[chalender.ch] " + subject);
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        String eventUrl = appUrl + "/" + event.getId();

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", emailAddress);
        ctx.setVariable("name", name);
        ctx.setVariable("logo", LOGO_PATH);
        ctx.setVariable("eventUrl", eventUrl);
        ctx.setVariable("comment", comment);
        ctx.setVariable("mainLink", appUrl);
        ctx.setVariable("subject", subject);

        final String textContent = this.templateEngine.process("email-moderation-event/event-update-published.txt", ctx);
        final String htmlContent = this.templateEngine.process("email-moderation-event/event-update-published.html", ctx);

        email.setText(textContent, htmlContent);

        ClassPathResource clr = new ClassPathResource(LOGO_PATH);

        email.addInline("logo", clr, PNG_MIME);

        mailSender.send(mimeMessage);
    }

    @Override
    public void sendEventRefusedEmail(String emailAddress, String name, Event event, String comment) throws MessagingException, UnsupportedEncodingException {
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String subject = "La moderaziun ha refusà la publicaziun da «" + event.getRejected().getTitle() + "»";

        email.setTo(emailAddress);
        email.setSubject("[chalender.ch] " + subject);
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        String eventUrl = appUrl + "/" + event.getId();

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", emailAddress);
        ctx.setVariable("name", name);
        ctx.setVariable("logo", LOGO_PATH);
        ctx.setVariable("eventUrl", eventUrl);
        ctx.setVariable("comment", comment);
        ctx.setVariable("mainLink", appUrl);
        ctx.setVariable("subject", subject);

        final String textContent = this.templateEngine.process("email-moderation-event/event-rejected.txt", ctx);
        final String htmlContent = this.templateEngine.process("email-moderation-event/event-rejected.html", ctx);

        email.setText(textContent, htmlContent);

        ClassPathResource clr = new ClassPathResource(LOGO_PATH);

        email.addInline("logo", clr, PNG_MIME);

        mailSender.send(mimeMessage);
    }

    @Override
    public void sendEventUpdateRefusedEmail(String emailAddress, String name, Event event, String comment) throws MessagingException, UnsupportedEncodingException {
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String subject = "La moderaziun ha refusà la midada da l’occurrenza «" + event.getCurrentlyPublished().getTitle() + "»";

        email.setTo(emailAddress);
        email.setSubject("[chalender.ch] " + subject);
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        String eventUrl = appUrl + "/" + event.getId();

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", emailAddress);
        ctx.setVariable("name", name);
        ctx.setVariable("logo", LOGO_PATH);
        ctx.setVariable("eventUrl", eventUrl);
        ctx.setVariable("comment", comment);
        ctx.setVariable("mainLink", appUrl);
        ctx.setVariable("subject", subject);

        final String textContent = this.templateEngine.process("email-moderation-event/event-update-rejected.txt", ctx);
        final String htmlContent = this.templateEngine.process("email-moderation-event/event-update-rejected.html", ctx);

        email.setText(textContent, htmlContent);

        ClassPathResource clr = new ClassPathResource(LOGO_PATH);

        email.addInline("logo", clr, PNG_MIME);

        mailSender.send(mimeMessage);
    }


    @Override
    public void sendNoticeBoardPublishedEmail(String emailAddress, String name, NoticeBoardItem item, String comment) throws MessagingException, UnsupportedEncodingException {
        // TODO: implement
    }

    @Override
    public void sendNoticeBoardUpdateAcceptedEmail(String emailAddress, String name, NoticeBoardItem item, String comment) throws MessagingException, UnsupportedEncodingException {
        // TODO: implement
    }

    @Override
    public void sendNoticeBoardRefusedEmail(String emailAddress, String name, NoticeBoardItem item, String comment) throws MessagingException, UnsupportedEncodingException {
        // TODO: implement
    }

    @Override
    public void sendNoticeBoardUpdateRefusedEmail(String emailAddress, String name, NoticeBoardItem item, String comment) throws MessagingException, UnsupportedEncodingException {
        // TODO: implement
    }

    @Override
    public void sendEmailSubscriptionInstant(String emailAddress, String userName, String subscriptionName, Event event, String subscriptionId) throws MessagingException, UnsupportedEncodingException {
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String subject = "Ina occurrenza per tes abo «" + subscriptionName + "»";

        email.setTo(emailAddress);
        email.setSubject("[chalender.ch] " + subject);
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        List<String> datesList = new ArrayList<>();
        for (EventOccurrence occ : event.getCurrentlyPublished().getOccurrences()) {
            String date = getOccurrenceString(occ.getDate(), occ.getStart(), occ.getEnd(), occ.isAllDay());
            datesList.add(date);
        }

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", emailAddress);
        ctx.setVariable("name", userName);
        ctx.setVariable("logo", LOGO_PATH);
        ctx.setVariable("subscriptionName", subscriptionName);
        ctx.setVariable("event", event);
        ctx.setVariable("datesList", datesList);
        ctx.setVariable("subject", subject);
        ctx.setVariable("accountLink", appUrl + "/user/subscriptions/events/" + subscriptionId);
        ctx.setVariable("unsubscribeLink", appUrl + "/user/subscriptions/events/disable/" + subscriptionId);
        ctx.setVariable("mainLink", appUrl);

        final String textContent = this.templateEngine.process("email-subscription-events/subscription-instant.txt", ctx);
        final String htmlContent = this.templateEngine.process("email-subscription-events/subscription-instant.html", ctx);

        email.setText(textContent, htmlContent);

        ClassPathResource clr = new ClassPathResource(LOGO_PATH);

        email.addInline("logo", clr, PNG_MIME);

        mailSender.send(mimeMessage);
    }

    @Override
    public void sendEmailSubscriptionWeekly(String emailAddress, String userName, String subscriptionName, List<EventLookup> events, String subscriptionId) throws MessagingException, UnsupportedEncodingException {
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String subject = events.size() + " occurrenzas per tes abo «" + subscriptionName + "»";
        if (events.size() == 1) {
            subject = "Ina occurrenza per tes abo «" + subscriptionName + "»";
        }

        email.setTo(emailAddress);
        email.setSubject("[chalender.ch] " + subject);
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        List<String> datesList = new ArrayList<>();
        for (EventLookup event : events) {
            String date = getOccurrenceString(event.getDate(), event.getStart(), event.getEnd(), event.isAllDay());
            datesList.add(date);
        }

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", emailAddress);
        ctx.setVariable("name", userName);
        ctx.setVariable("logo", LOGO_PATH);
        ctx.setVariable("subscriptionName", subscriptionName);
        ctx.setVariable("events", events);
        ctx.setVariable("datesList", datesList);
        ctx.setVariable("subject", subject);
        ctx.setVariable("accountLink", appUrl + "/user/subscriptions/events/" + subscriptionId);
        ctx.setVariable("unsubscribeLink", appUrl + "/user/subscriptions/events/disable/" + subscriptionId);
        ctx.setVariable("mainLink", appUrl);

        final String textContent = this.templateEngine.process("email-subscription-events/subscription-weekly.txt", ctx);
        final String htmlContent = this.templateEngine.process("email-subscription-events/subscription-weekly.html", ctx);

        email.setText(textContent, htmlContent);

        ClassPathResource clr = new ClassPathResource(LOGO_PATH);

        email.addInline("logo", clr, PNG_MIME);

        mailSender.send(mimeMessage);
    }

    @Override
    public void sendEventModeratorEmail(Event event) throws MessagingException, UnsupportedEncodingException {
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String subject = "«" + event.getTitle() + "» spetga sin la moderaziun";

        email.setTo(moderatorEmails);
        email.setSubject("[chalender.ch] " + subject);
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("logo", LOGO_PATH);
        ctx.setVariable("event", event);
        ctx.setVariable("subject", subject);
        ctx.setVariable("moderationLink", appUrl + "/moderator/events");
        ctx.setVariable("mainLink", appUrl);

        final String textContent = this.templateEngine.process("email-moderation-event/moderation-event.txt", ctx);
        final String htmlContent = this.templateEngine.process("email-moderation-event/moderation-event.html", ctx);

        email.setText(textContent, htmlContent);

        ClassPathResource clr = new ClassPathResource(LOGO_PATH);

        email.addInline("logo", clr, PNG_MIME);

        mailSender.send(mimeMessage);
    }

    @Override
    public void sendNoticeBoardModeratorEmail(NoticeBoardItem item) throws MessagingException, UnsupportedEncodingException {
        // TODO: implement
    }

    private String getOccurrenceString(LocalDate date, LocalTime startTime, LocalTime endTime, boolean isAllDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String result = formatter.format(date);
        if (isAllDay) {
            return result + ", tuttadi";
        }

        result = result + ", " + startTime.toString();
        if (endTime != null) {
            result = result + " - " + endTime.toString();
        }

        return result;
    }
}
