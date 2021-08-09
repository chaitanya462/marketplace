package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.Refereces;
import com.simplify.marketplace.repository.ReferecesRepository;
import com.simplify.marketplace.service.ReferecesService;
import com.simplify.marketplace.service.dto.ReferecesDTO;
import com.simplify.marketplace.service.mapper.ReferecesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Refereces}.
 */
@Service
@Transactional
public class ReferecesServiceImpl implements ReferecesService {

    private final Logger log = LoggerFactory.getLogger(ReferecesServiceImpl.class);

    private final ReferecesRepository referecesRepository;

    private final ReferecesMapper referecesMapper;

    public ReferecesServiceImpl(ReferecesRepository referecesRepository, ReferecesMapper referecesMapper) {
        this.referecesRepository = referecesRepository;
        this.referecesMapper = referecesMapper;
    }

    @Override
    public ReferecesDTO save(ReferecesDTO referecesDTO) {
        log.debug("Request to save Refereces : {}", referecesDTO);
        Refereces refereces = referecesMapper.toEntity(referecesDTO);
        refereces = referecesRepository.save(refereces);
        return referecesMapper.toDto(refereces);
    }

    @Override
    public Optional<ReferecesDTO> partialUpdate(ReferecesDTO referecesDTO) {
        log.debug("Request to partially update Refereces : {}", referecesDTO);

        return referecesRepository
            .findById(referecesDTO.getId())
            .map(
                existingRefereces -> {
                    referecesMapper.partialUpdate(existingRefereces, referecesDTO);

                    return existingRefereces;
                }
            )
            .map(referecesRepository::save)
            .map(referecesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReferecesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Refereces");
        return referecesRepository.findAll(pageable).map(referecesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReferecesDTO> findOne(Long id) {
        log.debug("Request to get Refereces : {}", id);
        return referecesRepository.findById(id).map(referecesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Refereces : {}", id);
        referecesRepository.deleteById(id);
    }
}
