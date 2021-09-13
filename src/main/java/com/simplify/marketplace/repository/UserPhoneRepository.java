package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.UserPhone;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserPhone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPhoneRepository extends JpaRepository<UserPhone, Long> {
    @Query("select userPhone from UserPhone userPhone where userPhone.user.login = ?#{principal.username}")
    List<UserPhone> findByUserIsCurrentUser();
}
