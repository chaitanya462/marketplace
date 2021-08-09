package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.FieldValue;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FieldValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FieldValueRepository extends JpaRepository<FieldValue, Long> {}
