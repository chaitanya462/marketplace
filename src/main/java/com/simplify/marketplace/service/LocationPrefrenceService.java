package com.simplify.marketplace.service;

import com.simplify.marketplace.service.dto.LocationPrefrenceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.LocationPrefrence}.
 */
public interface LocationPrefrenceService {
    /**
     * Save a locationPrefrence.
     *
     * @param locationPrefrenceDTO the entity to save.
     * @return the persisted entity.
     */
    LocationPrefrenceDTO save(LocationPrefrenceDTO locationPrefrenceDTO);

    /**
     * Partially updates a locationPrefrence.
     *
     * @param locationPrefrenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LocationPrefrenceDTO> partialUpdate(LocationPrefrenceDTO locationPrefrenceDTO);

    /**
     * Get all the locationPrefrences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LocationPrefrenceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" locationPrefrence.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LocationPrefrenceDTO> findOne(Long id);

    /**
     * Delete the "id" locationPrefrence.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
