package com.caycon.util;

import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/exam_db";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // public static void main(String[] args) {
    // Connection connection = null;
    // try {
    // // Kết nối đến MySQL
    // connection = DriverManager.getConnection(URL, USER, PASSWORD);
    // System.out.println("Kết nối đến MySQL thành công!");

    // // (Tùy chọn) Kiểm tra xem database có tồn tại không
    // if (connection != null) {
    // System.out.println("Database đang kết nối: " + connection.getCatalog());
    // }
    // } catch (SQLException e) {
    // System.err.println("Kết nối thất bại! Lỗi: " + e.getMessage());
    // e.printStackTrace();
    // } finally {
    // // Đóng kết nối nếu có
    // if (connection != null) {
    // try {
    // connection.close();
    // System.out.println("Đã đóng kết nối.");
    // } catch (SQLException e) {
    // System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
    // }
    // }
    // }
    // }
}
