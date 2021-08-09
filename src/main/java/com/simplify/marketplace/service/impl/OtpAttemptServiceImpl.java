package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.OtpAttempt;
import com.simplify.marketplace.repository.OtpAttemptRepository;
import com.simplify.marketplace.service.OtpAttemptService;
import com.simplify.marketplace.service.dto.OtpAttemptDTO;
import com.simplify.marketplace.service.mapper.OtpAttemptMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OtpAttempt}.
 */
@Service
@Transactional
public class OtpAttemptServiceImpl implements OtpAttemptService {

    private final Logger log = LoggerFactory.getLogger(OtpAttemptServiceImpl.class);

    private final OtpAttemptRepository otpAttemptRepository;

    private final OtpAttemptMapper otpAttemptMapper;

    public OtpAttemptServiceImpl(OtpAttemptRepository otpAttemptRepository, OtpAttemptMapper otpAttemptMapper) {
        this.otpAttemptRepository = otpAttemptRepository;
        this.otpAttemptMapper = otpAttemptMapper;
    }

    @Override
    public OtpAttemptDTO save(OtpAttemptDTO otpAttemptDTO) {
        log.debug("Request to save OtpAttempt : {}", otpAttemptDTO);
        OtpAttempt otpAttempt = otpAttemptMapper.toEntity(otpAttemptDTO);
        otpAttempt = otpAttemptRepository.save(otpAttempt);
        return otpAttemptMapper.toDto(otpAttempt);
    }

    @Override
    public Optional<OtpAttemptDTO> partialUpdate(OtpAttemptDTO otpAttemptDTO) {
        log.debug("Request to partially update OtpAttempt : {}", otpAttemptDTO);

        return otpAttemptRepository
            .findById(otpAttemptDTO.getId())
            .map(
                existingOtpAttempt -> {
                    otpAttemptMapper.partialUpdate(existingOtpAttempt, otpAttemptDTO);

                    return existingOtpAttempt;
                }
            )
            .map(otpAttemptRepository::save)
            .map(otpAttemptMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OtpAttemptDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OtpAttempts");
        return otpAttemptRepository.findAll(pageable).map(otpAttemptMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OtpAttemptDTO> findOne(Long id) {
        log.debug("Request to get OtpAttempt : {}", id);
        return otpAttemptRepository.findById(id).map(otpAttemptMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OtpAttempt : {}", id);
        otpAttemptRepository.deleteById(id);
    }
}
