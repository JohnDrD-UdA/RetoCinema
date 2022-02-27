package com.cinema.web.rest;

import com.cinema.domain.Booking;
import com.cinema.domain.Chair;
import com.cinema.domain.Moviefunction;
import com.cinema.repository.ChairRepository;
import com.cinema.service.ChairService;
import com.cinema.service.dto.ChairDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cinema.domain.Chair}.
 */
@RestController
@RequestMapping("/api")
public class ChairResource {

    private final Logger log = LoggerFactory.getLogger(ChairResource.class);

    private static final String ENTITY_NAME = "chair";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChairService chairService;

    private final ChairRepository chairRepository;

    public ChairResource(ChairService chairService, ChairRepository chairRepository) {
        this.chairService = chairService;
        this.chairRepository = chairRepository;
    }

    /**
     * {@code POST  /chairs} : Create a new chair.
     *
     * @param chairDTO the chairDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chairDTO, or with status {@code 400 (Bad Request)} if the chair has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chairs")
    public ResponseEntity<ChairDTO> createChair(@Valid @RequestBody ChairDTO chairDTO) throws URISyntaxException {
        log.debug("REST request to save Chair : {}", chairDTO);
        if (chairDTO.getId() != null) {
            throw new BadRequestAlertException("A new chair cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChairDTO result = chairService.save(chairDTO);
        return ResponseEntity
            .created(new URI("/api/chairs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chairs/:id} : Updates an existing chair.
     *
     * @param id the id of the chairDTO to save.
     * @param chairDTO the chairDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chairDTO,
     * or with status {@code 400 (Bad Request)} if the chairDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chairDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chairs/{id}")
    public ResponseEntity<ChairDTO> updateChair(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChairDTO chairDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Chair : {}, {}", id, chairDTO);
        if (chairDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chairDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chairRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChairDTO result = chairService.save(chairDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chairDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chairs/:id} : Partial updates given fields of an existing chair, field will ignore if it is null
     *
     * @param id the id of the chairDTO to save.
     * @param chairDTO the chairDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chairDTO,
     * or with status {@code 400 (Bad Request)} if the chairDTO is not valid,
     * or with status {@code 404 (Not Found)} if the chairDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the chairDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chairs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChairDTO> partialUpdateChair(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChairDTO chairDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Chair partially : {}, {}", id, chairDTO);
        if (chairDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chairDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chairRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChairDTO> result = chairService.partialUpdate(chairDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chairDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /chairs} : get all the chairs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chairs in body.
     */
    @GetMapping("/chairs")
    public ResponseEntity<List<ChairDTO>> getAllChairs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Chairs");
        Page<ChairDTO> page = chairService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chairs/:id} : get the "id" chair.
     *
     * @param id the id of the chairDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chairDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chairs/{id}")
    public ResponseEntity<ChairDTO> getChair(@PathVariable Long id) {
        log.debug("REST request to get Chair : {}", id);
        Optional<ChairDTO> chairDTO = chairService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chairDTO);
    }

    /**
     * {@code DELETE  /chairs/:id} : delete the "id" chair.
     *
     * @param id the id of the chairDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chairs/{id}")
    public ResponseEntity<Void> deleteChair(@PathVariable Long id) {
        log.debug("REST request to delete Chair : {}", id);
        chairService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
    @GetMapping("/chairs/getByFunctionM/{fID}")
    
    public List<List<Chair>> findChairsByFunction(@PathVariable("fID") Long fID){
        Moviefunction moviefunction= new Moviefunction();
        moviefunction.setId(fID);
        try {
            return chairService.getByMovieFunction(moviefunction);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
    @GetMapping("/chairs/getByBooking/{bID}")
    public List<ChairDTO> findChairsByBooking(@PathVariable("bID") Long bID){
        Booking booking= new Booking();
        booking.setId(bID);
        try {
            return chairService.getByBooking(booking);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    @PostMapping("/chairs/freeChairs")
    public boolean freeChairs(@RequestBody List<Chair> chairs){
        return chairService.freeChair(chairs);
    }
}
