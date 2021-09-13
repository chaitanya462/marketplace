package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.*;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Education entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByWorkerId(Long workerid);
}
