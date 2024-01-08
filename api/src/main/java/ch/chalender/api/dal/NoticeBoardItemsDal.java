package ch.chalender.api.dal;

import ch.chalender.api.model.ModerationNoticeBoardFilter;
import ch.chalender.api.model.NoticeBoardFilter;
import ch.chalender.api.model.NoticeBoardItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeBoardItemsDal {
    Page<NoticeBoardItem> getAllNoticeBoardItems(NoticeBoardFilter filter, Pageable pageable);
    public Page<NoticeBoardItem> getAllNoticeBoardItems(ModerationNoticeBoardFilter filter, Pageable pageable);
    public Page<NoticeBoardItem> getAllNoticeBoardItemsByUser(ModerationNoticeBoardFilter filter, Pageable pageable, String email);
}
