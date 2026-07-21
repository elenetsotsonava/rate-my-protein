package com.ratemyprotein.repository;

import com.ratemyprotein.entity.Product;
import com.ratemyprotein.entity.ProteinType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = {"brand", "flavor"})
    List<Product> findByActiveTrue();

    @Override
    @EntityGraph(attributePaths = {"brand", "flavor"})
    Optional<Product> findById(Long id);

    List<Product> findByBrandIdAndActiveTrue(Long brandId);

    List<Product> findByFlavorIdAndActiveTrue(Long flavorId);

    List<Product> findByProteinTypeAndActiveTrue(ProteinType proteinType);

    boolean existsByBrandIdAndNameIgnoreCaseAndFlavorId(
            Long brandId,
            String name,
            Long flavorId
    );
}