package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.FieldValue;
import com.simplify.marketplace.repository.FieldValueRepository;
import com.simplify.marketplace.service.FieldValueService;
import com.simplify.marketplace.service.dto.FieldValueDTO;
import com.simplify.marketplace.service.mapper.FieldValueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FieldValue}.
 */
@Service
@Transactional
public class FieldValueServiceImpl implements FieldValueService {

    private final Logger log = LoggerFactory.getLogger(FieldValueServiceImpl.class);

    private final FieldValueRepository fieldValueRepository;

    private final FieldValueMapper fieldValueMapper;

    public FieldValueServiceImpl(FieldValueRepository fieldValueRepository, FieldValueMapper fieldValueMapper) {
        this.fieldValueRepository = fieldValueRepository;
        this.fieldValueMapper = fieldValueMapper;
    }

    @Override
    public FieldValueDTO save(FieldValueDTO fieldValueDTO) {
        log.debug("Request to save FieldValue : {}", fieldValueDTO);
        FieldValue fieldValue = fieldValueMapper.toEntity(fieldValueDTO);
        fieldValue = fieldValueRepository.save(fieldValue);
        return fieldValueMapper.toDto(fieldValue);
    }

    @Override
    public Optional<FieldValueDTO> partialUpdate(FieldValueDTO fieldValueDTO) {
        log.debug("Request to partially update FieldValue : {}", fieldValueDTO);

        return fieldValueRepository
            .findById(fieldValueDTO.getId())
            .map(
                existingFieldValue -> {
                    fieldValueMapper.partialUpdate(existingFieldValue, fieldValueDTO);

                    return existingFieldValue;
                }
            )
            .map(fieldValueRepository::save)
            .map(fieldValueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FieldValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FieldValues");
        return fieldValueRepository.findAll(pageable).map(fieldValueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FieldValueDTO> findOne(Long id) {
        log.debug("Request to get FieldValue : {}", id);
        return fieldValueRepository.findById(id).map(fieldValueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FieldValue : {}", id);
        fieldValueRepository.deleteById(id);
    }
}
