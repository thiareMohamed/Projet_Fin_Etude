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
import sn.giesara.domain.Forage;
import sn.giesara.repository.ForageRepository;
import sn.giesara.service.ForageService;
import sn.giesara.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.giesara.domain.Forage}.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ForageResource {

    private final Logger log = LoggerFactory.getLogger(ForageResource.class);

    private static final String ENTITY_NAME = "forage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ForageService forageService;

    private final ForageRepository forageRepository;

    public ForageResource(ForageService forageService, ForageRepository forageRepository) {
        this.forageService = forageService;
        this.forageRepository = forageRepository;
    }

    /**
     * {@code POST  /forages} : Create a new forage.
     *
     * @param forage the forage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new forage, or with status {@code 400 (Bad Request)} if the forage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/forages")
    public ResponseEntity<Forage> createForage(@RequestBody Forage forage) throws URISyntaxException {
        log.debug("REST request to save Forage : {}", forage);
        if (forage.getId() != null) {
            throw new BadRequestAlertException("A new forage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Forage result = forageService.save(forage);
        return ResponseEntity
            .created(new URI("/api/forages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /forages/:id} : Updates an existing forage.
     *
     * @param id the id of the forage to save.
     * @param forage the forage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated forage,
     * or with status {@code 400 (Bad Request)} if the forage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the forage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/forages/{id}")
    public ResponseEntity<Forage> updateForage(@PathVariable(value = "id", required = false) final Long id, @RequestBody Forage forage)
        throws URISyntaxException {
        log.debug("REST request to update Forage : {}, {}", id, forage);
        if (forage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, forage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!forageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Forage result = forageService.update(forage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, forage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /forages/:id} : Partial updates given fields of an existing forage, field will ignore if it is null
     *
     * @param id the id of the forage to save.
     * @param forage the forage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated forage,
     * or with status {@code 400 (Bad Request)} if the forage is not valid,
     * or with status {@code 404 (Not Found)} if the forage is not found,
     * or with status {@code 500 (Internal Server Error)} if the forage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/forages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Forage> partialUpdateForage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Forage forage
    ) throws URISyntaxException {
        log.debug("REST request to partial update Forage partially : {}, {}", id, forage);
        if (forage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, forage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!forageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Forage> result = forageService.partialUpdate(forage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, forage.getId().toString())
        );
    }

    /**
     * {@code GET  /forages} : get all the forages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of forages in body.
     */
    @GetMapping("/forages")
    public List<Forage> getAllForages() {
        log.debug("REST request to get all Forages");
        return forageService.findAll();
    }

    /**
     * {@code GET  /forages/:id} : get the "id" forage.
     *
     * @param id the id of the forage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the forage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/forages/{id}")
    public ResponseEntity<Forage> getForage(@PathVariable Long id) {
        log.debug("REST request to get Forage : {}", id);
        Optional<Forage> forage = forageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(forage);
    }

    /**
     * {@code DELETE  /forages/:id} : delete the "id" forage.
     *
     * @param id the id of the forage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/forages/{id}")
    public ResponseEntity<Void> deleteForage(@PathVariable Long id) {
        log.debug("REST request to delete Forage : {}", id);
        forageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
