package sn.giesara.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.giesara.domain.BonCoupure;

/**
 * Spring Data SQL repository for the BonCoupure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BonCoupureRepository extends JpaRepository<BonCoupure, Long> {}
