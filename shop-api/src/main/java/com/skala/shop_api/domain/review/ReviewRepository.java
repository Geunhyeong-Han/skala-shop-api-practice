package com.skala.shop_api.domain.review;

import com.skala.shop_api.domain.customer.Customer;
import com.skala.shop_api.domain.product.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByProductOrderByIdDesc(Product product);

    boolean existsByCustomerAndProduct(Customer customer, Product product);
}
