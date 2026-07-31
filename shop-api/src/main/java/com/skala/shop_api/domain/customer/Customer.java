package com.skala.shop_api.domain.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_id", length = 20)
    private String customerId;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(nullable = false)
    private long point;

    protected Customer() {
    }

    public Customer(String customerId, String passwordHash, long point) {
        this.customerId = customerId;
        this.passwordHash = passwordHash;
        this.point = point;
    }

    public void usePoint(long amount) {
        this.point -= amount;
    }

    public void refundPoint(long amount) {
        this.point += amount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public long getPoint() {
        return point;
    }
}