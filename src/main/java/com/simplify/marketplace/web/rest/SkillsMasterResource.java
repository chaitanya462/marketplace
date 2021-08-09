package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.repository.SkillsMasterRepository;
import com.simplify.marketplace.service.SkillsMasterService;
import com.simplify.marketplace.service.dto.SkillsMasterDTO;
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
 * REST controller for managing {@link com.simplify.marketplace.domain.SkillsMaster}.
 */
@RestController
@RequestMapping("/api")
public class SkillsMasterResource {

    private final Logger log = LoggerFactory.getLogger(SkillsMasterResource.class);

    private static final String ENTITY_NAME = "skillsMaster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SkillsMasterService skillsMasterService;

    private final SkillsMasterRepository skillsMasterRepository;

    public SkillsMasterResource(SkillsMasterService skillsMasterService, SkillsMasterRepository skillsMasterRepository) {
        this.skillsMasterService = skillsMasterService;
        this.skillsMasterRepository = skillsMasterRepository;
    }

    /**
     * {@code POST  /skills-masters} : Create a new skillsMaster.
     *
     * @param skillsMasterDTO the skillsMasterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new skillsMasterDTO, or with status {@code 400 (Bad Request)} if the skillsMaster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/skills-masters")
    public ResponseEntity<SkillsMasterDTO> createSkillsMaster(@RequestBody SkillsMasterDTO skillsMasterDTO) throws URISyntaxException {
        log.debug("REST request to save SkillsMaster : {}", skillsMasterDTO);
        if (skillsMasterDTO.getId() != null) {
            throw new BadRequestAlertException("A new skillsMaster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SkillsMasterDTO result = skillsMasterService.save(skillsMasterDTO);
        return ResponseEntity
            .created(new URI("/api/skills-masters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /skills-masters/:id} : Updates an existing skillsMaster.
     *
     * @param id the id of the skillsMasterDTO to save.
     * @param skillsMasterDTO the skillsMasterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skillsMasterDTO,
     * or with status {@code 400 (Bad Request)} if the skillsMasterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the skillsMasterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/skills-masters/{id}")
    public ResponseEntity<SkillsMasterDTO> updateSkillsMaster(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SkillsMasterDTO skillsMasterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SkillsMaster : {}, {}", id, skillsMasterDTO);
        if (skillsMasterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skillsMasterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillsMasterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SkillsMasterDTO result = skillsMasterService.save(skillsMasterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, skillsMasterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /skills-masters/:id} : Partial updates given fields of an existing skillsMaster, field will ignore if it is null
     *
     * @param id the id of the skillsMasterDTO to save.
     * @param skillsMasterDTO the skillsMasterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skillsMasterDTO,
     * or with status {@code 400 (Bad Request)} if the skillsMasterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the skillsMasterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the skillsMasterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/skills-masters/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SkillsMasterDTO> partialUpdateSkillsMaster(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SkillsMasterDTO skillsMasterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SkillsMaster partially : {}, {}", id, skillsMasterDTO);
        if (skillsMasterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skillsMasterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillsMasterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SkillsMasterDTO> result = skillsMasterService.partialUpdate(skillsMasterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, skillsMasterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /skills-masters} : get all the skillsMasters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of skillsMasters in body.
     */
    @GetMapping("/skills-masters")
    public ResponseEntity<List<SkillsMasterDTO>> getAllSkillsMasters(Pageable pageable) {
        log.debug("REST request to get a page of SkillsMasters");
        Page<SkillsMasterDTO> page = skillsMasterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /skills-masters/:id} : get the "id" skillsMaster.
     *
     * @param id the id of the skillsMasterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the skillsMasterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/skills-masters/{id}")
    public ResponseEntity<SkillsMasterDTO> getSkillsMaster(@PathVariable Long id) {
        log.debug("REST request to get SkillsMaster : {}", id);
        Optional<SkillsMasterDTO> skillsMasterDTO = skillsMasterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(skillsMasterDTO);
    }

    /**
     * {@code DELETE  /skills-masters/:id} : delete the "id" skillsMaster.
     *
     * @param id the id of the skillsMasterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/skills-masters/{id}")
    public ResponseEntity<Void> deleteSkillsMaster(@PathVariable Long id) {
        log.debug("REST request to delete SkillsMaster : {}", id);
        skillsMasterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
