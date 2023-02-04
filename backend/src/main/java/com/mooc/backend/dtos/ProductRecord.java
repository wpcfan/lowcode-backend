package com.mooc.backend.dtos;

import com.mooc.backend.entities.Product;
import com.mooc.backend.entities.ProductImage;

import java.util.Set;
import java.util.stream.Collectors;

public record ProductRecord(Long id, String name, String description, Integer price, Set<CategoryDTO> categories, Set<String> images) {

    public static ProductRecord fromEntity(Product product) {
        return new ProductRecord(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategories().stream()
                        .map(CategoryDTO::fromEntity)
                        .collect(Collectors.toSet()),
                product.getImages().stream()
                        .map(ProductImage::getImageUrl)
                        .collect(Collectors.toSet())
        );
    }

    public Product toEntity() {
        var product = Product.builder()
                .name(name())
                .description(description())
                .price(price())
                .build();
        images().forEach(image -> {
            var productImage = ProductImage.builder()
                    .product(product)
                    .imageUrl(image)
                    .build();
            product.addImage(productImage);
        });
        categories().forEach(category -> {
            var categoryEntity = category.toEntity();
            product.addCategory(categoryEntity);
        });
        return product;
    }
}