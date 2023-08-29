package ch.chalender.api.fixtures;

import ch.chalender.api.model.*;
import ch.chalender.api.repository.EventGenresRepository;
import ch.chalender.api.repository.EventLanguagesRepository;
import ch.chalender.api.repository.EventRegionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ch.chalender.api.model.EventStatus.*;


@Component
public class EventFixtures {

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Autowired
    private EventGenresRepository eventGenresRepository;

    @Autowired
    private EventRegionsRepository eventRegionsRepository;

    @Autowired
    private EventLanguagesRepository eventLanguagesRepository;

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();

        EventVersion ev1 = new EventVersion();
        ev1.setTitle("Singen mit Flurin");
        ev1.setGenres(getGenres("1"));
        ev1.setDescription("„Singen mit Flurin“ –  wunderbare, ungezwungene Abende im Marsöl in Chur. Das Publikum ist der Chor. Und wenn jemand den Ton nicht ganz genau trifft, kein Problem.\n\n„Singen mit Flurin“ – eine Erfolgsgeschichte auf allen Ebenen. Wunderbare, ungezwungene und entspannte Abende im Marsöl und rundum glückliche Gesichter. Das Ziel von Caviezel, die Gesellschaft wieder zusammenzubringen, wurde erreicht.  Und da der Wunsch nach einer Fortsetzung dieses gemeinsamen Singens immer lauter wurde, geht es im 2023 weiter. An vier Mittwoch-Abenden im Frühling, Sommer, Herbst und Winter wird wieder zusammen gesungen und auch gelacht.\nSingen entspannt, singen macht glücklich und wenn man singt, wird das Kuschelhormon Oxytocin ausgeschüttet. Für einmal sieht und hört man den Bündner Kabarettisten und Musiker Flurin Caviezel nicht nur, nein das Publikum macht mit, alle singen mit  ihm zusammen. Der Text wird auf der Kinoleinwand projiziert und schon geht es los. Gemeinsam statt einsam. Und wenn jemand den Ton nicht ganz genau trifft, kein Problem, diejenigen links und rechts werden das übertönen.\nGanz bewusst wird der Gesang nur durch ein Klavier unterstützt. Das bringt zum einen den Vorteil, dass Tempo und Tonart sofort, direkt und unkompliziert angepasst werden können und zum andern, dass dem Gesang mehr Raum, mehr Klang gegeben wird.\n\nAn einem Abend werden jeweils drei Sets a  circa 30 Minuten gesungen. Die Lieder der einzelnen Sets haben ein gemeinsames Motto. Das kann zum Beispiel eine Jahreszeit, eine besondere Kategorie wie Liebeslieder, oder Lieder einer bestimmten Musikgruppe wie z.B. Abba oder Beatles sein. Neben verschiedenen Sprachen haben auch Lieder in unseren Kantonssprachen, Walserdeutsch, Rätoromanisch und Italienisch ihren festen Platz im Repertoire. Wir singen Hits, Evergreens, Volkslieder, von «Sch’eu füss ‘na randulina» bis «I’m sailing», von «Azzuro» bis «Über den Wolken», «Champs Elysees» bis «Hemmige» und noch vieles mehr und alle werden einen glücklichen Abend erleben. Also: «Let it be»!");
        ev1.setLocation("Restaurant Marsöl, Chur");
        ev1.setAddress("Restaurant Marsöl, Süsswinkelgasse 25, 7000 Chur");
        ev1.setOccurrences(eventOccurrences(false, "04-04-2023", "17:30", "19:30", false));
        ev1.setRegions(getRegions("5"));
        ev1.setEventLanguages(eventLanguages("de,rm,it"));
        ev1.setOnlineOnly(false);
        ev1.setLink("https://www.flurincaviezel.ch/");
        events.add(createEvent(ev1, PUBLISHED, "user1@chalender.ch"));

        EventVersion ev2 = new EventVersion();
        ev2.setTitle("festival da la poesia alpina contemporana");
        ev2.setGenres(getGenres("5,6"));
        ev2.setDescription("Dus dis da litteratura, cun 15 auturas ed auturs da la Ladinia, dal Grischun e dal Friul che vegnan a preleger lur texts, presentads e commentads da trais critics letterars. Plinavant vegn il festival accumpagnà da chantauturas e chantauturs da las trais regiuns linguisticas partecipadas. Ina occasiun d’inscunter e da cumparaziun preziusa per approfundar l’enconuschientscha da la realitad linguistic-litterara da questas trais inslas linguisticas alpinas. Nus ans legrain da vossa visita!");
        ev2.setLocation("Biblioteca municipala, Persenon-Brixen");
        ev2.setAddress("Biblioteca municipala, Piazza del Duomo 4, 39042 Bressanone BZ, Italien");
        ev2.setOccurrences(eventOccurrences(true, "11-05-2023", null, null, false));
        ev2.setRegions(getRegions("9"));
        ev2.setEventLanguages(eventLanguages("rm,de"));
        ev2.setOnlineOnly(false);
        ev2.setLink(null);
        events.add(createEvent(ev2, PUBLISHED, "user1@chalender.ch"));

        EventVersion ev3 = new EventVersion();
        ev3.setTitle("Guida publica tras l’exposiziun ‘Retrospectiva Gieri Schmed’");
        ev3.setGenres(getGenres("5"));
        ev3.setDescription("La retrospectiva a caschun digl 80avel natalezi che Gieri Schmed havess festivau uonn muossa ovras digl artist dall’entschatta entochen alla fin da siu temps creativ, da 1962 entochen 2019. Ei setracta d’ina premiera, nua che plirs maletgs meins- e nunenconuschents vegnan presentai alla publicitad. In omagi en regurdientscha dad ina veta dedicada agl art.");
        ev3.setLocation("Museum sursilvan Cuort Ligia Grischa, Trun");
        ev3.setAddress("Museum Sursilvan, Cuort Ligia Grischa, Via Principala 90, CH-7166 Trun");
        ev3.setOccurrences(eventOccurrences(false, "22-04-2023", "12:15", "13:15", false));
        ev3.setRegions(getRegions("2"));
        ev3.setEventLanguages(eventLanguages("rm,de"));
        ev3.setOnlineOnly(false);
        ev3.setLink("http://museum-trun.ch/");
        events.add(createEvent(ev3, PUBLISHED, "user1@chalender.ch"));

        EventVersion ev4 = new EventVersion();
        ev4.setTitle("Lavuratori Traversadas litteraras 2023");
        ev4.setGenres(getGenres("7"));
        ev4.setDescription("Il lavuratori è avert per tut las persunas interessadas cun experientscha da scriver e/u da translatar.\nLa publicaziun dal lavuratori Traversadas litteraras dals 20 d’october 2023 suonda la fin d’avust");
        ev4.setLocation("Lia Rumantscha, Cuira");
        ev4.setAddress("Lia Rumantscha, Via da la Plessur 47, 7001 Cuira");
        ev4.setOccurrences(eventOccurrences(false, "19-10-2023", "08:00", "15:00", false));
        ev4.setRegions(getRegions("5,6"));
        ev4.setEventLanguages(eventLanguages("rm"));
        ev4.setOnlineOnly(false);
        ev4.setLink("http://www.liarumantscha.ch/");
        EventVersion ev4Old = new EventVersion();
        ev4Old.setTitle("Lavuratori Traversadas litteraras 2022");
        ev4Old.setGenres(getGenres("6"));
        ev4Old.setDescription("Il lavuratori è avert per tut las persunas interessadas cun experientscha da scriver e da translatar.\nLa publicaziun dal lavuratori Traversadas litteraras dals 20 d’october 2023 suonda la fin d’avust");
        ev4Old.setLocation("Lia Rumantscha, Chur");
        ev4Old.setAddress("Lia Rumantscha, Via da la Plessur 47, 7001 Cuira");
        ev4Old.setOccurrences(eventOccurrences(false, "19-10-2023", "08:00", "16:00", false));
        ev4Old.setRegions(getRegions("5"));
        ev4Old.setEventLanguages(eventLanguages("rm,de"));
        ev4Old.setOnlineOnly(false);
        ev4Old.setLink("http://www.liarumantscha.ch/");
        Event event4 = createEvent(ev4, NEW_MODIFICATION, "user1@chalender.ch");
        event4.setCurrentlyPublished(ev4Old);
        events.add(event4);

        EventVersion ev5 = new EventVersion();
        ev5.setTitle("Grischun conta");
        ev5.setGenres(getGenres("8"));
        ev5.setDescription("Sut il motto “Grischun conta” ha l’uniun Cultura Rumantscha en la Bassa gudignau il chor viriil Ligia Grischa, cantus firmus surselva ed il Chor da giuvenil*as grischun per in concert unic. Tuts treis chors presentan canzuns primarmein romontschas e contemporanas.");
        ev5.setLocation("Baselgia catolica a Cham");
        ev5.setAddress("Baselgia catolica, Kirchbühl 10, 6330 Cham");
        ev5.setOccurrences(eventOccurrences(false, "22-04-2023", "17:30", "20:00", false));
        ev5.setRegions(getRegions("7,8"));
        ev5.setEventLanguages(eventLanguages("rm"));
        ev5.setOnlineOnly(false);
        ev5.setLink(null);
        events.add(createEvent(ev5, DRAFT, "user1@chalender.ch"));

        EventVersion ev6 = new EventVersion();
        ev6.setTitle("prelecziun da Gian Gaudenz");
        ev6.setGenres(getGenres("6"));
        ev6.setDescription("19:15 h radunanza generala\n20:15 h prelecziun / Vorlesung da Gian Gaudenz our da seis cudesch / aus seinem Buch “Bündnerspeck”.\n\nEntrada libra, collecta\nReservaziun facultativ, tel. 081 864 08 89 / 076 343 63 51\nSco finischun da l’occurrenza offrischa la società ün aperitiv.");
        ev6.setLocation("Baselgia San Niclà, Strada");
        ev6.setAddress("Baselgia San Niclà, Strada");
        ev6.setOccurrences(eventOccurrences(false, "13-04-2023", "18:15", "19:30", false));
        ev6.setRegions(getRegions("4"));
        ev6.setEventLanguages(eventLanguages("rm"));
        ev6.setOnlineOnly(false);
        ev6.setLink("http://www.san-nicla.ch/");
        events.add(createEvent(ev6, IN_REVIEW, "user1@chalender.ch"));

        EventVersion ev7 = new EventVersion();
        ev7.setTitle("Retrospectiva Gieri Schmed");
        ev7.setGenres(getGenres("5"));
        ev7.setDescription("La retrospectiva a caschun digl 80avel natalezi che Gieri Schmed havess festivau uonn muossa ovras digl artist dall’entschatta entochen alla fin da siu temps creativ, da 1962 entochen 2019.\n\nEi setracta d’ina premiera, alla quala era maletgs meins- e nunenconuschents vegnan presentai alla publicitad. \nIn omagi en regurdientscha ad ina veta dedicada agl art.\n\nLa contribuziun da RTR da tedlar sin quei link.\n\t\nGieri Schmed ei naschius ils 3 da schaner 1943 a Trun ed ha passentau si’affonza a Cartatscha, in uclaun da Trun. Cun 15 onns eis el serendius a Tavau per far siu emprendissadi da pictur. Tavau enconuscheva Gieri gia pulit bein cunquei ch’el fageva\nleu savens visetas a sia tatta. A Tavau ha el era empriu d’enconuscher meglier igl artist Alois Carigiet, ha luvrau per lez sco gidonter e survegniu da lez grond sustegn per sefatschentar adina pli intensivamein cun l’atgna forza artistica ella pictura.\n1970 ha el fatg sias empremas lavurs artisticas.\nSecasaus puspei a Trun/Gravas cun sia consorta Barla Maria Schmed-Albin e ses dus fegls Rafael e Marcus, ha Gieri Schmed fundau l’atgna fatschenta da pictur.\nTonaton ha el impundiu vinavon aschi bia temps sco pusseivel per igl art.\n1988 ha el survegniu il premi da promoziun dil Cantun Grischun ed igl onn 1993 ei il cudisch Konstellationen vegnius publicaus. En var 57 exposiziuns ha igl artist da Trun presentau sias ovras alla publicitad.\n1997 ei Gieri Schmed vegnius recepius ella GSMBA, oz num-\nnada Visarte.\nEnsemen cun sia consorta Barla ha el astgau passentar in bi e productiv temps a Paris 1999 ella Cité Internationale des Arts Paris ed ella Belgia 2007 el Center Frans Masereel Kasterlee.\nUonn havess igl artist festivau siu 80avel anniversari.");
        ev7.setLocation("Museum Sursilvan Cuort Ligia Grischa, Trun");
        ev7.setAddress("Museum Sursilvan, Cuort Ligia Grischa, Via Principala 90, CH-7166 Trun");
        ev7.setOccurrences(eventOccurrences(false, "07-04-2023", "08:00", "15:00", false));
        ev7.setRegions(getRegions("1"));
        ev7.setEventLanguages(eventLanguages("rm,de"));
        ev7.setOnlineOnly(false);
        ev7.setLink("http://museum-trun.ch/");
        events.add(createEvent(ev7, PUBLISHED, "user2@chalender.ch"));

        EventVersion ev8 = new EventVersion();
        ev8.setTitle("Hasi Farinelli");
        ev8.setGenres(getGenres("2"));
        ev8.setDescription("La historia basescha sin ina fabla da Aesop. Ils dus protagonists, Hasi e Farinelli, secreian omisdus ch’els seigien ils megliers che lur antagonist, e muossan tgei ch‘els san en ina concurrenza tut speciala, mo per anflar ora ch’els ein megliers ensemen. \n\nEi tunan arias e duets bein enconuschents da baroc tochen la romantica e denteren cumposiziuns da Quirina Lechmann.");
        ev8.setLocation("Postremise, Cuira");
        ev8.setAddress("Postremise, Engadinstrasse 43, 7000 Chur");
        ev8.setOccurrences(eventOccurrences(false, "09-04-2023", "18:00", "20:00", false));
        ev8.setRegions(getRegions("5"));
        ev8.setEventLanguages(eventLanguages("rm"));
        ev8.setOnlineOnly(false);
        ev8.setLink("https://www.postremise.ch/events/hasi-farinelli-2/form");
        events.add(createEvent(ev8, PUBLISHED, "user2@chalender.ch"));

        EventVersion ev9 = new EventVersion();
        ev9.setTitle("surdada dal premi grischun da litteratura a Joachim B. Schmidt");
        ev9.setGenres(getGenres("6"));
        ev9.setDescription("Invit a la surdada dal Premi grischun da litteratura 2023\nJoachim B. Schmidt, «Tell», Diogenes Verlag, Zürich 2022");
        ev9.setLocation("Alter Konsum, Cazis");
        ev9.setAddress("Alter Konsum, Bahnhofstasse 8, 7408 Cazis");
        ev9.setOccurrences(eventOccurrences(false, "13-04-2023", "16:30", "18:00", false));
        ev9.setRegions(getRegions("5"));
        ev9.setEventLanguages(eventLanguages("rm"));
        ev9.setOnlineOnly(false);
        ev9.setLink(null);
        events.add(createEvent(ev9, PUBLISHED, "user2@chalender.ch"));

        EventVersion ev10 = new EventVersion();
        ev10.setTitle("Rumantsch è …");
        ev10.setGenres(getGenres("9"));
        ev10.setDescription("Co vesan atgnamain or ils ierts en la Rumantschia?\n\nLa resposta: fitg multifars. En Surselva creschan per exempel ils urteis, en la Tumleastga il mangieult ed en l'Engiadina Bassa las piessas. Betg da smirvegliar che er ils capuns na vegnan betg fatgs dapertut tuttina. Quai mussa ch'il rumantsch procura cun ses tschintg idioms per varietad – betg mo en iert ed en cuschina, mabain er en la cultura ed en la vita. L'exposiziun ambulanta «Rumantsch è…», ina iniziativa da l'anteriur cusseglier guvernativ Christian Rathgeb, dal chantun Grischun e da la Lia Rumantscha, vul sensibilisar per il rumantsch, gida a colliar ed envida las visitadras ed ils visitaders da «semnar lingua».\n\nUltra da quai chatt'ins en l'exposiziun: la sutga rumantscha, l'ura rumantscha e la charta geografica «Svizra Rumantscha». Donat Caduff ha translatà ils nums da tut las radund 2200 vischnancas en rumantsch. Per las bleras na devi fin ussa nagina denominaziun rumantscha.\n\nL’exposiziun ‘Rumantsch è …` vegn accumpagnada d’in vast program: curs da lingua, concert da Mattiu, saira rumantscha en il Museum Laax, guida cun ils artists cumpigliads Anna.R.Stoffel e Donat Caduff etc.\n\navertura sonda, 1.04.23, 13-17h\n14.00 introducziun Andreas Gabriel, Lia Rumantscha\n\ntemps d’avertura\n\n02.04. – 14.05.2023\ngievgia – dumengia\n13.30 – 16.30 h");
        ev10.setLocation("Cularta Laax");
        ev10.setAddress("Cularta, Via Falera 2a, 7031 Laax");
        ev10.setOccurrences(eventOccurrences(true, "31-03-2023", null, null, false));
        ev10.setRegions(getRegions("1"));
        ev10.setEventLanguages(eventLanguages("rm,de"));
        ev10.setOnlineOnly(false);
        ev10.setLink("http://cularta.ch/");
        events.add(createEvent(ev10, PUBLISHED, "user2@chalender.ch"));

        EventVersion ev11 = new EventVersion();
        ev11.setTitle("Pina Palau");
        ev11.setGenres(getGenres("1"));
        ev11.setDescription("infos: www.cinemasilplaz.ch");
        ev11.setLocation("Cinema Sil Plaz");
        ev11.setAddress("Cinema Sil Plaz, Via Centrala 2, 7130 Ilanz/Glion");
        ev11.setOccurrences(eventOccurrences(false, "13-04-2023", "16:30", "18:00", false));
        ev11.setRegions(getRegions("1"));
        ev11.setEventLanguages(eventLanguages("de"));
        ev11.setOnlineOnly(false);
        ev11.setLink("https://pinapalau.bandcamp.com/track/closer");
        events.add(createEvent(ev11, PUBLISHED, "user2@chalender.ch"));

        EventVersion ev12 = new EventVersion();
        ev12.setTitle("Prelecziun Jachen Andry");
        ev12.setGenres(getGenres("6"));
        ev12.setDescription("La prelecziun ha lieu in rumantsch cun translaziuns en talian e tudestg.");
        ev12.setLocation("Bücher Lüthy Cuira");
        ev12.setAddress("Bahnhofstrasse 8, 7000 Cuira");
        ev12.setOccurrences(eventOccurrences(false, "13-04-2023", "16:30", "18:00", false));
        ev12.setRegions(getRegions("5"));
        ev12.setEventLanguages(eventLanguages("rm,de,it"));
        ev12.setOnlineOnly(false);
        ev12.setLink(null);
        events.add(createEvent(ev12, PUBLISHED, "user2@chalender.ch"));

        EventVersion ev13 = new EventVersion();
        ev13.setTitle("Poetry Slam Engiadinais");
        ev13.setGenres(getGenres("6"));
        ev13.setDescription("Poetry Slam Engiadinais\n\nPoetry Slams sun occurrenzas, inua cha persunas prelegian lur texts ad ün publicum. Ils texts paun esser poesias, istorgias, que po dafatta ir in direcziun da „rap\"… que es listess, ma minchün ho be 6 minuts per sia performance.\n\nIl publicum decida chi chi guadagna e quella persuna survain tradiziunelmaing üna butiglia Whiskey, tar nus Engiadinais saro que però üna buna butiglia iva (sponsoriseda da Mia Iva). In generel sun Poetry Slams sairedas fich pachificas e divertaivlas chi promouvan in prüma lingia la cumpagnia vi da la bar ed in nos cas natürelmaing eir la lingua rumauntscha.\n\nQue nu do auncha üngüna scena da Poetry Slam rumauntscha e que vulainsa müder quist an.\n\nIl prüm Poetry Slam Engiadinais ho lö in sanda, ils 15 avrigl 2023 illa Grotta da Cultura a Sent. La bar es avierta a partir da las 19:30 ed a las 20:00 cumainza il slam!\n\nModeraziun: Romana Ganzoni\nSlammedras e slammeder: Hannah Flury, Nadja Hort, Selina Müller, Janic Maskos\n\nPer dumandas u tar interess per as parteciper: info(at)udg.ch");
        ev13.setLocation("Grotta da Cultura, Sent");
        ev13.setAddress("Grotta da Cultura, Schigliana 204, 7554 Sent");
        ev13.setOccurrences(eventOccurrences(false, "13-04-2023", "16:30", "18:00", false));
        ev13.setRegions(getRegions("4"));
        ev13.setEventLanguages(eventLanguages("rm"));
        ev13.setOnlineOnly(false);
        ev13.setLink("https://www.udg.ch");
        events.add(createEvent(ev13, PUBLISHED, "user3@chalender.ch"));

        EventVersion ev14 = new EventVersion();
        ev14.setTitle("ina messa per pasch");
        ev14.setGenres(getGenres("8"));
        ev14.setDescription("Nus tuts selegrein dad astgar presentar in concert monumental suenter quater onns abstinenza!\n\nIl Chor dalla Scola claustrala ei puspei sin via cun ina capo-ovra contemporana.\n\nEnsemen cun ils ensembles deCanto e PiCant ed in grond orchester essan nus varga 160 giuvenils che\n\ncontan la \"Messa per pasch\" da Sir Karl Jenkins.");
        ev14.setLocation("Baselgia S. Franciscus, Turitg/Wollishofen");
        ev14.setAddress("Baselgia S. Franciscus, Albisstrasse, 8038 Zürich");
        ev14.setOccurrences(eventOccurrences(false, "13-04-2023", "16:30", "18:00", false));
        ev14.setRegions(getRegions("7"));
        ev14.setEventLanguages(eventLanguages("rm"));
        ev14.setOnlineOnly(false);
        ev14.setLink(null);
        events.add(createEvent(ev14, PUBLISHED, "user3@chalender.ch"));

        EventVersion ev15 = new EventVersion();
        ev15.setTitle("Concert da Curdin Nicolai & band");
        ev15.setGenres(getGenres("1"));
        ev15.setDescription("16.00 chantar ensemen chanzuns rumantschas\n17.30 pizza per tuts\n18.30 star si legher e chantar\n19.00 concert cun Curdin Nicolay e band");
        ev15.setLocation("Holzlegi, Winterthur");
        ev15.setAddress("Holzlegi, Holzlegistrasse 40, 8408 Winterthur");
        ev15.setOccurrences(eventOccurrences(false, "13-04-2023", "08:00", "18:00", false));
        ev15.setRegions(getRegions("7"));
        ev15.setEventLanguages(eventLanguages("rm"));
        ev15.setOnlineOnly(false);
        ev15.setLink("www.allegrawinti.ch");
        events.add(createEvent(ev15, PUBLISHED, "user3@chalender.ch"));

        EventVersion ev16 = new EventVersion();
        ev16.setTitle("Luvratori «fundar ina cooperativa»");
        ev16.setGenres(getGenres("10"));
        ev16.setDescription("Caras amitg*as dalla Cooperativa Encarden\n\nUssa daventi concret! Nus prendein a mauns la fundaziun dalla Cooperativa Encarden. Cheu anflas ti igl invit al luvratori «fundar ina cooperativa» ils 29 d’avrel a Sagogn. Vul ti habitar ella Casa Encarden ni promover il project cun tias cumpetenzas? Lu selegrein nus da tia annunzia. En cass che ti sas buca vegnir al luvratori, astgas ti bugen separticipar la proxima ga.\n \nL’evaluaziun dil luvratori dil vitg ei terminada ed il protocol fotografic culs resultats vegn publicaus proximamein sin nossa pagina d’internet. Leu anflas ti era in artechel dalla gasetta Wochenzeitung (WOZ) che tematisescha la dificila situaziun sil marcau da habitaziuns en vitgs turistics els cuolms. Igl artechel presenta il project Cooperativa Encarden sco ina propo sta per sligiaziuns pusseivlas. Ultra ei la pagina d’internet ussa era accessibla cumpletamein per romontsch.\n\nAl luvratori dil vitg ei ina gruppa da diversas persunas da Sagogn seconstituida. Quella vegn ad accumpignar e sustener il project cun lur savida locala.\n\nCordials salids");
        ev16.setLocation("Sagogn");
        ev16.setAddress("Cooperativa Encarden, Via Vitg dadens 55, 7152 Sagogn");
        ev16.setOccurrences(eventOccurrences(false, "11-05-2023", "08:00", "18:00", false));
        ev16.setRegions(getRegions("1"));
        ev16.setEventLanguages(eventLanguages("rm,de"));
        ev16.setOnlineOnly(false);
        ev16.setLink("http://www.encarden.ch/");
        events.add(createEvent(ev16, PUBLISHED, "user3@chalender.ch"));

        return events;
    }

    private Event createEvent(EventVersion eventVersion, EventStatus eventStatus, String ownerEmail) {
        Event event = new Event();
        if (eventStatus == PUBLISHED) {
            event.setCurrentlyPublished(eventVersion);
        } else if (eventStatus == EventStatus.DRAFT) {
            event.setDraft(eventVersion);
        } else if (eventStatus == IN_REVIEW) {
            event.setWaitingForReview(eventVersion);
        } else if (eventStatus == NEW_MODIFICATION) {
            event.setWaitingForReview(eventVersion);
            event.setCurrentlyPublished(eventVersion);
        }

        event.setOwnerEmail(ownerEmail);

        return event;
    }

    private List<EventGenre> getGenres(String ids) {
        List<EventGenre> genres = new ArrayList<>();
        for (String id : ids.split(",")) {
            EventGenre genre = eventGenresRepository.findById(Integer.valueOf(id)).orElseThrow();
            genres.add(genre);
        }
        return genres;
    }

    private List<EventRegion> getRegions(String ids) {
        List<EventRegion> regions = new ArrayList<>();
        for (String id : ids.split(",")) {
            EventRegion region = eventRegionsRepository.findById(Integer.valueOf(id)).orElseThrow();
            regions.add(region);
        }
        return regions;
    }

    private List<EventLanguage> eventLanguages(String ids) {
        List<EventLanguage> eventLanguages = new ArrayList<>();
        for (String id : ids.split(",")) {
            EventLanguage eventLanguage = eventLanguagesRepository.findById(id).orElseThrow();
            eventLanguages.add(eventLanguage);
        }
        return eventLanguages;
    }

    private List<EventOccurrence> eventOccurrences(boolean allDay, String date, String start, String end, boolean cancelled) {
        List<EventOccurrence> eventOccurrences = new ArrayList<>();

        EventOccurrence eventOccurrence = new EventOccurrence();
        eventOccurrence.setAllDay(allDay);
        eventOccurrence.setDate(LocalDate.parse(date, dateFormatter));
        if (!allDay) {
            eventOccurrence.setStart(LocalTime.parse(start, timeFormatter));
            if (end != null) {
                eventOccurrence.setEnd(LocalTime.parse(end, timeFormatter));
            }
        }
        eventOccurrence.setCancelled(cancelled);

        eventOccurrences.add(eventOccurrence);
        return eventOccurrences;
    }
}
