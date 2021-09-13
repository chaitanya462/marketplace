package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Portfolio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByWorkerId(Long workerid);
}
