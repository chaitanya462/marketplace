package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.repository.UserPhoneRepository;
import com.simplify.marketplace.service.UserPhoneService;
import com.simplify.marketplace.service.UserService;
import com.simplify.marketplace.service.dto.UserPhoneDTO;
import com.simplify.marketplace.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
 * REST controller for managing {@link com.simplify.marketplace.domain.UserPhone}.
 */
@RestController
@RequestMapping("/api")
public class UserPhoneResource {

    private UserService userService;

    private final Logger log = LoggerFactory.getLogger(UserPhoneResource.class);

    private static final String ENTITY_NAME = "userPhone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserPhoneService userPhoneService;

    private final UserPhoneRepository userPhoneRepository;

    public UserPhoneResource(UserPhoneService userPhoneService, UserPhoneRepository userPhoneRepository, UserService userService) {
        this.userPhoneService = userPhoneService;
        this.userPhoneRepository = userPhoneRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /user-phones} : Create a new userPhone.
     *
     * @param userPhoneDTO the userPhoneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userPhoneDTO, or with status {@code 400 (Bad Request)} if the userPhone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-phones")
    public ResponseEntity<UserPhoneDTO> createUserPhone(@RequestBody UserPhoneDTO userPhoneDTO) throws URISyntaxException {
        log.debug("REST request to save UserPhone : {}", userPhoneDTO);
        if (userPhoneDTO.getId() != null) {
            throw new BadRequestAlertException("A new userPhone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userPhoneDTO.setCreatedBy(userService.getUserWithAuthorities().get().getId() + "");
        userPhoneDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        userPhoneDTO.setUpdatedAt(LocalDate.now());
        userPhoneDTO.setCreatedAt(LocalDate.now());
        UserPhoneDTO result = userPhoneService.save(userPhoneDTO);
        return ResponseEntity
            .created(new URI("/api/user-phones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-phones/:id} : Updates an existing userPhone.
     *
     * @param id the id of the userPhoneDTO to save.
     * @param userPhoneDTO the userPhoneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPhoneDTO,
     * or with status {@code 400 (Bad Request)} if the userPhoneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userPhoneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-phones/{id}")
    public ResponseEntity<UserPhoneDTO> updateUserPhone(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserPhoneDTO userPhoneDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserPhone : {}, {}", id, userPhoneDTO);
        if (userPhoneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPhoneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPhoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        userPhoneDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        userPhoneDTO.setUpdatedAt(LocalDate.now());
        UserPhoneDTO result = userPhoneService.save(userPhoneDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPhoneDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-phones/:id} : Partial updates given fields of an existing userPhone, field will ignore if it is null
     *
     * @param id the id of the userPhoneDTO to save.
     * @param userPhoneDTO the userPhoneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPhoneDTO,
     * or with status {@code 400 (Bad Request)} if the userPhoneDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userPhoneDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userPhoneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-phones/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UserPhoneDTO> partialUpdateUserPhone(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserPhoneDTO userPhoneDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserPhone partially : {}, {}", id, userPhoneDTO);
        if (userPhoneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPhoneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPhoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        userPhoneDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        userPhoneDTO.setUpdatedAt(LocalDate.now());
        Optional<UserPhoneDTO> result = userPhoneService.partialUpdate(userPhoneDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPhoneDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-phones} : get all the userPhones.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userPhones in body.
     */
    @GetMapping("/user-phones")
    public ResponseEntity<List<UserPhoneDTO>> getAllUserPhones(Pageable pageable) {
        log.debug("REST request to get a page of UserPhones");
        Page<UserPhoneDTO> page = userPhoneService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-phones/:id} : get the "id" userPhone.
     *
     * @param id the id of the userPhoneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userPhoneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-phones/{id}")
    public ResponseEntity<UserPhoneDTO> getUserPhone(@PathVariable Long id) {
        log.debug("REST request to get UserPhone : {}", id);
        Optional<UserPhoneDTO> userPhoneDTO = userPhoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userPhoneDTO);
    }

    /**
     * {@code DELETE  /user-phones/:id} : delete the "id" userPhone.
     *
     * @param id the id of the userPhoneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-phones/{id}")
    public ResponseEntity<Void> deleteUserPhone(@PathVariable Long id) {
        log.debug("REST request to delete UserPhone : {}", id);
        userPhoneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
