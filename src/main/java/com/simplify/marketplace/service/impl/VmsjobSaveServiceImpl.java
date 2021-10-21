package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.VmsjobSave;
import com.simplify.marketplace.repository.VmsjobSaveRepository;
import com.simplify.marketplace.service.VmsjobSaveService;
import com.simplify.marketplace.service.dto.VmsjobSaveDTO;
import com.simplify.marketplace.service.mapper.VmsjobSaveMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VmsjobSave}.
 */
@Service
@Transactional
public class VmsjobSaveServiceImpl implements VmsjobSaveService {

    private final Logger log = LoggerFactory.getLogger(VmsjobSaveServiceImpl.class);

    private final VmsjobSaveRepository vmsjobSaveRepository;

    private final VmsjobSaveMapper vmsjobSaveMapper;

    public VmsjobSaveServiceImpl(VmsjobSaveRepository vmsjobSaveRepository, VmsjobSaveMapper vmsjobSaveMapper) {
        this.vmsjobSaveRepository = vmsjobSaveRepository;
        this.vmsjobSaveMapper = vmsjobSaveMapper;
    }

    @Override
    public VmsjobSaveDTO save(VmsjobSaveDTO vmsjobSaveDTO) {
        log.debug("Request to save VmsjobSave : {}", vmsjobSaveDTO);
        VmsjobSave vmsjobSave = vmsjobSaveMapper.toEntity(vmsjobSaveDTO);
        vmsjobSave = vmsjobSaveRepository.save(vmsjobSave);
        return vmsjobSaveMapper.toDto(vmsjobSave);
    }

    @Override
    public Optional<VmsjobSaveDTO> partialUpdate(VmsjobSaveDTO vmsjobSaveDTO) {
        log.debug("Request to partially update VmsjobSave : {}", vmsjobSaveDTO);

        return vmsjobSaveRepository
            .findById(vmsjobSaveDTO.getId())
            .map(
                existingVmsjobSave -> {
                    vmsjobSaveMapper.partialUpdate(existingVmsjobSave, vmsjobSaveDTO);

                    return existingVmsjobSave;
                }
            )
            .map(vmsjobSaveRepository::save)
            .map(vmsjobSaveMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VmsjobSaveDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VmsjobSaves");
        return vmsjobSaveRepository.findAll(pageable).map(vmsjobSaveMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VmsjobSaveDTO> findOne(Long id) {
        log.debug("Request to get VmsjobSave : {}", id);
        return vmsjobSaveRepository.findById(id).map(vmsjobSaveMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VmsjobSave : {}", id);
        vmsjobSaveRepository.deleteById(id);
    }
}
