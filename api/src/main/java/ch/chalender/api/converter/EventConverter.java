package ch.chalender.api.converter;

import ch.chalender.api.dto.EventDto;
import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventVersion;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EventConverter {

    public static Event toEvent(ModelMapper modelMapper, EventDto eventDto) {
        EventVersion eventVersion = modelMapper.map(eventDto, EventVersion.class);
        Event event = new Event();
        event.setDraft(eventVersion);

        return event;
    }

    public static EventDto toEventDto(ModelMapper modelMapper, Event event, EventVersionSelection eventVersionSelection) {
        EventVersion eventVersion = null;
        if (eventVersionSelection == EventVersionSelection.CURRENTLY_PUBLISHED) {
            eventVersion = event.getCurrentlyPublished();
        } else if (eventVersionSelection == EventVersionSelection.DRAFT) {
            eventVersion = event.getDraft();
        } else if (eventVersionSelection == EventVersionSelection.LAST_REVIEWED) {
            eventVersion = event.getLastReviewed();
        }
        if (eventVersion == null) {
            return null;
        }
        EventDto eventDto = modelMapper.map(eventVersion, EventDto.class);
        eventDto.setId(event.getId());

        return eventDto;
    }

    public static List<EventDto> toEventDtoList(ModelMapper modelMapper, List<Event> events, EventVersionSelection eventVersionSelection) {
        return events.stream()
                .map(event -> toEventDto(modelMapper, event, eventVersionSelection))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public enum EventVersionSelection {
        CURRENTLY_PUBLISHED,
        DRAFT,
        LAST_REVIEWED
    }
}
