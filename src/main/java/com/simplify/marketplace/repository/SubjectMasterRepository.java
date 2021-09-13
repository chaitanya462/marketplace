package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.SubjectMaster;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SubjectMaster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubjectMasterRepository extends JpaRepository<SubjectMaster, Long> {}
