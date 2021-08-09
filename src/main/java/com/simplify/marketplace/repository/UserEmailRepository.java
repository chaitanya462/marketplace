package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.UserEmail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserEmail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserEmailRepository extends JpaRepository<UserEmail, Long> {}
