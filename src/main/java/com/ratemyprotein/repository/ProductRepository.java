package com.ratemyprotein.repository;

import com.ratemyprotein.entity.Product;
import com.ratemyprotein.entity.ProteinType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = {"brand", "flavor"})
    List<Product> findByActiveTrue();

    @Override
    @EntityGraph(attributePaths = {"brand", "flavor"})
    Optional<Product> findById(Long id);

    @EntityGraph(attributePaths = {"brand", "flavor"})
    @Query("""
            SELECT p
            FROM Product p
            WHERE p.active = true

              AND (
                    :search = ''
                    OR LOWER(p.name) LIKE LOWER(
                        CONCAT('%', :search, '%')
                    )
                    OR LOWER(p.brand.name) LIKE LOWER(
                        CONCAT('%', :search, '%')
                    )
                    OR LOWER(p.flavor.name) LIKE LOWER(
                        CONCAT('%', :search, '%')
                    )
              )

              AND (
                    :brandId IS NULL
                    OR p.brand.id = :brandId
              )

              AND (
                    :flavorId IS NULL
                    OR p.flavor.id = :flavorId
              )

              AND (
                    :proteinType IS NULL
                    OR p.proteinType = :proteinType
              )

            ORDER BY
                p.brand.name,
                p.name,
                p.flavor.name
            """)
    List<Product> searchActiveProducts(
            @Param("search") String search,
            @Param("brandId") Long brandId,
            @Param("flavorId") Long flavorId,
            @Param("proteinType") ProteinType proteinType
    );

    /*
     * Add the new pending-submission methods here,
     * after searchActiveProducts().
     */

    @EntityGraph(attributePaths = {
            "brand",
            "flavor",
            "submittedBy"
    })
    List<Product>
    findByActiveFalseAndSubmittedByIsNotNullOrderBySubmittedAtAsc();

    @EntityGraph(attributePaths = {
            "brand",
            "flavor",
            "submittedBy"
    })
    @Query("""
            SELECT p
            FROM Product p
            WHERE p.id = :id
              AND p.active = false
              AND p.submittedBy IS NOT NULL
            """)
    Optional<Product> findPendingSubmissionById(
            @Param("id") Long id
    );

    boolean existsByBrandIdAndNameIgnoreCaseAndFlavorId(
            Long brandId,
            String name,
            Long flavorId
    );
}