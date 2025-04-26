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

-- Tạo bảng EXAM
CREATE TABLE EXAM (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL
);

-- Tạo bảng QUESTION
CREATE TABLE QUESTION (
    id INT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    point DOUBLE NOT NULL,
    exam_id INT,
    FOREIGN KEY (exam_id) REFERENCES EXAM(id) ON DELETE CASCADE
);

-- Tạo bảng ANSWER
CREATE TABLE ANSWER (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_id INT NOT NULL,
    content TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL,
    FOREIGN KEY (question_id) REFERENCES QUESTION(id) ON DELETE CASCADE
);

-- Thêm dữ liệu cho bảng USER
INSERT INTO USER (username, password, role) VALUES
('user', '123456', 'candidate'),
('admin', '123456', 'admin');

-- Chèn dữ liệu vào EXAM
INSERT INTO EXAM (name, category) VALUES
('Bài thi Toán', 'Toán'),
('Bài thi Văn', 'Văn'),
('Bài thi Anh', 'Anh');

-- Chèn dữ liệu vào QUESTION
INSERT INTO QUESTION (content, category, point, exam_id) VALUES
('2 + 3 = ?', 'Toán', 10.0, 1),
('Giá trị của x trong 2x + 5 = 11?', 'Toán', 10.0, 1),
('Diện tích hình vuông cạnh 4cm?', 'Toán', 10.0, 1),
('6 × 4 = ?', 'Toán', 10.0, 1),
('Số nguyên tố nhỏ nhất là?', 'Toán', 10.0, 1),
('Tác giả của "Truyện Kiều" là ai?', 'Văn', 10.0, 2),
('Thể loại của "Lục Vân Tiên"?', 'Văn', 10.0, 2),
('Nhân vật chính trong "Tắt Đèn"?', 'Văn', 10.0, 2),
('Bài thơ "Tràng Giang" của ai?', 'Văn', 10.0, 2),
('"Chí Phèo" thuộc thể loại gì?', 'Văn', 10.0, 2),
('"Hello" trong tiếng Việt là?', 'Anh', 10.0, 3),
('Past tense của "go" là?', 'Anh', 10.0, 3),
('"Apple" nghĩa là gì?', 'Anh', 10.0, 3),
('Câu đúng ngữ pháp là?', 'Anh', 10.0, 3),
('Từ đồng nghĩa với "happy"?', 'Anh', 10.0, 3);

-- Chèn dữ liệu vào ANSWER
INSERT INTO ANSWER (question_id, content, is_correct) VALUES
(1, '5', TRUE),
(1, '6', FALSE),
(1, '7', FALSE),
(2, '3', TRUE),
(2, '4', FALSE),
(2, '5', FALSE),
(3, '16 cm²', TRUE),
(3, '8 cm²', FALSE),
(3, '12 cm²', FALSE),
(4, '24', TRUE),
(4, '20', FALSE),
(4, '28', FALSE),
(5, '2', TRUE),
(5, '1', FALSE),
(5, '3', FALSE),
(6, 'Nguyễn Du', TRUE),
(6, 'Hồ Xuân Hương', FALSE),
(6, 'Tố Hữu', FALSE),
(7, 'Truyện thơ', TRUE),
(7, 'Tiểu thuyết', FALSE),
(7, 'Kịch', FALSE),
(8, 'Chị Dậu', TRUE),
(8, 'Anh Dậu', FALSE),
(8, 'Nghị Quế', FALSE),
(9, 'Huy Cận', TRUE),
(9, 'Xuân Diệu', FALSE),
(9, 'Hàn Mặc Tử', FALSE),
(10, 'Truyện ngắn', TRUE),
(10, 'Tiểu thuyết', FALSE),
(10, 'Thơ', FALSE),
(11, 'Xin chào', TRUE),
(11, 'Tạm biệt', FALSE),
(11, 'Cảm ơn', FALSE),
(12, 'Went', TRUE),
(12, 'Goed', FALSE),
(12, 'Gone', FALSE),
(13, 'Quả táo', TRUE),
(13, 'Quả cam', FALSE),
(13, 'Quả lê', FALSE),
(14, 'She is a student', TRUE),
(14, 'She are a student', FALSE),
(14, 'She am a student', FALSE),
(15, 'Joyful', TRUE),
(15, 'Sad', FALSE),
(15, 'Angry', FALSE);

-- Xác nhận dữ liệu
SELECT 'Tạo dữ liệu thành công!' AS message;