package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.SkillsMaster;
import com.simplify.marketplace.repository.SkillsMasterRepository;
import com.simplify.marketplace.service.dto.SkillsMasterDTO;
import com.simplify.marketplace.service.mapper.SkillsMasterMapper;
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
 * Integration tests for the {@link SkillsMasterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SkillsMasterResourceIT {

    private static final String DEFAULT_SKILL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SKILL_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/skills-masters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SkillsMasterRepository skillsMasterRepository;

    @Autowired
    private SkillsMasterMapper skillsMasterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSkillsMasterMockMvc;

    private SkillsMaster skillsMaster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SkillsMaster createEntity(EntityManager em) {
        SkillsMaster skillsMaster = new SkillsMaster().skillName(DEFAULT_SKILL_NAME);
        return skillsMaster;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SkillsMaster createUpdatedEntity(EntityManager em) {
        SkillsMaster skillsMaster = new SkillsMaster().skillName(UPDATED_SKILL_NAME);
        return skillsMaster;
    }

    @BeforeEach
    public void initTest() {
        skillsMaster = createEntity(em);
    }

    @Test
    @Transactional
    void createSkillsMaster() throws Exception {
        int databaseSizeBeforeCreate = skillsMasterRepository.findAll().size();
        // Create the SkillsMaster
        SkillsMasterDTO skillsMasterDTO = skillsMasterMapper.toDto(skillsMaster);
        restSkillsMasterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(skillsMasterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SkillsMaster in the database
        List<SkillsMaster> skillsMasterList = skillsMasterRepository.findAll();
        assertThat(skillsMasterList).hasSize(databaseSizeBeforeCreate + 1);
        SkillsMaster testSkillsMaster = skillsMasterList.get(skillsMasterList.size() - 1);
        assertThat(testSkillsMaster.getSkillName()).isEqualTo(DEFAULT_SKILL_NAME);
    }

    @Test
    @Transactional
    void createSkillsMasterWithExistingId() throws Exception {
        // Create the SkillsMaster with an existing ID
        skillsMaster.setId(1L);
        SkillsMasterDTO skillsMasterDTO = skillsMasterMapper.toDto(skillsMaster);

        int databaseSizeBeforeCreate = skillsMasterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkillsMasterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(skillsMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillsMaster in the database
        List<SkillsMaster> skillsMasterList = skillsMasterRepository.findAll();
        assertThat(skillsMasterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSkillsMasters() throws Exception {
        // Initialize the database
        skillsMasterRepository.saveAndFlush(skillsMaster);

        // Get all the skillsMasterList
        restSkillsMasterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skillsMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].skillName").value(hasItem(DEFAULT_SKILL_NAME)));
    }

    @Test
    @Transactional
    void getSkillsMaster() throws Exception {
        // Initialize the database
        skillsMasterRepository.saveAndFlush(skillsMaster);

        // Get the skillsMaster
        restSkillsMasterMockMvc
            .perform(get(ENTITY_API_URL_ID, skillsMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(skillsMaster.getId().intValue()))
            .andExpect(jsonPath("$.skillName").value(DEFAULT_SKILL_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSkillsMaster() throws Exception {
        // Get the skillsMaster
        restSkillsMasterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSkillsMaster() throws Exception {
        // Initialize the database
        skillsMasterRepository.saveAndFlush(skillsMaster);

        int databaseSizeBeforeUpdate = skillsMasterRepository.findAll().size();

        // Update the skillsMaster
        SkillsMaster updatedSkillsMaster = skillsMasterRepository.findById(skillsMaster.getId()).get();
        // Disconnect from session so that the updates on updatedSkillsMaster are not directly saved in db
        em.detach(updatedSkillsMaster);
        updatedSkillsMaster.skillName(UPDATED_SKILL_NAME);
        SkillsMasterDTO skillsMasterDTO = skillsMasterMapper.toDto(updatedSkillsMaster);

        restSkillsMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillsMasterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(skillsMasterDTO))
            )
            .andExpect(status().isOk());

        // Validate the SkillsMaster in the database
        List<SkillsMaster> skillsMasterList = skillsMasterRepository.findAll();
        assertThat(skillsMasterList).hasSize(databaseSizeBeforeUpdate);
        SkillsMaster testSkillsMaster = skillsMasterList.get(skillsMasterList.size() - 1);
        assertThat(testSkillsMaster.getSkillName()).isEqualTo(UPDATED_SKILL_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSkillsMaster() throws Exception {
        int databaseSizeBeforeUpdate = skillsMasterRepository.findAll().size();
        skillsMaster.setId(count.incrementAndGet());

        // Create the SkillsMaster
        SkillsMasterDTO skillsMasterDTO = skillsMasterMapper.toDto(skillsMaster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillsMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillsMasterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(skillsMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillsMaster in the database
        List<SkillsMaster> skillsMasterList = skillsMasterRepository.findAll();
        assertThat(skillsMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSkillsMaster() throws Exception {
        int databaseSizeBeforeUpdate = skillsMasterRepository.findAll().size();
        skillsMaster.setId(count.incrementAndGet());

        // Create the SkillsMaster
        SkillsMasterDTO skillsMasterDTO = skillsMasterMapper.toDto(skillsMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillsMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(skillsMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillsMaster in the database
        List<SkillsMaster> skillsMasterList = skillsMasterRepository.findAll();
        assertThat(skillsMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSkillsMaster() throws Exception {
        int databaseSizeBeforeUpdate = skillsMasterRepository.findAll().size();
        skillsMaster.setId(count.incrementAndGet());

        // Create the SkillsMaster
        SkillsMasterDTO skillsMasterDTO = skillsMasterMapper.toDto(skillsMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillsMasterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(skillsMasterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SkillsMaster in the database
        List<SkillsMaster> skillsMasterList = skillsMasterRepository.findAll();
        assertThat(skillsMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSkillsMasterWithPatch() throws Exception {
        // Initialize the database
        skillsMasterRepository.saveAndFlush(skillsMaster);

        int databaseSizeBeforeUpdate = skillsMasterRepository.findAll().size();

        // Update the skillsMaster using partial update
        SkillsMaster partialUpdatedSkillsMaster = new SkillsMaster();
        partialUpdatedSkillsMaster.setId(skillsMaster.getId());

        restSkillsMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkillsMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSkillsMaster))
            )
            .andExpect(status().isOk());

        // Validate the SkillsMaster in the database
        List<SkillsMaster> skillsMasterList = skillsMasterRepository.findAll();
        assertThat(skillsMasterList).hasSize(databaseSizeBeforeUpdate);
        SkillsMaster testSkillsMaster = skillsMasterList.get(skillsMasterList.size() - 1);
        assertThat(testSkillsMaster.getSkillName()).isEqualTo(DEFAULT_SKILL_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSkillsMasterWithPatch() throws Exception {
        // Initialize the database
        skillsMasterRepository.saveAndFlush(skillsMaster);

        int databaseSizeBeforeUpdate = skillsMasterRepository.findAll().size();

        // Update the skillsMaster using partial update
        SkillsMaster partialUpdatedSkillsMaster = new SkillsMaster();
        partialUpdatedSkillsMaster.setId(skillsMaster.getId());

        partialUpdatedSkillsMaster.skillName(UPDATED_SKILL_NAME);

        restSkillsMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkillsMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSkillsMaster))
            )
            .andExpect(status().isOk());

        // Validate the SkillsMaster in the database
        List<SkillsMaster> skillsMasterList = skillsMasterRepository.findAll();
        assertThat(skillsMasterList).hasSize(databaseSizeBeforeUpdate);
        SkillsMaster testSkillsMaster = skillsMasterList.get(skillsMasterList.size() - 1);
        assertThat(testSkillsMaster.getSkillName()).isEqualTo(UPDATED_SKILL_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSkillsMaster() throws Exception {
        int databaseSizeBeforeUpdate = skillsMasterRepository.findAll().size();
        skillsMaster.setId(count.incrementAndGet());

        // Create the SkillsMaster
        SkillsMasterDTO skillsMasterDTO = skillsMasterMapper.toDto(skillsMaster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillsMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, skillsMasterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(skillsMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillsMaster in the database
        List<SkillsMaster> skillsMasterList = skillsMasterRepository.findAll();
        assertThat(skillsMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSkillsMaster() throws Exception {
        int databaseSizeBeforeUpdate = skillsMasterRepository.findAll().size();
        skillsMaster.setId(count.incrementAndGet());

        // Create the SkillsMaster
        SkillsMasterDTO skillsMasterDTO = skillsMasterMapper.toDto(skillsMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillsMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(skillsMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillsMaster in the database
        List<SkillsMaster> skillsMasterList = skillsMasterRepository.findAll();
        assertThat(skillsMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSkillsMaster() throws Exception {
        int databaseSizeBeforeUpdate = skillsMasterRepository.findAll().size();
        skillsMaster.setId(count.incrementAndGet());

        // Create the SkillsMaster
        SkillsMasterDTO skillsMasterDTO = skillsMasterMapper.toDto(skillsMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillsMasterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(skillsMasterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SkillsMaster in the database
        List<SkillsMaster> skillsMasterList = skillsMasterRepository.findAll();
        assertThat(skillsMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSkillsMaster() throws Exception {
        // Initialize the database
        skillsMasterRepository.saveAndFlush(skillsMaster);

        int databaseSizeBeforeDelete = skillsMasterRepository.findAll().size();

        // Delete the skillsMaster
        restSkillsMasterMockMvc
            .perform(delete(ENTITY_API_URL_ID, skillsMaster.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SkillsMaster> skillsMasterList = skillsMasterRepository.findAll();
        assertThat(skillsMasterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
