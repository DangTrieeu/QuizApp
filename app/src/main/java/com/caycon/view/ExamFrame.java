package com.caycon.view;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

import com.caycon.dao.QuestionDAO;
import com.caycon.model.Question;
import com.caycon.model.User;
import com.caycon.controller.ExamController;

public class ExamFrame extends JFrame {
    private User user;
    private JLabel questionLabel;
    private JRadioButton option1, option2, option3;
    private ButtonGroup optionGroup;
    private List<Question> questions;
    private JButton[] questionButtons;
    private int[] userAnswers;
    private ExamController examController;

    public ExamFrame(int examId, User user) {
        this.user = user;
        // Thiết lập frame chính
        setTitle("Bài Thi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        JLabel lblUser = new JLabel("Người dùng: " + user.getUsername());
        add(lblUser, BorderLayout.NORTH);

        // Tải câu hỏi từ cơ sở dữ liệu
        try {
            QuestionDAO questionDAO = new QuestionDAO();
            questions = questionDAO.getQuestionsByExamId(examId);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải câu hỏi: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            questions = List.of();
        }
        userAnswers = new int[questions.size()];
        examController = new ExamController(questions, userAnswers, questionLabel, option1, option2, option3,
                optionGroup, questionButtons, user);

        // Khởi tạo mảng lưu đáp án
        userAnswers = new int[questions.size()];

        // Tạo JSplitPane với tỷ lệ 7:3
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(0.7);
        splitPane.setResizeWeight(0.7);

        // Panel bên trái cho câu hỏi và đáp án
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Câu hỏi"));

        // Nhãn câu hỏi
        questionLabel = new JLabel(questions.isEmpty() ? "Không có câu hỏi" : "");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        leftPanel.add(questionLabel);
        leftPanel.add(Box.createVerticalStrut(20));

        // Tùy chọn đáp án
        option1 = new JRadioButton();
        option2 = new JRadioButton();
        option3 = new JRadioButton();

        Font optionFont = new Font("Arial", Font.PLAIN, 16);
        option1.setFont(optionFont);
        option2.setFont(optionFont);
        option3.setFont(optionFont);

        optionGroup = new ButtonGroup();
        optionGroup.add(option1);
        optionGroup.add(option2);
        optionGroup.add(option3);

        leftPanel.add(option1);
        leftPanel.add(option2);
        leftPanel.add(option3);

        // Panel bên phải cho danh sách câu hỏi
        JPanel questionListPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        questionListPanel.setBorder(BorderFactory.createTitledBorder("Danh sách câu hỏi"));
        questionButtons = new JButton[questions.size()];

        // Khởi tạo ExamController
        examController = new ExamController(questions, userAnswers, questionLabel, option1, option2, option3,
                optionGroup, questionButtons, user);

        // Tạo nút cho mỗi câu hỏi
        for (int i = 0; i < questions.size(); i++) {
            questionButtons[i] = new JButton("Câu " + (i + 1));
            questionButtons[i].setFont(new Font("Arial", Font.PLAIN, 10));
            questionButtons[i].setActionCommand(String.valueOf(i));
            questionButtons[i].addActionListener(e -> {
                int index = Integer.parseInt(e.getActionCommand());
                examController.saveAnswer();
                examController.updateQuestion(index);
            });
            questionListPanel.add(questionButtons[i]);
        }

        // Thêm danh sách câu hỏi vào scroll pane
        JScrollPane scrollPane = new JScrollPane(questionListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Panel dưới cùng cho nút điều hướng
        JPanel buttonPanel = new JPanel();
        JButton firstButton = new JButton("First");
        JButton previousButton = new JButton("<-");
        JButton nextButton = new JButton("->");
        JButton lastButton = new JButton("Last");
        JButton submitButton = new JButton("Submit");

        // Xử lý sự kiện cho các nút điều hướng
        firstButton.addActionListener(e -> {
            examController.saveAnswer();
            examController.updateQuestion(0);
        });
        previousButton.addActionListener(e -> {
            if (examController.getCurrentQuestionIndex() > 0) {
                examController.saveAnswer();
                examController.updateQuestion(examController.getCurrentQuestionIndex() - 1);
            }
        });
        nextButton.addActionListener(e -> {
            if (examController.getCurrentQuestionIndex() < questions.size() - 1) {
                examController.saveAnswer();
                examController.updateQuestion(examController.getCurrentQuestionIndex() + 1);
            }
        });
        lastButton.addActionListener(e -> {
            examController.saveAnswer();
            examController.updateQuestion(questions.size() - 1);
        });
        submitButton.addActionListener(e -> examController.submitExam(this));

        buttonPanel.add(firstButton);
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(lastButton);
        buttonPanel.add(submitButton);

        // Thêm các panel vào split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(scrollPane);

        // Tạo panel chính với BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Thêm panel chính vào frame
        add(mainPanel);

        // Tải câu hỏi đầu tiên
        if (!questions.isEmpty()) {
            examController.updateQuestion(0);
        }
    }

    public static void main(String[] args) {
        User user = new User(2, "admin", "123456", "admin");
        SwingUtilities.invokeLater(() -> {
            ExamFrame demo = new ExamFrame(1, user);
            demo.setVisible(true);
        });
    }
}