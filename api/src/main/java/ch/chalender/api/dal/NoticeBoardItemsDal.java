package ch.chalender.api.dal;

import ch.chalender.api.model.ModerationNoticeBoardFilter;
import ch.chalender.api.model.NoticeBoardFilter;
import ch.chalender.api.model.NoticeBoardItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface NoticeBoardItemsDal {
    Page<NoticeBoardItem> getAllNoticeBoardItems(NoticeBoardFilter filter, Pageable pageable);
    List<NoticeBoardItem> getNoticeBoardItemsInDateRange(NoticeBoardFilter filter, LocalDate start, LocalDate end);
    public Page<NoticeBoardItem> getAllNoticeBoardItems(ModerationNoticeBoardFilter filter, Pageable pageable);
    public Page<NoticeBoardItem> getAllNoticeBoardItemsByUser(ModerationNoticeBoardFilter filter, Pageable pageable, String email);
}
