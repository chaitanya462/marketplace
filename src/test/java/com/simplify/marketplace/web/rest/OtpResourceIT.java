package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.Otp;
import com.simplify.marketplace.domain.enumeration.OtpStatus;
import com.simplify.marketplace.domain.enumeration.OtpType;
import com.simplify.marketplace.repository.OtpRepository;
import com.simplify.marketplace.service.dto.OtpDTO;
import com.simplify.marketplace.service.mapper.OtpMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link OtpResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OtpResourceIT {

    private static final String DEFAULT_CONTEXT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CONTEXT_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_OTP = 1;
    private static final Integer UPDATED_OTP = 2;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_PHONE = 1;
    private static final Integer UPDATED_PHONE = 2;

    private static final OtpType DEFAULT_TYPE = OtpType.Email;
    private static final OtpType UPDATED_TYPE = OtpType.Phone;

    private static final LocalDate DEFAULT_EXPIRY_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final OtpStatus DEFAULT_STATUS = OtpStatus.Pending;
    private static final OtpStatus UPDATED_STATUS = OtpStatus.Failed;

    private static final String ENTITY_API_URL = "/api/otps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private OtpMapper otpMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOtpMockMvc;

    private Otp otp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Otp createEntity(EntityManager em) {
        Otp otp = new Otp()
            .contextId(DEFAULT_CONTEXT_ID)
            .otp(DEFAULT_OTP)
            .email(DEFAULT_EMAIL)
            .isActive(DEFAULT_IS_ACTIVE)
            .phone(DEFAULT_PHONE)
            .type(DEFAULT_TYPE)
            .expiryTime(DEFAULT_EXPIRY_TIME)
            .status(DEFAULT_STATUS);
        return otp;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Otp createUpdatedEntity(EntityManager em) {
        Otp otp = new Otp()
            .contextId(UPDATED_CONTEXT_ID)
            .otp(UPDATED_OTP)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .type(UPDATED_TYPE)
            .expiryTime(UPDATED_EXPIRY_TIME)
            .status(UPDATED_STATUS);
        return otp;
    }

    @BeforeEach
    public void initTest() {
        otp = createEntity(em);
    }

    @Test
    @Transactional
    void createOtp() throws Exception {
        int databaseSizeBeforeCreate = otpRepository.findAll().size();
        // Create the Otp
        OtpDTO otpDTO = otpMapper.toDto(otp);
        restOtpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(otpDTO)))
            .andExpect(status().isCreated());

        // Validate the Otp in the database
        List<Otp> otpList = otpRepository.findAll();
        assertThat(otpList).hasSize(databaseSizeBeforeCreate + 1);
        Otp testOtp = otpList.get(otpList.size() - 1);
        assertThat(testOtp.getContextId()).isEqualTo(DEFAULT_CONTEXT_ID);
        assertThat(testOtp.getOtp()).isEqualTo(DEFAULT_OTP);
        assertThat(testOtp.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOtp.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testOtp.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testOtp.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testOtp.getExpiryTime()).isEqualTo(DEFAULT_EXPIRY_TIME);
        assertThat(testOtp.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createOtpWithExistingId() throws Exception {
        // Create the Otp with an existing ID
        otp.setId(1L);
        OtpDTO otpDTO = otpMapper.toDto(otp);

        int databaseSizeBeforeCreate = otpRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOtpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(otpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Otp in the database
        List<Otp> otpList = otpRepository.findAll();
        assertThat(otpList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOtps() throws Exception {
        // Initialize the database
        otpRepository.saveAndFlush(otp);

        // Get all the otpList
        restOtpMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(otp.getId().intValue())))
            .andExpect(jsonPath("$.[*].contextId").value(hasItem(DEFAULT_CONTEXT_ID)))
            .andExpect(jsonPath("$.[*].otp").value(hasItem(DEFAULT_OTP)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].expiryTime").value(hasItem(DEFAULT_EXPIRY_TIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getOtp() throws Exception {
        // Initialize the database
        otpRepository.saveAndFlush(otp);

        // Get the otp
        restOtpMockMvc
            .perform(get(ENTITY_API_URL_ID, otp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(otp.getId().intValue()))
            .andExpect(jsonPath("$.contextId").value(DEFAULT_CONTEXT_ID))
            .andExpect(jsonPath("$.otp").value(DEFAULT_OTP))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.expiryTime").value(DEFAULT_EXPIRY_TIME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOtp() throws Exception {
        // Get the otp
        restOtpMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOtp() throws Exception {
        // Initialize the database
        otpRepository.saveAndFlush(otp);

        int databaseSizeBeforeUpdate = otpRepository.findAll().size();

        // Update the otp
        Otp updatedOtp = otpRepository.findById(otp.getId()).get();
        // Disconnect from session so that the updates on updatedOtp are not directly saved in db
        em.detach(updatedOtp);
        updatedOtp
            .contextId(UPDATED_CONTEXT_ID)
            .otp(UPDATED_OTP)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .type(UPDATED_TYPE)
            .expiryTime(UPDATED_EXPIRY_TIME)
            .status(UPDATED_STATUS);
        OtpDTO otpDTO = otpMapper.toDto(updatedOtp);

        restOtpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, otpDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(otpDTO))
            )
            .andExpect(status().isOk());

        // Validate the Otp in the database
        List<Otp> otpList = otpRepository.findAll();
        assertThat(otpList).hasSize(databaseSizeBeforeUpdate);
        Otp testOtp = otpList.get(otpList.size() - 1);
        assertThat(testOtp.getContextId()).isEqualTo(UPDATED_CONTEXT_ID);
        assertThat(testOtp.getOtp()).isEqualTo(UPDATED_OTP);
        assertThat(testOtp.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOtp.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testOtp.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testOtp.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOtp.getExpiryTime()).isEqualTo(UPDATED_EXPIRY_TIME);
        assertThat(testOtp.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingOtp() throws Exception {
        int databaseSizeBeforeUpdate = otpRepository.findAll().size();
        otp.setId(count.incrementAndGet());

        // Create the Otp
        OtpDTO otpDTO = otpMapper.toDto(otp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOtpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, otpDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(otpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Otp in the database
        List<Otp> otpList = otpRepository.findAll();
        assertThat(otpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOtp() throws Exception {
        int databaseSizeBeforeUpdate = otpRepository.findAll().size();
        otp.setId(count.incrementAndGet());

        // Create the Otp
        OtpDTO otpDTO = otpMapper.toDto(otp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(otpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Otp in the database
        List<Otp> otpList = otpRepository.findAll();
        assertThat(otpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOtp() throws Exception {
        int databaseSizeBeforeUpdate = otpRepository.findAll().size();
        otp.setId(count.incrementAndGet());

        // Create the Otp
        OtpDTO otpDTO = otpMapper.toDto(otp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(otpDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Otp in the database
        List<Otp> otpList = otpRepository.findAll();
        assertThat(otpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOtpWithPatch() throws Exception {
        // Initialize the database
        otpRepository.saveAndFlush(otp);

        int databaseSizeBeforeUpdate = otpRepository.findAll().size();

        // Update the otp using partial update
        Otp partialUpdatedOtp = new Otp();
        partialUpdatedOtp.setId(otp.getId());

        partialUpdatedOtp
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .type(UPDATED_TYPE)
            .expiryTime(UPDATED_EXPIRY_TIME)
            .status(UPDATED_STATUS);

        restOtpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOtp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOtp))
            )
            .andExpect(status().isOk());

        // Validate the Otp in the database
        List<Otp> otpList = otpRepository.findAll();
        assertThat(otpList).hasSize(databaseSizeBeforeUpdate);
        Otp testOtp = otpList.get(otpList.size() - 1);
        assertThat(testOtp.getContextId()).isEqualTo(DEFAULT_CONTEXT_ID);
        assertThat(testOtp.getOtp()).isEqualTo(DEFAULT_OTP);
        assertThat(testOtp.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOtp.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testOtp.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testOtp.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOtp.getExpiryTime()).isEqualTo(UPDATED_EXPIRY_TIME);
        assertThat(testOtp.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateOtpWithPatch() throws Exception {
        // Initialize the database
        otpRepository.saveAndFlush(otp);

        int databaseSizeBeforeUpdate = otpRepository.findAll().size();

        // Update the otp using partial update
        Otp partialUpdatedOtp = new Otp();
        partialUpdatedOtp.setId(otp.getId());

        partialUpdatedOtp
            .contextId(UPDATED_CONTEXT_ID)
            .otp(UPDATED_OTP)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .type(UPDATED_TYPE)
            .expiryTime(UPDATED_EXPIRY_TIME)
            .status(UPDATED_STATUS);

        restOtpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOtp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOtp))
            )
            .andExpect(status().isOk());

        // Validate the Otp in the database
        List<Otp> otpList = otpRepository.findAll();
        assertThat(otpList).hasSize(databaseSizeBeforeUpdate);
        Otp testOtp = otpList.get(otpList.size() - 1);
        assertThat(testOtp.getContextId()).isEqualTo(UPDATED_CONTEXT_ID);
        assertThat(testOtp.getOtp()).isEqualTo(UPDATED_OTP);
        assertThat(testOtp.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOtp.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testOtp.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testOtp.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOtp.getExpiryTime()).isEqualTo(UPDATED_EXPIRY_TIME);
        assertThat(testOtp.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingOtp() throws Exception {
        int databaseSizeBeforeUpdate = otpRepository.findAll().size();
        otp.setId(count.incrementAndGet());

        // Create the Otp
        OtpDTO otpDTO = otpMapper.toDto(otp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOtpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, otpDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(otpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Otp in the database
        List<Otp> otpList = otpRepository.findAll();
        assertThat(otpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOtp() throws Exception {
        int databaseSizeBeforeUpdate = otpRepository.findAll().size();
        otp.setId(count.incrementAndGet());

        // Create the Otp
        OtpDTO otpDTO = otpMapper.toDto(otp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(otpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Otp in the database
        List<Otp> otpList = otpRepository.findAll();
        assertThat(otpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOtp() throws Exception {
        int databaseSizeBeforeUpdate = otpRepository.findAll().size();
        otp.setId(count.incrementAndGet());

        // Create the Otp
        OtpDTO otpDTO = otpMapper.toDto(otp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(otpDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Otp in the database
        List<Otp> otpList = otpRepository.findAll();
        assertThat(otpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOtp() throws Exception {
        // Initialize the database
        otpRepository.saveAndFlush(otp);

        int databaseSizeBeforeDelete = otpRepository.findAll().size();

        // Delete the otp
        restOtpMockMvc.perform(delete(ENTITY_API_URL_ID, otp.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Otp> otpList = otpRepository.findAll();
        assertThat(otpList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
