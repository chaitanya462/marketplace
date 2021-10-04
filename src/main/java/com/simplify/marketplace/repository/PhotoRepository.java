package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.Photo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Photo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Photo findByWorkerId(Long workerid);
}
