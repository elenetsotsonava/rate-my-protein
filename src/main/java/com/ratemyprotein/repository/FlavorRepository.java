package com.ratemyprotein.repository;

import com.ratemyprotein.entity.Flavor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlavorRepository extends JpaRepository<Flavor, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Flavor> findByNameIgnoreCase(String name);
}