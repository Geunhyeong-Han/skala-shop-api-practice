package com.skala.shop_api.dto.order;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(

        @NotNull(message = "상품 ID는 필수입니다.")
        Long productId,

        @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
        @Max(value = 1000, message = "한 번에 주문하거나 취소할 수 있는 수량은 1000개 이하입니다.")
        int quantity
) {
}