package ch.chalender.api.service;

import ch.chalender.api.dto.ModerationComment;
import ch.chalender.api.dto.NoticeBoardItemDto;
import ch.chalender.api.model.*;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.UnsupportedEncodingException;

public interface NoticeBoardService {
    public NoticeBoardItem createNoticeBoardItem(NoticeBoardItem item);
    public NoticeBoardItem updateNoticeBoardItem(NoticeBoardItem item);
    public NoticeBoardItem getNoticeBoardItem(String id);
    public Page<NoticeBoardItemDto> getAllNoticeBoardEntries(NoticeBoardFilter filter, Pageable pageable);
    public Page<NoticeBoardItem> listAllNoticeBoardItem(ModerationNoticeBoardFilter filter, Pageable pageable);
    public Page<NoticeBoardItem> listAllNoticeBoardItemsByUser(ModerationNoticeBoardFilter filter, User user, Pageable pageable);
    public NoticeBoardItem acceptChanges(String id, ModerationComment moderationComment) throws RuntimeException, MessagingException, UnsupportedEncodingException;
    public NoticeBoardItem refuseChanges(String id, ModerationComment moderationComment) throws RuntimeException, MessagingException, UnsupportedEncodingException;
    public NoticeBoardItem changeAndPublish(String id, NoticeBoardItemVersion noticeBoardItemVersion) throws RuntimeException, MessagingException, UnsupportedEncodingException;
    public void deleteNoticeBoardItem(String id);
}
