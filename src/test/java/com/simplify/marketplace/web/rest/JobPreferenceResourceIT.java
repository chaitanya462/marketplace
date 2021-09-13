package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.JobPreference;
import com.simplify.marketplace.domain.enumeration.EmploymentType;
import com.simplify.marketplace.domain.enumeration.EngagementType;
import com.simplify.marketplace.domain.enumeration.LocationType;
import com.simplify.marketplace.repository.JobPreferenceRepository;
import com.simplify.marketplace.service.dto.JobPreferenceDTO;
import com.simplify.marketplace.service.mapper.JobPreferenceMapper;
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
 * Integration tests for the {@link JobPreferenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JobPreferenceResourceIT {

    private static final Integer DEFAULT_HOURLY_RATE = 1;
    private static final Integer UPDATED_HOURLY_RATE = 2;

    private static final Integer DEFAULT_DAILY_RATE = 1;
    private static final Integer UPDATED_DAILY_RATE = 2;

    private static final Integer DEFAULT_MONTHLY_RATE = 1;
    private static final Integer UPDATED_MONTHLY_RATE = 2;

    private static final Integer DEFAULT_HOUR_PER_DAY = 1;
    private static final Integer UPDATED_HOUR_PER_DAY = 2;

    private static final Integer DEFAULT_HOUR_PER_WEEK = 1;
    private static final Integer UPDATED_HOUR_PER_WEEK = 2;

    private static final EngagementType DEFAULT_ENGAGEMENT_TYPE = EngagementType.FullTime;
    private static final EngagementType UPDATED_ENGAGEMENT_TYPE = EngagementType.PartTime;

    private static final EmploymentType DEFAULT_EMPLOYMENT_TYPE = EmploymentType.Permanent;
    private static final EmploymentType UPDATED_EMPLOYMENT_TYPE = EmploymentType.Contractual;

    private static final LocationType DEFAULT_LOCATION_TYPE = LocationType.WorkFromOffice;
    private static final LocationType UPDATED_LOCATION_TYPE = LocationType.WorkFromHome;

    private static final LocalDate DEFAULT_AVAILABLE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_AVAILABLE_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_AVAILABLE_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_AVAILABLE_TO = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/job-preferences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JobPreferenceRepository jobPreferenceRepository;

    @Autowired
    private JobPreferenceMapper jobPreferenceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobPreferenceMockMvc;

    private JobPreference jobPreference;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobPreference createEntity(EntityManager em) {
        JobPreference jobPreference = new JobPreference()
            .hourlyRate(DEFAULT_HOURLY_RATE)
            .dailyRate(DEFAULT_DAILY_RATE)
            .monthlyRate(DEFAULT_MONTHLY_RATE)
            .hourPerDay(DEFAULT_HOUR_PER_DAY)
            .hourPerWeek(DEFAULT_HOUR_PER_WEEK)
            .engagementType(DEFAULT_ENGAGEMENT_TYPE)
            .employmentType(DEFAULT_EMPLOYMENT_TYPE)
            .locationType(DEFAULT_LOCATION_TYPE)
            .availableFrom(DEFAULT_AVAILABLE_FROM)
            .availableTo(DEFAULT_AVAILABLE_TO)
            .isActive(DEFAULT_IS_ACTIVE);
        return jobPreference;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobPreference createUpdatedEntity(EntityManager em) {
        JobPreference jobPreference = new JobPreference()
            .hourlyRate(UPDATED_HOURLY_RATE)
            .dailyRate(UPDATED_DAILY_RATE)
            .monthlyRate(UPDATED_MONTHLY_RATE)
            .hourPerDay(UPDATED_HOUR_PER_DAY)
            .hourPerWeek(UPDATED_HOUR_PER_WEEK)
            .engagementType(UPDATED_ENGAGEMENT_TYPE)
            .employmentType(UPDATED_EMPLOYMENT_TYPE)
            .locationType(UPDATED_LOCATION_TYPE)
            .availableFrom(UPDATED_AVAILABLE_FROM)
            .availableTo(UPDATED_AVAILABLE_TO)
            .isActive(UPDATED_IS_ACTIVE);
        return jobPreference;
    }

    @BeforeEach
    public void initTest() {
        jobPreference = createEntity(em);
    }

    @Test
    @Transactional
    void createJobPreference() throws Exception {
        int databaseSizeBeforeCreate = jobPreferenceRepository.findAll().size();
        // Create the JobPreference
        JobPreferenceDTO jobPreferenceDTO = jobPreferenceMapper.toDto(jobPreference);
        restJobPreferenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobPreferenceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeCreate + 1);
        JobPreference testJobPreference = jobPreferenceList.get(jobPreferenceList.size() - 1);
        assertThat(testJobPreference.getHourlyRate()).isEqualTo(DEFAULT_HOURLY_RATE);
        assertThat(testJobPreference.getDailyRate()).isEqualTo(DEFAULT_DAILY_RATE);
        assertThat(testJobPreference.getMonthlyRate()).isEqualTo(DEFAULT_MONTHLY_RATE);
        assertThat(testJobPreference.getHourPerDay()).isEqualTo(DEFAULT_HOUR_PER_DAY);
        assertThat(testJobPreference.getHourPerWeek()).isEqualTo(DEFAULT_HOUR_PER_WEEK);
        assertThat(testJobPreference.getEngagementType()).isEqualTo(DEFAULT_ENGAGEMENT_TYPE);
        assertThat(testJobPreference.getEmploymentType()).isEqualTo(DEFAULT_EMPLOYMENT_TYPE);
        assertThat(testJobPreference.getLocationType()).isEqualTo(DEFAULT_LOCATION_TYPE);
        assertThat(testJobPreference.getAvailableFrom()).isEqualTo(DEFAULT_AVAILABLE_FROM);
        assertThat(testJobPreference.getAvailableTo()).isEqualTo(DEFAULT_AVAILABLE_TO);
        assertThat(testJobPreference.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createJobPreferenceWithExistingId() throws Exception {
        // Create the JobPreference with an existing ID
        jobPreference.setId(1L);
        JobPreferenceDTO jobPreferenceDTO = jobPreferenceMapper.toDto(jobPreference);

        int databaseSizeBeforeCreate = jobPreferenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobPreferenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllJobPreferences() throws Exception {
        // Initialize the database
        jobPreferenceRepository.saveAndFlush(jobPreference);

        // Get all the jobPreferenceList
        restJobPreferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobPreference.getId().intValue())))
            .andExpect(jsonPath("$.[*].hourlyRate").value(hasItem(DEFAULT_HOURLY_RATE)))
            .andExpect(jsonPath("$.[*].dailyRate").value(hasItem(DEFAULT_DAILY_RATE)))
            .andExpect(jsonPath("$.[*].monthlyRate").value(hasItem(DEFAULT_MONTHLY_RATE)))
            .andExpect(jsonPath("$.[*].hourPerDay").value(hasItem(DEFAULT_HOUR_PER_DAY)))
            .andExpect(jsonPath("$.[*].hourPerWeek").value(hasItem(DEFAULT_HOUR_PER_WEEK)))
            .andExpect(jsonPath("$.[*].engagementType").value(hasItem(DEFAULT_ENGAGEMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].employmentType").value(hasItem(DEFAULT_EMPLOYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].locationType").value(hasItem(DEFAULT_LOCATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].availableFrom").value(hasItem(DEFAULT_AVAILABLE_FROM.toString())))
            .andExpect(jsonPath("$.[*].availableTo").value(hasItem(DEFAULT_AVAILABLE_TO.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getJobPreference() throws Exception {
        // Initialize the database
        jobPreferenceRepository.saveAndFlush(jobPreference);

        // Get the jobPreference
        restJobPreferenceMockMvc
            .perform(get(ENTITY_API_URL_ID, jobPreference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobPreference.getId().intValue()))
            .andExpect(jsonPath("$.hourlyRate").value(DEFAULT_HOURLY_RATE))
            .andExpect(jsonPath("$.dailyRate").value(DEFAULT_DAILY_RATE))
            .andExpect(jsonPath("$.monthlyRate").value(DEFAULT_MONTHLY_RATE))
            .andExpect(jsonPath("$.hourPerDay").value(DEFAULT_HOUR_PER_DAY))
            .andExpect(jsonPath("$.hourPerWeek").value(DEFAULT_HOUR_PER_WEEK))
            .andExpect(jsonPath("$.engagementType").value(DEFAULT_ENGAGEMENT_TYPE.toString()))
            .andExpect(jsonPath("$.employmentType").value(DEFAULT_EMPLOYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.locationType").value(DEFAULT_LOCATION_TYPE.toString()))
            .andExpect(jsonPath("$.availableFrom").value(DEFAULT_AVAILABLE_FROM.toString()))
            .andExpect(jsonPath("$.availableTo").value(DEFAULT_AVAILABLE_TO.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingJobPreference() throws Exception {
        // Get the jobPreference
        restJobPreferenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJobPreference() throws Exception {
        // Initialize the database
        jobPreferenceRepository.saveAndFlush(jobPreference);

        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();

        // Update the jobPreference
        JobPreference updatedJobPreference = jobPreferenceRepository.findById(jobPreference.getId()).get();
        // Disconnect from session so that the updates on updatedJobPreference are not directly saved in db
        em.detach(updatedJobPreference);
        updatedJobPreference
            .hourlyRate(UPDATED_HOURLY_RATE)
            .dailyRate(UPDATED_DAILY_RATE)
            .monthlyRate(UPDATED_MONTHLY_RATE)
            .hourPerDay(UPDATED_HOUR_PER_DAY)
            .hourPerWeek(UPDATED_HOUR_PER_WEEK)
            .engagementType(UPDATED_ENGAGEMENT_TYPE)
            .employmentType(UPDATED_EMPLOYMENT_TYPE)
            .locationType(UPDATED_LOCATION_TYPE)
            .availableFrom(UPDATED_AVAILABLE_FROM)
            .availableTo(UPDATED_AVAILABLE_TO)
            .isActive(UPDATED_IS_ACTIVE);
        JobPreferenceDTO jobPreferenceDTO = jobPreferenceMapper.toDto(updatedJobPreference);

        restJobPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobPreferenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobPreferenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);
        JobPreference testJobPreference = jobPreferenceList.get(jobPreferenceList.size() - 1);
        assertThat(testJobPreference.getHourlyRate()).isEqualTo(UPDATED_HOURLY_RATE);
        assertThat(testJobPreference.getDailyRate()).isEqualTo(UPDATED_DAILY_RATE);
        assertThat(testJobPreference.getMonthlyRate()).isEqualTo(UPDATED_MONTHLY_RATE);
        assertThat(testJobPreference.getHourPerDay()).isEqualTo(UPDATED_HOUR_PER_DAY);
        assertThat(testJobPreference.getHourPerWeek()).isEqualTo(UPDATED_HOUR_PER_WEEK);
        assertThat(testJobPreference.getEngagementType()).isEqualTo(UPDATED_ENGAGEMENT_TYPE);
        assertThat(testJobPreference.getEmploymentType()).isEqualTo(UPDATED_EMPLOYMENT_TYPE);
        assertThat(testJobPreference.getLocationType()).isEqualTo(UPDATED_LOCATION_TYPE);
        assertThat(testJobPreference.getAvailableFrom()).isEqualTo(UPDATED_AVAILABLE_FROM);
        assertThat(testJobPreference.getAvailableTo()).isEqualTo(UPDATED_AVAILABLE_TO);
        assertThat(testJobPreference.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingJobPreference() throws Exception {
        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();
        jobPreference.setId(count.incrementAndGet());

        // Create the JobPreference
        JobPreferenceDTO jobPreferenceDTO = jobPreferenceMapper.toDto(jobPreference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobPreferenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJobPreference() throws Exception {
        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();
        jobPreference.setId(count.incrementAndGet());

        // Create the JobPreference
        JobPreferenceDTO jobPreferenceDTO = jobPreferenceMapper.toDto(jobPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJobPreference() throws Exception {
        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();
        jobPreference.setId(count.incrementAndGet());

        // Create the JobPreference
        JobPreferenceDTO jobPreferenceDTO = jobPreferenceMapper.toDto(jobPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobPreferenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJobPreferenceWithPatch() throws Exception {
        // Initialize the database
        jobPreferenceRepository.saveAndFlush(jobPreference);

        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();

        // Update the jobPreference using partial update
        JobPreference partialUpdatedJobPreference = new JobPreference();
        partialUpdatedJobPreference.setId(jobPreference.getId());

        partialUpdatedJobPreference.hourPerDay(UPDATED_HOUR_PER_DAY).hourPerWeek(UPDATED_HOUR_PER_WEEK).locationType(UPDATED_LOCATION_TYPE);

        restJobPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobPreference.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobPreference))
            )
            .andExpect(status().isOk());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);
        JobPreference testJobPreference = jobPreferenceList.get(jobPreferenceList.size() - 1);
        assertThat(testJobPreference.getHourlyRate()).isEqualTo(DEFAULT_HOURLY_RATE);
        assertThat(testJobPreference.getDailyRate()).isEqualTo(DEFAULT_DAILY_RATE);
        assertThat(testJobPreference.getMonthlyRate()).isEqualTo(DEFAULT_MONTHLY_RATE);
        assertThat(testJobPreference.getHourPerDay()).isEqualTo(UPDATED_HOUR_PER_DAY);
        assertThat(testJobPreference.getHourPerWeek()).isEqualTo(UPDATED_HOUR_PER_WEEK);
        assertThat(testJobPreference.getEngagementType()).isEqualTo(DEFAULT_ENGAGEMENT_TYPE);
        assertThat(testJobPreference.getEmploymentType()).isEqualTo(DEFAULT_EMPLOYMENT_TYPE);
        assertThat(testJobPreference.getLocationType()).isEqualTo(UPDATED_LOCATION_TYPE);
        assertThat(testJobPreference.getAvailableFrom()).isEqualTo(DEFAULT_AVAILABLE_FROM);
        assertThat(testJobPreference.getAvailableTo()).isEqualTo(DEFAULT_AVAILABLE_TO);
        assertThat(testJobPreference.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateJobPreferenceWithPatch() throws Exception {
        // Initialize the database
        jobPreferenceRepository.saveAndFlush(jobPreference);

        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();

        // Update the jobPreference using partial update
        JobPreference partialUpdatedJobPreference = new JobPreference();
        partialUpdatedJobPreference.setId(jobPreference.getId());

        partialUpdatedJobPreference
            .hourlyRate(UPDATED_HOURLY_RATE)
            .dailyRate(UPDATED_DAILY_RATE)
            .monthlyRate(UPDATED_MONTHLY_RATE)
            .hourPerDay(UPDATED_HOUR_PER_DAY)
            .hourPerWeek(UPDATED_HOUR_PER_WEEK)
            .engagementType(UPDATED_ENGAGEMENT_TYPE)
            .employmentType(UPDATED_EMPLOYMENT_TYPE)
            .locationType(UPDATED_LOCATION_TYPE)
            .availableFrom(UPDATED_AVAILABLE_FROM)
            .availableTo(UPDATED_AVAILABLE_TO)
            .isActive(UPDATED_IS_ACTIVE);

        restJobPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobPreference.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobPreference))
            )
            .andExpect(status().isOk());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);
        JobPreference testJobPreference = jobPreferenceList.get(jobPreferenceList.size() - 1);
        assertThat(testJobPreference.getHourlyRate()).isEqualTo(UPDATED_HOURLY_RATE);
        assertThat(testJobPreference.getDailyRate()).isEqualTo(UPDATED_DAILY_RATE);
        assertThat(testJobPreference.getMonthlyRate()).isEqualTo(UPDATED_MONTHLY_RATE);
        assertThat(testJobPreference.getHourPerDay()).isEqualTo(UPDATED_HOUR_PER_DAY);
        assertThat(testJobPreference.getHourPerWeek()).isEqualTo(UPDATED_HOUR_PER_WEEK);
        assertThat(testJobPreference.getEngagementType()).isEqualTo(UPDATED_ENGAGEMENT_TYPE);
        assertThat(testJobPreference.getEmploymentType()).isEqualTo(UPDATED_EMPLOYMENT_TYPE);
        assertThat(testJobPreference.getLocationType()).isEqualTo(UPDATED_LOCATION_TYPE);
        assertThat(testJobPreference.getAvailableFrom()).isEqualTo(UPDATED_AVAILABLE_FROM);
        assertThat(testJobPreference.getAvailableTo()).isEqualTo(UPDATED_AVAILABLE_TO);
        assertThat(testJobPreference.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingJobPreference() throws Exception {
        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();
        jobPreference.setId(count.incrementAndGet());

        // Create the JobPreference
        JobPreferenceDTO jobPreferenceDTO = jobPreferenceMapper.toDto(jobPreference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobPreferenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJobPreference() throws Exception {
        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();
        jobPreference.setId(count.incrementAndGet());

        // Create the JobPreference
        JobPreferenceDTO jobPreferenceDTO = jobPreferenceMapper.toDto(jobPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJobPreference() throws Exception {
        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();
        jobPreference.setId(count.incrementAndGet());

        // Create the JobPreference
        JobPreferenceDTO jobPreferenceDTO = jobPreferenceMapper.toDto(jobPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobPreferenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJobPreference() throws Exception {
        // Initialize the database
        jobPreferenceRepository.saveAndFlush(jobPreference);

        int databaseSizeBeforeDelete = jobPreferenceRepository.findAll().size();

        // Delete the jobPreference
        restJobPreferenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, jobPreference.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
