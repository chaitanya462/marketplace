package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.Photo;
import com.simplify.marketplace.repository.PhotoRepository;
import com.simplify.marketplace.service.PhotoService;
import com.simplify.marketplace.service.dto.PhotoDTO;
import com.simplify.marketplace.service.mapper.PhotoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Photo}.
 */
@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {

    private final Logger log = LoggerFactory.getLogger(PhotoServiceImpl.class);

    private final PhotoRepository photoRepository;

    private final PhotoMapper photoMapper;

    public PhotoServiceImpl(PhotoRepository photoRepository, PhotoMapper photoMapper) {
        this.photoRepository = photoRepository;
        this.photoMapper = photoMapper;
    }

    @Override
    public PhotoDTO save(PhotoDTO photoDTO) {
        log.debug("Request to save Photo : {}", photoDTO);
        Photo photo = photoMapper.toEntity(photoDTO);
        photo = photoRepository.save(photo);
        return photoMapper.toDto(photo);
    }

    @Override
    public Optional<PhotoDTO> partialUpdate(PhotoDTO photoDTO) {
        log.debug("Request to partially update Photo : {}", photoDTO);

        return photoRepository
            .findById(photoDTO.getId())
            .map(
                existingPhoto -> {
                    photoMapper.partialUpdate(existingPhoto, photoDTO);

                    return existingPhoto;
                }
            )
            .map(photoRepository::save)
            .map(photoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PhotoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Photos");
        return photoRepository.findAll(pageable).map(photoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PhotoDTO> findOne(Long id) {
        log.debug("Request to get Photo : {}", id);
        return photoRepository.findById(id).map(photoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Photo : {}", id);
        photoRepository.deleteById(id);
    }
}
