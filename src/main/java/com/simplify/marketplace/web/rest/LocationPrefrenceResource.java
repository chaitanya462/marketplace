package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.domain.ElasticWorker;
import com.simplify.marketplace.domain.JobPreference;
import com.simplify.marketplace.domain.LocationPrefrence;
import com.simplify.marketplace.repository.ESearchWorkerRepository;
import com.simplify.marketplace.repository.LocationPrefrenceRepository;
import com.simplify.marketplace.service.JobPreferenceService;
import com.simplify.marketplace.service.LocationPrefrenceService;
import com.simplify.marketplace.service.UserService;
import com.simplify.marketplace.service.dto.JobPreferenceDTO;
import com.simplify.marketplace.service.dto.LocationPrefrenceDTO;
import com.simplify.marketplace.service.mapper.LocationPrefrenceMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.simplify.marketplace.domain.LocationPrefrence}.
 */
@RestController
@RequestMapping("/api")
public class LocationPrefrenceResource {

    private UserService userService;

    @Autowired
    ESearchWorkerRepository elasticsearchRepo;

    @Autowired
    JobPreferenceService jobpreferenceService;

    @Autowired
    LocationPrefrenceMapper locationPreferenceMapper;

    @Autowired
    RabbitTemplate rabbit_msg;

    private final Logger log = LoggerFactory.getLogger(LocationPrefrenceResource.class);

    private static final String ENTITY_NAME = "locationPrefrence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationPrefrenceService locationPrefrenceService;

    private final LocationPrefrenceRepository locationPrefrenceRepository;

    public LocationPrefrenceResource(
        LocationPrefrenceService locationPrefrenceService,
        LocationPrefrenceRepository locationPrefrenceRepository,
        UserService userService
    ) {
        this.locationPrefrenceService = locationPrefrenceService;
        this.locationPrefrenceRepository = locationPrefrenceRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /location-prefrences} : Create a new locationPrefrence.
     *
     * @param locationPrefrenceDTO the locationPrefrenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationPrefrenceDTO, or with status {@code 400 (Bad Request)} if the locationPrefrence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/location-prefrences")
    public ResponseEntity<LocationPrefrenceDTO> createLocationPrefrence(@RequestBody LocationPrefrenceDTO locationPrefrenceDTO)
        throws URISyntaxException {
        log.debug("REST request to save LocationPrefrence : {}", locationPrefrenceDTO);
        if (locationPrefrenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new locationPrefrence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        locationPrefrenceDTO.setCreatedBy(userService.getUserWithAuthorities().get().getId() + "");
        locationPrefrenceDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        locationPrefrenceDTO.setUpdatedAt(LocalDate.now());
        locationPrefrenceDTO.setCreatedAt(LocalDate.now());
        LocationPrefrenceDTO result = locationPrefrenceService.save(locationPrefrenceDTO);

        System.out.println("\n\n\n\n\n\n" + locationPrefrenceDTO + "\n\n\n\n\n\n");
        locationPrefrenceDTO.setId(result.getId());
        Long jobPreferenceId = locationPrefrenceDTO.getWorker().getId();
        Long workerId = locationPrefrenceDTO.getWorker().getWorker().getId();
        ElasticWorker elasticworker = elasticsearchRepo.findById(workerId.toString()).get();
        Set<JobPreference> jobPreferenceSet = elasticworker.getJobPreferences();
        for (JobPreference jobpreference : jobPreferenceSet) {
            if (jobpreference.getId() == jobPreferenceId) {
                elasticworker.removeJobPreference(jobpreference);
                LocationPrefrence newlocationpreference = locationPreferenceMapper.toEntity(locationPrefrenceDTO);
                jobpreference.addLocationPrefrence(newlocationpreference);
                jobPreferenceSet.add(jobpreference);
                elasticworker.setJobPreferences(jobPreferenceSet);
                rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticworker);
            }
        }

        return ResponseEntity
            .created(new URI("/api/location-prefrences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /location-prefrences/:id} : Updates an existing locationPrefrence.
     *
     * @param id the id of the locationPrefrenceDTO to save.
     * @param locationPrefrenceDTO the locationPrefrenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationPrefrenceDTO,
     * or with status {@code 400 (Bad Request)} if the locationPrefrenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationPrefrenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/location-prefrences/{id}")
    public ResponseEntity<LocationPrefrenceDTO> updateLocationPrefrence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationPrefrenceDTO locationPrefrenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LocationPrefrence : {}, {}", id, locationPrefrenceDTO);
        if (locationPrefrenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationPrefrenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationPrefrenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        locationPrefrenceDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        locationPrefrenceDTO.setUpdatedAt(LocalDate.now());
        LocationPrefrenceDTO result = locationPrefrenceService.save(locationPrefrenceDTO);

        System.out.println("\n\n\n\n\n\n" + locationPrefrenceDTO + "\n\n\n\n\n");
        locationPrefrenceDTO.setId(result.getId());
        Long jobPreferenceId = locationPrefrenceDTO.getWorker().getId();
        Long workerId = locationPrefrenceDTO.getWorker().getWorker().getId();
        ElasticWorker elasticworker = elasticsearchRepo.findById(workerId.toString()).get();
        Set<JobPreference> jobPreferenceSet = elasticworker.getJobPreferences();
        for (JobPreference jobpreference : jobPreferenceSet) {
            if (jobpreference.getId() == jobPreferenceId) {
                elasticworker.removeJobPreference(jobpreference);
                Set<LocationPrefrence> locationpreferenceset = jobpreference.getLocationPrefrences();
                for (LocationPrefrence locationpreference : locationpreferenceset) {
                    if (locationpreference.getId() == locationPrefrenceDTO.getId()) {
                        jobpreference.removeLocationPrefrence(locationpreference);
                        LocationPrefrence newlocationpreference = locationPreferenceMapper.toEntity(locationPrefrenceDTO);
                        jobpreference.addLocationPrefrence(newlocationpreference);
                        jobPreferenceSet.add(jobpreference);
                        elasticworker.setJobPreferences(jobPreferenceSet);
                        rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticworker);
                    }
                }
            }
        }

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationPrefrenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /location-prefrences/:id} : Partial updates given fields of an existing locationPrefrence, field will ignore if it is null
     *
     * @param id the id of the locationPrefrenceDTO to save.
     * @param locationPrefrenceDTO the locationPrefrenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationPrefrenceDTO,
     * or with status {@code 400 (Bad Request)} if the locationPrefrenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the locationPrefrenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the locationPrefrenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/location-prefrences/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LocationPrefrenceDTO> partialUpdateLocationPrefrence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationPrefrenceDTO locationPrefrenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LocationPrefrence partially : {}, {}", id, locationPrefrenceDTO);
        if (locationPrefrenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationPrefrenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationPrefrenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        locationPrefrenceDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        locationPrefrenceDTO.setUpdatedAt(LocalDate.now());
        Optional<LocationPrefrenceDTO> result = locationPrefrenceService.partialUpdate(locationPrefrenceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationPrefrenceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /location-prefrences} : get all the locationPrefrences.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationPrefrences in body.
     */
    @GetMapping("/location-prefrences")
    public ResponseEntity<List<LocationPrefrenceDTO>> getAllLocationPrefrences(Pageable pageable) {
        log.debug("REST request to get a page of LocationPrefrences");
        Page<LocationPrefrenceDTO> page = locationPrefrenceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /location-prefrences/:id} : get the "id" locationPrefrence.
     *
     * @param id the id of the locationPrefrenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationPrefrenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/location-prefrences/{id}")
    public ResponseEntity<LocationPrefrenceDTO> getLocationPrefrence(@PathVariable Long id) {
        log.debug("REST request to get LocationPrefrence : {}", id);
        Optional<LocationPrefrenceDTO> locationPrefrenceDTO = locationPrefrenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locationPrefrenceDTO);
    }

    /**
     * {@code DELETE  /location-prefrences/:id} : delete the "id" locationPrefrence.
     *
     * @param id the id of the locationPrefrenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/location-prefrences/{id}")
    public ResponseEntity<Void> deleteLocationPrefrence(@PathVariable Long id) {
        log.debug("REST request to delete LocationPrefrence : {}", id);

        LocationPrefrenceDTO locationPrefrenceDTO = locationPrefrenceService.findOne(id).get();

        System.out.println("\n\n\n\n\n\n" + locationPrefrenceDTO + "\n\n\n\n\n");

        locationPrefrenceService.delete(id);

        Long jobPreferenceId = locationPrefrenceDTO.getWorker().getId();
        JobPreferenceDTO job = jobpreferenceService.findOne(jobPreferenceId).get();

        System.out.println("\n\n\n\n\n\n" + job + "\n\n\n\n\n");

        Long workerId = job.getWorker().getId();
        ElasticWorker elasticworker = elasticsearchRepo.findById(workerId.toString()).get();
        Set<JobPreference> jobPreferenceSet = elasticworker.getJobPreferences();
        for (JobPreference jobpreference : jobPreferenceSet) {
            if (jobpreference.getId() == jobPreferenceId) {
                elasticworker.removeJobPreference(jobpreference);
                Set<LocationPrefrence> locationpreferenceset = jobpreference.getLocationPrefrences();
                for (LocationPrefrence locationpreference : locationpreferenceset) {
                    if (locationpreference.getId() != null && locationpreference.getId() == locationPrefrenceDTO.getId()) {
                        jobpreference.removeLocationPrefrence(locationpreference);

                        jobPreferenceSet.add(jobpreference);
                        elasticworker.setJobPreferences(jobPreferenceSet);
                        rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticworker);
                    }
                }
            }
        }

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
