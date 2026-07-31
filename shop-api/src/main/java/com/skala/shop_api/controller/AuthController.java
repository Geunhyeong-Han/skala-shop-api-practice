package com.skala.shop_api.controller;

import com.skala.shop_api.dto.customer.LoginRequest;
import com.skala.shop_api.dto.customer.LoginResponse;
import com.skala.shop_api.dto.customer.SignUpRequest;
import com.skala.shop_api.dto.customer.SignUpResponse;
import com.skala.shop_api.service.AuthService;
import jakarta.validation.Valid;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class AuthController {

    private final AuthService authService;
    private final String cookieName;
    private final long expirationMinutes;

    public AuthController(
            AuthService authService,
            @Value("${jwt.cookie-name}") String cookieName,
            @Value("${jwt.expiration-minutes}") long expirationMinutes
    ) {
        this.authService = authService;
        this.cookieName = cookieName;
        this.expirationMinutes = expirationMinutes;
    }

    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        String token = authService.login(request);

        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofMinutes(expirationMinutes))
                .build();

        LoginResponse response = new LoginResponse(
                request.customerId(),
                "로그인이 완료되었습니다.",
                expirationMinutes
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }
}