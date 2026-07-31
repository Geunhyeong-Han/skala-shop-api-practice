package com.skala.shop_api.dto.review;

import com.skala.shop_api.domain.review.Review;
import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long productId,
        String customerId,
        int rating,
        String content,
        LocalDateTime createdAt
) {

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getProduct().getId(),
                review.getCustomer().getCustomerId(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt()
        );
    }
}
