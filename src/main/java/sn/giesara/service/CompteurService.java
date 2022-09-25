package sn.giesara.service;

import java.util.List;
import java.util.Optional;
import sn.giesara.domain.Compteur;
import sn.giesara.service.dto.AddCompteurDto;

/**
 * Service Interface for managing {@link Compteur}.
 */
public interface CompteurService {
    /**
     * Save a compteur.
     *
     * @param compteur the entity to save.
     * @return the persisted entity.
     */
    Compteur save(AddCompteurDto compteur);

    /**
     * Updates a compteur.
     *
     * @param compteur the entity to update.
     * @return the persisted entity.
     */
    Compteur update(AddCompteurDto compteur);

    /**
     * Partially updates a compteur.
     *
     * @param compteur the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Compteur> partialUpdate(Compteur compteur);

    /**
     * Get all the compteurs.
     *
     * @return the list of entities.
     */
    List<Compteur> findAll();

    /**
     * Get the "id" compteur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Compteur> findOne(Long id);

    /**
     * Delete the "id" compteur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
