package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.Education;
import com.simplify.marketplace.domain.ElasticWorker;
import com.simplify.marketplace.domain.SubjectMaster;
import com.simplify.marketplace.repository.ESearchWorkerRepository;
import com.simplify.marketplace.repository.EducationRepository;
import com.simplify.marketplace.repository.SubjectMasterRepository;
import com.simplify.marketplace.repository.WorkerRepository;
import com.simplify.marketplace.service.EducationService;
import com.simplify.marketplace.service.dto.EducationDTO;
import com.simplify.marketplace.service.mapper.EducationMapper;
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
 * Service Implementation for managing {@link Education}.
 */
@Service
@Transactional
public class EducationServiceImpl implements EducationService {

    private final Logger log = LoggerFactory.getLogger(EducationServiceImpl.class);

    private final EducationRepository educationRepository;

    private final EducationMapper educationMapper;

    @Autowired
    SubjectMasterRepository sub;

    @Autowired
    WorkerRepository wrepo;

    @Autowired
    ESearchWorkerRepository rep1;

    public EducationServiceImpl(EducationRepository educationRepository, EducationMapper educationMapper) {
        this.educationRepository = educationRepository;
        this.educationMapper = educationMapper;
    }

    @Override
    public EducationDTO save(EducationDTO educationDTO) {
        log.debug("Request to save Education : {}", educationDTO);
        Education education = educationMapper.toEntity(educationDTO);
        education = educationRepository.save(education);
        return educationMapper.toDto(education);
    }

    @Override
    public Optional<EducationDTO> partialUpdate(EducationDTO educationDTO) {
        log.debug("Request to partially update Education : {}", educationDTO);

        return educationRepository
            .findById(educationDTO.getId())
            .map(
                existingEducation -> {
                    educationMapper.partialUpdate(existingEducation, educationDTO);

                    return existingEducation;
                }
            )
            .map(educationRepository::save)
            .map(educationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EducationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Educations");
        return educationRepository.findAll(pageable).map(educationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EducationDTO> findOne(Long id) {
        log.debug("Request to get Education : {}", id);
        return educationRepository.findById(id).map(educationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Education> findOneWorker(Long id) {
        log.debug("Request to get Education : {}", id);
        return educationRepository.findByWorkerId(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Education : {}", id);
        educationRepository.deleteById(id);
    }

    public Set<Education> insertElasticSearch(EducationDTO educationDTO) {
        String Workerid = educationDTO.getWorker().getId().toString();

        ElasticWorker e = rep1.findById(Workerid).get();

        Education ed = new Education();

        ed.setId(educationDTO.getId());
        ed.setDegreeName(educationDTO.getDegreeName());

        ed.setInstitute(educationDTO.getInstitute());

        ed.setYearOfPassing(educationDTO.getYearOfPassing());

        ed.setMarks(educationDTO.getMarks());

        ed.setMarksType(educationDTO.getMarksType());

        ed.setGrade(educationDTO.getGrade());

        ed.setStartDate(educationDTO.getStartDate());

        ed.setEndDate(educationDTO.getEndDate());

        ed.setIsComplete(educationDTO.getIsComplete());

        ed.setDegreeType(educationDTO.getDegreeType());

        ed.setDescription(educationDTO.getDescription());

        SubjectMaster major = sub.findById(educationDTO.getMajorSubject().getId()).get();
        //        major.setId(educationDTO.getMajorSubject().getId());
        //        major.setSubjectName(educationDTO.getMajorSubject().getSubjectName());
        ed.setMajorSubject(major);

        SubjectMaster minor = sub.findById(educationDTO.getMinorSubject().getId()).get();
        //         minor.setId(educationDTO.getMinorSubject().getId());
        //         minor.setSubjectName(educationDTO.getMinorSubject().getSubjectName());
        ed.setMinorSubject(minor);

        ed.setWorker(wrepo.findById(educationDTO.getWorker().getId()).get());

        Set<Education> s = e.getEducations();

        s.add(ed);

        return s;
    }
}
