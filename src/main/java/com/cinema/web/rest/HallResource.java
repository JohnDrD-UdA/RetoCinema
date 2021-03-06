package com.cinema.web.rest;

import com.cinema.repository.HallRepository;
import com.cinema.service.HallService;
import com.cinema.service.dto.HallDTO;
import com.cinema.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
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
 * REST controller for managing {@link com.cinema.domain.Hall}.
 */
@RestController
@RequestMapping("/api")
public class HallResource {

    private final Logger log = LoggerFactory.getLogger(HallResource.class);

    private static final String ENTITY_NAME = "hall";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HallService hallService;

    private final HallRepository hallRepository;

    public HallResource(HallService hallService, HallRepository hallRepository) {
        this.hallService = hallService;
        this.hallRepository = hallRepository;
    }

    /**
     * {@code POST  /halls} : Create a new hall.
     *
     * @param hallDTO the hallDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hallDTO, or with status {@code 400 (Bad Request)} if the hall has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/halls")
    public ResponseEntity<HallDTO> createHall(@Valid @RequestBody HallDTO hallDTO) throws URISyntaxException {
        log.debug("REST request to save Hall : {}", hallDTO);
        if (hallDTO.getId() != null) {
            throw new BadRequestAlertException("A new hall cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HallDTO result = hallService.save(hallDTO);
        return ResponseEntity
            .created(new URI("/api/halls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /halls/:id} : Updates an existing hall.
     *
     * @param id the id of the hallDTO to save.
     * @param hallDTO the hallDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hallDTO,
     * or with status {@code 400 (Bad Request)} if the hallDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hallDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/halls/{id}")
    public ResponseEntity<HallDTO> updateHall(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HallDTO hallDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Hall : {}, {}", id, hallDTO);
        if (hallDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hallDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hallRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HallDTO result = hallService.save(hallDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hallDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /halls/:id} : Partial updates given fields of an existing hall, field will ignore if it is null
     *
     * @param id the id of the hallDTO to save.
     * @param hallDTO the hallDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hallDTO,
     * or with status {@code 400 (Bad Request)} if the hallDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hallDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hallDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/halls/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HallDTO> partialUpdateHall(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HallDTO hallDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Hall partially : {}, {}", id, hallDTO);
        if (hallDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hallDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hallRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HallDTO> result = hallService.partialUpdate(hallDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hallDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /halls} : get all the halls.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of halls in body.
     */
    @GetMapping("/halls")
    public ResponseEntity<List<HallDTO>> getAllHalls(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Halls");
        Page<HallDTO> page = hallService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /halls/:id} : get the "id" hall.
     *
     * @param id the id of the hallDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hallDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/halls/{id}")
    public ResponseEntity<HallDTO> getHall(@PathVariable Long id) {
        log.debug("REST request to get Hall : {}", id);
        Optional<HallDTO> hallDTO = hallService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hallDTO);
    }

    /**
     * {@code DELETE  /halls/:id} : delete the "id" hall.
     *
     * @param id the id of the hallDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/halls/{id}")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id) {
        log.debug("REST request to delete Hall : {}", id);
        hallService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
