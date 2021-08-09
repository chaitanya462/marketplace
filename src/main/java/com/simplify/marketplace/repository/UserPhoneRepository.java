package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.UserPhone;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserPhone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPhoneRepository extends JpaRepository<UserPhone, Long> {}
