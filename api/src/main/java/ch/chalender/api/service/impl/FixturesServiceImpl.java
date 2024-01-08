package ch.chalender.api.service.impl;

import ch.chalender.api.dto.Role;
import ch.chalender.api.fixtures.EventFixtures;
import ch.chalender.api.fixtures.NoticeBoardItemFixtures;
import ch.chalender.api.model.EventGenre;
import ch.chalender.api.model.EventLanguage;
import ch.chalender.api.model.EventRegion;
import ch.chalender.api.model.User;
import ch.chalender.api.repository.*;
import ch.chalender.api.service.EventLookupService;
import ch.chalender.api.service.FixturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

@Service
public class FixturesServiceImpl implements FixturesService  {

    @Autowired
    private EventGenresRepository genresRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRegionsRepository regionsRepository;

    @Autowired
    private EventLanguagesRepository languagesRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private NoticeBoardItemsRepository noticeBoardItemsRepository;

    @Autowired
    private EventFixtures eventFixtures;

    @Autowired
    private NoticeBoardItemFixtures noticeBoardItemFixtures;

    @Autowired
    private EventLookupService eventLookupService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private DocumentsRepository documentsRepository;


    @Override
    public void loadUserFixtures() {
        User user1 = userRepository.findByEmail("user1@chalender.ch");
        if (user1 == null) {
            user1 = getUser("Utilisader", "1", "user1@chalender.ch");
        } else {
            String id = user1.getId();
            user1 = getUser("Utilisader", "1", "user1@chalender.ch");
            user1.setId(id);
        }
        userRepository.save(user1);

        User user2 = userRepository.findByEmail("user2@chalender.ch");
        if (user2 == null) {
            user2 = getUser("Utilisader", "2", "user2@chalender.ch");
        } else {
            String id = user2.getId();
            user2 = getUser("Utilisader", "2", "user2@chalender.ch");
            user2.setId(id);
        }
        userRepository.save(user2);

        User user3 = userRepository.findByEmail("user3@chalender.ch");
        if (user3 == null) {
            user3 = getUser("Utilisader", "3", "user3@chalender.ch");
        } else {
            String id = user3.getId();
            user3 = getUser("Utilisader", "3", "user3@chalender.ch");
            user3.setId(id);
        }
        userRepository.save(user3);
    }

    @Override
    public void loadEventGenreFixtures() {
        List<EventGenre> genres = new ArrayList<>(List.of(
                new EventGenre(1, "musica", 1, false),
                new EventGenre(2, "teater", 2, false),
                new EventGenre(3, "litteratura", 3, false),
                new EventGenre(4, "referat/podium", 4, false),
                new EventGenre(5, "saut", 5, false),
                new EventGenre(6, "chor", 6, false),
                new EventGenre(7, "art", 7, false),
                new EventGenre(8, "film", 8, false),
                new EventGenre(9, "festa", 9, false),
                new EventGenre(10, "exposiziun", 10, false),
                new EventGenre(11, "giuventetgna", 11, false),
                new EventGenre(12, "uffants e famiglia", 12, false),
                new EventGenre(13, "inscunter rumantsch", 13, false),
                new EventGenre(14, "curs", 14, false),
                new EventGenre(15, "sport", 15, false),
                new EventGenre(16, "auter", 16, false)
        ));

        genresRepository.deleteAll();
        genresRepository.saveAll(genres);
    }

    @Override
    public void loadEventRegionFixtures() {
        List<EventRegion> regions = new ArrayList<>(List.of(
                new EventRegion(1, "Engiadin’auta", 1, false),
                new EventRegion(2, "Engiadina Bassa/Val Müstair", 2, false),
                new EventRegion(3, "Grischun central", 3, false),
                new EventRegion(4, "Surselva", 4, false),
                new EventRegion(5, "Val dal Rain da Cuira", 5, false),
                new EventRegion(6, "Grischun talian", 6, false),
                new EventRegion(7, "Tavau / Partenz", 7, false),
                new EventRegion(8, "regiun Turitg", 8, false),
                new EventRegion(9, "regiun Berna", 9, false),
                new EventRegion(10, "regiun Basilea", 10, false),
                new EventRegion(11, "Svizra Centrala", 11, false),
                new EventRegion(12, "Svizra Orientala", 12, false),
                new EventRegion(13, "Svizra franzosa", 13, false),
                new EventRegion(14, "Svizra taliana", 14, false),
                new EventRegion(15, "auter", 15, false)
        ));

        regionsRepository.deleteAll();
        regionsRepository.saveAll(regions);
    }

    @Override
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
        imagesRepository.deleteAll();
        documentsRepository.deleteAll();
        eventsRepository.saveAll(eventFixtures.getEvents());
        eventLookupService.recreateAllEventLookupData();
    }

    @Override
    public void loadNoticeBoardFixtures() {
        noticeBoardItemsRepository.deleteAll();
        // imagesRepository.deleteAll();
        // documentsRepository.deleteAll();
        noticeBoardItemsRepository.saveAll(noticeBoardItemFixtures.getNoticeBoardItems());
    }

    private User getUser(String firstName, String lastName, String email) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setOrganisation(null);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("testtest"));
        final HashSet<Role> roles = new HashSet<Role>();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);
        user.setProvider("local");
        user.setEnabled(true);
        user.setProviderUserId(null);
        user.setCreatedDate(Calendar.getInstance().getTime());
        user.setModifiedDate(Calendar.getInstance().getTime());
        return user;
    }
}
