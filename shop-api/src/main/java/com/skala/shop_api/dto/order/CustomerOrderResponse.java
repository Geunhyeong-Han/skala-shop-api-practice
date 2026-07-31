package com.skala.shop_api.dto.order;

import java.util.List;

public record CustomerOrderResponse(
        String customerId,
        long customerPoint,
        List<OrderItemResponse> products
) {
}