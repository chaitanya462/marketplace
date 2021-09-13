package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.Otp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Otp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {}
