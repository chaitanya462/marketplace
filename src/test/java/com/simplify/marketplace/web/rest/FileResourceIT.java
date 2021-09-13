package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.File;
import com.simplify.marketplace.domain.enumeration.FileFormat;
import com.simplify.marketplace.domain.enumeration.FileType;
import com.simplify.marketplace.repository.FileRepository;
import com.simplify.marketplace.service.dto.FileDTO;
import com.simplify.marketplace.service.mapper.FileMapper;
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
 * Integration tests for the {@link FileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileResourceIT {

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final FileFormat DEFAULT_FILEFORMAT = FileFormat.PDF;
    private static final FileFormat UPDATED_FILEFORMAT = FileFormat.DOC;

    private static final FileType DEFAULT_FILETYPE = FileType.Resume;
    private static final FileType UPDATED_FILETYPE = FileType.Profile_Pic;

    private static final String DEFAULT_TAG = "AAAAAAAAAA";
    private static final String UPDATED_TAG = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final Boolean DEFAULT_IS_RESUME = false;
    private static final Boolean UPDATED_IS_RESUME = true;

    private static final Boolean DEFAULT_IS_PROFILE_PIC = false;
    private static final Boolean UPDATED_IS_PROFILE_PIC = true;

    private static final String ENTITY_API_URL = "/api/files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileMockMvc;

    private File file;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createEntity(EntityManager em) {
        File file = new File()
            .path(DEFAULT_PATH)
            .fileformat(DEFAULT_FILEFORMAT)
            .filetype(DEFAULT_FILETYPE)
            .tag(DEFAULT_TAG)
            .isDefault(DEFAULT_IS_DEFAULT)
            .isResume(DEFAULT_IS_RESUME)
            .isProfilePic(DEFAULT_IS_PROFILE_PIC);
        return file;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createUpdatedEntity(EntityManager em) {
        File file = new File()
            .path(UPDATED_PATH)
            .fileformat(UPDATED_FILEFORMAT)
            .filetype(UPDATED_FILETYPE)
            .tag(UPDATED_TAG)
            .isDefault(UPDATED_IS_DEFAULT)
            .isResume(UPDATED_IS_RESUME)
            .isProfilePic(UPDATED_IS_PROFILE_PIC);
        return file;
    }

    @BeforeEach
    public void initTest() {
        file = createEntity(em);
    }

    @Test
    @Transactional
    void createFile() throws Exception {
        int databaseSizeBeforeCreate = fileRepository.findAll().size();
        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);
        restFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDTO)))
            .andExpect(status().isCreated());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeCreate + 1);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testFile.getFileformat()).isEqualTo(DEFAULT_FILEFORMAT);
        assertThat(testFile.getFiletype()).isEqualTo(DEFAULT_FILETYPE);
        assertThat(testFile.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testFile.getIsDefault()).isEqualTo(DEFAULT_IS_DEFAULT);
        assertThat(testFile.getIsResume()).isEqualTo(DEFAULT_IS_RESUME);
        assertThat(testFile.getIsProfilePic()).isEqualTo(DEFAULT_IS_PROFILE_PIC);
    }

    @Test
    @Transactional
    void createFileWithExistingId() throws Exception {
        // Create the File with an existing ID
        file.setId(1L);
        FileDTO fileDTO = fileMapper.toDto(file);

        int databaseSizeBeforeCreate = fileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFiles() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        // Get all the fileList
        restFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(file.getId().intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].fileformat").value(hasItem(DEFAULT_FILEFORMAT.toString())))
            .andExpect(jsonPath("$.[*].filetype").value(hasItem(DEFAULT_FILETYPE.toString())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())))
            .andExpect(jsonPath("$.[*].isResume").value(hasItem(DEFAULT_IS_RESUME.booleanValue())))
            .andExpect(jsonPath("$.[*].isProfilePic").value(hasItem(DEFAULT_IS_PROFILE_PIC.booleanValue())));
    }

    @Test
    @Transactional
    void getFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        // Get the file
        restFileMockMvc
            .perform(get(ENTITY_API_URL_ID, file.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(file.getId().intValue()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.fileformat").value(DEFAULT_FILEFORMAT.toString()))
            .andExpect(jsonPath("$.filetype").value(DEFAULT_FILETYPE.toString()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT.booleanValue()))
            .andExpect(jsonPath("$.isResume").value(DEFAULT_IS_RESUME.booleanValue()))
            .andExpect(jsonPath("$.isProfilePic").value(DEFAULT_IS_PROFILE_PIC.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFile() throws Exception {
        // Get the file
        restFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        int databaseSizeBeforeUpdate = fileRepository.findAll().size();

        // Update the file
        File updatedFile = fileRepository.findById(file.getId()).get();
        // Disconnect from session so that the updates on updatedFile are not directly saved in db
        em.detach(updatedFile);
        updatedFile
            .path(UPDATED_PATH)
            .fileformat(UPDATED_FILEFORMAT)
            .filetype(UPDATED_FILETYPE)
            .tag(UPDATED_TAG)
            .isDefault(UPDATED_IS_DEFAULT)
            .isResume(UPDATED_IS_RESUME)
            .isProfilePic(UPDATED_IS_PROFILE_PIC);
        FileDTO fileDTO = fileMapper.toDto(updatedFile);

        restFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileDTO))
            )
            .andExpect(status().isOk());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testFile.getFileformat()).isEqualTo(UPDATED_FILEFORMAT);
        assertThat(testFile.getFiletype()).isEqualTo(UPDATED_FILETYPE);
        assertThat(testFile.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testFile.getIsDefault()).isEqualTo(UPDATED_IS_DEFAULT);
        assertThat(testFile.getIsResume()).isEqualTo(UPDATED_IS_RESUME);
        assertThat(testFile.getIsProfilePic()).isEqualTo(UPDATED_IS_PROFILE_PIC);
    }

    @Test
    @Transactional
    void putNonExistingFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileWithPatch() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        int databaseSizeBeforeUpdate = fileRepository.findAll().size();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        partialUpdatedFile.path(UPDATED_PATH).isResume(UPDATED_IS_RESUME).isProfilePic(UPDATED_IS_PROFILE_PIC);

        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFile))
            )
            .andExpect(status().isOk());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testFile.getFileformat()).isEqualTo(DEFAULT_FILEFORMAT);
        assertThat(testFile.getFiletype()).isEqualTo(DEFAULT_FILETYPE);
        assertThat(testFile.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testFile.getIsDefault()).isEqualTo(DEFAULT_IS_DEFAULT);
        assertThat(testFile.getIsResume()).isEqualTo(UPDATED_IS_RESUME);
        assertThat(testFile.getIsProfilePic()).isEqualTo(UPDATED_IS_PROFILE_PIC);
    }

    @Test
    @Transactional
    void fullUpdateFileWithPatch() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        int databaseSizeBeforeUpdate = fileRepository.findAll().size();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        partialUpdatedFile
            .path(UPDATED_PATH)
            .fileformat(UPDATED_FILEFORMAT)
            .filetype(UPDATED_FILETYPE)
            .tag(UPDATED_TAG)
            .isDefault(UPDATED_IS_DEFAULT)
            .isResume(UPDATED_IS_RESUME)
            .isProfilePic(UPDATED_IS_PROFILE_PIC);

        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFile))
            )
            .andExpect(status().isOk());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testFile.getFileformat()).isEqualTo(UPDATED_FILEFORMAT);
        assertThat(testFile.getFiletype()).isEqualTo(UPDATED_FILETYPE);
        assertThat(testFile.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testFile.getIsDefault()).isEqualTo(UPDATED_IS_DEFAULT);
        assertThat(testFile.getIsResume()).isEqualTo(UPDATED_IS_RESUME);
        assertThat(testFile.getIsProfilePic()).isEqualTo(UPDATED_IS_PROFILE_PIC);
    }

    @Test
    @Transactional
    void patchNonExistingFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        int databaseSizeBeforeDelete = fileRepository.findAll().size();

        // Delete the file
        restFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, file.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
