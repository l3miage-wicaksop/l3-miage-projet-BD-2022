package fr.uga.im2ag.l3.miage.db.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import fr.uga.im2ag.l3.miage.db.model.Enums.Situation;

@Entity
@Table(name = "Location")
public class Location {

    
    public Location() {
        this.velos = new  ArrayList<Velo>();
    }

    @Id
    @GeneratedValue
    private int idLoc;

    private Timestamp heureDebut;

    private Timestamp heureFin;
    @OneToOne
    private Station stationDepart;
    @OneToOne
    private Station stationArrivee;

    private float cout;

    @ManyToMany
    private List<Velo> velos;

    @ManyToOne(targetEntity = Client.class)
    private Client client;

    public int getIdLoc() {
        return idLoc;
    }

    public void setIdLoc(int idLoc) {
        this.idLoc = idLoc;
    }

    public List<Velo> getVelos() {
        return velos;
    }

    public void setVelos(List<Velo> velos) {
        this.velos = velos;
    }

    public void addVelos(Velo velo){
        this.velos.add(velo);
        velo.setSituation(Situation.EN_LOCATION);
        
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        client.addLocation(this);
        
    }

    public int getId() {
        return idLoc;
    }

    public void setId(int idLoc) {
        this.idLoc = idLoc;
    }

    

    public Timestamp getHeureDebut() {
        return heureDebut;
    }

    private void setHeureDebut(Timestamp heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Timestamp getHeureFin() {
        return heureFin;
    }

    private void setHeureFin(Timestamp heureFin) {
        this.heureFin = heureFin;
    }

    public float getCout() {
        return cout;
    }

    public void setCout(float cout) {
        this.cout = cout;
    }
    
    public Station getStationDepart() {
        return stationDepart;
    }

    private void setStationDepart(Station stationDepart) {
        this.stationDepart = stationDepart;
    }

    public Station getStationArrivee() {
        return stationArrivee;
    }

    private void setStationArrivee(Station stationArrivee) {
        this.stationArrivee = stationArrivee;
    }

    public void startLocation(Station s, Timestamp ...t){
        setStationDepart(s);
        if(t == null){
            setHeureDebut(new Timestamp(System.currentTimeMillis()));
        } else {
            setHeureDebut(t[0]);
        }

        for (Velo v: getVelos()){
            v.veloEstLoue();
        }
    }

    public void endLocation(Station s, Timestamp ...t){
        setStationArrivee(s);
        if(t == null){
            setHeureFin(new Timestamp(System.currentTimeMillis()));
        } else {
            setHeureFin(t[0]);
        }

        getVelos().forEach(e -> {
            calculerCout(e);
        });
    }

    public void calculerCout(Velo velo) {
        // Calculer le temps de location
        int minute = 1000 * 60;        
        float minutes = (heureFin.getTime() - heureDebut.getTime()) / minute;
        
        // Recuperer le €/min du velo
        int valeurV = velo.getModele().getValeur();

        // Multiplier le prix par le temps (en minutes)
        float cout = valeurV * minutes;

        if (client instanceof Abonne) {
            cout = cout * 0.7f;         // Réduction de 30%
        }

        setCout(cout);
    }

    @Override
    public String toString() {
        return "Location [client=" + client + ", cout=" + cout + ", heureDebut=" + heureDebut + ", heureFin=" + heureFin
                + ", idLoc=" + idLoc + ", stationArrivee=" + stationArrivee + ", stationDepart=" + stationDepart
                + ", velos=" + velos + "]";
    }

    
}
