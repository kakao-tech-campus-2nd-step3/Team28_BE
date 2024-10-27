-- 채팅 관련 data.sql (line 1~25)
-- 예시 명함 데이터
INSERT INTO chat_user (id, name, company, position, email, phone, timestamp) VALUES
    (1, '라이언', 'kakao', 'Software Engineer', 'ryan@kakao.com', '123-123', CURRENT_TIMESTAMP),
    (2, '죠르디', 'kakao', 'Product Manager', 'jordy@kakao.com', '456-456', CURRENT_TIMESTAMP),
    (3, '어피치', 'kakao', 'Backend Developer', 'apeach@kakao.com', '789-789', CURRENT_TIMESTAMP);

-- 예시 채팅방 데이터
INSERT INTO chat_room (created_at, last_message, last_message_time) VALUES
    (CURRENT_TIMESTAMP, 'Hello, world!', CURRENT_TIMESTAMP),
    (CURRENT_TIMESTAMP, '새로운 채팅방 메시지~', CURRENT_TIMESTAMP);

-- 예시 채팅 메시지 데이터
INSERT INTO chat_message (content, sender, timestamp, chat_room_id) VALUES
    ('안녕하세요', '라이언', CURRENT_TIMESTAMP, 1),
    ('새로운 채팅방 메시지~', '어피치', CURRENT_TIMESTAMP, 2),
    ('Hello, world!', '죠르디', CURRENT_TIMESTAMP + 0.1, 1);

-- 예시 채팅방 참가자 데이터
INSERT INTO chat_room_participants (chat_room_id, participants_id) VALUES
    (1, 1),
    (1, 2),
    (2, 1),
    (2, 3);

-- 명함(Card) 테이블에 예시 데이터 추가
INSERT INTO card (github_id, name, company, position, email, phone, profile_picture, bio) VALUES
    ('hong-gildong', '홍길동', 'DevCompany', '백엔드 개발자', 'hong@example.com', '010-1234-5678', 'profile.jpg', '열정적인 개발자입니다.');
