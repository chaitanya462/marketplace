package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.SubjectMaster;
import com.simplify.marketplace.repository.SubjectMasterRepository;
import com.simplify.marketplace.service.dto.SubjectMasterDTO;
import com.simplify.marketplace.service.mapper.SubjectMasterMapper;
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
 * Integration tests for the {@link SubjectMasterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubjectMasterResourceIT {

    private static final String DEFAULT_SUBJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/subject-masters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubjectMasterRepository subjectMasterRepository;

    @Autowired
    private SubjectMasterMapper subjectMasterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubjectMasterMockMvc;

    private SubjectMaster subjectMaster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubjectMaster createEntity(EntityManager em) {
        SubjectMaster subjectMaster = new SubjectMaster().subjectName(DEFAULT_SUBJECT_NAME);
        return subjectMaster;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubjectMaster createUpdatedEntity(EntityManager em) {
        SubjectMaster subjectMaster = new SubjectMaster().subjectName(UPDATED_SUBJECT_NAME);
        return subjectMaster;
    }

    @BeforeEach
    public void initTest() {
        subjectMaster = createEntity(em);
    }

    @Test
    @Transactional
    void createSubjectMaster() throws Exception {
        int databaseSizeBeforeCreate = subjectMasterRepository.findAll().size();
        // Create the SubjectMaster
        SubjectMasterDTO subjectMasterDTO = subjectMasterMapper.toDto(subjectMaster);
        restSubjectMasterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subjectMasterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SubjectMaster in the database
        List<SubjectMaster> subjectMasterList = subjectMasterRepository.findAll();
        assertThat(subjectMasterList).hasSize(databaseSizeBeforeCreate + 1);
        SubjectMaster testSubjectMaster = subjectMasterList.get(subjectMasterList.size() - 1);
        assertThat(testSubjectMaster.getSubjectName()).isEqualTo(DEFAULT_SUBJECT_NAME);
    }

    @Test
    @Transactional
    void createSubjectMasterWithExistingId() throws Exception {
        // Create the SubjectMaster with an existing ID
        subjectMaster.setId(1L);
        SubjectMasterDTO subjectMasterDTO = subjectMasterMapper.toDto(subjectMaster);

        int databaseSizeBeforeCreate = subjectMasterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubjectMasterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subjectMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubjectMaster in the database
        List<SubjectMaster> subjectMasterList = subjectMasterRepository.findAll();
        assertThat(subjectMasterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSubjectMasters() throws Exception {
        // Initialize the database
        subjectMasterRepository.saveAndFlush(subjectMaster);

        // Get all the subjectMasterList
        restSubjectMasterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subjectMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].subjectName").value(hasItem(DEFAULT_SUBJECT_NAME)));
    }

    @Test
    @Transactional
    void getSubjectMaster() throws Exception {
        // Initialize the database
        subjectMasterRepository.saveAndFlush(subjectMaster);

        // Get the subjectMaster
        restSubjectMasterMockMvc
            .perform(get(ENTITY_API_URL_ID, subjectMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subjectMaster.getId().intValue()))
            .andExpect(jsonPath("$.subjectName").value(DEFAULT_SUBJECT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSubjectMaster() throws Exception {
        // Get the subjectMaster
        restSubjectMasterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubjectMaster() throws Exception {
        // Initialize the database
        subjectMasterRepository.saveAndFlush(subjectMaster);

        int databaseSizeBeforeUpdate = subjectMasterRepository.findAll().size();

        // Update the subjectMaster
        SubjectMaster updatedSubjectMaster = subjectMasterRepository.findById(subjectMaster.getId()).get();
        // Disconnect from session so that the updates on updatedSubjectMaster are not directly saved in db
        em.detach(updatedSubjectMaster);
        updatedSubjectMaster.subjectName(UPDATED_SUBJECT_NAME);
        SubjectMasterDTO subjectMasterDTO = subjectMasterMapper.toDto(updatedSubjectMaster);

        restSubjectMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subjectMasterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subjectMasterDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubjectMaster in the database
        List<SubjectMaster> subjectMasterList = subjectMasterRepository.findAll();
        assertThat(subjectMasterList).hasSize(databaseSizeBeforeUpdate);
        SubjectMaster testSubjectMaster = subjectMasterList.get(subjectMasterList.size() - 1);
        assertThat(testSubjectMaster.getSubjectName()).isEqualTo(UPDATED_SUBJECT_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSubjectMaster() throws Exception {
        int databaseSizeBeforeUpdate = subjectMasterRepository.findAll().size();
        subjectMaster.setId(count.incrementAndGet());

        // Create the SubjectMaster
        SubjectMasterDTO subjectMasterDTO = subjectMasterMapper.toDto(subjectMaster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubjectMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subjectMasterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subjectMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubjectMaster in the database
        List<SubjectMaster> subjectMasterList = subjectMasterRepository.findAll();
        assertThat(subjectMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubjectMaster() throws Exception {
        int databaseSizeBeforeUpdate = subjectMasterRepository.findAll().size();
        subjectMaster.setId(count.incrementAndGet());

        // Create the SubjectMaster
        SubjectMasterDTO subjectMasterDTO = subjectMasterMapper.toDto(subjectMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subjectMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubjectMaster in the database
        List<SubjectMaster> subjectMasterList = subjectMasterRepository.findAll();
        assertThat(subjectMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubjectMaster() throws Exception {
        int databaseSizeBeforeUpdate = subjectMasterRepository.findAll().size();
        subjectMaster.setId(count.incrementAndGet());

        // Create the SubjectMaster
        SubjectMasterDTO subjectMasterDTO = subjectMasterMapper.toDto(subjectMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectMasterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subjectMasterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubjectMaster in the database
        List<SubjectMaster> subjectMasterList = subjectMasterRepository.findAll();
        assertThat(subjectMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubjectMasterWithPatch() throws Exception {
        // Initialize the database
        subjectMasterRepository.saveAndFlush(subjectMaster);

        int databaseSizeBeforeUpdate = subjectMasterRepository.findAll().size();

        // Update the subjectMaster using partial update
        SubjectMaster partialUpdatedSubjectMaster = new SubjectMaster();
        partialUpdatedSubjectMaster.setId(subjectMaster.getId());

        restSubjectMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubjectMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubjectMaster))
            )
            .andExpect(status().isOk());

        // Validate the SubjectMaster in the database
        List<SubjectMaster> subjectMasterList = subjectMasterRepository.findAll();
        assertThat(subjectMasterList).hasSize(databaseSizeBeforeUpdate);
        SubjectMaster testSubjectMaster = subjectMasterList.get(subjectMasterList.size() - 1);
        assertThat(testSubjectMaster.getSubjectName()).isEqualTo(DEFAULT_SUBJECT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSubjectMasterWithPatch() throws Exception {
        // Initialize the database
        subjectMasterRepository.saveAndFlush(subjectMaster);

        int databaseSizeBeforeUpdate = subjectMasterRepository.findAll().size();

        // Update the subjectMaster using partial update
        SubjectMaster partialUpdatedSubjectMaster = new SubjectMaster();
        partialUpdatedSubjectMaster.setId(subjectMaster.getId());

        partialUpdatedSubjectMaster.subjectName(UPDATED_SUBJECT_NAME);

        restSubjectMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubjectMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubjectMaster))
            )
            .andExpect(status().isOk());

        // Validate the SubjectMaster in the database
        List<SubjectMaster> subjectMasterList = subjectMasterRepository.findAll();
        assertThat(subjectMasterList).hasSize(databaseSizeBeforeUpdate);
        SubjectMaster testSubjectMaster = subjectMasterList.get(subjectMasterList.size() - 1);
        assertThat(testSubjectMaster.getSubjectName()).isEqualTo(UPDATED_SUBJECT_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSubjectMaster() throws Exception {
        int databaseSizeBeforeUpdate = subjectMasterRepository.findAll().size();
        subjectMaster.setId(count.incrementAndGet());

        // Create the SubjectMaster
        SubjectMasterDTO subjectMasterDTO = subjectMasterMapper.toDto(subjectMaster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubjectMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subjectMasterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subjectMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubjectMaster in the database
        List<SubjectMaster> subjectMasterList = subjectMasterRepository.findAll();
        assertThat(subjectMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubjectMaster() throws Exception {
        int databaseSizeBeforeUpdate = subjectMasterRepository.findAll().size();
        subjectMaster.setId(count.incrementAndGet());

        // Create the SubjectMaster
        SubjectMasterDTO subjectMasterDTO = subjectMasterMapper.toDto(subjectMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subjectMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubjectMaster in the database
        List<SubjectMaster> subjectMasterList = subjectMasterRepository.findAll();
        assertThat(subjectMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubjectMaster() throws Exception {
        int databaseSizeBeforeUpdate = subjectMasterRepository.findAll().size();
        subjectMaster.setId(count.incrementAndGet());

        // Create the SubjectMaster
        SubjectMasterDTO subjectMasterDTO = subjectMasterMapper.toDto(subjectMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectMasterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subjectMasterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubjectMaster in the database
        List<SubjectMaster> subjectMasterList = subjectMasterRepository.findAll();
        assertThat(subjectMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubjectMaster() throws Exception {
        // Initialize the database
        subjectMasterRepository.saveAndFlush(subjectMaster);

        int databaseSizeBeforeDelete = subjectMasterRepository.findAll().size();

        // Delete the subjectMaster
        restSubjectMasterMockMvc
            .perform(delete(ENTITY_API_URL_ID, subjectMaster.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubjectMaster> subjectMasterList = subjectMasterRepository.findAll();
        assertThat(subjectMasterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
