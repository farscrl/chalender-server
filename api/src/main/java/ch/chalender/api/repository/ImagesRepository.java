package ch.chalender.api.repository;

import ch.chalender.api.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImagesRepository extends MongoRepository<Image, String> {
}
