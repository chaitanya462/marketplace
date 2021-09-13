package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.domain.Refereces;
import com.simplify.marketplace.repository.ESearchWorkerRepository;
import com.simplify.marketplace.repository.ReferecesRepository;
import com.simplify.marketplace.repository.WorkerRepository;
import com.simplify.marketplace.service.ReferecesService;
import com.simplify.marketplace.service.dto.ReferecesDTO;
import com.simplify.marketplace.service.mapper.ReferecesMapper;
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
 * Service Implementation for managing {@link Refereces}.
 */
@Service
@Transactional
public class ReferecesServiceImpl implements ReferecesService {

    private final Logger log = LoggerFactory.getLogger(ReferecesServiceImpl.class);

    private final ReferecesRepository referecesRepository;

    private final ReferecesMapper referecesMapper;

    @Autowired
    WorkerRepository workerRepo;

    @Autowired
    ESearchWorkerRepository elasticWorkerRepo;

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
    @Transactional(readOnly = true)
    public List<Refereces> findOneWorker(Long id) {
        log.debug("Request to get References : {}", id);
        return referecesRepository.findByWorkerId(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Refereces : {}", id);
        referecesRepository.deleteById(id);
    }

    public Set<Refereces> getRefereces(ReferecesDTO refercesDTO) {
        String Workerid = refercesDTO.getWorker().getId().toString();

        ElasticWorker elasticworker = elasticWorkerRepo.findById(Workerid).get();

        Refereces refereces = new Refereces();

        refereces.setId(refercesDTO.getId());
        refereces.setName(refercesDTO.getName());
        refereces.setEmail(refercesDTO.getEmail());
        refereces.setPhone(refercesDTO.getPhone());
        refereces.setProfileLink(refercesDTO.getProfileLink());
        refereces.setRelationType(refercesDTO.getRelationType());
        refereces.setWorker(workerRepo.findById(refercesDTO.getWorker().getId()).get());

        Set<Refereces> set = elasticworker.getRefereces();
        set.add(refereces);

        return set;
    }
}
