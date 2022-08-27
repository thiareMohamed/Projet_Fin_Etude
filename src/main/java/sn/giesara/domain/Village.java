package sn.giesara.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Village.
 */
@Entity
@Table(name = "village")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Village implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom_nom_chef_village")
    private Double prenomNomChefVillage;

    @OneToMany(mappedBy = "village")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "factures", "client", "abonnement", "village", "forage" }, allowSetters = true)
    private Set<Compteur> compteurs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "villages" }, allowSetters = true)
    private Commune commune;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Village id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Village nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getPrenomNomChefVillage() {
        return this.prenomNomChefVillage;
    }

    public Village prenomNomChefVillage(Double prenomNomChefVillage) {
        this.setPrenomNomChefVillage(prenomNomChefVillage);
        return this;
    }

    public void setPrenomNomChefVillage(Double prenomNomChefVillage) {
        this.prenomNomChefVillage = prenomNomChefVillage;
    }

    public Set<Compteur> getCompteurs() {
        return this.compteurs;
    }

    public void setCompteurs(Set<Compteur> compteurs) {
        if (this.compteurs != null) {
            this.compteurs.forEach(i -> i.setVillage(null));
        }
        if (compteurs != null) {
            compteurs.forEach(i -> i.setVillage(this));
        }
        this.compteurs = compteurs;
    }

    public Village compteurs(Set<Compteur> compteurs) {
        this.setCompteurs(compteurs);
        return this;
    }

    public Village addCompteur(Compteur compteur) {
        this.compteurs.add(compteur);
        compteur.setVillage(this);
        return this;
    }

    public Village removeCompteur(Compteur compteur) {
        this.compteurs.remove(compteur);
        compteur.setVillage(null);
        return this;
    }

    public Commune getCommune() {
        return this.commune;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }

    public Village commune(Commune commune) {
        this.setCommune(commune);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Village)) {
            return false;
        }
        return id != null && id.equals(((Village) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Village{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenomNomChefVillage=" + getPrenomNomChefVillage() +
            "}";
    }
}
