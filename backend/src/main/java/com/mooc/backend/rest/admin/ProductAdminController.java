package com.mooc.backend.rest.admin;

import com.mooc.backend.dtos.ProductAdminDTO;
import com.mooc.backend.services.QiniuService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.mooc.backend.dtos.CreateOrUpdateProductDTO;
import com.mooc.backend.error.CustomException;
import com.mooc.backend.services.ProductAdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Tag(name = "商品管理", description = "添加、修改、删除商品，以及给商品添加类目，删除类目")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/products")
public class ProductAdminController {
    private final ProductAdminService productAdminService;
    private final QiniuService qiniuService;

    @Operation(summary = "添加商品")
    @PostMapping()
    public ProductAdminDTO createProduct(@RequestBody CreateOrUpdateProductDTO product) {
        return ProductAdminDTO.fromEntity(productAdminService.createProduct(product));
    }

    @Operation(summary = "修改商品")
    @PutMapping("/{id}")
    public ProductAdminDTO updateProduct(
            @Parameter(description = "商品 id", name = "id") @PathVariable Long id,
            CreateOrUpdateProductDTO product) {
        return productAdminService.updateProduct(id, product)
                .map(ProductAdminDTO::fromEntity)
                .orElseThrow(() -> new CustomException("Product not found", "Product " + id + " not found",
                        HttpStatus.NOT_FOUND.value()));
    }

    @Operation(summary = "给商品添加类目")
    @PutMapping("/{id}/category/{categoryId}")
    public ProductAdminDTO addCategoryToProduct(
            @Parameter(description = "商品 id", name = "id") @PathVariable Long id,
            @Parameter(description = "类目 id", name = "categoryId") @PathVariable Long categoryId) {
        return productAdminService.addCategoryToProduct(id, categoryId)
                .map(ProductAdminDTO::fromEntity)
                .orElseThrow(() -> new CustomException("Product not found", "Product " + id + " not found",
                        HttpStatus.NOT_FOUND.value()));
    }

    @Operation(summary = "删除商品的类目")
    @DeleteMapping("/{id}/category/{categoryId}")
    public ProductAdminDTO removeCategoryFromProduct(
            @Parameter(description = "商品 id", name = "id") @PathVariable Long id,
            @Parameter(description = "类目 id", name = "categoryId") @PathVariable("categoryId") Long categoryId) {
        return productAdminService.removeCategoryFromProduct(id, categoryId)
                .map(ProductAdminDTO::fromEntity)
                .orElseThrow(() -> new CustomException("Product not found", "Product " + id + " not found",
                        HttpStatus.NOT_FOUND.value()));
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    public void deleteProduct(
            @Parameter(description = "商品 id", name = "id") @PathVariable Long id) {
        productAdminService.deleteProduct(id);
    }

    @Operation(summary = "添加商品图片")
    @PostMapping(value = "/{id}/images", consumes = "multipart/form-data")
    public ProductAdminDTO addImageToProduct(
            @Parameter(description = "商品 id", name = "id") @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            var result =  qiniuService.upload(file.getBytes(), UUID.randomUUID().toString());
            return productAdminService.addImageToProduct(id, result.url())
                    .map(ProductAdminDTO::fromEntity)
                    .orElseThrow(() -> new CustomException("Product not found", "Product " + id + " not found",
                            HttpStatus.NOT_FOUND.value()));
        } catch (IOException e) {
            throw new CustomException("File Upload error", e.getMessage(), 500);
        }
    }

    @Operation(summary = "删除商品图片")
    @DeleteMapping("/{id}/images/{imageId}")
    public ProductAdminDTO removeImageFromProduct(
            @Parameter(description = "商品 id", name = "id") @PathVariable Long id,
            @Parameter(description = "图片 id", name = "imageId") @PathVariable Long imageId) {
        return productAdminService.removeImageFromProduct(id, imageId)
                .map(ProductAdminDTO::fromEntity)
                .orElseThrow(() -> new CustomException("Product not found", "Product " + id + " not found",
                        HttpStatus.NOT_FOUND.value()));
    }
}
