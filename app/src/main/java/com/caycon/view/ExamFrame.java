package com.caycon.view;

import javax.swing.*;

import com.caycon.dao.QuestionDAO;
import com.caycon.model.Answer;
import com.caycon.model.Question;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ExamFrame extends JFrame {
    private JLabel questionLabel;
    private JRadioButton option1, option2, option3;
    private ButtonGroup optionGroup;
    private List<Question> questions;
    private JButton[] questionButtons;
    private int currentQuestionIndex = 0;
    private int[] userAnswers; // Lưu answer_id của đáp án được chọn

    public ExamFrame() {
        // Thiết lập frame chính
        setTitle("Bài Thi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Tải câu hỏi từ cơ sở dữ liệu
        try {
            QuestionDAO questionDAO = new QuestionDAO();
            questions = questionDAO.getAllQuestions();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải câu hỏi: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            questions = List.of();
        }

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

        // Tạo nút cho mỗi câu hỏi
        for (int i = 0; i < questions.size(); i++) {
            questionButtons[i] = new JButton("Câu " + (i + 1));
            questionButtons[i].setFont(new Font("Arial", Font.PLAIN, 10));
            questionButtons[i].setActionCommand(String.valueOf(i));
            questionButtons[i].addActionListener(e -> {
                int index = Integer.parseInt(e.getActionCommand());
                saveAnswer();
                updateQuestion(index);
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
        JButton submitButton = new JButton("Submit"); // Nút Submit

        // Xử lý sự kiện cho các nút điều hướng
        firstButton.addActionListener(e -> {
            saveAnswer();
            updateQuestion(0);
        });
        previousButton.addActionListener(e -> {
            if (currentQuestionIndex > 0) {
                saveAnswer();
                updateQuestion(currentQuestionIndex - 1);
            }
        });
        nextButton.addActionListener(e -> {
            if (currentQuestionIndex < questions.size() - 1) {
                saveAnswer();
                updateQuestion(currentQuestionIndex + 1);
            }
        });
        lastButton.addActionListener(e -> {
            saveAnswer();
            updateQuestion(questions.size() - 1);
        });
        submitButton.addActionListener(e -> submitExam()); // Xử lý Submit

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

        // Tải câu hỏi đầu tiên sau khi khởi tạo questionButtons
        if (!questions.isEmpty()) {
            updateQuestion(0);
        }
    }

    // Lưu đáp án được chọn
    private void saveAnswer() {
        if (!questions.isEmpty()) {
            List<Answer> answers = questions.get(currentQuestionIndex).getAnswers();
            if (answers.size() >= 3) {
                if (option1.isSelected()) {
                    userAnswers[currentQuestionIndex] = answers.get(0).getId();
                } else if (option2.isSelected()) {
                    userAnswers[currentQuestionIndex] = answers.get(1).getId();
                } else if (option3.isSelected()) {
                    userAnswers[currentQuestionIndex] = answers.get(2).getId();
                } else {
                    userAnswers[currentQuestionIndex] = 0; // Không chọn đáp án
                }
            }
        }
    }

    // Cập nhật câu hỏi và đáp án
    private void updateQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            currentQuestionIndex = index;
            Question question = questions.get(index);
            questionLabel.setText("Câu " + (index + 1) + ": " + question.getContent());

            // Cập nhật đáp án
            List<Answer> answers = question.getAnswers();
            if (answers.size() >= 3) {
                option1.setText(answers.get(0).getContent());
                option2.setText(answers.get(1).getContent());
                option3.setText(answers.get(2).getContent());

                // Khôi phục lựa chọn trước đó
                optionGroup.clearSelection();
                int selectedAnswerId = userAnswers[currentQuestionIndex];
                for (int i = 0; i < 3; i++) {
                    if (answers.get(i).getId() == selectedAnswerId) {
                        switch (i) {
                            case 0:
                                option1.setSelected(true);
                                break;
                            case 1:
                                option2.setSelected(true);
                                break;
                            case 2:
                                option3.setSelected(true);
                                break;
                        }
                    }
                }
            } else {
                option1.setText("Không có đáp án");
                option2.setText("Không có đáp án");
                option3.setText("Không có đáp án");
            }

            updateQuestionButtonHighlight(index);
        }
    }

    // Đánh dấu nút câu hỏi được chọn
    private void updateQuestionButtonHighlight(int index) {
        if (questionButtons != null && index >= 0 && index < questionButtons.length) {
            for (int i = 0; i < questionButtons.length; i++) {
                if (i == index) {
                    questionButtons[i].setBackground(Color.LIGHT_GRAY);
                } else {
                    questionButtons[i].setBackground(null);
                }
            }
        }
    }

    // Xử lý nộp bài
    private void submitExam() {
        saveAnswer();
        double score = 0;
        for (int i = 0; i < questions.size(); i++) {
            int selectedAnswerId = userAnswers[i];
            for (Answer a : questions.get(i).getAnswers()) {
                if (a.getId() == selectedAnswerId && a.isCorrect()) {
                    score += questions.get(i).getPoint();
                    break;
                }
            }
        }
        dispose();
        new ResultFrame(score, questions.size() * 10).setVisible(true); // Tổng điểm tối đa là 100
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExamFrame demo = new ExamFrame();
            demo.setVisible(true);
        });
    }
}