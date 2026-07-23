package com.ratemyprotein.config;

import com.ratemyprotein.entity.Brand;
import com.ratemyprotein.entity.Flavor;
import com.ratemyprotein.entity.Product;
import com.ratemyprotein.entity.ProteinType;
import com.ratemyprotein.repository.BrandRepository;
import com.ratemyprotein.repository.FlavorRepository;
import com.ratemyprotein.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeData(
            BrandRepository brandRepository,
            FlavorRepository flavorRepository,
            ProductRepository productRepository
    ) {
        return args -> {

            Brand optimumNutrition = brandRepository
                    .findByNameIgnoreCase("Optimum Nutrition")
                    .orElseGet(() -> brandRepository.save(
                            new Brand(
                                    "Optimum Nutrition",
                                    "International sports nutrition brand."
                            )
                    ));

            Brand myProtein = brandRepository
                    .findByNameIgnoreCase("MyProtein")
                    .orElseGet(() -> brandRepository.save(
                            new Brand(
                                    "MyProtein",
                                    "Sports nutrition and supplement brand."
                            )
                    ));

            Brand biotechUsa = brandRepository
                    .findByNameIgnoreCase("BioTechUSA")
                    .orElseGet(() -> brandRepository.save(
                            new Brand(
                                    "BioTechUSA",
                                    "Protein and fitness supplement manufacturer."
                            )
                    ));

            Flavor chocolate = flavorRepository
                    .findByNameIgnoreCase("Double Chocolate")
                    .orElseGet(() -> flavorRepository.save(
                            new Flavor(
                                    "Double Chocolate",
                                    "Chocolate"
                            )
                    ));

            Flavor vanilla = flavorRepository
                    .findByNameIgnoreCase("Vanilla Ice Cream")
                    .orElseGet(() -> flavorRepository.save(
                            new Flavor(
                                    "Vanilla Ice Cream",
                                    "Vanilla"
                            )
                    ));

            Flavor strawberry = flavorRepository
                    .findByNameIgnoreCase("Strawberry Cream")
                    .orElseGet(() -> flavorRepository.save(
                            new Flavor(
                                    "Strawberry Cream",
                                    "Fruit"
                            )
                    ));

            Flavor saltedCaramel = flavorRepository
                    .findByNameIgnoreCase("Salted Caramel")
                    .orElseGet(() -> flavorRepository.save(
                            new Flavor(
                                    "Salted Caramel",
                                    "Caramel"
                            )
                    ));

            /*
             * Product prices are stored in USD.
             */

            addProductIfMissing(
                    productRepository,
                    optimumNutrition,
                    chocolate,
                    "Gold Standard 100% Whey",
                    ProteinType.WHEY,
                    "Whey protein powder with a rich chocolate flavor.",
                    24,
                    120,
                    new BigDecimal("39.99"),
                    20
            );

            addProductIfMissing(
                    productRepository,
                    optimumNutrition,
                    vanilla,
                    "Gold Standard 100% Whey",
                    ProteinType.WHEY,
                    "Whey protein powder with vanilla ice cream flavor.",
                    24,
                    120,
                    new BigDecimal("39.99"),
                    15
            );

            addProductIfMissing(
                    productRepository,
                    myProtein,
                    saltedCaramel,
                    "Impact Whey Protein",
                    ProteinType.WHEY,
                    "Whey protein with a sweet and creamy salted caramel flavor.",
                    21,
                    103,
                    new BigDecimal("29.99"),
                    30
            );

            addProductIfMissing(
                    productRepository,
                    biotechUsa,
                    strawberry,
                    "Iso Whey Zero",
                    ProteinType.WHEY_ISOLATE,
                    "Whey isolate with strawberry flavor and low sugar content.",
                    25,
                    110,
                    new BigDecimal("44.99"),
                    12
            );
        };
    }

    private void addProductIfMissing(
            ProductRepository productRepository,
            Brand brand,
            Flavor flavor,
            String name,
            ProteinType proteinType,
            String description,
            Integer proteinPerServing,
            Integer calories,
            BigDecimal price,
            Integer stockQuantity
    ) {
        boolean exists =
                productRepository
                        .existsByBrandIdAndNameIgnoreCaseAndFlavorId(
                                brand.getId(),
                                name,
                                flavor.getId()
                        );

        if (!exists) {
            Product product = new Product(
                    brand,
                    flavor,
                    name,
                    proteinType
            );

            product.setDescription(description);
            product.setProteinPerServing(proteinPerServing);
            product.setCalories(calories);
            product.setPrice(price);
            product.setStockQuantity(stockQuantity);
            product.setActive(true);

            productRepository.save(product);
        }
    }
}