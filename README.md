# Ứng Dụng Thi Trắc Nghiệm

## Mô Tả

Ứng dụng Thi Trắc Nghiệm là một phần mềm Java sử dụng giao diện Swing, cho phép người dùng quản lý và thực hiện các bài thi trắc nghiệm. Ứng dụng hỗ trợ:

- **Quản lý bài thi** (chỉ dành cho admin):
  - Thêm bài thi từ file Excel.
  - Xóa bài thi.
- **Làm bài thi** (dành cho tất cả người dùng):
  - Chọn bài thi, trả lời câu hỏi, xem kết quả.
- **Đăng nhập/đăng xuất**:
  - Phân quyền admin (quản lý) và candidate (làm bài thi).

Ứng dụng hiện hỗ trợ các danh mục bài thi: Toán, Văn, Anh. Mỗi bài thi có nhiều câu hỏi, mỗi câu có 3 đáp án (1 đúng, 2 sai).

## Yêu Cầu Hệ Thống

- **Hệ điều hành**: Windows, macOS, hoặc Linux.
- **Java**: JDK 8 trở lên.
- **Cơ sở dữ liệu**: MySQL 5.7 trở lên.
- **Thư viện**:
  - Apache POI (đọc file Excel): `poi-5.2.3`, `poi-ooxml-5.2.3`.
  - Log4j2 (ghi log): `log4j-core-2.20.0`, `log4j-api-2.20.0`.
  - MySQL Connector/J: `mysql-connector-java-8.0.27`.
- **IDE** (khuyến nghị): IntelliJ IDEA, Eclipse.
- **Maven** (nếu dùng): Để quản lý dependency.

## Hướng Dẫn Cài Đặt

### 1. Thiết Lập Cơ Sở Dữ Liệu

1. **Cài đặt MySQL**:
   - Tải và cài đặt MySQL từ [mysql.com](https://dev.mysql.com/downloads/).
   
2. **Tạo cơ sở dữ liệu**:
   - Đăng nhập MySQL:
     ```
     username: root
     password: 123456
     ```
   - Sử dụng file `patternData.sql` để tạo dữ liêu.


### 2. Chạy Ứng Dụng

1. **Biên dịch và chạy**:
   - Trong IDE, chạy `LoginFrame.java`:
     ```java
     public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
     }
     ```
   - Hoặc từ terminal:
     ```bash
     javac -cp .;path_to_jars/* com/caycon/view/*.java com/caycon/dao/*.java com/caycon/util/*.java com/caycon/model/*.java
     java -cp .;path_to_jars/* com.caycon.view.LoginFrame
     ```

2. **Đăng nhập**:
   - Sử dụng tài khoản:
     - Admin: `admin` / `123456` (quản lý bài thi).
     - Candidate: `user` / `123456` (làm bài thi).

## Hướng Dẫn Sử Dụng

### 1. Đăng Nhập

- **Màn hình đăng nhập** (`LoginFrame`):
  - Nhập tên đăng nhập và mật khẩu.
  - Nhấn "Đăng Nhập".
  - Nếu sai, thông báo "Sai tên đăng nhập hoặc mật khẩu!".
  - Nếu đúng, mở `MainFrame` với thông tin người dùng.

### 2. Giao Diện Chính (`MainFrame`)

- **Taskbar bên trái**:
  - **Thông Tin Cá Nhân**: Hiển thị tên đăng nhập và vai trò.
  - **Danh sách bài thi**: Xem và chọn bài thi.
  - **Đăng Xuất**: Quay lại `LoginFrame`.
- **Quyền admin**:
  - Thấy thêm nút "Thêm Bài Thi" và "Xóa Bài Thi" trong panel danh sách bài thi.

### 3. Quản Lý Bài Thi (Admin)

- **Thêm bài thi** (`AddExamFrame`):
  1. Nhấn "Thêm Bài Thi" trong `MainFrame`.
  2. Nhập tên bài thi (ví dụ: "Bài thi Văn học").
  3. Chọn danh mục: Toán, Văn, hoặc Anh.
  4. Nhấn "Chọn file" để tải file Excel (`.xlsx`) chứa câu hỏi.
  5. Nhấn "Lưu" → Thông báo "Thêm bài thi với X câu hỏi thành công!".
  6. Danh sách bài thi tự động làm mới.

- **Xóa bài thi**:
  1. Nhấn "Xóa Bài Thi" trong `MainFrame`.
  2. Chọn bài thi từ danh sách (ví dụ: "Bài thi Văn học (Văn)").
  3. Nhấn OK → Thông báo "Xóa bài thi thành công!".
  4. Danh sách bài thi tự động làm mới.

### 4. Làm Bài Thi

- **Chọn bài thi**:
  1. Trong `MainFrame`, nhấn nút bài thi (ví dụ: "Bài thi Văn học (Văn, 5 câu)") → Mở `ExamFrame`.
- **Trả lời câu hỏi**:
  - Mỗi câu hỏi có 3 đáp án (chọn bằng `JRadioButton`).
  - Nhấn "Câu tiếp theo" để chuyển câu.
  - Nhấn "Nộp bài" để xem kết quả.
- **Xem kết quả** (`ResultFrame`):
  - Hiển thị điểm số (ví dụ: 50.00/50.00 nếu trả lời đúng hết).
  - Nhấn "Quay lại" để trở về `MainFrame`.

### 5. Đăng Xuất

- Nhấn "Đăng Xuất" trong `MainFrame`.
- `MainFrame` ẩn, `LoginFrame` xuất hiện để đăng nhập lại.
- Sau khi đăng nhập, `MainFrame` tái xuất hiện với thông tin người dùng mới.

## Cấu Trúc File Excel

Để thêm bài thi, file Excel (`.xlsx`) phải có định dạng:

| Nội dung câu hỏi | Đáp án đúng | Đáp án sai 1 | Đáp án sai 2 |
|------------------|-------------|--------------|--------------|
| Tác giả của tác phẩm "Tuyên ngôn độc lập" là ai? | Hồ Chí Minh | Nguyễn Đình Chiểu | Nam Cao |
| ... | ... | ... | ... |

- **Hàng 1**: Header (bỏ qua).
- **Cột 1**: Nội dung câu hỏi (chuỗi).
- **Cột 2**: Đáp án đúng (`isCorrect = true`).
- **Cột 3, 4**: Đáp án sai (`isCorrect = false`).
- **Lưu ý**:
  - File phải là `.xlsx`.
  - Các ô không được để trống.
  - Mỗi câu hỏi cần đúng 3 đáp án.