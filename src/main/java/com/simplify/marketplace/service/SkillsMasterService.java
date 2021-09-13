package com.simplify.marketplace.service;

import com.simplify.marketplace.service.dto.SkillsMasterDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.SkillsMaster}.
 */
public interface SkillsMasterService {
    /**
     * Save a skillsMaster.
     *
     * @param skillsMasterDTO the entity to save.
     * @return the persisted entity.
     */
    SkillsMasterDTO save(SkillsMasterDTO skillsMasterDTO);

    /**
     * Partially updates a skillsMaster.
     *
     * @param skillsMasterDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SkillsMasterDTO> partialUpdate(SkillsMasterDTO skillsMasterDTO);

    /**
     * Get all the skillsMasters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SkillsMasterDTO> findAll(Pageable pageable);

    /**
     * Get the "id" skillsMaster.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SkillsMasterDTO> findOne(Long id);

    /**
     * Delete the "id" skillsMaster.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
