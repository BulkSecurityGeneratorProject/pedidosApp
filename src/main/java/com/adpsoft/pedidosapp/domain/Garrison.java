package com.adpsoft.pedidosapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Garrison.
 */
@Entity
@Table(name = "garrison")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Garrison implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    private Delicatessen delicatessenId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Garrison name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Delicatessen getDelicatessenId() {
        return delicatessenId;
    }

    public Garrison delicatessenId(Delicatessen delicatessen) {
        this.delicatessenId = delicatessen;
        return this;
    }

    public void setDelicatessenId(Delicatessen delicatessen) {
        this.delicatessenId = delicatessen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Garrison garrison = (Garrison) o;
        if(garrison.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, garrison.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Garrison{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
