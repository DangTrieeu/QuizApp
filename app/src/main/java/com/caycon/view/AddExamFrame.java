package com.caycon.view;

import com.caycon.dao.ExamDAO;
import com.caycon.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AddExamFrame extends JFrame {
    private User user;

    public AddExamFrame(User user) {
        this.user = user;
        setTitle("Thêm Bài Thi");
        setSize(400, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tên bài thi
        JLabel lblName = new JLabel("Tên bài thi:");
        JTextField txtName = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblName, gbc);
        gbc.gridx = 1;
        panel.add(txtName, gbc);

        // Danh mục
        JLabel lblCategory = new JLabel("Danh mục:");
        String[] categories = { "Toán", "Văn", "Anh" };
        JComboBox<String> cbCategory = new JComboBox<>(categories);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblCategory, gbc);
        gbc.gridx = 1;
        panel.add(cbCategory, gbc);

        // Nút lưu
        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> {
            String name = txtName.getText().trim();
            String category = (String) cbCategory.getSelectedItem();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên bài thi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                ExamDAO examDAO = new ExamDAO();
                examDAO.addExam(name, category);
                JOptionPane.showMessageDialog(this, "Thêm bài thi thành công!");
                dispose();
                new MainFrame(user).setVisible(true); // Quay lại MainFrame
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm bài thi: " + ex.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnSave, gbc);

        add(panel);
    }
}