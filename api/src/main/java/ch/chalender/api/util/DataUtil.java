package ch.chalender.api.util;

import ch.chalender.api.model.Document;
import ch.chalender.api.model.Image;
import ch.chalender.api.repository.DocumentsRepository;
import ch.chalender.api.repository.ImagesRepository;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DataUtil {

    /**
     * Update the images in the dto with the ones from the database.
     * This is used, as not all data regarding images is exposed over the API.
     *
     * @param images The list of the images to update
     * @return The updated images-list
     */
    public static List<Image> updateImages(ImagesRepository imagesRepository, List<Image> images) {
        List<Image> imagesFound = new ArrayList<>();

        for (Image image : images) {
            Image imageFound = imagesRepository.findById(image.getId()).orElse(null);
            if (imageFound != null) {
                imagesFound.add(imageFound);
            }
        }

        return imagesFound;
    }

    /**
     * Update the documents in the dto with the ones from the database.
     * This is used, as not all data regarding documents is exposed over the API.
     *
     * @param documents The list of the documents to update
     * @return The updated documents-list
     */
    public static List<Document> updateDocuments(DocumentsRepository documentsRepository, List<Document> documents) {
        List<Document> documentsFound = new ArrayList<>();

        for (Document document : documents) {
            Document documentFound = documentsRepository.findById(document.getId()).orElse(null);
            if (documentFound != null) {
                documentsFound.add(documentFound);
            }
        }

        return documentsFound;
    }

    public static LocalTime convertStringToLocalTime(String timeString) {
        if (timeString == null || timeString.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing time: " + e.getMessage());
            return null;
        }
    }
}
