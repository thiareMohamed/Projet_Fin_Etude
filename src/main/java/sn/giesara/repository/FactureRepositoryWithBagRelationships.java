package sn.giesara.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import sn.giesara.domain.Facture;

public interface FactureRepositoryWithBagRelationships {
    Optional<Facture> fetchBagRelationships(Optional<Facture> facture);

    List<Facture> fetchBagRelationships(List<Facture> factures);

    Page<Facture> fetchBagRelationships(Page<Facture> factures);
}
