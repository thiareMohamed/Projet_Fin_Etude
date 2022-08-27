package sn.giesara.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Abonnement.
 */
@Entity
@Table(name = "abonnement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Abonnement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "prix_unitaire")
    private Double prixUnitaire;

    @OneToMany(mappedBy = "abonnement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "factures", "client", "abonnement", "village", "forage" }, allowSetters = true)
    private Set<Compteur> compteurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Abonnement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Abonnement libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getPrixUnitaire() {
        return this.prixUnitaire;
    }

    public Abonnement prixUnitaire(Double prixUnitaire) {
        this.setPrixUnitaire(prixUnitaire);
        return this;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Set<Compteur> getCompteurs() {
        return this.compteurs;
    }

    public void setCompteurs(Set<Compteur> compteurs) {
        if (this.compteurs != null) {
            this.compteurs.forEach(i -> i.setAbonnement(null));
        }
        if (compteurs != null) {
            compteurs.forEach(i -> i.setAbonnement(this));
        }
        this.compteurs = compteurs;
    }

    public Abonnement compteurs(Set<Compteur> compteurs) {
        this.setCompteurs(compteurs);
        return this;
    }

    public Abonnement addCompteur(Compteur compteur) {
        this.compteurs.add(compteur);
        compteur.setAbonnement(this);
        return this;
    }

    public Abonnement removeCompteur(Compteur compteur) {
        this.compteurs.remove(compteur);
        compteur.setAbonnement(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Abonnement)) {
            return false;
        }
        return id != null && id.equals(((Abonnement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Abonnement{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", prixUnitaire=" + getPrixUnitaire() +
            "}";
    }
}
