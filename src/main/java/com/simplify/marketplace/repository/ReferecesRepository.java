package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.Refereces;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Refereces entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReferecesRepository extends JpaRepository<Refereces, Long> {}
