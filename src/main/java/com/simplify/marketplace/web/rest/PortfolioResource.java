package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.repository.ESearchWorkerRepository;
import com.simplify.marketplace.repository.PortfolioRepository;
import com.simplify.marketplace.repository.WorkerRepository;
import com.simplify.marketplace.service.PortfolioService;
import com.simplify.marketplace.service.UserService;
import com.simplify.marketplace.service.dto.PortfolioDTO;
import com.simplify.marketplace.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.simplify.marketplace.domain.Portfolio}.
 */
@RestController
@RequestMapping("/api")
public class PortfolioResource {

    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(PortfolioResource.class);

    private static final String ENTITY_NAME = "portfolio";

    @Autowired
    ESearchWorkerRepository rep1;

    @Autowired
    RabbitTemplate rabbit_msg;

    @Autowired
    WorkerRepository wrepo;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PortfolioService portfolioService;

    private final PortfolioRepository portfolioRepository;

    public PortfolioResource(PortfolioService portfolioService, PortfolioRepository portfolioRepository, UserService userService) {
        this.portfolioService = portfolioService;
        this.portfolioRepository = portfolioRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /portfolios} : Create a new portfolio.
     *
     * @param portfolioDTO the portfolioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new portfolioDTO, or with status {@code 400 (Bad Request)} if the portfolio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/portfolios")
    public ResponseEntity<PortfolioDTO> createPortfolio(@RequestBody PortfolioDTO portfolioDTO) throws URISyntaxException {
        log.debug("REST request to save Portfolio : {}", portfolioDTO);
        if (portfolioDTO.getId() != null) {
            throw new BadRequestAlertException("A new portfolio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        portfolioDTO.setCreatedBy(userService.getUserWithAuthorities().get().getId() + "");
        portfolioDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        portfolioDTO.setUpdatedAt(LocalDate.now());
        portfolioDTO.setCreatedAt(LocalDate.now());
        PortfolioDTO result = portfolioService.save(portfolioDTO);

        Portfolio portfolio = portfolioRepository.findById(result.getId()).get();
        if (portfolio.getWorker() != null) {
            Long workerid = portfolio.getWorker().getId();
            ElasticWorker elasticworker = rep1.findById(workerid.toString()).get();
            elasticworker.addPortfolio(portfolio);

            rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticworker);
        }
        return ResponseEntity
            .created(new URI("/api/portfolios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /portfolios/:id} : Updates an existing portfolio.
     *
     * @param id the id of the portfolioDTO to save.
     * @param portfolioDTO the portfolioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portfolioDTO,
     * or with status {@code 400 (Bad Request)} if the portfolioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the portfolioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/portfolios/{id}")
    public ResponseEntity<PortfolioDTO> updatePortfolio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PortfolioDTO portfolioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Portfolio : {}, {}", id, portfolioDTO);
        if (portfolioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, portfolioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!portfolioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        portfolioDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        portfolioDTO.setUpdatedAt(LocalDate.now());
        Portfolio prevPortfolio = portfolioRepository.findById(portfolioDTO.getId()).get();

        PortfolioDTO result = portfolioService.save(portfolioDTO);
        Portfolio updatedPortfolio = portfolioRepository.findById(result.getId()).get();

        if (prevPortfolio.getWorker() != null && !Objects.equals(updatedPortfolio.getWorker().getId(), prevPortfolio.getWorker().getId())) {
            ElasticWorker prevElasticWorker = rep1.findById(prevPortfolio.getWorker().getId().toString()).get();
            prevPortfolio.setWorker(null);
            prevElasticWorker = prevElasticWorker.removePortfolio(prevPortfolio);
            rabbit_msg.convertAndSend("topicExchange1", "routingKey", prevElasticWorker);
        }

        prevPortfolio.setWorker(null);

        ElasticWorker elasticworker = rep1.findById(updatedPortfolio.getWorker().getId().toString()).get();
        elasticworker.removePortfolio(prevPortfolio);
        elasticworker.addPortfolio(updatedPortfolio);
        rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticworker);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, portfolioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /portfolios/:id} : Partial updates given fields of an existing portfolio, field will ignore if it is null
     *
     * @param id the id of the portfolioDTO to save.
     * @param portfolioDTO the portfolioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portfolioDTO,
     * or with status {@code 400 (Bad Request)} if the portfolioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the portfolioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the portfolioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/portfolios/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PortfolioDTO> partialUpdatePortfolio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PortfolioDTO portfolioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Portfolio partially : {}, {}", id, portfolioDTO);
        if (portfolioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, portfolioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!portfolioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        portfolioDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        portfolioDTO.setUpdatedAt(LocalDate.now());

        Optional<PortfolioDTO> result = portfolioService.partialUpdate(portfolioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, portfolioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /portfolios} : get all the portfolios.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of portfolios in body.
     */
    @GetMapping("/portfolios")
    public ResponseEntity<List<PortfolioDTO>> getAllPortfolios(Pageable pageable) {
        log.debug("REST request to get a page of Portfolios");
        Page<PortfolioDTO> page = portfolioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/portfolios/worker/{workerid}")
    public List<Portfolio> getworkerPortfolio(@PathVariable Long workerid) {
        log.debug("REST request to get Portfolio : {}", workerid);
        List<Portfolio> portfolios = portfolioService.findOneWorker(workerid);
        return portfolios;
    }

    /**
     * {@code GET  /portfolios/:id} : get the "id" portfolio.
     *
     * @param id the id of the portfolioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the portfolioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portfolios/{id}")
    public ResponseEntity<PortfolioDTO> getPortfolio(@PathVariable Long id) {
        log.debug("REST request to get Portfolio : {}", id);
        Optional<PortfolioDTO> portfolioDTO = portfolioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(portfolioDTO);
    }

    /**
     * {@code DELETE  /portfolios/:id} : delete the "id" portfolio.
     *
     * @param id the id of the portfolioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/portfolios/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id) {
        log.debug("REST request to delete Portfolio : {}", id);
        Portfolio portfolio = portfolioRepository.findById(id).get();

        portfolioService.delete(id);
        Long curr_id = portfolio.getWorker().getId();
        ElasticWorker elasicWorker = rep1.findById(curr_id.toString()).get();
        portfolio.setWorker(null);

        elasicWorker.removePortfolio(portfolio);

        rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasicWorker);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
