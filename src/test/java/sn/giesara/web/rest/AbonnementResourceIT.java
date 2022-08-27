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
import sn.giesara.domain.Abonnement;
import sn.giesara.repository.AbonnementRepository;

/**
 * Integration tests for the {@link AbonnementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AbonnementResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Double DEFAULT_PRIX_UNITAIRE = 1D;
    private static final Double UPDATED_PRIX_UNITAIRE = 2D;

    private static final String ENTITY_API_URL = "/api/abonnements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AbonnementRepository abonnementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAbonnementMockMvc;

    private Abonnement abonnement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Abonnement createEntity(EntityManager em) {
        Abonnement abonnement = new Abonnement().libelle(DEFAULT_LIBELLE).prixUnitaire(DEFAULT_PRIX_UNITAIRE);
        return abonnement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Abonnement createUpdatedEntity(EntityManager em) {
        Abonnement abonnement = new Abonnement().libelle(UPDATED_LIBELLE).prixUnitaire(UPDATED_PRIX_UNITAIRE);
        return abonnement;
    }

    @BeforeEach
    public void initTest() {
        abonnement = createEntity(em);
    }

    @Test
    @Transactional
    void createAbonnement() throws Exception {
        int databaseSizeBeforeCreate = abonnementRepository.findAll().size();
        // Create the Abonnement
        restAbonnementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonnement)))
            .andExpect(status().isCreated());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeCreate + 1);
        Abonnement testAbonnement = abonnementList.get(abonnementList.size() - 1);
        assertThat(testAbonnement.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testAbonnement.getPrixUnitaire()).isEqualTo(DEFAULT_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    void createAbonnementWithExistingId() throws Exception {
        // Create the Abonnement with an existing ID
        abonnement.setId(1L);

        int databaseSizeBeforeCreate = abonnementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAbonnementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonnement)))
            .andExpect(status().isBadRequest());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAbonnements() throws Exception {
        // Initialize the database
        abonnementRepository.saveAndFlush(abonnement);

        // Get all the abonnementList
        restAbonnementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(abonnement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.doubleValue())));
    }

    @Test
    @Transactional
    void getAbonnement() throws Exception {
        // Initialize the database
        abonnementRepository.saveAndFlush(abonnement);

        // Get the abonnement
        restAbonnementMockMvc
            .perform(get(ENTITY_API_URL_ID, abonnement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(abonnement.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.prixUnitaire").value(DEFAULT_PRIX_UNITAIRE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingAbonnement() throws Exception {
        // Get the abonnement
        restAbonnementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAbonnement() throws Exception {
        // Initialize the database
        abonnementRepository.saveAndFlush(abonnement);

        int databaseSizeBeforeUpdate = abonnementRepository.findAll().size();

        // Update the abonnement
        Abonnement updatedAbonnement = abonnementRepository.findById(abonnement.getId()).get();
        // Disconnect from session so that the updates on updatedAbonnement are not directly saved in db
        em.detach(updatedAbonnement);
        updatedAbonnement.libelle(UPDATED_LIBELLE).prixUnitaire(UPDATED_PRIX_UNITAIRE);

        restAbonnementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAbonnement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAbonnement))
            )
            .andExpect(status().isOk());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeUpdate);
        Abonnement testAbonnement = abonnementList.get(abonnementList.size() - 1);
        assertThat(testAbonnement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testAbonnement.getPrixUnitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    void putNonExistingAbonnement() throws Exception {
        int databaseSizeBeforeUpdate = abonnementRepository.findAll().size();
        abonnement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbonnementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, abonnement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(abonnement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAbonnement() throws Exception {
        int databaseSizeBeforeUpdate = abonnementRepository.findAll().size();
        abonnement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonnementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(abonnement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAbonnement() throws Exception {
        int databaseSizeBeforeUpdate = abonnementRepository.findAll().size();
        abonnement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonnementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonnement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAbonnementWithPatch() throws Exception {
        // Initialize the database
        abonnementRepository.saveAndFlush(abonnement);

        int databaseSizeBeforeUpdate = abonnementRepository.findAll().size();

        // Update the abonnement using partial update
        Abonnement partialUpdatedAbonnement = new Abonnement();
        partialUpdatedAbonnement.setId(abonnement.getId());

        partialUpdatedAbonnement.libelle(UPDATED_LIBELLE).prixUnitaire(UPDATED_PRIX_UNITAIRE);

        restAbonnementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbonnement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAbonnement))
            )
            .andExpect(status().isOk());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeUpdate);
        Abonnement testAbonnement = abonnementList.get(abonnementList.size() - 1);
        assertThat(testAbonnement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testAbonnement.getPrixUnitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    void fullUpdateAbonnementWithPatch() throws Exception {
        // Initialize the database
        abonnementRepository.saveAndFlush(abonnement);

        int databaseSizeBeforeUpdate = abonnementRepository.findAll().size();

        // Update the abonnement using partial update
        Abonnement partialUpdatedAbonnement = new Abonnement();
        partialUpdatedAbonnement.setId(abonnement.getId());

        partialUpdatedAbonnement.libelle(UPDATED_LIBELLE).prixUnitaire(UPDATED_PRIX_UNITAIRE);

        restAbonnementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbonnement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAbonnement))
            )
            .andExpect(status().isOk());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeUpdate);
        Abonnement testAbonnement = abonnementList.get(abonnementList.size() - 1);
        assertThat(testAbonnement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testAbonnement.getPrixUnitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingAbonnement() throws Exception {
        int databaseSizeBeforeUpdate = abonnementRepository.findAll().size();
        abonnement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbonnementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, abonnement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(abonnement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAbonnement() throws Exception {
        int databaseSizeBeforeUpdate = abonnementRepository.findAll().size();
        abonnement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonnementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(abonnement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAbonnement() throws Exception {
        int databaseSizeBeforeUpdate = abonnementRepository.findAll().size();
        abonnement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonnementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(abonnement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAbonnement() throws Exception {
        // Initialize the database
        abonnementRepository.saveAndFlush(abonnement);

        int databaseSizeBeforeDelete = abonnementRepository.findAll().size();

        // Delete the abonnement
        restAbonnementMockMvc
            .perform(delete(ENTITY_API_URL_ID, abonnement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
