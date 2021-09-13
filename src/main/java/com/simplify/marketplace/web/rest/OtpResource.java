package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.repository.OtpRepository;
import com.simplify.marketplace.service.OtpService;
import com.simplify.marketplace.service.UserService;
import com.simplify.marketplace.service.dto.OtpDTO;
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
 * REST controller for managing {@link com.simplify.marketplace.domain.Otp}.
 */
@RestController
@RequestMapping("/api")
public class OtpResource {

    private UserService userService;

    private final Logger log = LoggerFactory.getLogger(OtpResource.class);

    private static final String ENTITY_NAME = "otp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OtpService otpService;

    private final OtpRepository otpRepository;

    public OtpResource(OtpService otpService, OtpRepository otpRepository, UserService userService) {
        this.otpService = otpService;
        this.otpRepository = otpRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /otps} : Create a new otp.
     *
     * @param otpDTO the otpDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new otpDTO, or with status {@code 400 (Bad Request)} if the otp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/otps")
    public ResponseEntity<OtpDTO> createOtp(@RequestBody OtpDTO otpDTO) throws URISyntaxException {
        log.debug("REST request to save Otp : {}", otpDTO);
        if (otpDTO.getId() != null) {
            throw new BadRequestAlertException("A new otp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        otpDTO.setCreatedBy(userService.getUserWithAuthorities().get().getId() + "");
        otpDTO.setCreatedAt(LocalDate.now());
        OtpDTO result = otpService.save(otpDTO);
        return ResponseEntity
            .created(new URI("/api/otps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /otps/:id} : Updates an existing otp.
     *
     * @param id the id of the otpDTO to save.
     * @param otpDTO the otpDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otpDTO,
     * or with status {@code 400 (Bad Request)} if the otpDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the otpDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/otps/{id}")
    public ResponseEntity<OtpDTO> updateOtp(@PathVariable(value = "id", required = false) final Long id, @RequestBody OtpDTO otpDTO)
        throws URISyntaxException {
        log.debug("REST request to update Otp : {}, {}", id, otpDTO);
        if (otpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otpDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otpRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OtpDTO result = otpService.save(otpDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otpDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /otps/:id} : Partial updates given fields of an existing otp, field will ignore if it is null
     *
     * @param id the id of the otpDTO to save.
     * @param otpDTO the otpDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otpDTO,
     * or with status {@code 400 (Bad Request)} if the otpDTO is not valid,
     * or with status {@code 404 (Not Found)} if the otpDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the otpDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/otps/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OtpDTO> partialUpdateOtp(@PathVariable(value = "id", required = false) final Long id, @RequestBody OtpDTO otpDTO)
        throws URISyntaxException {
        log.debug("REST request to partial update Otp partially : {}, {}", id, otpDTO);
        if (otpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otpDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otpRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OtpDTO> result = otpService.partialUpdate(otpDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otpDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /otps} : get all the otps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of otps in body.
     */
    @GetMapping("/otps")
    public ResponseEntity<List<OtpDTO>> getAllOtps(Pageable pageable) {
        log.debug("REST request to get a page of Otps");
        Page<OtpDTO> page = otpService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /otps/:id} : get the "id" otp.
     *
     * @param id the id of the otpDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the otpDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/otps/{id}")
    public ResponseEntity<OtpDTO> getOtp(@PathVariable Long id) {
        log.debug("REST request to get Otp : {}", id);
        Optional<OtpDTO> otpDTO = otpService.findOne(id);
        return ResponseUtil.wrapOrNotFound(otpDTO);
    }

    /**
     * {@code DELETE  /otps/:id} : delete the "id" otp.
     *
     * @param id the id of the otpDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/otps/{id}")
    public ResponseEntity<Void> deleteOtp(@PathVariable Long id) {
        log.debug("REST request to delete Otp : {}", id);
        otpService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
