package com.adpsoft.pedidosapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.adpsoft.pedidosapp.domain.Garrison;

import com.adpsoft.pedidosapp.repository.GarrisonRepository;
import com.adpsoft.pedidosapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Garrison.
 */
@RestController
@RequestMapping("/api")
public class GarrisonResource {

    private final Logger log = LoggerFactory.getLogger(GarrisonResource.class);
        
    @Inject
    private GarrisonRepository garrisonRepository;

    /**
     * POST  /garrisons : Create a new garrison.
     *
     * @param garrison the garrison to create
     * @return the ResponseEntity with status 201 (Created) and with body the new garrison, or with status 400 (Bad Request) if the garrison has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/garrisons",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Garrison> createGarrison(@Valid @RequestBody Garrison garrison) throws URISyntaxException {
        log.debug("REST request to save Garrison : {}", garrison);
        if (garrison.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("garrison", "idexists", "A new garrison cannot already have an ID")).body(null);
        }
        Garrison result = garrisonRepository.save(garrison);
        return ResponseEntity.created(new URI("/api/garrisons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("garrison", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /garrisons : Updates an existing garrison.
     *
     * @param garrison the garrison to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated garrison,
     * or with status 400 (Bad Request) if the garrison is not valid,
     * or with status 500 (Internal Server Error) if the garrison couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/garrisons",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Garrison> updateGarrison(@Valid @RequestBody Garrison garrison) throws URISyntaxException {
        log.debug("REST request to update Garrison : {}", garrison);
        if (garrison.getId() == null) {
            return createGarrison(garrison);
        }
        Garrison result = garrisonRepository.save(garrison);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("garrison", garrison.getId().toString()))
            .body(result);
    }

    /**
     * GET  /garrisons : get all the garrisons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of garrisons in body
     */
    @RequestMapping(value = "/garrisons",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Garrison> getAllGarrisons() {
        log.debug("REST request to get all Garrisons");
        List<Garrison> garrisons = garrisonRepository.findAll();
        return garrisons;
    }

    /**
     * GET  /garrisons/:id : get the "id" garrison.
     *
     * @param id the id of the garrison to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the garrison, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/garrisons/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Garrison> getGarrison(@PathVariable Long id) {
        log.debug("REST request to get Garrison : {}", id);
        Garrison garrison = garrisonRepository.findOne(id);
        return Optional.ofNullable(garrison)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /garrisons/:id : delete the "id" garrison.
     *
     * @param id the id of the garrison to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/garrisons/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGarrison(@PathVariable Long id) {
        log.debug("REST request to delete Garrison : {}", id);
        garrisonRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("garrison", id.toString())).build();
    }

}
