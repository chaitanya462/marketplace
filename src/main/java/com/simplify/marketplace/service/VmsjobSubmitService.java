package com.simplify.marketplace.service;

import com.simplify.marketplace.service.dto.VmsjobSubmitDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.VmsjobSubmit}.
 */
public interface VmsjobSubmitService {
    /**
     * Save a vmsjobSubmit.
     *
     * @param vmsjobSubmitDTO the entity to save.
     * @return the persisted entity.
     */
    VmsjobSubmitDTO save(VmsjobSubmitDTO vmsjobSubmitDTO);

    /**
     * Partially updates a vmsjobSubmit.
     *
     * @param vmsjobSubmitDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VmsjobSubmitDTO> partialUpdate(VmsjobSubmitDTO vmsjobSubmitDTO);

    /**
     * Get all the vmsjobSubmits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VmsjobSubmitDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vmsjobSubmit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VmsjobSubmitDTO> findOne(Long id);

    /**
     * Delete the "id" vmsjobSubmit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
