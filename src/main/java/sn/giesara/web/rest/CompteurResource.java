package sn.giesara.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.giesara.domain.Compteur;
import sn.giesara.repository.CompteurRepository;
import sn.giesara.service.CompteurService;
import sn.giesara.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.giesara.domain.Compteur}.
 */
@RestController
@RequestMapping("/api")
public class CompteurResource {

    private final Logger log = LoggerFactory.getLogger(CompteurResource.class);

    private static final String ENTITY_NAME = "compteur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompteurService compteurService;

    private final CompteurRepository compteurRepository;

    public CompteurResource(CompteurService compteurService, CompteurRepository compteurRepository) {
        this.compteurService = compteurService;
        this.compteurRepository = compteurRepository;
    }

    /**
     * {@code POST  /compteurs} : Create a new compteur.
     *
     * @param compteur the compteur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compteur, or with status {@code 400 (Bad Request)} if the compteur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/compteurs")
    public ResponseEntity<Compteur> createCompteur(@RequestBody Compteur compteur) throws URISyntaxException {
        log.debug("REST request to save Compteur : {}", compteur);
        if (compteur.getId() != null) {
            throw new BadRequestAlertException("A new compteur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Compteur result = compteurService.save(compteur);
        return ResponseEntity
            .created(new URI("/api/compteurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /compteurs/:id} : Updates an existing compteur.
     *
     * @param id the id of the compteur to save.
     * @param compteur the compteur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compteur,
     * or with status {@code 400 (Bad Request)} if the compteur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compteur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/compteurs/{id}")
    public ResponseEntity<Compteur> updateCompteur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Compteur compteur
    ) throws URISyntaxException {
        log.debug("REST request to update Compteur : {}, {}", id, compteur);
        if (compteur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compteur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Compteur result = compteurService.update(compteur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compteur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /compteurs/:id} : Partial updates given fields of an existing compteur, field will ignore if it is null
     *
     * @param id the id of the compteur to save.
     * @param compteur the compteur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compteur,
     * or with status {@code 400 (Bad Request)} if the compteur is not valid,
     * or with status {@code 404 (Not Found)} if the compteur is not found,
     * or with status {@code 500 (Internal Server Error)} if the compteur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/compteurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Compteur> partialUpdateCompteur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Compteur compteur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Compteur partially : {}, {}", id, compteur);
        if (compteur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compteur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Compteur> result = compteurService.partialUpdate(compteur);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compteur.getId().toString())
        );
    }

    /**
     * {@code GET  /compteurs} : get all the compteurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compteurs in body.
     */
    @GetMapping("/compteurs")
    public List<Compteur> getAllCompteurs() {
        log.debug("REST request to get all Compteurs");
        return compteurService.findAll();
    }

    /**
     * {@code GET  /compteurs/:id} : get the "id" compteur.
     *
     * @param id the id of the compteur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compteur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/compteurs/{id}")
    public ResponseEntity<Compteur> getCompteur(@PathVariable Long id) {
        log.debug("REST request to get Compteur : {}", id);
        Optional<Compteur> compteur = compteurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compteur);
    }

    /**
     * {@code DELETE  /compteurs/:id} : delete the "id" compteur.
     *
     * @param id the id of the compteur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/compteurs/{id}")
    public ResponseEntity<Void> deleteCompteur(@PathVariable Long id) {
        log.debug("REST request to delete Compteur : {}", id);
        compteurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
