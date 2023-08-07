package ch.chalender.api.controller.all;

import ch.chalender.api.converter.EventConverter;
import ch.chalender.api.dto.EventDto;
import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventFilter;
import ch.chalender.api.service.EventsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/events")
@Tag(name = "Events", description = "List events")
public class EventsController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EventsService eventsService;

    @GetMapping("")
    public ResponseEntity<List<EventDto>> listAllEvents() {
        List<Event> events = eventsService.getPublicEvents(new EventFilter());
        return ResponseEntity.ok(EventConverter.toEventDtoList(modelMapper, events));
    }

    @PostMapping("")
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventDto eventDto) {
        Event eventToCreate = EventConverter.toEvent(modelMapper, eventDto);
        Event event = eventsService.createEvent(eventToCreate);
        return ResponseEntity.ok(EventConverter.toEventDto(modelMapper, event));
    }
}
