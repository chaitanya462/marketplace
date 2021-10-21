package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.repository.VmsjobSubmitRepository;
import com.simplify.marketplace.service.VmsjobSubmitService;
import com.simplify.marketplace.service.dto.VmsjobSubmitDTO;
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
 * REST controller for managing {@link com.simplify.marketplace.domain.VmsjobSubmit}.
 */
@RestController
@RequestMapping("/api")
public class VmsjobSubmitResource {

    private final Logger log = LoggerFactory.getLogger(VmsjobSubmitResource.class);

    private static final String ENTITY_NAME = "vmsjobSubmit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VmsjobSubmitService vmsjobSubmitService;

    private final VmsjobSubmitRepository vmsjobSubmitRepository;

    public VmsjobSubmitResource(VmsjobSubmitService vmsjobSubmitService, VmsjobSubmitRepository vmsjobSubmitRepository) {
        this.vmsjobSubmitService = vmsjobSubmitService;
        this.vmsjobSubmitRepository = vmsjobSubmitRepository;
    }

    /**
     * {@code POST  /vmsjob-submits} : Create a new vmsjobSubmit.
     *
     * @param vmsjobSubmitDTO the vmsjobSubmitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vmsjobSubmitDTO, or with status {@code 400 (Bad Request)} if the vmsjobSubmit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vmsjob-submits")
    public ResponseEntity<VmsjobSubmitDTO> createVmsjobSubmit(@RequestBody VmsjobSubmitDTO vmsjobSubmitDTO) throws URISyntaxException {
        log.debug("REST request to save VmsjobSubmit : {}", vmsjobSubmitDTO);
        if (vmsjobSubmitDTO.getId() != null) {
            throw new BadRequestAlertException("A new vmsjobSubmit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VmsjobSubmitDTO result = vmsjobSubmitService.save(vmsjobSubmitDTO);
        return ResponseEntity
            .created(new URI("/api/vmsjob-submits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vmsjob-submits/:id} : Updates an existing vmsjobSubmit.
     *
     * @param id the id of the vmsjobSubmitDTO to save.
     * @param vmsjobSubmitDTO the vmsjobSubmitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vmsjobSubmitDTO,
     * or with status {@code 400 (Bad Request)} if the vmsjobSubmitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vmsjobSubmitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vmsjob-submits/{id}")
    public ResponseEntity<VmsjobSubmitDTO> updateVmsjobSubmit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VmsjobSubmitDTO vmsjobSubmitDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VmsjobSubmit : {}, {}", id, vmsjobSubmitDTO);
        if (vmsjobSubmitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vmsjobSubmitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vmsjobSubmitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VmsjobSubmitDTO result = vmsjobSubmitService.save(vmsjobSubmitDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vmsjobSubmitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vmsjob-submits/:id} : Partial updates given fields of an existing vmsjobSubmit, field will ignore if it is null
     *
     * @param id the id of the vmsjobSubmitDTO to save.
     * @param vmsjobSubmitDTO the vmsjobSubmitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vmsjobSubmitDTO,
     * or with status {@code 400 (Bad Request)} if the vmsjobSubmitDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vmsjobSubmitDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vmsjobSubmitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vmsjob-submits/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VmsjobSubmitDTO> partialUpdateVmsjobSubmit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VmsjobSubmitDTO vmsjobSubmitDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VmsjobSubmit partially : {}, {}", id, vmsjobSubmitDTO);
        if (vmsjobSubmitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vmsjobSubmitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vmsjobSubmitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VmsjobSubmitDTO> result = vmsjobSubmitService.partialUpdate(vmsjobSubmitDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vmsjobSubmitDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vmsjob-submits} : get all the vmsjobSubmits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vmsjobSubmits in body.
     */
    @GetMapping("/vmsjob-submits")
    public ResponseEntity<List<VmsjobSubmitDTO>> getAllVmsjobSubmits(Pageable pageable) {
        log.debug("REST request to get a page of VmsjobSubmits");
        Page<VmsjobSubmitDTO> page = vmsjobSubmitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vmsjob-submits/:id} : get the "id" vmsjobSubmit.
     *
     * @param id the id of the vmsjobSubmitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vmsjobSubmitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vmsjob-submits/{id}")
    public ResponseEntity<VmsjobSubmitDTO> getVmsjobSubmit(@PathVariable Long id) {
        log.debug("REST request to get VmsjobSubmit : {}", id);
        Optional<VmsjobSubmitDTO> vmsjobSubmitDTO = vmsjobSubmitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vmsjobSubmitDTO);
    }

    /**
     * {@code DELETE  /vmsjob-submits/:id} : delete the "id" vmsjobSubmit.
     *
     * @param id the id of the vmsjobSubmitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vmsjob-submits/{id}")
    public ResponseEntity<Void> deleteVmsjobSubmit(@PathVariable Long id) {
        log.debug("REST request to delete VmsjobSubmit : {}", id);
        vmsjobSubmitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
