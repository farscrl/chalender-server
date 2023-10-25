package ch.chalender.api.service.impl;

import ch.chalender.api.model.Event;
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


@Service
public class EmailServiceImpl implements EmailService {
    private static final String LOGO_PATH = "templates/images/logo.jpg";
    private static final String PNG_MIME = "image/png";
    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    @Value("${chalender.appUrl}")
    private String appUrl;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendAccountConfirmationEmail(String emailAddress, String name, String confirmationCode) throws MessagingException, UnsupportedEncodingException {
        String confirmationUrl = appUrl + "/u/confirm-email?code=" + confirmationCode;
        String mailFrom = "no-reply@chalender.ch";
        String mailFromName = "chalender.ch";

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(emailAddress);
        email.setSubject("Confermar tia adressa dad e-mail");
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
        String passwordResetUrl = appUrl + "/u/confirm-password?token=" + passwordResetToken;
        String mailFrom = "no-reply@chalender.ch";
        String mailFromName = "chalender.ch";

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(emailAddress);
        email.setSubject("Redefinir tes pled-clav");
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
        // TODO: implement me
    }

    @Override
    public void sendEventUpdateAcceptedEmail(String emailAddress, String name, Event event, String comment) throws MessagingException, UnsupportedEncodingException {
        // TODO: implement me
    }

    @Override
    public void sendEventRefusedEmail(String emailAddress, String name, Event event, String comment) throws MessagingException, UnsupportedEncodingException {
        // TODO: implement me
    }

    @Override
    public void sendEventUpdateRefusedEmail(String emailAddress, String name, Event event, String comment) throws MessagingException, UnsupportedEncodingException {
        // TODO: implement me
    }
}
