package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.LocationPrefrence;
import com.simplify.marketplace.repository.LocationPrefrenceRepository;
import com.simplify.marketplace.service.LocationPrefrenceService;
import com.simplify.marketplace.service.dto.LocationPrefrenceDTO;
import com.simplify.marketplace.service.mapper.LocationPrefrenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LocationPrefrence}.
 */
@Service
@Transactional
public class LocationPrefrenceServiceImpl implements LocationPrefrenceService {

    private final Logger log = LoggerFactory.getLogger(LocationPrefrenceServiceImpl.class);

    private final LocationPrefrenceRepository locationPrefrenceRepository;

    private final LocationPrefrenceMapper locationPrefrenceMapper;

    public LocationPrefrenceServiceImpl(
        LocationPrefrenceRepository locationPrefrenceRepository,
        LocationPrefrenceMapper locationPrefrenceMapper
    ) {
        this.locationPrefrenceRepository = locationPrefrenceRepository;
        this.locationPrefrenceMapper = locationPrefrenceMapper;
    }

    @Override
    public LocationPrefrenceDTO save(LocationPrefrenceDTO locationPrefrenceDTO) {
        log.debug("Request to save LocationPrefrence : {}", locationPrefrenceDTO);
        LocationPrefrence locationPrefrence = locationPrefrenceMapper.toEntity(locationPrefrenceDTO);
        locationPrefrence = locationPrefrenceRepository.save(locationPrefrence);
        return locationPrefrenceMapper.toDto(locationPrefrence);
    }

    @Override
    public Optional<LocationPrefrenceDTO> partialUpdate(LocationPrefrenceDTO locationPrefrenceDTO) {
        log.debug("Request to partially update LocationPrefrence : {}", locationPrefrenceDTO);

        return locationPrefrenceRepository
            .findById(locationPrefrenceDTO.getId())
            .map(
                existingLocationPrefrence -> {
                    locationPrefrenceMapper.partialUpdate(existingLocationPrefrence, locationPrefrenceDTO);

                    return existingLocationPrefrence;
                }
            )
            .map(locationPrefrenceRepository::save)
            .map(locationPrefrenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LocationPrefrenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LocationPrefrences");
        return locationPrefrenceRepository.findAll(pageable).map(locationPrefrenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LocationPrefrenceDTO> findOne(Long id) {
        log.debug("Request to get LocationPrefrence : {}", id);
        return locationPrefrenceRepository.findById(id).map(locationPrefrenceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LocationPrefrence : {}", id);
        locationPrefrenceRepository.deleteById(id);
    }
}
