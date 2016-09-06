package com.adpsoft.pedidosapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.adpsoft.pedidosapp.domain.GroupConfiguration;

import com.adpsoft.pedidosapp.repository.GroupConfigurationRepository;
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
 * REST controller for managing GroupConfiguration.
 */
@RestController
@RequestMapping("/api")
public class GroupConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(GroupConfigurationResource.class);
        
    @Inject
    private GroupConfigurationRepository groupConfigurationRepository;

    /**
     * POST  /group-configurations : Create a new groupConfiguration.
     *
     * @param groupConfiguration the groupConfiguration to create
     * @return the ResponseEntity with status 201 (Created) and with body the new groupConfiguration, or with status 400 (Bad Request) if the groupConfiguration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/group-configurations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GroupConfiguration> createGroupConfiguration(@Valid @RequestBody GroupConfiguration groupConfiguration) throws URISyntaxException {
        log.debug("REST request to save GroupConfiguration : {}", groupConfiguration);
        if (groupConfiguration.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("groupConfiguration", "idexists", "A new groupConfiguration cannot already have an ID")).body(null);
        }
        GroupConfiguration result = groupConfigurationRepository.save(groupConfiguration);
        return ResponseEntity.created(new URI("/api/group-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("groupConfiguration", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /group-configurations : Updates an existing groupConfiguration.
     *
     * @param groupConfiguration the groupConfiguration to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated groupConfiguration,
     * or with status 400 (Bad Request) if the groupConfiguration is not valid,
     * or with status 500 (Internal Server Error) if the groupConfiguration couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/group-configurations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GroupConfiguration> updateGroupConfiguration(@Valid @RequestBody GroupConfiguration groupConfiguration) throws URISyntaxException {
        log.debug("REST request to update GroupConfiguration : {}", groupConfiguration);
        if (groupConfiguration.getId() == null) {
            return createGroupConfiguration(groupConfiguration);
        }
        GroupConfiguration result = groupConfigurationRepository.save(groupConfiguration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("groupConfiguration", groupConfiguration.getId().toString()))
            .body(result);
    }

    /**
     * GET  /group-configurations : get all the groupConfigurations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of groupConfigurations in body
     */
    @RequestMapping(value = "/group-configurations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GroupConfiguration> getAllGroupConfigurations() {
        log.debug("REST request to get all GroupConfigurations");
        List<GroupConfiguration> groupConfigurations = groupConfigurationRepository.findAllWithEagerRelationships();
        return groupConfigurations;
    }

    /**
     * GET  /group-configurations/:id : get the "id" groupConfiguration.
     *
     * @param id the id of the groupConfiguration to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groupConfiguration, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/group-configurations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GroupConfiguration> getGroupConfiguration(@PathVariable Long id) {
        log.debug("REST request to get GroupConfiguration : {}", id);
        GroupConfiguration groupConfiguration = groupConfigurationRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(groupConfiguration)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /group-configurations/:id : delete the "id" groupConfiguration.
     *
     * @param id the id of the groupConfiguration to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/group-configurations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGroupConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete GroupConfiguration : {}", id);
        groupConfigurationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("groupConfiguration", id.toString())).build();
    }

}
