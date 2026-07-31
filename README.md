# skala-shop-api-practice

SKALA 백엔드 과정 실습용 온라인 쇼핑몰(E-commerce) API 프로젝트입니다.
Spring Boot 기반으로 상품/회원/주문/리뷰 도메인을 구현하고, 입력 검증, 전역 예외 처리, JWT 인증, API 문서 자동화(OAS), 데이터 영속화까지 실무 수준의 백엔드 구성을 갖췄습니다.

## 기술 스택

- Java 21, Spring Boot 4.1.0
- Spring Web MVC, Spring Data JPA, Spring Security
- H2 Database (파일 모드)
- JWT (`io.jsonwebtoken`)
- springdoc-openapi (Swagger UI)
- Gradle

## 프로젝트 구조

```
skala-shop-api-practice/
├── README.md
└── shop-api/
    ├── build.gradle
    ├── settings.gradle
    └── src/
        ├── main/
        │   ├── java/com/skala/shop_api/
        │   │   ├── ShopApiApplication.java     # 부트 진입점
        │   │   ├── config/                     # SecurityConfig, PasswordConfig, OpenApiConfig
        │   │   ├── controller/                 # Health / Product / Auth / Order / Review
        │   │   ├── service/                    # 비즈니스 로직, 트랜잭션 경계
        │   │   ├── domain/                     # JPA 엔티티 + 리포지토리 (customer/product/order/review)
        │   │   ├── dto/                        # 요청/응답 DTO
        │   │   ├── security/                   # JwtTokenProvider, JwtAuthenticationFilter, EntryPoint
        │   │   └── exception/                  # GlobalExceptionHandler, BusinessException, ErrorCode
        │   └── resources/
        │       ├── application.yml             # H2 파일 모드, JPA, JWT 설정
        │       ├── data.sql                     # 초기 시드 데이터
        │       └── static/
        │           ├── index.html               # 전체 API 테스트용 프론트엔드
        │           └── cocoball2.png
        └── test/
            ├── java/com/skala/shop_api/         # 통합 테스트
            └── resources/
                └── application.yml               # 테스트 전용 격리 DB(in-memory)
```

계층 구조는 **Controller → Service → Repository/Entity**로 일관되며, 인증(`security`)·예외 처리(`exception`)·설정(`config`)은 횡단 관심사로 분리되어 있습니다.

## 실행 방법

```bash
cd shop-api
./gradlew bootRun
```

기본 포트는 `8080`이며, 브라우저에서 `http://localhost:8080` 접속 시 전체 API를 테스트할 수 있는 프론트엔드 화면이 뜹니다.

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI 스펙(JSON): `http://localhost:8080/v3/api-docs`

빌드만 하려면:

```bash
cd shop-api
./gradlew build
```

## 주요 기능

- **입력 검증 + 전역 예외 처리**: `@Valid` 기반 요청 검증과 `@RestControllerAdvice`(`GlobalExceptionHandler`)로 모든 예외를 `{timestamp, status, code, message, path}` 형식의 일관된 응답으로 처리
- **JWT 인증**: 로그인 성공 시 JWT를 httpOnly 쿠키(`bff-access`)로 발급, 이후 요청은 쿠키 기반으로 인증
- **데이터 영속화**: H2를 파일 모드(`jdbc:h2:file:./data/shopdb`)로 구성해 서버 재시작에도 데이터 유지
- **API 문서 자동화**: springdoc-openapi로 OAS 3.0 스펙과 Swagger UI를 코드로부터 자동 생성
- **리뷰 기능**: 상품당 고객 1인 1리뷰, 본인 리뷰만 삭제 가능

## 비즈니스 규칙 요약

| 도메인 | 규칙 |
|---|---|
| 회원 | 고객 ID 4~20자, 비밀번호 6~30자(BCrypt 해시 저장), 가입 시 초기 포인트 1,000,000 지급 |
| 상품 | 상품명 유니크, 가격 1원 이상 |
| 주문 | 동일 상품 재주문 시 수량 합산, 포인트 부족 시 주문 거부, 1회 주문/취소 수량 1~1000개 |
| 리뷰 | 상품당 고객 1인 1리뷰(중복 작성 차단), 평점 1~5점, 내용 500자 이하, 본인 리뷰만 삭제 가능 |

## API 목록

| Resource | Method | URL | 인증 | 설명 |
|---|---|---|---|---|
| Health | GET | `/api/health` | 불필요 | 서버 상태 확인 |
| Product | GET | `/api/products` | 불필요 | 상품 전체 목록 조회 |
| Product | GET | `/api/products/{id}` | 불필요 | 상품 단건 조회 |
| Product | POST | `/api/products` | 불필요 | 상품 등록 |
| Product | PUT | `/api/products/{id}` | 불필요 | 상품 수정 |
| Product | DELETE | `/api/products/{id}` | 불필요 | 상품 삭제 |
| Customer(Auth) | POST | `/api/customers` | 불필요 | 회원가입 |
| Customer(Auth) | POST | `/api/customers/login` | 불필요 | 로그인 (JWT 쿠키 발급) |
| Customer(Order) | GET | `/api/customers/me` | 필요 | 내 주문/포인트 조회 |
| Customer(Order) | POST | `/api/customers/order` | 필요 | 상품 주문 (포인트 차감) |
| Customer(Order) | POST | `/api/customers/cancel` | 필요 | 주문 취소 (포인트 환불) |
| Review | GET | `/api/reviews?productId={id}` | 불필요 | 상품별 리뷰 목록 조회 |
| Review | POST | `/api/reviews` | 필요 | 리뷰 작성 (상품당 1회) |
| Review | DELETE | `/api/reviews/{id}` | 필요 | 본인 리뷰 삭제 |
| Docs | GET | `/v3/api-docs` | 불필요 | OpenAPI 스펙(JSON) |
| Docs | GET | `/swagger-ui/index.html` | 불필요 | Swagger UI |

> `/api/products/**`는 현재 모든 HTTP 메서드가 인증 없이 열려 있습니다 (별도 관리자 권한 체계는 미구현).
