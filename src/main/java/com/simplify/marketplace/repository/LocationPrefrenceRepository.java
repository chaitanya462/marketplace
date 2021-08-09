package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.LocationPrefrence;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LocationPrefrence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationPrefrenceRepository extends JpaRepository<LocationPrefrence, Long> {}
