package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.repository.ESearchWorkerRepository;
import com.simplify.marketplace.repository.EducationRepository;
import com.simplify.marketplace.repository.WorkerRepository;
import com.simplify.marketplace.service.EducationService;
import com.simplify.marketplace.service.UserService;
import com.simplify.marketplace.service.dto.EducationDTO;
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
 * REST controller for managing {@link com.simplify.marketplace.domain.Education}.
 */
@RestController
@RequestMapping("/api")
public class EducationResource {

    private UserService userService;

    @Autowired
    ESearchWorkerRepository rep1;

    @Autowired
    RabbitTemplate rabbit_msg;

    @Autowired
    WorkerRepository wrepo;

    @Autowired
    EducationRepository edurep;

    private final Logger log = LoggerFactory.getLogger(EducationResource.class);

    private static final String ENTITY_NAME = "education";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EducationService educationService;

    private final EducationRepository educationRepository;

    public EducationResource(EducationService educationService, EducationRepository educationRepository, UserService userService) {
        this.educationService = educationService;
        this.educationRepository = educationRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /educations} : Create a new education.
     *
     * @param educationDTO the educationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new educationDTO, or with status {@code 400 (Bad Request)} if the education has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/educations")
    public ResponseEntity<EducationDTO> createEducation(@RequestBody EducationDTO educationDTO) throws URISyntaxException {
        log.debug("REST request to save Education : {}", educationDTO);
        if (educationDTO.getId() != null) {
            throw new BadRequestAlertException("A new education cannot already have an ID", ENTITY_NAME, "idexists");
        }
        educationDTO.setCreatedBy(userService.getUserWithAuthorities().get().getId() + "");
        educationDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        educationDTO.setUpdatedAt(LocalDate.now());
        educationDTO.setCreatedAt(LocalDate.now());

        EducationDTO result = educationService.save(educationDTO);

        Education education = educationRepository.findById(result.getId()).get();
        if (education.getWorker() != null) {
            Long workerid = education.getWorker().getId();
            ElasticWorker elasticworker = rep1.findById(workerid.toString()).get();
            elasticworker.addEducation(education);
            rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticworker);
        }

        return ResponseEntity
            .created(new URI("/api/educations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /educations/:id} : Updates an existing education.
     *
     * @param id the id of the educationDTO to save.
     * @param educationDTO the educationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated educationDTO,
     * or with status {@code 400 (Bad Request)} if the educationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the educationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/educations/{id}")
    public ResponseEntity<EducationDTO> updateEducation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EducationDTO educationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Education : {}, {}", id, educationDTO);
        if (educationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, educationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!educationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        educationDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        educationDTO.setUpdatedAt(LocalDate.now());

        Education prevEducation = educationRepository.findById(educationDTO.getId()).get();

        EducationDTO result = educationService.save(educationDTO);

        Education updatedEducation = educationRepository.findById(result.getId()).get();

        if (prevEducation.getWorker() != null && !Objects.equals(updatedEducation.getWorker().getId(), prevEducation.getWorker().getId())) {
            ElasticWorker prevElasticWorker = rep1.findById(prevEducation.getWorker().getId().toString()).get();
            prevEducation.setWorker(null);
            prevElasticWorker = prevElasticWorker.removeEducation(prevEducation);
            rabbit_msg.convertAndSend("topicExchange1", "routingKey", prevElasticWorker);
        }

        prevEducation.setWorker(null);

        ElasticWorker elasticworker = rep1.findById(updatedEducation.getWorker().getId().toString()).get();
        elasticworker.removeEducation(prevEducation);
        elasticworker.addEducation(updatedEducation);
        rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticworker);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, educationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /educations/:id} : Partial updates given fields of an existing education, field will ignore if it is null
     *
     * @param id the id of the educationDTO to save.
     * @param educationDTO the educationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated educationDTO,
     * or with status {@code 400 (Bad Request)} if the educationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the educationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the educationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/educations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EducationDTO> partialUpdateEducation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EducationDTO educationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Education partially : {}, {}", id, educationDTO);
        if (educationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, educationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!educationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        educationDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        educationDTO.setUpdatedAt(LocalDate.now());

        Optional<EducationDTO> result = educationService.partialUpdate(educationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, educationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /educations} : get all the educations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of educations in body.
     */
    @GetMapping("/educations")
    public ResponseEntity<List<EducationDTO>> getAllEducations(Pageable pageable) {
        log.debug("REST request to get a page of Educations");
        Page<EducationDTO> page = educationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/educations/worker/{workerid}")
    public List<Education> getworkerEducation(@PathVariable Long workerid) {
        log.debug("REST request to get Education : {}", workerid);
        List<Education> educations = educationService.findOneWorker(workerid);
        return educations;
    }

    /**
     * {@code GET  /educations/:id} : get the "id" education.
     *
     * @param id the id of the educationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the educationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/educations/{id}")
    public ResponseEntity<EducationDTO> getEducation(@PathVariable Long id) {
        log.debug("REST request to get Education : {}", id);
        Optional<EducationDTO> educationDTO = educationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(educationDTO);
    }

    /**
     * {@code DELETE  /educations/:id} : delete the "id" education.
     *
     * @param id the id of the educationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/educations/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id) {
        log.debug("REST request to delete Education : {}", id);
        Education edu = edurep.findById(id).get();
        educationService.delete(id);

        Long curr_id = edu.getWorker().getId();
        edu.setWorker(null);
        ElasticWorker eworker = rep1.findById(curr_id.toString()).get();
        eworker.removeEducation(edu);

        rabbit_msg.convertAndSend("topicExchange1", "routingKey", eworker);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
