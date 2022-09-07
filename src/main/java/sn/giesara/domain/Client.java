package sn.giesara.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
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

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "date_naissance")
    private Date dateNaissance;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    @Column(name = "sexe")
    private String sexe;

    @Column(name = "numero_telephone", unique = true, nullable = false  )
    private Long numeroTelephone;

    @Column(name = "numero_cni", unique = true, nullable = false)
    private String numeroCni;

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "factures", "client", "abonnement", "village", "forage" }, allowSetters = true)
    private Set<Compteur> compteurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Client() {
        super();
    }


    public Client(Long id, String nom, String prenom, Date dateNaissance, String lieuNaissance, String sexe, Long numeroTelephone, String numeroCni, Set<Compteur> compteurs) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
        this.sexe = sexe;
        this.numeroTelephone = numeroTelephone;
        this.numeroCni = numeroCni;
        this.compteurs = compteurs;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Long getNumeroTelephone() {
        return numeroTelephone;
    }

    public void setNumeroTelephone(Long numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public String getNumeroCni() {
        return numeroCni;
    }

    public void setNumeroCni(String numeroCni) {
        this.numeroCni = numeroCni;
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
