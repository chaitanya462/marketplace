package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.Employment;
import com.simplify.marketplace.repository.EmploymentRepository;
import com.simplify.marketplace.service.dto.EmploymentDTO;
import com.simplify.marketplace.service.mapper.EmploymentMapper;
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
 * Integration tests for the {@link EmploymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmploymentResourceIT {

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_CURRENT = false;
    private static final Boolean UPDATED_IS_CURRENT = true;

    private static final Integer DEFAULT_LAST_SALARY = 1;
    private static final Integer UPDATED_LAST_SALARY = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private EmploymentMapper employmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmploymentMockMvc;

    private Employment employment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employment createEntity(EntityManager em) {
        Employment employment = new Employment()
            .jobTitle(DEFAULT_JOB_TITLE)
            .companyName(DEFAULT_COMPANY_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .isCurrent(DEFAULT_IS_CURRENT)
            .lastSalary(DEFAULT_LAST_SALARY)
            .description(DEFAULT_DESCRIPTION);
        return employment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employment createUpdatedEntity(EntityManager em) {
        Employment employment = new Employment()
            .jobTitle(UPDATED_JOB_TITLE)
            .companyName(UPDATED_COMPANY_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isCurrent(UPDATED_IS_CURRENT)
            .lastSalary(UPDATED_LAST_SALARY)
            .description(UPDATED_DESCRIPTION);
        return employment;
    }

    @BeforeEach
    public void initTest() {
        employment = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployment() throws Exception {
        int databaseSizeBeforeCreate = employmentRepository.findAll().size();
        // Create the Employment
        EmploymentDTO employmentDTO = employmentMapper.toDto(employment);
        restEmploymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeCreate + 1);
        Employment testEmployment = employmentList.get(employmentList.size() - 1);
        assertThat(testEmployment.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testEmployment.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testEmployment.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEmployment.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testEmployment.getIsCurrent()).isEqualTo(DEFAULT_IS_CURRENT);
        assertThat(testEmployment.getLastSalary()).isEqualTo(DEFAULT_LAST_SALARY);
        assertThat(testEmployment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createEmploymentWithExistingId() throws Exception {
        // Create the Employment with an existing ID
        employment.setId(1L);
        EmploymentDTO employmentDTO = employmentMapper.toDto(employment);

        int databaseSizeBeforeCreate = employmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmploymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployments() throws Exception {
        // Initialize the database
        employmentRepository.saveAndFlush(employment);

        // Get all the employmentList
        restEmploymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employment.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isCurrent").value(hasItem(DEFAULT_IS_CURRENT.booleanValue())))
            .andExpect(jsonPath("$.[*].lastSalary").value(hasItem(DEFAULT_LAST_SALARY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getEmployment() throws Exception {
        // Initialize the database
        employmentRepository.saveAndFlush(employment);

        // Get the employment
        restEmploymentMockMvc
            .perform(get(ENTITY_API_URL_ID, employment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employment.getId().intValue()))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.isCurrent").value(DEFAULT_IS_CURRENT.booleanValue()))
            .andExpect(jsonPath("$.lastSalary").value(DEFAULT_LAST_SALARY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingEmployment() throws Exception {
        // Get the employment
        restEmploymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmployment() throws Exception {
        // Initialize the database
        employmentRepository.saveAndFlush(employment);

        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();

        // Update the employment
        Employment updatedEmployment = employmentRepository.findById(employment.getId()).get();
        // Disconnect from session so that the updates on updatedEmployment are not directly saved in db
        em.detach(updatedEmployment);
        updatedEmployment
            .jobTitle(UPDATED_JOB_TITLE)
            .companyName(UPDATED_COMPANY_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isCurrent(UPDATED_IS_CURRENT)
            .lastSalary(UPDATED_LAST_SALARY)
            .description(UPDATED_DESCRIPTION);
        EmploymentDTO employmentDTO = employmentMapper.toDto(updatedEmployment);

        restEmploymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);
        Employment testEmployment = employmentList.get(employmentList.size() - 1);
        assertThat(testEmployment.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testEmployment.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testEmployment.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEmployment.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEmployment.getIsCurrent()).isEqualTo(UPDATED_IS_CURRENT);
        assertThat(testEmployment.getLastSalary()).isEqualTo(UPDATED_LAST_SALARY);
        assertThat(testEmployment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingEmployment() throws Exception {
        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();
        employment.setId(count.incrementAndGet());

        // Create the Employment
        EmploymentDTO employmentDTO = employmentMapper.toDto(employment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployment() throws Exception {
        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();
        employment.setId(count.incrementAndGet());

        // Create the Employment
        EmploymentDTO employmentDTO = employmentMapper.toDto(employment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployment() throws Exception {
        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();
        employment.setId(count.incrementAndGet());

        // Create the Employment
        EmploymentDTO employmentDTO = employmentMapper.toDto(employment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmploymentWithPatch() throws Exception {
        // Initialize the database
        employmentRepository.saveAndFlush(employment);

        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();

        // Update the employment using partial update
        Employment partialUpdatedEmployment = new Employment();
        partialUpdatedEmployment.setId(employment.getId());

        partialUpdatedEmployment
            .jobTitle(UPDATED_JOB_TITLE)
            .companyName(UPDATED_COMPANY_NAME)
            .startDate(UPDATED_START_DATE)
            .lastSalary(UPDATED_LAST_SALARY);

        restEmploymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployment))
            )
            .andExpect(status().isOk());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);
        Employment testEmployment = employmentList.get(employmentList.size() - 1);
        assertThat(testEmployment.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testEmployment.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testEmployment.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEmployment.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testEmployment.getIsCurrent()).isEqualTo(DEFAULT_IS_CURRENT);
        assertThat(testEmployment.getLastSalary()).isEqualTo(UPDATED_LAST_SALARY);
        assertThat(testEmployment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateEmploymentWithPatch() throws Exception {
        // Initialize the database
        employmentRepository.saveAndFlush(employment);

        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();

        // Update the employment using partial update
        Employment partialUpdatedEmployment = new Employment();
        partialUpdatedEmployment.setId(employment.getId());

        partialUpdatedEmployment
            .jobTitle(UPDATED_JOB_TITLE)
            .companyName(UPDATED_COMPANY_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isCurrent(UPDATED_IS_CURRENT)
            .lastSalary(UPDATED_LAST_SALARY)
            .description(UPDATED_DESCRIPTION);

        restEmploymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployment))
            )
            .andExpect(status().isOk());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);
        Employment testEmployment = employmentList.get(employmentList.size() - 1);
        assertThat(testEmployment.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testEmployment.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testEmployment.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEmployment.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEmployment.getIsCurrent()).isEqualTo(UPDATED_IS_CURRENT);
        assertThat(testEmployment.getLastSalary()).isEqualTo(UPDATED_LAST_SALARY);
        assertThat(testEmployment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingEmployment() throws Exception {
        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();
        employment.setId(count.incrementAndGet());

        // Create the Employment
        EmploymentDTO employmentDTO = employmentMapper.toDto(employment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployment() throws Exception {
        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();
        employment.setId(count.incrementAndGet());

        // Create the Employment
        EmploymentDTO employmentDTO = employmentMapper.toDto(employment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployment() throws Exception {
        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();
        employment.setId(count.incrementAndGet());

        // Create the Employment
        EmploymentDTO employmentDTO = employmentMapper.toDto(employment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployment() throws Exception {
        // Initialize the database
        employmentRepository.saveAndFlush(employment);

        int databaseSizeBeforeDelete = employmentRepository.findAll().size();

        // Delete the employment
        restEmploymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, employment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
