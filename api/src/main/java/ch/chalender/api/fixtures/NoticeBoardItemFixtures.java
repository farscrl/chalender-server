package ch.chalender.api.fixtures;

import ch.chalender.api.model.*;
import ch.chalender.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ch.chalender.api.model.PublicationStatus.*;


@Component
public class NoticeBoardItemFixtures {

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Autowired
    private EventGenresRepository eventGenresRepository;

    @Autowired
    private EventRegionsRepository eventRegionsRepository;

    @Autowired
    private EventLanguagesRepository eventLanguagesRepository;

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private DocumentsRepository documentsRepository;

    public List<NoticeBoardItem> getNoticeBoardItems() {
        List<NoticeBoardItem> notices = new ArrayList<>();

        NoticeBoardItemVersion n1 = new NoticeBoardItemVersion();
        n1.setTitle("EMNA INTERCULTURALA DA MIRS SITGS EN IL GRISCHUN");
        n1.setDescription("barat tranter la Svizra, l'Israel, la Palestina e l'Irlanda dal Nord\n" +
                "„Building Walls – Breaking Walls Grischun“\n" +
                "\n" +
                "4 - 11 settember 2023 – Val Müstair, GR / CH\n" +
                "\n" +
                "INSCUNTER INTERCULTURAL – LAVURS NATIRALAS\n" +
                "CULTURA RUMANTSCHA – VIANDAR – TRATGAS DA TUT IL MUND\n" +
                "\n" +
                "Durant in'emna sa scuntran en las Alps grischunas giuvnas e giuvens da la Svizra, /Irlanda\n" +
                "dal Nord, Irlanda, Israel e Palestina. Nus vegnin a collavurar en lavuratoris interculturals\n" +
                "davart la vita ed il mintgadi da tut ils participants, cuschinar ensemen, esser en la natira –\n" +
                "e construir ensemen en il parc natiral in mir sitg da crappa natirala che sa chatta là. In mir\n" +
                "che vegn a restar decenis.\n" +
                "Construir mirs per surmuntar mirs.\n" +
                "\n" +
                "Tge porscha l'emna:\n" +
                "• scuntradas ed activitads che arvan novs orizonts tar autras culturas\n" +
                "• Lavur da pasch e lavur interculturala tras lavuratoris cun giuvens creschids\n" +
                "• Construir cuminaivlamain in mir sitg da crappa\n" +
                "• Viandar, cuschinar ensemen tratgas da las autras culturas\n" +
                "• Confruntaziun cun la tematica \"mirs e cunfins\" tranter las culturas\n" +
                "• Representar la Svizra e la cultura rumantscha");
        n1.setContactData("Verein Naturkultur: info@nakultur.ch, +41 76 338 93 51");
        n1.getImages().add(getImg("walls.png", "static/walls.png"));
        n1.getDocuments().add(getDoc("walls.pdf", "static/walls.pdf"));
        notices.add(createNotice(n1, PUBLISHED, "user1@chalender.ch"));

        NoticeBoardItemVersion n2 = new NoticeBoardItemVersion();
        n2.setTitle("Cuors vallader da conversaziun a Turich");
        n2.setDescription("Hast tü imprais vallader o imprendast tü güsta vallader? Voust approfundir tias\n" +
                "cugnuschentschas da vallader? Hast vöglia da discuorrer ün pa vallader? Voust manar\n" +
                "interessantas discussiuns per vallader? Lura vè eir tü al cuors da conversaziun\n" +
                "(vallader) a Turich.\n" +
                "Cura?\n" +
                "I dà duos cuors:\n" +
                "- Cuors 1: 17:00-18:30\n" +
                "- Cuors 2: 19:00-20:30\n" +
                "Ils cuors han lö a las seguaintas datas:\n" +
                "- Marcurdi, 18 october 2023\n" +
                "- Marcurdi, 1. november 2023\n" +
                "- Marcurdi, 15 november 2023\n" +
                "- Marcurdi, 6 december 2023\n" +
                "- Marcurdi, 20 december 2023\n" +
                "Il cuors ha lö 5 jadas. Tuot tenor interess vain spüert ün cuors seguaint.\n" +
                "Ingio?\n" +
                "Hofackerstrasse 33, 8032 Zürich\n" +
                "Cuosts:\n" +
                "- Gruppa da 3 persunas: 330 CHF per persuna per 5 jadas\n" +
                "- Gruppa da 4 persunas: 250 CHF per persuna per 5 jadas\n" +
                "- A partir da 5 persunas: 200 CHF per persuna per 5 jadas\n" +
                "Scha quels cuosts sun massa ots per tai, schi t'annunzcha pro mai - nus chattain\n" +
                "garanti üna soluziun.\n" +
                "Persuna d’instrucziun\n" +
                "Chi chi’d es la persuna d’instrucziun vegn amo comunichà.\n" +
                "Co s’annunzchar?\n" +
                "T’annunzcha via mail fin als 1. october 2023. Scriva ün mail ad alina.mueller@uzh.ch\n" +
                "cun teis nom, teis nomer da telefon, scha tü voust far il cuors 1 o il cuors 2 (o scha tü\n" +
                "est flexibla/flexibel) e suot che cundiziuns cha tü est da la partida (p.ex. scha tü voust\n" +
                "pür gnir a partir d'üna gruppa da 5 persunas o oter). Las plazzas sun limitadas.");
        n2.setContactData("alina.mueller@uzh.ch");
        n2.getImages().add(getImg("conversaziun.png", "static/conversaziun.png"));
        notices.add(createNotice(n2, PUBLISHED, "user1@chalender.ch"));

        NoticeBoardItemVersion n3 = new NoticeBoardItemVersion();
        n3.setTitle("Pro Svizra Rumantscha e Lia Rumantscha: Spellchecker per il sutsilvan");
        n3.setDescription("Nov program da correctura per il sutsilvan – online ed en Word\n" +
                "\n" +
                "En il rom dal project «Programs da correctura ortografica rumantscha» creeschan la Pro Svizra Rumantscha e la Lia Rumantscha novs «spellcheckers» – programs per curreger texts scrits cun il computer. I sa tracta d'in utensil gratuit per ina e scadin che duai gidar a scriver rumantsch. Ussa vegn rendì accessibel il program da correctura per il sutsilvan. \n" +
                "\n" +
                "Ein Bild, das Text, Screenshot, Software, Computersymbol enthält. Automatisch generierte Beschreibung\n" +
                "\n" +
                "Nov è che tant il spellchecker sutsilvan sco era quel surmiran, publitgà l’onn passà, funcziunan en Microsoft Word.\n" +
                "\n" +
                "Il project vegn sustegnì finanzialmain da l'Uffizi federal da cultura e dal chantun Grischun e realisà da la firma far.ch cun l’agid da la partiziun Lingua da la Lia Rumantscha.     \n" +
                "\n" +
                "Dapli infurmaziuns chattais Vus sin la pagina iniziala da la Lia Rumantscha: www.liarumantscha.ch.\n" +
                "\n" +
                "Cordials salids\n" +
                "Pro Svizra Rumantscha e Lia Rumantscha");
        n3.setContactData("info@rumantsch.ch");
        n3.getImages().add(getImg("spellchecker.png", "static/spellchecker.png"));
        notices.add(createNotice(n3, PUBLISHED, "user1@chalender.ch"));

        NoticeBoardItemVersion n4 = new NoticeBoardItemVersion();
        n4.setTitle("La FMR tschertga redacturs e redacturas!");
        n4.setDescription("La Fundaziun Medias Rumantschas FMR tschertga redacturs e redacturas!\n" +
                "\n" +
                "... cun preferientscha per l’Engiadina ed il Grischun Central.\n" +
                "\n" +
                "Nus essan ina squadra che ha gust vi da la lavur e che s’allegra da novs/as collegas.\n" +
                "\n" +
                "La lavur sco redactur/a è fitg vasta. Sche Ti has veglia da lavurar schurnalisticamain, has la pussaivladad da:\n" +
                "-        scriver en tes idiom rumantsch sur da cultura, politica, sport u economia en tia regiun\n" +
                "-        viver en tia regiun, vul dir en Engiadina/Val Müstair u en Grischun Central\n" +
                "-        lavurar en in pensum variabel (50%-100%) cun temps da lavur detg flexibels\n" +
                "\n" +
                "ta barattar cun l’entira redacziun FMR e cun ils partenaris en Engiadina (Posta Ladina), Grischun Central (Pagina da Surmeir) e cun il collegas dad RTR\n" +
                "La salarisaziun tar la FMR correspunda a las reglas da la branscha.\n" +
                "\n" +
                "Nus sperain che tut questas infurmaziuns han sveglià tes interess! Tut ils detagls chattas Ti en l’inserat che sa chatta en l'agiunta.\n" +
                "Sche Ti has ulteriuras dumondas, lura ans contactescha. Nus ans legrain da Tes interess.\n" +
                "\n" +
                "Cordials salids\n" +
                "David Truttmann, schefredactur");
        n4.setContactData("FMR Fundaziun Medias Rumantschas\n" +
                "\n" +
                "Via da Masans 2, 7000 Cuira\n" +
                "\n" +
                "081 544 89 10 / +41 79 752 25 67\n" +
                "\n" +
                "david.truttmann@fmr.ch");
        n4.getImages().add(getImg("fmr.png", "static/fmr.png"));
        n4.getDocuments().add(getDoc("fmr.pdf", "static/fmr.pdf"));
        notices.add(createNotice(n4, PUBLISHED, "user1@chalender.ch"));

        NoticeBoardItemVersion n5 = new NoticeBoardItemVersion();
        n5.setTitle("Roter-Faden-Text: Sprachliche Frühförderung für Kinder");
        n5.setDescription("Nach dem erfolgreichen Start des Lehrmittels Roter-Faden-Text erweitert der SJW Verlag sein Angebot in Rätoromanisch um drei Geschichten in den Idiomen Puter und Sursilvan. Bei dieser Lernhilfe wird das Wesentliche einer Originalgeschichte in einfacher Sprache zusammengefasst. Das hilft Kindern, komplexe Inhalte und Handlungsstränge einer Geschichte mühelos zu verstehen. Detaillierte Informationen finden Sie in der Medienmitteilung im Anhang und auf der Website.\n" +
                "\n" +
                "Freundliche Grüsse\n" +
                "\n" +
                "Sandra Indermaur\n" +
                "Marketing");
        n5.setContactData("s.indermaur@sjw.ch");
        n5.getImages().add(getImg("roter-faden.png", "static/roter-faden.png"));
        n5.getDocuments().add(getDoc("roter-faden.pdf", "static/roter-faden.pdf"));
        notices.add(createNotice(n5, PUBLISHED, "user1@chalender.ch"));

        NoticeBoardItemVersion n6 = new NoticeBoardItemVersion();
        n6.setTitle("inserat manadra/manader Servetschs e persunal tar la LR");
        n6.setDescription("Stimadas damas e stimads signurs\n" +
                "\n" +
                "Nus vulessan far attent ad ina plazza attractiva cun incumbensas multifaras.\n" +
                "Il job includa diversas spartas e pussibilitescha contacts cun blera glieud. Cun plaschair spetgain nus Vossa annunzia.\n" +
                "\n" +
                "Dapli infurmaziuns chattais Vus en l’inserat agiuntà e sin www.liarumantscha.ch.\n" +
                "Gugent stat Andreas Gabriel a disposiziun per infurmaziuns detagliadas. \n" +
                "\n" +
                "Cordials salids\n" +
                "Lia Rumantscha\n" +
                "per nossa lingua");
        n6.setContactData("Lia Rumantscha\n" +
                "\n" +
                "Via da la Plessur 47 | 7001 Cuira\n" +
                "\n" +
                "telefon: +41 (0)81 258 32 22 | e-mail: info@rumantsch.ch \n" +
                "\n" +
                "www.liarumantscha.ch");
        n6.getDocuments().add(getDoc("liarumantscha.pdf", "static/liarumantscha.pdf"));
        notices.add(createNotice(n6, PUBLISHED, "user1@chalender.ch"));

        NoticeBoardItemVersion n7 = new NoticeBoardItemVersion();
        n7.setTitle("Emprender insieme");
        n7.setDescription("Essas Vus geniturs da scolars dal stgalim secundar I u II? Lura è quest messadi per Vus!\n" +
                "\n" +
                "Movetia, l'agentura naziunala per barat e mobilitad, ha introducì il program Emprender insieme cun il qual igl è pussaivel d'interprender in barat vicendaivel da duas emnas en ina scola d'ina autra regiun linguistica.\n" +
                "\n" +
                "I basta d'avair il consentiment da la scola e Movetia emprova da chattar in partenari da barat adequat e paja in pachet da 300 francs per il viadi.\n" +
                "\n" +
                "Il termin per in barat durant il 1. semester 2023/24 è ils 9 da zercladur.\n" +
                "\n" +
                "Ulteriuras infurmaziuns sco er il link d'annunzia chattais Vus qua.");
        n7.setContactData("emprenderinsieme@movetia.ch\n" +
                "+41 31 303 22 01\n");
        n7.getImages().add(getImg("insieme.jpg", "static/insieme.jpg"));
        n6.getDocuments().add(getDoc("insieme1.pdf", "static/insieme1.pdf"));
        n6.getDocuments().add(getDoc("insieme2.pdf", "static/insieme2.pdf"));
        notices.add(createNotice(n7, PUBLISHED, "user1@chalender.ch"));

        NoticeBoardItemVersion n8 = new NoticeBoardItemVersion();
        n8.setTitle("Anna Florin - post vacant");
        n8.setDescription("La società Anna Florina tschercha üna secretaria o ün secretari \n" +
                "a partir dals prüms da mai 2023 (o tenor cunvegna) per:\n" +
                "\n" +
                "    communicaziun e chüra da la commembranza (rm/tud.)\n" +
                "    communicaziun e chüra da la rait professiunala e medias \n" +
                "    chürar la pagina d'internet\n" +
                "    sustgen per l'organisaziun d’occurrenzas e foundraising\n" +
                "    ningünas lavurs d'administraziun\n" +
                "\n" +
                "No spordschain:\n" +
                "\n" +
                "    50 francs l’ura / almain 100 uras l'on\n" +
                "    lavur flexibla \n" +
                "    stretta collavuraziun cun Riet Fanzun e Flurina Badel\n" +
                "\n" +
                "Per dumondas: mail@annaflorin.ch ");
        n8.setContactData("mail@annaflorin.ch ");
        n8.getImages().add(getImg("anna-florin.png", "static/anna-florin.png"));
        notices.add(createNotice(n8, PUBLISHED, "user1@chalender.ch"));

        NoticeBoardItemVersion n9 = new NoticeBoardItemVersion();
        n9.setTitle("Canorta Rumantscha Turitg tschertga educatur*as");
        n9.setDescription("CANORTA RUMANTSCHA TURITG:\n" +
                "NUS TSCHERTGAIN EDUCATUR*AS\n" +
                "\n" +
                "NUS PURSCHAIN\n" +
                "in clima da lavur bun e famigliar\n" +
                "pitschnas gruppas d'uffants\n" +
                "collavuraziun vi dad in project unic en Svizra\n" +
                "pensum flexibel (20–100%)\n" +
                "paja tenor standards dil chantun Turitg\n" +
                "\n" +
                "NUS TSCHERTGAIN\n" +
                "plaschair vi da la lavur cun uffants\n" +
                "bunas cumpetenzas da discurrer en in dils\n" +
                "idioms rumantschs\n" +
                "cumpetenzas socialas\n" +
                "scolaziun sco educatur*a n'è betg necessaria\n" +
                "dentant d'avantatg");
        n9.setContactData("Per dumondas ed ulteriuras infurmaziuns:\n" +
                "\n" +
                "Uniun Canorta Rumantscha Turitg\n" +
                "Gian Andri Caviezel\n" +
                "Email: canortarumantscha@gmail.com\n" +
                "Tel: 079 293 05 17");
        n9.getImages().add(getImg("canorta.png", "static/canorta.png"));
        n9.getDocuments().add(getDoc("canorta.pdf", "static/canorta.pdf"));
        notices.add(createNotice(n9, PUBLISHED, "user1@chalender.ch"));

        NoticeBoardItemVersion n10 = new NoticeBoardItemVersion();
        n10.setTitle("Tschartge nov abunents");
        n10.setDescription("Allegra\n" +
                "\n" +
                "Nous dall’uniun rumantscha da Surmeir (URS) tschartgagn sen chella veia novs abunents per nossa gasetta, La Pagina da Surmeir.\n" +
                "\n" +
                "La gasetta cumpara en’eda all’emda cun reportaschas surtot or digl Surmeir, ma er da otras regiuns, reportaschas cantunalas e nationalas. Igls texts èn per ca. 90% an Surmiran e ca. 10% an Rumantsch Grischung.\n" +
                "\n" +
                "Ans faschess grond plascheir schi savessan contanscher voss interessa e gudagner encaltgi abunent/a nov/a. Daple indicaziuns cattez sot www.paginadasurmeir.ch\n" +
                "\n" +
                "Cordials saleid\n" +
                "Gian Sonder, president URS");
        n10.setContactData("gian.sonder@bluewin.ch");
        notices.add(createNotice(n10, PUBLISHED, "user1@chalender.ch"));

        NoticeBoardItemVersion n11 = new NoticeBoardItemVersion();
        n11.setTitle("Certificat rumantsch: prefasa dal test 'Cumpetenzas grammaticalas e lexicalas'");
        n11.setDescription("Allegra\n" +
                "\n" +
                "Nus dovrain Teis agüd. Nus eschan a la tschercha da persunas chi’s partecipeschan a la pilotaziun dals items grammaticals e lexicals dal nouv certificat rumantsch. Ultimamaing ha RTR rapportà da quel proget. Nus vessan grond plaschair, scha Tü füssast da la partida.\n" +
                "\n" +
                "Ün pêr infuormaziuns davart il proget:\n" +
                "\n" +
                "    La Scoul’otra da pedagogia dal Grischun (SAP) ha surgni da la regenza dal chantun Grischun l’incumbenza da sviluppar ün certificat rumantsch pels livels C1/C2 (ils duos livels ils plü ots tenor il Rom europeic cumünaivel da referenza per linguas).\n" +
                "    Cun agüd dal certificat rumantsch dessan persunas chi han ün livel linguistic ot avair la pussibiltà da laschar attestar quella cumpetenza.\n" +
                "    Il test per pudair laschar certifichar las cumpetenzas da rumantsch vegn manà tras in collavuraziun cun l’Institut für Bildungsevaluation, ün institut associà da l’Università da Turich.\n" +
                "    Il certificat final consista da differentas parts. Üna da quellas parts as basa sün cumpetenzas grammaticalas e lexicalas. Per quella part han linguistas da la SAP sviluppà differentas lezchas (items).\n" +
                "    Per pudair analisar sistematicamaing la qualità, la validità ed il livel linguistic dals items valladers sto üna tscherta quantità da persunas avair respus a las dumondas grammaticalas e lexicalas (items) dal certificat.\n" +
                "    Actualmaing pilotaina la prüma part dal certificat (cumpetenzas grammaticalas e lexicalas) in ün seguond pass vegnan eir sviluppadas e pilotadas lezchas chi masüran otras cumpetenzas, sco per exaimpel l’incletta da leger. \n" +
                "\n" +
                "Che avantags hast tü?\n" +
                "\n" +
                "    Tü hast la schanza da guadagnar ün bon da 100 CHF. I vain trat la sort per eruir las persunas chi guadognan ün bon.\n" +
                "    Culla partecipaziun al proget sustegnast Tü nossa lavur e promouvast il rumantsch.\n" +
                "    Plü tard poust Tü lura masürar e certifichar Tias aignas cumpetenzas rumantschas. \n" +
                "\n" +
                "Chi as po partecipar al pilot e co poust tü t’annunzchar?\n" +
                "\n" +
                "    Partecipar as pon tuot:tas chi san bain vallader (livel da lingua materna).\n" +
                "    Tü stoust avair temp ca. 1.5 uras tanter ils 1. da mai ed ils 31 da mai 2023 (Tü stoust indichar che di cha Tü fast il pilot, l’urari da quel di po gnir tschernü libramaing).\n" +
                "    Scha Tü at lessast partecipar schi scriva fin als 30 d’avrigl 2023 ün mail ad alina.mueller@phgr.ch cullas seguaintas infuormaziuns:\n" +
                "        Il di dal pilot (tanter ils 1. da mai ed ils 31 da mai 2023), il pilot düra ca. 1.5 uras.\n" +
                "        Las seguaintas infuormaziuns: prenom, nom, data da naschentscha, schlatta.\n" +
                "\n" +
                "Co funcziuna il pilot?\n" +
                "\n" +
                "    Davo cha Tü t’hast annunzchà/annunzchada tramettain nus ün pêr dis avant il pilot las infuormaziuns davart il decuors dal pilot ed il login.\n" +
                "    Il pilot vain fat online. Tü stoust avair ün PC, ün laptop o ün iPad cun tastatura e cun access a l’internet. Il sistem, cul qual ils pilots vegnan realistats, es simpel e fich intuitiv.\n" +
                "    Il pilot consista da duas parts, minchüna düra ca. 45 minuts. Las duos parts pon gnir fattas üna davo tschella o minchüna per sai.\n" +
                "    A la fin da la seguonda part daja amo ün pêr dumondas a reguard Tia situaziun linguistica.\n" +
                "    Attenziun: Scha Tü at partecipeschast, esa fich important cha Tü nu douvrast ingüns mezs d’agüd (p.ex. internet, dicziunaris). I va be per valütar il certificat e per chattar oura quant pretensiusas cha las dumondas sun. La prestaziun persunala nu vain valütada. Schi’s douvra mezs d’agüd vegnan ils resultats falsifichats.\n" +
                "\n" +
                "Per ulteriuras dumondas staina jent a disposiziun. Nus ans allegrain sün Ti’annunzcha!\n" +
                "\n" +
                "amiaivels salids - cordiali saluti - freundliche Grüsse\n" +
                "__\n" +
                "\n" +
                "Alina Müller");
        n11.setContactData("Alina.Mueller@phgr.ch\n" +
                "081 354 03 19\n" +
                "\n" +
                "Pädagogische Hochschule Graubünden\n" +
                "Scola auta da pedagogia dal Grischun\n" +
                "Alta scuola pedagogica dei Grigioni\n" +
                "\n" +
                "Scalärastrasse 17, 7000 Chur\n" +
                "+41 81 354 03 02, www.phgr.ch");
        notices.add(createNotice(n11, PUBLISHED, "user1@chalender.ch"));

        /*
        NoticeBoardItemVersion nX = new NoticeBoardItemVersion();
        nX.setTitle("");
        nX.setDescription("");
        nX.setContactData("");
        nX.getImages().add(getImg("walls.png", "static/walls.png"));
        notices.add(createNotice(nX, PUBLISHED, "user1@chalender.ch"));
        */

        return notices;
    }

    private NoticeBoardItem createNotice(NoticeBoardItemVersion itemVersion, PublicationStatus publicationStatus, String ownerEmail) {
        NoticeBoardItem item = new NoticeBoardItem();
        if (publicationStatus == PUBLISHED) {
            item.setCurrentlyPublished(itemVersion);
        } else if (publicationStatus == PublicationStatus.DRAFT) {
            item.setDraft(itemVersion);
        } else if (publicationStatus == IN_REVIEW) {
            item.setWaitingForReview(itemVersion);
        } else if (publicationStatus == NEW_MODIFICATION) {
            item.setWaitingForReview(itemVersion);
            item.setCurrentlyPublished(itemVersion);
        }

        item.setOwnerEmail(ownerEmail);
        item.updateCalculatedFields();

        return item;
    }

    private List<EventGenre> getGenres(String ids) {
        List<EventGenre> genres = new ArrayList<>();
        for (String id : ids.split(",")) {
            EventGenre genre = eventGenresRepository.findById(Integer.valueOf(id)).orElseThrow();
            genres.add(genre);
        }
        return genres;
    }

    private String generateDate(int addition) {
        LocalDate date = LocalDate.now().plusDays(30 + addition);
        return date.format(dateFormatter);
    }

    private Image getImg(String originalName, String path) {
        Image img = new Image();
        img.setOriginalName(originalName);
        img.setPath(path);

        img = imagesRepository.save(img);

        return img;
    }

    private Document getDoc(String originalName, String path) {
        Document doc = new Document();
        doc.setOriginalName(originalName);
        doc.setPath(path);

        doc = documentsRepository.save(doc);

        return doc;
    }
}
