package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.repository.OtpAttemptRepository;
import com.simplify.marketplace.service.OtpAttemptService;
import com.simplify.marketplace.service.UserService;
import com.simplify.marketplace.service.dto.OtpAttemptDTO;
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
 * REST controller for managing {@link com.simplify.marketplace.domain.OtpAttempt}.
 */
@RestController
@RequestMapping("/api")
public class OtpAttemptResource {

    private UserService userService;

    private final Logger log = LoggerFactory.getLogger(OtpAttemptResource.class);

    private static final String ENTITY_NAME = "otpAttempt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OtpAttemptService otpAttemptService;

    private final OtpAttemptRepository otpAttemptRepository;

    public OtpAttemptResource(OtpAttemptService otpAttemptService, OtpAttemptRepository otpAttemptRepository, UserService userService) {
        this.otpAttemptService = otpAttemptService;
        this.otpAttemptRepository = otpAttemptRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /otp-attempts} : Create a new otpAttempt.
     *
     * @param otpAttemptDTO the otpAttemptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new otpAttemptDTO, or with status {@code 400 (Bad Request)} if the otpAttempt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/otp-attempts")
    public ResponseEntity<OtpAttemptDTO> createOtpAttempt(@RequestBody OtpAttemptDTO otpAttemptDTO) throws URISyntaxException {
        log.debug("REST request to save OtpAttempt : {}", otpAttemptDTO);
        if (otpAttemptDTO.getId() != null) {
            throw new BadRequestAlertException("A new otpAttempt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        otpAttemptDTO.setCreatedBy(userService.getUserWithAuthorities().get().getId() + "");
        otpAttemptDTO.setCreatedAt(LocalDate.now());
        OtpAttemptDTO result = otpAttemptService.save(otpAttemptDTO);
        return ResponseEntity
            .created(new URI("/api/otp-attempts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /otp-attempts/:id} : Updates an existing otpAttempt.
     *
     * @param id the id of the otpAttemptDTO to save.
     * @param otpAttemptDTO the otpAttemptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otpAttemptDTO,
     * or with status {@code 400 (Bad Request)} if the otpAttemptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the otpAttemptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/otp-attempts/{id}")
    public ResponseEntity<OtpAttemptDTO> updateOtpAttempt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OtpAttemptDTO otpAttemptDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OtpAttempt : {}, {}", id, otpAttemptDTO);
        if (otpAttemptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otpAttemptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otpAttemptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OtpAttemptDTO result = otpAttemptService.save(otpAttemptDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otpAttemptDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /otp-attempts/:id} : Partial updates given fields of an existing otpAttempt, field will ignore if it is null
     *
     * @param id the id of the otpAttemptDTO to save.
     * @param otpAttemptDTO the otpAttemptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otpAttemptDTO,
     * or with status {@code 400 (Bad Request)} if the otpAttemptDTO is not valid,
     * or with status {@code 404 (Not Found)} if the otpAttemptDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the otpAttemptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/otp-attempts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OtpAttemptDTO> partialUpdateOtpAttempt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OtpAttemptDTO otpAttemptDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OtpAttempt partially : {}, {}", id, otpAttemptDTO);
        if (otpAttemptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otpAttemptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otpAttemptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OtpAttemptDTO> result = otpAttemptService.partialUpdate(otpAttemptDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otpAttemptDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /otp-attempts} : get all the otpAttempts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of otpAttempts in body.
     */
    @GetMapping("/otp-attempts")
    public ResponseEntity<List<OtpAttemptDTO>> getAllOtpAttempts(Pageable pageable) {
        log.debug("REST request to get a page of OtpAttempts");
        Page<OtpAttemptDTO> page = otpAttemptService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /otp-attempts/:id} : get the "id" otpAttempt.
     *
     * @param id the id of the otpAttemptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the otpAttemptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/otp-attempts/{id}")
    public ResponseEntity<OtpAttemptDTO> getOtpAttempt(@PathVariable Long id) {
        log.debug("REST request to get OtpAttempt : {}", id);
        Optional<OtpAttemptDTO> otpAttemptDTO = otpAttemptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(otpAttemptDTO);
    }

    /**
     * {@code DELETE  /otp-attempts/:id} : delete the "id" otpAttempt.
     *
     * @param id the id of the otpAttemptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/otp-attempts/{id}")
    public ResponseEntity<Void> deleteOtpAttempt(@PathVariable Long id) {
        log.debug("REST request to delete OtpAttempt : {}", id);
        otpAttemptService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
