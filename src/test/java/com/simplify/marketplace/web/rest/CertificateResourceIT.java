package com.simplify.marketplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.marketplace.IntegrationTest;
import com.simplify.marketplace.domain.Certificate;
import com.simplify.marketplace.repository.CertificateRepository;
import com.simplify.marketplace.service.dto.CertificateDTO;
import com.simplify.marketplace.service.mapper.CertificateMapper;
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
 * Integration tests for the {@link CertificateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CertificateResourceIT {

    private static final String DEFAULT_CERTIFICATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CERTIFICATE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUER = "AAAAAAAAAA";
    private static final String UPDATED_ISSUER = "BBBBBBBBBB";

    private static final Integer DEFAULT_ISSUE_YEAR = 1;
    private static final Integer UPDATED_ISSUE_YEAR = 2;

    private static final Integer DEFAULT_EXPIRY_YEAR = 1;
    private static final Integer UPDATED_EXPIRY_YEAR = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/certificates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private CertificateMapper certificateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCertificateMockMvc;

    private Certificate certificate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Certificate createEntity(EntityManager em) {
        Certificate certificate = new Certificate()
            .certificateName(DEFAULT_CERTIFICATE_NAME)
            .issuer(DEFAULT_ISSUER)
            .issueYear(DEFAULT_ISSUE_YEAR)
            .expiryYear(DEFAULT_EXPIRY_YEAR)
            .description(DEFAULT_DESCRIPTION);
        return certificate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Certificate createUpdatedEntity(EntityManager em) {
        Certificate certificate = new Certificate()
            .certificateName(UPDATED_CERTIFICATE_NAME)
            .issuer(UPDATED_ISSUER)
            .issueYear(UPDATED_ISSUE_YEAR)
            .expiryYear(UPDATED_EXPIRY_YEAR)
            .description(UPDATED_DESCRIPTION);
        return certificate;
    }

    @BeforeEach
    public void initTest() {
        certificate = createEntity(em);
    }

    @Test
    @Transactional
    void createCertificate() throws Exception {
        int databaseSizeBeforeCreate = certificateRepository.findAll().size();
        // Create the Certificate
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);
        restCertificateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificateDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeCreate + 1);
        Certificate testCertificate = certificateList.get(certificateList.size() - 1);
        assertThat(testCertificate.getCertificateName()).isEqualTo(DEFAULT_CERTIFICATE_NAME);
        assertThat(testCertificate.getIssuer()).isEqualTo(DEFAULT_ISSUER);
        assertThat(testCertificate.getIssueYear()).isEqualTo(DEFAULT_ISSUE_YEAR);
        assertThat(testCertificate.getExpiryYear()).isEqualTo(DEFAULT_EXPIRY_YEAR);
        assertThat(testCertificate.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createCertificateWithExistingId() throws Exception {
        // Create the Certificate with an existing ID
        certificate.setId(1L);
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        int databaseSizeBeforeCreate = certificateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCertificateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCertificates() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get all the certificateList
        restCertificateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(certificate.getId().intValue())))
            .andExpect(jsonPath("$.[*].certificateName").value(hasItem(DEFAULT_CERTIFICATE_NAME)))
            .andExpect(jsonPath("$.[*].issuer").value(hasItem(DEFAULT_ISSUER)))
            .andExpect(jsonPath("$.[*].issueYear").value(hasItem(DEFAULT_ISSUE_YEAR)))
            .andExpect(jsonPath("$.[*].expiryYear").value(hasItem(DEFAULT_EXPIRY_YEAR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCertificate() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get the certificate
        restCertificateMockMvc
            .perform(get(ENTITY_API_URL_ID, certificate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(certificate.getId().intValue()))
            .andExpect(jsonPath("$.certificateName").value(DEFAULT_CERTIFICATE_NAME))
            .andExpect(jsonPath("$.issuer").value(DEFAULT_ISSUER))
            .andExpect(jsonPath("$.issueYear").value(DEFAULT_ISSUE_YEAR))
            .andExpect(jsonPath("$.expiryYear").value(DEFAULT_EXPIRY_YEAR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingCertificate() throws Exception {
        // Get the certificate
        restCertificateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCertificate() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();

        // Update the certificate
        Certificate updatedCertificate = certificateRepository.findById(certificate.getId()).get();
        // Disconnect from session so that the updates on updatedCertificate are not directly saved in db
        em.detach(updatedCertificate);
        updatedCertificate
            .certificateName(UPDATED_CERTIFICATE_NAME)
            .issuer(UPDATED_ISSUER)
            .issueYear(UPDATED_ISSUE_YEAR)
            .expiryYear(UPDATED_EXPIRY_YEAR)
            .description(UPDATED_DESCRIPTION);
        CertificateDTO certificateDTO = certificateMapper.toDto(updatedCertificate);

        restCertificateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, certificateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(certificateDTO))
            )
            .andExpect(status().isOk());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
        Certificate testCertificate = certificateList.get(certificateList.size() - 1);
        assertThat(testCertificate.getCertificateName()).isEqualTo(UPDATED_CERTIFICATE_NAME);
        assertThat(testCertificate.getIssuer()).isEqualTo(UPDATED_ISSUER);
        assertThat(testCertificate.getIssueYear()).isEqualTo(UPDATED_ISSUE_YEAR);
        assertThat(testCertificate.getExpiryYear()).isEqualTo(UPDATED_EXPIRY_YEAR);
        assertThat(testCertificate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingCertificate() throws Exception {
        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();
        certificate.setId(count.incrementAndGet());

        // Create the Certificate
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCertificateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, certificateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(certificateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCertificate() throws Exception {
        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();
        certificate.setId(count.incrementAndGet());

        // Create the Certificate
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(certificateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCertificate() throws Exception {
        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();
        certificate.setId(count.incrementAndGet());

        // Create the Certificate
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCertificateWithPatch() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();

        // Update the certificate using partial update
        Certificate partialUpdatedCertificate = new Certificate();
        partialUpdatedCertificate.setId(certificate.getId());

        partialUpdatedCertificate.certificateName(UPDATED_CERTIFICATE_NAME);

        restCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCertificate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCertificate))
            )
            .andExpect(status().isOk());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
        Certificate testCertificate = certificateList.get(certificateList.size() - 1);
        assertThat(testCertificate.getCertificateName()).isEqualTo(UPDATED_CERTIFICATE_NAME);
        assertThat(testCertificate.getIssuer()).isEqualTo(DEFAULT_ISSUER);
        assertThat(testCertificate.getIssueYear()).isEqualTo(DEFAULT_ISSUE_YEAR);
        assertThat(testCertificate.getExpiryYear()).isEqualTo(DEFAULT_EXPIRY_YEAR);
        assertThat(testCertificate.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCertificateWithPatch() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();

        // Update the certificate using partial update
        Certificate partialUpdatedCertificate = new Certificate();
        partialUpdatedCertificate.setId(certificate.getId());

        partialUpdatedCertificate
            .certificateName(UPDATED_CERTIFICATE_NAME)
            .issuer(UPDATED_ISSUER)
            .issueYear(UPDATED_ISSUE_YEAR)
            .expiryYear(UPDATED_EXPIRY_YEAR)
            .description(UPDATED_DESCRIPTION);

        restCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCertificate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCertificate))
            )
            .andExpect(status().isOk());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
        Certificate testCertificate = certificateList.get(certificateList.size() - 1);
        assertThat(testCertificate.getCertificateName()).isEqualTo(UPDATED_CERTIFICATE_NAME);
        assertThat(testCertificate.getIssuer()).isEqualTo(UPDATED_ISSUER);
        assertThat(testCertificate.getIssueYear()).isEqualTo(UPDATED_ISSUE_YEAR);
        assertThat(testCertificate.getExpiryYear()).isEqualTo(UPDATED_EXPIRY_YEAR);
        assertThat(testCertificate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCertificate() throws Exception {
        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();
        certificate.setId(count.incrementAndGet());

        // Create the Certificate
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, certificateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(certificateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCertificate() throws Exception {
        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();
        certificate.setId(count.incrementAndGet());

        // Create the Certificate
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(certificateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCertificate() throws Exception {
        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();
        certificate.setId(count.incrementAndGet());

        // Create the Certificate
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(certificateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCertificate() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        int databaseSizeBeforeDelete = certificateRepository.findAll().size();

        // Delete the certificate
        restCertificateMockMvc
            .perform(delete(ENTITY_API_URL_ID, certificate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
