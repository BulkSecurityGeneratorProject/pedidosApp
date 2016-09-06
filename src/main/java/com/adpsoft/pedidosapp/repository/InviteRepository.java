package com.adpsoft.pedidosapp.repository;

import com.adpsoft.pedidosapp.domain.Invite;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Invite entity.
 */
@SuppressWarnings("unused")
public interface InviteRepository extends JpaRepository<Invite,Long> {

    @Query("select invite from Invite invite where invite.host.login = ?#{principal.username}")
    List<Invite> findByHostIsCurrentUser();

}
