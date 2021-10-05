package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.repository.ESearchWorkerRepository;
import com.simplify.marketplace.repository.EmploymentRepository;
import com.simplify.marketplace.repository.EmploymentSuggestionRepository;
import com.simplify.marketplace.repository.WorkerRepository;
import com.simplify.marketplace.service.EmploymentService;
import com.simplify.marketplace.service.UserService;
import com.simplify.marketplace.service.dto.EmploymentDTO;
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
 * REST controller for managing {@link com.simplify.marketplace.domain.Employment}.
 */
@RestController
@RequestMapping("/api")
public class EmploymentResource {

    private UserService userService;

    @Autowired
    ESearchWorkerRepository rep1;

    @Autowired
    RabbitTemplate rabbit_msg;

    @Autowired
    EmploymentSuggestionRepository employmentSuggetionrepo;

    @Autowired
    WorkerRepository wrepo;

    private final Logger log = LoggerFactory.getLogger(EmploymentResource.class);

    private static final String ENTITY_NAME = "employment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmploymentService employmentService;

    private final EmploymentRepository employmentRepository;

    public EmploymentResource(EmploymentService employmentService, EmploymentRepository employmentRepository, UserService userService) {
        this.employmentService = employmentService;
        this.employmentRepository = employmentRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /employments} : Create a new employment.
     *
     * @param employmentDTO the employmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employmentDTO, or with status {@code 400 (Bad Request)} if the employment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employments")
    public ResponseEntity<EmploymentDTO> createEmployment(@RequestBody EmploymentDTO employmentDTO) throws URISyntaxException {
        log.debug("REST request to save Employment : {}", employmentDTO);
        if (employmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new employment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employmentDTO.setCreatedBy(userService.getUserWithAuthorities().get().getId() + "");
        employmentDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        employmentDTO.setUpdatedAt(LocalDate.now());
        employmentDTO.setCreatedAt(LocalDate.now());

        EmploymentDTO result = employmentService.save(employmentDTO);

        Employment employment = employmentService.getEmploymentById(result.getId());

        if (employment.getWorker() != null) {
            Long workerid = employment.getWorker().getId();
            ElasticWorker elasticworker = rep1.findById(workerid.toString()).get();
            elasticworker.addEmployment(employment);
            EmploymentSuggestIndexDomain employmentsug = new EmploymentSuggestIndexDomain();
            employmentsug.setName(employmentDTO.getJobTitle());

            employmentSuggetionrepo.save(employmentsug);

            rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticworker);
        }

        return ResponseEntity
            .created(new URI("/api/employments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employments/:id} : Updates an existing employment.
     *
     * @param id the id of the employmentDTO to save.
     * @param employmentDTO the employmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employmentDTO,
     * or with status {@code 400 (Bad Request)} if the employmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employments/{id}")
    public ResponseEntity<EmploymentDTO> updateEmployment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmploymentDTO employmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Employment : {}, {}", id, employmentDTO);
        if (employmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        employmentDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        employmentDTO.setUpdatedAt(LocalDate.now());

        Employment prevEmployment = employmentRepository.findById(employmentDTO.getId()).get();

        EmploymentDTO result = employmentService.save(employmentDTO);
        Employment updatedEmployment = employmentRepository.findById(result.getId()).get();

        if (
            prevEmployment.getWorker() != null && !Objects.equals(updatedEmployment.getWorker().getId(), prevEmployment.getWorker().getId())
        ) {
            ElasticWorker prevElasticWorker = rep1.findById(prevEmployment.getWorker().getId().toString()).get();
            prevEmployment.setWorker(null);
            prevElasticWorker = prevElasticWorker.removeEmployment(prevEmployment);
            rabbit_msg.convertAndSend("topicExchange1", "routingKey", prevElasticWorker);
        }

        prevEmployment.setWorker(null);

        ElasticWorker elasticworker = rep1.findById(updatedEmployment.getWorker().getId().toString()).get();
        elasticworker.removeEmployment(prevEmployment);
        elasticworker.addEmployment(updatedEmployment);
        rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticworker);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /employments/:id} : Partial updates given fields of an existing employment, field will ignore if it is null
     *
     * @param id the id of the employmentDTO to save.
     * @param employmentDTO the employmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employmentDTO,
     * or with status {@code 400 (Bad Request)} if the employmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/employments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EmploymentDTO> partialUpdateEmployment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmploymentDTO employmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Employment partially : {}, {}", id, employmentDTO);
        if (employmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        employmentDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        employmentDTO.setUpdatedAt(LocalDate.now());

        Optional<EmploymentDTO> result = employmentService.partialUpdate(employmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /employments} : get all the employments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employments in body.
     */
    @GetMapping("/employments")
    public ResponseEntity<List<EmploymentDTO>> getAllEmployments(Pageable pageable) {
        log.debug("REST request to get a page of Employments");
        Page<EmploymentDTO> page = employmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employments/:id} : get the "id" employment.
     *
     * @param id the id of the employmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employments/{id}")
    public ResponseEntity<EmploymentDTO> getEmployment(@PathVariable Long id) {
        log.debug("REST request to get Employment : {}", id);
        Optional<EmploymentDTO> employmentDTO = employmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employmentDTO);
    }

    @GetMapping("/employments/worker/{workerid}")
    public List<Employment> getworkerEmployment(@PathVariable Long workerid) {
        log.debug("REST request to get employment : {}", workerid);
        List<Employment> employments = employmentService.findOneWorker(workerid);
        return employments;
    }

    /**
     * {@code DELETE  /employments/:id} : delete the "id" employment.
     *
     * @param id the id of the employmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employments/{id}")
    public ResponseEntity<Void> deleteEmployment(@PathVariable Long id) {
        log.debug("REST request to delete Employment : {}", id);
        Employment emp = employmentRepository.findById(id).get();
        employmentService.delete(id);

        Long elastic_id = emp.getWorker().getId();
        emp.setWorker(null);

        ElasticWorker e = rep1.findById(elastic_id.toString()).get();
        e.removeEmployment(emp);

        rabbit_msg.convertAndSend("topicExchange1", "routingKey", e);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
