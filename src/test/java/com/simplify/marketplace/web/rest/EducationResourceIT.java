package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.Education;
import com.simplify.marketplace.domain.enumeration.DegreeType;
import com.simplify.marketplace.domain.enumeration.EducationGrade;
import com.simplify.marketplace.domain.enumeration.MarksType;
import com.simplify.marketplace.repository.EducationRepository;
import com.simplify.marketplace.service.dto.EducationDTO;
import com.simplify.marketplace.service.mapper.EducationMapper;
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
 * Integration tests for the {@link EducationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EducationResourceIT {

    private static final String DEFAULT_DEGREE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEGREE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTE = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR_OF_PASSING = 1;
    private static final Integer UPDATED_YEAR_OF_PASSING = 2;

    private static final Float DEFAULT_MARKS = 1F;
    private static final Float UPDATED_MARKS = 2F;

    private static final MarksType DEFAULT_MARKS_TYPE = MarksType.PERCENTAGE;
    private static final MarksType UPDATED_MARKS_TYPE = MarksType.CGPA;

    private static final EducationGrade DEFAULT_GRADE = EducationGrade.FIRST;
    private static final EducationGrade UPDATED_GRADE = EducationGrade.SECOND;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_COMPLETE = false;
    private static final Boolean UPDATED_IS_COMPLETE = true;

    private static final DegreeType DEFAULT_DEGREE_TYPE = DegreeType.HSC;
    private static final DegreeType UPDATED_DEGREE_TYPE = DegreeType.Graduate;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/educations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private EducationMapper educationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEducationMockMvc;

    private Education education;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Education createEntity(EntityManager em) {
        Education education = new Education()
            .degreeName(DEFAULT_DEGREE_NAME)
            .institute(DEFAULT_INSTITUTE)
            .yearOfPassing(DEFAULT_YEAR_OF_PASSING)
            .marks(DEFAULT_MARKS)
            .marksType(DEFAULT_MARKS_TYPE)
            .grade(DEFAULT_GRADE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .isComplete(DEFAULT_IS_COMPLETE)
            .degreeType(DEFAULT_DEGREE_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return education;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Education createUpdatedEntity(EntityManager em) {
        Education education = new Education()
            .degreeName(UPDATED_DEGREE_NAME)
            .institute(UPDATED_INSTITUTE)
            .yearOfPassing(UPDATED_YEAR_OF_PASSING)
            .marks(UPDATED_MARKS)
            .marksType(UPDATED_MARKS_TYPE)
            .grade(UPDATED_GRADE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isComplete(UPDATED_IS_COMPLETE)
            .degreeType(UPDATED_DEGREE_TYPE)
            .description(UPDATED_DESCRIPTION);
        return education;
    }

    @BeforeEach
    public void initTest() {
        education = createEntity(em);
    }

    @Test
    @Transactional
    void createEducation() throws Exception {
        int databaseSizeBeforeCreate = educationRepository.findAll().size();
        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);
        restEducationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationDTO)))
            .andExpect(status().isCreated());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeCreate + 1);
        Education testEducation = educationList.get(educationList.size() - 1);
        assertThat(testEducation.getDegreeName()).isEqualTo(DEFAULT_DEGREE_NAME);
        assertThat(testEducation.getInstitute()).isEqualTo(DEFAULT_INSTITUTE);
        assertThat(testEducation.getYearOfPassing()).isEqualTo(DEFAULT_YEAR_OF_PASSING);
        assertThat(testEducation.getMarks()).isEqualTo(DEFAULT_MARKS);
        assertThat(testEducation.getMarksType()).isEqualTo(DEFAULT_MARKS_TYPE);
        assertThat(testEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testEducation.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEducation.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testEducation.getIsComplete()).isEqualTo(DEFAULT_IS_COMPLETE);
        assertThat(testEducation.getDegreeType()).isEqualTo(DEFAULT_DEGREE_TYPE);
        assertThat(testEducation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createEducationWithExistingId() throws Exception {
        // Create the Education with an existing ID
        education.setId(1L);
        EducationDTO educationDTO = educationMapper.toDto(education);

        int databaseSizeBeforeCreate = educationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEducationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEducations() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList
        restEducationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(education.getId().intValue())))
            .andExpect(jsonPath("$.[*].degreeName").value(hasItem(DEFAULT_DEGREE_NAME)))
            .andExpect(jsonPath("$.[*].institute").value(hasItem(DEFAULT_INSTITUTE)))
            .andExpect(jsonPath("$.[*].yearOfPassing").value(hasItem(DEFAULT_YEAR_OF_PASSING)))
            .andExpect(jsonPath("$.[*].marks").value(hasItem(DEFAULT_MARKS.doubleValue())))
            .andExpect(jsonPath("$.[*].marksType").value(hasItem(DEFAULT_MARKS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isComplete").value(hasItem(DEFAULT_IS_COMPLETE.booleanValue())))
            .andExpect(jsonPath("$.[*].degreeType").value(hasItem(DEFAULT_DEGREE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getEducation() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get the education
        restEducationMockMvc
            .perform(get(ENTITY_API_URL_ID, education.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(education.getId().intValue()))
            .andExpect(jsonPath("$.degreeName").value(DEFAULT_DEGREE_NAME))
            .andExpect(jsonPath("$.institute").value(DEFAULT_INSTITUTE))
            .andExpect(jsonPath("$.yearOfPassing").value(DEFAULT_YEAR_OF_PASSING))
            .andExpect(jsonPath("$.marks").value(DEFAULT_MARKS.doubleValue()))
            .andExpect(jsonPath("$.marksType").value(DEFAULT_MARKS_TYPE.toString()))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.isComplete").value(DEFAULT_IS_COMPLETE.booleanValue()))
            .andExpect(jsonPath("$.degreeType").value(DEFAULT_DEGREE_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingEducation() throws Exception {
        // Get the education
        restEducationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEducation() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        int databaseSizeBeforeUpdate = educationRepository.findAll().size();

        // Update the education
        Education updatedEducation = educationRepository.findById(education.getId()).get();
        // Disconnect from session so that the updates on updatedEducation are not directly saved in db
        em.detach(updatedEducation);
        updatedEducation
            .degreeName(UPDATED_DEGREE_NAME)
            .institute(UPDATED_INSTITUTE)
            .yearOfPassing(UPDATED_YEAR_OF_PASSING)
            .marks(UPDATED_MARKS)
            .marksType(UPDATED_MARKS_TYPE)
            .grade(UPDATED_GRADE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isComplete(UPDATED_IS_COMPLETE)
            .degreeType(UPDATED_DEGREE_TYPE)
            .description(UPDATED_DESCRIPTION);
        EducationDTO educationDTO = educationMapper.toDto(updatedEducation);

        restEducationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, educationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
        Education testEducation = educationList.get(educationList.size() - 1);
        assertThat(testEducation.getDegreeName()).isEqualTo(UPDATED_DEGREE_NAME);
        assertThat(testEducation.getInstitute()).isEqualTo(UPDATED_INSTITUTE);
        assertThat(testEducation.getYearOfPassing()).isEqualTo(UPDATED_YEAR_OF_PASSING);
        assertThat(testEducation.getMarks()).isEqualTo(UPDATED_MARKS);
        assertThat(testEducation.getMarksType()).isEqualTo(UPDATED_MARKS_TYPE);
        assertThat(testEducation.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testEducation.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEducation.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEducation.getIsComplete()).isEqualTo(UPDATED_IS_COMPLETE);
        assertThat(testEducation.getDegreeType()).isEqualTo(UPDATED_DEGREE_TYPE);
        assertThat(testEducation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, educationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEducationWithPatch() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        int databaseSizeBeforeUpdate = educationRepository.findAll().size();

        // Update the education using partial update
        Education partialUpdatedEducation = new Education();
        partialUpdatedEducation.setId(education.getId());

        partialUpdatedEducation
            .marks(UPDATED_MARKS)
            .marksType(UPDATED_MARKS_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .degreeType(UPDATED_DEGREE_TYPE)
            .description(UPDATED_DESCRIPTION);

        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEducation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEducation))
            )
            .andExpect(status().isOk());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
        Education testEducation = educationList.get(educationList.size() - 1);
        assertThat(testEducation.getDegreeName()).isEqualTo(DEFAULT_DEGREE_NAME);
        assertThat(testEducation.getInstitute()).isEqualTo(DEFAULT_INSTITUTE);
        assertThat(testEducation.getYearOfPassing()).isEqualTo(DEFAULT_YEAR_OF_PASSING);
        assertThat(testEducation.getMarks()).isEqualTo(UPDATED_MARKS);
        assertThat(testEducation.getMarksType()).isEqualTo(UPDATED_MARKS_TYPE);
        assertThat(testEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testEducation.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEducation.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEducation.getIsComplete()).isEqualTo(DEFAULT_IS_COMPLETE);
        assertThat(testEducation.getDegreeType()).isEqualTo(UPDATED_DEGREE_TYPE);
        assertThat(testEducation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateEducationWithPatch() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        int databaseSizeBeforeUpdate = educationRepository.findAll().size();

        // Update the education using partial update
        Education partialUpdatedEducation = new Education();
        partialUpdatedEducation.setId(education.getId());

        partialUpdatedEducation
            .degreeName(UPDATED_DEGREE_NAME)
            .institute(UPDATED_INSTITUTE)
            .yearOfPassing(UPDATED_YEAR_OF_PASSING)
            .marks(UPDATED_MARKS)
            .marksType(UPDATED_MARKS_TYPE)
            .grade(UPDATED_GRADE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isComplete(UPDATED_IS_COMPLETE)
            .degreeType(UPDATED_DEGREE_TYPE)
            .description(UPDATED_DESCRIPTION);

        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEducation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEducation))
            )
            .andExpect(status().isOk());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
        Education testEducation = educationList.get(educationList.size() - 1);
        assertThat(testEducation.getDegreeName()).isEqualTo(UPDATED_DEGREE_NAME);
        assertThat(testEducation.getInstitute()).isEqualTo(UPDATED_INSTITUTE);
        assertThat(testEducation.getYearOfPassing()).isEqualTo(UPDATED_YEAR_OF_PASSING);
        assertThat(testEducation.getMarks()).isEqualTo(UPDATED_MARKS);
        assertThat(testEducation.getMarksType()).isEqualTo(UPDATED_MARKS_TYPE);
        assertThat(testEducation.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testEducation.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEducation.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEducation.getIsComplete()).isEqualTo(UPDATED_IS_COMPLETE);
        assertThat(testEducation.getDegreeType()).isEqualTo(UPDATED_DEGREE_TYPE);
        assertThat(testEducation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, educationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(educationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(educationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(educationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEducation() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        int databaseSizeBeforeDelete = educationRepository.findAll().size();

        // Delete the education
        restEducationMockMvc
            .perform(delete(ENTITY_API_URL_ID, education.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
