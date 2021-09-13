package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.repository.UserEmailRepository;
import com.simplify.marketplace.service.UserEmailService;
import com.simplify.marketplace.service.UserService;
import com.simplify.marketplace.service.dto.UserEmailDTO;
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
 * REST controller for managing {@link com.simplify.marketplace.domain.UserEmail}.
 */
@RestController
@RequestMapping("/api")
public class UserEmailResource {

    private UserService userService;
    private final Logger log = LoggerFactory.getLogger(UserEmailResource.class);

    private static final String ENTITY_NAME = "userEmail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserEmailService userEmailService;

    private final UserEmailRepository userEmailRepository;

    public UserEmailResource(UserEmailService userEmailService, UserEmailRepository userEmailRepository, UserService userService) {
        this.userEmailService = userEmailService;
        this.userEmailRepository = userEmailRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /user-emails} : Create a new userEmail.
     *
     * @param userEmailDTO the userEmailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userEmailDTO, or with status {@code 400 (Bad Request)} if the userEmail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-emails")
    public ResponseEntity<UserEmailDTO> createUserEmail(@Valid @RequestBody UserEmailDTO userEmailDTO) throws URISyntaxException {
        log.debug("REST request to save UserEmail : {}", userEmailDTO);
        if (userEmailDTO.getId() != null) {
            throw new BadRequestAlertException("A new userEmail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userEmailDTO.setCreatedBy(userService.getUserWithAuthorities().get().getId() + "");
        userEmailDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        userEmailDTO.setUpdatedAt(LocalDate.now());
        userEmailDTO.setCreatedAt(LocalDate.now());
        UserEmailDTO result = userEmailService.save(userEmailDTO);
        return ResponseEntity
            .created(new URI("/api/user-emails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-emails/:id} : Updates an existing userEmail.
     *
     * @param id the id of the userEmailDTO to save.
     * @param userEmailDTO the userEmailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userEmailDTO,
     * or with status {@code 400 (Bad Request)} if the userEmailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userEmailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-emails/{id}")
    public ResponseEntity<UserEmailDTO> updateUserEmail(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserEmailDTO userEmailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserEmail : {}, {}", id, userEmailDTO);
        if (userEmailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userEmailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userEmailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        userEmailDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        userEmailDTO.setUpdatedAt(LocalDate.now());

        UserEmailDTO result = userEmailService.save(userEmailDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userEmailDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-emails/:id} : Partial updates given fields of an existing userEmail, field will ignore if it is null
     *
     * @param id the id of the userEmailDTO to save.
     * @param userEmailDTO the userEmailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userEmailDTO,
     * or with status {@code 400 (Bad Request)} if the userEmailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userEmailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userEmailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-emails/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UserEmailDTO> partialUpdateUserEmail(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserEmailDTO userEmailDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserEmail partially : {}, {}", id, userEmailDTO);
        if (userEmailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userEmailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userEmailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        userEmailDTO.setUpdatedBy(userService.getUserWithAuthorities().get().getId() + "");
        userEmailDTO.setUpdatedAt(LocalDate.now());

        Optional<UserEmailDTO> result = userEmailService.partialUpdate(userEmailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userEmailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-emails} : get all the userEmails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userEmails in body.
     */
    @GetMapping("/user-emails")
    public ResponseEntity<List<UserEmailDTO>> getAllUserEmails(Pageable pageable) {
        log.debug("REST request to get a page of UserEmails");
        Page<UserEmailDTO> page = userEmailService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-emails/:id} : get the "id" userEmail.
     *
     * @param id the id of the userEmailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userEmailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-emails/{id}")
    public ResponseEntity<UserEmailDTO> getUserEmail(@PathVariable Long id) {
        log.debug("REST request to get UserEmail : {}", id);
        Optional<UserEmailDTO> userEmailDTO = userEmailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userEmailDTO);
    }

    /**
     * {@code DELETE  /user-emails/:id} : delete the "id" userEmail.
     *
     * @param id the id of the userEmailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-emails/{id}")
    public ResponseEntity<Void> deleteUserEmail(@PathVariable Long id) {
        log.debug("REST request to delete UserEmail : {}", id);
        userEmailService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
