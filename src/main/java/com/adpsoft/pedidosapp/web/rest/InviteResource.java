package com.adpsoft.pedidosapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.adpsoft.pedidosapp.domain.Invite;

import com.adpsoft.pedidosapp.repository.InviteRepository;
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
 * REST controller for managing Invite.
 */
@RestController
@RequestMapping("/api")
public class InviteResource {

    private final Logger log = LoggerFactory.getLogger(InviteResource.class);
        
    @Inject
    private InviteRepository inviteRepository;

    /**
     * POST  /invites : Create a new invite.
     *
     * @param invite the invite to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invite, or with status 400 (Bad Request) if the invite has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/invites",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invite> createInvite(@Valid @RequestBody Invite invite) throws URISyntaxException {
        log.debug("REST request to save Invite : {}", invite);
        if (invite.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("invite", "idexists", "A new invite cannot already have an ID")).body(null);
        }
        Invite result = inviteRepository.save(invite);
        return ResponseEntity.created(new URI("/api/invites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("invite", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invites : Updates an existing invite.
     *
     * @param invite the invite to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invite,
     * or with status 400 (Bad Request) if the invite is not valid,
     * or with status 500 (Internal Server Error) if the invite couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/invites",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invite> updateInvite(@Valid @RequestBody Invite invite) throws URISyntaxException {
        log.debug("REST request to update Invite : {}", invite);
        if (invite.getId() == null) {
            return createInvite(invite);
        }
        Invite result = inviteRepository.save(invite);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("invite", invite.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invites : get all the invites.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of invites in body
     */
    @RequestMapping(value = "/invites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Invite> getAllInvites() {
        log.debug("REST request to get all Invites");
        List<Invite> invites = inviteRepository.findAll();
        return invites;
    }

    /**
     * GET  /invites/:id : get the "id" invite.
     *
     * @param id the id of the invite to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invite, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/invites/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invite> getInvite(@PathVariable Long id) {
        log.debug("REST request to get Invite : {}", id);
        Invite invite = inviteRepository.findOne(id);
        return Optional.ofNullable(invite)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /invites/:id : delete the "id" invite.
     *
     * @param id the id of the invite to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/invites/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInvite(@PathVariable Long id) {
        log.debug("REST request to delete Invite : {}", id);
        inviteRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("invite", id.toString())).build();
    }

}
