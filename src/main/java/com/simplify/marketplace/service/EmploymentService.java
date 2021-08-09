package com.simplify.marketplace.service;

import com.simplify.marketplace.service.dto.EmploymentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.Employment}.
 */
public interface EmploymentService {
    /**
     * Save a employment.
     *
     * @param employmentDTO the entity to save.
     * @return the persisted entity.
     */
    EmploymentDTO save(EmploymentDTO employmentDTO);

    /**
     * Partially updates a employment.
     *
     * @param employmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmploymentDTO> partialUpdate(EmploymentDTO employmentDTO);

    /**
     * Get all the employments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmploymentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" employment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmploymentDTO> findOne(Long id);

    /**
     * Delete the "id" employment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
