package com.caycon.controller;

import com.caycon.model.Answer;
import com.caycon.model.Question;
import com.caycon.model.User;
import com.caycon.view.ResultFrame;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamController {
    private List<Question> questions;
    private int[] userAnswers;
    private int currentQuestionIndex;
    private JLabel questionLabel;
    private JRadioButton option1, option2, option3;
    private ButtonGroup optionGroup;
    private JButton[] questionButtons;
    private QuestionController questionController;
    private User user;
    // Thêm bản đồ để ánh xạ JRadioButton với Answer ID
    private Map<JRadioButton, Integer> answerIdMap;

    public ExamController(List<Question> questions, int[] userAnswers, JLabel questionLabel,
            JRadioButton option1, JRadioButton option2, JRadioButton option3,
            ButtonGroup optionGroup, JButton[] questionButtons, User user) {
        this.questions = questions;
        this.userAnswers = userAnswers;
        this.currentQuestionIndex = 0;
        this.questionLabel = questionLabel;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.optionGroup = optionGroup;
        this.questionButtons = questionButtons;
        this.questionController = new QuestionController();
        this.user = user;
        // Khởi tạo bản đồ
        this.answerIdMap = new HashMap<>();
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void saveAnswer() {
        if (!questions.isEmpty()) {
            // Lấy danh sách đáp án đã xáo trộn
            List<Answer> answers = questionController.getShuffledAnswers(questions.get(currentQuestionIndex));
            if (answers.size() >= 3) {
                // Tìm JRadioButton được chọn và lấy ID tương ứng từ answerIdMap
                if (option1.isSelected() && answerIdMap.containsKey(option1)) {
                    userAnswers[currentQuestionIndex] = answerIdMap.get(option1);
                } else if (option2.isSelected() && answerIdMap.containsKey(option2)) {
                    userAnswers[currentQuestionIndex] = answerIdMap.get(option2);
                } else if (option3.isSelected() && answerIdMap.containsKey(option3)) {
                    userAnswers[currentQuestionIndex] = answerIdMap.get(option3);
                } else {
                    userAnswers[currentQuestionIndex] = 0; // Không có đáp án nào được chọn
                }
            }
        }
    }

    public void updateQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            currentQuestionIndex = index;
            Question question = questions.get(index);
            questionLabel.setText("Câu " + (index + 1) + ": " + question.getContent());

            // Xóa ánh xạ cũ
            answerIdMap.clear();

            // Lấy danh sách đáp án đã xáo trộn
            List<Answer> answers = questionController.getShuffledAnswers(question);
            if (answers.size() >= 3) {
                // Cập nhật nội dung và ánh xạ ID cho các tùy chọn
                option1.setText(answers.get(0).getContent());
                answerIdMap.put(option1, answers.get(0).getId());
                option2.setText(answers.get(1).getContent());
                answerIdMap.put(option2, answers.get(1).getId());
                option3.setText(answers.get(2).getContent());
                answerIdMap.put(option3, answers.get(2).getId());

                // Khôi phục lựa chọn trước đó
                optionGroup.clearSelection();
                int selectedAnswerId = userAnswers[currentQuestionIndex];
                for (Map.Entry<JRadioButton, Integer> entry : answerIdMap.entrySet()) {
                    if (entry.getValue() == selectedAnswerId) {
                        entry.getKey().setSelected(true);
                        break;
                    }
                }
            } else {
                option1.setText("Không có đáp án");
                option2.setText("Không có đáp án");
                option3.setText("Không có đáp án");
                answerIdMap.clear();
            }

            updateQuestionButtonHighlight(index);
        }
    }

    public void updateQuestionButtonHighlight(int index) {
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

    public void submitExam(JFrame examFrame) {
        saveAnswer();
        double score = 0;
        for (int i = 0; i < questions.size(); i++) {
            int selectedAnswerId = userAnswers[i];
            // Sử dụng danh sách đáp án gốc để kiểm tra
            for (Answer a : questions.get(i).getAnswers()) {
                if (a.getId() == selectedAnswerId && a.isCorrect()) {
                    score += questions.get(i).getPoint();
                    break;
                }
            }
        }
        examFrame.dispose();
        new ResultFrame(score, questions.size() * 10, user).setVisible(true);
    }
}