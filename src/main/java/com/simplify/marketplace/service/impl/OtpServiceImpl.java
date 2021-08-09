package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.Otp;
import com.simplify.marketplace.repository.OtpRepository;
import com.simplify.marketplace.service.OtpService;
import com.simplify.marketplace.service.dto.OtpDTO;
import com.simplify.marketplace.service.mapper.OtpMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Otp}.
 */
@Service
@Transactional
public class OtpServiceImpl implements OtpService {

    private final Logger log = LoggerFactory.getLogger(OtpServiceImpl.class);

    private final OtpRepository otpRepository;

    private final OtpMapper otpMapper;

    public OtpServiceImpl(OtpRepository otpRepository, OtpMapper otpMapper) {
        this.otpRepository = otpRepository;
        this.otpMapper = otpMapper;
    }

    @Override
    public OtpDTO save(OtpDTO otpDTO) {
        log.debug("Request to save Otp : {}", otpDTO);
        Otp otp = otpMapper.toEntity(otpDTO);
        otp = otpRepository.save(otp);
        return otpMapper.toDto(otp);
    }

    @Override
    public Optional<OtpDTO> partialUpdate(OtpDTO otpDTO) {
        log.debug("Request to partially update Otp : {}", otpDTO);

        return otpRepository
            .findById(otpDTO.getId())
            .map(
                existingOtp -> {
                    otpMapper.partialUpdate(existingOtp, otpDTO);

                    return existingOtp;
                }
            )
            .map(otpRepository::save)
            .map(otpMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OtpDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Otps");
        return otpRepository.findAll(pageable).map(otpMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OtpDTO> findOne(Long id) {
        log.debug("Request to get Otp : {}", id);
        return otpRepository.findById(id).map(otpMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Otp : {}", id);
        otpRepository.deleteById(id);
    }
}
