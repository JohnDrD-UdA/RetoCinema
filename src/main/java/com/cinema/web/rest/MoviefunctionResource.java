package com.cinema.web.rest;

import com.cinema.domain.Movie;
import com.cinema.domain.Moviefunction;
import com.cinema.repository.MoviefunctionRepository;
import com.cinema.service.MoviefunctionService;
import com.cinema.service.dto.MoviefunctionDTO;
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
 * REST controller for managing {@link com.cinema.domain.Moviefunction}.
 */
@RestController
@RequestMapping("/api")
public class MoviefunctionResource {

    private final Logger log = LoggerFactory.getLogger(MoviefunctionResource.class);

    private static final String ENTITY_NAME = "moviefunction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoviefunctionService moviefunctionService;

    private final MoviefunctionRepository moviefunctionRepository;

    public MoviefunctionResource(MoviefunctionService moviefunctionService, MoviefunctionRepository moviefunctionRepository) {
        this.moviefunctionService = moviefunctionService;
        this.moviefunctionRepository = moviefunctionRepository;
    }

    /**
     * {@code POST  /moviefunctions} : Create a new moviefunction.
     *
     * @param moviefunctionDTO the moviefunctionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moviefunctionDTO, or with status {@code 400 (Bad Request)} if the moviefunction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/moviefunctions")
    public ResponseEntity<MoviefunctionDTO> createMoviefunction(@Valid @RequestBody MoviefunctionDTO moviefunctionDTO)
        throws URISyntaxException {
        log.debug("REST request to save Moviefunction : {}", moviefunctionDTO);
        if (moviefunctionDTO.getId() != null) {
            throw new BadRequestAlertException("A new moviefunction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MoviefunctionDTO result = moviefunctionService.save(moviefunctionDTO);
        return ResponseEntity
            .created(new URI("/api/moviefunctions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /moviefunctions/:id} : Updates an existing moviefunction.
     *
     * @param id the id of the moviefunctionDTO to save.
     * @param moviefunctionDTO the moviefunctionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moviefunctionDTO,
     * or with status {@code 400 (Bad Request)} if the moviefunctionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moviefunctionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/moviefunctions/{id}")
    public ResponseEntity<MoviefunctionDTO> updateMoviefunction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MoviefunctionDTO moviefunctionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Moviefunction : {}, {}", id, moviefunctionDTO);
        if (moviefunctionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moviefunctionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moviefunctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MoviefunctionDTO result = moviefunctionService.save(moviefunctionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moviefunctionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /moviefunctions/:id} : Partial updates given fields of an existing moviefunction, field will ignore if it is null
     *
     * @param id the id of the moviefunctionDTO to save.
     * @param moviefunctionDTO the moviefunctionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moviefunctionDTO,
     * or with status {@code 400 (Bad Request)} if the moviefunctionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moviefunctionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moviefunctionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/moviefunctions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MoviefunctionDTO> partialUpdateMoviefunction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MoviefunctionDTO moviefunctionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Moviefunction partially : {}, {}", id, moviefunctionDTO);
        if (moviefunctionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moviefunctionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moviefunctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoviefunctionDTO> result = moviefunctionService.partialUpdate(moviefunctionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moviefunctionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /moviefunctions} : get all the moviefunctions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moviefunctions in body.
     */
    @GetMapping("/moviefunctions")
    public ResponseEntity<List<MoviefunctionDTO>> getAllMoviefunctions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Moviefunctions");
        Page<MoviefunctionDTO> page = moviefunctionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /moviefunctions/:id} : get the "id" moviefunction.
     *
     * @param id the id of the moviefunctionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moviefunctionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/moviefunctions/{id}")
    public ResponseEntity<MoviefunctionDTO> getMoviefunction(@PathVariable Long id) {
        log.debug("REST request to get Moviefunction : {}", id);
        Optional<MoviefunctionDTO> moviefunctionDTO = moviefunctionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moviefunctionDTO);
    }

    /**
     * {@code DELETE  /moviefunctions/:id} : delete the "id" moviefunction.
     *
     * @param id the id of the moviefunctionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/moviefunctions/{id}")
    public ResponseEntity<Void> deleteMoviefunction(@PathVariable Long id) {
        log.debug("REST request to delete Moviefunction : {}", id);
        moviefunctionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
    @GetMapping("/moviefunctions/getbyMovie/{movieID}")

    public List<Moviefunction> getByMovie(@PathVariable("movieID") Long movieID){
        Movie movie=new Movie();
        movie.setId(movieID);
        try{return moviefunctionService.getAllByMovie(movie);}
        catch(Exception e){
            System.out.print(e.toString());
            return null;
        }
    }
}
