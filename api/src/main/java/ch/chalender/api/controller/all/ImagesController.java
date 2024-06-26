package ch.chalender.api.controller.all;

import ch.chalender.api.model.Image;
import ch.chalender.api.repository.ImagesRepository;
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
@RequestMapping("/api/images")
@Tag(name = "Images", description = "Images API")
public class ImagesController {

    @Autowired
    private FileService fileService;

    @Autowired
    private ImagesRepository imagesRepository;

    @PostMapping("")
    @PageableAsQueryParam
    @PreAuthorize("permitAll()")
    public ResponseEntity<Image> uploadImage(@RequestParam("image") MultipartFile file) {
        Image img = fileService.uploadImage(file);
        return ResponseEntity.ok(img);
    }

    @PostMapping("/{id}/unlink")
    @PageableAsQueryParam
    @PreAuthorize("permitAll()")
    public ResponseEntity<Image> unlinkImage(@PathVariable String id) {
        Image img = imagesRepository.findById(id).orElseThrow();
        img.setUsed(false);
        imagesRepository.save(img);
        return ResponseEntity.ok(img);
    }
}
