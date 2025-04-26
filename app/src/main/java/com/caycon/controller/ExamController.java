package com.caycon.controller;

import com.caycon.model.Answer;
import com.caycon.model.Question;
import com.caycon.view.ResultFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ExamController {
    private List<Question> questions;
    private int[] userAnswers;
    private int currentQuestionIndex;
    private JLabel questionLabel;
    private JRadioButton option1, option2, option3;
    private ButtonGroup optionGroup;
    private JButton[] questionButtons;

    public ExamController(List<Question> questions, int[] userAnswers, JLabel questionLabel,
            JRadioButton option1, JRadioButton option2, JRadioButton option3,
            ButtonGroup optionGroup, JButton[] questionButtons) {
        this.questions = questions;
        this.userAnswers = userAnswers;
        this.currentQuestionIndex = 0;
        this.questionLabel = questionLabel;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.optionGroup = optionGroup;
        this.questionButtons = questionButtons;
    }

    // lấy vị trí đang đứng
    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    // Lưu đáp án được chọn
    public void saveAnswer() {
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
    public void updateQuestion(int index) {
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

    // Xử lý nộp bài
    public void submitExam(JFrame examFrame) {
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
        examFrame.dispose();
        new ResultFrame(score, questions.size() * 10).setVisible(true); // Tổng điểm tối đa là 100
    }
}