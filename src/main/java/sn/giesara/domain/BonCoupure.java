package sn.giesara.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BonCoupure.
 */
@Entity
@Table(name = "bon_coupure")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BonCoupure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_bon")
    private String codeBon;

    @Column(name = "raison_coupure")
    private String raisonCoupure;

    @Column(name = "taxe")
    private Double taxe;

    @ManyToMany(mappedBy = "bonCoupures")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bonCoupures", "compteur" }, allowSetters = true)
    private Set<Facture> factures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BonCoupure id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeBon() {
        return this.codeBon;
    }

    public BonCoupure codeBon(String codeBon) {
        this.setCodeBon(codeBon);
        return this;
    }

    public void setCodeBon(String codeBon) {
        this.codeBon = codeBon;
    }

    public String getRaisonCoupure() {
        return this.raisonCoupure;
    }

    public BonCoupure raisonCoupure(String raisonCoupure) {
        this.setRaisonCoupure(raisonCoupure);
        return this;
    }

    public void setRaisonCoupure(String raisonCoupure) {
        this.raisonCoupure = raisonCoupure;
    }

    public Double getTaxe() {
        return this.taxe;
    }

    public BonCoupure taxe(Double taxe) {
        this.setTaxe(taxe);
        return this;
    }

    public void setTaxe(Double taxe) {
        this.taxe = taxe;
    }

    public Set<Facture> getFactures() {
        return this.factures;
    }

    public void setFactures(Set<Facture> factures) {
        if (this.factures != null) {
            this.factures.forEach(i -> i.removeBonCoupure(this));
        }
        if (factures != null) {
            factures.forEach(i -> i.addBonCoupure(this));
        }
        this.factures = factures;
    }

    public BonCoupure factures(Set<Facture> factures) {
        this.setFactures(factures);
        return this;
    }

    public BonCoupure addFacture(Facture facture) {
        this.factures.add(facture);
        facture.getBonCoupures().add(this);
        return this;
    }

    public BonCoupure removeFacture(Facture facture) {
        this.factures.remove(facture);
        facture.getBonCoupures().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BonCoupure)) {
            return false;
        }
        return id != null && id.equals(((BonCoupure) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BonCoupure{" +
            "id=" + getId() +
            ", codeBon='" + getCodeBon() + "'" +
            ", raisonCoupure='" + getRaisonCoupure() + "'" +
            ", taxe=" + getTaxe() +
            "}";
    }
}
