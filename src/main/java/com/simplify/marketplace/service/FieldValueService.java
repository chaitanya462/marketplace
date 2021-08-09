package com.simplify.marketplace.service;

import com.simplify.marketplace.service.dto.FieldValueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.FieldValue}.
 */
public interface FieldValueService {
    /**
     * Save a fieldValue.
     *
     * @param fieldValueDTO the entity to save.
     * @return the persisted entity.
     */
    FieldValueDTO save(FieldValueDTO fieldValueDTO);

    /**
     * Partially updates a fieldValue.
     *
     * @param fieldValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FieldValueDTO> partialUpdate(FieldValueDTO fieldValueDTO);

    /**
     * Get all the fieldValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FieldValueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fieldValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FieldValueDTO> findOne(Long id);

    /**
     * Delete the "id" fieldValue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
