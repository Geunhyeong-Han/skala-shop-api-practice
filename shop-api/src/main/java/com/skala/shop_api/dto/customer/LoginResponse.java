package com.skala.shop_api.dto.customer;

public record LoginResponse(
        String customerId,
        String message,
        long expiresInMinutes
) {
}
