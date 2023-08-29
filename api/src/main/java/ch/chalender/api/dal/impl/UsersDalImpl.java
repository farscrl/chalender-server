package ch.chalender.api.dal.impl;

import ch.chalender.api.dal.UsersDal;
import ch.chalender.api.model.User;
import ch.chalender.api.model.UserFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsersDalImpl implements UsersDal {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<User> getAllUsers(UserFilter filter, Pageable pageable) {
        Criteria criteria = new Criteria();
        if (filter.getSearchTerm() != null) {
            criteria = criteria.orOperator(
                    Criteria.where("firstName").regex(filter.getSearchTerm(), "i"),
                    Criteria.where("lastName").regex(filter.getSearchTerm(), "i"),
                    Criteria.where("email").regex(filter.getSearchTerm(), "i")
            );
        }

        Query query = new Query(criteria).with(pageable).with(Sort.by(Sort.Direction.ASC, "email"));
        List<User> users = mongoTemplate.find(query, User.class);

        return PageableExecutionUtils.getPage(
                users,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), User.class));
    }
}
