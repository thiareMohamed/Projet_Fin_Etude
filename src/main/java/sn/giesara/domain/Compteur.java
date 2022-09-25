package sn.giesara.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Compteur.
 */
@Entity
@Table(name = "compteur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Compteur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_compteur")
    private Long numeroCompteur;

    @Column(name = "type_compteur")
    private String typeCompteur;

    @Column(name = "date_abonnement")
    private LocalDate dateAbonnement;

    @Column(name = "marque")
    private String marque;

    @Column(name = "statut")
    private Boolean statut;

    @OneToMany(mappedBy = "compteur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bonCoupures", "compteur" }, allowSetters = true)
    private Set<Facture> factures = new HashSet<>();

    @ManyToOne
//    @JsonIgnoreProperties(value = { "personne", "compteurs" }, allowSetters = true)
    private Client client;

    @ManyToOne
//    @JsonIgnoreProperties(value = { "compteurs" }, allowSetters = true)
    private Abonnement abonnement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "compteurs", "commune" }, allowSetters = true)
    private Village village;

    @ManyToOne
    @JsonIgnoreProperties(value = { "compteurs" }, allowSetters = true)
    private Forage forage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Compteur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumeroCompteur() {
        return this.numeroCompteur;
    }

    public Compteur numeroCompteur(Long numeroCompteur) {
        this.setNumeroCompteur(numeroCompteur);
        return this;
    }

    public void setNumeroCompteur(Long numeroCompteur) {
        this.numeroCompteur = numeroCompteur;
    }

    public String getTypeCompteur() {
        return this.typeCompteur;
    }

    public Compteur typeCompteur(String typeCompteur) {
        this.setTypeCompteur(typeCompteur);
        return this;
    }

    public void setTypeCompteur(String typeCompteur) {
        this.typeCompteur = typeCompteur;
    }

    public LocalDate getDateAbonnement() {
        return this.dateAbonnement;
    }

    public Compteur dateAbonnement(LocalDate dateAbonnement) {
        this.setDateAbonnement(dateAbonnement);
        return this;
    }

    public void setDateAbonnement(LocalDate dateAbonnement) {
        this.dateAbonnement = dateAbonnement;
    }

    public String getMarque() {
        return this.marque;
    }

    public Compteur marque(String marque) {
        this.setMarque(marque);
        return this;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public Boolean getStatut() {
        return this.statut;
    }

    public Compteur statut(Boolean statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }

    public Set<Facture> getFactures() {
        return this.factures;
    }

    public void setFactures(Set<Facture> factures) {
        if (this.factures != null) {
            this.factures.forEach(i -> i.setCompteur(null));
        }
        if (factures != null) {
            factures.forEach(i -> i.setCompteur(this));
        }
        this.factures = factures;
    }

    public Compteur factures(Set<Facture> factures) {
        this.setFactures(factures);
        return this;
    }

    public Compteur addFacture(Facture facture) {
        this.factures.add(facture);
        facture.setCompteur(this);
        return this;
    }

    public Compteur removeFacture(Facture facture) {
        this.factures.remove(facture);
        facture.setCompteur(null);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Compteur client(Client client) {
        this.setClient(client);
        return this;
    }

    public Abonnement getAbonnement() {
        return this.abonnement;
    }

    public void setAbonnement(Abonnement abonnement) {
        this.abonnement = abonnement;
    }

    public Compteur abonnement(Abonnement abonnement) {
        this.setAbonnement(abonnement);
        return this;
    }

    public Village getVillage() {
        return this.village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public Compteur village(Village village) {
        this.setVillage(village);
        return this;
    }

    public Forage getForage() {
        return this.forage;
    }

    public void setForage(Forage forage) {
        this.forage = forage;
    }

    public Compteur forage(Forage forage) {
        this.setForage(forage);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compteur)) {
            return false;
        }
        return id != null && id.equals(((Compteur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Compteur{" +
            "id=" + getId() +
            ", numeroCompteur=" + getNumeroCompteur() +
            ", typeCompteur=" + getTypeCompteur() +
            ", dateAbonnement='" + getDateAbonnement() + "'" +
            ", marque='" + getMarque() + "'" +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
