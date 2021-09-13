package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.*;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Certificate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    //@Query("select certificate from Certificate certificate where certificate.workerid=?1")
    List<Certificate> findByWorkerId(Long workerid);
}
