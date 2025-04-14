-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS exam_db;
USE exam_db;

-- Tạo bảng USER
CREATE TABLE USER (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Thêm dữ liệu cho bảng USER
INSERT INTO USER (username, password, role) VALUES
('user', '123456', 'candidate'),
('admin', '123456', 'admin');

-- Tạo bảng QUESTION
CREATE TABLE QUESTION (
    id INT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    category VARCHAR(100),
    point DOUBLE NOT NULL
);

-- Tạo bảng ANSWER
CREATE TABLE ANSWER (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_id INT NOT NULL,
    content TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL,
    FOREIGN KEY (question_id) REFERENCES QUESTION(id) ON DELETE CASCADE
);

-- Thêm 10 câu hỏi và 3 đáp án cho mỗi câu hỏi
-- Câu 1
INSERT INTO QUESTION (content, category, point) VALUES
('1 + 1 = ?', 'Toán', 10.0);
SET @question_id = LAST_INSERT_ID();
INSERT INTO ANSWER (question_id, content, is_correct) VALUES
(@question_id, '2', TRUE),
(@question_id, '11', FALSE),
(@question_id, '0', FALSE);

-- Câu 2
INSERT INTO QUESTION (content, category, point) VALUES
('Thủ đô của Việt Nam là gì?', 'Địa lý', 10.0);
SET @question_id = LAST_INSERT_ID();
INSERT INTO ANSWER (question_id, content, is_correct) VALUES
(@question_id, 'Hà Nội', TRUE),
(@question_id, 'TP. Hồ Chí Minh', FALSE),
(@question_id, 'Đà Nẵng', FALSE);

-- Câu 3
INSERT INTO QUESTION (content, category, point) VALUES
('Nước có công thức hóa học H₂O là gì?', 'Hóa học', 10.0);
SET @question_id = LAST_INSERT_ID();
INSERT INTO ANSWER (question_id, content, is_correct) VALUES
(@question_id, 'Nước', TRUE),
(@question_id, 'Rượu', FALSE),
(@question_id, 'Dầu', FALSE);

-- Câu 4
INSERT INTO QUESTION (content, category, point) VALUES
('5 × 3 = ?', 'Toán', 10.0);
SET @question_id = LAST_INSERT_ID();
INSERT INTO ANSWER (question_id, content, is_correct) VALUES
(@question_id, '15', TRUE),
(@question_id, '8', FALSE),
(@question_id, '25', FALSE);

-- Câu 5
INSERT INTO QUESTION (content, category, point) VALUES
('Hành tinh nào gần Mặt Trời nhất?', 'Thiên văn', 10.0);
SET @question_id = LAST_INSERT_ID();
INSERT INTO ANSWER (question_id, content, is_correct) VALUES
(@question_id, 'Sao Thủy', TRUE),
(@question_id, 'Sao Hỏa', FALSE),
(@question_id, 'Trái Đất', FALSE);

-- Câu 6
INSERT INTO QUESTION (content, category, point) VALUES
('Ai là tác giả của "Truyện Kiều"?', 'Văn học', 10.0);
SET @question_id = LAST_INSERT_ID();
INSERT INTO ANSWER (question_id, content, is_correct) VALUES
(@question_id, 'Nguyễn Du', TRUE),
(@question_id, 'Hồ Xuân Hương', FALSE),
(@question_id, 'Tố Hữu', FALSE);

-- Câu 7
INSERT INTO QUESTION (content, category, point) VALUES
('Đơn vị đo độ dài là gì?', 'Vật lý', 10.0);
SET @question_id = LAST_INSERT_ID();
INSERT INTO ANSWER (question_id, content, is_correct) VALUES
(@question_id, 'Mét', TRUE),
(@question_id, 'Kilogram', FALSE),
(@question_id, 'Giây', FALSE);

-- Câu 8
INSERT INTO QUESTION (content, category, point) VALUES
('10 - 4 = ?', 'Toán', 10.0);
SET @question_id = LAST_INSERT_ID();
INSERT INTO ANSWER (question_id, content, is_correct) VALUES
(@question_id, '6', TRUE),
(@question_id, '14', FALSE),
(@question_id, '7', FALSE);

-- Câu 9
INSERT INTO QUESTION (content, category, point) VALUES
('Quốc hoa của Việt Nam là gì?', 'Văn hóa', 10.0);
SET @question_id = LAST_INSERT_ID();
INSERT INTO ANSWER (question_id, content, is_correct) VALUES
(@question_id, 'Hoa sen', TRUE),
(@question_id, 'Hoa mai', FALSE),
(@question_id, 'Hoa đào', FALSE);

-- Câu 10
INSERT INTO QUESTION (content, category, point) VALUES
('Mặt Trời mọc ở hướng nào?', 'Địa lý', 10.0);
SET @question_id = LAST_INSERT_ID();
INSERT INTO ANSWER (question_id, content, is_correct) VALUES
(@question_id, 'Đông', TRUE),
(@question_id, 'Tây', FALSE),
(@question_id, 'Bắc', FALSE);

-- Xác nhận dữ liệu
SELECT 'Tạo dữ liệu thành công!' AS message;

-- Tạo bảng EXAM
CREATE TABLE EXAM (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(100) NOT NULL
);

-- Tạo bảng liên kết EXAM_QUESTION
CREATE TABLE EXAM_QUESTION (
    exam_id INT NOT NULL,
    question_id INT NOT NULL,
    PRIMARY KEY (exam_id, question_id),
    FOREIGN KEY (exam_id) REFERENCES EXAM(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES QUESTION(id) ON DELETE CASCADE
);

-- Thêm dữ liệu mẫu cho EXAM
INSERT INTO EXAM (name, category) VALUES
('Kiểm tra Toán 1', 'Toán'),
('Kiểm tra Địa lý 1', 'Địa lý'),
('Kiểm tra Tổng hợp', 'Tổng hợp');

-- Liên kết câu hỏi với bài thi
-- Kiểm tra Toán 1: Câu 1, 4, 8
INSERT INTO EXAM_QUESTION (exam_id, question_id) VALUES
(1, 1),
(1, 4),
(1, 8);

-- Kiểm tra Địa lý 1: Câu 2, 10
INSERT INTO EXAM_QUESTION (exam_id, question_id) VALUES
(2, 2),
(2, 10);

-- Kiểm tra Tổng hợp: Câu 3, 5, 6, 7, 9
INSERT INTO EXAM_QUESTION (exam_id, question_id) VALUES
(3, 3),
(3, 5),
(3, 6),
(3, 7),
(3, 9);

-- Xác nhận
SELECT 'Tạo bảng EXAM và dữ liệu thành công!' AS message;