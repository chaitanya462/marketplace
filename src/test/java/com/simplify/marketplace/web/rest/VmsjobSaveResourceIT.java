package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.VmsjobSave;
import com.simplify.marketplace.repository.VmsjobSaveRepository;
import com.simplify.marketplace.service.dto.VmsjobSaveDTO;
import com.simplify.marketplace.service.mapper.VmsjobSaveMapper;
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
 * Integration tests for the {@link VmsjobSaveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VmsjobSaveResourceIT {

    private static final String DEFAULT_VMSJOBSAVE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VMSJOBSAVE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vmsjob-saves";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VmsjobSaveRepository vmsjobSaveRepository;

    @Autowired
    private VmsjobSaveMapper vmsjobSaveMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVmsjobSaveMockMvc;

    private VmsjobSave vmsjobSave;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VmsjobSave createEntity(EntityManager em) {
        VmsjobSave vmsjobSave = new VmsjobSave().vmsjobsaveName(DEFAULT_VMSJOBSAVE_NAME);
        return vmsjobSave;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VmsjobSave createUpdatedEntity(EntityManager em) {
        VmsjobSave vmsjobSave = new VmsjobSave().vmsjobsaveName(UPDATED_VMSJOBSAVE_NAME);
        return vmsjobSave;
    }

    @BeforeEach
    public void initTest() {
        vmsjobSave = createEntity(em);
    }

    @Test
    @Transactional
    void createVmsjobSave() throws Exception {
        int databaseSizeBeforeCreate = vmsjobSaveRepository.findAll().size();
        // Create the VmsjobSave
        VmsjobSaveDTO vmsjobSaveDTO = vmsjobSaveMapper.toDto(vmsjobSave);
        restVmsjobSaveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vmsjobSaveDTO)))
            .andExpect(status().isCreated());

        // Validate the VmsjobSave in the database
        List<VmsjobSave> vmsjobSaveList = vmsjobSaveRepository.findAll();
        assertThat(vmsjobSaveList).hasSize(databaseSizeBeforeCreate + 1);
        VmsjobSave testVmsjobSave = vmsjobSaveList.get(vmsjobSaveList.size() - 1);
        assertThat(testVmsjobSave.getVmsjobsaveName()).isEqualTo(DEFAULT_VMSJOBSAVE_NAME);
    }

    @Test
    @Transactional
    void createVmsjobSaveWithExistingId() throws Exception {
        // Create the VmsjobSave with an existing ID
        vmsjobSave.setId(1L);
        VmsjobSaveDTO vmsjobSaveDTO = vmsjobSaveMapper.toDto(vmsjobSave);

        int databaseSizeBeforeCreate = vmsjobSaveRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVmsjobSaveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vmsjobSaveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VmsjobSave in the database
        List<VmsjobSave> vmsjobSaveList = vmsjobSaveRepository.findAll();
        assertThat(vmsjobSaveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVmsjobSaves() throws Exception {
        // Initialize the database
        vmsjobSaveRepository.saveAndFlush(vmsjobSave);

        // Get all the vmsjobSaveList
        restVmsjobSaveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vmsjobSave.getId().intValue())))
            .andExpect(jsonPath("$.[*].vmsjobsaveName").value(hasItem(DEFAULT_VMSJOBSAVE_NAME)));
    }

    @Test
    @Transactional
    void getVmsjobSave() throws Exception {
        // Initialize the database
        vmsjobSaveRepository.saveAndFlush(vmsjobSave);

        // Get the vmsjobSave
        restVmsjobSaveMockMvc
            .perform(get(ENTITY_API_URL_ID, vmsjobSave.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vmsjobSave.getId().intValue()))
            .andExpect(jsonPath("$.vmsjobsaveName").value(DEFAULT_VMSJOBSAVE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingVmsjobSave() throws Exception {
        // Get the vmsjobSave
        restVmsjobSaveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVmsjobSave() throws Exception {
        // Initialize the database
        vmsjobSaveRepository.saveAndFlush(vmsjobSave);

        int databaseSizeBeforeUpdate = vmsjobSaveRepository.findAll().size();

        // Update the vmsjobSave
        VmsjobSave updatedVmsjobSave = vmsjobSaveRepository.findById(vmsjobSave.getId()).get();
        // Disconnect from session so that the updates on updatedVmsjobSave are not directly saved in db
        em.detach(updatedVmsjobSave);
        updatedVmsjobSave.vmsjobsaveName(UPDATED_VMSJOBSAVE_NAME);
        VmsjobSaveDTO vmsjobSaveDTO = vmsjobSaveMapper.toDto(updatedVmsjobSave);

        restVmsjobSaveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vmsjobSaveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vmsjobSaveDTO))
            )
            .andExpect(status().isOk());

        // Validate the VmsjobSave in the database
        List<VmsjobSave> vmsjobSaveList = vmsjobSaveRepository.findAll();
        assertThat(vmsjobSaveList).hasSize(databaseSizeBeforeUpdate);
        VmsjobSave testVmsjobSave = vmsjobSaveList.get(vmsjobSaveList.size() - 1);
        assertThat(testVmsjobSave.getVmsjobsaveName()).isEqualTo(UPDATED_VMSJOBSAVE_NAME);
    }

    @Test
    @Transactional
    void putNonExistingVmsjobSave() throws Exception {
        int databaseSizeBeforeUpdate = vmsjobSaveRepository.findAll().size();
        vmsjobSave.setId(count.incrementAndGet());

        // Create the VmsjobSave
        VmsjobSaveDTO vmsjobSaveDTO = vmsjobSaveMapper.toDto(vmsjobSave);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVmsjobSaveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vmsjobSaveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vmsjobSaveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VmsjobSave in the database
        List<VmsjobSave> vmsjobSaveList = vmsjobSaveRepository.findAll();
        assertThat(vmsjobSaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVmsjobSave() throws Exception {
        int databaseSizeBeforeUpdate = vmsjobSaveRepository.findAll().size();
        vmsjobSave.setId(count.incrementAndGet());

        // Create the VmsjobSave
        VmsjobSaveDTO vmsjobSaveDTO = vmsjobSaveMapper.toDto(vmsjobSave);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVmsjobSaveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vmsjobSaveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VmsjobSave in the database
        List<VmsjobSave> vmsjobSaveList = vmsjobSaveRepository.findAll();
        assertThat(vmsjobSaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVmsjobSave() throws Exception {
        int databaseSizeBeforeUpdate = vmsjobSaveRepository.findAll().size();
        vmsjobSave.setId(count.incrementAndGet());

        // Create the VmsjobSave
        VmsjobSaveDTO vmsjobSaveDTO = vmsjobSaveMapper.toDto(vmsjobSave);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVmsjobSaveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vmsjobSaveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VmsjobSave in the database
        List<VmsjobSave> vmsjobSaveList = vmsjobSaveRepository.findAll();
        assertThat(vmsjobSaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVmsjobSaveWithPatch() throws Exception {
        // Initialize the database
        vmsjobSaveRepository.saveAndFlush(vmsjobSave);

        int databaseSizeBeforeUpdate = vmsjobSaveRepository.findAll().size();

        // Update the vmsjobSave using partial update
        VmsjobSave partialUpdatedVmsjobSave = new VmsjobSave();
        partialUpdatedVmsjobSave.setId(vmsjobSave.getId());

        partialUpdatedVmsjobSave.vmsjobsaveName(UPDATED_VMSJOBSAVE_NAME);

        restVmsjobSaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVmsjobSave.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVmsjobSave))
            )
            .andExpect(status().isOk());

        // Validate the VmsjobSave in the database
        List<VmsjobSave> vmsjobSaveList = vmsjobSaveRepository.findAll();
        assertThat(vmsjobSaveList).hasSize(databaseSizeBeforeUpdate);
        VmsjobSave testVmsjobSave = vmsjobSaveList.get(vmsjobSaveList.size() - 1);
        assertThat(testVmsjobSave.getVmsjobsaveName()).isEqualTo(UPDATED_VMSJOBSAVE_NAME);
    }

    @Test
    @Transactional
    void fullUpdateVmsjobSaveWithPatch() throws Exception {
        // Initialize the database
        vmsjobSaveRepository.saveAndFlush(vmsjobSave);

        int databaseSizeBeforeUpdate = vmsjobSaveRepository.findAll().size();

        // Update the vmsjobSave using partial update
        VmsjobSave partialUpdatedVmsjobSave = new VmsjobSave();
        partialUpdatedVmsjobSave.setId(vmsjobSave.getId());

        partialUpdatedVmsjobSave.vmsjobsaveName(UPDATED_VMSJOBSAVE_NAME);

        restVmsjobSaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVmsjobSave.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVmsjobSave))
            )
            .andExpect(status().isOk());

        // Validate the VmsjobSave in the database
        List<VmsjobSave> vmsjobSaveList = vmsjobSaveRepository.findAll();
        assertThat(vmsjobSaveList).hasSize(databaseSizeBeforeUpdate);
        VmsjobSave testVmsjobSave = vmsjobSaveList.get(vmsjobSaveList.size() - 1);
        assertThat(testVmsjobSave.getVmsjobsaveName()).isEqualTo(UPDATED_VMSJOBSAVE_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingVmsjobSave() throws Exception {
        int databaseSizeBeforeUpdate = vmsjobSaveRepository.findAll().size();
        vmsjobSave.setId(count.incrementAndGet());

        // Create the VmsjobSave
        VmsjobSaveDTO vmsjobSaveDTO = vmsjobSaveMapper.toDto(vmsjobSave);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVmsjobSaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vmsjobSaveDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vmsjobSaveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VmsjobSave in the database
        List<VmsjobSave> vmsjobSaveList = vmsjobSaveRepository.findAll();
        assertThat(vmsjobSaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVmsjobSave() throws Exception {
        int databaseSizeBeforeUpdate = vmsjobSaveRepository.findAll().size();
        vmsjobSave.setId(count.incrementAndGet());

        // Create the VmsjobSave
        VmsjobSaveDTO vmsjobSaveDTO = vmsjobSaveMapper.toDto(vmsjobSave);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVmsjobSaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vmsjobSaveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VmsjobSave in the database
        List<VmsjobSave> vmsjobSaveList = vmsjobSaveRepository.findAll();
        assertThat(vmsjobSaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVmsjobSave() throws Exception {
        int databaseSizeBeforeUpdate = vmsjobSaveRepository.findAll().size();
        vmsjobSave.setId(count.incrementAndGet());

        // Create the VmsjobSave
        VmsjobSaveDTO vmsjobSaveDTO = vmsjobSaveMapper.toDto(vmsjobSave);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVmsjobSaveMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vmsjobSaveDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VmsjobSave in the database
        List<VmsjobSave> vmsjobSaveList = vmsjobSaveRepository.findAll();
        assertThat(vmsjobSaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVmsjobSave() throws Exception {
        // Initialize the database
        vmsjobSaveRepository.saveAndFlush(vmsjobSave);

        int databaseSizeBeforeDelete = vmsjobSaveRepository.findAll().size();

        // Delete the vmsjobSave
        restVmsjobSaveMockMvc
            .perform(delete(ENTITY_API_URL_ID, vmsjobSave.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VmsjobSave> vmsjobSaveList = vmsjobSaveRepository.findAll();
        assertThat(vmsjobSaveList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
