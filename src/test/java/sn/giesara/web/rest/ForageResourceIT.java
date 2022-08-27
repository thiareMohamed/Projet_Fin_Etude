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
import sn.giesara.domain.Forage;
import sn.giesara.repository.ForageRepository;

/**
 * Integration tests for the {@link ForageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ForageResourceIT {

    private static final String DEFAULT_NOM_SITE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_SITE = "BBBBBBBBBB";

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Instant DEFAULT_DATE_INSTALATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_INSTALATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_PROFONDEUR_FORAGE = 1D;
    private static final Double UPDATED_PROFONDEUR_FORAGE = 2D;

    private static final Double DEFAULT_HAUTEUR = 1D;
    private static final Double UPDATED_HAUTEUR = 2D;

    private static final Double DEFAULT_CAPACITE = 1D;
    private static final Double UPDATED_CAPACITE = 2D;

    private static final Double DEFAULT_HAUTEUR_SOUS_RADIER = 1D;
    private static final Double UPDATED_HAUTEUR_SOUS_RADIER = 2D;

    private static final String DEFAULT_TYPE_PARATONNERRE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_PARATONNERRE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_RESERVOIR = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_RESERVOIR = "BBBBBBBBBB";

    private static final Double DEFAULT_CAPACITE_RESERVOIR = 1D;
    private static final Double UPDATED_CAPACITE_RESERVOIR = 2D;

    private static final String ENTITY_API_URL = "/api/forages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ForageRepository forageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restForageMockMvc;

    private Forage forage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Forage createEntity(EntityManager em) {
        Forage forage = new Forage()
            .nomSite(DEFAULT_NOM_SITE)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .dateInstalation(DEFAULT_DATE_INSTALATION)
            .profondeurForage(DEFAULT_PROFONDEUR_FORAGE)
            .hauteur(DEFAULT_HAUTEUR)
            .capacite(DEFAULT_CAPACITE)
            .hauteurSousRadier(DEFAULT_HAUTEUR_SOUS_RADIER)
            .typeParatonnerre(DEFAULT_TYPE_PARATONNERRE)
            .typeReservoir(DEFAULT_TYPE_RESERVOIR)
            .capaciteReservoir(DEFAULT_CAPACITE_RESERVOIR);
        return forage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Forage createUpdatedEntity(EntityManager em) {
        Forage forage = new Forage()
            .nomSite(UPDATED_NOM_SITE)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .dateInstalation(UPDATED_DATE_INSTALATION)
            .profondeurForage(UPDATED_PROFONDEUR_FORAGE)
            .hauteur(UPDATED_HAUTEUR)
            .capacite(UPDATED_CAPACITE)
            .hauteurSousRadier(UPDATED_HAUTEUR_SOUS_RADIER)
            .typeParatonnerre(UPDATED_TYPE_PARATONNERRE)
            .typeReservoir(UPDATED_TYPE_RESERVOIR)
            .capaciteReservoir(UPDATED_CAPACITE_RESERVOIR);
        return forage;
    }

    @BeforeEach
    public void initTest() {
        forage = createEntity(em);
    }

    @Test
    @Transactional
    void createForage() throws Exception {
        int databaseSizeBeforeCreate = forageRepository.findAll().size();
        // Create the Forage
        restForageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(forage)))
            .andExpect(status().isCreated());

        // Validate the Forage in the database
        List<Forage> forageList = forageRepository.findAll();
        assertThat(forageList).hasSize(databaseSizeBeforeCreate + 1);
        Forage testForage = forageList.get(forageList.size() - 1);
        assertThat(testForage.getNomSite()).isEqualTo(DEFAULT_NOM_SITE);
        assertThat(testForage.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testForage.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testForage.getDateInstalation()).isEqualTo(DEFAULT_DATE_INSTALATION);
        assertThat(testForage.getProfondeurForage()).isEqualTo(DEFAULT_PROFONDEUR_FORAGE);
        assertThat(testForage.getHauteur()).isEqualTo(DEFAULT_HAUTEUR);
        assertThat(testForage.getCapacite()).isEqualTo(DEFAULT_CAPACITE);
        assertThat(testForage.getHauteurSousRadier()).isEqualTo(DEFAULT_HAUTEUR_SOUS_RADIER);
        assertThat(testForage.getTypeParatonnerre()).isEqualTo(DEFAULT_TYPE_PARATONNERRE);
        assertThat(testForage.getTypeReservoir()).isEqualTo(DEFAULT_TYPE_RESERVOIR);
        assertThat(testForage.getCapaciteReservoir()).isEqualTo(DEFAULT_CAPACITE_RESERVOIR);
    }

    @Test
    @Transactional
    void createForageWithExistingId() throws Exception {
        // Create the Forage with an existing ID
        forage.setId(1L);

        int databaseSizeBeforeCreate = forageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restForageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(forage)))
            .andExpect(status().isBadRequest());

        // Validate the Forage in the database
        List<Forage> forageList = forageRepository.findAll();
        assertThat(forageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllForages() throws Exception {
        // Initialize the database
        forageRepository.saveAndFlush(forage);

        // Get all the forageList
        restForageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(forage.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomSite").value(hasItem(DEFAULT_NOM_SITE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateInstalation").value(hasItem(DEFAULT_DATE_INSTALATION.toString())))
            .andExpect(jsonPath("$.[*].profondeurForage").value(hasItem(DEFAULT_PROFONDEUR_FORAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].hauteur").value(hasItem(DEFAULT_HAUTEUR.doubleValue())))
            .andExpect(jsonPath("$.[*].capacite").value(hasItem(DEFAULT_CAPACITE.doubleValue())))
            .andExpect(jsonPath("$.[*].hauteurSousRadier").value(hasItem(DEFAULT_HAUTEUR_SOUS_RADIER.doubleValue())))
            .andExpect(jsonPath("$.[*].typeParatonnerre").value(hasItem(DEFAULT_TYPE_PARATONNERRE)))
            .andExpect(jsonPath("$.[*].typeReservoir").value(hasItem(DEFAULT_TYPE_RESERVOIR)))
            .andExpect(jsonPath("$.[*].capaciteReservoir").value(hasItem(DEFAULT_CAPACITE_RESERVOIR.doubleValue())));
    }

    @Test
    @Transactional
    void getForage() throws Exception {
        // Initialize the database
        forageRepository.saveAndFlush(forage);

        // Get the forage
        restForageMockMvc
            .perform(get(ENTITY_API_URL_ID, forage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(forage.getId().intValue()))
            .andExpect(jsonPath("$.nomSite").value(DEFAULT_NOM_SITE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.dateInstalation").value(DEFAULT_DATE_INSTALATION.toString()))
            .andExpect(jsonPath("$.profondeurForage").value(DEFAULT_PROFONDEUR_FORAGE.doubleValue()))
            .andExpect(jsonPath("$.hauteur").value(DEFAULT_HAUTEUR.doubleValue()))
            .andExpect(jsonPath("$.capacite").value(DEFAULT_CAPACITE.doubleValue()))
            .andExpect(jsonPath("$.hauteurSousRadier").value(DEFAULT_HAUTEUR_SOUS_RADIER.doubleValue()))
            .andExpect(jsonPath("$.typeParatonnerre").value(DEFAULT_TYPE_PARATONNERRE))
            .andExpect(jsonPath("$.typeReservoir").value(DEFAULT_TYPE_RESERVOIR))
            .andExpect(jsonPath("$.capaciteReservoir").value(DEFAULT_CAPACITE_RESERVOIR.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingForage() throws Exception {
        // Get the forage
        restForageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewForage() throws Exception {
        // Initialize the database
        forageRepository.saveAndFlush(forage);

        int databaseSizeBeforeUpdate = forageRepository.findAll().size();

        // Update the forage
        Forage updatedForage = forageRepository.findById(forage.getId()).get();
        // Disconnect from session so that the updates on updatedForage are not directly saved in db
        em.detach(updatedForage);
        updatedForage
            .nomSite(UPDATED_NOM_SITE)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .dateInstalation(UPDATED_DATE_INSTALATION)
            .profondeurForage(UPDATED_PROFONDEUR_FORAGE)
            .hauteur(UPDATED_HAUTEUR)
            .capacite(UPDATED_CAPACITE)
            .hauteurSousRadier(UPDATED_HAUTEUR_SOUS_RADIER)
            .typeParatonnerre(UPDATED_TYPE_PARATONNERRE)
            .typeReservoir(UPDATED_TYPE_RESERVOIR)
            .capaciteReservoir(UPDATED_CAPACITE_RESERVOIR);

        restForageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedForage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedForage))
            )
            .andExpect(status().isOk());

        // Validate the Forage in the database
        List<Forage> forageList = forageRepository.findAll();
        assertThat(forageList).hasSize(databaseSizeBeforeUpdate);
        Forage testForage = forageList.get(forageList.size() - 1);
        assertThat(testForage.getNomSite()).isEqualTo(UPDATED_NOM_SITE);
        assertThat(testForage.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testForage.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testForage.getDateInstalation()).isEqualTo(UPDATED_DATE_INSTALATION);
        assertThat(testForage.getProfondeurForage()).isEqualTo(UPDATED_PROFONDEUR_FORAGE);
        assertThat(testForage.getHauteur()).isEqualTo(UPDATED_HAUTEUR);
        assertThat(testForage.getCapacite()).isEqualTo(UPDATED_CAPACITE);
        assertThat(testForage.getHauteurSousRadier()).isEqualTo(UPDATED_HAUTEUR_SOUS_RADIER);
        assertThat(testForage.getTypeParatonnerre()).isEqualTo(UPDATED_TYPE_PARATONNERRE);
        assertThat(testForage.getTypeReservoir()).isEqualTo(UPDATED_TYPE_RESERVOIR);
        assertThat(testForage.getCapaciteReservoir()).isEqualTo(UPDATED_CAPACITE_RESERVOIR);
    }

    @Test
    @Transactional
    void putNonExistingForage() throws Exception {
        int databaseSizeBeforeUpdate = forageRepository.findAll().size();
        forage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restForageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, forage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(forage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forage in the database
        List<Forage> forageList = forageRepository.findAll();
        assertThat(forageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchForage() throws Exception {
        int databaseSizeBeforeUpdate = forageRepository.findAll().size();
        forage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restForageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(forage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forage in the database
        List<Forage> forageList = forageRepository.findAll();
        assertThat(forageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamForage() throws Exception {
        int databaseSizeBeforeUpdate = forageRepository.findAll().size();
        forage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restForageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(forage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Forage in the database
        List<Forage> forageList = forageRepository.findAll();
        assertThat(forageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateForageWithPatch() throws Exception {
        // Initialize the database
        forageRepository.saveAndFlush(forage);

        int databaseSizeBeforeUpdate = forageRepository.findAll().size();

        // Update the forage using partial update
        Forage partialUpdatedForage = new Forage();
        partialUpdatedForage.setId(forage.getId());

        partialUpdatedForage
            .longitude(UPDATED_LONGITUDE)
            .dateInstalation(UPDATED_DATE_INSTALATION)
            .capacite(UPDATED_CAPACITE)
            .typeParatonnerre(UPDATED_TYPE_PARATONNERRE)
            .typeReservoir(UPDATED_TYPE_RESERVOIR)
            .capaciteReservoir(UPDATED_CAPACITE_RESERVOIR);

        restForageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForage))
            )
            .andExpect(status().isOk());

        // Validate the Forage in the database
        List<Forage> forageList = forageRepository.findAll();
        assertThat(forageList).hasSize(databaseSizeBeforeUpdate);
        Forage testForage = forageList.get(forageList.size() - 1);
        assertThat(testForage.getNomSite()).isEqualTo(DEFAULT_NOM_SITE);
        assertThat(testForage.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testForage.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testForage.getDateInstalation()).isEqualTo(UPDATED_DATE_INSTALATION);
        assertThat(testForage.getProfondeurForage()).isEqualTo(DEFAULT_PROFONDEUR_FORAGE);
        assertThat(testForage.getHauteur()).isEqualTo(DEFAULT_HAUTEUR);
        assertThat(testForage.getCapacite()).isEqualTo(UPDATED_CAPACITE);
        assertThat(testForage.getHauteurSousRadier()).isEqualTo(DEFAULT_HAUTEUR_SOUS_RADIER);
        assertThat(testForage.getTypeParatonnerre()).isEqualTo(UPDATED_TYPE_PARATONNERRE);
        assertThat(testForage.getTypeReservoir()).isEqualTo(UPDATED_TYPE_RESERVOIR);
        assertThat(testForage.getCapaciteReservoir()).isEqualTo(UPDATED_CAPACITE_RESERVOIR);
    }

    @Test
    @Transactional
    void fullUpdateForageWithPatch() throws Exception {
        // Initialize the database
        forageRepository.saveAndFlush(forage);

        int databaseSizeBeforeUpdate = forageRepository.findAll().size();

        // Update the forage using partial update
        Forage partialUpdatedForage = new Forage();
        partialUpdatedForage.setId(forage.getId());

        partialUpdatedForage
            .nomSite(UPDATED_NOM_SITE)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .dateInstalation(UPDATED_DATE_INSTALATION)
            .profondeurForage(UPDATED_PROFONDEUR_FORAGE)
            .hauteur(UPDATED_HAUTEUR)
            .capacite(UPDATED_CAPACITE)
            .hauteurSousRadier(UPDATED_HAUTEUR_SOUS_RADIER)
            .typeParatonnerre(UPDATED_TYPE_PARATONNERRE)
            .typeReservoir(UPDATED_TYPE_RESERVOIR)
            .capaciteReservoir(UPDATED_CAPACITE_RESERVOIR);

        restForageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForage))
            )
            .andExpect(status().isOk());

        // Validate the Forage in the database
        List<Forage> forageList = forageRepository.findAll();
        assertThat(forageList).hasSize(databaseSizeBeforeUpdate);
        Forage testForage = forageList.get(forageList.size() - 1);
        assertThat(testForage.getNomSite()).isEqualTo(UPDATED_NOM_SITE);
        assertThat(testForage.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testForage.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testForage.getDateInstalation()).isEqualTo(UPDATED_DATE_INSTALATION);
        assertThat(testForage.getProfondeurForage()).isEqualTo(UPDATED_PROFONDEUR_FORAGE);
        assertThat(testForage.getHauteur()).isEqualTo(UPDATED_HAUTEUR);
        assertThat(testForage.getCapacite()).isEqualTo(UPDATED_CAPACITE);
        assertThat(testForage.getHauteurSousRadier()).isEqualTo(UPDATED_HAUTEUR_SOUS_RADIER);
        assertThat(testForage.getTypeParatonnerre()).isEqualTo(UPDATED_TYPE_PARATONNERRE);
        assertThat(testForage.getTypeReservoir()).isEqualTo(UPDATED_TYPE_RESERVOIR);
        assertThat(testForage.getCapaciteReservoir()).isEqualTo(UPDATED_CAPACITE_RESERVOIR);
    }

    @Test
    @Transactional
    void patchNonExistingForage() throws Exception {
        int databaseSizeBeforeUpdate = forageRepository.findAll().size();
        forage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restForageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, forage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(forage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forage in the database
        List<Forage> forageList = forageRepository.findAll();
        assertThat(forageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchForage() throws Exception {
        int databaseSizeBeforeUpdate = forageRepository.findAll().size();
        forage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restForageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(forage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forage in the database
        List<Forage> forageList = forageRepository.findAll();
        assertThat(forageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamForage() throws Exception {
        int databaseSizeBeforeUpdate = forageRepository.findAll().size();
        forage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restForageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(forage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Forage in the database
        List<Forage> forageList = forageRepository.findAll();
        assertThat(forageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteForage() throws Exception {
        // Initialize the database
        forageRepository.saveAndFlush(forage);

        int databaseSizeBeforeDelete = forageRepository.findAll().size();

        // Delete the forage
        restForageMockMvc
            .perform(delete(ENTITY_API_URL_ID, forage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Forage> forageList = forageRepository.findAll();
        assertThat(forageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
