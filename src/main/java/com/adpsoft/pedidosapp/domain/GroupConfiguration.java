package com.adpsoft.pedidosapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A GroupConfiguration.
 */
@Entity
@Table(name = "group_configuration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GroupConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "weekly")
    private Boolean weekly;

    @Min(value = 0)
    @Max(value = 23)
    @Column(name = "order_opening_hour")
    private Integer orderOpeningHour;

    @Min(value = 0)
    @Max(value = 23)
    @Column(name = "order_closing_hour")
    private Integer orderClosingHour;

    @Column(name = "cc_order_email")
    private String ccOrderEmail;

    @Column(name = "cc_cancel_email")
    private String ccCancelEmail;

    @Column(name = "order_email_body")
    private String orderEmailBody;

    @Column(name = "cancel_email_body")
    private String cancelEmailBody;

    @Column(name = "days_for_cancellation")
    private Integer daysForCancellation;

    @Min(value = 0)
    @Max(value = 23)
    @Column(name = "hours_for_cancelation")
    private Integer hoursForCancelation;

    @OneToOne
    @JoinColumn(unique = true)
    private Day orderOpeningDay;

    @OneToOne
    @JoinColumn(unique = true)
    private Day orderClosingDay;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "group_configuration_order_days",
               joinColumns = @JoinColumn(name="group_configurations_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="order_days_id", referencedColumnName="ID"))
    private Set<Day> orderDays = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isWeekly() {
        return weekly;
    }

    public GroupConfiguration weekly(Boolean weekly) {
        this.weekly = weekly;
        return this;
    }

    public void setWeekly(Boolean weekly) {
        this.weekly = weekly;
    }

    public Integer getOrderOpeningHour() {
        return orderOpeningHour;
    }

    public GroupConfiguration orderOpeningHour(Integer orderOpeningHour) {
        this.orderOpeningHour = orderOpeningHour;
        return this;
    }

    public void setOrderOpeningHour(Integer orderOpeningHour) {
        this.orderOpeningHour = orderOpeningHour;
    }

    public Integer getOrderClosingHour() {
        return orderClosingHour;
    }

    public GroupConfiguration orderClosingHour(Integer orderClosingHour) {
        this.orderClosingHour = orderClosingHour;
        return this;
    }

    public void setOrderClosingHour(Integer orderClosingHour) {
        this.orderClosingHour = orderClosingHour;
    }

    public String getCcOrderEmail() {
        return ccOrderEmail;
    }

    public GroupConfiguration ccOrderEmail(String ccOrderEmail) {
        this.ccOrderEmail = ccOrderEmail;
        return this;
    }

    public void setCcOrderEmail(String ccOrderEmail) {
        this.ccOrderEmail = ccOrderEmail;
    }

    public String getCcCancelEmail() {
        return ccCancelEmail;
    }

    public GroupConfiguration ccCancelEmail(String ccCancelEmail) {
        this.ccCancelEmail = ccCancelEmail;
        return this;
    }

    public void setCcCancelEmail(String ccCancelEmail) {
        this.ccCancelEmail = ccCancelEmail;
    }

    public String getOrderEmailBody() {
        return orderEmailBody;
    }

    public GroupConfiguration orderEmailBody(String orderEmailBody) {
        this.orderEmailBody = orderEmailBody;
        return this;
    }

    public void setOrderEmailBody(String orderEmailBody) {
        this.orderEmailBody = orderEmailBody;
    }

    public String getCancelEmailBody() {
        return cancelEmailBody;
    }

    public GroupConfiguration cancelEmailBody(String cancelEmailBody) {
        this.cancelEmailBody = cancelEmailBody;
        return this;
    }

    public void setCancelEmailBody(String cancelEmailBody) {
        this.cancelEmailBody = cancelEmailBody;
    }

    public Integer getDaysForCancellation() {
        return daysForCancellation;
    }

    public GroupConfiguration daysForCancellation(Integer daysForCancellation) {
        this.daysForCancellation = daysForCancellation;
        return this;
    }

    public void setDaysForCancellation(Integer daysForCancellation) {
        this.daysForCancellation = daysForCancellation;
    }

    public Integer getHoursForCancelation() {
        return hoursForCancelation;
    }

    public GroupConfiguration hoursForCancelation(Integer hoursForCancelation) {
        this.hoursForCancelation = hoursForCancelation;
        return this;
    }

    public void setHoursForCancelation(Integer hoursForCancelation) {
        this.hoursForCancelation = hoursForCancelation;
    }

    public Day getOrderOpeningDay() {
        return orderOpeningDay;
    }

    public GroupConfiguration orderOpeningDay(Day day) {
        this.orderOpeningDay = day;
        return this;
    }

    public void setOrderOpeningDay(Day day) {
        this.orderOpeningDay = day;
    }

    public Day getOrderClosingDay() {
        return orderClosingDay;
    }

    public GroupConfiguration orderClosingDay(Day day) {
        this.orderClosingDay = day;
        return this;
    }

    public void setOrderClosingDay(Day day) {
        this.orderClosingDay = day;
    }

    public Set<Day> getOrderDays() {
        return orderDays;
    }

    public GroupConfiguration orderDays(Set<Day> days) {
        this.orderDays = days;
        return this;
    }

    public GroupConfiguration addDay(Day day) {
        orderDays.add(day);
        return this;
    }

    public GroupConfiguration removeDay(Day day) {
        orderDays.remove(day);
        return this;
    }

    public void setOrderDays(Set<Day> days) {
        this.orderDays = days;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupConfiguration groupConfiguration = (GroupConfiguration) o;
        if(groupConfiguration.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, groupConfiguration.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "GroupConfiguration{" +
            "id=" + id +
            ", weekly='" + weekly + "'" +
            ", orderOpeningHour='" + orderOpeningHour + "'" +
            ", orderClosingHour='" + orderClosingHour + "'" +
            ", ccOrderEmail='" + ccOrderEmail + "'" +
            ", ccCancelEmail='" + ccCancelEmail + "'" +
            ", orderEmailBody='" + orderEmailBody + "'" +
            ", cancelEmailBody='" + cancelEmailBody + "'" +
            ", daysForCancellation='" + daysForCancellation + "'" +
            ", hoursForCancelation='" + hoursForCancelation + "'" +
            '}';
    }
}
