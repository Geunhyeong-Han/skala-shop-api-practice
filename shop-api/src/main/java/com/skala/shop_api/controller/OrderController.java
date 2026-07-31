package com.skala.shop_api.controller;

import com.skala.shop_api.dto.order.CustomerOrderResponse;
import com.skala.shop_api.dto.order.OrderRequest;
import com.skala.shop_api.dto.order.OrderResultResponse;
import com.skala.shop_api.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerOrderResponse> findMyOrders(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                orderService.findMyOrders(authentication.getName())
        );
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResultResponse> placeOrder(
            Authentication authentication,
            @Valid @RequestBody OrderRequest request
    ) {
        return ResponseEntity.ok(
                orderService.placeOrder(authentication.getName(), request)
        );
    }

    @PostMapping("/cancel")
    public ResponseEntity<OrderResultResponse> cancelOrder(
            Authentication authentication,
            @Valid @RequestBody OrderRequest request
    ) {
        return ResponseEntity.ok(
                orderService.cancelOrder(authentication.getName(), request)
        );
    }
}