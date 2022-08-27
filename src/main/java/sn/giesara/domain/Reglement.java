package sn.giesara.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reglement.
 */
@Entity
@Table(name = "reglement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Reglement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "somme_verse")
    private Double sommeVerse;

    @Column(name = "date_reglement")
    private Instant dateReglement;

    @JsonIgnoreProperties(value = { "bonCoupures", "compteur" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Facture facture;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reglement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSommeVerse() {
        return this.sommeVerse;
    }

    public Reglement sommeVerse(Double sommeVerse) {
        this.setSommeVerse(sommeVerse);
        return this;
    }

    public void setSommeVerse(Double sommeVerse) {
        this.sommeVerse = sommeVerse;
    }

    public Instant getDateReglement() {
        return this.dateReglement;
    }

    public Reglement dateReglement(Instant dateReglement) {
        this.setDateReglement(dateReglement);
        return this;
    }

    public void setDateReglement(Instant dateReglement) {
        this.dateReglement = dateReglement;
    }

    public Facture getFacture() {
        return this.facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public Reglement facture(Facture facture) {
        this.setFacture(facture);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reglement)) {
            return false;
        }
        return id != null && id.equals(((Reglement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reglement{" +
            "id=" + getId() +
            ", sommeVerse=" + getSommeVerse() +
            ", dateReglement='" + getDateReglement() + "'" +
            "}";
    }
}
