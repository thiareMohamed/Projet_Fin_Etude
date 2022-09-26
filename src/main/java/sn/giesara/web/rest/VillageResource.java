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
import sn.giesara.domain.Village;
import sn.giesara.repository.VillageRepository;
import sn.giesara.service.VillageService;
import sn.giesara.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.giesara.domain.Village}.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class VillageResource {

    private final Logger log = LoggerFactory.getLogger(VillageResource.class);

    private static final String ENTITY_NAME = "village";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VillageService villageService;

    private final VillageRepository villageRepository;

    public VillageResource(VillageService villageService, VillageRepository villageRepository) {
        this.villageService = villageService;
        this.villageRepository = villageRepository;
    }

    /**
     * {@code POST  /villages} : Create a new village.
     *
     * @param village the village to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new village, or with status {@code 400 (Bad Request)} if the village has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/villages")
    public ResponseEntity<Village> createVillage(@RequestBody Village village) throws URISyntaxException {
        log.debug("REST request to save Village : {}", village);
        if (village.getId() != null) {
            throw new BadRequestAlertException("A new village cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Village result = villageService.save(village);
        return ResponseEntity
            .created(new URI("/api/villages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /villages/:id} : Updates an existing village.
     *
     * @param id the id of the village to save.
     * @param village the village to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated village,
     * or with status {@code 400 (Bad Request)} if the village is not valid,
     * or with status {@code 500 (Internal Server Error)} if the village couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/villages/{id}")
    public ResponseEntity<Village> updateVillage(@PathVariable(value = "id", required = false) final Long id, @RequestBody Village village)
        throws URISyntaxException {
        log.debug("REST request to update Village : {}, {}", id, village);
        if (village.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, village.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!villageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Village result = villageService.update(village);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, village.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /villages/:id} : Partial updates given fields of an existing village, field will ignore if it is null
     *
     * @param id the id of the village to save.
     * @param village the village to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated village,
     * or with status {@code 400 (Bad Request)} if the village is not valid,
     * or with status {@code 404 (Not Found)} if the village is not found,
     * or with status {@code 500 (Internal Server Error)} if the village couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/villages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Village> partialUpdateVillage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Village village
    ) throws URISyntaxException {
        log.debug("REST request to partial update Village partially : {}, {}", id, village);
        if (village.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, village.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!villageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Village> result = villageService.partialUpdate(village);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, village.getId().toString())
        );
    }

    /**
     * {@code GET  /villages} : get all the villages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of villages in body.
     */
    @GetMapping("/villages")
    public List<Village> getAllVillages() {
        log.debug("REST request to get all Villages");
        return villageService.findAll();
    }

    /**
     * {@code GET  /villages/:id} : get the "id" village.
     *
     * @param id the id of the village to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the village, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/villages/{id}")
    public ResponseEntity<Village> getVillage(@PathVariable Long id) {
        log.debug("REST request to get Village : {}", id);
        Optional<Village> village = villageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(village);
    }

    /**
     * {@code DELETE  /villages/:id} : delete the "id" village.
     *
     * @param id the id of the village to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/villages/{id}")
    public ResponseEntity<Void> deleteVillage(@PathVariable Long id) {
        log.debug("REST request to delete Village : {}", id);
        villageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
