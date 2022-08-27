package sn.giesara.service;

import java.util.List;
import java.util.Optional;
import sn.giesara.domain.Village;

/**
 * Service Interface for managing {@link Village}.
 */
public interface VillageService {
    /**
     * Save a village.
     *
     * @param village the entity to save.
     * @return the persisted entity.
     */
    Village save(Village village);

    /**
     * Updates a village.
     *
     * @param village the entity to update.
     * @return the persisted entity.
     */
    Village update(Village village);

    /**
     * Partially updates a village.
     *
     * @param village the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Village> partialUpdate(Village village);

    /**
     * Get all the villages.
     *
     * @return the list of entities.
     */
    List<Village> findAll();

    /**
     * Get the "id" village.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Village> findOne(Long id);

    /**
     * Delete the "id" village.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
