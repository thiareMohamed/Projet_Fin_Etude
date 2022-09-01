package sn.giesara.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Personne.
 */
@Entity
@Table(name = "personne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Personne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "date_naissance")
    private Instant dateNaissance;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    @Column(name = "sexe")
    private String sexe;

    @Column(name = "numero_telephone")
    private Integer numeroTelephone;

    @Column(name = "numero_cni")
    private Integer numeroCni;

    @OneToOne
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Personne() {
        super();
    }

    public Personne(Long id, String nom, String prenom, Instant dateNaissance, String lieuNaissance, String sexe, Integer numeroTelephone, Integer numeroCni, Client client) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
        this.sexe = sexe;
        this.numeroTelephone = numeroTelephone;
        this.numeroCni = numeroCni;
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getId() {
        return this.id;
    }

    public Personne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Personne nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Personne prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Instant getDateNaissance() {
        return this.dateNaissance;
    }

    public Personne dateNaissance(Instant dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(Instant dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return this.lieuNaissance;
    }

    public Personne lieuNaissance(String lieuNaissance) {
        this.setLieuNaissance(lieuNaissance);
        return this;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public String getSexe() {
        return this.sexe;
    }

    public Personne sexe(String sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Integer getNumeroTelephone() {
        return this.numeroTelephone;
    }

    public Personne numeroTelephone(Integer numeroTelephone) {
        this.setNumeroTelephone(numeroTelephone);
        return this;
    }

    public void setNumeroTelephone(Integer numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public Integer getNumeroCni() {
        return this.numeroCni;
    }

    public Personne numeroCni(Integer numeroCni) {
        this.setNumeroCni(numeroCni);
        return this;
    }

    public void setNumeroCni(Integer numeroCni) {
        this.numeroCni = numeroCni;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Personne)) {
            return false;
        }
        return id != null && id.equals(((Personne) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Personne{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", lieuNaissance='" + getLieuNaissance() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", numeroTelephone=" + getNumeroTelephone() +
            ", numeroCni=" + getNumeroCni() +
            "}";
    }
}
