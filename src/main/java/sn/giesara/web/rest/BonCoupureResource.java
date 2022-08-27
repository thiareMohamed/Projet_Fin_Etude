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
import sn.giesara.domain.BonCoupure;
import sn.giesara.repository.BonCoupureRepository;
import sn.giesara.service.BonCoupureService;
import sn.giesara.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.giesara.domain.BonCoupure}.
 */
@RestController
@RequestMapping("/api")
public class BonCoupureResource {

    private final Logger log = LoggerFactory.getLogger(BonCoupureResource.class);

    private static final String ENTITY_NAME = "bonCoupure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BonCoupureService bonCoupureService;

    private final BonCoupureRepository bonCoupureRepository;

    public BonCoupureResource(BonCoupureService bonCoupureService, BonCoupureRepository bonCoupureRepository) {
        this.bonCoupureService = bonCoupureService;
        this.bonCoupureRepository = bonCoupureRepository;
    }

    /**
     * {@code POST  /bon-coupures} : Create a new bonCoupure.
     *
     * @param bonCoupure the bonCoupure to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bonCoupure, or with status {@code 400 (Bad Request)} if the bonCoupure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bon-coupures")
    public ResponseEntity<BonCoupure> createBonCoupure(@RequestBody BonCoupure bonCoupure) throws URISyntaxException {
        log.debug("REST request to save BonCoupure : {}", bonCoupure);
        if (bonCoupure.getId() != null) {
            throw new BadRequestAlertException("A new bonCoupure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BonCoupure result = bonCoupureService.save(bonCoupure);
        return ResponseEntity
            .created(new URI("/api/bon-coupures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bon-coupures/:id} : Updates an existing bonCoupure.
     *
     * @param id the id of the bonCoupure to save.
     * @param bonCoupure the bonCoupure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bonCoupure,
     * or with status {@code 400 (Bad Request)} if the bonCoupure is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bonCoupure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bon-coupures/{id}")
    public ResponseEntity<BonCoupure> updateBonCoupure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BonCoupure bonCoupure
    ) throws URISyntaxException {
        log.debug("REST request to update BonCoupure : {}, {}", id, bonCoupure);
        if (bonCoupure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bonCoupure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bonCoupureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BonCoupure result = bonCoupureService.update(bonCoupure);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bonCoupure.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bon-coupures/:id} : Partial updates given fields of an existing bonCoupure, field will ignore if it is null
     *
     * @param id the id of the bonCoupure to save.
     * @param bonCoupure the bonCoupure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bonCoupure,
     * or with status {@code 400 (Bad Request)} if the bonCoupure is not valid,
     * or with status {@code 404 (Not Found)} if the bonCoupure is not found,
     * or with status {@code 500 (Internal Server Error)} if the bonCoupure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bon-coupures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BonCoupure> partialUpdateBonCoupure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BonCoupure bonCoupure
    ) throws URISyntaxException {
        log.debug("REST request to partial update BonCoupure partially : {}, {}", id, bonCoupure);
        if (bonCoupure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bonCoupure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bonCoupureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BonCoupure> result = bonCoupureService.partialUpdate(bonCoupure);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bonCoupure.getId().toString())
        );
    }

    /**
     * {@code GET  /bon-coupures} : get all the bonCoupures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bonCoupures in body.
     */
    @GetMapping("/bon-coupures")
    public List<BonCoupure> getAllBonCoupures() {
        log.debug("REST request to get all BonCoupures");
        return bonCoupureService.findAll();
    }

    /**
     * {@code GET  /bon-coupures/:id} : get the "id" bonCoupure.
     *
     * @param id the id of the bonCoupure to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bonCoupure, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bon-coupures/{id}")
    public ResponseEntity<BonCoupure> getBonCoupure(@PathVariable Long id) {
        log.debug("REST request to get BonCoupure : {}", id);
        Optional<BonCoupure> bonCoupure = bonCoupureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bonCoupure);
    }

    /**
     * {@code DELETE  /bon-coupures/:id} : delete the "id" bonCoupure.
     *
     * @param id the id of the bonCoupure to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bon-coupures/{id}")
    public ResponseEntity<Void> deleteBonCoupure(@PathVariable Long id) {
        log.debug("REST request to delete BonCoupure : {}", id);
        bonCoupureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
