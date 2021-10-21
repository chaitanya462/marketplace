package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.VmsjobSubmit;
import com.simplify.marketplace.repository.VmsjobSubmitRepository;
import com.simplify.marketplace.service.dto.VmsjobSubmitDTO;
import com.simplify.marketplace.service.mapper.VmsjobSubmitMapper;
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
 * Integration tests for the {@link VmsjobSubmitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VmsjobSubmitResourceIT {

    private static final String DEFAULT_VMSJOBSUBMIT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VMSJOBSUBMIT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vmsjob-submits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VmsjobSubmitRepository vmsjobSubmitRepository;

    @Autowired
    private VmsjobSubmitMapper vmsjobSubmitMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVmsjobSubmitMockMvc;

    private VmsjobSubmit vmsjobSubmit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VmsjobSubmit createEntity(EntityManager em) {
        VmsjobSubmit vmsjobSubmit = new VmsjobSubmit().vmsjobsubmitName(DEFAULT_VMSJOBSUBMIT_NAME);
        return vmsjobSubmit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VmsjobSubmit createUpdatedEntity(EntityManager em) {
        VmsjobSubmit vmsjobSubmit = new VmsjobSubmit().vmsjobsubmitName(UPDATED_VMSJOBSUBMIT_NAME);
        return vmsjobSubmit;
    }

    @BeforeEach
    public void initTest() {
        vmsjobSubmit = createEntity(em);
    }

    @Test
    @Transactional
    void createVmsjobSubmit() throws Exception {
        int databaseSizeBeforeCreate = vmsjobSubmitRepository.findAll().size();
        // Create the VmsjobSubmit
        VmsjobSubmitDTO vmsjobSubmitDTO = vmsjobSubmitMapper.toDto(vmsjobSubmit);
        restVmsjobSubmitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vmsjobSubmitDTO))
            )
            .andExpect(status().isCreated());

        // Validate the VmsjobSubmit in the database
        List<VmsjobSubmit> vmsjobSubmitList = vmsjobSubmitRepository.findAll();
        assertThat(vmsjobSubmitList).hasSize(databaseSizeBeforeCreate + 1);
        VmsjobSubmit testVmsjobSubmit = vmsjobSubmitList.get(vmsjobSubmitList.size() - 1);
        assertThat(testVmsjobSubmit.getVmsjobsubmitName()).isEqualTo(DEFAULT_VMSJOBSUBMIT_NAME);
    }

    @Test
    @Transactional
    void createVmsjobSubmitWithExistingId() throws Exception {
        // Create the VmsjobSubmit with an existing ID
        vmsjobSubmit.setId(1L);
        VmsjobSubmitDTO vmsjobSubmitDTO = vmsjobSubmitMapper.toDto(vmsjobSubmit);

        int databaseSizeBeforeCreate = vmsjobSubmitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVmsjobSubmitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vmsjobSubmitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VmsjobSubmit in the database
        List<VmsjobSubmit> vmsjobSubmitList = vmsjobSubmitRepository.findAll();
        assertThat(vmsjobSubmitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVmsjobSubmits() throws Exception {
        // Initialize the database
        vmsjobSubmitRepository.saveAndFlush(vmsjobSubmit);

        // Get all the vmsjobSubmitList
        restVmsjobSubmitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vmsjobSubmit.getId().intValue())))
            .andExpect(jsonPath("$.[*].vmsjobsubmitName").value(hasItem(DEFAULT_VMSJOBSUBMIT_NAME)));
    }

    @Test
    @Transactional
    void getVmsjobSubmit() throws Exception {
        // Initialize the database
        vmsjobSubmitRepository.saveAndFlush(vmsjobSubmit);

        // Get the vmsjobSubmit
        restVmsjobSubmitMockMvc
            .perform(get(ENTITY_API_URL_ID, vmsjobSubmit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vmsjobSubmit.getId().intValue()))
            .andExpect(jsonPath("$.vmsjobsubmitName").value(DEFAULT_VMSJOBSUBMIT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingVmsjobSubmit() throws Exception {
        // Get the vmsjobSubmit
        restVmsjobSubmitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVmsjobSubmit() throws Exception {
        // Initialize the database
        vmsjobSubmitRepository.saveAndFlush(vmsjobSubmit);

        int databaseSizeBeforeUpdate = vmsjobSubmitRepository.findAll().size();

        // Update the vmsjobSubmit
        VmsjobSubmit updatedVmsjobSubmit = vmsjobSubmitRepository.findById(vmsjobSubmit.getId()).get();
        // Disconnect from session so that the updates on updatedVmsjobSubmit are not directly saved in db
        em.detach(updatedVmsjobSubmit);
        updatedVmsjobSubmit.vmsjobsubmitName(UPDATED_VMSJOBSUBMIT_NAME);
        VmsjobSubmitDTO vmsjobSubmitDTO = vmsjobSubmitMapper.toDto(updatedVmsjobSubmit);

        restVmsjobSubmitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vmsjobSubmitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vmsjobSubmitDTO))
            )
            .andExpect(status().isOk());

        // Validate the VmsjobSubmit in the database
        List<VmsjobSubmit> vmsjobSubmitList = vmsjobSubmitRepository.findAll();
        assertThat(vmsjobSubmitList).hasSize(databaseSizeBeforeUpdate);
        VmsjobSubmit testVmsjobSubmit = vmsjobSubmitList.get(vmsjobSubmitList.size() - 1);
        assertThat(testVmsjobSubmit.getVmsjobsubmitName()).isEqualTo(UPDATED_VMSJOBSUBMIT_NAME);
    }

    @Test
    @Transactional
    void putNonExistingVmsjobSubmit() throws Exception {
        int databaseSizeBeforeUpdate = vmsjobSubmitRepository.findAll().size();
        vmsjobSubmit.setId(count.incrementAndGet());

        // Create the VmsjobSubmit
        VmsjobSubmitDTO vmsjobSubmitDTO = vmsjobSubmitMapper.toDto(vmsjobSubmit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVmsjobSubmitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vmsjobSubmitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vmsjobSubmitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VmsjobSubmit in the database
        List<VmsjobSubmit> vmsjobSubmitList = vmsjobSubmitRepository.findAll();
        assertThat(vmsjobSubmitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVmsjobSubmit() throws Exception {
        int databaseSizeBeforeUpdate = vmsjobSubmitRepository.findAll().size();
        vmsjobSubmit.setId(count.incrementAndGet());

        // Create the VmsjobSubmit
        VmsjobSubmitDTO vmsjobSubmitDTO = vmsjobSubmitMapper.toDto(vmsjobSubmit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVmsjobSubmitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vmsjobSubmitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VmsjobSubmit in the database
        List<VmsjobSubmit> vmsjobSubmitList = vmsjobSubmitRepository.findAll();
        assertThat(vmsjobSubmitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVmsjobSubmit() throws Exception {
        int databaseSizeBeforeUpdate = vmsjobSubmitRepository.findAll().size();
        vmsjobSubmit.setId(count.incrementAndGet());

        // Create the VmsjobSubmit
        VmsjobSubmitDTO vmsjobSubmitDTO = vmsjobSubmitMapper.toDto(vmsjobSubmit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVmsjobSubmitMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vmsjobSubmitDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VmsjobSubmit in the database
        List<VmsjobSubmit> vmsjobSubmitList = vmsjobSubmitRepository.findAll();
        assertThat(vmsjobSubmitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVmsjobSubmitWithPatch() throws Exception {
        // Initialize the database
        vmsjobSubmitRepository.saveAndFlush(vmsjobSubmit);

        int databaseSizeBeforeUpdate = vmsjobSubmitRepository.findAll().size();

        // Update the vmsjobSubmit using partial update
        VmsjobSubmit partialUpdatedVmsjobSubmit = new VmsjobSubmit();
        partialUpdatedVmsjobSubmit.setId(vmsjobSubmit.getId());

        restVmsjobSubmitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVmsjobSubmit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVmsjobSubmit))
            )
            .andExpect(status().isOk());

        // Validate the VmsjobSubmit in the database
        List<VmsjobSubmit> vmsjobSubmitList = vmsjobSubmitRepository.findAll();
        assertThat(vmsjobSubmitList).hasSize(databaseSizeBeforeUpdate);
        VmsjobSubmit testVmsjobSubmit = vmsjobSubmitList.get(vmsjobSubmitList.size() - 1);
        assertThat(testVmsjobSubmit.getVmsjobsubmitName()).isEqualTo(DEFAULT_VMSJOBSUBMIT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateVmsjobSubmitWithPatch() throws Exception {
        // Initialize the database
        vmsjobSubmitRepository.saveAndFlush(vmsjobSubmit);

        int databaseSizeBeforeUpdate = vmsjobSubmitRepository.findAll().size();

        // Update the vmsjobSubmit using partial update
        VmsjobSubmit partialUpdatedVmsjobSubmit = new VmsjobSubmit();
        partialUpdatedVmsjobSubmit.setId(vmsjobSubmit.getId());

        partialUpdatedVmsjobSubmit.vmsjobsubmitName(UPDATED_VMSJOBSUBMIT_NAME);

        restVmsjobSubmitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVmsjobSubmit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVmsjobSubmit))
            )
            .andExpect(status().isOk());

        // Validate the VmsjobSubmit in the database
        List<VmsjobSubmit> vmsjobSubmitList = vmsjobSubmitRepository.findAll();
        assertThat(vmsjobSubmitList).hasSize(databaseSizeBeforeUpdate);
        VmsjobSubmit testVmsjobSubmit = vmsjobSubmitList.get(vmsjobSubmitList.size() - 1);
        assertThat(testVmsjobSubmit.getVmsjobsubmitName()).isEqualTo(UPDATED_VMSJOBSUBMIT_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingVmsjobSubmit() throws Exception {
        int databaseSizeBeforeUpdate = vmsjobSubmitRepository.findAll().size();
        vmsjobSubmit.setId(count.incrementAndGet());

        // Create the VmsjobSubmit
        VmsjobSubmitDTO vmsjobSubmitDTO = vmsjobSubmitMapper.toDto(vmsjobSubmit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVmsjobSubmitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vmsjobSubmitDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vmsjobSubmitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VmsjobSubmit in the database
        List<VmsjobSubmit> vmsjobSubmitList = vmsjobSubmitRepository.findAll();
        assertThat(vmsjobSubmitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVmsjobSubmit() throws Exception {
        int databaseSizeBeforeUpdate = vmsjobSubmitRepository.findAll().size();
        vmsjobSubmit.setId(count.incrementAndGet());

        // Create the VmsjobSubmit
        VmsjobSubmitDTO vmsjobSubmitDTO = vmsjobSubmitMapper.toDto(vmsjobSubmit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVmsjobSubmitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vmsjobSubmitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VmsjobSubmit in the database
        List<VmsjobSubmit> vmsjobSubmitList = vmsjobSubmitRepository.findAll();
        assertThat(vmsjobSubmitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVmsjobSubmit() throws Exception {
        int databaseSizeBeforeUpdate = vmsjobSubmitRepository.findAll().size();
        vmsjobSubmit.setId(count.incrementAndGet());

        // Create the VmsjobSubmit
        VmsjobSubmitDTO vmsjobSubmitDTO = vmsjobSubmitMapper.toDto(vmsjobSubmit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVmsjobSubmitMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vmsjobSubmitDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VmsjobSubmit in the database
        List<VmsjobSubmit> vmsjobSubmitList = vmsjobSubmitRepository.findAll();
        assertThat(vmsjobSubmitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVmsjobSubmit() throws Exception {
        // Initialize the database
        vmsjobSubmitRepository.saveAndFlush(vmsjobSubmit);

        int databaseSizeBeforeDelete = vmsjobSubmitRepository.findAll().size();

        // Delete the vmsjobSubmit
        restVmsjobSubmitMockMvc
            .perform(delete(ENTITY_API_URL_ID, vmsjobSubmit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VmsjobSubmit> vmsjobSubmitList = vmsjobSubmitRepository.findAll();
        assertThat(vmsjobSubmitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
