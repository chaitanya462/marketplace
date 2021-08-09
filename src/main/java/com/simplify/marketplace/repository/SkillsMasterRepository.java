package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.SkillsMaster;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SkillsMaster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkillsMasterRepository extends JpaRepository<SkillsMaster, Long> {}
