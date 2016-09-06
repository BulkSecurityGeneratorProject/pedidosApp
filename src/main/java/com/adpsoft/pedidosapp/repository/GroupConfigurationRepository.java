package com.adpsoft.pedidosapp.repository;

import com.adpsoft.pedidosapp.domain.GroupConfiguration;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the GroupConfiguration entity.
 */
@SuppressWarnings("unused")
public interface GroupConfigurationRepository extends JpaRepository<GroupConfiguration,Long> {

    @Query("select distinct groupConfiguration from GroupConfiguration groupConfiguration left join fetch groupConfiguration.orderDays")
    List<GroupConfiguration> findAllWithEagerRelationships();

    @Query("select groupConfiguration from GroupConfiguration groupConfiguration left join fetch groupConfiguration.orderDays where groupConfiguration.id =:id")
    GroupConfiguration findOneWithEagerRelationships(@Param("id") Long id);

}
