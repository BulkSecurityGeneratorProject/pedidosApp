package com.adpsoft.pedidosapp.repository;

import com.adpsoft.pedidosapp.domain.UserGroup;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserGroup entity.
 */
@SuppressWarnings("unused")
public interface UserGroupRepository extends JpaRepository<UserGroup,Long> {

}
