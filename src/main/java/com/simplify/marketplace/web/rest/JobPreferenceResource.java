package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.repository.CategoryRepository;
import com.simplify.marketplace.repository.ESearchWorkerRepository;
import com.simplify.marketplace.repository.JobPreferenceRepository;
import com.simplify.marketplace.repository.WorkerRepository;
import com.simplify.marketplace.service.JobPreferenceService;
import com.simplify.marketplace.service.UserService;
import com.simplify.marketplace.service.dto.JobPreferenceDTO;
import com.simplify.marketplace.service.mapper.CategoryMapper;
import com.simplify.marketplace.service.mapper.JobPreferenceMapper;
import com.simplify.marketplace.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.simplify.marketplace.domain.JobPreference}.
 */
@RestController
@RequestMapping("/api")
public class JobPreferenceResource {

    private UserService userService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    JobPreferenceMapper jobpreferencesMapper;

    private final Logger log = LoggerFactory.getLogger(JobPreferenceResource.class);

    private static final String ENTITY_NAME = "jobPreference";

    @Autowired
    ESearchWorkerRepository rep1;

    @Autowired
    RabbitTemplate rabbit_msg;

    @Autowired
    WorkerRepository wrepo;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobPreferenceService jobPreferenceService;

    private final JobPreferenceRepository jobPreferenceRepository;

    public JobPreferenceResource(
        JobPreferenceService jobPreferenceService,
        JobPreferenceRepository jobPreferenceRepository,
        UserService userService
    ) {
        this.jobPreferenceService = jobPreferenceService;
        this.jobPreferenceRepository = jobPreferenceRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /job-preferences} : Create a new jobPreference.
     *
     * @param jobPreferenceDTO the jobPreferenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobPreferenceDTO, or with status {@code 400 (Bad Request)} if the jobPreference has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-preferences")
    public ResponseEntity<JobPreferenceDTO> createJobPreference(@RequestBody JobPreferenceDTO jobPreferenceDTO) throws URISyntaxException {
        log.debug("REST request to save JobPreference : {}", jobPreferenceDTO);
        if (jobPreferenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new jobPreference cannot already have an ID", ENTITY_NAME, "idexists");
        }
        jobPreferenceDTO.setCreatedBy(userService.getUserWithAuthorities().get().getId() + "");
        jobPreferenceDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        jobPreferenceDTO.setUpdatedAt(LocalDate.now());
        jobPreferenceDTO.setCreatedAt(LocalDate.now());
        JobPreferenceDTO result = jobPreferenceService.save(jobPreferenceDTO);

        if (jobPreferenceDTO.getWorker() != null) {
            String Workerid = jobPreferenceDTO.getWorker().getId().toString();

            ElasticWorker elasticworker = rep1.findById(Workerid).get();

            Category subCategory = categoryMapper.toEntity(jobPreferenceDTO.getSubCategory());
            //            System.out.println("\n\n\n\n\n\n\n" + subCategory + "\n\n\n\n\n");
            Category ParentCategory = categoryRepository.findById(subCategory.getParent().getId()).get();
            elasticworker.setCategory(ParentCategory.getName());
            jobPreferenceDTO.setId(result.getId());
            elasticworker.addJobPreference(jobpreferencesMapper.toEntity(jobPreferenceDTO));

            rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticworker);
        }

        return ResponseEntity
            .created(new URI("/api/job-preferences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-preferences/:id} : Updates an existing jobPreference.
     *
     * @param id the id of the jobPreferenceDTO to save.
     * @param jobPreferenceDTO the jobPreferenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobPreferenceDTO,
     * or with status {@code 400 (Bad Request)} if the jobPreferenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobPreferenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-preferences/{id}")
    public ResponseEntity<JobPreferenceDTO> updateJobPreference(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JobPreferenceDTO jobPreferenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update JobPreference : {}, {}", id, jobPreferenceDTO);
        if (jobPreferenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobPreferenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobPreferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        jobPreferenceDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        jobPreferenceDTO.setUpdatedAt(LocalDate.now());
        JobPreferenceDTO result = jobPreferenceService.save(jobPreferenceDTO);

        JobPreferenceDTO jobpreference = jobPreferenceService.findOne(id).get();

        Long workerId = jobpreference.getWorker().getId();
        ElasticWorker elasticWorker = rep1.findById(workerId.toString()).get();
        Set<JobPreference> jobpreferenceset = elasticWorker.getJobPreferences();
        jobpreferenceset.remove(null);
        for (JobPreference jobpreferencei : jobpreferenceset) {
            if (jobpreferencei != null && jobpreferencei.getId() != null && jobpreferencei.getId() == id) {
                elasticWorker.removeJobPreference(jobpreferencei);
                jobpreferenceset.add(jobpreferencesMapper.toEntity(jobPreferenceDTO));
                elasticWorker.setJobPreferences(jobpreferenceset);
                rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticWorker);
                break;
            }
        }

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobPreferenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /job-preferences/:id} : Partial updates given fields of an existing jobPreference, field will ignore if it is null
     *
     * @param id the id of the jobPreferenceDTO to save.
     * @param jobPreferenceDTO the jobPreferenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobPreferenceDTO,
     * or with status {@code 400 (Bad Request)} if the jobPreferenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the jobPreferenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the jobPreferenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/job-preferences/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<JobPreferenceDTO> partialUpdateJobPreference(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JobPreferenceDTO jobPreferenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update JobPreference partially : {}, {}", id, jobPreferenceDTO);
        if (jobPreferenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobPreferenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobPreferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        jobPreferenceDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        jobPreferenceDTO.setUpdatedAt(LocalDate.now());

        Optional<JobPreferenceDTO> result = jobPreferenceService.partialUpdate(jobPreferenceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobPreferenceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /job-preferences} : get all the jobPreferences.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobPreferences in body.
     */
    @GetMapping("/job-preferences")
    public ResponseEntity<List<JobPreferenceDTO>> getAllJobPreferences(Pageable pageable) {
        log.debug("REST request to get a page of JobPreferences");
        Page<JobPreferenceDTO> page = jobPreferenceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /job-preferences/:id} : get the "id" jobPreference.
     *
     * @param id the id of the jobPreferenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobPreferenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-preferences/{id}")
    public ResponseEntity<JobPreferenceDTO> getJobPreference(@PathVariable Long id) {
        log.debug("REST request to get JobPreference : {}", id);
        Optional<JobPreferenceDTO> jobPreferenceDTO = jobPreferenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobPreferenceDTO);
    }

    @GetMapping("/job-preferences/worker/{workerid}")
    public List<JobPreference> getworkerJobPreference(@PathVariable Long workerid) {
        log.debug("REST request to get Certificate : {}", workerid);
        List<JobPreference> jobPreferences = jobPreferenceService.findOneWorker(workerid);
        return jobPreferences;
    }

    /**
     * {@code DELETE  /job-preferences/:id} : delete the "id" jobPreference.
     *
     * @param id the id of the jobPreferenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-preferences/{id}")
    public ResponseEntity<Void> deleteJobPreference(@PathVariable Long id) {
        log.debug("REST request to delete JobPreference : {}", id);

        JobPreferenceDTO jobpreference = jobPreferenceService.findOne(id).get();

        jobPreferenceService.delete(id);

        Long workerId = jobpreference.getWorker().getId();
        ElasticWorker elasticWorker = rep1.findById(workerId.toString()).get();
        Set<JobPreference> jobpreferenceset = elasticWorker.getJobPreferences();
        jobpreferenceset.remove(null);
        for (JobPreference jobpreferencei : jobpreferenceset) {
            if (jobpreferencei != null && jobpreferencei.getId() != null && jobpreferencei.getId() == id) {
                elasticWorker.removeJobPreference(jobpreferencei);

                rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticWorker);
                break;
            }
        }

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
