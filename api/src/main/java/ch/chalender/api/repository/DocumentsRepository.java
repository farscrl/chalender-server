package ch.chalender.api.repository;

import ch.chalender.api.model.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentsRepository extends MongoRepository<Document, String> {
}
