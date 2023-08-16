package ch.chalender.api.controller.admin;

import ch.chalender.api.dto.EventDto;
import ch.chalender.api.fixtures.EventFixtures;
import ch.chalender.api.repository.EventGenresRepository;
import ch.chalender.api.repository.EventLanguagesRepository;
import ch.chalender.api.repository.EventRegionsRepository;
import ch.chalender.api.repository.EventsRepository;
import ch.chalender.api.service.EventLookupService;
import ch.chalender.api.service.EventsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;

    @Autowired
    private EventsService eventsService;

    @Autowired
    private EventRegionsRepository eventRegionsRepository;

    @Autowired
    private EventGenresRepository eventGenresRepository;

    @Autowired
    private EventLanguagesRepository eventLanguagesRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private EventFixtures eventFixtures;

    @Autowired
    private EventLookupService eventLookupService;

    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> loadEventFixtures() throws IOException {
        eventsRepository.deleteAll();
        eventsRepository.saveAll(eventFixtures.getEvents());
        eventLookupService.recreateAllEventLookupData();

        return ResponseEntity.ok().build();
    }


}
