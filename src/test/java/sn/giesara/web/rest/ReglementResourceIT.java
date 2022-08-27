package sn.giesara.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import sn.giesara.domain.Reglement;
import sn.giesara.repository.ReglementRepository;

/**
 * Integration tests for the {@link ReglementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReglementResourceIT {

    private static final Double DEFAULT_SOMME_VERSE = 1D;
    private static final Double UPDATED_SOMME_VERSE = 2D;

    private static final Instant DEFAULT_DATE_REGLEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_REGLEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/reglements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReglementRepository reglementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReglementMockMvc;

    private Reglement reglement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reglement createEntity(EntityManager em) {
        Reglement reglement = new Reglement().sommeVerse(DEFAULT_SOMME_VERSE).dateReglement(DEFAULT_DATE_REGLEMENT);
        return reglement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reglement createUpdatedEntity(EntityManager em) {
        Reglement reglement = new Reglement().sommeVerse(UPDATED_SOMME_VERSE).dateReglement(UPDATED_DATE_REGLEMENT);
        return reglement;
    }

    @BeforeEach
    public void initTest() {
        reglement = createEntity(em);
    }

    @Test
    @Transactional
    void createReglement() throws Exception {
        int databaseSizeBeforeCreate = reglementRepository.findAll().size();
        // Create the Reglement
        restReglementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reglement)))
            .andExpect(status().isCreated());

        // Validate the Reglement in the database
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeCreate + 1);
        Reglement testReglement = reglementList.get(reglementList.size() - 1);
        assertThat(testReglement.getSommeVerse()).isEqualTo(DEFAULT_SOMME_VERSE);
        assertThat(testReglement.getDateReglement()).isEqualTo(DEFAULT_DATE_REGLEMENT);
    }

    @Test
    @Transactional
    void createReglementWithExistingId() throws Exception {
        // Create the Reglement with an existing ID
        reglement.setId(1L);

        int databaseSizeBeforeCreate = reglementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReglementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reglement)))
            .andExpect(status().isBadRequest());

        // Validate the Reglement in the database
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReglements() throws Exception {
        // Initialize the database
        reglementRepository.saveAndFlush(reglement);

        // Get all the reglementList
        restReglementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reglement.getId().intValue())))
            .andExpect(jsonPath("$.[*].sommeVerse").value(hasItem(DEFAULT_SOMME_VERSE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateReglement").value(hasItem(DEFAULT_DATE_REGLEMENT.toString())));
    }

    @Test
    @Transactional
    void getReglement() throws Exception {
        // Initialize the database
        reglementRepository.saveAndFlush(reglement);

        // Get the reglement
        restReglementMockMvc
            .perform(get(ENTITY_API_URL_ID, reglement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reglement.getId().intValue()))
            .andExpect(jsonPath("$.sommeVerse").value(DEFAULT_SOMME_VERSE.doubleValue()))
            .andExpect(jsonPath("$.dateReglement").value(DEFAULT_DATE_REGLEMENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReglement() throws Exception {
        // Get the reglement
        restReglementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReglement() throws Exception {
        // Initialize the database
        reglementRepository.saveAndFlush(reglement);

        int databaseSizeBeforeUpdate = reglementRepository.findAll().size();

        // Update the reglement
        Reglement updatedReglement = reglementRepository.findById(reglement.getId()).get();
        // Disconnect from session so that the updates on updatedReglement are not directly saved in db
        em.detach(updatedReglement);
        updatedReglement.sommeVerse(UPDATED_SOMME_VERSE).dateReglement(UPDATED_DATE_REGLEMENT);

        restReglementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReglement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReglement))
            )
            .andExpect(status().isOk());

        // Validate the Reglement in the database
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeUpdate);
        Reglement testReglement = reglementList.get(reglementList.size() - 1);
        assertThat(testReglement.getSommeVerse()).isEqualTo(UPDATED_SOMME_VERSE);
        assertThat(testReglement.getDateReglement()).isEqualTo(UPDATED_DATE_REGLEMENT);
    }

    @Test
    @Transactional
    void putNonExistingReglement() throws Exception {
        int databaseSizeBeforeUpdate = reglementRepository.findAll().size();
        reglement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReglementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reglement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reglement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reglement in the database
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReglement() throws Exception {
        int databaseSizeBeforeUpdate = reglementRepository.findAll().size();
        reglement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReglementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reglement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reglement in the database
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReglement() throws Exception {
        int databaseSizeBeforeUpdate = reglementRepository.findAll().size();
        reglement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReglementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reglement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reglement in the database
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReglementWithPatch() throws Exception {
        // Initialize the database
        reglementRepository.saveAndFlush(reglement);

        int databaseSizeBeforeUpdate = reglementRepository.findAll().size();

        // Update the reglement using partial update
        Reglement partialUpdatedReglement = new Reglement();
        partialUpdatedReglement.setId(reglement.getId());

        partialUpdatedReglement.dateReglement(UPDATED_DATE_REGLEMENT);

        restReglementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReglement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReglement))
            )
            .andExpect(status().isOk());

        // Validate the Reglement in the database
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeUpdate);
        Reglement testReglement = reglementList.get(reglementList.size() - 1);
        assertThat(testReglement.getSommeVerse()).isEqualTo(DEFAULT_SOMME_VERSE);
        assertThat(testReglement.getDateReglement()).isEqualTo(UPDATED_DATE_REGLEMENT);
    }

    @Test
    @Transactional
    void fullUpdateReglementWithPatch() throws Exception {
        // Initialize the database
        reglementRepository.saveAndFlush(reglement);

        int databaseSizeBeforeUpdate = reglementRepository.findAll().size();

        // Update the reglement using partial update
        Reglement partialUpdatedReglement = new Reglement();
        partialUpdatedReglement.setId(reglement.getId());

        partialUpdatedReglement.sommeVerse(UPDATED_SOMME_VERSE).dateReglement(UPDATED_DATE_REGLEMENT);

        restReglementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReglement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReglement))
            )
            .andExpect(status().isOk());

        // Validate the Reglement in the database
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeUpdate);
        Reglement testReglement = reglementList.get(reglementList.size() - 1);
        assertThat(testReglement.getSommeVerse()).isEqualTo(UPDATED_SOMME_VERSE);
        assertThat(testReglement.getDateReglement()).isEqualTo(UPDATED_DATE_REGLEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingReglement() throws Exception {
        int databaseSizeBeforeUpdate = reglementRepository.findAll().size();
        reglement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReglementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reglement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reglement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reglement in the database
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReglement() throws Exception {
        int databaseSizeBeforeUpdate = reglementRepository.findAll().size();
        reglement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReglementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reglement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reglement in the database
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReglement() throws Exception {
        int databaseSizeBeforeUpdate = reglementRepository.findAll().size();
        reglement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReglementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reglement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reglement in the database
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReglement() throws Exception {
        // Initialize the database
        reglementRepository.saveAndFlush(reglement);

        int databaseSizeBeforeDelete = reglementRepository.findAll().size();

        // Delete the reglement
        restReglementMockMvc
            .perform(delete(ENTITY_API_URL_ID, reglement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
