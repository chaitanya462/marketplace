package com.simplify.marketplace.service;

import com.simplify.marketplace.domain.Worker;
import com.simplify.marketplace.domain.VmsjobSubmit;
import java.util.Set;
import com.simplify.marketplace.service.dto.WorkerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.Worker}.
 */
public interface WorkerService {
    /**
     * Save a worker.
     *
     * @param workerDTO the entity to save.
     * @return the persisted entity.
     */
    WorkerDTO save(WorkerDTO workerDTO);

    /**
     * Partially updates a worker.
     *
     * @param workerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkerDTO> partialUpdate(WorkerDTO workerDTO);

    /**
     * Get all the workers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkerDTO> findAll(Pageable pageable);

    /**
     * Get all the workers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkerDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" worker.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkerDTO> findOne(Long id);

    /**
     * Delete the "id" worker.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<Worker> findBySkillsId(Long id);

    Set<VmsjobSubmit> getworkervmsjobsubmits(Long worker_id);
}
