package sn.giesara.service;

import java.util.List;
import java.util.Optional;
import sn.giesara.domain.Forage;

/**
 * Service Interface for managing {@link Forage}.
 */
public interface ForageService {
    /**
     * Save a forage.
     *
     * @param forage the entity to save.
     * @return the persisted entity.
     */
    Forage save(Forage forage);

    /**
     * Updates a forage.
     *
     * @param forage the entity to update.
     * @return the persisted entity.
     */
    Forage update(Forage forage);

    /**
     * Partially updates a forage.
     *
     * @param forage the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Forage> partialUpdate(Forage forage);

    /**
     * Get all the forages.
     *
     * @return the list of entities.
     */
    List<Forage> findAll();

    /**
     * Get the "id" forage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Forage> findOne(Long id);

    /**
     * Delete the "id" forage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
