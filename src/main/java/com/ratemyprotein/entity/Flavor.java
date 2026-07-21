package com.ratemyprotein.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "flavors",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_flavor_name",
                        columnNames = "name"
                )
        }
)
public class Flavor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "flavor_family", length = 100)
    private String flavorFamily;

    public Flavor() {
    }

    public Flavor(String name, String flavorFamily) {
        this.name = name;
        this.flavorFamily = flavorFamily;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFlavorFamily() {
        return flavorFamily;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFlavorFamily(String flavorFamily) {
        this.flavorFamily = flavorFamily;
    }
}