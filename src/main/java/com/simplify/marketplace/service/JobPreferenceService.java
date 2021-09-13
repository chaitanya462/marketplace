package com.simplify.marketplace.service;

import com.simplify.marketplace.domain.JobPreference;
import com.simplify.marketplace.service.dto.JobPreferenceDTO;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.JobPreference}.
 */
public interface JobPreferenceService {
    /**
     * Save a jobPreference.
     *
     * @param jobPreferenceDTO the entity to save.
     * @return the persisted entity.
     */
    JobPreferenceDTO save(JobPreferenceDTO jobPreferenceDTO);

    /**
     * Partially updates a jobPreference.
     *
     * @param jobPreferenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<JobPreferenceDTO> partialUpdate(JobPreferenceDTO jobPreferenceDTO);

    /**
     * Get all the jobPreferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<JobPreferenceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" jobPreference.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JobPreferenceDTO> findOne(Long id);
    List<JobPreference> findOneWorker(Long workerid);

    /**
     * Delete the "id" jobPreference.
     *
     * @param id the id of the entity.
     */

    void delete(Long id);
    Set<JobPreference> getJobPreferences(JobPreferenceDTO jobPreferenceDTO);
}
