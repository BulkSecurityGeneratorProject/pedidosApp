package com.adpsoft.pedidosapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Delicatessen.
 */
@Entity
@Table(name = "delicatessen")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Delicatessen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "custom", nullable = false)
    private Boolean custom;

    @ManyToOne
    private UserGroup userGroupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Delicatessen name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public Delicatessen address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public Delicatessen phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public Delicatessen email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isCustom() {
        return custom;
    }

    public Delicatessen custom(Boolean custom) {
        this.custom = custom;
        return this;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public UserGroup getUserGroupId() {
        return userGroupId;
    }

    public Delicatessen userGroupId(UserGroup userGroup) {
        this.userGroupId = userGroup;
        return this;
    }

    public void setUserGroupId(UserGroup userGroup) {
        this.userGroupId = userGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Delicatessen delicatessen = (Delicatessen) o;
        if(delicatessen.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, delicatessen.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Delicatessen{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", address='" + address + "'" +
            ", phone='" + phone + "'" +
            ", email='" + email + "'" +
            ", custom='" + custom + "'" +
            '}';
    }
}
