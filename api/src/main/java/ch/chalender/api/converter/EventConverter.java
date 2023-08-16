package ch.chalender.api.converter;

import ch.chalender.api.dto.EventDto;
import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventVersion;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EventConverter {

    public static EventDto toEventDto(ModelMapper modelMapper, Event event, EventVersionSelection eventVersionSelection) {
        if (event == null) {
            return null;
        }

        EventVersion eventVersion = null;
        if (eventVersionSelection == EventVersionSelection.CURRENTLY_PUBLISHED) {
            eventVersion = event.getCurrentlyPublished();
        } else if (eventVersionSelection == EventVersionSelection.DRAFT) {
            eventVersion = event.getDraft();
        } else if (eventVersionSelection == EventVersionSelection.WAITING_FOR_REVIEW) {
            eventVersion = event.getWaitingForReview();
        }
        if (eventVersion == null) {
            return null;
        }
        EventDto eventDto = modelMapper.map(eventVersion, EventDto.class);
        eventDto.setId(event.getId());
        eventDto.setStatus(event.getEventStatus());

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
        WAITING_FOR_REVIEW
    }
}
