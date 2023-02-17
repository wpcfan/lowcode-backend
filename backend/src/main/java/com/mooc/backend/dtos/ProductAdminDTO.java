package com.mooc.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mooc.backend.entities.Product;
import com.mooc.backend.entities.ProductImage;
import com.mooc.backend.entities.blocks.BlockData;
import com.mooc.backend.enumerations.BlockDataType;
import com.mooc.backend.projections.ProductImageInfo;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@JsonDeserialize(as = ProductAdminDTO.class)

public record ProductAdminDTO(Long id, String name, String description, BigDecimal price, Set<CategoryDTO> categories,
                              Set<ProductImageInfo> images) implements BlockData {

    public static ProductAdminDTO fromEntity(Product product) {
        return new ProductAdminDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategories().stream()
                        .map(CategoryDTO::fromEntity)
                        .collect(Collectors.toSet()),
                product.getImages().stream()
                        .map(i -> new ProductImageInfo(i.getId(), i.getImageUrl()))
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
                    .imageUrl(image.imageUrl())
                    .build();
            product.addImage(productImage);
        });
        categories().forEach(category -> {
            var categoryEntity = category.toEntity();
            product.addCategory(categoryEntity);
        });
        return product;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Override
    public BlockDataType getDataType() {
        return BlockDataType.Product;
    }
}
