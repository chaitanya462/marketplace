package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.repository.VmsjobSaveRepository;
import com.simplify.marketplace.service.VmsjobSaveService;
import com.simplify.marketplace.service.dto.VmsjobSaveDTO;
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
 * REST controller for managing {@link com.simplify.marketplace.domain.VmsjobSave}.
 */
@RestController
@RequestMapping("/api")
public class VmsjobSaveResource {

    private final Logger log = LoggerFactory.getLogger(VmsjobSaveResource.class);

    private static final String ENTITY_NAME = "vmsjobSave";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VmsjobSaveService vmsjobSaveService;

    private final VmsjobSaveRepository vmsjobSaveRepository;

    public VmsjobSaveResource(VmsjobSaveService vmsjobSaveService, VmsjobSaveRepository vmsjobSaveRepository) {
        this.vmsjobSaveService = vmsjobSaveService;
        this.vmsjobSaveRepository = vmsjobSaveRepository;
    }

    /**
     * {@code POST  /vmsjob-saves} : Create a new vmsjobSave.
     *
     * @param vmsjobSaveDTO the vmsjobSaveDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vmsjobSaveDTO, or with status {@code 400 (Bad Request)} if the vmsjobSave has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vmsjob-saves")
    public ResponseEntity<VmsjobSaveDTO> createVmsjobSave(@RequestBody VmsjobSaveDTO vmsjobSaveDTO) throws URISyntaxException {
        log.debug("REST request to save VmsjobSave : {}", vmsjobSaveDTO);
        if (vmsjobSaveDTO.getId() != null) {
            throw new BadRequestAlertException("A new vmsjobSave cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VmsjobSaveDTO result = vmsjobSaveService.save(vmsjobSaveDTO);
        return ResponseEntity
            .created(new URI("/api/vmsjob-saves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vmsjob-saves/:id} : Updates an existing vmsjobSave.
     *
     * @param id the id of the vmsjobSaveDTO to save.
     * @param vmsjobSaveDTO the vmsjobSaveDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vmsjobSaveDTO,
     * or with status {@code 400 (Bad Request)} if the vmsjobSaveDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vmsjobSaveDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vmsjob-saves/{id}")
    public ResponseEntity<VmsjobSaveDTO> updateVmsjobSave(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VmsjobSaveDTO vmsjobSaveDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VmsjobSave : {}, {}", id, vmsjobSaveDTO);
        if (vmsjobSaveDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vmsjobSaveDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vmsjobSaveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VmsjobSaveDTO result = vmsjobSaveService.save(vmsjobSaveDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vmsjobSaveDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vmsjob-saves/:id} : Partial updates given fields of an existing vmsjobSave, field will ignore if it is null
     *
     * @param id the id of the vmsjobSaveDTO to save.
     * @param vmsjobSaveDTO the vmsjobSaveDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vmsjobSaveDTO,
     * or with status {@code 400 (Bad Request)} if the vmsjobSaveDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vmsjobSaveDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vmsjobSaveDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vmsjob-saves/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VmsjobSaveDTO> partialUpdateVmsjobSave(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VmsjobSaveDTO vmsjobSaveDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VmsjobSave partially : {}, {}", id, vmsjobSaveDTO);
        if (vmsjobSaveDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vmsjobSaveDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vmsjobSaveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VmsjobSaveDTO> result = vmsjobSaveService.partialUpdate(vmsjobSaveDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vmsjobSaveDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vmsjob-saves} : get all the vmsjobSaves.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vmsjobSaves in body.
     */
    @GetMapping("/vmsjob-saves")
    public ResponseEntity<List<VmsjobSaveDTO>> getAllVmsjobSaves(Pageable pageable) {
        log.debug("REST request to get a page of VmsjobSaves");
        Page<VmsjobSaveDTO> page = vmsjobSaveService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vmsjob-saves/:id} : get the "id" vmsjobSave.
     *
     * @param id the id of the vmsjobSaveDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vmsjobSaveDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vmsjob-saves/{id}")
    public ResponseEntity<VmsjobSaveDTO> getVmsjobSave(@PathVariable Long id) {
        log.debug("REST request to get VmsjobSave : {}", id);
        Optional<VmsjobSaveDTO> vmsjobSaveDTO = vmsjobSaveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vmsjobSaveDTO);
    }

    /**
     * {@code DELETE  /vmsjob-saves/:id} : delete the "id" vmsjobSave.
     *
     * @param id the id of the vmsjobSaveDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vmsjob-saves/{id}")
    public ResponseEntity<Void> deleteVmsjobSave(@PathVariable Long id) {
        log.debug("REST request to delete VmsjobSave : {}", id);
        vmsjobSaveService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
