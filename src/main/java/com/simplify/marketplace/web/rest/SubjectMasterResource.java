package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.repository.SubjectMasterRepository;
import com.simplify.marketplace.service.SubjectMasterService;
import com.simplify.marketplace.service.dto.SubjectMasterDTO;
import com.simplify.marketplace.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing {@link com.simplify.marketplace.domain.SubjectMaster}.
 */
@RestController
@RequestMapping("/api")
public class SubjectMasterResource {

    private final Logger log = LoggerFactory.getLogger(SubjectMasterResource.class);

    private static final String ENTITY_NAME = "subjectMaster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubjectMasterService subjectMasterService;

    private final SubjectMasterRepository subjectMasterRepository;

    public SubjectMasterResource(SubjectMasterService subjectMasterService, SubjectMasterRepository subjectMasterRepository) {
        this.subjectMasterService = subjectMasterService;
        this.subjectMasterRepository = subjectMasterRepository;
    }

    /**
     * {@code POST  /subject-masters} : Create a new subjectMaster.
     *
     * @param subjectMasterDTO the subjectMasterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subjectMasterDTO, or with status {@code 400 (Bad Request)} if the subjectMaster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subject-masters")
    public ResponseEntity<SubjectMasterDTO> createSubjectMaster(@RequestBody SubjectMasterDTO subjectMasterDTO) throws URISyntaxException {
        log.debug("REST request to save SubjectMaster : {}", subjectMasterDTO);
        if (subjectMasterDTO.getId() != null) {
            throw new BadRequestAlertException("A new subjectMaster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubjectMasterDTO result = subjectMasterService.save(subjectMasterDTO);
        return ResponseEntity
            .created(new URI("/api/subject-masters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subject-masters/:id} : Updates an existing subjectMaster.
     *
     * @param id the id of the subjectMasterDTO to save.
     * @param subjectMasterDTO the subjectMasterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subjectMasterDTO,
     * or with status {@code 400 (Bad Request)} if the subjectMasterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subjectMasterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subject-masters/{id}")
    public ResponseEntity<SubjectMasterDTO> updateSubjectMaster(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubjectMasterDTO subjectMasterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubjectMaster : {}, {}", id, subjectMasterDTO);
        if (subjectMasterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subjectMasterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subjectMasterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubjectMasterDTO result = subjectMasterService.save(subjectMasterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subjectMasterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /subject-masters/:id} : Partial updates given fields of an existing subjectMaster, field will ignore if it is null
     *
     * @param id the id of the subjectMasterDTO to save.
     * @param subjectMasterDTO the subjectMasterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subjectMasterDTO,
     * or with status {@code 400 (Bad Request)} if the subjectMasterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subjectMasterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subjectMasterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/subject-masters/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SubjectMasterDTO> partialUpdateSubjectMaster(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubjectMasterDTO subjectMasterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubjectMaster partially : {}, {}", id, subjectMasterDTO);
        if (subjectMasterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subjectMasterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subjectMasterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubjectMasterDTO> result = subjectMasterService.partialUpdate(subjectMasterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subjectMasterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /subject-masters} : get all the subjectMasters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subjectMasters in body.
     */
    @GetMapping("/subject-masters")
    public ResponseEntity<List<SubjectMasterDTO>> getAllSubjectMasters(Pageable pageable) {
        log.debug("REST request to get a page of SubjectMasters");
        Page<SubjectMasterDTO> page = subjectMasterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subject-masters/:id} : get the "id" subjectMaster.
     *
     * @param id the id of the subjectMasterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subjectMasterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subject-masters/{id}")
    public ResponseEntity<SubjectMasterDTO> getSubjectMaster(@PathVariable Long id) {
        log.debug("REST request to get SubjectMaster : {}", id);
        Optional<SubjectMasterDTO> subjectMasterDTO = subjectMasterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subjectMasterDTO);
    }

    /**
     * {@code DELETE  /subject-masters/:id} : delete the "id" subjectMaster.
     *
     * @param id the id of the subjectMasterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subject-masters/{id}")
    public ResponseEntity<Void> deleteSubjectMaster(@PathVariable Long id) {
        log.debug("REST request to delete SubjectMaster : {}", id);
        subjectMasterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
