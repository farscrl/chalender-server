package ch.chalender.api.service;

import ch.chalender.api.model.Document;
import ch.chalender.api.model.Image;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Image uploadImage(MultipartFile multipartFile);

    Document uploadDocument(MultipartFile multipartFile);
}
