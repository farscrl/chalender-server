package ch.chalender.api.service.impl;

import ch.chalender.api.dto.Role;
import ch.chalender.api.fixtures.EventFixtures;
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
    private EventFixtures eventFixtures;

    @Autowired
    private EventLookupService eventLookupService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImagesRepository imagesRepository;


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

    @Override
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
        eventsRepository.saveAll(eventFixtures.getEvents());
        eventLookupService.recreateAllEventLookupData();
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
