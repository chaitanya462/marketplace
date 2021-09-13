package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.UserEmail;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserEmail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserEmailRepository extends JpaRepository<UserEmail, Long> {
    @Query("select userEmail from UserEmail userEmail where userEmail.user.login = ?#{principal.username}")
    List<UserEmail> findByUserIsCurrentUser();
}
