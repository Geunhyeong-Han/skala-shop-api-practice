package com.skala.shop_api.security;

import tools.jackson.databind.json.JsonMapper;
import com.skala.shop_api.exception.ErrorCode;
import com.skala.shop_api.exception.ErrorResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JsonMapper jsonMapper;
    private final String cookieName;

    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            JsonMapper jsonMapper,
            @Value("${jwt.cookie-name}") String cookieName
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jsonMapper = jsonMapper;
        this.cookieName = cookieName;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        if ("/api/health".equals(path) || path.startsWith("/api/products")) {
            return true;
        }

        return "POST".equals(request.getMethod())
                && ("/api/customers".equals(path)
                || "/api/customers/login".equals(path));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = resolveToken(request);

        if (token != null) {
            try {
                String customerId = jwtTokenProvider.getCustomerId(token);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                customerId,
                                null,
                                List.of()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException | IllegalArgumentException exception) {
                SecurityContextHolder.clearContext();
                writeUnauthorized(response, request, ErrorCode.INVALID_TOKEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    private void writeUnauthorized(
            HttpServletResponse response,
            HttpServletRequest request,
            ErrorCode errorCode
    ) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getStatus().value(),
                errorCode.getCode(),
                errorCode.getMessage(),
                request.getRequestURI()
        );

        jsonMapper.writeValue(response.getWriter(), errorResponse);
    }
}