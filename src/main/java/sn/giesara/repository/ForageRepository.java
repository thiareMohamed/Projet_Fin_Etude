package sn.giesara.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.giesara.domain.Forage;

/**
 * Spring Data SQL repository for the Forage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ForageRepository extends JpaRepository<Forage, Long> {}
