package com.skala.shop_api.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", "상품을 찾을 수 없습니다."),
    DUPLICATE_PRODUCT_NAME(HttpStatus.CONFLICT, "DUPLICATE_PRODUCT_NAME", "이미 등록된 상품명입니다."),
    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "CUSTOMER_NOT_FOUND", "고객을 찾을 수 없습니다."),
    DUPLICATE_CUSTOMER_ID(HttpStatus.CONFLICT, "DUPLICATE_CUSTOMER_ID", "이미 사용 중인 고객 ID입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "고객 ID 또는 비밀번호가 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "로그인이 필요한 요청입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "유효하지 않거나 만료된 인증 토큰입니다."),
    INSUFFICIENT_FUNDS(HttpStatus.CONFLICT, "INSUFFICIENT_FUNDS", "보유 포인트가 부족합니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", "주문한 상품을 찾을 수 없습니다."),
    INSUFFICIENT_QUANTITY(HttpStatus.CONFLICT, "INSUFFICIENT_QUANTITY", "취소 수량이 주문 수량보다 많습니다."),
    DATA_CONFLICT(HttpStatus.CONFLICT, "DATA_CONFLICT", "연결된 데이터가 있어 요청을 처리할 수 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW_NOT_FOUND", "리뷰를 찾을 수 없습니다."),
    DUPLICATE_REVIEW(HttpStatus.CONFLICT, "DUPLICATE_REVIEW", "이미 해당 상품에 리뷰를 작성했습니다."),
    REVIEW_ACCESS_DENIED(HttpStatus.FORBIDDEN, "REVIEW_ACCESS_DENIED", "본인이 작성한 리뷰만 삭제할 수 있습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "요청 값을 확인해 주세요."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
