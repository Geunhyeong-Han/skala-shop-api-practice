package com.skala.shop_api.dto.customer;

public record SignUpResponse(
        String customerId,
        long customerPoint,
        String message
) {
}