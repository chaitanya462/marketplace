package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.Worker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Worker entity.
 */
@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    @Query(
        value = "select distinct worker from Worker worker left join fetch worker.skills",
        countQuery = "select count(distinct worker) from Worker worker"
    )
    Page<Worker> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct worker from Worker worker left join fetch worker.skills")
    List<Worker> findAllWithEagerRelationships();

    @Query("select worker from Worker worker left join fetch worker.skills where worker.id =:id")
    Optional<Worker> findOneWithEagerRelationships(@Param("id") Long id);
}
