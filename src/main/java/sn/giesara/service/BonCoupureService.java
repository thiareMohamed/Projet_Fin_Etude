package sn.giesara.service;

import java.util.List;
import java.util.Optional;
import sn.giesara.domain.BonCoupure;

/**
 * Service Interface for managing {@link BonCoupure}.
 */
public interface BonCoupureService {
    /**
     * Save a bonCoupure.
     *
     * @param bonCoupure the entity to save.
     * @return the persisted entity.
     */
    BonCoupure save(BonCoupure bonCoupure);

    /**
     * Updates a bonCoupure.
     *
     * @param bonCoupure the entity to update.
     * @return the persisted entity.
     */
    BonCoupure update(BonCoupure bonCoupure);

    /**
     * Partially updates a bonCoupure.
     *
     * @param bonCoupure the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BonCoupure> partialUpdate(BonCoupure bonCoupure);

    /**
     * Get all the bonCoupures.
     *
     * @return the list of entities.
     */
    List<BonCoupure> findAll();

    /**
     * Get the "id" bonCoupure.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BonCoupure> findOne(Long id);

    /**
     * Delete the "id" bonCoupure.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
