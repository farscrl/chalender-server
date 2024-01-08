package ch.chalender.api.converter;

import ch.chalender.api.dto.NoticeBoardItemDto;
import ch.chalender.api.model.NoticeBoardItem;
import ch.chalender.api.model.NoticeBoardItemVersion;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NoticeBoardItemConverter {

    public static NoticeBoardItemDto toNoticeBoardItemDto(ModelMapper modelMapper, NoticeBoardItem item) {
        if (item == null) {
            return null;
        }

        NoticeBoardItemVersion itemVersion = null;

        switch (item.getPublicationStatus()) {
            case DRAFT:
                itemVersion = item.getDraft();
                break;

            case IN_REVIEW:
            case NEW_MODIFICATION:
                itemVersion = item.getWaitingForReview();
                break;

            case REJECTED:
                itemVersion = item.getRejected();
                break;

            case PUBLISHED:
                itemVersion = item.getCurrentlyPublished();
                break;
        }

        if (itemVersion == null) {
            return null;
        }
        NoticeBoardItemDto itemDto = modelMapper.map(itemVersion, NoticeBoardItemDto.class);
        itemDto.setId(item.getId());
        itemDto.setStatus(item.getPublicationStatus());
        // itemDto.setContactEmail(item.getOwnerEmail());
        itemDto.setPublicationDate(LocalDate.ofInstant(item.getCreatedDate(), ZoneId.of("Europe/Zurich")));

        return itemDto;
    }

    public static List<NoticeBoardItemDto> toNoticeBoardItemDtoList(ModelMapper modelMapper, List<NoticeBoardItem> items) {
        return items.stream()
                .map(item -> toNoticeBoardItemDto(modelMapper, item))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static NoticeBoardItemVersion toNoticeBoardItemVersion(ModelMapper modelMapper, NoticeBoardItemDto itemDto) {
        if (itemDto == null) {
            return null;
        }

        return modelMapper.map(itemDto, NoticeBoardItemVersion.class);
    }
}
