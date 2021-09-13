package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.repository.CustomUserRepository;
import com.simplify.marketplace.service.CustomUserService;
import com.simplify.marketplace.service.dto.CustomUserDTO;
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
 * REST controller for managing {@link com.simplify.marketplace.domain.CustomUser}.
 */
@RestController
@RequestMapping("/api")
public class CustomUserResource {

    private final Logger log = LoggerFactory.getLogger(CustomUserResource.class);

    private static final String ENTITY_NAME = "customUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomUserService customUserService;

    private final CustomUserRepository customUserRepository;

    public CustomUserResource(CustomUserService customUserService, CustomUserRepository customUserRepository) {
        this.customUserService = customUserService;
        this.customUserRepository = customUserRepository;
    }

    /**
     * {@code POST  /custom-users} : Create a new customUser.
     *
     * @param customUserDTO the customUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customUserDTO, or with status {@code 400 (Bad Request)} if the customUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/custom-users")
    public ResponseEntity<CustomUserDTO> createCustomUser(@RequestBody CustomUserDTO customUserDTO) throws URISyntaxException {
        log.debug("REST request to save CustomUser : {}", customUserDTO);
        if (customUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new customUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomUserDTO result = customUserService.save(customUserDTO);
        return ResponseEntity
            .created(new URI("/api/custom-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /custom-users/:id} : Updates an existing customUser.
     *
     * @param id the id of the customUserDTO to save.
     * @param customUserDTO the customUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customUserDTO,
     * or with status {@code 400 (Bad Request)} if the customUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/custom-users/{id}")
    public ResponseEntity<CustomUserDTO> updateCustomUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CustomUserDTO customUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CustomUser : {}, {}", id, customUserDTO);
        if (customUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomUserDTO result = customUserService.save(customUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /custom-users/:id} : Partial updates given fields of an existing customUser, field will ignore if it is null
     *
     * @param id the id of the customUserDTO to save.
     * @param customUserDTO the customUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customUserDTO,
     * or with status {@code 400 (Bad Request)} if the customUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/custom-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CustomUserDTO> partialUpdateCustomUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CustomUserDTO customUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomUser partially : {}, {}", id, customUserDTO);
        if (customUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomUserDTO> result = customUserService.partialUpdate(customUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /custom-users} : get all the customUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customUsers in body.
     */
    @GetMapping("/custom-users")
    public ResponseEntity<List<CustomUserDTO>> getAllCustomUsers(Pageable pageable) {
        log.debug("REST request to get a page of CustomUsers");
        Page<CustomUserDTO> page = customUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /custom-users/:id} : get the "id" customUser.
     *
     * @param id the id of the customUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/custom-users/{id}")
    public ResponseEntity<CustomUserDTO> getCustomUser(@PathVariable Long id) {
        log.debug("REST request to get CustomUser : {}", id);
        Optional<CustomUserDTO> customUserDTO = customUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customUserDTO);
    }

    /**
     * {@code DELETE  /custom-users/:id} : delete the "id" customUser.
     *
     * @param id the id of the customUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/custom-users/{id}")
    public ResponseEntity<Void> deleteCustomUser(@PathVariable Long id) {
        log.debug("REST request to delete CustomUser : {}", id);
        customUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
