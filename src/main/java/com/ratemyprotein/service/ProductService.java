package com.ratemyprotein.service;

import com.ratemyprotein.entity.Brand;
import com.ratemyprotein.entity.Flavor;
import com.ratemyprotein.entity.Product;
import com.ratemyprotein.entity.ProteinType;
import com.ratemyprotein.repository.BrandRepository;
import com.ratemyprotein.repository.FlavorRepository;
import com.ratemyprotein.repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final FlavorRepository flavorRepository;

    public ProductService(
            ProductRepository productRepository,
            BrandRepository brandRepository,
            FlavorRepository flavorRepository
    ) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.flavorRepository = flavorRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> getActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<Product> searchProducts(
            String search,
            Long brandId,
            Long flavorId,
            ProteinType proteinType
    ) {
        String normalizedSearch =
                search == null ? "" : search.trim();

        return productRepository.searchActiveProducts(
                normalizedSearch,
                brandId,
                flavorId,
                proteinType
        );
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found"
            );
        }

        return product;
    }

    @Transactional(readOnly = true)
    public List<Brand> getAllBrands() {
        return brandRepository.findAll(
                Sort.by(
                        Sort.Direction.ASC,
                        "name"
                )
        );
    }

    @Transactional(readOnly = true)
    public List<Flavor> getAllFlavors() {
        return flavorRepository.findAll(
                Sort.by(
                        Sort.Direction.ASC,
                        "name"
                )
        );
    }
}