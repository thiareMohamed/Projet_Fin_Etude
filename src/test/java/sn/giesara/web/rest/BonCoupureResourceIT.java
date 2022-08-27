package sn.giesara.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.giesara.IntegrationTest;
import sn.giesara.domain.BonCoupure;
import sn.giesara.repository.BonCoupureRepository;

/**
 * Integration tests for the {@link BonCoupureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BonCoupureResourceIT {

    private static final String DEFAULT_CODE_BON = "AAAAAAAAAA";
    private static final String UPDATED_CODE_BON = "BBBBBBBBBB";

    private static final String DEFAULT_RAISON_COUPURE = "AAAAAAAAAA";
    private static final String UPDATED_RAISON_COUPURE = "BBBBBBBBBB";

    private static final Double DEFAULT_TAXE = 1D;
    private static final Double UPDATED_TAXE = 2D;

    private static final String ENTITY_API_URL = "/api/bon-coupures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BonCoupureRepository bonCoupureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBonCoupureMockMvc;

    private BonCoupure bonCoupure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BonCoupure createEntity(EntityManager em) {
        BonCoupure bonCoupure = new BonCoupure().codeBon(DEFAULT_CODE_BON).raisonCoupure(DEFAULT_RAISON_COUPURE).taxe(DEFAULT_TAXE);
        return bonCoupure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BonCoupure createUpdatedEntity(EntityManager em) {
        BonCoupure bonCoupure = new BonCoupure().codeBon(UPDATED_CODE_BON).raisonCoupure(UPDATED_RAISON_COUPURE).taxe(UPDATED_TAXE);
        return bonCoupure;
    }

    @BeforeEach
    public void initTest() {
        bonCoupure = createEntity(em);
    }

    @Test
    @Transactional
    void createBonCoupure() throws Exception {
        int databaseSizeBeforeCreate = bonCoupureRepository.findAll().size();
        // Create the BonCoupure
        restBonCoupureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bonCoupure)))
            .andExpect(status().isCreated());

        // Validate the BonCoupure in the database
        List<BonCoupure> bonCoupureList = bonCoupureRepository.findAll();
        assertThat(bonCoupureList).hasSize(databaseSizeBeforeCreate + 1);
        BonCoupure testBonCoupure = bonCoupureList.get(bonCoupureList.size() - 1);
        assertThat(testBonCoupure.getCodeBon()).isEqualTo(DEFAULT_CODE_BON);
        assertThat(testBonCoupure.getRaisonCoupure()).isEqualTo(DEFAULT_RAISON_COUPURE);
        assertThat(testBonCoupure.getTaxe()).isEqualTo(DEFAULT_TAXE);
    }

    @Test
    @Transactional
    void createBonCoupureWithExistingId() throws Exception {
        // Create the BonCoupure with an existing ID
        bonCoupure.setId(1L);

        int databaseSizeBeforeCreate = bonCoupureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBonCoupureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bonCoupure)))
            .andExpect(status().isBadRequest());

        // Validate the BonCoupure in the database
        List<BonCoupure> bonCoupureList = bonCoupureRepository.findAll();
        assertThat(bonCoupureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBonCoupures() throws Exception {
        // Initialize the database
        bonCoupureRepository.saveAndFlush(bonCoupure);

        // Get all the bonCoupureList
        restBonCoupureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bonCoupure.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeBon").value(hasItem(DEFAULT_CODE_BON)))
            .andExpect(jsonPath("$.[*].raisonCoupure").value(hasItem(DEFAULT_RAISON_COUPURE)))
            .andExpect(jsonPath("$.[*].taxe").value(hasItem(DEFAULT_TAXE.doubleValue())));
    }

    @Test
    @Transactional
    void getBonCoupure() throws Exception {
        // Initialize the database
        bonCoupureRepository.saveAndFlush(bonCoupure);

        // Get the bonCoupure
        restBonCoupureMockMvc
            .perform(get(ENTITY_API_URL_ID, bonCoupure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bonCoupure.getId().intValue()))
            .andExpect(jsonPath("$.codeBon").value(DEFAULT_CODE_BON))
            .andExpect(jsonPath("$.raisonCoupure").value(DEFAULT_RAISON_COUPURE))
            .andExpect(jsonPath("$.taxe").value(DEFAULT_TAXE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingBonCoupure() throws Exception {
        // Get the bonCoupure
        restBonCoupureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBonCoupure() throws Exception {
        // Initialize the database
        bonCoupureRepository.saveAndFlush(bonCoupure);

        int databaseSizeBeforeUpdate = bonCoupureRepository.findAll().size();

        // Update the bonCoupure
        BonCoupure updatedBonCoupure = bonCoupureRepository.findById(bonCoupure.getId()).get();
        // Disconnect from session so that the updates on updatedBonCoupure are not directly saved in db
        em.detach(updatedBonCoupure);
        updatedBonCoupure.codeBon(UPDATED_CODE_BON).raisonCoupure(UPDATED_RAISON_COUPURE).taxe(UPDATED_TAXE);

        restBonCoupureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBonCoupure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBonCoupure))
            )
            .andExpect(status().isOk());

        // Validate the BonCoupure in the database
        List<BonCoupure> bonCoupureList = bonCoupureRepository.findAll();
        assertThat(bonCoupureList).hasSize(databaseSizeBeforeUpdate);
        BonCoupure testBonCoupure = bonCoupureList.get(bonCoupureList.size() - 1);
        assertThat(testBonCoupure.getCodeBon()).isEqualTo(UPDATED_CODE_BON);
        assertThat(testBonCoupure.getRaisonCoupure()).isEqualTo(UPDATED_RAISON_COUPURE);
        assertThat(testBonCoupure.getTaxe()).isEqualTo(UPDATED_TAXE);
    }

    @Test
    @Transactional
    void putNonExistingBonCoupure() throws Exception {
        int databaseSizeBeforeUpdate = bonCoupureRepository.findAll().size();
        bonCoupure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBonCoupureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bonCoupure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bonCoupure))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonCoupure in the database
        List<BonCoupure> bonCoupureList = bonCoupureRepository.findAll();
        assertThat(bonCoupureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBonCoupure() throws Exception {
        int databaseSizeBeforeUpdate = bonCoupureRepository.findAll().size();
        bonCoupure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonCoupureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bonCoupure))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonCoupure in the database
        List<BonCoupure> bonCoupureList = bonCoupureRepository.findAll();
        assertThat(bonCoupureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBonCoupure() throws Exception {
        int databaseSizeBeforeUpdate = bonCoupureRepository.findAll().size();
        bonCoupure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonCoupureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bonCoupure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BonCoupure in the database
        List<BonCoupure> bonCoupureList = bonCoupureRepository.findAll();
        assertThat(bonCoupureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBonCoupureWithPatch() throws Exception {
        // Initialize the database
        bonCoupureRepository.saveAndFlush(bonCoupure);

        int databaseSizeBeforeUpdate = bonCoupureRepository.findAll().size();

        // Update the bonCoupure using partial update
        BonCoupure partialUpdatedBonCoupure = new BonCoupure();
        partialUpdatedBonCoupure.setId(bonCoupure.getId());

        partialUpdatedBonCoupure.codeBon(UPDATED_CODE_BON).raisonCoupure(UPDATED_RAISON_COUPURE).taxe(UPDATED_TAXE);

        restBonCoupureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBonCoupure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBonCoupure))
            )
            .andExpect(status().isOk());

        // Validate the BonCoupure in the database
        List<BonCoupure> bonCoupureList = bonCoupureRepository.findAll();
        assertThat(bonCoupureList).hasSize(databaseSizeBeforeUpdate);
        BonCoupure testBonCoupure = bonCoupureList.get(bonCoupureList.size() - 1);
        assertThat(testBonCoupure.getCodeBon()).isEqualTo(UPDATED_CODE_BON);
        assertThat(testBonCoupure.getRaisonCoupure()).isEqualTo(UPDATED_RAISON_COUPURE);
        assertThat(testBonCoupure.getTaxe()).isEqualTo(UPDATED_TAXE);
    }

    @Test
    @Transactional
    void fullUpdateBonCoupureWithPatch() throws Exception {
        // Initialize the database
        bonCoupureRepository.saveAndFlush(bonCoupure);

        int databaseSizeBeforeUpdate = bonCoupureRepository.findAll().size();

        // Update the bonCoupure using partial update
        BonCoupure partialUpdatedBonCoupure = new BonCoupure();
        partialUpdatedBonCoupure.setId(bonCoupure.getId());

        partialUpdatedBonCoupure.codeBon(UPDATED_CODE_BON).raisonCoupure(UPDATED_RAISON_COUPURE).taxe(UPDATED_TAXE);

        restBonCoupureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBonCoupure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBonCoupure))
            )
            .andExpect(status().isOk());

        // Validate the BonCoupure in the database
        List<BonCoupure> bonCoupureList = bonCoupureRepository.findAll();
        assertThat(bonCoupureList).hasSize(databaseSizeBeforeUpdate);
        BonCoupure testBonCoupure = bonCoupureList.get(bonCoupureList.size() - 1);
        assertThat(testBonCoupure.getCodeBon()).isEqualTo(UPDATED_CODE_BON);
        assertThat(testBonCoupure.getRaisonCoupure()).isEqualTo(UPDATED_RAISON_COUPURE);
        assertThat(testBonCoupure.getTaxe()).isEqualTo(UPDATED_TAXE);
    }

    @Test
    @Transactional
    void patchNonExistingBonCoupure() throws Exception {
        int databaseSizeBeforeUpdate = bonCoupureRepository.findAll().size();
        bonCoupure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBonCoupureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bonCoupure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bonCoupure))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonCoupure in the database
        List<BonCoupure> bonCoupureList = bonCoupureRepository.findAll();
        assertThat(bonCoupureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBonCoupure() throws Exception {
        int databaseSizeBeforeUpdate = bonCoupureRepository.findAll().size();
        bonCoupure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonCoupureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bonCoupure))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonCoupure in the database
        List<BonCoupure> bonCoupureList = bonCoupureRepository.findAll();
        assertThat(bonCoupureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBonCoupure() throws Exception {
        int databaseSizeBeforeUpdate = bonCoupureRepository.findAll().size();
        bonCoupure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonCoupureMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bonCoupure))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BonCoupure in the database
        List<BonCoupure> bonCoupureList = bonCoupureRepository.findAll();
        assertThat(bonCoupureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBonCoupure() throws Exception {
        // Initialize the database
        bonCoupureRepository.saveAndFlush(bonCoupure);

        int databaseSizeBeforeDelete = bonCoupureRepository.findAll().size();

        // Delete the bonCoupure
        restBonCoupureMockMvc
            .perform(delete(ENTITY_API_URL_ID, bonCoupure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BonCoupure> bonCoupureList = bonCoupureRepository.findAll();
        assertThat(bonCoupureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
