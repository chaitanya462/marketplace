package com.simplify.marketplace.service;

import com.simplify.marketplace.domain.Refereces;
import com.simplify.marketplace.service.dto.ReferecesDTO;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.Refereces}.
 */
public interface ReferecesService {
    /**
     * Save a refereces.
     *
     * @param referecesDTO the entity to save.
     * @return the persisted entity.
     */
    ReferecesDTO save(ReferecesDTO referecesDTO);

    /**
     * Partially updates a refereces.
     *
     * @param referecesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReferecesDTO> partialUpdate(ReferecesDTO referecesDTO);

    /**
     * Get all the refereces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReferecesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" refereces.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReferecesDTO> findOne(Long id);
    List<Refereces> findOneWorker(Long workerid);

    /**
     * Delete the "id" refereces.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    Set<Refereces> getRefereces(ReferecesDTO referecesDTO);
}
