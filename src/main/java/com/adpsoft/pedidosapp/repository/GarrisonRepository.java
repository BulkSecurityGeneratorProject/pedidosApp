package com.adpsoft.pedidosapp.repository;

import com.adpsoft.pedidosapp.domain.Garrison;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Garrison entity.
 */
@SuppressWarnings("unused")
public interface GarrisonRepository extends JpaRepository<Garrison,Long> {

}
