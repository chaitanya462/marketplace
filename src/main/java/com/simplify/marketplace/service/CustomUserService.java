package com.simplify.marketplace.service;

import com.simplify.marketplace.service.dto.CustomUserDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.CustomUser}.
 */
public interface CustomUserService {
    /**
     * Save a customUser.
     *
     * @param customUserDTO the entity to save.
     * @return the persisted entity.
     */
    CustomUserDTO save(CustomUserDTO customUserDTO);

    /**
     * Partially updates a customUser.
     *
     * @param customUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomUserDTO> partialUpdate(CustomUserDTO customUserDTO);

    /**
     * Get all the customUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CustomUserDTO> findAll(Pageable pageable);

    /**
     * Get the "id" customUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomUserDTO> findOne(Long id);

    /**
     * Delete the "id" customUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
