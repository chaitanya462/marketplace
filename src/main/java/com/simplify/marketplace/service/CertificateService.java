package com.simplify.marketplace.service;

import com.simplify.marketplace.domain.Certificate;
import com.simplify.marketplace.service.dto.CertificateDTO;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.Certificate}.
 */
public interface CertificateService {
    /**
     * Save a certificate.
     *
     * @param certificateDTO the entity to save.
     * @return the persisted entity.
     */
    CertificateDTO save(CertificateDTO certificateDTO);

    /**
     * Partially updates a certificate.
     *
     * @param certificateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CertificateDTO> partialUpdate(CertificateDTO certificateDTO);

    /**
     * Get all the certificates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CertificateDTO> findAll(Pageable pageable);

    /**
     * Get the "id" certificate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CertificateDTO> findOne(Long id);
    List<Certificate> findOneWorker(Long workerid);

    /**
     * Delete the "id" certificate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    Set<Certificate> insertElasticSearch(CertificateDTO certificateDTO);
}
