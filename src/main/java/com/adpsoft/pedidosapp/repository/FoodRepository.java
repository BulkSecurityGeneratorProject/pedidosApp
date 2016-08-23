package com.adpsoft.pedidosapp.repository;

import com.adpsoft.pedidosapp.domain.Food;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Food entity.
 */
@SuppressWarnings("unused")
public interface FoodRepository extends JpaRepository<Food,Long> {

    @Query("select distinct food from Food food left join fetch food.garrisons")
    List<Food> findAllWithEagerRelationships();

    @Query("select food from Food food left join fetch food.garrisons where food.id =:id")
    Food findOneWithEagerRelationships(@Param("id") Long id);

}
