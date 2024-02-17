package ch.chalender.api.controller.all;

import ch.chalender.api.model.EventGenre;
import ch.chalender.api.model.EventLanguage;
import ch.chalender.api.model.EventRegion;
import ch.chalender.api.repository.EventGenresRepository;
import ch.chalender.api.repository.EventLanguagesRepository;
import ch.chalender.api.repository.EventRegionsRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/api/static")
@Tag(name = "Static", description = "Static data")
public class StaticDataController {

    @Autowired
    private EventGenresRepository eventGenresRepository;

    @Autowired
    private EventRegionsRepository eventRegionsRepository;

    @Autowired
    private EventLanguagesRepository eventLanguagesRepository;

    @GetMapping("/genres")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<EventGenre>> listGenres() {
        List<EventGenre> genres = eventGenresRepository.findByIsHiddenIsFalse(false);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
                .body(genres);
    }

    @GetMapping("/regions")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<EventRegion>> listRegions() {
        List<EventRegion> regions = eventRegionsRepository.findByIsHiddenIsFalse(false);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
                .body(regions);
    }

    @GetMapping("/languages")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<EventLanguage>> listLanguages() {
        List<EventLanguage> regions = eventLanguagesRepository.findByIsHiddenIsFalse(false);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
                .body(regions);
    }
}
