package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.Worker;
import com.simplify.marketplace.domain.SkillsMaster;
import com.simplify.marketplace.repository.WorkerRepository;
import com.simplify.marketplace.service.WorkerService;
import com.simplify.marketplace.service.dto.WorkerDTO;
import com.simplify.marketplace.domain.VmsjobSave;
import com.simplify.marketplace.service.mapper.WorkerMapper;
import com.simplify.marketplace.domain.VmsjobSubmit;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Worker}.
 */
@Service
@Transactional
public class WorkerServiceImpl implements WorkerService {

    private final Logger log = LoggerFactory.getLogger(WorkerServiceImpl.class);

    private final WorkerRepository workerRepository;

    private final WorkerMapper workerMapper;

    public WorkerServiceImpl(WorkerRepository workerRepository, WorkerMapper workerMapper) {
        this.workerRepository = workerRepository;
        this.workerMapper = workerMapper;
    }

    @Override
    public WorkerDTO save(WorkerDTO workerDTO) {
        log.debug("Request to save Worker : {}", workerDTO);
        Worker worker = workerMapper.toEntity(workerDTO);
        worker = workerRepository.save(worker);
        return workerMapper.toDto(worker);
    }

    @Override
    public Optional<WorkerDTO> partialUpdate(WorkerDTO workerDTO) {
        log.debug("Request to partially update Worker : {}", workerDTO);

        return workerRepository
            .findById(workerDTO.getId())
            .map(
                existingWorker -> {
                    workerMapper.partialUpdate(existingWorker, workerDTO);

                    return existingWorker;
                }
            )
            .map(workerRepository::save)
            .map(workerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Workers");
        return workerRepository.findAll(pageable).map(workerMapper::toDto);
    }

    public Page<WorkerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return workerRepository.findAllWithEagerRelationships(pageable).map(workerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkerDTO> findOne(Long id) {
        log.debug("Request to get Worker : {}", id);
        return workerRepository.findOneWithEagerRelationships(id).map(workerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Worker> findBySkillsId(Long id) {
        log.debug("Request to get Worker : {}", id);
        return workerRepository.findBySkillsId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<VmsjobSubmit> getworkervmsjobsubmits(Long worker_id) {

        Worker worker = workerRepository.findById(worker_id).get();

        return worker.getVmsjobsubmits();

    }

    @Override
    @Transactional(readOnly = true)
    public Set<VmsjobSave> getworkervmsjobSave(Long worker_id){
        Worker worker = workerRepository.findById(worker_id).get();
        return worker.getVmsjobsaves();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<SkillsMaster> getworkerskills(Long worker_id){
        Worker worker = workerRepository.findById(worker_id).get();
        return worker.getSkills();
    }

    @Override
    @Transactional(readOnly = true)
    public Worker findByWorkerId(Long worker_id){
        return workerRepository.findById(worker_id).get();
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Worker : {}", id);
        workerRepository.deleteById(id);
    }
}
