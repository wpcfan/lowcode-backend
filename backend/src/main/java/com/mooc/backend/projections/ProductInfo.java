package com.mooc.backend.projections;

import java.math.BigDecimal;
import java.util.Set;

/**
 * A Projection for the {@link com.mooc.backend.entities.Product} entity
 */
public interface ProductInfo {
    Long getId();

    String getName();

    String getDescription();

    BigDecimal getPrice();

    Set<CategoryInfo> getCategories();

    Set<ProductImageInfo> getImages();
}