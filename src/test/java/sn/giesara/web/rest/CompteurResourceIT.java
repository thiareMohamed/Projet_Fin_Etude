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
import sn.giesara.domain.Compteur;
import sn.giesara.repository.CompteurRepository;

/**
 * Integration tests for the {@link CompteurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompteurResourceIT {

    private static final Long DEFAULT_NUMERO_COMPTEUR = 1L;
    private static final Long UPDATED_NUMERO_COMPTEUR = 2L;

    private static final Integer DEFAULT_TYPE_COMPTEUR = 1;
    private static final Integer UPDATED_TYPE_COMPTEUR = 2;

    private static final Instant DEFAULT_DATE_ABONNEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ABONNEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MARQUE = "AAAAAAAAAA";
    private static final String UPDATED_MARQUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUT = false;
    private static final Boolean UPDATED_STATUT = true;

    private static final String ENTITY_API_URL = "/api/compteurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompteurRepository compteurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompteurMockMvc;

    private Compteur compteur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compteur createEntity(EntityManager em) {
        Compteur compteur = new Compteur()
            .numeroCompteur(DEFAULT_NUMERO_COMPTEUR)
            .typeCompteur(DEFAULT_TYPE_COMPTEUR)
            .dateAbonnement(DEFAULT_DATE_ABONNEMENT)
            .marque(DEFAULT_MARQUE)
            .statut(DEFAULT_STATUT);
        return compteur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compteur createUpdatedEntity(EntityManager em) {
        Compteur compteur = new Compteur()
            .numeroCompteur(UPDATED_NUMERO_COMPTEUR)
            .typeCompteur(UPDATED_TYPE_COMPTEUR)
            .dateAbonnement(UPDATED_DATE_ABONNEMENT)
            .marque(UPDATED_MARQUE)
            .statut(UPDATED_STATUT);
        return compteur;
    }

    @BeforeEach
    public void initTest() {
        compteur = createEntity(em);
    }

    @Test
    @Transactional
    void createCompteur() throws Exception {
        int databaseSizeBeforeCreate = compteurRepository.findAll().size();
        // Create the Compteur
        restCompteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteur)))
            .andExpect(status().isCreated());

        // Validate the Compteur in the database
        List<Compteur> compteurList = compteurRepository.findAll();
        assertThat(compteurList).hasSize(databaseSizeBeforeCreate + 1);
        Compteur testCompteur = compteurList.get(compteurList.size() - 1);
        assertThat(testCompteur.getNumeroCompteur()).isEqualTo(DEFAULT_NUMERO_COMPTEUR);
        assertThat(testCompteur.getTypeCompteur()).isEqualTo(DEFAULT_TYPE_COMPTEUR);
        assertThat(testCompteur.getDateAbonnement()).isEqualTo(DEFAULT_DATE_ABONNEMENT);
        assertThat(testCompteur.getMarque()).isEqualTo(DEFAULT_MARQUE);
        assertThat(testCompteur.getStatut()).isEqualTo(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    void createCompteurWithExistingId() throws Exception {
        // Create the Compteur with an existing ID
        compteur.setId(1L);

        int databaseSizeBeforeCreate = compteurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteur)))
            .andExpect(status().isBadRequest());

        // Validate the Compteur in the database
        List<Compteur> compteurList = compteurRepository.findAll();
        assertThat(compteurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompteurs() throws Exception {
        // Initialize the database
        compteurRepository.saveAndFlush(compteur);

        // Get all the compteurList
        restCompteurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroCompteur").value(hasItem(DEFAULT_NUMERO_COMPTEUR.intValue())))
            .andExpect(jsonPath("$.[*].typeCompteur").value(hasItem(DEFAULT_TYPE_COMPTEUR)))
            .andExpect(jsonPath("$.[*].dateAbonnement").value(hasItem(DEFAULT_DATE_ABONNEMENT.toString())))
            .andExpect(jsonPath("$.[*].marque").value(hasItem(DEFAULT_MARQUE)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.booleanValue())));
    }

    @Test
    @Transactional
    void getCompteur() throws Exception {
        // Initialize the database
        compteurRepository.saveAndFlush(compteur);

        // Get the compteur
        restCompteurMockMvc
            .perform(get(ENTITY_API_URL_ID, compteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compteur.getId().intValue()))
            .andExpect(jsonPath("$.numeroCompteur").value(DEFAULT_NUMERO_COMPTEUR.intValue()))
            .andExpect(jsonPath("$.typeCompteur").value(DEFAULT_TYPE_COMPTEUR))
            .andExpect(jsonPath("$.dateAbonnement").value(DEFAULT_DATE_ABONNEMENT.toString()))
            .andExpect(jsonPath("$.marque").value(DEFAULT_MARQUE))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCompteur() throws Exception {
        // Get the compteur
        restCompteurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompteur() throws Exception {
        // Initialize the database
        compteurRepository.saveAndFlush(compteur);

        int databaseSizeBeforeUpdate = compteurRepository.findAll().size();

        // Update the compteur
        Compteur updatedCompteur = compteurRepository.findById(compteur.getId()).get();
        // Disconnect from session so that the updates on updatedCompteur are not directly saved in db
        em.detach(updatedCompteur);
        updatedCompteur
            .numeroCompteur(UPDATED_NUMERO_COMPTEUR)
            .typeCompteur(UPDATED_TYPE_COMPTEUR)
            .dateAbonnement(UPDATED_DATE_ABONNEMENT)
            .marque(UPDATED_MARQUE)
            .statut(UPDATED_STATUT);

        restCompteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompteur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompteur))
            )
            .andExpect(status().isOk());

        // Validate the Compteur in the database
        List<Compteur> compteurList = compteurRepository.findAll();
        assertThat(compteurList).hasSize(databaseSizeBeforeUpdate);
        Compteur testCompteur = compteurList.get(compteurList.size() - 1);
        assertThat(testCompteur.getNumeroCompteur()).isEqualTo(UPDATED_NUMERO_COMPTEUR);
        assertThat(testCompteur.getTypeCompteur()).isEqualTo(UPDATED_TYPE_COMPTEUR);
        assertThat(testCompteur.getDateAbonnement()).isEqualTo(UPDATED_DATE_ABONNEMENT);
        assertThat(testCompteur.getMarque()).isEqualTo(UPDATED_MARQUE);
        assertThat(testCompteur.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    void putNonExistingCompteur() throws Exception {
        int databaseSizeBeforeUpdate = compteurRepository.findAll().size();
        compteur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compteur in the database
        List<Compteur> compteurList = compteurRepository.findAll();
        assertThat(compteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompteur() throws Exception {
        int databaseSizeBeforeUpdate = compteurRepository.findAll().size();
        compteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compteur in the database
        List<Compteur> compteurList = compteurRepository.findAll();
        assertThat(compteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompteur() throws Exception {
        int databaseSizeBeforeUpdate = compteurRepository.findAll().size();
        compteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compteur in the database
        List<Compteur> compteurList = compteurRepository.findAll();
        assertThat(compteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompteurWithPatch() throws Exception {
        // Initialize the database
        compteurRepository.saveAndFlush(compteur);

        int databaseSizeBeforeUpdate = compteurRepository.findAll().size();

        // Update the compteur using partial update
        Compteur partialUpdatedCompteur = new Compteur();
        partialUpdatedCompteur.setId(compteur.getId());

        partialUpdatedCompteur.numeroCompteur(UPDATED_NUMERO_COMPTEUR).typeCompteur(UPDATED_TYPE_COMPTEUR).statut(UPDATED_STATUT);

        restCompteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompteur))
            )
            .andExpect(status().isOk());

        // Validate the Compteur in the database
        List<Compteur> compteurList = compteurRepository.findAll();
        assertThat(compteurList).hasSize(databaseSizeBeforeUpdate);
        Compteur testCompteur = compteurList.get(compteurList.size() - 1);
        assertThat(testCompteur.getNumeroCompteur()).isEqualTo(UPDATED_NUMERO_COMPTEUR);
        assertThat(testCompteur.getTypeCompteur()).isEqualTo(UPDATED_TYPE_COMPTEUR);
        assertThat(testCompteur.getDateAbonnement()).isEqualTo(DEFAULT_DATE_ABONNEMENT);
        assertThat(testCompteur.getMarque()).isEqualTo(DEFAULT_MARQUE);
        assertThat(testCompteur.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    void fullUpdateCompteurWithPatch() throws Exception {
        // Initialize the database
        compteurRepository.saveAndFlush(compteur);

        int databaseSizeBeforeUpdate = compteurRepository.findAll().size();

        // Update the compteur using partial update
        Compteur partialUpdatedCompteur = new Compteur();
        partialUpdatedCompteur.setId(compteur.getId());

        partialUpdatedCompteur
            .numeroCompteur(UPDATED_NUMERO_COMPTEUR)
            .typeCompteur(UPDATED_TYPE_COMPTEUR)
            .dateAbonnement(UPDATED_DATE_ABONNEMENT)
            .marque(UPDATED_MARQUE)
            .statut(UPDATED_STATUT);

        restCompteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompteur))
            )
            .andExpect(status().isOk());

        // Validate the Compteur in the database
        List<Compteur> compteurList = compteurRepository.findAll();
        assertThat(compteurList).hasSize(databaseSizeBeforeUpdate);
        Compteur testCompteur = compteurList.get(compteurList.size() - 1);
        assertThat(testCompteur.getNumeroCompteur()).isEqualTo(UPDATED_NUMERO_COMPTEUR);
        assertThat(testCompteur.getTypeCompteur()).isEqualTo(UPDATED_TYPE_COMPTEUR);
        assertThat(testCompteur.getDateAbonnement()).isEqualTo(UPDATED_DATE_ABONNEMENT);
        assertThat(testCompteur.getMarque()).isEqualTo(UPDATED_MARQUE);
        assertThat(testCompteur.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    void patchNonExistingCompteur() throws Exception {
        int databaseSizeBeforeUpdate = compteurRepository.findAll().size();
        compteur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compteur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compteur in the database
        List<Compteur> compteurList = compteurRepository.findAll();
        assertThat(compteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompteur() throws Exception {
        int databaseSizeBeforeUpdate = compteurRepository.findAll().size();
        compteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compteur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compteur in the database
        List<Compteur> compteurList = compteurRepository.findAll();
        assertThat(compteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompteur() throws Exception {
        int databaseSizeBeforeUpdate = compteurRepository.findAll().size();
        compteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(compteur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compteur in the database
        List<Compteur> compteurList = compteurRepository.findAll();
        assertThat(compteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompteur() throws Exception {
        // Initialize the database
        compteurRepository.saveAndFlush(compteur);

        int databaseSizeBeforeDelete = compteurRepository.findAll().size();

        // Delete the compteur
        restCompteurMockMvc
            .perform(delete(ENTITY_API_URL_ID, compteur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Compteur> compteurList = compteurRepository.findAll();
        assertThat(compteurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
