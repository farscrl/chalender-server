package ch.chalender.api.dal;

import ch.chalender.api.model.User;
import ch.chalender.api.model.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsersDal {
    public Page<User> getAllUsers(UserFilter filter, Pageable pageable);
}
