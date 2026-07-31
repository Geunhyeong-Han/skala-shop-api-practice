-- ==========================================
-- 1. 상품(Product) 데이터
-- ==========================================
MERGE INTO products (name, price) KEY(name) VALUES ('무선 마우스', 15000);
MERGE INTO products (name, price) KEY(name) VALUES ('블루투스 키보드', 29000);
MERGE INTO products (name, price) KEY(name) VALUES ('USB 허브', 39000);
MERGE INTO products (name, price) KEY(name) VALUES ('SK NUGU 스마트 AI 스피커', 150000);
MERGE INTO products (name, price) KEY(name) VALUES ('호이! 깐따삐야 1인용 텐트', 85000);
MERGE INTO products (name, price) KEY(name) VALUES ('고길동 시그니처 프리미엄 낚싯대', 250000);
MERGE INTO products (name, price) KEY(name) VALUES ('SK매직 올클린 공기청정기', 320000);
MERGE INTO products (name, price) KEY(name) VALUES ('마이콜의 통기타 (입문자용)', 99000);

-- ==========================================
-- 2. 고객(Customer) 데이터
-- 비밀번호는 BCrypt로 해시된 값입니다 (원문: 둘리=dooly1234, 고길동=go1234, 마이콜=michol1234, 희동이=heedong1234)
-- ==========================================
MERGE INTO customers (customer_id, password_hash, point) KEY(customer_id)
    VALUES ('둘리', '$2y$10$/7HTrVUUtSMHc5wE6uFrO.1i6S547egXwtq2t5frpy20il1glfqrm', 1000000);
MERGE INTO customers (customer_id, password_hash, point) KEY(customer_id)
    VALUES ('고길동', '$2y$10$MEuSaf55C6DsbReKXU701OH02Q4yajGzTsZt5xK3/bXijz/rIE5HW', 1000000);
MERGE INTO customers (customer_id, password_hash, point) KEY(customer_id)
    VALUES ('마이콜', '$2y$10$po3l3kOa41Fbq2tb9Hsmp.muPGyIulbNqbh4sYHvUrndr.npOf2s.', 1000000);
MERGE INTO customers (customer_id, password_hash, point) KEY(customer_id)
    VALUES ('희동이', '$2y$10$MSEhYFezSvQuynEGHq35BechZv3y9.XJRYFO8gcVqCIwZUtg98liu', 1000000);

-- ==========================================
-- 3. 리뷰(Review) 데이터
-- product_id는 상품명으로 조회하여 채웁니다 (IDENTITY 자동 채번이라 고정 id를 알 수 없음)
-- ==========================================
MERGE INTO reviews (customer_id, product_id, rating, content, created_at) KEY(customer_id, product_id)
    VALUES ('둘리', (SELECT id FROM products WHERE name = 'SK NUGU 스마트 AI 스피커'), 5, '호이! 깐따삐야라고 해도 찰떡같이 알아듣고 노래를 틀어주네요. 너무 좋아요!', CURRENT_TIMESTAMP);
MERGE INTO reviews (customer_id, product_id, rating, content, created_at) KEY(customer_id, product_id)
    VALUES ('고길동', (SELECT id FROM products WHERE name = 'SK매직 올클린 공기청정기'), 5, '맨날 둘리 녀석이 집안을 어질러서 샀는데, 공기가 확 달라졌습니다. 대만족.', CURRENT_TIMESTAMP);
MERGE INTO reviews (customer_id, product_id, rating, content, created_at) KEY(customer_id, product_id)
    VALUES ('고길동', (SELECT id FROM products WHERE name = '고길동 시그니처 프리미엄 낚싯대'), 4, '그립감이 아주 훌륭합니다. 주말 낚시가 기다려지네요. 흠집이 살짝 있어서 별 하나 뺍니다.', CURRENT_TIMESTAMP);
MERGE INTO reviews (customer_id, product_id, rating, content, created_at) KEY(customer_id, product_id)
    VALUES ('마이콜', (SELECT id FROM products WHERE name = '마이콜의 통기타 (입문자용)'), 5, '이 기타로 오디션 준비 중입니다. 소리가 아주 맑고 경쾌하네요~ 굿!', CURRENT_TIMESTAMP);
MERGE INTO reviews (customer_id, product_id, rating, content, created_at) KEY(customer_id, product_id)
    VALUES ('둘리', (SELECT id FROM products WHERE name = '호이! 깐따삐야 1인용 텐트'), 5, '도우너랑 같이 누워도 넉넉해요 호잇! 배송도 엄청 빠릅니다.', CURRENT_TIMESTAMP);
MERGE INTO reviews (customer_id, product_id, rating, content, created_at) KEY(customer_id, product_id)
    VALUES ('희동이', (SELECT id FROM products WHERE name = 'SK NUGU 스마트 AI 스피커'), 5, '아따따따! (번역: AI 스피커에서 나오는 동요가 마음에 듭니다)', CURRENT_TIMESTAMP);
