package ch.chalender.api.dal.impl;

import ch.chalender.api.dal.EventsDal;
import ch.chalender.api.model.Event;
import ch.chalender.api.model.ModerationEventsFilter;
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
public class EventsDalImpl implements EventsDal {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Event> getAllEvents(ModerationEventsFilter filter, Pageable pageable) {
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

        return getEvents(filter, pageable, criteria);
    }
    
    @Override
    public Page<Event> getAllEventsByUser(ModerationEventsFilter filter, Pageable pageable, String email) {
        Criteria criteria = generateBaseCriteria(filter)
                .and("ownerEmail").is(email);

        return getEvents(filter, pageable, criteria);
    }

    private Page<Event> getEvents(ModerationEventsFilter filter, Pageable pageable, Criteria criteria) {
        String sortField = "lastModifiedDate";
        if (filter.getSortBy() == ModerationEventsFilter.SortBy.TITLE) {
            sortField = "title";
        } else if (filter.getSortBy() == ModerationEventsFilter.SortBy.USER) {
            sortField = "ownerEmail";
        } else if (filter.getSortBy() == ModerationEventsFilter.SortBy.STATE) {
            sortField = "eventStatus";
        } else if (filter.getSortBy() == ModerationEventsFilter.SortBy.MODIFIED_DATE) {
            sortField = "lastModifiedDate";
        }
        Sort.Direction sortDirection = Sort.Direction.ASC;
        if (filter.getSortOrder() == ModerationEventsFilter.SortOrder.DESC) {
            sortDirection = Sort.Direction.DESC;
        }

        Query query = new Query(criteria).with(pageable).with(Sort.by(sortDirection, sortField));
        List<Event> events = mongoTemplate.find(query, Event.class);

        return PageableExecutionUtils.getPage(
                events,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Event.class));
    }

    private Criteria generateBaseCriteria(ModerationEventsFilter filter) {
        Criteria criteria = new Criteria();

        if (filter.getSearchTerm() != null) {
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(filter.getSearchTerm(), "i"),
                    Criteria.where("ownerEmail").regex(filter.getSearchTerm(), "i")
            );
        }

        if (filter.getDates() == ModerationEventsFilter.DatesDisplay.FUTURE) {
            criteria = criteria.and("lastOccurrenceDate").gte(LocalDateTime.now().minusDays(1));
        } else if (filter.getDates() == ModerationEventsFilter.DatesDisplay.PAST) {
            criteria = criteria.and("firstOccurrenceDate").lte(LocalDateTime.now().plusDays(1));
        }

        return criteria;
    }
}
