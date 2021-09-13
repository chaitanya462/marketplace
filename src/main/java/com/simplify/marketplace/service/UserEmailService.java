package com.simplify.marketplace.service;

import com.simplify.marketplace.service.dto.UserEmailDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.UserEmail}.
 */
public interface UserEmailService {
    /**
     * Save a userEmail.
     *
     * @param userEmailDTO the entity to save.
     * @return the persisted entity.
     */
    UserEmailDTO save(UserEmailDTO userEmailDTO);

    /**
     * Partially updates a userEmail.
     *
     * @param userEmailDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserEmailDTO> partialUpdate(UserEmailDTO userEmailDTO);

    /**
     * Get all the userEmails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserEmailDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userEmail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserEmailDTO> findOne(Long id);

    /**
     * Delete the "id" userEmail.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
