package com.skala.shop_api.dto.product;

import com.skala.shop_api.domain.product.Product;

public record ProductResponse(
        Long id,
        String name,
        long price
) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }
}