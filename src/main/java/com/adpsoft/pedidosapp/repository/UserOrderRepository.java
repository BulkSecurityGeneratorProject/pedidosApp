package com.adpsoft.pedidosapp.repository;

import com.adpsoft.pedidosapp.domain.UserOrder;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserOrder entity.
 */
@SuppressWarnings("unused")
public interface UserOrderRepository extends JpaRepository<UserOrder,Long> {

    @Query("select userOrder from UserOrder userOrder where userOrder.userId.login = ?#{principal.username}")
    List<UserOrder> findByUserIdIsCurrentUser();

}
