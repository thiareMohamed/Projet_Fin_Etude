package sn.giesara.service;

import java.util.List;
import java.util.Optional;
import sn.giesara.domain.Reglement;

/**
 * Service Interface for managing {@link Reglement}.
 */
public interface ReglementService {
    /**
     * Save a reglement.
     *
     * @param reglement the entity to save.
     * @return the persisted entity.
     */
    Reglement save(Reglement reglement);

    /**
     * Updates a reglement.
     *
     * @param reglement the entity to update.
     * @return the persisted entity.
     */
    Reglement update(Reglement reglement);

    /**
     * Partially updates a reglement.
     *
     * @param reglement the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Reglement> partialUpdate(Reglement reglement);

    /**
     * Get all the reglements.
     *
     * @return the list of entities.
     */
    List<Reglement> findAll();

    /**
     * Get the "id" reglement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Reglement> findOne(Long id);

    /**
     * Delete the "id" reglement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
