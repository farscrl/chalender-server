package ch.chalender.api.controller.admin;

import ch.chalender.api.dto.EventDto;
import ch.chalender.api.model.EventGenre;
import ch.chalender.api.model.EventLanguage;
import ch.chalender.api.model.EventRegion;
import ch.chalender.api.model.User;
import ch.chalender.api.service.FixturesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/fixtures")
@Tag(name = "Fixtures", description = "Load fixtures")
@PreAuthorize("hasRole('ADMIN')")
public class FixturesController {

    @Autowired
    private FixturesService fixturesService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> loadUserFixtures() throws IOException {
        fixturesService.loadUserFixtures();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/genres")
    public ResponseEntity<List<EventGenre>> loadGenresFixtures() throws IOException {
        fixturesService.loadEventGenreFixtures();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/regions")
    public ResponseEntity<List<EventRegion>> loadRegionsFixtures() throws IOException {
        fixturesService.loadEventRegionFixtures();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/languages")
    public ResponseEntity<List<EventLanguage>> loadLanguagesFixtures() throws IOException {
        fixturesService.loadEventLanguagesFixtures();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> loadEventFixtures() throws IOException {
        fixturesService.loadEventFixtures();
        return ResponseEntity.ok().build();
    }
}
