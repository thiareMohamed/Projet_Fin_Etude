package sn.giesara.service;

import java.util.List;
import java.util.Optional;
import sn.giesara.domain.Personne;

/**
 * Service Interface for managing {@link Personne}.
 */
public interface PersonneService {
    /**
     * Save a personne.
     *
     * @param personne the entity to save.
     * @return the persisted entity.
     */
    Personne save(Personne personne);

    /**
     * Updates a personne.
     *
     * @param personne the entity to update.
     * @return the persisted entity.
     */
    Personne update(Personne personne);

    /**
     * Partially updates a personne.
     *
     * @param personne the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Personne> partialUpdate(Personne personne);

    /**
     * Get all the personnes.
     *
     * @return the list of entities.
     */
    List<Personne> findAll();

    /**
     * Get the "id" personne.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Personne> findOne(Long id);

    /**
     * Delete the "id" personne.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
