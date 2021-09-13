package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.LocationPrefrence;
import com.simplify.marketplace.repository.LocationPrefrenceRepository;
import com.simplify.marketplace.service.dto.LocationPrefrenceDTO;
import com.simplify.marketplace.service.mapper.LocationPrefrenceMapper;
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

/**
 * Integration tests for the {@link LocationPrefrenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationPrefrenceResourceIT {

    private static final Integer DEFAULT_PREFRENCE_ORDER = 1;
    private static final Integer UPDATED_PREFRENCE_ORDER = 2;

    private static final String ENTITY_API_URL = "/api/location-prefrences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocationPrefrenceRepository locationPrefrenceRepository;

    @Autowired
    private LocationPrefrenceMapper locationPrefrenceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationPrefrenceMockMvc;

    private LocationPrefrence locationPrefrence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationPrefrence createEntity(EntityManager em) {
        LocationPrefrence locationPrefrence = new LocationPrefrence().prefrenceOrder(DEFAULT_PREFRENCE_ORDER);
        return locationPrefrence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationPrefrence createUpdatedEntity(EntityManager em) {
        LocationPrefrence locationPrefrence = new LocationPrefrence().prefrenceOrder(UPDATED_PREFRENCE_ORDER);
        return locationPrefrence;
    }

    @BeforeEach
    public void initTest() {
        locationPrefrence = createEntity(em);
    }

    @Test
    @Transactional
    void createLocationPrefrence() throws Exception {
        int databaseSizeBeforeCreate = locationPrefrenceRepository.findAll().size();
        // Create the LocationPrefrence
        LocationPrefrenceDTO locationPrefrenceDTO = locationPrefrenceMapper.toDto(locationPrefrence);
        restLocationPrefrenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationPrefrenceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeCreate + 1);
        LocationPrefrence testLocationPrefrence = locationPrefrenceList.get(locationPrefrenceList.size() - 1);
        assertThat(testLocationPrefrence.getPrefrenceOrder()).isEqualTo(DEFAULT_PREFRENCE_ORDER);
    }

    @Test
    @Transactional
    void createLocationPrefrenceWithExistingId() throws Exception {
        // Create the LocationPrefrence with an existing ID
        locationPrefrence.setId(1L);
        LocationPrefrenceDTO locationPrefrenceDTO = locationPrefrenceMapper.toDto(locationPrefrence);

        int databaseSizeBeforeCreate = locationPrefrenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationPrefrenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationPrefrenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocationPrefrences() throws Exception {
        // Initialize the database
        locationPrefrenceRepository.saveAndFlush(locationPrefrence);

        // Get all the locationPrefrenceList
        restLocationPrefrenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationPrefrence.getId().intValue())))
            .andExpect(jsonPath("$.[*].prefrenceOrder").value(hasItem(DEFAULT_PREFRENCE_ORDER)));
    }

    @Test
    @Transactional
    void getLocationPrefrence() throws Exception {
        // Initialize the database
        locationPrefrenceRepository.saveAndFlush(locationPrefrence);

        // Get the locationPrefrence
        restLocationPrefrenceMockMvc
            .perform(get(ENTITY_API_URL_ID, locationPrefrence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locationPrefrence.getId().intValue()))
            .andExpect(jsonPath("$.prefrenceOrder").value(DEFAULT_PREFRENCE_ORDER));
    }

    @Test
    @Transactional
    void getNonExistingLocationPrefrence() throws Exception {
        // Get the locationPrefrence
        restLocationPrefrenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocationPrefrence() throws Exception {
        // Initialize the database
        locationPrefrenceRepository.saveAndFlush(locationPrefrence);

        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();

        // Update the locationPrefrence
        LocationPrefrence updatedLocationPrefrence = locationPrefrenceRepository.findById(locationPrefrence.getId()).get();
        // Disconnect from session so that the updates on updatedLocationPrefrence are not directly saved in db
        em.detach(updatedLocationPrefrence);
        updatedLocationPrefrence.prefrenceOrder(UPDATED_PREFRENCE_ORDER);
        LocationPrefrenceDTO locationPrefrenceDTO = locationPrefrenceMapper.toDto(updatedLocationPrefrence);

        restLocationPrefrenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationPrefrenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationPrefrenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);
        LocationPrefrence testLocationPrefrence = locationPrefrenceList.get(locationPrefrenceList.size() - 1);
        assertThat(testLocationPrefrence.getPrefrenceOrder()).isEqualTo(UPDATED_PREFRENCE_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingLocationPrefrence() throws Exception {
        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();
        locationPrefrence.setId(count.incrementAndGet());

        // Create the LocationPrefrence
        LocationPrefrenceDTO locationPrefrenceDTO = locationPrefrenceMapper.toDto(locationPrefrence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationPrefrenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationPrefrenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationPrefrenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocationPrefrence() throws Exception {
        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();
        locationPrefrence.setId(count.incrementAndGet());

        // Create the LocationPrefrence
        LocationPrefrenceDTO locationPrefrenceDTO = locationPrefrenceMapper.toDto(locationPrefrence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationPrefrenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationPrefrenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocationPrefrence() throws Exception {
        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();
        locationPrefrence.setId(count.incrementAndGet());

        // Create the LocationPrefrence
        LocationPrefrenceDTO locationPrefrenceDTO = locationPrefrenceMapper.toDto(locationPrefrence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationPrefrenceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationPrefrenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationPrefrenceWithPatch() throws Exception {
        // Initialize the database
        locationPrefrenceRepository.saveAndFlush(locationPrefrence);

        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();

        // Update the locationPrefrence using partial update
        LocationPrefrence partialUpdatedLocationPrefrence = new LocationPrefrence();
        partialUpdatedLocationPrefrence.setId(locationPrefrence.getId());

        restLocationPrefrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationPrefrence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationPrefrence))
            )
            .andExpect(status().isOk());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);
        LocationPrefrence testLocationPrefrence = locationPrefrenceList.get(locationPrefrenceList.size() - 1);
        assertThat(testLocationPrefrence.getPrefrenceOrder()).isEqualTo(DEFAULT_PREFRENCE_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateLocationPrefrenceWithPatch() throws Exception {
        // Initialize the database
        locationPrefrenceRepository.saveAndFlush(locationPrefrence);

        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();

        // Update the locationPrefrence using partial update
        LocationPrefrence partialUpdatedLocationPrefrence = new LocationPrefrence();
        partialUpdatedLocationPrefrence.setId(locationPrefrence.getId());

        partialUpdatedLocationPrefrence.prefrenceOrder(UPDATED_PREFRENCE_ORDER);

        restLocationPrefrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationPrefrence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationPrefrence))
            )
            .andExpect(status().isOk());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);
        LocationPrefrence testLocationPrefrence = locationPrefrenceList.get(locationPrefrenceList.size() - 1);
        assertThat(testLocationPrefrence.getPrefrenceOrder()).isEqualTo(UPDATED_PREFRENCE_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingLocationPrefrence() throws Exception {
        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();
        locationPrefrence.setId(count.incrementAndGet());

        // Create the LocationPrefrence
        LocationPrefrenceDTO locationPrefrenceDTO = locationPrefrenceMapper.toDto(locationPrefrence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationPrefrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationPrefrenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationPrefrenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocationPrefrence() throws Exception {
        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();
        locationPrefrence.setId(count.incrementAndGet());

        // Create the LocationPrefrence
        LocationPrefrenceDTO locationPrefrenceDTO = locationPrefrenceMapper.toDto(locationPrefrence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationPrefrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationPrefrenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocationPrefrence() throws Exception {
        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();
        locationPrefrence.setId(count.incrementAndGet());

        // Create the LocationPrefrence
        LocationPrefrenceDTO locationPrefrenceDTO = locationPrefrenceMapper.toDto(locationPrefrence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationPrefrenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationPrefrenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocationPrefrence() throws Exception {
        // Initialize the database
        locationPrefrenceRepository.saveAndFlush(locationPrefrence);

        int databaseSizeBeforeDelete = locationPrefrenceRepository.findAll().size();

        // Delete the locationPrefrence
        restLocationPrefrenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, locationPrefrence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
