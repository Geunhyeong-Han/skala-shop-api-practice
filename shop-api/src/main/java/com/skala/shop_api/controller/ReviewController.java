package com.skala.shop_api.controller;

import com.skala.shop_api.dto.review.ReviewRequest;
import com.skala.shop_api.dto.review.ReviewResponse;
import com.skala.shop_api.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> findAllByProduct(
            @RequestParam Long productId
    ) {
        return ResponseEntity.ok(reviewService.findAllByProduct(productId));
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> create(
            Authentication authentication,
            @Valid @RequestBody ReviewRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.create(authentication.getName(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            Authentication authentication,
            @PathVariable Long id
    ) {
        reviewService.delete(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
