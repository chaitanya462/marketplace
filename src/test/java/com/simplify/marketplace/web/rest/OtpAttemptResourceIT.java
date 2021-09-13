package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.OtpAttempt;
import com.simplify.marketplace.domain.enumeration.OtpStatus;
import com.simplify.marketplace.repository.OtpAttemptRepository;
import com.simplify.marketplace.service.dto.OtpAttemptDTO;
import com.simplify.marketplace.service.mapper.OtpAttemptMapper;
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
 * Integration tests for the {@link OtpAttemptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OtpAttemptResourceIT {

    private static final String DEFAULT_CONTEXT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CONTEXT_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_OTP = 1;
    private static final Integer UPDATED_OTP = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final OtpStatus DEFAULT_STATUS = OtpStatus.Pending;
    private static final OtpStatus UPDATED_STATUS = OtpStatus.Failed;

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    private static final String DEFAULT_COOOKIE = "AAAAAAAAAA";
    private static final String UPDATED_COOOKIE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/otp-attempts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OtpAttemptRepository otpAttemptRepository;

    @Autowired
    private OtpAttemptMapper otpAttemptMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOtpAttemptMockMvc;

    private OtpAttempt otpAttempt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OtpAttempt createEntity(EntityManager em) {
        OtpAttempt otpAttempt = new OtpAttempt()
            .contextId(DEFAULT_CONTEXT_ID)
            .otp(DEFAULT_OTP)
            .isActive(DEFAULT_IS_ACTIVE)
            .status(DEFAULT_STATUS)
            .ip(DEFAULT_IP)
            .coookie(DEFAULT_COOOKIE);
        return otpAttempt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OtpAttempt createUpdatedEntity(EntityManager em) {
        OtpAttempt otpAttempt = new OtpAttempt()
            .contextId(UPDATED_CONTEXT_ID)
            .otp(UPDATED_OTP)
            .isActive(UPDATED_IS_ACTIVE)
            .status(UPDATED_STATUS)
            .ip(UPDATED_IP)
            .coookie(UPDATED_COOOKIE);
        return otpAttempt;
    }

    @BeforeEach
    public void initTest() {
        otpAttempt = createEntity(em);
    }

    @Test
    @Transactional
    void createOtpAttempt() throws Exception {
        int databaseSizeBeforeCreate = otpAttemptRepository.findAll().size();
        // Create the OtpAttempt
        OtpAttemptDTO otpAttemptDTO = otpAttemptMapper.toDto(otpAttempt);
        restOtpAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(otpAttemptDTO)))
            .andExpect(status().isCreated());

        // Validate the OtpAttempt in the database
        List<OtpAttempt> otpAttemptList = otpAttemptRepository.findAll();
        assertThat(otpAttemptList).hasSize(databaseSizeBeforeCreate + 1);
        OtpAttempt testOtpAttempt = otpAttemptList.get(otpAttemptList.size() - 1);
        assertThat(testOtpAttempt.getContextId()).isEqualTo(DEFAULT_CONTEXT_ID);
        assertThat(testOtpAttempt.getOtp()).isEqualTo(DEFAULT_OTP);
        assertThat(testOtpAttempt.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testOtpAttempt.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOtpAttempt.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testOtpAttempt.getCoookie()).isEqualTo(DEFAULT_COOOKIE);
    }

    @Test
    @Transactional
    void createOtpAttemptWithExistingId() throws Exception {
        // Create the OtpAttempt with an existing ID
        otpAttempt.setId(1L);
        OtpAttemptDTO otpAttemptDTO = otpAttemptMapper.toDto(otpAttempt);

        int databaseSizeBeforeCreate = otpAttemptRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOtpAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(otpAttemptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OtpAttempt in the database
        List<OtpAttempt> otpAttemptList = otpAttemptRepository.findAll();
        assertThat(otpAttemptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOtpAttempts() throws Exception {
        // Initialize the database
        otpAttemptRepository.saveAndFlush(otpAttempt);

        // Get all the otpAttemptList
        restOtpAttemptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(otpAttempt.getId().intValue())))
            .andExpect(jsonPath("$.[*].contextId").value(hasItem(DEFAULT_CONTEXT_ID)))
            .andExpect(jsonPath("$.[*].otp").value(hasItem(DEFAULT_OTP)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].coookie").value(hasItem(DEFAULT_COOOKIE)));
    }

    @Test
    @Transactional
    void getOtpAttempt() throws Exception {
        // Initialize the database
        otpAttemptRepository.saveAndFlush(otpAttempt);

        // Get the otpAttempt
        restOtpAttemptMockMvc
            .perform(get(ENTITY_API_URL_ID, otpAttempt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(otpAttempt.getId().intValue()))
            .andExpect(jsonPath("$.contextId").value(DEFAULT_CONTEXT_ID))
            .andExpect(jsonPath("$.otp").value(DEFAULT_OTP))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP))
            .andExpect(jsonPath("$.coookie").value(DEFAULT_COOOKIE));
    }

    @Test
    @Transactional
    void getNonExistingOtpAttempt() throws Exception {
        // Get the otpAttempt
        restOtpAttemptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOtpAttempt() throws Exception {
        // Initialize the database
        otpAttemptRepository.saveAndFlush(otpAttempt);

        int databaseSizeBeforeUpdate = otpAttemptRepository.findAll().size();

        // Update the otpAttempt
        OtpAttempt updatedOtpAttempt = otpAttemptRepository.findById(otpAttempt.getId()).get();
        // Disconnect from session so that the updates on updatedOtpAttempt are not directly saved in db
        em.detach(updatedOtpAttempt);
        updatedOtpAttempt
            .contextId(UPDATED_CONTEXT_ID)
            .otp(UPDATED_OTP)
            .isActive(UPDATED_IS_ACTIVE)
            .status(UPDATED_STATUS)
            .ip(UPDATED_IP)
            .coookie(UPDATED_COOOKIE);
        OtpAttemptDTO otpAttemptDTO = otpAttemptMapper.toDto(updatedOtpAttempt);

        restOtpAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, otpAttemptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(otpAttemptDTO))
            )
            .andExpect(status().isOk());

        // Validate the OtpAttempt in the database
        List<OtpAttempt> otpAttemptList = otpAttemptRepository.findAll();
        assertThat(otpAttemptList).hasSize(databaseSizeBeforeUpdate);
        OtpAttempt testOtpAttempt = otpAttemptList.get(otpAttemptList.size() - 1);
        assertThat(testOtpAttempt.getContextId()).isEqualTo(UPDATED_CONTEXT_ID);
        assertThat(testOtpAttempt.getOtp()).isEqualTo(UPDATED_OTP);
        assertThat(testOtpAttempt.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testOtpAttempt.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOtpAttempt.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testOtpAttempt.getCoookie()).isEqualTo(UPDATED_COOOKIE);
    }

    @Test
    @Transactional
    void putNonExistingOtpAttempt() throws Exception {
        int databaseSizeBeforeUpdate = otpAttemptRepository.findAll().size();
        otpAttempt.setId(count.incrementAndGet());

        // Create the OtpAttempt
        OtpAttemptDTO otpAttemptDTO = otpAttemptMapper.toDto(otpAttempt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOtpAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, otpAttemptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(otpAttemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtpAttempt in the database
        List<OtpAttempt> otpAttemptList = otpAttemptRepository.findAll();
        assertThat(otpAttemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOtpAttempt() throws Exception {
        int databaseSizeBeforeUpdate = otpAttemptRepository.findAll().size();
        otpAttempt.setId(count.incrementAndGet());

        // Create the OtpAttempt
        OtpAttemptDTO otpAttemptDTO = otpAttemptMapper.toDto(otpAttempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(otpAttemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtpAttempt in the database
        List<OtpAttempt> otpAttemptList = otpAttemptRepository.findAll();
        assertThat(otpAttemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOtpAttempt() throws Exception {
        int databaseSizeBeforeUpdate = otpAttemptRepository.findAll().size();
        otpAttempt.setId(count.incrementAndGet());

        // Create the OtpAttempt
        OtpAttemptDTO otpAttemptDTO = otpAttemptMapper.toDto(otpAttempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpAttemptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(otpAttemptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OtpAttempt in the database
        List<OtpAttempt> otpAttemptList = otpAttemptRepository.findAll();
        assertThat(otpAttemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOtpAttemptWithPatch() throws Exception {
        // Initialize the database
        otpAttemptRepository.saveAndFlush(otpAttempt);

        int databaseSizeBeforeUpdate = otpAttemptRepository.findAll().size();

        // Update the otpAttempt using partial update
        OtpAttempt partialUpdatedOtpAttempt = new OtpAttempt();
        partialUpdatedOtpAttempt.setId(otpAttempt.getId());

        partialUpdatedOtpAttempt.otp(UPDATED_OTP).coookie(UPDATED_COOOKIE);

        restOtpAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOtpAttempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOtpAttempt))
            )
            .andExpect(status().isOk());

        // Validate the OtpAttempt in the database
        List<OtpAttempt> otpAttemptList = otpAttemptRepository.findAll();
        assertThat(otpAttemptList).hasSize(databaseSizeBeforeUpdate);
        OtpAttempt testOtpAttempt = otpAttemptList.get(otpAttemptList.size() - 1);
        assertThat(testOtpAttempt.getContextId()).isEqualTo(DEFAULT_CONTEXT_ID);
        assertThat(testOtpAttempt.getOtp()).isEqualTo(UPDATED_OTP);
        assertThat(testOtpAttempt.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testOtpAttempt.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOtpAttempt.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testOtpAttempt.getCoookie()).isEqualTo(UPDATED_COOOKIE);
    }

    @Test
    @Transactional
    void fullUpdateOtpAttemptWithPatch() throws Exception {
        // Initialize the database
        otpAttemptRepository.saveAndFlush(otpAttempt);

        int databaseSizeBeforeUpdate = otpAttemptRepository.findAll().size();

        // Update the otpAttempt using partial update
        OtpAttempt partialUpdatedOtpAttempt = new OtpAttempt();
        partialUpdatedOtpAttempt.setId(otpAttempt.getId());

        partialUpdatedOtpAttempt
            .contextId(UPDATED_CONTEXT_ID)
            .otp(UPDATED_OTP)
            .isActive(UPDATED_IS_ACTIVE)
            .status(UPDATED_STATUS)
            .ip(UPDATED_IP)
            .coookie(UPDATED_COOOKIE);

        restOtpAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOtpAttempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOtpAttempt))
            )
            .andExpect(status().isOk());

        // Validate the OtpAttempt in the database
        List<OtpAttempt> otpAttemptList = otpAttemptRepository.findAll();
        assertThat(otpAttemptList).hasSize(databaseSizeBeforeUpdate);
        OtpAttempt testOtpAttempt = otpAttemptList.get(otpAttemptList.size() - 1);
        assertThat(testOtpAttempt.getContextId()).isEqualTo(UPDATED_CONTEXT_ID);
        assertThat(testOtpAttempt.getOtp()).isEqualTo(UPDATED_OTP);
        assertThat(testOtpAttempt.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testOtpAttempt.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOtpAttempt.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testOtpAttempt.getCoookie()).isEqualTo(UPDATED_COOOKIE);
    }

    @Test
    @Transactional
    void patchNonExistingOtpAttempt() throws Exception {
        int databaseSizeBeforeUpdate = otpAttemptRepository.findAll().size();
        otpAttempt.setId(count.incrementAndGet());

        // Create the OtpAttempt
        OtpAttemptDTO otpAttemptDTO = otpAttemptMapper.toDto(otpAttempt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOtpAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, otpAttemptDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(otpAttemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtpAttempt in the database
        List<OtpAttempt> otpAttemptList = otpAttemptRepository.findAll();
        assertThat(otpAttemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOtpAttempt() throws Exception {
        int databaseSizeBeforeUpdate = otpAttemptRepository.findAll().size();
        otpAttempt.setId(count.incrementAndGet());

        // Create the OtpAttempt
        OtpAttemptDTO otpAttemptDTO = otpAttemptMapper.toDto(otpAttempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(otpAttemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtpAttempt in the database
        List<OtpAttempt> otpAttemptList = otpAttemptRepository.findAll();
        assertThat(otpAttemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOtpAttempt() throws Exception {
        int databaseSizeBeforeUpdate = otpAttemptRepository.findAll().size();
        otpAttempt.setId(count.incrementAndGet());

        // Create the OtpAttempt
        OtpAttemptDTO otpAttemptDTO = otpAttemptMapper.toDto(otpAttempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(otpAttemptDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OtpAttempt in the database
        List<OtpAttempt> otpAttemptList = otpAttemptRepository.findAll();
        assertThat(otpAttemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOtpAttempt() throws Exception {
        // Initialize the database
        otpAttemptRepository.saveAndFlush(otpAttempt);

        int databaseSizeBeforeDelete = otpAttemptRepository.findAll().size();

        // Delete the otpAttempt
        restOtpAttemptMockMvc
            .perform(delete(ENTITY_API_URL_ID, otpAttempt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OtpAttempt> otpAttemptList = otpAttemptRepository.findAll();
        assertThat(otpAttemptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
