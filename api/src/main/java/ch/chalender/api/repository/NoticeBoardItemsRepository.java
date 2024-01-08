package ch.chalender.api.repository;

import ch.chalender.api.model.NoticeBoardItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoticeBoardItemsRepository extends MongoRepository<NoticeBoardItem, String> {
    Page<NoticeBoardItem> findByOwnerEmail(String ownerEmail, Pageable pageable);
}
