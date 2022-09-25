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
 * A Facture.
 */
@Entity
@Table(name = "facture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "date_dernier_releve")
    private Instant dateDernierReleve;

    @Column(name = "ancien_index")
    private Double ancienIndex;

    @Column(name = "nouvel_index")
    private Double nouvelIndex;

    @Column(name = "date_limite_paiment")
    private Instant dateLimitePaiment;

    @Column(name = "statut")
    private Boolean statut;

    @ManyToMany
    @JoinTable(
        name = "rel_facture__bon_coupure",
        joinColumns = @JoinColumn(name = "facture_id"),
        inverseJoinColumns = @JoinColumn(name = "bon_coupure_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "factures" }, allowSetters = true)
    private Set<BonCoupure> bonCoupures = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "factures", "village", "forage" }, allowSetters = true)
    private Compteur compteur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Facture id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Facture code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getDateDernierReleve() {
        return this.dateDernierReleve;
    }

    public Facture dateDernierReleve(Instant dateDernierReleve) {
        this.setDateDernierReleve(dateDernierReleve);
        return this;
    }

    public void setDateDernierReleve(Instant dateDernierReleve) {
        this.dateDernierReleve = dateDernierReleve;
    }

    public Double getAncienIndex() {
        return this.ancienIndex;
    }

    public Facture ancienIndex(Double ancienIndex) {
        this.setAncienIndex(ancienIndex);
        return this;
    }

    public void setAncienIndex(Double ancienIndex) {
        this.ancienIndex = ancienIndex;
    }

    public Double getNouvelIndex() {
        return this.nouvelIndex;
    }

    public Facture nouvelIndex(Double nouvelIndex) {
        this.setNouvelIndex(nouvelIndex);
        return this;
    }

    public void setNouvelIndex(Double nouvelIndex) {
        this.nouvelIndex = nouvelIndex;
    }

    public Instant getDateLimitePaiment() {
        return this.dateLimitePaiment;
    }

    public Facture dateLimitePaiment(Instant dateLimitePaiment) {
        this.setDateLimitePaiment(dateLimitePaiment);
        return this;
    }

    public void setDateLimitePaiment(Instant dateLimitePaiment) {
        this.dateLimitePaiment = dateLimitePaiment;
    }

    public Boolean getStatut() {
        return this.statut;
    }

    public Facture statut(Boolean statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }

    public Set<BonCoupure> getBonCoupures() {
        return this.bonCoupures;
    }

    public void setBonCoupures(Set<BonCoupure> bonCoupures) {
        this.bonCoupures = bonCoupures;
    }

    public Facture bonCoupures(Set<BonCoupure> bonCoupures) {
        this.setBonCoupures(bonCoupures);
        return this;
    }

    public Facture addBonCoupure(BonCoupure bonCoupure) {
        this.bonCoupures.add(bonCoupure);
        bonCoupure.getFactures().add(this);
        return this;
    }

    public Facture removeBonCoupure(BonCoupure bonCoupure) {
        this.bonCoupures.remove(bonCoupure);
        bonCoupure.getFactures().remove(this);
        return this;
    }

    public Compteur getCompteur() {
        return this.compteur;
    }

    public void setCompteur(Compteur compteur) {
        this.compteur = compteur;
    }

    public Facture compteur(Compteur compteur) {
        this.setCompteur(compteur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facture)) {
            return false;
        }
        return id != null && id.equals(((Facture) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facture{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", dateDernierReleve='" + getDateDernierReleve() + "'" +
            ", ancienIndex=" + getAncienIndex() +
            ", nouvelIndex=" + getNouvelIndex() +
            ", dateLimitePaiment='" + getDateLimitePaiment() + "'" +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
