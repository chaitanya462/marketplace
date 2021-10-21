package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.VmsjobSubmit;
import com.simplify.marketplace.repository.VmsjobSubmitRepository;
import com.simplify.marketplace.service.VmsjobSubmitService;
import com.simplify.marketplace.service.dto.VmsjobSubmitDTO;
import com.simplify.marketplace.service.mapper.VmsjobSubmitMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VmsjobSubmit}.
 */
@Service
@Transactional
public class VmsjobSubmitServiceImpl implements VmsjobSubmitService {

    private final Logger log = LoggerFactory.getLogger(VmsjobSubmitServiceImpl.class);

    private final VmsjobSubmitRepository vmsjobSubmitRepository;

    private final VmsjobSubmitMapper vmsjobSubmitMapper;

    public VmsjobSubmitServiceImpl(VmsjobSubmitRepository vmsjobSubmitRepository, VmsjobSubmitMapper vmsjobSubmitMapper) {
        this.vmsjobSubmitRepository = vmsjobSubmitRepository;
        this.vmsjobSubmitMapper = vmsjobSubmitMapper;
    }

    @Override
    public VmsjobSubmitDTO save(VmsjobSubmitDTO vmsjobSubmitDTO) {
        log.debug("Request to save VmsjobSubmit : {}", vmsjobSubmitDTO);
        VmsjobSubmit vmsjobSubmit = vmsjobSubmitMapper.toEntity(vmsjobSubmitDTO);
        vmsjobSubmit = vmsjobSubmitRepository.save(vmsjobSubmit);
        return vmsjobSubmitMapper.toDto(vmsjobSubmit);
    }

    @Override
    public Optional<VmsjobSubmitDTO> partialUpdate(VmsjobSubmitDTO vmsjobSubmitDTO) {
        log.debug("Request to partially update VmsjobSubmit : {}", vmsjobSubmitDTO);

        return vmsjobSubmitRepository
            .findById(vmsjobSubmitDTO.getId())
            .map(
                existingVmsjobSubmit -> {
                    vmsjobSubmitMapper.partialUpdate(existingVmsjobSubmit, vmsjobSubmitDTO);

                    return existingVmsjobSubmit;
                }
            )
            .map(vmsjobSubmitRepository::save)
            .map(vmsjobSubmitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VmsjobSubmitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VmsjobSubmits");
        return vmsjobSubmitRepository.findAll(pageable).map(vmsjobSubmitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VmsjobSubmitDTO> findOne(Long id) {
        log.debug("Request to get VmsjobSubmit : {}", id);
        return vmsjobSubmitRepository.findById(id).map(vmsjobSubmitMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VmsjobSubmit : {}", id);
        vmsjobSubmitRepository.deleteById(id);
    }
}
