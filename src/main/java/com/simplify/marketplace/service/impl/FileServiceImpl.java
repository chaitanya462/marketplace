package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.ElasticWorker;
import com.simplify.marketplace.domain.File;
import com.simplify.marketplace.repository.ESearchWorkerRepository;
import com.simplify.marketplace.repository.FileRepository;
import com.simplify.marketplace.repository.WorkerRepository;
import com.simplify.marketplace.service.FileService;
import com.simplify.marketplace.service.dto.FileDTO;
import com.simplify.marketplace.service.mapper.FileMapper;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link File}.
 */
@Service
@Transactional
public class FileServiceImpl implements FileService {

    private final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    private final FileRepository fileRepository;

    private final FileMapper fileMapper;

    @Autowired
    ESearchWorkerRepository rep1;

    @Autowired
    WorkerRepository wrepo;

    public FileServiceImpl(FileRepository fileRepository, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    @Override
    public FileDTO save(FileDTO fileDTO) {
        log.debug("Request to save File : {}", fileDTO);
        File file = fileMapper.toEntity(fileDTO);
        file = fileRepository.save(file);
        return fileMapper.toDto(file);
    }

    @Override
    public Optional<FileDTO> partialUpdate(FileDTO fileDTO) {
        log.debug("Request to partially update File : {}", fileDTO);

        return fileRepository
            .findById(fileDTO.getId())
            .map(
                existingFile -> {
                    fileMapper.partialUpdate(existingFile, fileDTO);

                    return existingFile;
                }
            )
            .map(fileRepository::save)
            .map(fileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Files");
        return fileRepository.findAll(pageable).map(fileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileDTO> findOne(Long id) {
        log.debug("Request to get File : {}", id);
        return fileRepository.findById(id).map(fileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<File> findOneWorker(Long id) {
        log.debug("Request to get Certificate : {}", id);
        return fileRepository.findByWorkerId(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete File : {}", id);
        fileRepository.deleteById(id);
    }

    public Set<File> insertElasticSearch(FileDTO fileDTO) {
        File file = new File();

        String Workerid = fileDTO.getWorker().getId().toString();

        ElasticWorker e = rep1.findById(Workerid).get();

        file.setId(fileDTO.getId());
        file.setFileformat(fileDTO.getFileformat());
        file.setFiletype(fileDTO.getFiletype());
        file.setIsDefault(fileDTO.getIsDefault());
        file.setIsProfilePic(fileDTO.getIsProfilePic());
        file.setIsResume(fileDTO.getIsResume());
        file.setPath(fileDTO.getPath());
        file.setTag(fileDTO.getTag());
        file.setWorker(wrepo.findById(fileDTO.getWorker().getId()).get());

        //        file.setCreatedAt(fileDTO.getCreatedAt());
        //        file.setCreatedBy(fileDTO.getCreatedBy());
        //        file.setUpdatedAt(fileDTO.getUpdatedAt());
        //        file.setUpdatedBy(fileDTO.getUpdatedBy());
        //
        //        System.out.println("\n\n\n\n\n\n\n\n" + fileDTO.getCreatedAt()+ "\n\n\n\n\n\n\n\n");

        Set<File> filesSet = e.getFiles();

        filesSet.add(file);

        return filesSet;
    }
}
