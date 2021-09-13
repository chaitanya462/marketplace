package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.LocationPrefrence;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LocationPrefrence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationPrefrenceRepository extends JpaRepository<LocationPrefrence, Long> {
    @Query(" select loc from LocationPrefrence loc where loc.worker.id=:id ")
    Set<LocationPrefrence> findByJobPreferenceId(@Param("id") Long id);
}
