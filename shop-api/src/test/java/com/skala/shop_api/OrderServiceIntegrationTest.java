package com.skala.shop_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.skala.shop_api.domain.customer.Customer;
import com.skala.shop_api.domain.customer.CustomerRepository;
import com.skala.shop_api.domain.order.OrderItemRepository;
import com.skala.shop_api.domain.product.Product;
import com.skala.shop_api.domain.product.ProductRepository;
import com.skala.shop_api.dto.order.OrderRequest;
import com.skala.shop_api.exception.BusinessException;
import com.skala.shop_api.exception.ErrorCode;
import com.skala.shop_api.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceIntegrationTest {

    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    private Product product;

    @Autowired
    OrderServiceIntegrationTest(
            OrderService orderService,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            OrderItemRepository orderItemRepository
    ) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @BeforeEach
    void setUp() {
        orderItemRepository.deleteAll();
        customerRepository.deleteAll();
        productRepository.deleteAll();

        customerRepository.save(
                new Customer("test01", "test-password-hash", 100_000L)
        );

        product = productRepository.save(
                new Product("테스트 상품", 10_000L)
        );
    }

    @Test
    void sameProductOrderIncreasesQuantity() {
        orderService.placeOrder(
                "test01",
                new OrderRequest(product.getId(), 2)
        );

        orderService.placeOrder(
                "test01",
                new OrderRequest(product.getId(), 1)
        );

        assertThat(
                orderService.findMyOrders("test01")
                        .products()
                        .getFirst()
                        .quantity()
        ).isEqualTo(3);

        assertThat(
                customerRepository.findById("test01")
                        .orElseThrow()
                        .getPoint()
        ).isEqualTo(70_000L);
    }

    @Test
    void cancelAllQuantityDeletesOrderItemAndRefundsPoint() {
        orderService.placeOrder(
                "test01",
                new OrderRequest(product.getId(), 2)
        );

        orderService.cancelOrder(
                "test01",
                new OrderRequest(product.getId(), 2)
        );

        orderItemRepository.flush();

        assertThat(
                orderService.findMyOrders("test01").products()
        ).isEmpty();

        assertThat(
                customerRepository.findById("test01")
                        .orElseThrow()
                        .getPoint()
        ).isEqualTo(100_000L);
    }

    @Test
    void insufficientFundsRejectsOrder() {
        assertThatThrownBy(
                () -> orderService.placeOrder(
                        "test01",
                        new OrderRequest(product.getId(), 11)
                )
        )
                .isInstanceOf(BusinessException.class)
                .satisfies(exception -> assertThat(
                        ((BusinessException) exception).getErrorCode()
                ).isEqualTo(ErrorCode.INSUFFICIENT_FUNDS));

        assertThat(
                orderService.findMyOrders("test01").products()
        ).isEmpty();

        assertThat(
                customerRepository.findById("test01")
                        .orElseThrow()
                        .getPoint()
        ).isEqualTo(100_000L);
    }
}