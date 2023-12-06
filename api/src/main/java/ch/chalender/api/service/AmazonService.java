package ch.chalender.api.service;

import java.io.File;

public interface AmazonService {
    String uploadFile(String fileName, File file, String mimeType);
}
