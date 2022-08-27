package sn.giesara.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.giesara.domain.Abonnement;

/**
 * Spring Data SQL repository for the Abonnement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {}
