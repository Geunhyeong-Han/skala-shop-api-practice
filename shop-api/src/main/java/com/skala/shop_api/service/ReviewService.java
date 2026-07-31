package com.skala.shop_api.service;

import com.skala.shop_api.domain.customer.Customer;
import com.skala.shop_api.domain.customer.CustomerRepository;
import com.skala.shop_api.domain.product.Product;
import com.skala.shop_api.domain.product.ProductRepository;
import com.skala.shop_api.domain.review.Review;
import com.skala.shop_api.domain.review.ReviewRepository;
import com.skala.shop_api.dto.review.ReviewRequest;
import com.skala.shop_api.dto.review.ReviewResponse;
import com.skala.shop_api.exception.BusinessException;
import com.skala.shop_api.exception.ErrorCode;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public List<ReviewResponse> findAllByProduct(Long productId) {
        Product product = getProduct(productId);

        return reviewRepository.findAllByProductOrderByIdDesc(product)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }

    @Transactional
    public ReviewResponse create(String customerId, ReviewRequest request) {
        Customer customer = getCustomer(customerId);
        Product product = getProduct(request.productId());

        if (reviewRepository.existsByCustomerAndProduct(customer, product)) {
            throw new BusinessException(ErrorCode.DUPLICATE_REVIEW);
        }

        Review review = new Review(
                customer,
                product,
                request.rating(),
                request.content()
        );

        return ReviewResponse.from(reviewRepository.save(review));
    }

    @Transactional
    public void delete(String customerId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getCustomer().getCustomerId().equals(customerId)) {
            throw new BusinessException(ErrorCode.REVIEW_ACCESS_DENIED);
        }

        reviewRepository.delete(review);
    }

    private Customer getCustomer(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
