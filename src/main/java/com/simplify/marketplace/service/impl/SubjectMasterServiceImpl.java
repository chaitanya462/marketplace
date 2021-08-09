package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.SubjectMaster;
import com.simplify.marketplace.repository.SubjectMasterRepository;
import com.simplify.marketplace.service.SubjectMasterService;
import com.simplify.marketplace.service.dto.SubjectMasterDTO;
import com.simplify.marketplace.service.mapper.SubjectMasterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SubjectMaster}.
 */
@Service
@Transactional
public class SubjectMasterServiceImpl implements SubjectMasterService {

    private final Logger log = LoggerFactory.getLogger(SubjectMasterServiceImpl.class);

    private final SubjectMasterRepository subjectMasterRepository;

    private final SubjectMasterMapper subjectMasterMapper;

    public SubjectMasterServiceImpl(SubjectMasterRepository subjectMasterRepository, SubjectMasterMapper subjectMasterMapper) {
        this.subjectMasterRepository = subjectMasterRepository;
        this.subjectMasterMapper = subjectMasterMapper;
    }

    @Override
    public SubjectMasterDTO save(SubjectMasterDTO subjectMasterDTO) {
        log.debug("Request to save SubjectMaster : {}", subjectMasterDTO);
        SubjectMaster subjectMaster = subjectMasterMapper.toEntity(subjectMasterDTO);
        subjectMaster = subjectMasterRepository.save(subjectMaster);
        return subjectMasterMapper.toDto(subjectMaster);
    }

    @Override
    public Optional<SubjectMasterDTO> partialUpdate(SubjectMasterDTO subjectMasterDTO) {
        log.debug("Request to partially update SubjectMaster : {}", subjectMasterDTO);

        return subjectMasterRepository
            .findById(subjectMasterDTO.getId())
            .map(
                existingSubjectMaster -> {
                    subjectMasterMapper.partialUpdate(existingSubjectMaster, subjectMasterDTO);

                    return existingSubjectMaster;
                }
            )
            .map(subjectMasterRepository::save)
            .map(subjectMasterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubjectMasterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubjectMasters");
        return subjectMasterRepository.findAll(pageable).map(subjectMasterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubjectMasterDTO> findOne(Long id) {
        log.debug("Request to get SubjectMaster : {}", id);
        return subjectMasterRepository.findById(id).map(subjectMasterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubjectMaster : {}", id);
        subjectMasterRepository.deleteById(id);
    }
}
