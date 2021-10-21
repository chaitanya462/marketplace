package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.VmsjobSubmit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the VmsjobSubmit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VmsjobSubmitRepository extends JpaRepository<VmsjobSubmit, Long> {}
