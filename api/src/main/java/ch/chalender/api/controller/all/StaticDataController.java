package ch.chalender.api.controller.all;

import ch.chalender.api.model.EventGenre;
import ch.chalender.api.model.EventRegion;
import ch.chalender.api.repository.EventGenresRepository;
import ch.chalender.api.repository.EventRegionsRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/genres")
    public ResponseEntity<List<EventGenre>> listGenres() {
        List<EventGenre> genres = eventGenresRepository.findByIsHiddenIsFalse(false);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS)) // TODO: set to 1 day
                .body(genres);
    }

    @GetMapping("/regions")
    public ResponseEntity<List<EventRegion>> listRegions() {
        List<EventRegion> regions = eventRegionsRepository.findByIsHiddenIsFalse(false);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS)) // TODO: set to 1 day
                .body(regions);
    }
}
