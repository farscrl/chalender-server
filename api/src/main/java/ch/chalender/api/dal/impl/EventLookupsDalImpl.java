package ch.chalender.api.dal.impl;

import ch.chalender.api.dal.EventLookupsDal;
import ch.chalender.api.model.EventFilter;
import ch.chalender.api.model.EventLookup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventLookupsDalImpl implements EventLookupsDal {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<EventLookup> getAllEvents(EventFilter filter, Pageable pageable) {
        Criteria criteria = new Criteria();
        if (filter.getGenres() != null && !filter.getGenres().isEmpty()) {
            criteria = criteria.and("genres._id").in(filter.getGenres());
        }
        if (filter.getRegions() != null && !filter.getRegions().isEmpty()) {
            criteria = criteria.and("regions._id").in(filter.getRegions());
        }
        if (filter.getEventLanguages() != null && !filter.getEventLanguages().isEmpty()) {
            criteria = criteria.and("eventLanguages._id").in(filter.getEventLanguages());
        }
        if (filter.getDate() != null) {
            criteria = criteria.and("date").gte(filter.getDate());
        }
        if (filter.getSearchTerm() != null) {
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(filter.getSearchTerm(), "i"),
                    Criteria.where("location").regex(filter.getSearchTerm(), "i")
            );
        }

        Query query = new Query(criteria);
        List<EventLookup> events = mongoTemplate.find(query, EventLookup.class);

        return PageableExecutionUtils.getPage(
                events,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), EventLookup.class));
    }
}
