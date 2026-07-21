package com.ratemyprotein.repository;

import com.ratemyprotein.entity.Product;
import com.ratemyprotein.entity.ProteinType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = {"brand", "flavor"})
    List<Product> findByActiveTrue();

    List<Product> findByBrandIdAndActiveTrue(Long brandId);

    List<Product> findByFlavorIdAndActiveTrue(Long flavorId);

    List<Product> findByProteinTypeAndActiveTrue(ProteinType proteinType);

    boolean existsByBrandIdAndNameIgnoreCaseAndFlavorId(
            Long brandId,
            String name,
            Long flavorId
    );
}