package ch.chalender.api.dal.impl;

import ch.chalender.api.dal.NoticeBoardItemsDal;
import ch.chalender.api.model.ModerationNoticeBoardFilter;
import ch.chalender.api.model.NoticeBoardFilter;
import ch.chalender.api.model.NoticeBoardItem;
import ch.chalender.api.model.PublicationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NoticeBoardItemsDalImpl implements NoticeBoardItemsDal {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<NoticeBoardItem> getAllNoticeBoardItems(NoticeBoardFilter filter, Pageable pageable) {
        Criteria criteria = new Criteria();
        if (filter.getGenres() != null && !filter.getGenres().isEmpty()) {
            criteria = criteria.and("genres._id").in(filter.getGenres());
        }
        if (filter.getSearchTerm() != null) {
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(filter.getSearchTerm(), "i"),
                    Criteria.where("currentlyPublished.description").regex(filter.getSearchTerm(), "i"),
                    Criteria.where("currentlyPublished.contactData").regex(filter.getSearchTerm(), "i")
            );
        }

        Query query = new Query(criteria).with(pageable).with(Sort.by(Sort.Direction.ASC, "date", "start"));
        List<NoticeBoardItem> items = mongoTemplate.find(query, NoticeBoardItem.class);

        return PageableExecutionUtils.getPage(
                items,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), NoticeBoardItem.class));
    }

    @Override
    public Page<NoticeBoardItem> getAllNoticeBoardItems(ModerationNoticeBoardFilter filter, Pageable pageable) {
        Criteria criteria = generateBaseCriteria(filter);

        List<PublicationStatus> eventStates = new ArrayList<>();
        if (filter.isIncludeStateInReview()) {
            eventStates.add(PublicationStatus.IN_REVIEW);
        }
        if (filter.isIncludeStateNewModification()) {
            eventStates.add(PublicationStatus.NEW_MODIFICATION);
        }
        if (filter.isIncludeStatePublished()) {
            eventStates.add(PublicationStatus.PUBLISHED);
        }
        if (filter.isIncludeStateRejected()) {
            eventStates.add(PublicationStatus.REJECTED);
        }
        if (filter.isIncludeStateInvalid()) {
            eventStates.add(PublicationStatus.INVALID);
        }
        criteria =  criteria.and("eventStatus").in(eventStates);

        return getItems(filter, pageable, criteria);
    }
    
    @Override
    public Page<NoticeBoardItem> getAllNoticeBoardItemsByUser(ModerationNoticeBoardFilter filter, Pageable pageable, String email) {
        Criteria criteria = generateBaseCriteria(filter)
                .and("ownerEmail").is(email);

        return getItems(filter, pageable, criteria);
    }

    private Page<NoticeBoardItem> getItems(ModerationNoticeBoardFilter filter, Pageable pageable, Criteria criteria) {
        String sortField = "lastModifiedDate";
        if (filter.getSortBy() == ModerationNoticeBoardFilter.SortBy.TITLE) {
            sortField = "title";
        } else if (filter.getSortBy() == ModerationNoticeBoardFilter.SortBy.USER) {
            sortField = "ownerEmail";
        } else if (filter.getSortBy() == ModerationNoticeBoardFilter.SortBy.STATE) {
            sortField = "eventStatus";
        } else if (filter.getSortBy() == ModerationNoticeBoardFilter.SortBy.MODIFIED_DATE) {
            sortField = "lastModifiedDate";
        }
        Sort.Direction sortDirection = Sort.Direction.ASC;
        if (filter.getSortOrder() == ModerationNoticeBoardFilter.SortOrder.DESC) {
            sortDirection = Sort.Direction.DESC;
        }

        Query query = new Query(criteria).with(pageable).with(Sort.by(sortDirection, sortField));
        List<NoticeBoardItem> items = mongoTemplate.find(query, NoticeBoardItem.class);

        return PageableExecutionUtils.getPage(
                items,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), NoticeBoardItem.class));
    }

    private Criteria generateBaseCriteria(ModerationNoticeBoardFilter filter) {
        Criteria criteria = new Criteria();

        if (filter.getSearchTerm() != null) {
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(filter.getSearchTerm(), "i"),
                    Criteria.where("ownerEmail").regex(filter.getSearchTerm(), "i")
            );
        }

        if (filter.getDates() == ModerationNoticeBoardFilter.DatesDisplay.FUTURE) {
            criteria = criteria.and("lastOccurrenceDate").gte(LocalDateTime.now().minusDays(1));
        } else if (filter.getDates() == ModerationNoticeBoardFilter.DatesDisplay.PAST) {
            criteria = criteria.and("firstOccurrenceDate").lte(LocalDateTime.now().plusDays(1));
        }

        return criteria;
    }
}
