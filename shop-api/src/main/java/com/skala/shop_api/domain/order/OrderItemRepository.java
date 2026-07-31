package com.skala.shop_api.domain.order;

import com.skala.shop_api.domain.customer.Customer;
import com.skala.shop_api.domain.product.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findByCustomerAndProduct(
            Customer customer,
            Product product
    );

    List<OrderItem> findAllByCustomerOrderByIdAsc(Customer customer);
}