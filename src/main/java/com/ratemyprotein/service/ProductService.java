package com.ratemyprotein.service;

import com.ratemyprotein.dto.ProductEditRequest;
import com.ratemyprotein.dto.ProductSubmissionRequest;
import com.ratemyprotein.entity.AppUser;
import com.ratemyprotein.entity.Brand;
import com.ratemyprotein.entity.Flavor;
import com.ratemyprotein.entity.Product;
import com.ratemyprotein.entity.ProteinType;
import com.ratemyprotein.repository.BrandRepository;
import com.ratemyprotein.repository.FlavorRepository;
import com.ratemyprotein.repository.ProductRepository;
import com.ratemyprotein.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final FlavorRepository flavorRepository;
    private final UserRepository userRepository;

    public ProductService(
            ProductRepository productRepository,
            BrandRepository brandRepository,
            FlavorRepository flavorRepository,
            UserRepository userRepository
    ) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.flavorRepository = flavorRepository;
        this.userRepository = userRepository;
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
        return productRepository.findById(id)
                .filter(product ->
                        Boolean.TRUE.equals(product.getActive())
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found."
                        )
                );
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

    /**
     * Creates a product submitted by a logged-in user.
     *
     * The product is inactive until an administrator approves it.
     */
    @Transactional
    public Product submitProduct(
            ProductSubmissionRequest request,
            String userEmail
    ) {
        AppUser submittingUser = userRepository
                .findByEmailIgnoreCase(userEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Logged-in user was not found."
                        )
                );

        Brand brand = brandRepository
                .findById(request.getBrandId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Selected brand was not found."
                        )
                );

        Flavor flavor = flavorRepository
                .findById(request.getFlavorId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Selected flavor was not found."
                        )
                );

        Product product = new Product();

        product.setName(request.getName().trim());
        product.setBrand(brand);
        product.setFlavor(flavor);
        product.setProteinType(request.getProteinType());

        product.setDescription(
                normalizeOptionalText(request.getDescription())
        );

        product.setProteinPerServing(
                request.getProteinPerServing()
        );

        product.setCalories(request.getCalories());
        product.setPrice(request.getPrice());

        product.setImageUrl(
                normalizeOptionalText(request.getImageUrl())
        );

        /*
         * User submissions are not immediately public.
         */
        product.setActive(false);

        /*
         * RateMyProtein currently functions as a review platform,
         * so users cannot define shop inventory.
         */
        product.setStockQuantity(0);

        product.setSubmittedBy(submittingUser);
        product.setSubmittedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    private String normalizeOptionalText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    @Transactional(readOnly = true)
    public List<Product> getPendingProductSubmissions() {
        return productRepository
                .findByActiveFalseAndSubmittedByIsNotNullAndApprovedAtIsNullOrderBySubmittedAtAsc();
    }

    @Transactional
    public Product approveProductSubmission(Long productId) {

        Product product = productRepository
                .findPendingSubmissionById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pending product submission was not found."
                        )
                );

        product.setActive(true);
        product.setApprovedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    @Transactional
    public void rejectProductSubmission(Long productId) {

        Product product = productRepository
                .findPendingSubmissionById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pending product submission was not found."
                        )
                );

        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getManagedProducts() {
        return productRepository.findAllManagedProducts();
    }

    @Transactional
    public Product activateProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product was not found."
                        )
                );

        if (isPendingSubmission(product)) {
            throw new IllegalArgumentException(
                    "Pending submissions must be approved from the pending-products page."
            );
        }

        product.setActive(true);

        return productRepository.save(product);
    }

    @Transactional
    public Product deactivateProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product was not found."
                        )
                );

        if (isPendingSubmission(product)) {
            throw new IllegalArgumentException(
                    "This product is still waiting for approval."
            );
        }

        product.setActive(false);

        return productRepository.save(product);
    }

    private boolean isPendingSubmission(Product product) {
        return product.getSubmittedBy() != null
                && product.getApprovedAt() == null;
    }

    @Transactional(readOnly = true)
    public ProductEditRequest getProductEditRequest(Long productId) {

        Product product = getManagedProductForAdmin(productId);

        ProductEditRequest request = new ProductEditRequest();

        request.setName(product.getName());
        request.setBrandId(product.getBrand().getId());
        request.setFlavorId(product.getFlavor().getId());
        request.setProteinType(product.getProteinType());
        request.setDescription(product.getDescription());
        request.setProteinPerServing(
                product.getProteinPerServing()
        );
        request.setCalories(product.getCalories());
        request.setPrice(product.getPrice());
        request.setStockQuantity(product.getStockQuantity());
        request.setImageUrl(product.getImageUrl());

        return request;
    }

    @Transactional
    public Product updateProduct(
            Long productId,
            ProductEditRequest request
    ) {
        Product product = getManagedProductForAdmin(productId);

        Brand brand = brandRepository
                .findById(request.getBrandId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Selected brand was not found."
                        )
                );

        Flavor flavor = flavorRepository
                .findById(request.getFlavorId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Selected flavor was not found."
                        )
                );

        product.setName(request.getName().trim());
        product.setBrand(brand);
        product.setFlavor(flavor);
        product.setProteinType(request.getProteinType());

        product.setDescription(
                normalizeOptionalText(request.getDescription())
        );

        product.setProteinPerServing(
                request.getProteinPerServing()
        );

        product.setCalories(request.getCalories());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());

        product.setImageUrl(
                normalizeOptionalText(request.getImageUrl())
        );

        return productRepository.save(product);
    }

    private Product getManagedProductForAdmin(Long productId) {

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product was not found."
                        )
                );

        if (isPendingSubmission(product)) {
            throw new IllegalArgumentException(
                    "Pending submissions must be approved before editing."
            );
        }

        return product;
    }
}