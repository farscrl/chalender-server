package ch.chalender.api.controller.all;

import ch.chalender.api.dto.ContactFormDto;
import ch.chalender.api.service.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping("/api/contact")
@Tag(name = "Contact form", description = "Contact form API")
public class ContactFormController {
    @Autowired
    private EmailService emailService;

    @PostMapping("contact-form")
    @PageableAsQueryParam
    @PreAuthorize("permitAll()")
    public ResponseEntity<ContactFormDto> sendContactForm(@Valid @RequestBody ContactFormDto contactFormDto) throws MessagingException, UnsupportedEncodingException {
        emailService.sendContactForm(contactFormDto);
        return ResponseEntity.ok(contactFormDto);
    }
}
