package ch.chalender.api.converter;

import ch.chalender.api.dto.EventDto;
import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventVersion;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class EventConverter {

    public static Event toEvent(ModelMapper modelMapper, EventDto eventDto) {
        EventVersion eventVersion = modelMapper.map(eventDto, EventVersion.class);
        Event event = new Event();
        event.setDraft(eventVersion);

        return event;
    }

    public static EventDto toEventDto(ModelMapper modelMapper, Event event) {
        EventVersion eventVersion = event.getDraft();
        EventDto eventDto = modelMapper.map(eventVersion, EventDto.class);
        eventDto.setId(event.getId());

        return eventDto;
    }

    public static List<EventDto> toEventDtoList(ModelMapper modelMapper, List<Event> events) {
        return events.stream()
                .map(event -> toEventDto(modelMapper, event))
                .collect(Collectors.toList());
    }
}
