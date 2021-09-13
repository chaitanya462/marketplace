package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.UserEmail;
import com.simplify.marketplace.repository.UserEmailRepository;
import com.simplify.marketplace.service.UserEmailService;
import com.simplify.marketplace.service.dto.UserEmailDTO;
import com.simplify.marketplace.service.mapper.UserEmailMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserEmail}.
 */
@Service
@Transactional
public class UserEmailServiceImpl implements UserEmailService {

    private final Logger log = LoggerFactory.getLogger(UserEmailServiceImpl.class);

    private final UserEmailRepository userEmailRepository;

    private final UserEmailMapper userEmailMapper;

    public UserEmailServiceImpl(UserEmailRepository userEmailRepository, UserEmailMapper userEmailMapper) {
        this.userEmailRepository = userEmailRepository;
        this.userEmailMapper = userEmailMapper;
    }

    @Override
    public UserEmailDTO save(UserEmailDTO userEmailDTO) {
        log.debug("Request to save UserEmail : {}", userEmailDTO);
        UserEmail userEmail = userEmailMapper.toEntity(userEmailDTO);
        userEmail = userEmailRepository.save(userEmail);
        return userEmailMapper.toDto(userEmail);
    }

    @Override
    public Optional<UserEmailDTO> partialUpdate(UserEmailDTO userEmailDTO) {
        log.debug("Request to partially update UserEmail : {}", userEmailDTO);

        return userEmailRepository
            .findById(userEmailDTO.getId())
            .map(
                existingUserEmail -> {
                    userEmailMapper.partialUpdate(existingUserEmail, userEmailDTO);

                    return existingUserEmail;
                }
            )
            .map(userEmailRepository::save)
            .map(userEmailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserEmailDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserEmails");
        return userEmailRepository.findAll(pageable).map(userEmailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserEmailDTO> findOne(Long id) {
        log.debug("Request to get UserEmail : {}", id);
        return userEmailRepository.findById(id).map(userEmailMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserEmail : {}", id);
        userEmailRepository.deleteById(id);
    }
}
