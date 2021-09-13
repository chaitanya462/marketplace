package com.simplify.marketplace.service;

import com.simplify.marketplace.service.dto.OtpAttemptDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.OtpAttempt}.
 */
public interface OtpAttemptService {
    /**
     * Save a otpAttempt.
     *
     * @param otpAttemptDTO the entity to save.
     * @return the persisted entity.
     */
    OtpAttemptDTO save(OtpAttemptDTO otpAttemptDTO);

    /**
     * Partially updates a otpAttempt.
     *
     * @param otpAttemptDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OtpAttemptDTO> partialUpdate(OtpAttemptDTO otpAttemptDTO);

    /**
     * Get all the otpAttempts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OtpAttemptDTO> findAll(Pageable pageable);

    /**
     * Get the "id" otpAttempt.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OtpAttemptDTO> findOne(Long id);

    /**
     * Delete the "id" otpAttempt.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
