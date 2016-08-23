package com.adpsoft.pedidosapp.repository;

import com.adpsoft.pedidosapp.domain.Delicatessen;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Delicatessen entity.
 */
@SuppressWarnings("unused")
public interface DelicatessenRepository extends JpaRepository<Delicatessen,Long> {

}
