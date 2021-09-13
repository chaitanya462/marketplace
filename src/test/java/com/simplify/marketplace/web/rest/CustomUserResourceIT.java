package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.CustomUser;
import com.simplify.marketplace.repository.CustomUserRepository;
import com.simplify.marketplace.service.dto.CustomUserDTO;
import com.simplify.marketplace.service.mapper.CustomUserMapper;
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
 * Integration tests for the {@link CustomUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomUserResourceIT {

    private static final String ENTITY_API_URL = "/api/custom-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private CustomUserMapper customUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomUserMockMvc;

    private CustomUser customUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomUser createEntity(EntityManager em) {
        CustomUser customUser = new CustomUser();
        return customUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomUser createUpdatedEntity(EntityManager em) {
        CustomUser customUser = new CustomUser();
        return customUser;
    }

    @BeforeEach
    public void initTest() {
        customUser = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomUser() throws Exception {
        int databaseSizeBeforeCreate = customUserRepository.findAll().size();
        // Create the CustomUser
        CustomUserDTO customUserDTO = customUserMapper.toDto(customUser);
        restCustomUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customUserDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeCreate + 1);
        CustomUser testCustomUser = customUserList.get(customUserList.size() - 1);
    }

    @Test
    @Transactional
    void createCustomUserWithExistingId() throws Exception {
        // Create the CustomUser with an existing ID
        customUser.setId(1L);
        CustomUserDTO customUserDTO = customUserMapper.toDto(customUser);

        int databaseSizeBeforeCreate = customUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustomUsers() throws Exception {
        // Initialize the database
        customUserRepository.saveAndFlush(customUser);

        // Get all the customUserList
        restCustomUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customUser.getId().intValue())));
    }

    @Test
    @Transactional
    void getCustomUser() throws Exception {
        // Initialize the database
        customUserRepository.saveAndFlush(customUser);

        // Get the customUser
        restCustomUserMockMvc
            .perform(get(ENTITY_API_URL_ID, customUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customUser.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingCustomUser() throws Exception {
        // Get the customUser
        restCustomUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCustomUser() throws Exception {
        // Initialize the database
        customUserRepository.saveAndFlush(customUser);

        int databaseSizeBeforeUpdate = customUserRepository.findAll().size();

        // Update the customUser
        CustomUser updatedCustomUser = customUserRepository.findById(customUser.getId()).get();
        // Disconnect from session so that the updates on updatedCustomUser are not directly saved in db
        em.detach(updatedCustomUser);
        CustomUserDTO customUserDTO = customUserMapper.toDto(updatedCustomUser);

        restCustomUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeUpdate);
        CustomUser testCustomUser = customUserList.get(customUserList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingCustomUser() throws Exception {
        int databaseSizeBeforeUpdate = customUserRepository.findAll().size();
        customUser.setId(count.incrementAndGet());

        // Create the CustomUser
        CustomUserDTO customUserDTO = customUserMapper.toDto(customUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomUser() throws Exception {
        int databaseSizeBeforeUpdate = customUserRepository.findAll().size();
        customUser.setId(count.incrementAndGet());

        // Create the CustomUser
        CustomUserDTO customUserDTO = customUserMapper.toDto(customUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomUser() throws Exception {
        int databaseSizeBeforeUpdate = customUserRepository.findAll().size();
        customUser.setId(count.incrementAndGet());

        // Create the CustomUser
        CustomUserDTO customUserDTO = customUserMapper.toDto(customUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomUserWithPatch() throws Exception {
        // Initialize the database
        customUserRepository.saveAndFlush(customUser);

        int databaseSizeBeforeUpdate = customUserRepository.findAll().size();

        // Update the customUser using partial update
        CustomUser partialUpdatedCustomUser = new CustomUser();
        partialUpdatedCustomUser.setId(customUser.getId());

        restCustomUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomUser))
            )
            .andExpect(status().isOk());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeUpdate);
        CustomUser testCustomUser = customUserList.get(customUserList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateCustomUserWithPatch() throws Exception {
        // Initialize the database
        customUserRepository.saveAndFlush(customUser);

        int databaseSizeBeforeUpdate = customUserRepository.findAll().size();

        // Update the customUser using partial update
        CustomUser partialUpdatedCustomUser = new CustomUser();
        partialUpdatedCustomUser.setId(customUser.getId());

        restCustomUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomUser))
            )
            .andExpect(status().isOk());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeUpdate);
        CustomUser testCustomUser = customUserList.get(customUserList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingCustomUser() throws Exception {
        int databaseSizeBeforeUpdate = customUserRepository.findAll().size();
        customUser.setId(count.incrementAndGet());

        // Create the CustomUser
        CustomUserDTO customUserDTO = customUserMapper.toDto(customUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomUser() throws Exception {
        int databaseSizeBeforeUpdate = customUserRepository.findAll().size();
        customUser.setId(count.incrementAndGet());

        // Create the CustomUser
        CustomUserDTO customUserDTO = customUserMapper.toDto(customUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomUser() throws Exception {
        int databaseSizeBeforeUpdate = customUserRepository.findAll().size();
        customUser.setId(count.incrementAndGet());

        // Create the CustomUser
        CustomUserDTO customUserDTO = customUserMapper.toDto(customUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(customUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomUser() throws Exception {
        // Initialize the database
        customUserRepository.saveAndFlush(customUser);

        int databaseSizeBeforeDelete = customUserRepository.findAll().size();

        // Delete the customUser
        restCustomUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, customUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
