package com.simplify.marketplace.service;

import com.simplify.marketplace.service.dto.VmsjobSaveDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.VmsjobSave}.
 */
public interface VmsjobSaveService {
    /**
     * Save a vmsjobSave.
     *
     * @param vmsjobSaveDTO the entity to save.
     * @return the persisted entity.
     */
    VmsjobSaveDTO save(VmsjobSaveDTO vmsjobSaveDTO);

    /**
     * Partially updates a vmsjobSave.
     *
     * @param vmsjobSaveDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VmsjobSaveDTO> partialUpdate(VmsjobSaveDTO vmsjobSaveDTO);

    /**
     * Get all the vmsjobSaves.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VmsjobSaveDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vmsjobSave.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VmsjobSaveDTO> findOne(Long id);

    /**
     * Delete the "id" vmsjobSave.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    
}
