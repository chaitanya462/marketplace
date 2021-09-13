package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.domain.*;
import com.simplify.marketplace.repository.ESearchWorkerRepository;
import com.simplify.marketplace.repository.ReferecesRepository;
import com.simplify.marketplace.repository.WorkerRepository;
import com.simplify.marketplace.service.ReferecesService;
import com.simplify.marketplace.service.UserService;
import com.simplify.marketplace.service.dto.ReferecesDTO;
import com.simplify.marketplace.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.simplify.marketplace.domain.Refereces}.
 */
@RestController
@RequestMapping("/api")
public class ReferecesResource {

    private UserService userService;
    private final Logger log = LoggerFactory.getLogger(ReferecesResource.class);

    private static final String ENTITY_NAME = "refereces";

    @Autowired
    ESearchWorkerRepository rep1;

    @Autowired
    RabbitTemplate rabbit_msg;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReferecesService referecesService;

    private final ReferecesRepository referecesRepository;

    public ReferecesResource(ReferecesService referecesService, ReferecesRepository referecesRepository, UserService userService) {
        this.referecesService = referecesService;
        this.referecesRepository = referecesRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /refereces} : Create a new refereces.
     *
     * @param referecesDTO the referecesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new referecesDTO, or with status {@code 400 (Bad Request)} if the refereces has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/refereces")
    public ResponseEntity<ReferecesDTO> createRefereces(@Valid @RequestBody ReferecesDTO referecesDTO) throws URISyntaxException {
        log.debug("REST request to save Refereces : {}", referecesDTO);
        if (referecesDTO.getId() != null) {
            throw new BadRequestAlertException("A new refereces cannot already have an ID", ENTITY_NAME, "idexists");
        }
        referecesDTO.setCreatedBy(userService.getUserWithAuthorities().get().getId() + "");
        referecesDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        referecesDTO.setUpdatedAt(LocalDate.now());
        referecesDTO.setCreatedAt(LocalDate.now());
        ReferecesDTO result = referecesService.save(referecesDTO);

        Refereces refereces = referecesRepository.findById(result.getId()).get();
        if (refereces.getWorker() != null) {
            Long workerid = refereces.getWorker().getId();
            ElasticWorker elasticworker = rep1.findById(workerid.toString()).get();
            elasticworker.addRefereces(refereces);

            rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticworker);
        }
        return ResponseEntity
            .created(new URI("/api/refereces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /refereces/:id} : Updates an existing refereces.
     *
     * @param id the id of the referecesDTO to save.
     * @param referecesDTO the referecesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referecesDTO,
     * or with status {@code 400 (Bad Request)} if the referecesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the referecesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/refereces/{id}")
    public ResponseEntity<ReferecesDTO> updateRefereces(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReferecesDTO referecesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Refereces : {}, {}", id, referecesDTO);
        if (referecesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referecesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referecesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        referecesDTO.setUpdatedAt(LocalDate.now());

        Refereces prevRefereces = referecesRepository.findById(referecesDTO.getId()).get();

        ReferecesDTO result = referecesService.save(referecesDTO);
        Refereces updatedRefereces = referecesRepository.findById(result.getId()).get();

        if (prevRefereces.getWorker() != null && !Objects.equals(updatedRefereces.getWorker().getId(), prevRefereces.getWorker().getId())) {
            ElasticWorker prevElasticWorker = rep1.findById(prevRefereces.getWorker().getId().toString()).get();
            prevRefereces.setWorker(null);
            prevElasticWorker = prevElasticWorker.removeRefereces(prevRefereces);
            rabbit_msg.convertAndSend("topicExchange1", "routingKey", prevElasticWorker);
        }

        prevRefereces.setWorker(null);

        ElasticWorker elasticworker = rep1.findById(updatedRefereces.getWorker().getId().toString()).get();
        elasticworker.removeRefereces(prevRefereces);
        elasticworker.addRefereces(updatedRefereces);
        rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticworker);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referecesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /refereces/:id} : Partial updates given fields of an existing refereces, field will ignore if it is null
     *
     * @param id the id of the referecesDTO to save.
     * @param referecesDTO the referecesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referecesDTO,
     * or with status {@code 400 (Bad Request)} if the referecesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the referecesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the referecesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/refereces/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ReferecesDTO> partialUpdateRefereces(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReferecesDTO referecesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Refereces partially : {}, {}", id, referecesDTO);
        if (referecesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referecesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referecesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        referecesDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        referecesDTO.setUpdatedAt(LocalDate.now());
        Optional<ReferecesDTO> result = referecesService.partialUpdate(referecesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referecesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /refereces} : get all the refereces.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of refereces in body.
     */
    @GetMapping("/refereces")
    public ResponseEntity<List<ReferecesDTO>> getAllRefereces(Pageable pageable) {
        log.debug("REST request to get a page of Refereces");
        Page<ReferecesDTO> page = referecesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/refereces/worker/{workerid}")
    public List<Refereces> getworkerReference(@PathVariable Long workerid) {
        log.debug("REST request to get Reference : {}", workerid);
        List<Refereces> refereces = referecesService.findOneWorker(workerid);
        return refereces;
    }

    /**
     * {@code GET  /refereces/:id} : get the "id" refereces.
     *
     * @param id the id of the referecesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referecesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/refereces/{id}")
    public ResponseEntity<ReferecesDTO> getRefereces(@PathVariable Long id) {
        log.debug("REST request to get Refereces : {}", id);
        Optional<ReferecesDTO> referecesDTO = referecesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(referecesDTO);
    }

    /**
     * {@code DELETE  /refereces/:id} : delete the "id" refereces.
     *
     * @param id the id of the referecesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/refereces/{id}")
    public ResponseEntity<Void> deleteRefereces(@PathVariable Long id) {
        log.debug("REST request to delete Refereces : {}", id);
        Refereces references = referecesRepository.findById(id).get();
        referecesService.delete(id);

        Long curr_id = references.getWorker().getId();
        references.setWorker(null);

        ElasticWorker elastic_worker = rep1.findById(curr_id.toString()).get();
        elastic_worker.removeRefereces(references);

        rabbit_msg.convertAndSend("topicExchange1", "routingKey", elastic_worker);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
// package com.simplify.marketplace.web.rest;
// import com.simplify.marketplace.domain.ElasticWorker;
// import com.simplify.marketplace.domain.File;
// import com.simplify.marketplace.domain.Refereces;
// import com.simplify.marketplace.repository.ESearchWorkerRepository;
// import com.simplify.marketplace.repository.ReferecesRepository;
// import com.simplify.marketplace.repository.WorkerRepository;
// import com.simplify.marketplace.service.ReferecesService;
// import com.simplify.marketplace.service.UserService;
// import com.simplify.marketplace.service.dto.FileDTO;
// import com.simplify.marketplace.service.dto.ReferecesDTO;
// import com.simplify.marketplace.web.rest.errors.BadRequestAlertException;
// import java.net.URI;
// import java.net.URISyntaxException;
// import java.time.LocalDate;
// import java.util.List;
// import java.util.Objects;
// import java.util.Optional;
// import java.util.Set;
// import javax.validation.Valid;
// import javax.validation.constraints.NotNull;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.amqp.rabbit.core.RabbitTemplate;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
// import tech.jhipster.web.util.HeaderUtil;
// import tech.jhipster.web.util.PaginationUtil;
// import tech.jhipster.web.util.ResponseUtil;
// /**
//  * REST controller for managing {@link com.simplify.marketplace.domain.Refereces}.
//  */
// @RestController
// @RequestMapping("/api")
// public class ReferecesResource {
//     private UserService userService;
//     private final Logger log = LoggerFactory.getLogger(ReferecesResource.class);
//     private static final String ENTITY_NAME = "refereces";
//     @Autowired
//     ESearchWorkerRepository rep1;
//     @Autowired
//     RabbitTemplate rabbit_msg;
//     @Value("${jhipster.clientApp.name}")
//     private String applicationName;
//     private final ReferecesService referecesService;
//     private final ReferecesRepository referecesRepository;
//     public ReferecesResource(ReferecesService referecesService, ReferecesRepository referecesRepository, UserService userService) {
//         this.referecesService = referecesService;
//         this.referecesRepository = referecesRepository;
//         this.userService = userService;
//     }
//     /**
//      * {@code POST  /refereces} : Create a new refereces.
//      *
//      * @param referecesDTO the referecesDTO to create.
//      * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new referecesDTO, or with status {@code 400 (Bad Request)} if the refereces has already an ID.
//      * @throws URISyntaxException if the Location URI syntax is incorrect.
//      */
//     @PostMapping("/refereces")
//     public ResponseEntity<ReferecesDTO> createRefereces(@Valid @RequestBody ReferecesDTO referecesDTO) throws URISyntaxException {
//         log.debug("REST request to save Refereces : {}", referecesDTO);
//         if (referecesDTO.getId() != null) {
//             throw new BadRequestAlertException("A new refereces cannot already have an ID", ENTITY_NAME, "idexists");
//         }
//         referecesDTO.setCreatedBy(userService.getUserWithAuthorities().get().getId() + "");
//         referecesDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
//         referecesDTO.setUpdatedAt(LocalDate.now());
//         referecesDTO.setCreatedAt(LocalDate.now());
//         ReferecesDTO result = referecesService.save(referecesDTO);
//         Refereces refereces = referecesRepository.findById(result.getId()).get();
//         if (refereces.getWorker() != null) {
//             Long workerid = refereces.getWorker().getId();
//             ElasticWorker elasticworker = rep1.findById(workerid.toString()).get();
//             elasticworker.addRefereces(refereces);
//             rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticworker);
//         }
//         return ResponseEntity
//             .created(new URI("/api/refereces/" + result.getId()))
//             .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//             .body(result);
//     }
//     /**
//      * {@code PUT  /refereces/:id} : Updates an existing refereces.
//      *
//      * @param id the id of the referecesDTO to save.
//      * @param referecesDTO the referecesDTO to update.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referecesDTO,
//      * or with status {@code 400 (Bad Request)} if the referecesDTO is not valid,
//      * or with status {@code 500 (Internal Server Error)} if the referecesDTO couldn't be updated.
//      * @throws URISyntaxException if the Location URI syntax is incorrect.
//      */
//     @PutMapping("/refereces/{id}")
//     public ResponseEntity<ReferecesDTO> updateRefereces(
//         @PathVariable(value = "id", required = false) final Long id,
//         @Valid @RequestBody ReferecesDTO referecesDTO
//     ) throws URISyntaxException {
//         log.debug("REST request to update Refereces : {}, {}", id, referecesDTO);
//         if (referecesDTO.getId() == null) {
//             throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//         }
//         if (!Objects.equals(id, referecesDTO.getId())) {
//             throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//         }
//         if (!referecesRepository.existsById(id)) {
//             throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//         }
//         referecesDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
//         referecesDTO.setUpdatedAt(LocalDate.now());
//         Refereces prevRefereces = referecesRepository.findById(referecesDTO.getId()).get();
//         ReferecesDTO result = referecesService.save(referecesDTO);
//         Refereces updatedRefereces = referecesRepository.findById(result.getId()).get();
//         if (prevRefereces.getWorker() != null && !Objects.equals(updatedRefereces.getWorker().getId(), prevRefereces.getWorker().getId())) {
//             ElasticWorker prevElasticWorker = rep1.findById(prevRefereces.getWorker().getId().toString()).get();
//             prevRefereces.setWorker(null);
//             prevElasticWorker = prevElasticWorker.removeRefereces(prevRefereces);
//             rabbit_msg.convertAndSend("topicExchange1", "routingKey", prevElasticWorker);
//         }
//         prevRefereces.setWorker(null);
//         ElasticWorker elasticworker = rep1.findById(updatedRefereces.getWorker().getId().toString()).get();
//         elasticworker.removeRefereces(prevRefereces);
//         elasticworker.addRefereces(updatedRefereces);
//         rabbit_msg.convertAndSend("topicExchange1", "routingKey", elasticworker);
//         return ResponseEntity
//             .ok()
//             .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referecesDTO.getId().toString()))
//             .body(result);
//     }
//     /**
//      * {@code PATCH  /refereces/:id} : Partial updates given fields of an existing refereces, field will ignore if it is null
//      *
//      * @param id the id of the referecesDTO to save.
//      * @param referecesDTO the referecesDTO to update.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referecesDTO,
//      * or with status {@code 400 (Bad Request)} if the referecesDTO is not valid,
//      * or with status {@code 404 (Not Found)} if the referecesDTO is not found,
//      * or with status {@code 500 (Internal Server Error)} if the referecesDTO couldn't be updated.
//      * @throws URISyntaxException if the Location URI syntax is incorrect.
//      */
//     @PatchMapping(value = "/refereces/{id}", consumes = "application/merge-patch+json")
//     public ResponseEntity<ReferecesDTO> partialUpdateRefereces(
//         @PathVariable(value = "id", required = false) final Long id,
//         @NotNull @RequestBody ReferecesDTO referecesDTO
//     ) throws URISyntaxException {
//         log.debug("REST request to partial update Refereces partially : {}, {}", id, referecesDTO);
//         if (referecesDTO.getId() == null) {
//             throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//         }
//         if (!Objects.equals(id, referecesDTO.getId())) {
//             throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//         }
//         if (!referecesRepository.existsById(id)) {
//             throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//         }
//         referecesDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
//         referecesDTO.setUpdatedAt(LocalDate.now());
//         Optional<ReferecesDTO> result = referecesService.partialUpdate(referecesDTO);
//         return ResponseUtil.wrapOrNotFound(
//             result,
//             HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referecesDTO.getId().toString())
//         );
//     }
//     /**
//      * {@code GET  /refereces} : get all the refereces.
//      *
//      * @param pageable the pagination information.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of refereces in body.
//      */
//     @GetMapping("/refereces")
//     public ResponseEntity<List<ReferecesDTO>> getAllRefereces(Pageable pageable) {
//         log.debug("REST request to get a page of Refereces");
//         Page<ReferecesDTO> page = referecesService.findAll(pageable);
//         HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//         return ResponseEntity.ok().headers(headers).body(page.getContent());
//     }
//     /**
//      * {@code GET  /refereces/:id} : get the "id" refereces.
//      *
//      * @param id the id of the referecesDTO to retrieve.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referecesDTO, or with status {@code 404 (Not Found)}.
//      */
//     @GetMapping("/refereces/{id}")
//     public ResponseEntity<ReferecesDTO> getRefereces(@PathVariable Long id) {
//         log.debug("REST request to get Refereces : {}", id);
//         Optional<ReferecesDTO> referecesDTO = referecesService.findOne(id);
//         return ResponseUtil.wrapOrNotFound(referecesDTO);
//     }
//     /**
//      * {@code DELETE  /refereces/:id} : delete the "id" refereces.
//      *
//      * @param id the id of the referecesDTO to delete.
//      * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//      */
//     @DeleteMapping("/refereces/{id}")
//     public ResponseEntity<Void> deleteRefereces(@PathVariable Long id) {
//         log.debug("REST request to delete Refereces : {}", id);
//         Refereces references = referecesRepository.findById(id).get();
//         referecesService.delete(id);
//         Long curr_id = references.getWorker().getId();
//         references.setWorker(null);
//         ElasticWorker elastic_worker = rep1.findById(curr_id.toString()).get();
//         elastic_worker.removeRefereces(references);
//         rabbit_msg.convertAndSend("topicExchange1", "routingKey", elastic_worker);
//         return ResponseEntity
//             .noContent()
//             .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//             .build();
//     }
// }
