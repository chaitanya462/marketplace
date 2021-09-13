package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.OtpAttempt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OtpAttempt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OtpAttemptRepository extends JpaRepository<OtpAttempt, Long> {}
