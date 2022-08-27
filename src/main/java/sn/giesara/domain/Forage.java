package sn.giesara.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Forage.
 */
@Entity
@Table(name = "forage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Forage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_site")
    private String nomSite;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "date_instalation")
    private Instant dateInstalation;

    @Column(name = "profondeur_forage")
    private Double profondeurForage;

    @Column(name = "hauteur")
    private Double hauteur;

    @Column(name = "capacite")
    private Double capacite;

    @Column(name = "hauteur_sous_radier")
    private Double hauteurSousRadier;

    @Column(name = "type_paratonnerre")
    private String typeParatonnerre;

    @Column(name = "type_reservoir")
    private String typeReservoir;

    @Column(name = "capacite_reservoir")
    private Double capaciteReservoir;

    @OneToMany(mappedBy = "forage")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "factures", "client", "abonnement", "village", "forage" }, allowSetters = true)
    private Set<Compteur> compteurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Forage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomSite() {
        return this.nomSite;
    }

    public Forage nomSite(String nomSite) {
        this.setNomSite(nomSite);
        return this;
    }

    public void setNomSite(String nomSite) {
        this.nomSite = nomSite;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Forage longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Forage latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Instant getDateInstalation() {
        return this.dateInstalation;
    }

    public Forage dateInstalation(Instant dateInstalation) {
        this.setDateInstalation(dateInstalation);
        return this;
    }

    public void setDateInstalation(Instant dateInstalation) {
        this.dateInstalation = dateInstalation;
    }

    public Double getProfondeurForage() {
        return this.profondeurForage;
    }

    public Forage profondeurForage(Double profondeurForage) {
        this.setProfondeurForage(profondeurForage);
        return this;
    }

    public void setProfondeurForage(Double profondeurForage) {
        this.profondeurForage = profondeurForage;
    }

    public Double getHauteur() {
        return this.hauteur;
    }

    public Forage hauteur(Double hauteur) {
        this.setHauteur(hauteur);
        return this;
    }

    public void setHauteur(Double hauteur) {
        this.hauteur = hauteur;
    }

    public Double getCapacite() {
        return this.capacite;
    }

    public Forage capacite(Double capacite) {
        this.setCapacite(capacite);
        return this;
    }

    public void setCapacite(Double capacite) {
        this.capacite = capacite;
    }

    public Double getHauteurSousRadier() {
        return this.hauteurSousRadier;
    }

    public Forage hauteurSousRadier(Double hauteurSousRadier) {
        this.setHauteurSousRadier(hauteurSousRadier);
        return this;
    }

    public void setHauteurSousRadier(Double hauteurSousRadier) {
        this.hauteurSousRadier = hauteurSousRadier;
    }

    public String getTypeParatonnerre() {
        return this.typeParatonnerre;
    }

    public Forage typeParatonnerre(String typeParatonnerre) {
        this.setTypeParatonnerre(typeParatonnerre);
        return this;
    }

    public void setTypeParatonnerre(String typeParatonnerre) {
        this.typeParatonnerre = typeParatonnerre;
    }

    public String getTypeReservoir() {
        return this.typeReservoir;
    }

    public Forage typeReservoir(String typeReservoir) {
        this.setTypeReservoir(typeReservoir);
        return this;
    }

    public void setTypeReservoir(String typeReservoir) {
        this.typeReservoir = typeReservoir;
    }

    public Double getCapaciteReservoir() {
        return this.capaciteReservoir;
    }

    public Forage capaciteReservoir(Double capaciteReservoir) {
        this.setCapaciteReservoir(capaciteReservoir);
        return this;
    }

    public void setCapaciteReservoir(Double capaciteReservoir) {
        this.capaciteReservoir = capaciteReservoir;
    }

    public Set<Compteur> getCompteurs() {
        return this.compteurs;
    }

    public void setCompteurs(Set<Compteur> compteurs) {
        if (this.compteurs != null) {
            this.compteurs.forEach(i -> i.setForage(null));
        }
        if (compteurs != null) {
            compteurs.forEach(i -> i.setForage(this));
        }
        this.compteurs = compteurs;
    }

    public Forage compteurs(Set<Compteur> compteurs) {
        this.setCompteurs(compteurs);
        return this;
    }

    public Forage addCompteur(Compteur compteur) {
        this.compteurs.add(compteur);
        compteur.setForage(this);
        return this;
    }

    public Forage removeCompteur(Compteur compteur) {
        this.compteurs.remove(compteur);
        compteur.setForage(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Forage)) {
            return false;
        }
        return id != null && id.equals(((Forage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Forage{" +
            "id=" + getId() +
            ", nomSite='" + getNomSite() + "'" +
            ", longitude=" + getLongitude() +
            ", latitude=" + getLatitude() +
            ", dateInstalation='" + getDateInstalation() + "'" +
            ", profondeurForage=" + getProfondeurForage() +
            ", hauteur=" + getHauteur() +
            ", capacite=" + getCapacite() +
            ", hauteurSousRadier=" + getHauteurSousRadier() +
            ", typeParatonnerre='" + getTypeParatonnerre() + "'" +
            ", typeReservoir='" + getTypeReservoir() + "'" +
            ", capaciteReservoir=" + getCapaciteReservoir() +
            "}";
    }
}
