package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.CustomUser;
import com.simplify.marketplace.repository.CustomUserRepository;
import com.simplify.marketplace.service.CustomUserService;
import com.simplify.marketplace.service.dto.CustomUserDTO;
import com.simplify.marketplace.service.mapper.CustomUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomUser}.
 */
@Service
@Transactional
public class CustomUserServiceImpl implements CustomUserService {

    private final Logger log = LoggerFactory.getLogger(CustomUserServiceImpl.class);

    private final CustomUserRepository customUserRepository;

    private final CustomUserMapper customUserMapper;

    public CustomUserServiceImpl(CustomUserRepository customUserRepository, CustomUserMapper customUserMapper) {
        this.customUserRepository = customUserRepository;
        this.customUserMapper = customUserMapper;
    }

    @Override
    public CustomUserDTO save(CustomUserDTO customUserDTO) {
        log.debug("Request to save CustomUser : {}", customUserDTO);
        CustomUser customUser = customUserMapper.toEntity(customUserDTO);
        customUser = customUserRepository.save(customUser);
        return customUserMapper.toDto(customUser);
    }

    @Override
    public Optional<CustomUserDTO> partialUpdate(CustomUserDTO customUserDTO) {
        log.debug("Request to partially update CustomUser : {}", customUserDTO);

        return customUserRepository
            .findById(customUserDTO.getId())
            .map(
                existingCustomUser -> {
                    customUserMapper.partialUpdate(existingCustomUser, customUserDTO);

                    return existingCustomUser;
                }
            )
            .map(customUserRepository::save)
            .map(customUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomUsers");
        return customUserRepository.findAll(pageable).map(customUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomUserDTO> findOne(Long id) {
        log.debug("Request to get CustomUser : {}", id);
        return customUserRepository.findById(id).map(customUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomUser : {}", id);
        customUserRepository.deleteById(id);
    }
}
