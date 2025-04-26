package com.caycon.view;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

import com.caycon.dao.ExamDAO;
import com.caycon.model.Exam;
import com.caycon.model.User;

public class MainFrame extends JFrame {
    private User user;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public MainFrame(User user) {
        this.user = user;
        setTitle("Ứng Dụng Thi Trắc Nghiệm");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Taskbar bên trái
        JPanel taskbar = new JPanel();
        taskbar.setLayout(new BoxLayout(taskbar, BoxLayout.Y_AXIS));
        taskbar.setPreferredSize(new Dimension(200, 0));
        taskbar.setBackground(new Color(220, 220, 220));

        JButton btnProfile = new JButton("Thông Tin Cá Nhân");
        JButton btnExam = new JButton("Danh sách bài thi");
        JButton btnLogout = new JButton("Đăng Xuất");

        btnProfile.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExam.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnProfile.setMaximumSize(new Dimension(180, 40));
        btnExam.setMaximumSize(new Dimension(180, 40));
        btnLogout.setMaximumSize(new Dimension(180, 40));

        taskbar.add(Box.createVerticalStrut(20));
        taskbar.add(btnProfile);
        taskbar.add(Box.createVerticalStrut(10));
        taskbar.add(btnExam);
        taskbar.add(Box.createVerticalStrut(10));
        taskbar.add(btnLogout);
        taskbar.add(Box.createVerticalGlue());

        // Panel nội dung bên phải
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        JPanel profilePanel = createProfilePanel();
        JPanel examPanel = createExamPanel();
        JPanel logoutPanel = new JPanel();

        contentPanel.add(profilePanel, "profile");
        contentPanel.add(examPanel, "exam");
        contentPanel.add(logoutPanel, "logout");

        add(taskbar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        btnProfile.addActionListener(e -> cardLayout.show(contentPanel, "profile"));
        btnExam.addActionListener(e -> {
            JPanel newExamPanel = createExamPanel(); // Làm mới danh sách bài thi
            contentPanel.add(newExamPanel, "exam");
            cardLayout.show(contentPanel, "exam");
        });
        btnLogout.addActionListener(e -> {
            setVisible(false); // Ẩn MainFrame
            new LoginFrame().setVisible(true); // Mở LoginFrame, truyền MainFrame
        });
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblUsername = new JLabel("Tên đăng nhập: " + user.getUsername());
        JLabel lblRole = new JLabel("Vai trò: " + user.getRole());

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblUsername, gbc);

        gbc.gridy = 1;
        panel.add(lblRole, gbc);

        return panel;
    }

    private JPanel createExamPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Danh Sách Bài Thi", SwingConstants.CENTER);
        panel.add(lblTitle, BorderLayout.NORTH);

        // Danh sách bài thi
        JPanel examListPanel = new JPanel();
        examListPanel.setLayout(new BoxLayout(examListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(examListPanel);

        // Nút thêm và xóa bài thi (chỉ dành cho admin)
        JPanel adminPanel = new JPanel();
        if ("admin".equals(user.getRole())) {
            JButton btnAddExam = new JButton("Thêm Bài Thi");
            JButton btnDeleteExam = new JButton("Xóa Bài Thi");

            btnAddExam.addActionListener(e -> {
                new AddExamFrame(user).setVisible(true);
            });

            btnDeleteExam.addActionListener(e -> {
                try {
                    ExamDAO examDAO = new ExamDAO();
                    List<Exam> exams = examDAO.getAllExams();
                    String[] examNames = exams.stream()
                            .map(exam -> exam.getName() + " (" + exam.getCategory() + ")")
                            .toArray(String[]::new);
                    String selectedExam = (String) JOptionPane.showInputDialog(
                            this,
                            "Chọn bài thi để xóa:",
                            "Xóa Bài Thi",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            examNames,
                            examNames.length > 0 ? examNames[0] : null);
                    if (selectedExam != null) {
                        int examId = exams.stream()
                                .filter(exam -> (exam.getName() + " (" + exam.getCategory() + ")").equals(selectedExam))
                                .findFirst()
                                .map(Exam::getId)
                                .orElse(-1);
                        if (examId != -1) {
                            examDAO.deleteExam(examId);
                            JOptionPane.showMessageDialog(this, "Xóa bài thi thành công!");
                            // Làm mới panel danh sách bài thi
                            JPanel newExamPanel = createExamPanel();
                            contentPanel.add(newExamPanel, "exam");
                            cardLayout.show(contentPanel, "exam");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa bài thi: " + ex.getMessage());
                }
            });

            adminPanel.add(btnAddExam);
            adminPanel.add(btnDeleteExam);
        }

        try {
            ExamDAO examDAO = new ExamDAO();
            List<Exam> exams = examDAO.getAllExams();

            for (Exam exam : exams) {
                JButton btnExam = new JButton(
                        exam.getName() + " (" + exam.getCategory() + ", " + exam.getQuestions().size() + " câu)");
                btnExam.setAlignmentX(Component.LEFT_ALIGNMENT);
                btnExam.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

                btnExam.addActionListener(e -> {
                    dispose();
                    new ExamFrame(exam.getId(), user).setVisible(true);
                });

                examListPanel.add(btnExam);
                examListPanel.add(Box.createVerticalStrut(5));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JLabel lblError = new JLabel("Lỗi khi tải bài thi: " + ex.getMessage());
            examListPanel.add(lblError);
        }

        panel.add(adminPanel, BorderLayout.SOUTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        User user = new User(2, "admin", "123456", "admin");
        SwingUtilities.invokeLater(() -> new MainFrame(user).setVisible(true));
    }
}