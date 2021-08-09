package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.repository.FieldValueRepository;
import com.simplify.marketplace.service.FieldValueService;
import com.simplify.marketplace.service.dto.FieldValueDTO;
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
 * REST controller for managing {@link com.simplify.marketplace.domain.FieldValue}.
 */
@RestController
@RequestMapping("/api")
public class FieldValueResource {

    private final Logger log = LoggerFactory.getLogger(FieldValueResource.class);

    private static final String ENTITY_NAME = "fieldValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FieldValueService fieldValueService;

    private final FieldValueRepository fieldValueRepository;

    public FieldValueResource(FieldValueService fieldValueService, FieldValueRepository fieldValueRepository) {
        this.fieldValueService = fieldValueService;
        this.fieldValueRepository = fieldValueRepository;
    }

    /**
     * {@code POST  /field-values} : Create a new fieldValue.
     *
     * @param fieldValueDTO the fieldValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fieldValueDTO, or with status {@code 400 (Bad Request)} if the fieldValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/field-values")
    public ResponseEntity<FieldValueDTO> createFieldValue(@RequestBody FieldValueDTO fieldValueDTO) throws URISyntaxException {
        log.debug("REST request to save FieldValue : {}", fieldValueDTO);
        if (fieldValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new fieldValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FieldValueDTO result = fieldValueService.save(fieldValueDTO);
        return ResponseEntity
            .created(new URI("/api/field-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /field-values/:id} : Updates an existing fieldValue.
     *
     * @param id the id of the fieldValueDTO to save.
     * @param fieldValueDTO the fieldValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldValueDTO,
     * or with status {@code 400 (Bad Request)} if the fieldValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fieldValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/field-values/{id}")
    public ResponseEntity<FieldValueDTO> updateFieldValue(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FieldValueDTO fieldValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FieldValue : {}, {}", id, fieldValueDTO);
        if (fieldValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fieldValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FieldValueDTO result = fieldValueService.save(fieldValueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fieldValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /field-values/:id} : Partial updates given fields of an existing fieldValue, field will ignore if it is null
     *
     * @param id the id of the fieldValueDTO to save.
     * @param fieldValueDTO the fieldValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldValueDTO,
     * or with status {@code 400 (Bad Request)} if the fieldValueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fieldValueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fieldValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/field-values/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FieldValueDTO> partialUpdateFieldValue(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FieldValueDTO fieldValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FieldValue partially : {}, {}", id, fieldValueDTO);
        if (fieldValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fieldValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FieldValueDTO> result = fieldValueService.partialUpdate(fieldValueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fieldValueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /field-values} : get all the fieldValues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fieldValues in body.
     */
    @GetMapping("/field-values")
    public ResponseEntity<List<FieldValueDTO>> getAllFieldValues(Pageable pageable) {
        log.debug("REST request to get a page of FieldValues");
        Page<FieldValueDTO> page = fieldValueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /field-values/:id} : get the "id" fieldValue.
     *
     * @param id the id of the fieldValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fieldValueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/field-values/{id}")
    public ResponseEntity<FieldValueDTO> getFieldValue(@PathVariable Long id) {
        log.debug("REST request to get FieldValue : {}", id);
        Optional<FieldValueDTO> fieldValueDTO = fieldValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fieldValueDTO);
    }

    /**
     * {@code DELETE  /field-values/:id} : delete the "id" fieldValue.
     *
     * @param id the id of the fieldValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/field-values/{id}")
    public ResponseEntity<Void> deleteFieldValue(@PathVariable Long id) {
        log.debug("REST request to delete FieldValue : {}", id);
        fieldValueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
