package sn.giesara.service;

import java.util.List;
import java.util.Optional;
import sn.giesara.domain.Abonnement;

/**
 * Service Interface for managing {@link Abonnement}.
 */
public interface AbonnementService {
    /**
     * Save a abonnement.
     *
     * @param abonnement the entity to save.
     * @return the persisted entity.
     */
    Abonnement save(Abonnement abonnement);

    /**
     * Updates a abonnement.
     *
     * @param abonnement the entity to update.
     * @return the persisted entity.
     */
    Abonnement update(Abonnement abonnement);

    /**
     * Partially updates a abonnement.
     *
     * @param abonnement the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Abonnement> partialUpdate(Abonnement abonnement);

    /**
     * Get all the abonnements.
     *
     * @return the list of entities.
     */
    List<Abonnement> findAll();

    /**
     * Get the "id" abonnement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Abonnement> findOne(Long id);

    /**
     * Delete the "id" abonnement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
