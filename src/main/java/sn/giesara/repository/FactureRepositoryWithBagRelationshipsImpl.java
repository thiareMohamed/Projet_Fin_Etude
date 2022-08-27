package sn.giesara.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import sn.giesara.domain.Facture;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class FactureRepositoryWithBagRelationshipsImpl implements FactureRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Facture> fetchBagRelationships(Optional<Facture> facture) {
        return facture.map(this::fetchBonCoupures);
    }

    @Override
    public Page<Facture> fetchBagRelationships(Page<Facture> factures) {
        return new PageImpl<>(fetchBagRelationships(factures.getContent()), factures.getPageable(), factures.getTotalElements());
    }

    @Override
    public List<Facture> fetchBagRelationships(List<Facture> factures) {
        return Optional.of(factures).map(this::fetchBonCoupures).orElse(Collections.emptyList());
    }

    Facture fetchBonCoupures(Facture result) {
        return entityManager
            .createQuery("select facture from Facture facture left join fetch facture.bonCoupures where facture is :facture", Facture.class)
            .setParameter("facture", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Facture> fetchBonCoupures(List<Facture> factures) {
        return entityManager
            .createQuery(
                "select distinct facture from Facture facture left join fetch facture.bonCoupures where facture in :factures",
                Facture.class
            )
            .setParameter("factures", factures)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
