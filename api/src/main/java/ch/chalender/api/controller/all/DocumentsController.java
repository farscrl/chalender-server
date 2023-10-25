package ch.chalender.api.controller.all;

import ch.chalender.api.model.Document;
import ch.chalender.api.repository.DocumentsRepository;
import ch.chalender.api.service.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/documents")
@Tag(name = "Documents", description = "Documents API")
public class DocumentsController {

    @Autowired
    private FileService fileService;

    @Autowired
    private DocumentsRepository documentsRepository;

    @PostMapping("")
    @PageableAsQueryParam
    @PreAuthorize("permitAll()")
    public ResponseEntity<Document> uploadImage(@RequestParam("document") MultipartFile file) {
        Document doc = fileService.uploadDocument(file);
        return ResponseEntity.ok(doc);
    }

    @PostMapping("/{id}/unlink")
    @PageableAsQueryParam
    @PreAuthorize("permitAll()")
    public ResponseEntity<Document> unlinkImage(@PathVariable String id) {
        Document doc = documentsRepository.findById(id).orElseThrow();
        doc.setUsed(false);
        documentsRepository.save(doc);
        return ResponseEntity.ok(doc);
    }
}
