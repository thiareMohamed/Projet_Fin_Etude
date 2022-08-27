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
import sn.giesara.domain.Village;
import sn.giesara.repository.VillageRepository;
import sn.giesara.service.dto.VillageDTO;
import sn.giesara.service.mapper.VillageMapper;

/**
 * Integration tests for the {@link VillageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VillageResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Double DEFAULT_PRENOM_NOM_CHEF_VILLAGE = 1D;
    private static final Double UPDATED_PRENOM_NOM_CHEF_VILLAGE = 2D;

    private static final String ENTITY_API_URL = "/api/villages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VillageRepository villageRepository;

    @Autowired
    private VillageMapper villageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVillageMockMvc;

    private Village village;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Village createEntity(EntityManager em) {
        Village village = new Village().nom(DEFAULT_NOM).prenomNomChefVillage(DEFAULT_PRENOM_NOM_CHEF_VILLAGE);
        return village;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Village createUpdatedEntity(EntityManager em) {
        Village village = new Village().nom(UPDATED_NOM).prenomNomChefVillage(UPDATED_PRENOM_NOM_CHEF_VILLAGE);
        return village;
    }

    @BeforeEach
    public void initTest() {
        village = createEntity(em);
    }

    @Test
    @Transactional
    void createVillage() throws Exception {
        int databaseSizeBeforeCreate = villageRepository.findAll().size();
        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);
        restVillageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(villageDTO)))
            .andExpect(status().isCreated());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeCreate + 1);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testVillage.getPrenomNomChefVillage()).isEqualTo(DEFAULT_PRENOM_NOM_CHEF_VILLAGE);
    }

    @Test
    @Transactional
    void createVillageWithExistingId() throws Exception {
        // Create the Village with an existing ID
        village.setId(1L);
        VillageDTO villageDTO = villageMapper.toDto(village);

        int databaseSizeBeforeCreate = villageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVillageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(villageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVillages() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(village.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenomNomChefVillage").value(hasItem(DEFAULT_PRENOM_NOM_CHEF_VILLAGE.doubleValue())));
    }

    @Test
    @Transactional
    void getVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get the village
        restVillageMockMvc
            .perform(get(ENTITY_API_URL_ID, village.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(village.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenomNomChefVillage").value(DEFAULT_PRENOM_NOM_CHEF_VILLAGE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingVillage() throws Exception {
        // Get the village
        restVillageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeUpdate = villageRepository.findAll().size();

        // Update the village
        Village updatedVillage = villageRepository.findById(village.getId()).get();
        // Disconnect from session so that the updates on updatedVillage are not directly saved in db
        em.detach(updatedVillage);
        updatedVillage.nom(UPDATED_NOM).prenomNomChefVillage(UPDATED_PRENOM_NOM_CHEF_VILLAGE);
        VillageDTO villageDTO = villageMapper.toDto(updatedVillage);

        restVillageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, villageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testVillage.getPrenomNomChefVillage()).isEqualTo(UPDATED_PRENOM_NOM_CHEF_VILLAGE);
    }

    @Test
    @Transactional
    void putNonExistingVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, villageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(villageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVillageWithPatch() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeUpdate = villageRepository.findAll().size();

        // Update the village using partial update
        Village partialUpdatedVillage = new Village();
        partialUpdatedVillage.setId(village.getId());

        partialUpdatedVillage.nom(UPDATED_NOM);

        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVillage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVillage))
            )
            .andExpect(status().isOk());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testVillage.getPrenomNomChefVillage()).isEqualTo(DEFAULT_PRENOM_NOM_CHEF_VILLAGE);
    }

    @Test
    @Transactional
    void fullUpdateVillageWithPatch() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeUpdate = villageRepository.findAll().size();

        // Update the village using partial update
        Village partialUpdatedVillage = new Village();
        partialUpdatedVillage.setId(village.getId());

        partialUpdatedVillage.nom(UPDATED_NOM).prenomNomChefVillage(UPDATED_PRENOM_NOM_CHEF_VILLAGE);

        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVillage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVillage))
            )
            .andExpect(status().isOk());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testVillage.getPrenomNomChefVillage()).isEqualTo(UPDATED_PRENOM_NOM_CHEF_VILLAGE);
    }

    @Test
    @Transactional
    void patchNonExistingVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, villageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeDelete = villageRepository.findAll().size();

        // Delete the village
        restVillageMockMvc
            .perform(delete(ENTITY_API_URL_ID, village.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
