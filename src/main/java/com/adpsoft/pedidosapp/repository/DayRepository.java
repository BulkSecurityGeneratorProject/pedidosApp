package com.adpsoft.pedidosapp.repository;

import com.adpsoft.pedidosapp.domain.Day;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Day entity.
 */
@SuppressWarnings("unused")
public interface DayRepository extends JpaRepository<Day,Long> {

}
