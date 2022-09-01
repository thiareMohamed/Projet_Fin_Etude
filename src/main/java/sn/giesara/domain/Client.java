package sn.giesara.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "factures", "client", "abonnement", "village", "forage" }, allowSetters = true)
    private Set<Compteur> compteurs = new HashSet<>();

    @OneToOne(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Personne personne;

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public Client(Long id, Set<Compteur> compteurs) {
        super();
        this.id = id;
        this.compteurs = compteurs;
    }

    public Client() {
        super();
    }
    public Client(Long id) {
        super();
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Set<Compteur> getCompteurs() {
        return this.compteurs;
    }

    public void setCompteurs(Set<Compteur> compteurs) {
        if (this.compteurs != null) {
            this.compteurs.forEach(i -> i.setClient(null));
        }
        if (compteurs != null) {
            compteurs.forEach(i -> i.setClient(this));
        }
        this.compteurs = compteurs;
    }

    public Client compteurs(Set<Compteur> compteurs) {
        this.setCompteurs(compteurs);
        return this;
    }

    public Client addCompteur(Compteur compteur) {
        this.compteurs.add(compteur);
        compteur.setClient(this);
        return this;
    }

    public Client removeCompteur(Compteur compteur) {
        this.compteurs.remove(compteur);
        compteur.setClient(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            "}";
    }
}
