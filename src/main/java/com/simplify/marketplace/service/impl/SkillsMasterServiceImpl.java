package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.SkillsMaster;
import com.simplify.marketplace.repository.SkillsMasterRepository;
import com.simplify.marketplace.service.SkillsMasterService;
import com.simplify.marketplace.service.dto.SkillsMasterDTO;
import com.simplify.marketplace.service.mapper.SkillsMasterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SkillsMaster}.
 */
@Service
@Transactional
public class SkillsMasterServiceImpl implements SkillsMasterService {

    private final Logger log = LoggerFactory.getLogger(SkillsMasterServiceImpl.class);

    private final SkillsMasterRepository skillsMasterRepository;

    private final SkillsMasterMapper skillsMasterMapper;

    public SkillsMasterServiceImpl(SkillsMasterRepository skillsMasterRepository, SkillsMasterMapper skillsMasterMapper) {
        this.skillsMasterRepository = skillsMasterRepository;
        this.skillsMasterMapper = skillsMasterMapper;
    }

    @Override
    public SkillsMasterDTO save(SkillsMasterDTO skillsMasterDTO) {
        log.debug("Request to save SkillsMaster : {}", skillsMasterDTO);
        SkillsMaster skillsMaster = skillsMasterMapper.toEntity(skillsMasterDTO);
        skillsMaster = skillsMasterRepository.save(skillsMaster);
        return skillsMasterMapper.toDto(skillsMaster);
    }

    @Override
    public Optional<SkillsMasterDTO> partialUpdate(SkillsMasterDTO skillsMasterDTO) {
        log.debug("Request to partially update SkillsMaster : {}", skillsMasterDTO);

        return skillsMasterRepository
            .findById(skillsMasterDTO.getId())
            .map(
                existingSkillsMaster -> {
                    skillsMasterMapper.partialUpdate(existingSkillsMaster, skillsMasterDTO);

                    return existingSkillsMaster;
                }
            )
            .map(skillsMasterRepository::save)
            .map(skillsMasterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SkillsMasterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SkillsMasters");
        return skillsMasterRepository.findAll(pageable).map(skillsMasterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SkillsMasterDTO> findOne(Long id) {
        log.debug("Request to get SkillsMaster : {}", id);
        return skillsMasterRepository.findById(id).map(skillsMasterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SkillsMaster : {}", id);
        skillsMasterRepository.deleteById(id);
    }
}
