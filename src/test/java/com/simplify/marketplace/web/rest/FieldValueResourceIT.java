package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.FieldValue;
import com.simplify.marketplace.repository.FieldValueRepository;
import com.simplify.marketplace.service.dto.FieldValueDTO;
import com.simplify.marketplace.service.mapper.FieldValueMapper;
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
 * Integration tests for the {@link FieldValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FieldValueResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/field-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FieldValueRepository fieldValueRepository;

    @Autowired
    private FieldValueMapper fieldValueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFieldValueMockMvc;

    private FieldValue fieldValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldValue createEntity(EntityManager em) {
        FieldValue fieldValue = new FieldValue().value(DEFAULT_VALUE);
        return fieldValue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldValue createUpdatedEntity(EntityManager em) {
        FieldValue fieldValue = new FieldValue().value(UPDATED_VALUE);
        return fieldValue;
    }

    @BeforeEach
    public void initTest() {
        fieldValue = createEntity(em);
    }

    @Test
    @Transactional
    void createFieldValue() throws Exception {
        int databaseSizeBeforeCreate = fieldValueRepository.findAll().size();
        // Create the FieldValue
        FieldValueDTO fieldValueDTO = fieldValueMapper.toDto(fieldValue);
        restFieldValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fieldValueDTO)))
            .andExpect(status().isCreated());

        // Validate the FieldValue in the database
        List<FieldValue> fieldValueList = fieldValueRepository.findAll();
        assertThat(fieldValueList).hasSize(databaseSizeBeforeCreate + 1);
        FieldValue testFieldValue = fieldValueList.get(fieldValueList.size() - 1);
        assertThat(testFieldValue.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createFieldValueWithExistingId() throws Exception {
        // Create the FieldValue with an existing ID
        fieldValue.setId(1L);
        FieldValueDTO fieldValueDTO = fieldValueMapper.toDto(fieldValue);

        int databaseSizeBeforeCreate = fieldValueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFieldValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fieldValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FieldValue in the database
        List<FieldValue> fieldValueList = fieldValueRepository.findAll();
        assertThat(fieldValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFieldValues() throws Exception {
        // Initialize the database
        fieldValueRepository.saveAndFlush(fieldValue);

        // Get all the fieldValueList
        restFieldValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fieldValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getFieldValue() throws Exception {
        // Initialize the database
        fieldValueRepository.saveAndFlush(fieldValue);

        // Get the fieldValue
        restFieldValueMockMvc
            .perform(get(ENTITY_API_URL_ID, fieldValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fieldValue.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingFieldValue() throws Exception {
        // Get the fieldValue
        restFieldValueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFieldValue() throws Exception {
        // Initialize the database
        fieldValueRepository.saveAndFlush(fieldValue);

        int databaseSizeBeforeUpdate = fieldValueRepository.findAll().size();

        // Update the fieldValue
        FieldValue updatedFieldValue = fieldValueRepository.findById(fieldValue.getId()).get();
        // Disconnect from session so that the updates on updatedFieldValue are not directly saved in db
        em.detach(updatedFieldValue);
        updatedFieldValue.value(UPDATED_VALUE);
        FieldValueDTO fieldValueDTO = fieldValueMapper.toDto(updatedFieldValue);

        restFieldValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fieldValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fieldValueDTO))
            )
            .andExpect(status().isOk());

        // Validate the FieldValue in the database
        List<FieldValue> fieldValueList = fieldValueRepository.findAll();
        assertThat(fieldValueList).hasSize(databaseSizeBeforeUpdate);
        FieldValue testFieldValue = fieldValueList.get(fieldValueList.size() - 1);
        assertThat(testFieldValue.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingFieldValue() throws Exception {
        int databaseSizeBeforeUpdate = fieldValueRepository.findAll().size();
        fieldValue.setId(count.incrementAndGet());

        // Create the FieldValue
        FieldValueDTO fieldValueDTO = fieldValueMapper.toDto(fieldValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fieldValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fieldValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FieldValue in the database
        List<FieldValue> fieldValueList = fieldValueRepository.findAll();
        assertThat(fieldValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFieldValue() throws Exception {
        int databaseSizeBeforeUpdate = fieldValueRepository.findAll().size();
        fieldValue.setId(count.incrementAndGet());

        // Create the FieldValue
        FieldValueDTO fieldValueDTO = fieldValueMapper.toDto(fieldValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fieldValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FieldValue in the database
        List<FieldValue> fieldValueList = fieldValueRepository.findAll();
        assertThat(fieldValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFieldValue() throws Exception {
        int databaseSizeBeforeUpdate = fieldValueRepository.findAll().size();
        fieldValue.setId(count.incrementAndGet());

        // Create the FieldValue
        FieldValueDTO fieldValueDTO = fieldValueMapper.toDto(fieldValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldValueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fieldValueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FieldValue in the database
        List<FieldValue> fieldValueList = fieldValueRepository.findAll();
        assertThat(fieldValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFieldValueWithPatch() throws Exception {
        // Initialize the database
        fieldValueRepository.saveAndFlush(fieldValue);

        int databaseSizeBeforeUpdate = fieldValueRepository.findAll().size();

        // Update the fieldValue using partial update
        FieldValue partialUpdatedFieldValue = new FieldValue();
        partialUpdatedFieldValue.setId(fieldValue.getId());

        restFieldValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFieldValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFieldValue))
            )
            .andExpect(status().isOk());

        // Validate the FieldValue in the database
        List<FieldValue> fieldValueList = fieldValueRepository.findAll();
        assertThat(fieldValueList).hasSize(databaseSizeBeforeUpdate);
        FieldValue testFieldValue = fieldValueList.get(fieldValueList.size() - 1);
        assertThat(testFieldValue.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateFieldValueWithPatch() throws Exception {
        // Initialize the database
        fieldValueRepository.saveAndFlush(fieldValue);

        int databaseSizeBeforeUpdate = fieldValueRepository.findAll().size();

        // Update the fieldValue using partial update
        FieldValue partialUpdatedFieldValue = new FieldValue();
        partialUpdatedFieldValue.setId(fieldValue.getId());

        partialUpdatedFieldValue.value(UPDATED_VALUE);

        restFieldValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFieldValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFieldValue))
            )
            .andExpect(status().isOk());

        // Validate the FieldValue in the database
        List<FieldValue> fieldValueList = fieldValueRepository.findAll();
        assertThat(fieldValueList).hasSize(databaseSizeBeforeUpdate);
        FieldValue testFieldValue = fieldValueList.get(fieldValueList.size() - 1);
        assertThat(testFieldValue.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingFieldValue() throws Exception {
        int databaseSizeBeforeUpdate = fieldValueRepository.findAll().size();
        fieldValue.setId(count.incrementAndGet());

        // Create the FieldValue
        FieldValueDTO fieldValueDTO = fieldValueMapper.toDto(fieldValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fieldValueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fieldValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FieldValue in the database
        List<FieldValue> fieldValueList = fieldValueRepository.findAll();
        assertThat(fieldValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFieldValue() throws Exception {
        int databaseSizeBeforeUpdate = fieldValueRepository.findAll().size();
        fieldValue.setId(count.incrementAndGet());

        // Create the FieldValue
        FieldValueDTO fieldValueDTO = fieldValueMapper.toDto(fieldValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fieldValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FieldValue in the database
        List<FieldValue> fieldValueList = fieldValueRepository.findAll();
        assertThat(fieldValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFieldValue() throws Exception {
        int databaseSizeBeforeUpdate = fieldValueRepository.findAll().size();
        fieldValue.setId(count.incrementAndGet());

        // Create the FieldValue
        FieldValueDTO fieldValueDTO = fieldValueMapper.toDto(fieldValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldValueMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fieldValueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FieldValue in the database
        List<FieldValue> fieldValueList = fieldValueRepository.findAll();
        assertThat(fieldValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFieldValue() throws Exception {
        // Initialize the database
        fieldValueRepository.saveAndFlush(fieldValue);

        int databaseSizeBeforeDelete = fieldValueRepository.findAll().size();

        // Delete the fieldValue
        restFieldValueMockMvc
            .perform(delete(ENTITY_API_URL_ID, fieldValue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FieldValue> fieldValueList = fieldValueRepository.findAll();
        assertThat(fieldValueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
