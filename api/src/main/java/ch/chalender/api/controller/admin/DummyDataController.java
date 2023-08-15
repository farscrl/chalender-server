package ch.chalender.api.controller.admin;

import ch.chalender.api.dto.EventDto;
import ch.chalender.api.model.*;
import ch.chalender.api.repository.EventGenresRepository;
import ch.chalender.api.repository.EventLanguagesRepository;
import ch.chalender.api.repository.EventRegionsRepository;
import ch.chalender.api.repository.EventsRepository;
import ch.chalender.api.service.EventsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/dummy-data")
@Tag(name = "Events", description = "Load dummy data")
@PreAuthorize("hasRole('ADMIN')")
public class DummyDataController {

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

    @GetMapping("")
    public ResponseEntity<List<EventDto>> loadDummyData() throws IOException {
        List<EventRegion> allRegions = eventRegionsRepository.findByIsHiddenIsFalse(false);
        List<EventGenre> allGenres = eventGenresRepository.findByIsHiddenIsFalse(false);
        List<EventLanguage> allLanguages = eventLanguagesRepository.findByIsHiddenIsFalse(false);

        ClassPathResource staticDataResource = new ClassPathResource("dummy-data/events.json");
        String staticDataString = IOUtils.toString(staticDataResource.getInputStream(), StandardCharsets.UTF_8);
        JSONArray array = new JSONArray(staticDataString);

        for(int i = 0; i < array.length(); i++) {
            JSONObject objects = array.getJSONObject(i);
            Map<String, Object> map = objects.toMap();

            EventVersion eventVersion = new EventVersion();
            eventVersion.setTitle((String) map.get("Titel"));
            eventVersion.setDescription((String) map.get("Descripziun"));
            eventVersion.setRegions(getRegions((String) map.get("Regiun"), allRegions));
            eventVersion.setAddress((String) map.get("Adressa"));
            eventVersion.setGenres(getGenres((String) map.get("Gener"), allGenres));
            eventVersion.setEventLanguages(getLanguages((String) map.get("Lingua"), allLanguages));
            eventVersion.setLocation((String) map.get("Lieu"));
            eventVersion.getOccurrences().add(parseDate((String) map.get("Cumenzament"), (String) map.get("Fin")));

            Event event = new Event();
            event.setId(String.valueOf((Integer)map.get("ID")));
            event.setCurrentlyPublished(eventVersion);

            eventsRepository.save(event);
        };

        return ResponseEntity.ok().build();
    }

    private List<EventRegion> getRegions(String region, List<EventRegion> allRegions) {
        List<EventRegion> regions = new ArrayList<>();
        if (region == null || region.isEmpty()) {
            return regions;
        }

        String[] splt = region.split(",");
        for (String s : splt) {
            s = s.trim();
            if (s.equals("Cuira")) {
                s = "Region Cuira";
            }

            String finalS = s.toLowerCase();
            allRegions.forEach(r -> {
                if (r.getName().equals(finalS)) {
                    regions.add(r);
                }
            });
        }

        return regions;
    }

    private List<EventGenre> getGenres(String genre, List<EventGenre> allGenres) {
        List<EventGenre> genres = new ArrayList<>();
        if (genre == null || genre.isEmpty()) {
            return genres;
        }

        String[] splt = genre.split(",");
        for (String s : splt) {
            s = s.trim();

            String finalS = s.toLowerCase();
            allGenres.forEach(r -> {
                if (r.getName().equals(finalS)) {
                    genres.add(r);
                }
            });
        }

        return genres;
    }

    private List<EventLanguage> getLanguages(String language, List<EventLanguage> allLanguages) {
        List<EventLanguage> languages = new ArrayList<>();
        if (language == null || language.isEmpty()) {
            return languages;
        }

        String[] splt = language.split(",");
        for (String s : splt) {
            s = s.trim();

            String finalS = s.toLowerCase();
            allLanguages.forEach(r -> {
                if (r.getName().equals(finalS)) {
                    languages.add(r);
                }
            });
        }

        return languages;
    }

    private EventOccurrence parseDate(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        EventOccurrence eventOccurrences = new EventOccurrence();
        String[] splt = startDate.split(",");
        LocalDate date = LocalDate.parse(splt[0].trim(), formatter);
        eventOccurrences.setDate(date);
        if (splt.length == 2) {
            eventOccurrences.setStart(LocalTime.parse(splt[1].trim()));
        } else {
            eventOccurrences.setAllDay(true);
        }

        if (endDate != null && !endDate.isEmpty()) {
            splt = endDate.split(",");
            if (splt.length == 2) {
                eventOccurrences.setEnd(LocalTime.parse(splt[1].trim()));
            }
        }

        return eventOccurrences;
    }
}
/*
        "Pretsch" -> ""
        "Auter" -> ""
        "Organisatura" -> "ArteCultura"
        "Lingua" -> "tudestg, rumantsch, talian"
        "Funtauna" -> "https://www.chur-kultur.ch/de/agenda-extended/singen-mit-flurin-chur_ATKPZSk?"
        "Maletg" -> "https://ik.imagekit.io/guidle/tr:h-250,c-at_least,dpr-2/1/93/5f/1935f217191e2469746dca9b756a9163265100e5_747711224.jpg"
        "URL" -> "flurincaviezel.ch/"
        "Cumenzament_temp" -> "19:30"
        "Contact" -> "ArteCultura, info@artecultura.ch"
        "Temp_alt" -> "19:30"
        "Durada" -> "02:00"
        "Durada_alt" -> "02:45"
        "Agiuntas" -> ""
        "Prevendita" -> "info@artecultura.ch \noder Tel. 076 375 825 55"
        "ID" -> {Integer@9767} 0
 */