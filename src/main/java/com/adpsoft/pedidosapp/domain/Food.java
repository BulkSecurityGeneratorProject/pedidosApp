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
 * A Food.
 */
@Entity
@Table(name = "food")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Food implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "prefix")
    private String prefix;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    private Delicatessen delicatessenId;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "food_garrison",
               joinColumns = @JoinColumn(name="foods_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="garrisons_id", referencedColumnName="ID"))
    private Set<Garrison> garrisons = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public Food prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public Food name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Delicatessen getDelicatessenId() {
        return delicatessenId;
    }

    public Food delicatessenId(Delicatessen delicatessen) {
        this.delicatessenId = delicatessen;
        return this;
    }

    public void setDelicatessenId(Delicatessen delicatessen) {
        this.delicatessenId = delicatessen;
    }

    public Set<Garrison> getGarrisons() {
        return garrisons;
    }

    public Food garrisons(Set<Garrison> garrisons) {
        this.garrisons = garrisons;
        return this;
    }

    public Food addGarrison(Garrison garrison) {
        garrisons.add(garrison);
        return this;
    }

    public Food removeGarrison(Garrison garrison) {
        garrisons.remove(garrison);
        return this;
    }

    public void setGarrisons(Set<Garrison> garrisons) {
        this.garrisons = garrisons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Food food = (Food) o;
        if(food.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, food.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Food{" +
            "id=" + id +
            ", prefix='" + prefix + "'" +
            ", name='" + name + "'" +
            '}';
    }
}
