package ch.chalender.api.converter;

import ch.chalender.api.dto.EventDto;
import ch.chalender.api.model.Event;
import ch.chalender.api.model.EventVersion;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EventConverter {

    public static EventDto toEventDto(ModelMapper modelMapper, Event event) {
        if (event == null) {
            return null;
        }

        EventVersion eventVersion = null;

        switch (event.getPublicationStatus()) {
            case DRAFT:
                eventVersion = event.getDraft();
                break;

            case IN_REVIEW:
            case NEW_MODIFICATION:
                eventVersion = event.getWaitingForReview();
                break;

            case REJECTED:
                eventVersion = event.getRejected();
                break;

            case PUBLISHED:
                eventVersion = event.getCurrentlyPublished();
                break;
        }

        if (eventVersion == null) {
            return null;
        }
        EventDto eventDto = modelMapper.map(eventVersion, EventDto.class);
        eventDto.setId(event.getId());
        eventDto.setStatus(event.getPublicationStatus());
        eventDto.setContactEmail(event.getOwnerEmail());

        return eventDto;
    }

    public static List<EventDto> toEventDtoList(ModelMapper modelMapper, List<Event> events) {
        return events.stream()
                .map(event -> toEventDto(modelMapper, event))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static EventVersion toEventVersion(ModelMapper modelMapper, EventDto eventDto) {
        if (eventDto == null) {
            return null;
        }

        EventVersion eventVersion = modelMapper.map(eventDto, EventVersion.class);

        return eventVersion;
    }
}
