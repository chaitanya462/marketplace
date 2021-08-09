package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.UserPhone;
import com.simplify.marketplace.repository.UserPhoneRepository;
import com.simplify.marketplace.service.UserPhoneService;
import com.simplify.marketplace.service.dto.UserPhoneDTO;
import com.simplify.marketplace.service.mapper.UserPhoneMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserPhone}.
 */
@Service
@Transactional
public class UserPhoneServiceImpl implements UserPhoneService {

    private final Logger log = LoggerFactory.getLogger(UserPhoneServiceImpl.class);

    private final UserPhoneRepository userPhoneRepository;

    private final UserPhoneMapper userPhoneMapper;

    public UserPhoneServiceImpl(UserPhoneRepository userPhoneRepository, UserPhoneMapper userPhoneMapper) {
        this.userPhoneRepository = userPhoneRepository;
        this.userPhoneMapper = userPhoneMapper;
    }

    @Override
    public UserPhoneDTO save(UserPhoneDTO userPhoneDTO) {
        log.debug("Request to save UserPhone : {}", userPhoneDTO);
        UserPhone userPhone = userPhoneMapper.toEntity(userPhoneDTO);
        userPhone = userPhoneRepository.save(userPhone);
        return userPhoneMapper.toDto(userPhone);
    }

    @Override
    public Optional<UserPhoneDTO> partialUpdate(UserPhoneDTO userPhoneDTO) {
        log.debug("Request to partially update UserPhone : {}", userPhoneDTO);

        return userPhoneRepository
            .findById(userPhoneDTO.getId())
            .map(
                existingUserPhone -> {
                    userPhoneMapper.partialUpdate(existingUserPhone, userPhoneDTO);

                    return existingUserPhone;
                }
            )
            .map(userPhoneRepository::save)
            .map(userPhoneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserPhoneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserPhones");
        return userPhoneRepository.findAll(pageable).map(userPhoneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserPhoneDTO> findOne(Long id) {
        log.debug("Request to get UserPhone : {}", id);
        return userPhoneRepository.findById(id).map(userPhoneMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserPhone : {}", id);
        userPhoneRepository.deleteById(id);
    }
}
