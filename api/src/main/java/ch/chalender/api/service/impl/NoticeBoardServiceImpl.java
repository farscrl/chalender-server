package ch.chalender.api.service.impl;

import ch.chalender.api.converter.NoticeBoardItemConverter;
import ch.chalender.api.dal.NoticeBoardItemsDal;
import ch.chalender.api.dto.ModerationComment;
import ch.chalender.api.dto.NoticeBoardItemDto;
import ch.chalender.api.model.*;
import ch.chalender.api.repository.NoticeBoardItemsRepository;
import ch.chalender.api.service.EmailService;
import ch.chalender.api.service.NoticeBoardService;
import ch.chalender.api.service.SubscriptionSendingService;
import ch.chalender.api.service.UserService;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class NoticeBoardServiceImpl implements NoticeBoardService {

    @Autowired
    private NoticeBoardItemsRepository noticeBoardItemsRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private NoticeBoardItemsDal noticeBoardItemsDal;

    @Autowired
    private SubscriptionSendingService subscriptionSendingService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public NoticeBoardItem createNoticeBoardItem(NoticeBoardItem item) {
        item = noticeBoardItemsRepository.save(item);

        if (item.getPublicationStatus() == PublicationStatus.IN_REVIEW) {
            try {
                emailService.sendNoticeBoardModeratorEmail(item);
            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return item;
    }

    @Override
    public NoticeBoardItem updateNoticeBoardItem(NoticeBoardItem item) {
        item = noticeBoardItemsRepository.save(item);
        if (item.getPublicationStatus() == PublicationStatus.IN_REVIEW || item.getPublicationStatus() == PublicationStatus.NEW_MODIFICATION) {
            try {
                emailService.sendNoticeBoardModeratorEmail(item);
            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return item;
    }

    @Override
    public NoticeBoardItem getNoticeBoardItem(String id) {
        return noticeBoardItemsRepository.findById(id).orElse(null);
    }

    @Override
    public Page<NoticeBoardItemDto> getAllNoticeBoardEntries(NoticeBoardFilter filter, Pageable pageable) {
        Page<NoticeBoardItem> results = noticeBoardItemsDal.getAllNoticeBoardItems(filter, pageable);
        return results.map(n -> NoticeBoardItemConverter.toNoticeBoardItemDto(modelMapper, n));
    }

    @Override
    public Page<NoticeBoardItem> listAllNoticeBoardItem(ModerationNoticeBoardFilter filter, Pageable pageable) {
        return noticeBoardItemsDal.getAllNoticeBoardItems(filter, pageable);
    }
    @Override
    public Page<NoticeBoardItem> listAllNoticeBoardItemsByUser(ModerationNoticeBoardFilter filter, User user, Pageable pageable) {
        return noticeBoardItemsDal.getAllNoticeBoardItemsByUser(filter, pageable, user.getEmail());
    }

    @Override
    public NoticeBoardItem acceptChanges(String id, ModerationComment moderationComment) throws RuntimeException, MessagingException, UnsupportedEncodingException {
        NoticeBoardItem item = noticeBoardItemsRepository.findById(id).orElse(null);
        if (item == null) {
            throw new RuntimeException("Notice Board item not found");
        }

        boolean isNew = item.getCurrentlyPublished() == null;

        item.setCurrentlyPublished(item.getWaitingForReview());
        item.setWaitingForReview(null);
        item.setRejected(null);
        item.setDraft(null);
        item.updateCalculatedFields();
        item = noticeBoardItemsRepository.save(item);

        User user = userService.findUserByEmail(item.getOwnerEmail());
        if (isNew) {
            emailService.sendNoticeBoardPublishedEmail(item.getOwnerEmail(), user != null ? user.getFullName() : null, item, moderationComment.getComment());
            this.subscriptionSendingService.notifyUsersAboutNewNoticeBoardItem(item);
            return item;
        }
        emailService.sendNoticeBoardUpdateAcceptedEmail(item.getOwnerEmail(), user != null ? user.getFullName() : null, item, moderationComment.getComment());
        return item;
    }

    @Override
    public NoticeBoardItem refuseChanges(String id, ModerationComment moderationComment) throws RuntimeException, MessagingException, UnsupportedEncodingException {
        NoticeBoardItem item = noticeBoardItemsRepository.findById(id).orElse(null);
        if (item == null) {
            throw new RuntimeException("Item not found");
        }

        if (item.getCurrentlyPublished() != null) {
            item.setWaitingForReview(null);
            item.updateCalculatedFields();

            item = noticeBoardItemsRepository.save(item);

            User user = userService.findUserByEmail(item.getOwnerEmail());
            emailService.sendNoticeBoardUpdateRefusedEmail(item.getOwnerEmail(), user != null ? user.getFullName() : null, item, moderationComment.getComment());

            return item;
        }
        item.setRejected(item.getWaitingForReview());
        item.setWaitingForReview(null);
        item.updateCalculatedFields();

        item = noticeBoardItemsRepository.save(item);

        User user = userService.findUserByEmail(item.getOwnerEmail());
        emailService.sendNoticeBoardRefusedEmail(item.getOwnerEmail(), user != null ? user.getFullName() : null, item, moderationComment.getComment());

        return item;
    }

    @Override
    public NoticeBoardItem changeAndPublish(String id, NoticeBoardItemVersion noticeBoardItemVersion) throws RuntimeException, MessagingException, UnsupportedEncodingException {
        NoticeBoardItem item = noticeBoardItemsRepository.findById(id).orElse(null);
        if (item == null) {
            throw new RuntimeException("Item not found");
        }

        boolean isNew = item.getCurrentlyPublished() == null;
        boolean isUpdate = item.getCurrentlyPublished() != null && item.getWaitingForReview() != null;

        item.setCurrentlyPublished(noticeBoardItemVersion);
        item.setWaitingForReview(null);
        item.setRejected(null);
        item.setDraft(null);
        item.updateCalculatedFields();

        item = noticeBoardItemsRepository.save(item);

        User user = userService.findUserByEmail(item.getOwnerEmail());
        if (isNew) {
            emailService.sendNoticeBoardPublishedEmail(item.getOwnerEmail(), user != null ? user.getFullName() : null, item, noticeBoardItemVersion.getModerationComment().getComment());
            return item;
        }
        if (isUpdate) {
            emailService.sendNoticeBoardUpdateAcceptedEmail(item.getOwnerEmail(), user != null ? user.getFullName() : null, item, noticeBoardItemVersion.getModerationComment().getComment());
            return item;
        }

        return item;
    }

    @Override
    public void deleteNoticeBoardItem(String id) {
        noticeBoardItemsRepository.deleteById(id);
    }
}
