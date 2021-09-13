package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.UserEmail;
import com.simplify.marketplace.repository.UserEmailRepository;
import com.simplify.marketplace.service.dto.UserEmailDTO;
import com.simplify.marketplace.service.mapper.UserEmailMapper;
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
 * Integration tests for the {@link UserEmailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserEmailResourceIT {

    private static final String DEFAULT_EMAIL = "|us/@aJ.{q3i*G";
    private static final String UPDATED_EMAIL = "[do<UO@M&MX.}&o";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_PRIMARY = false;
    private static final Boolean UPDATED_IS_PRIMARY = true;

    private static final String DEFAULT_TAG = "AAAAAAAAAA";
    private static final String UPDATED_TAG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-emails";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserEmailRepository userEmailRepository;

    @Autowired
    private UserEmailMapper userEmailMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserEmailMockMvc;

    private UserEmail userEmail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserEmail createEntity(EntityManager em) {
        UserEmail userEmail = new UserEmail()
            .email(DEFAULT_EMAIL)
            .isActive(DEFAULT_IS_ACTIVE)
            .isPrimary(DEFAULT_IS_PRIMARY)
            .tag(DEFAULT_TAG);
        return userEmail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserEmail createUpdatedEntity(EntityManager em) {
        UserEmail userEmail = new UserEmail()
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .isPrimary(UPDATED_IS_PRIMARY)
            .tag(UPDATED_TAG);
        return userEmail;
    }

    @BeforeEach
    public void initTest() {
        userEmail = createEntity(em);
    }

    @Test
    @Transactional
    void createUserEmail() throws Exception {
        int databaseSizeBeforeCreate = userEmailRepository.findAll().size();
        // Create the UserEmail
        UserEmailDTO userEmailDTO = userEmailMapper.toDto(userEmail);
        restUserEmailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userEmailDTO)))
            .andExpect(status().isCreated());

        // Validate the UserEmail in the database
        List<UserEmail> userEmailList = userEmailRepository.findAll();
        assertThat(userEmailList).hasSize(databaseSizeBeforeCreate + 1);
        UserEmail testUserEmail = userEmailList.get(userEmailList.size() - 1);
        assertThat(testUserEmail.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserEmail.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testUserEmail.getIsPrimary()).isEqualTo(DEFAULT_IS_PRIMARY);
        assertThat(testUserEmail.getTag()).isEqualTo(DEFAULT_TAG);
    }

    @Test
    @Transactional
    void createUserEmailWithExistingId() throws Exception {
        // Create the UserEmail with an existing ID
        userEmail.setId(1L);
        UserEmailDTO userEmailDTO = userEmailMapper.toDto(userEmail);

        int databaseSizeBeforeCreate = userEmailRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserEmailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userEmailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserEmail in the database
        List<UserEmail> userEmailList = userEmailRepository.findAll();
        assertThat(userEmailList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEmailRepository.findAll().size();
        // set the field null
        userEmail.setEmail(null);

        // Create the UserEmail, which fails.
        UserEmailDTO userEmailDTO = userEmailMapper.toDto(userEmail);

        restUserEmailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userEmailDTO)))
            .andExpect(status().isBadRequest());

        List<UserEmail> userEmailList = userEmailRepository.findAll();
        assertThat(userEmailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserEmails() throws Exception {
        // Initialize the database
        userEmailRepository.saveAndFlush(userEmail);

        // Get all the userEmailList
        restUserEmailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userEmail.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isPrimary").value(hasItem(DEFAULT_IS_PRIMARY.booleanValue())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG)));
    }

    @Test
    @Transactional
    void getUserEmail() throws Exception {
        // Initialize the database
        userEmailRepository.saveAndFlush(userEmail);

        // Get the userEmail
        restUserEmailMockMvc
            .perform(get(ENTITY_API_URL_ID, userEmail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userEmail.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isPrimary").value(DEFAULT_IS_PRIMARY.booleanValue()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG));
    }

    @Test
    @Transactional
    void getNonExistingUserEmail() throws Exception {
        // Get the userEmail
        restUserEmailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserEmail() throws Exception {
        // Initialize the database
        userEmailRepository.saveAndFlush(userEmail);

        int databaseSizeBeforeUpdate = userEmailRepository.findAll().size();

        // Update the userEmail
        UserEmail updatedUserEmail = userEmailRepository.findById(userEmail.getId()).get();
        // Disconnect from session so that the updates on updatedUserEmail are not directly saved in db
        em.detach(updatedUserEmail);
        updatedUserEmail.email(UPDATED_EMAIL).isActive(UPDATED_IS_ACTIVE).isPrimary(UPDATED_IS_PRIMARY).tag(UPDATED_TAG);
        UserEmailDTO userEmailDTO = userEmailMapper.toDto(updatedUserEmail);

        restUserEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userEmailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userEmailDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserEmail in the database
        List<UserEmail> userEmailList = userEmailRepository.findAll();
        assertThat(userEmailList).hasSize(databaseSizeBeforeUpdate);
        UserEmail testUserEmail = userEmailList.get(userEmailList.size() - 1);
        assertThat(testUserEmail.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserEmail.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testUserEmail.getIsPrimary()).isEqualTo(UPDATED_IS_PRIMARY);
        assertThat(testUserEmail.getTag()).isEqualTo(UPDATED_TAG);
    }

    @Test
    @Transactional
    void putNonExistingUserEmail() throws Exception {
        int databaseSizeBeforeUpdate = userEmailRepository.findAll().size();
        userEmail.setId(count.incrementAndGet());

        // Create the UserEmail
        UserEmailDTO userEmailDTO = userEmailMapper.toDto(userEmail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userEmailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userEmailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEmail in the database
        List<UserEmail> userEmailList = userEmailRepository.findAll();
        assertThat(userEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserEmail() throws Exception {
        int databaseSizeBeforeUpdate = userEmailRepository.findAll().size();
        userEmail.setId(count.incrementAndGet());

        // Create the UserEmail
        UserEmailDTO userEmailDTO = userEmailMapper.toDto(userEmail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userEmailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEmail in the database
        List<UserEmail> userEmailList = userEmailRepository.findAll();
        assertThat(userEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserEmail() throws Exception {
        int databaseSizeBeforeUpdate = userEmailRepository.findAll().size();
        userEmail.setId(count.incrementAndGet());

        // Create the UserEmail
        UserEmailDTO userEmailDTO = userEmailMapper.toDto(userEmail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEmailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userEmailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserEmail in the database
        List<UserEmail> userEmailList = userEmailRepository.findAll();
        assertThat(userEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserEmailWithPatch() throws Exception {
        // Initialize the database
        userEmailRepository.saveAndFlush(userEmail);

        int databaseSizeBeforeUpdate = userEmailRepository.findAll().size();

        // Update the userEmail using partial update
        UserEmail partialUpdatedUserEmail = new UserEmail();
        partialUpdatedUserEmail.setId(userEmail.getId());

        partialUpdatedUserEmail.email(UPDATED_EMAIL).isPrimary(UPDATED_IS_PRIMARY);

        restUserEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserEmail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserEmail))
            )
            .andExpect(status().isOk());

        // Validate the UserEmail in the database
        List<UserEmail> userEmailList = userEmailRepository.findAll();
        assertThat(userEmailList).hasSize(databaseSizeBeforeUpdate);
        UserEmail testUserEmail = userEmailList.get(userEmailList.size() - 1);
        assertThat(testUserEmail.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserEmail.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testUserEmail.getIsPrimary()).isEqualTo(UPDATED_IS_PRIMARY);
        assertThat(testUserEmail.getTag()).isEqualTo(DEFAULT_TAG);
    }

    @Test
    @Transactional
    void fullUpdateUserEmailWithPatch() throws Exception {
        // Initialize the database
        userEmailRepository.saveAndFlush(userEmail);

        int databaseSizeBeforeUpdate = userEmailRepository.findAll().size();

        // Update the userEmail using partial update
        UserEmail partialUpdatedUserEmail = new UserEmail();
        partialUpdatedUserEmail.setId(userEmail.getId());

        partialUpdatedUserEmail.email(UPDATED_EMAIL).isActive(UPDATED_IS_ACTIVE).isPrimary(UPDATED_IS_PRIMARY).tag(UPDATED_TAG);

        restUserEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserEmail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserEmail))
            )
            .andExpect(status().isOk());

        // Validate the UserEmail in the database
        List<UserEmail> userEmailList = userEmailRepository.findAll();
        assertThat(userEmailList).hasSize(databaseSizeBeforeUpdate);
        UserEmail testUserEmail = userEmailList.get(userEmailList.size() - 1);
        assertThat(testUserEmail.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserEmail.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testUserEmail.getIsPrimary()).isEqualTo(UPDATED_IS_PRIMARY);
        assertThat(testUserEmail.getTag()).isEqualTo(UPDATED_TAG);
    }

    @Test
    @Transactional
    void patchNonExistingUserEmail() throws Exception {
        int databaseSizeBeforeUpdate = userEmailRepository.findAll().size();
        userEmail.setId(count.incrementAndGet());

        // Create the UserEmail
        UserEmailDTO userEmailDTO = userEmailMapper.toDto(userEmail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userEmailDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userEmailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEmail in the database
        List<UserEmail> userEmailList = userEmailRepository.findAll();
        assertThat(userEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserEmail() throws Exception {
        int databaseSizeBeforeUpdate = userEmailRepository.findAll().size();
        userEmail.setId(count.incrementAndGet());

        // Create the UserEmail
        UserEmailDTO userEmailDTO = userEmailMapper.toDto(userEmail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userEmailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEmail in the database
        List<UserEmail> userEmailList = userEmailRepository.findAll();
        assertThat(userEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserEmail() throws Exception {
        int databaseSizeBeforeUpdate = userEmailRepository.findAll().size();
        userEmail.setId(count.incrementAndGet());

        // Create the UserEmail
        UserEmailDTO userEmailDTO = userEmailMapper.toDto(userEmail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEmailMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userEmailDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserEmail in the database
        List<UserEmail> userEmailList = userEmailRepository.findAll();
        assertThat(userEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserEmail() throws Exception {
        // Initialize the database
        userEmailRepository.saveAndFlush(userEmail);

        int databaseSizeBeforeDelete = userEmailRepository.findAll().size();

        // Delete the userEmail
        restUserEmailMockMvc
            .perform(delete(ENTITY_API_URL_ID, userEmail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserEmail> userEmailList = userEmailRepository.findAll();
        assertThat(userEmailList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
