package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.domain.JobPreference;
import com.simplify.marketplace.repository.CategoryRepository;
import com.simplify.marketplace.repository.ESearchWorkerRepository;
import com.simplify.marketplace.repository.FieldValueRepository;
import com.simplify.marketplace.repository.JobPreferenceRepository;
import com.simplify.marketplace.repository.LocationPrefrenceRepository;
import com.simplify.marketplace.repository.WorkerRepository;
import com.simplify.marketplace.service.JobPreferenceService;
import com.simplify.marketplace.service.dto.JobPreferenceDTO;
import com.simplify.marketplace.service.mapper.JobPreferenceMapper;
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
 * Service Implementation for managing {@link JobPreference}.
 */
@Service
@Transactional
public class JobPreferenceServiceImpl implements JobPreferenceService {

    private final Logger log = LoggerFactory.getLogger(JobPreferenceServiceImpl.class);

    private final JobPreferenceRepository jobPreferenceRepository;

    private final JobPreferenceMapper jobPreferenceMapper;

    @Autowired
    WorkerRepository workerRepo;

    @Autowired
    ESearchWorkerRepository elasticWorkerRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    FieldValueRepository fieldValueRepo;

    @Autowired
    LocationPrefrenceRepository locPrefRepo;

    public JobPreferenceServiceImpl(JobPreferenceRepository jobPreferenceRepository, JobPreferenceMapper jobPreferenceMapper) {
        this.jobPreferenceRepository = jobPreferenceRepository;
        this.jobPreferenceMapper = jobPreferenceMapper;
    }

    @Override
    public JobPreferenceDTO save(JobPreferenceDTO jobPreferenceDTO) {
        log.debug("Request to save JobPreference : {}", jobPreferenceDTO);
        JobPreference jobPreference = jobPreferenceMapper.toEntity(jobPreferenceDTO);
        jobPreference = jobPreferenceRepository.save(jobPreference);
        return jobPreferenceMapper.toDto(jobPreference);
    }

    @Override
    public Optional<JobPreferenceDTO> partialUpdate(JobPreferenceDTO jobPreferenceDTO) {
        log.debug("Request to partially update JobPreference : {}", jobPreferenceDTO);

        return jobPreferenceRepository
            .findById(jobPreferenceDTO.getId())
            .map(
                existingJobPreference -> {
                    jobPreferenceMapper.partialUpdate(existingJobPreference, jobPreferenceDTO);

                    return existingJobPreference;
                }
            )
            .map(jobPreferenceRepository::save)
            .map(jobPreferenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobPreferenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JobPreferences");
        return jobPreferenceRepository.findAll(pageable).map(jobPreferenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JobPreferenceDTO> findOne(Long id) {
        log.debug("Request to get JobPreference : {}", id);
        return jobPreferenceRepository.findById(id).map(jobPreferenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobPreference> findOneWorker(Long id) {
        log.debug("Request to get JobPreference : {}", id);
        return jobPreferenceRepository.findByWorkerId(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobPreference : {}", id);
        jobPreferenceRepository.deleteById(id);
    }

    public Set<JobPreference> getJobPreferences(JobPreferenceDTO jobPreferenceDTO) {
        String Workerid = jobPreferenceDTO.getWorker().getId().toString();
        ElasticWorker elasticworker = elasticWorkerRepo.findById(Workerid).get();

        JobPreference jobPreference = new JobPreference();

        jobPreference.setId(jobPreferenceDTO.getId());
        jobPreference.setAvailableFrom(jobPreferenceDTO.getAvailableFrom());
        jobPreference.setAvailableTo(jobPreferenceDTO.getAvailableTo());
        jobPreference.setDailyRate(jobPreferenceDTO.getDailyRate());
        jobPreference.setEmploymentType(jobPreferenceDTO.getEmploymentType());
        jobPreference.setEngagementType(jobPreferenceDTO.getEngagementType());
        jobPreference.setHourlyRate(jobPreferenceDTO.getHourlyRate());
        jobPreference.setHourPerDay(jobPreferenceDTO.getHourPerDay());
        jobPreference.setHourPerWeek(jobPreferenceDTO.getHourPerWeek());
        jobPreference.setIsActive(jobPreferenceDTO.getIsActive());
        jobPreference.setLocationType(jobPreferenceDTO.getLocationType());
        jobPreference.setMonthlyRate(jobPreferenceDTO.getMonthlyRate());

        //jobPreference.setFieldValues(fieldValueRepo.findById(jobPreferenceDTO.getF));

        jobPreference.setSubCategory(categoryRepo.findById(jobPreferenceDTO.getSubCategory().getId()).get());

        jobPreference.setLocationPrefrences(locPrefRepo.findByJobPreferenceId(jobPreferenceDTO.getId()));

        jobPreference.setFieldValues(null);

        jobPreference.setWorker(workerRepo.findById(jobPreferenceDTO.getWorker().getId()).get());

        Set<JobPreference> set = elasticworker.getJobPreferences();
        set.add(jobPreference);

        return set;
    }
}
