package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.SkillsMaster;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SkillsMaster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkillsMasterRepository extends JpaRepository<SkillsMaster, Long> {
    //@Query("select skillsMaster from SkillsMaster skillsMaster left join fetch skillsMaster.workers where worker.id=:id")
    List<SkillsMaster> findByWorkers_Id(Long id);

    SkillsMaster findBySkillName(String name);
}
