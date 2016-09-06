package com.adpsoft.pedidosapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.adpsoft.pedidosapp.domain.enumeration.OrderStatus;

/**
 * A UserOrder.
 */
@Entity
@Table(name = "user_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @ManyToOne
    private User userId;

    @ManyToOne
    private Food foodId;

    @ManyToOne
    private Garrison garrisonId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public UserOrder date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public UserOrder status(OrderStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public User getUserId() {
        return userId;
    }

    public UserOrder userId(User user) {
        this.userId = user;
        return this;
    }

    public void setUserId(User user) {
        this.userId = user;
    }

    public Food getFoodId() {
        return foodId;
    }

    public UserOrder foodId(Food food) {
        this.foodId = food;
        return this;
    }

    public void setFoodId(Food food) {
        this.foodId = food;
    }

    public Garrison getGarrisonId() {
        return garrisonId;
    }

    public UserOrder garrisonId(Garrison garrison) {
        this.garrisonId = garrison;
        return this;
    }

    public void setGarrisonId(Garrison garrison) {
        this.garrisonId = garrison;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserOrder userOrder = (UserOrder) o;
        if(userOrder.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userOrder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserOrder{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
