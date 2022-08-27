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
import sn.giesara.domain.Abonnement;
import sn.giesara.repository.AbonnementRepository;
import sn.giesara.service.AbonnementService;
import sn.giesara.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.giesara.domain.Abonnement}.
 */
@RestController
@RequestMapping("/api")
public class AbonnementResource {

    private final Logger log = LoggerFactory.getLogger(AbonnementResource.class);

    private static final String ENTITY_NAME = "abonnement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AbonnementService abonnementService;

    private final AbonnementRepository abonnementRepository;

    public AbonnementResource(AbonnementService abonnementService, AbonnementRepository abonnementRepository) {
        this.abonnementService = abonnementService;
        this.abonnementRepository = abonnementRepository;
    }

    /**
     * {@code POST  /abonnements} : Create a new abonnement.
     *
     * @param abonnement the abonnement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new abonnement, or with status {@code 400 (Bad Request)} if the abonnement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/abonnements")
    public ResponseEntity<Abonnement> createAbonnement(@RequestBody Abonnement abonnement) throws URISyntaxException {
        log.debug("REST request to save Abonnement : {}", abonnement);
        if (abonnement.getId() != null) {
            throw new BadRequestAlertException("A new abonnement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Abonnement result = abonnementService.save(abonnement);
        return ResponseEntity
            .created(new URI("/api/abonnements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /abonnements/:id} : Updates an existing abonnement.
     *
     * @param id the id of the abonnement to save.
     * @param abonnement the abonnement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated abonnement,
     * or with status {@code 400 (Bad Request)} if the abonnement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the abonnement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/abonnements/{id}")
    public ResponseEntity<Abonnement> updateAbonnement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Abonnement abonnement
    ) throws URISyntaxException {
        log.debug("REST request to update Abonnement : {}, {}", id, abonnement);
        if (abonnement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, abonnement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!abonnementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Abonnement result = abonnementService.update(abonnement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, abonnement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /abonnements/:id} : Partial updates given fields of an existing abonnement, field will ignore if it is null
     *
     * @param id the id of the abonnement to save.
     * @param abonnement the abonnement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated abonnement,
     * or with status {@code 400 (Bad Request)} if the abonnement is not valid,
     * or with status {@code 404 (Not Found)} if the abonnement is not found,
     * or with status {@code 500 (Internal Server Error)} if the abonnement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/abonnements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Abonnement> partialUpdateAbonnement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Abonnement abonnement
    ) throws URISyntaxException {
        log.debug("REST request to partial update Abonnement partially : {}, {}", id, abonnement);
        if (abonnement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, abonnement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!abonnementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Abonnement> result = abonnementService.partialUpdate(abonnement);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, abonnement.getId().toString())
        );
    }

    /**
     * {@code GET  /abonnements} : get all the abonnements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of abonnements in body.
     */
    @GetMapping("/abonnements")
    public List<Abonnement> getAllAbonnements() {
        log.debug("REST request to get all Abonnements");
        return abonnementService.findAll();
    }

    /**
     * {@code GET  /abonnements/:id} : get the "id" abonnement.
     *
     * @param id the id of the abonnement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the abonnement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/abonnements/{id}")
    public ResponseEntity<Abonnement> getAbonnement(@PathVariable Long id) {
        log.debug("REST request to get Abonnement : {}", id);
        Optional<Abonnement> abonnement = abonnementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(abonnement);
    }

    /**
     * {@code DELETE  /abonnements/:id} : delete the "id" abonnement.
     *
     * @param id the id of the abonnement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/abonnements/{id}")
    public ResponseEntity<Void> deleteAbonnement(@PathVariable Long id) {
        log.debug("REST request to delete Abonnement : {}", id);
        abonnementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
