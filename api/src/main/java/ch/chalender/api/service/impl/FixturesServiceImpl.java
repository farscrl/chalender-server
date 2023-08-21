package ch.chalender.api.service.impl;

import ch.chalender.api.fixtures.EventFixtures;
import ch.chalender.api.model.EventGenre;
import ch.chalender.api.model.EventLanguage;
import ch.chalender.api.model.EventRegion;
import ch.chalender.api.repository.EventGenresRepository;
import ch.chalender.api.repository.EventLanguagesRepository;
import ch.chalender.api.repository.EventRegionsRepository;
import ch.chalender.api.repository.EventsRepository;
import ch.chalender.api.service.EventLookupService;
import ch.chalender.api.service.FixturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FixturesServiceImpl implements FixturesService  {

    @Autowired
    private EventGenresRepository genresRepository;

    @Autowired
    private EventRegionsRepository regionsRepository;

    @Autowired
    private EventLanguagesRepository languagesRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private EventFixtures eventFixtures;

    @Autowired
    private EventLookupService eventLookupService;

    public void loadEventGenreFixtures() {
        List<EventGenre> genres = new ArrayList<>(List.of(
                new EventGenre(1, "concert", 1, false),
                new EventGenre(2, "teater", 2, false),
                new EventGenre(3, "saut", 3, false),
                new EventGenre(4, "film", 4, false),
                new EventGenre(5, "art", 5, false),
                new EventGenre(6, "litteratura", 6, false),
                new EventGenre(7, "curs", 7, false),
                new EventGenre(8, "chor", 8, false),
                new EventGenre(9, "exposiziun", 9, false),
                new EventGenre(10, "politica", 10, false),
                new EventGenre(11, "annunzia", 11, false),
                new EventGenre(12, "divers", 12, false)
        ));

        genresRepository.deleteAll();
        genresRepository.saveAll(genres);
    }


    public void loadEventRegionFixtures() {
        List<EventRegion> regions = new ArrayList<>(List.of(
                new EventRegion(1, "Surselva", 1, false),
                new EventRegion(2, "Grischun central", 2, false),
                new EventRegion(3, "Engiadina", 3, false),
                new EventRegion(4, "Val dal Rain / Cuira", 4, false),
                new EventRegion(5, "Grischun talian", 5, false),
                new EventRegion(6, "Tavau / Partenz", 6, false),
                new EventRegion(7, "regiun Turitg", 7, false),
                new EventRegion(8, "regiun Berna", 8, false),
                new EventRegion(9, " regiun Basilea", 9, false),
                new EventRegion(10, "svizra orientala", 10, false),
                new EventRegion(11, "svizra franzosa", 11, false),
                new EventRegion(12, "svizra taliana", 12, false),
                new EventRegion(13, "auter", 13, false)
        ));

        regionsRepository.deleteAll();
        regionsRepository.saveAll(regions);
    }


    public void loadEventLanguagesFixtures() {
        List<EventLanguage> languages = new ArrayList<>(List.of(
                new EventLanguage("rm", "rumantsch", 1, false),
                new EventLanguage("de", "tudestg", 2, false),
                new EventLanguage("it", "talian", 3, false),
                new EventLanguage("fr", "franzos", 4, false),
                new EventLanguage("en", "englais", 5, false),
                new EventLanguage("xx", "auter", 6, false)
        ));

        languagesRepository.deleteAll();
        languagesRepository.saveAll(languages);
    }


    @Override
    public void loadEventFixtures() {
        eventsRepository.deleteAll();
        eventsRepository.saveAll(eventFixtures.getEvents());
        eventLookupService.recreateAllEventLookupData();
    }
}
