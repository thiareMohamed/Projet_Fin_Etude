package sn.giesara.service;

import java.util.List;
import java.util.Optional;
import sn.giesara.domain.Commune;

/**
 * Service Interface for managing {@link Commune}.
 */
public interface CommuneService {
    /**
     * Save a commune.
     *
     * @param commune the entity to save.
     * @return the persisted entity.
     */
    Commune save(Commune commune);

    /**
     * Updates a commune.
     *
     * @param commune the entity to update.
     * @return the persisted entity.
     */
    Commune update(Commune commune);

    /**
     * Partially updates a commune.
     *
     * @param commune the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Commune> partialUpdate(Commune commune);

    /**
     * Get all the communes.
     *
     * @return the list of entities.
     */
    List<Commune> findAll();

    /**
     * Get the "id" commune.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Commune> findOne(Long id);

    /**
     * Delete the "id" commune.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
