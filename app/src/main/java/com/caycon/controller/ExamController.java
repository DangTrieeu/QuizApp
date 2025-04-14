package com.caycon.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.caycon.model.Answer;
import com.caycon.model.Question;

public class ExamController {
    private List<Question> questions;
    private Map<Integer, Integer> userAnswers; // questionId -> selectedAnswerId

    public ExamController(List<Question> questions) {
        this.questions = questions;
        this.userAnswers = new HashMap<>();
    }

    public void selectAnswer(int questionId, int answerId) {
        userAnswers.put(questionId, answerId);
    }

    public double calculateScore() {
        double totalScore = 0;
        for (Question q : questions) {
            Integer selectedAnswerId = userAnswers.get(q.getId());
            if (selectedAnswerId != null) {
                for (Answer a : q.getAnswers()) {
                    if (a.getId() == selectedAnswerId && a.isCorrect()) {
                        totalScore += q.getPoint();
                        break;
                    }
                }
            }
        }
        return totalScore;
    }
}