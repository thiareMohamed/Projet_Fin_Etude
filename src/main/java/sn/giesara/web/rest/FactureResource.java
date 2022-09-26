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
import sn.giesara.domain.Facture;
import sn.giesara.repository.FactureRepository;
import sn.giesara.service.FactureService;
import sn.giesara.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.giesara.domain.Facture}.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FactureResource {

    private final Logger log = LoggerFactory.getLogger(FactureResource.class);

    private static final String ENTITY_NAME = "facture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactureService factureService;

    private final FactureRepository factureRepository;

    public FactureResource(FactureService factureService, FactureRepository factureRepository) {
        this.factureService = factureService;
        this.factureRepository = factureRepository;
    }

    /**
     * {@code POST  /factures} : Create a new facture.
     *
     * @param facture the facture to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facture, or with status {@code 400 (Bad Request)} if the facture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/factures")
    public ResponseEntity<Facture> createFacture(@RequestBody Facture facture) throws URISyntaxException {
        log.debug("REST request to save Facture : {}", facture);
        if (facture.getId() != null) {
            throw new BadRequestAlertException("A new facture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Facture result = factureService.save(facture);
        return ResponseEntity
            .created(new URI("/api/factures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /factures/:id} : Updates an existing facture.
     *
     * @param id the id of the facture to save.
     * @param facture the facture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facture,
     * or with status {@code 400 (Bad Request)} if the facture is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/factures/{id}")
    public ResponseEntity<Facture> updateFacture(@PathVariable(value = "id", required = false) final Long id, @RequestBody Facture facture)
        throws URISyntaxException {
        log.debug("REST request to update Facture : {}, {}", id, facture);
        if (facture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Facture result = factureService.update(facture);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facture.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /factures/:id} : Partial updates given fields of an existing facture, field will ignore if it is null
     *
     * @param id the id of the facture to save.
     * @param facture the facture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facture,
     * or with status {@code 400 (Bad Request)} if the facture is not valid,
     * or with status {@code 404 (Not Found)} if the facture is not found,
     * or with status {@code 500 (Internal Server Error)} if the facture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/factures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Facture> partialUpdateFacture(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Facture facture
    ) throws URISyntaxException {
        log.debug("REST request to partial update Facture partially : {}, {}", id, facture);
        if (facture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Facture> result = factureService.partialUpdate(facture);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facture.getId().toString())
        );
    }

    /**
     * {@code GET  /factures} : get all the factures.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factures in body.
     */
    @GetMapping("/factures")
    public List<Facture> getAllFactures(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Factures");
        return factureService.findAll();
    }

    /**
     * {@code GET  /factures/:id} : get the "id" facture.
     *
     * @param id the id of the facture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/factures/{id}")
    public ResponseEntity<Facture> getFacture(@PathVariable Long id) {
        log.debug("REST request to get Facture : {}", id);
        Optional<Facture> facture = factureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facture);
    }

    /**
     * {@code DELETE  /factures/:id} : delete the "id" facture.
     *
     * @param id the id of the facture to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/factures/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        log.debug("REST request to delete Facture : {}", id);
        factureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
