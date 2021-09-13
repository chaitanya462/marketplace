package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.Refereces;
import com.simplify.marketplace.domain.enumeration.RelationType;
import com.simplify.marketplace.repository.ReferecesRepository;
import com.simplify.marketplace.service.dto.ReferecesDTO;
import com.simplify.marketplace.service.mapper.ReferecesMapper;
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
 * Integration tests for the {@link ReferecesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReferecesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = ":fR@d.-\\;OT8";
    private static final String UPDATED_EMAIL = "g5@#e4.l?dm5V";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_LINK = "BBBBBBBBBB";

    private static final RelationType DEFAULT_RELATION_TYPE = RelationType.Supervisor;
    private static final RelationType UPDATED_RELATION_TYPE = RelationType.Peer;

    private static final String ENTITY_API_URL = "/api/refereces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReferecesRepository referecesRepository;

    @Autowired
    private ReferecesMapper referecesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReferecesMockMvc;

    private Refereces refereces;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Refereces createEntity(EntityManager em) {
        Refereces refereces = new Refereces()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .profileLink(DEFAULT_PROFILE_LINK)
            .relationType(DEFAULT_RELATION_TYPE);
        return refereces;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Refereces createUpdatedEntity(EntityManager em) {
        Refereces refereces = new Refereces()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .profileLink(UPDATED_PROFILE_LINK)
            .relationType(UPDATED_RELATION_TYPE);
        return refereces;
    }

    @BeforeEach
    public void initTest() {
        refereces = createEntity(em);
    }

    @Test
    @Transactional
    void createRefereces() throws Exception {
        int databaseSizeBeforeCreate = referecesRepository.findAll().size();
        // Create the Refereces
        ReferecesDTO referecesDTO = referecesMapper.toDto(refereces);
        restReferecesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referecesDTO)))
            .andExpect(status().isCreated());

        // Validate the Refereces in the database
        List<Refereces> referecesList = referecesRepository.findAll();
        assertThat(referecesList).hasSize(databaseSizeBeforeCreate + 1);
        Refereces testRefereces = referecesList.get(referecesList.size() - 1);
        assertThat(testRefereces.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRefereces.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testRefereces.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testRefereces.getProfileLink()).isEqualTo(DEFAULT_PROFILE_LINK);
        assertThat(testRefereces.getRelationType()).isEqualTo(DEFAULT_RELATION_TYPE);
    }

    @Test
    @Transactional
    void createReferecesWithExistingId() throws Exception {
        // Create the Refereces with an existing ID
        refereces.setId(1L);
        ReferecesDTO referecesDTO = referecesMapper.toDto(refereces);

        int databaseSizeBeforeCreate = referecesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReferecesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referecesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Refereces in the database
        List<Refereces> referecesList = referecesRepository.findAll();
        assertThat(referecesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRefereces() throws Exception {
        // Initialize the database
        referecesRepository.saveAndFlush(refereces);

        // Get all the referecesList
        restReferecesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(refereces.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].profileLink").value(hasItem(DEFAULT_PROFILE_LINK)))
            .andExpect(jsonPath("$.[*].relationType").value(hasItem(DEFAULT_RELATION_TYPE.toString())));
    }

    @Test
    @Transactional
    void getRefereces() throws Exception {
        // Initialize the database
        referecesRepository.saveAndFlush(refereces);

        // Get the refereces
        restReferecesMockMvc
            .perform(get(ENTITY_API_URL_ID, refereces.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(refereces.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.profileLink").value(DEFAULT_PROFILE_LINK))
            .andExpect(jsonPath("$.relationType").value(DEFAULT_RELATION_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRefereces() throws Exception {
        // Get the refereces
        restReferecesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRefereces() throws Exception {
        // Initialize the database
        referecesRepository.saveAndFlush(refereces);

        int databaseSizeBeforeUpdate = referecesRepository.findAll().size();

        // Update the refereces
        Refereces updatedRefereces = referecesRepository.findById(refereces.getId()).get();
        // Disconnect from session so that the updates on updatedRefereces are not directly saved in db
        em.detach(updatedRefereces);
        updatedRefereces
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .profileLink(UPDATED_PROFILE_LINK)
            .relationType(UPDATED_RELATION_TYPE);
        ReferecesDTO referecesDTO = referecesMapper.toDto(updatedRefereces);

        restReferecesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referecesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(referecesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Refereces in the database
        List<Refereces> referecesList = referecesRepository.findAll();
        assertThat(referecesList).hasSize(databaseSizeBeforeUpdate);
        Refereces testRefereces = referecesList.get(referecesList.size() - 1);
        assertThat(testRefereces.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRefereces.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRefereces.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testRefereces.getProfileLink()).isEqualTo(UPDATED_PROFILE_LINK);
        assertThat(testRefereces.getRelationType()).isEqualTo(UPDATED_RELATION_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingRefereces() throws Exception {
        int databaseSizeBeforeUpdate = referecesRepository.findAll().size();
        refereces.setId(count.incrementAndGet());

        // Create the Refereces
        ReferecesDTO referecesDTO = referecesMapper.toDto(refereces);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferecesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referecesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(referecesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refereces in the database
        List<Refereces> referecesList = referecesRepository.findAll();
        assertThat(referecesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRefereces() throws Exception {
        int databaseSizeBeforeUpdate = referecesRepository.findAll().size();
        refereces.setId(count.incrementAndGet());

        // Create the Refereces
        ReferecesDTO referecesDTO = referecesMapper.toDto(refereces);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferecesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(referecesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refereces in the database
        List<Refereces> referecesList = referecesRepository.findAll();
        assertThat(referecesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRefereces() throws Exception {
        int databaseSizeBeforeUpdate = referecesRepository.findAll().size();
        refereces.setId(count.incrementAndGet());

        // Create the Refereces
        ReferecesDTO referecesDTO = referecesMapper.toDto(refereces);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferecesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referecesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Refereces in the database
        List<Refereces> referecesList = referecesRepository.findAll();
        assertThat(referecesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReferecesWithPatch() throws Exception {
        // Initialize the database
        referecesRepository.saveAndFlush(refereces);

        int databaseSizeBeforeUpdate = referecesRepository.findAll().size();

        // Update the refereces using partial update
        Refereces partialUpdatedRefereces = new Refereces();
        partialUpdatedRefereces.setId(refereces.getId());

        partialUpdatedRefereces
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .profileLink(UPDATED_PROFILE_LINK)
            .relationType(UPDATED_RELATION_TYPE);

        restReferecesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRefereces.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRefereces))
            )
            .andExpect(status().isOk());

        // Validate the Refereces in the database
        List<Refereces> referecesList = referecesRepository.findAll();
        assertThat(referecesList).hasSize(databaseSizeBeforeUpdate);
        Refereces testRefereces = referecesList.get(referecesList.size() - 1);
        assertThat(testRefereces.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRefereces.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRefereces.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testRefereces.getProfileLink()).isEqualTo(UPDATED_PROFILE_LINK);
        assertThat(testRefereces.getRelationType()).isEqualTo(UPDATED_RELATION_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateReferecesWithPatch() throws Exception {
        // Initialize the database
        referecesRepository.saveAndFlush(refereces);

        int databaseSizeBeforeUpdate = referecesRepository.findAll().size();

        // Update the refereces using partial update
        Refereces partialUpdatedRefereces = new Refereces();
        partialUpdatedRefereces.setId(refereces.getId());

        partialUpdatedRefereces
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .profileLink(UPDATED_PROFILE_LINK)
            .relationType(UPDATED_RELATION_TYPE);

        restReferecesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRefereces.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRefereces))
            )
            .andExpect(status().isOk());

        // Validate the Refereces in the database
        List<Refereces> referecesList = referecesRepository.findAll();
        assertThat(referecesList).hasSize(databaseSizeBeforeUpdate);
        Refereces testRefereces = referecesList.get(referecesList.size() - 1);
        assertThat(testRefereces.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRefereces.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRefereces.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testRefereces.getProfileLink()).isEqualTo(UPDATED_PROFILE_LINK);
        assertThat(testRefereces.getRelationType()).isEqualTo(UPDATED_RELATION_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingRefereces() throws Exception {
        int databaseSizeBeforeUpdate = referecesRepository.findAll().size();
        refereces.setId(count.incrementAndGet());

        // Create the Refereces
        ReferecesDTO referecesDTO = referecesMapper.toDto(refereces);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferecesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, referecesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(referecesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refereces in the database
        List<Refereces> referecesList = referecesRepository.findAll();
        assertThat(referecesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRefereces() throws Exception {
        int databaseSizeBeforeUpdate = referecesRepository.findAll().size();
        refereces.setId(count.incrementAndGet());

        // Create the Refereces
        ReferecesDTO referecesDTO = referecesMapper.toDto(refereces);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferecesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(referecesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refereces in the database
        List<Refereces> referecesList = referecesRepository.findAll();
        assertThat(referecesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRefereces() throws Exception {
        int databaseSizeBeforeUpdate = referecesRepository.findAll().size();
        refereces.setId(count.incrementAndGet());

        // Create the Refereces
        ReferecesDTO referecesDTO = referecesMapper.toDto(refereces);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferecesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(referecesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Refereces in the database
        List<Refereces> referecesList = referecesRepository.findAll();
        assertThat(referecesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRefereces() throws Exception {
        // Initialize the database
        referecesRepository.saveAndFlush(refereces);

        int databaseSizeBeforeDelete = referecesRepository.findAll().size();

        // Delete the refereces
        restReferecesMockMvc
            .perform(delete(ENTITY_API_URL_ID, refereces.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Refereces> referecesList = referecesRepository.findAll();
        assertThat(referecesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
