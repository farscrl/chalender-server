package ch.chalender.api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


@Service
public class EmailServiceImpl implements EmailService {

    private static final String SPRING_LOGO_IMAGE = "templates/images/logo.png";
    private static final String PNG_MIME = "image/png";
    private static final String MAIL_SUBJECT = "Registration Confirmation";

    private final Environment environment;

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    public EmailServiceImpl(Environment environment, JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.environment = environment;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendAccountConfirmationEmail(String emailAddress) throws MessagingException, UnsupportedEncodingException {
        String confirmationUrl = "generated_confirmation_url";
        String mailFrom = "no-reply@chalender.ch";
        String mailFromName = "chalender.ch";

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(emailAddress);
        email.setSubject(MAIL_SUBJECT);
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", emailAddress);
        ctx.setVariable("name", "userName");
        ctx.setVariable("springLogo", SPRING_LOGO_IMAGE);
        ctx.setVariable("url", confirmationUrl);

        final String textContent = this.templateEngine.process("email-user/confirm-email.txt", ctx);
        final String htmlContent = this.templateEngine.process("email-user/confirm-email.html", ctx);

        email.setText( textContent, htmlContent);

        ClassPathResource clr = new ClassPathResource(SPRING_LOGO_IMAGE);

        email.addInline("springLogo", clr, PNG_MIME);

        mailSender.send(mimeMessage);

        Map<String, String> body = new HashMap<>();
        body.put("message", "User created successfully.");
    }
}
