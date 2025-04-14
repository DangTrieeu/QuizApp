package com.caycon.dao;

import java.sql.*;

import com.caycon.model.User;
import com.caycon.util.DBConnection;

public class UserDAO {
    public User authenticate(String username, String password) throws SQLException {
        String query = "SELECT * FROM USER WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // Trong thực tế, so sánh với password đã mã hóa
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"),
                        rs.getString("password"), rs.getString("role"));
            }
        }
        return null;
    }
}
