package com.simplify.marketplace.service;

import com.simplify.marketplace.domain.Employment;
import com.simplify.marketplace.domain.Location;
import com.simplify.marketplace.service.dto.LocationDTO;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.Location}.
 */
public interface LocationService {
    /**
     * Save a location.
     *
     * @param locationDTO the entity to save.
     * @return the persisted entity.
     */
    LocationDTO save(LocationDTO locationDTO);

    /**
     * Partially updates a location.
     *
     * @param locationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LocationDTO> partialUpdate(LocationDTO locationDTO);

    /**
     * Get all the locations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LocationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" location.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LocationDTO> findOne(Long id);

    /**
     * Delete the "id" location.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Set<Location> fineONEEMP(Long id);
}
