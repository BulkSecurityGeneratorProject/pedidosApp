package com.adpsoft.pedidosapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.adpsoft.pedidosapp.domain.Delicatessen;

import com.adpsoft.pedidosapp.repository.DelicatessenRepository;
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
 * REST controller for managing Delicatessen.
 */
@RestController
@RequestMapping("/api")
public class DelicatessenResource {

    private final Logger log = LoggerFactory.getLogger(DelicatessenResource.class);
        
    @Inject
    private DelicatessenRepository delicatessenRepository;

    /**
     * POST  /delicatessens : Create a new delicatessen.
     *
     * @param delicatessen the delicatessen to create
     * @return the ResponseEntity with status 201 (Created) and with body the new delicatessen, or with status 400 (Bad Request) if the delicatessen has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/delicatessens",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Delicatessen> createDelicatessen(@Valid @RequestBody Delicatessen delicatessen) throws URISyntaxException {
        log.debug("REST request to save Delicatessen : {}", delicatessen);
        if (delicatessen.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("delicatessen", "idexists", "A new delicatessen cannot already have an ID")).body(null);
        }
        Delicatessen result = delicatessenRepository.save(delicatessen);
        return ResponseEntity.created(new URI("/api/delicatessens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("delicatessen", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /delicatessens : Updates an existing delicatessen.
     *
     * @param delicatessen the delicatessen to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated delicatessen,
     * or with status 400 (Bad Request) if the delicatessen is not valid,
     * or with status 500 (Internal Server Error) if the delicatessen couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/delicatessens",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Delicatessen> updateDelicatessen(@Valid @RequestBody Delicatessen delicatessen) throws URISyntaxException {
        log.debug("REST request to update Delicatessen : {}", delicatessen);
        if (delicatessen.getId() == null) {
            return createDelicatessen(delicatessen);
        }
        Delicatessen result = delicatessenRepository.save(delicatessen);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("delicatessen", delicatessen.getId().toString()))
            .body(result);
    }

    /**
     * GET  /delicatessens : get all the delicatessens.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of delicatessens in body
     */
    @RequestMapping(value = "/delicatessens",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Delicatessen> getAllDelicatessens() {
        log.debug("REST request to get all Delicatessens");
        List<Delicatessen> delicatessens = delicatessenRepository.findAll();
        return delicatessens;
    }

    /**
     * GET  /delicatessens/:id : get the "id" delicatessen.
     *
     * @param id the id of the delicatessen to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the delicatessen, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/delicatessens/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Delicatessen> getDelicatessen(@PathVariable Long id) {
        log.debug("REST request to get Delicatessen : {}", id);
        Delicatessen delicatessen = delicatessenRepository.findOne(id);
        return Optional.ofNullable(delicatessen)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /delicatessens/:id : delete the "id" delicatessen.
     *
     * @param id the id of the delicatessen to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/delicatessens/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDelicatessen(@PathVariable Long id) {
        log.debug("REST request to delete Delicatessen : {}", id);
        delicatessenRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("delicatessen", id.toString())).build();
    }

}
