package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.VmsjobSave;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the VmsjobSave entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VmsjobSaveRepository extends JpaRepository<VmsjobSave, Long> {
    VmsjobSave findByVmsjobsaveName(String vmsjobsaveName);
}
