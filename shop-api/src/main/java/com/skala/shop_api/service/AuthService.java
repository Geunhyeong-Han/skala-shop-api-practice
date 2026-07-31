package com.skala.shop_api.service;

import com.skala.shop_api.domain.customer.Customer;
import com.skala.shop_api.domain.customer.CustomerRepository;
import com.skala.shop_api.dto.customer.LoginRequest;
import com.skala.shop_api.dto.customer.SignUpRequest;
import com.skala.shop_api.dto.customer.SignUpResponse;
import com.skala.shop_api.exception.BusinessException;
import com.skala.shop_api.exception.ErrorCode;
import com.skala.shop_api.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private static final long INITIAL_POINT = 1_000_000L;

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        if (customerRepository.existsById(request.customerId())) {
            throw new BusinessException(ErrorCode.DUPLICATE_CUSTOMER_ID);
        }

        Customer customer = new Customer(
                request.customerId(),
                passwordEncoder.encode(request.customerPassword()),
                INITIAL_POINT
        );

        customerRepository.save(customer);

        return new SignUpResponse(
                customer.getCustomerId(),
                customer.getPoint(),
                "회원가입이 완료되었습니다."
        );
    }

    public String login(LoginRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(
                request.customerPassword(),
                customer.getPasswordHash()
        )) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        return jwtTokenProvider.generateToken(customer.getCustomerId());
    }
}