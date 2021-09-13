package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.ElasticWorker;
import com.simplify.marketplace.domain.Portfolio;
import com.simplify.marketplace.repository.ESearchWorkerRepository;
import com.simplify.marketplace.repository.PortfolioRepository;
import com.simplify.marketplace.repository.WorkerRepository;
import com.simplify.marketplace.service.PortfolioService;
import com.simplify.marketplace.service.dto.PortfolioDTO;
import com.simplify.marketplace.service.mapper.PortfolioMapper;
import java.util.*;
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
 * Service Implementation for managing {@link Portfolio}.
 */
@Service
@Transactional
public class PortfolioServiceImpl implements PortfolioService {

    private final Logger log = LoggerFactory.getLogger(PortfolioServiceImpl.class);

    private final PortfolioRepository portfolioRepository;

    private final PortfolioMapper portfolioMapper;

    @Autowired
    WorkerRepository workerRepo;

    @Autowired
    ESearchWorkerRepository elasticWorkerRepo;

    public PortfolioServiceImpl(PortfolioRepository portfolioRepository, PortfolioMapper portfolioMapper) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioMapper = portfolioMapper;
    }

    @Override
    public PortfolioDTO save(PortfolioDTO portfolioDTO) {
        log.debug("Request to save Portfolio : {}", portfolioDTO);
        Portfolio portfolio = portfolioMapper.toEntity(portfolioDTO);
        portfolio = portfolioRepository.save(portfolio);
        return portfolioMapper.toDto(portfolio);
    }

    @Override
    public Optional<PortfolioDTO> partialUpdate(PortfolioDTO portfolioDTO) {
        log.debug("Request to partially update Portfolio : {}", portfolioDTO);

        return portfolioRepository
            .findById(portfolioDTO.getId())
            .map(
                existingPortfolio -> {
                    portfolioMapper.partialUpdate(existingPortfolio, portfolioDTO);

                    return existingPortfolio;
                }
            )
            .map(portfolioRepository::save)
            .map(portfolioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PortfolioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Portfolios");
        return portfolioRepository.findAll(pageable).map(portfolioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PortfolioDTO> findOne(Long id) {
        log.debug("Request to get Portfolio : {}", id);
        return portfolioRepository.findById(id).map(portfolioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Portfolio> findOneWorker(Long id) {
        log.debug("Request to get Portfolio : {}", id);
        return portfolioRepository.findByWorkerId(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Portfolio : {}", id);
        portfolioRepository.deleteById(id);
    }

    public Set<Portfolio> getPortfolios(PortfolioDTO portfolioDTO) {
        String Workerid = portfolioDTO.getWorker().getId().toString();
        ElasticWorker elasticworker = elasticWorkerRepo.findById(Workerid).get();

        Portfolio portfolio = new Portfolio();

        portfolio.setId(portfolioDTO.getId());
        portfolio.setPortfolioURL(portfolioDTO.getPortfolioURL());
        portfolio.setType(portfolioDTO.getType());

        portfolio.setWorker(workerRepo.findById(portfolioDTO.getWorker().getId()).get());

        Set<Portfolio> set = elasticworker.getPortfolios();
        set.add(portfolio);

        return set;
    }
}
