package sn.giesara.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.giesara.domain.Compteur;

/**
 * Spring Data SQL repository for the Compteur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompteurRepository extends JpaRepository<Compteur, Long> {
}
