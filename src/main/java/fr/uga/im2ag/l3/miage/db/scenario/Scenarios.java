package fr.uga.im2ag.l3.miage.db.scenario;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import fr.uga.im2ag.l3.miage.db.model.*;
import fr.uga.im2ag.l3.miage.db.model.Enums;
import fr.uga.im2ag.l3.miage.db.model.Enums.Etat;
import fr.uga.im2ag.l3.miage.db.model.Enums.Modele;
import fr.uga.im2ag.l3.miage.db.model.Enums.Situation;
import fr.uga.im2ag.l3.miage.db.model.Enums.sexe;
import fr.uga.im2ag.l3.miage.db.utils.*;
import fr.uga.im2ag.l3.miage.db.repository.RepositoryFactory;
import fr.uga.im2ag.l3.miage.db.repository.api.*;

public class Scenarios {

    public Date convertDate(String dateString) throws ParseException {
        return new Date(new SimpleDateFormat("yyyy-MM-dd").parse(dateString).getTime());
    }


    RepositoryFactory daoFactory = new RepositoryFactory();
    
    
    
    public void resetDatabase(){
        Map<String,String> properties = new HashMap<String,String>(){{
            put("hibernate.hbm2ddl.auto", "create-drop");
        }};
        EntityManager entityManager = Persistence.createEntityManagerFactory("JPA-HBM", properties).createEntityManager();

    }

    
    EntityManager entityManager = Persistence.createEntityManagerFactory("JPA-HBM").createEntityManager();
    StationRepository stationRepository =  daoFactory.newStationRepository(entityManager);
    BornetteRepository bornetteRepository =  daoFactory.newBornetteRepository(entityManager);
    AbonneRepository abonneRepository =  daoFactory.newAbonneRepository(entityManager);
    VeloRepository veloRepository = daoFactory.newVeloRepository(entityManager);
    LocationRepository locationRepository = daoFactory.newLocationRepository(entityManager);

    
    // A. Un abonné A venir à une station S qui est situe à 12 Avenue De Gaulle
    // B. Station S a 4 bornettes
    // C. Une bornette est en hors service
    // D. Il y a 2 VTT dans cette station
    // E. Le VTT sur bornette 1 est mise en service depuis 15 Janvier 2021
    // F. Le TT sur bornette 3 est mise en service depuis 16 Mars 2022
    // G. La station demande sonID et Code Secret
    // H. Il a mis son ID et son code secret
    // I. Il a loué un VTT
    // J. Il va à la station R qui est situe à 458 avenue de la MIAGE
    // K. La station R a une seule bornette qui est aussi libre
    // L. Il a rendu le vélo après 54 minutes

    public void scenario1() throws ParseException {

        // A. Un abonné A venir à une station S
        Abonne A = new Abonne("MACRON", "Emmanuel", Enums.sexe.MALE, "33 Avenue Champs-Elysée",
                convertDate("2000-09-14"), convertDate("2022-03-15"));

        // B. Station S a 4 bornettes
        Station S = new Station("12 Avenue De Gaulle");
        Bornette b1 = new Bornette(Enums.Etat.OK, S);
        Bornette b2 = new Bornette(Enums.Etat.OK, S);
        Bornette b3 = new Bornette(Enums.Etat.OK, S);

        // C. Une bornette est en hors service
        Bornette b4 = new Bornette(Enums.Etat.HS, S);

        // D. Il y a 2 velo dans cette station, VTT et VTC
        // E. Le VTT sur bornette 1 est mise en service depuis 15 Janvier 2021
        Velo v1 = new Velo(Enums.Modele.VTT, Enums.Etat.OK, Enums.Situation.EN_STATION, convertDate("2021-01-15"), b1);

        // F. Le VTT sur bornette 3 est mise en service depuis 16 Mars 2022
        Velo v2 = new Velo(Enums.Modele.VTT, Enums.Etat.OK, Enums.Situation.EN_STATION, convertDate("2022-03-16"), b3);

        // G. La station demande son ID Client et Code Secret
        System.out.println("Veuillez saisir votre ID et Code Secret!");
        // To Do

        // H. Il a mis son ID et son code secret
        // To Do

        // I. Il a loué un VTT
        Location l = new Location();
        l.setClient(A);
        l.addVelos(v1);

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        l.startLocation(S, currentTime);

        System.out.println("-------------------------------------");
        System.out.println(v1.toString());
        System.out.println("-------------------------------------");

        // A.addLocation(l);

        // J. Il va à la station R qui est situe à 458 avenue de la MIAGE
        Station R = new Station("458 avenue de la MIAGE");

        // K. La station R a une seule bornette qui est aussi libre
        Bornette BRetour = new Bornette(Enums.Etat.OK, R);

        // L. Il a rendu le vélo après 54 minutes
        l.endLocation(R, new Timestamp((currentTime.getTime() + (54 * 60 * 1000))));

        BRetour.setVelo(v1);

        entityManager.persist(b1);
        entityManager.persist(b2);
        entityManager.persist(b3);
        entityManager.persist(b4);
        entityManager.getTransaction().begin();
        abonneRepository.save(A);
        locationRepository.save(l);
        stationRepository.save(S);
        stationRepository.save(R);
        veloRepository.save(v1);
        veloRepository.save(v2);
        entityManager.getTransaction().commit();

        System.out.println("-------------------------------------");
        System.out.println(A.toString());
        System.out.println("-------------------------------------");
        System.out.println(l.toString());
        System.out.println("-------------------------------------");
        System.out.println(S.toString());
        System.out.println("-------------------------------------");
        System.out.println(R.toString());
        System.out.println("-------------------------------------");
        System.out.println(v1.toString());
        System.out.println("-------------------------------------");

    }

    // A. Une abonnée est venu à une station S qui est situe à 41 rue maurice dodero
    // B. Station S a 4 bornettes
    // C. deux bornettes sont hors service
    // D. Il y a 1 VTT et 1 HOLLANDAIS dans cette station
    // E. Le VTT sur bornette 1 est mise en service depuis 15 Janvier 2021
    // F. Le TT sur bornette 2 est mise en service depuis 16 Mars 2022
    // G. La station demande sonID et Code Secret
    // H. Il a mis son ID et son code secret
    // I. Il a loué un VTT(V1) et un HOLLANDAIS(v2)
    // J. Il va à la station R qui est situe à 45 rue victor hugo
    // K. La station R a a deux bornettes qui sont libres
    // L. Il a rendu le VTT après 50 minutes et le hollandais après 55min

    public void scenario2() throws ParseException {

        // A. Un abonné A venir à une station S
        Abonne A = new Abonne("Pierre", "Anne", Enums.sexe.FEMELLE, "13 avenue marcelin berthelot",
                convertDate("2000-05-30"), convertDate("2022-03-15"));

        // B. Station S a 4 bornettes
        Station S = new Station("41 rue maurice dodero");
        Bornette b1 = new Bornette(Enums.Etat.OK, S);
        Bornette b2 = new Bornette(Enums.Etat.OK, S);

        // C. Deux bornettes sont hors service
        Bornette b4 = new Bornette(Enums.Etat.HS, S);
        Bornette b5 = new Bornette(Enums.Etat.HS, S);

        // EntityManager entityManager;
        // StationRepository stationRepository;

        // entityManager.getTransaction().begin();
        // stationRepository.save(S);
        // entityManager.getTransaction().commit();

        // D. Il y a 2 velo dans cette station, VTT et VTC
        // E. Le VTT sur bornette 1 est mise en service depuis 25 Janvier 2021
        Velo v1 = new Velo(Enums.Modele.VTT, Enums.Etat.OK, Enums.Situation.EN_STATION, convertDate("2021-01-25"), b1);

        // F. Le VTT sur bornette 2 est mise en service depuis 16 Mars 2022
        Velo v2 = new Velo(Enums.Modele.HOLLANDAIS, Enums.Etat.OK, Enums.Situation.EN_STATION, convertDate("2022-03-16"), b2);

        // G. La station demande son ID Client et Code Secret
        System.out.println("Veuillez saisir votre ID et Code Secret!");
        // To Do

        // H. Il a mis son ID et son code secret
        // To Do

        // I. Il a loué un VTT
        Location l1 = new Location();
        l1.setClient(A);
        l1.addVelos(v1);
        l1.setClient(A);
        l1.addVelos(v2);

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        l1.startLocation(S, currentTime);

        System.out.println("-------------------------------------");
        System.out.println(v1.toString());
        System.out.println("-------------------------------------");

        A.addLocation(l1);

        // J. Il va à la station R qui est situe à 458 avenue de la MIAGE
        Station R1 = new Station("45 rue victor hugo");

        // K. La station R1 a deux bornettes qui sont libres
        Bornette BRetour1 = new Bornette(Enums.Etat.OK, R1);
        Bornette BRetour2 = new Bornette(Enums.Etat.OK, R1);

        // L. Il a rendu le vélo après 54 minutes
        l1.endLocation(R1, new Timestamp((currentTime.getTime() + (50 * 60 * 1000))));

        // le client retourne les deux vélos aux deux bornes libres.
        BRetour1.setVelo(v1);
        BRetour2.setVelo(v2);

        System.out.println("-------------------------------------");
        System.out.println(A.toString());
        System.out.println("-------------------------------------");
        System.out.println(l1.toString());
        System.out.println("-------------------------------------");
        System.out.println(S.toString());
        System.out.println("-------------------------------------");
        System.out.println(R1.toString());
        System.out.println("-------------------------------------");
        System.out.println(v1.toString());
        System.out.println("-------------------------------------");
        System.out.println(v2.toString());
        System.out.println("-------------------------------------");

    }

    // ce scénario consulte le nombre de vélos dans chaque station, le nombre de
    // vélos endommagés, et le nombre de places libres.

    public void scenario3() throws ParseException {
        int nbVelo = 0;
        int nbEndo = 0;
        int nbPlacesLibres = 0;

        Station S = new Station("12 avenue alsace lorraine");
        Bornette b1 = new Bornette(Enums.Etat.OK, S);
        Bornette b2 = new Bornette(Enums.Etat.HS, S);

        Bornette b3 = new Bornette(Enums.Etat.HS, S);
        Bornette b4 = new Bornette(Enums.Etat.OK, S);
        Bornette b5 = new Bornette(Enums.Etat.HS, S);

        Velo v1 = new Velo(Enums.Modele.VTT, Enums.Etat.OK, Enums.Situation.EN_STATION, convertDate("2021-12-25"), b1);
        Velo v2 = new Velo(Enums.Modele.VTC, Enums.Etat.OK, Enums.Situation.EN_STATION, convertDate("2022-01-25"), b4);

        for (Bornette b : S.getBornettes()) {
            if (b.getLibre() == false) {
                nbVelo = nbVelo + 1;
            }
        }

        for (Bornette b : S.getBornettes()) {
            System.out.println(b.getVelo());
            if (b.getLibre() == false && b.getVelo().getEtat() == Enums.Etat.HS)
                nbEndo++;
        }

        for (Bornette b : S.getBornettes()) {
            if (b.getLibre() == true)
                nbPlacesLibres++;
        }
        ;
        System.out.println("-------------------------------------");
        System.out.println("-------------------------------------");
        System.out.println("-------------------------------------");
        System.out.println(b1.toString());
        System.out.println(b1.getVelo());
        System.out.println("-------------------------------------");
        System.out.println(v1.toString());
        System.out.println("-------------------------------------");
        System.out.println(v2.toString());
        System.out.println("-------------------------------------");
        System.out.println(S.toString());

        System.out.println("-------------------------------------");
        System.out.println(nbPlacesLibres);
        System.out.println("nbVelo:" + nbVelo);
        System.out.print("endo :" + nbEndo);
    }


    // Notre entreprise de location de vélos a 4 abonnées pour ce moment
    // On a 4 stations; station A,B,C,D
    // Chaque station a 4 bornettes
    // Notre entreprise avoir 6 velo qui sont circulant
    // La plupart des vélos sont garées à station D car la plupart de notre abonnées sont des étudiants à l'IM2AG


    public void peupler() throws ParseException{
        Abonne Andreas = new Abonne("Schevchenko", "Andreas", sexe.MALE, "Paris", convertDate("1945-08-17"), convertDate("2022-03-20"), 666);
        Abonne Emmanuelle = new Abonne("Macrone", "Emmanuelle", sexe.FEMELLE, "Amiens", convertDate("1977-12-21"), convertDate("2022-03-10"), 1789);
        Abonne Sofia = new Abonne("Boutella","Sofia", sexe.FEMELLE, "39 avenue de Deschamps", convertDate("1999-11-11"), convertDate("2022-02-15"), 9999 );
        Abonne Theo = new Abonne("Flavius", "Theodorus", sexe.MALE, "LXIX rue des Romains", convertDate("350-01-14"), convertDate("2022-03-21"), 7887); 


        Station A = new Station("44 Avenue de MIAGE");
        Bornette AB1 = new Bornette(Etat.OK, A);
        Bornette AB2 = new Bornette(Etat.OK, A);
        Bornette AB3 = new Bornette(Etat.OK, A);
        Bornette AB4 = new Bornette(Etat.OK, A);
        
        Station B = new Station("13 rue de Jakarta");
        Bornette BB1 = new Bornette(Etat.OK, B);
        Bornette BB2 = new Bornette(Etat.OK, B);
        Bornette BB3 = new Bornette(Etat.OK, B);
        Bornette BB4 = new Bornette(Etat.HS, B);
        
        Station C = new Station("69 rue de plaisir");
        Bornette CB1 = new Bornette(Etat.HS, C);
        Bornette CB2 = new Bornette(Etat.HS, C);
        Bornette CB3 = new Bornette(Etat.HS, C);
        Bornette CB4 = new Bornette(Etat.HS, C);

        Station D = new Station("En face de batiment IM2AG UGA");
        Bornette DB1 = new Bornette(Etat.OK, D);
        Bornette DB2 = new Bornette(Etat.OK, D);
        Bornette DB3 = new Bornette(Etat.OK, D);
        Bornette DB4 = new Bornette(Etat.OK, D);

        Velo v1 = new Velo(Modele.HOLLANDAIS, Etat.OK, Situation.EN_STATION, convertDate("2021-12-15"), DB1);
        Velo v2 = new Velo(Modele.VTC, Etat.OK, Situation.EN_STATION, convertDate("2021-12-15"), DB2);
        Velo v3 = new Velo(Modele.HOLLANDAIS, Etat.OK, Situation.EN_STATION, convertDate("2021-12-15"), DB3);
        Velo v4 = new Velo(Modele.HOLLANDAIS, Etat.OK, Situation.EN_STATION, convertDate("2021-12-15"), DB4);
        Velo v5 = new Velo(Modele.VTT, Etat.OK, Situation.EN_STATION, convertDate("2021-12-15"), CB1);
        Velo v6 = new Velo(Modele.HOLLANDAIS, Etat.OK, Situation.EN_STATION, convertDate("2021-12-15"), AB1);


        entityManager.getTransaction().begin();
        
        abonneRepository.save(Andreas);
        abonneRepository.save(Emmanuelle);
        abonneRepository.save(Sofia);
        abonneRepository.save(Theo);
        
        stationRepository.save(A);
        stationRepository.save(B);
        stationRepository.save(C);
        stationRepository.save(D);
        
        bornetteRepository.save(AB1);
        bornetteRepository.save(AB2);
        bornetteRepository.save(AB3);
        bornetteRepository.save(AB4);
        bornetteRepository.save(BB1);
        bornetteRepository.save(BB2);
        bornetteRepository.save(BB3);
        bornetteRepository.save(BB4);
        bornetteRepository.save(CB1);
        bornetteRepository.save(CB2);
        bornetteRepository.save(CB3);
        bornetteRepository.save(CB4);
        
        veloRepository.save(v1);
        veloRepository.save(v2);
        veloRepository.save(v3);
        veloRepository.save(v4);
        veloRepository.save(v5);
        veloRepository.save(v6);
        entityManager.getTransaction().commit();

    }

}
