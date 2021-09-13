package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.Employment;
import com.simplify.marketplace.domain.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Location entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Set<Location> findByEmploymentId(Long empId);
}
