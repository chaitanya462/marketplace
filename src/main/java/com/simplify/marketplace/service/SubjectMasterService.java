package com.simplify.marketplace.service;

import com.simplify.marketplace.service.dto.SubjectMasterDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.SubjectMaster}.
 */
public interface SubjectMasterService {
    /**
     * Save a subjectMaster.
     *
     * @param subjectMasterDTO the entity to save.
     * @return the persisted entity.
     */
    SubjectMasterDTO save(SubjectMasterDTO subjectMasterDTO);

    /**
     * Partially updates a subjectMaster.
     *
     * @param subjectMasterDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubjectMasterDTO> partialUpdate(SubjectMasterDTO subjectMasterDTO);

    /**
     * Get all the subjectMasters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubjectMasterDTO> findAll(Pageable pageable);

    /**
     * Get the "id" subjectMaster.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubjectMasterDTO> findOne(Long id);

    /**
     * Delete the "id" subjectMaster.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
