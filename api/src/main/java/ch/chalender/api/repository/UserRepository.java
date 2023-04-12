package ch.chalender.api.repository;

import ch.chalender.api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    @Query("{email:'?0'}")
    User findByEmail(String email);

    boolean existsByEmail(String email);

    public long count();
}
