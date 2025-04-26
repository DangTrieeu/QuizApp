package com.caycon.view;

import com.caycon.dao.ExamDAO;
import com.caycon.model.User;
import com.caycon.util.ExcelReader;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import com.caycon.model.Question;

public class AddExamFrame extends JFrame {
    private User user;
    private JTextField txtName;
    private JComboBox<String> cbCategory;
    private JLabel lblFile;
    private File selectedFile;

    public AddExamFrame(User user) {
        this.user = user;
        setTitle("Thêm Bài Thi");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tên bài thi
        JLabel lblName = new JLabel("Tên bài thi:");
        txtName = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblName, gbc);
        gbc.gridx = 1;
        panel.add(txtName, gbc);

        // Danh mục
        JLabel lblCategory = new JLabel("Danh mục:");
        String[] categories = { "Toán", "Văn", "Anh" };
        cbCategory = new JComboBox<>(categories);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblCategory, gbc);
        gbc.gridx = 1;
        panel.add(cbCategory, gbc);

        // Chọn file Excel
        JLabel lblFileLabel = new JLabel("File Excel:");
        JButton btnChooseFile = new JButton("Chọn file");
        lblFile = new JLabel("Chưa chọn file");
        btnChooseFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel files", "xlsx"));
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                try {
                    ExcelReader reader = new ExcelReader();
                    List<Question> questions = reader.readQuestions(selectedFile.getAbsolutePath());
                    lblFile.setText(selectedFile.getName() + " (" + questions.size() + " câu)");
                } catch (Exception ex) {
                    lblFile.setText(selectedFile.getName());
                    JOptionPane.showMessageDialog(this, "Lỗi khi đọc file: " + ex.getMessage(), "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblFileLabel, gbc);
        gbc.gridx = 1;
        panel.add(btnChooseFile, gbc);
        gbc.gridy = 3;
        panel.add(lblFile, gbc);

        // Nút lưu
        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> {
            String name = txtName.getText().trim();
            String category = (String) cbCategory.getSelectedItem();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên bài thi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (selectedFile == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                ExcelReader reader = new ExcelReader();
                List<Question> questions = reader.readQuestions(selectedFile.getAbsolutePath());
                if (questions.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "File Excel không chứa câu hỏi nào!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ExamDAO examDAO = new ExamDAO();
                examDAO.addExamWithQuestions(name, category, selectedFile.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Thêm bài thi với " + questions.size() + " câu hỏi thành công!");
                dispose();
                new MainFrame(user).setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm bài thi: " + ex.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnSave, gbc);

        add(panel);
    }
}