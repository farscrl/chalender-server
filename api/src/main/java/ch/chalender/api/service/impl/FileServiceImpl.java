package ch.chalender.api.service.impl;

import ch.chalender.api.model.Document;
import ch.chalender.api.model.Image;
import ch.chalender.api.repository.DocumentsRepository;
import ch.chalender.api.repository.ImagesRepository;
import ch.chalender.api.service.AmazonService;
import ch.chalender.api.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class FileServiceImpl implements FileService {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    @Autowired
    private AmazonService amazonService;

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private DocumentsRepository documentsRepository;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    private String[] allowedMimeTypesImages = {"image/jpeg","image/png","image/webp"};
    private String[] allowedMimeTypesDocuments = {"application/pdf"};

    @Override
    public Image uploadImage(MultipartFile multipartFile) {
        try {
            File file = convertMultiPartToFile(multipartFile);

            Tika tika = new Tika();
            String mimeType = tika.detect(file);
            if (Arrays.stream(allowedMimeTypesImages).noneMatch(mimeType::equals)) {
                throw new RuntimeException("File is not an image");
            }

            String fileName = toSlug(FilenameUtils.getBaseName(multipartFile.getOriginalFilename()));
            String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            Image img = new Image();
            img.setOriginalName(fileName + "." + fileExtension);
            img = imagesRepository.save(img);
            String path = activeProfile + "/" + img.getId() + "." + fileExtension;

            amazonService.uploadFile(path, file, mimeType);
            img.setPath(path);
            img = imagesRepository.save(img);
            boolean result = file.delete();
            if (!result) {
                logger.warn("Could not delete file: " + file.getAbsolutePath());
            }

            return img;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Document uploadDocument(MultipartFile multipartFile) {
        try {
            File file = convertMultiPartToFile(multipartFile);

            Tika tika = new Tika();
            String mimeType = tika.detect(file);
            if (Arrays.stream(allowedMimeTypesDocuments).noneMatch(mimeType::equals)) {
                throw new RuntimeException("File is not a document");
            }

            String fileName = toSlug(FilenameUtils.getBaseName(multipartFile.getOriginalFilename()));
            String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            Document doc = new Document();
            doc.setOriginalName(fileName + "." + fileExtension);
            doc = documentsRepository.save(doc);
            String path = activeProfile + "/" + doc.getId() + "." + fileExtension;

            amazonService.uploadFile(path, file, mimeType);
            doc.setPath(path);
            doc = documentsRepository.save(doc);
            boolean result = file.delete();
            if (!result) {
                logger.warn("Could not delete file: " + file.getAbsolutePath());
            }

            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = File.createTempFile("image_", "");
        logger.warn("File name: " + convFile.getName());
        logger.warn("File path: " + convFile.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String toSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
