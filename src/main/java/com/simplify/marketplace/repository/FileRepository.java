package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.*;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the File entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByWorkerId(Long workerid);
}
