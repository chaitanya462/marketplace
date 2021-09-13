package com.simplify.marketplace.service;

import com.simplify.marketplace.service.dto.UserPhoneDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.simplify.marketplace.domain.UserPhone}.
 */
public interface UserPhoneService {
    /**
     * Save a userPhone.
     *
     * @param userPhoneDTO the entity to save.
     * @return the persisted entity.
     */
    UserPhoneDTO save(UserPhoneDTO userPhoneDTO);

    /**
     * Partially updates a userPhone.
     *
     * @param userPhoneDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserPhoneDTO> partialUpdate(UserPhoneDTO userPhoneDTO);

    /**
     * Get all the userPhones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserPhoneDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userPhone.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserPhoneDTO> findOne(Long id);

    /**
     * Delete the "id" userPhone.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
