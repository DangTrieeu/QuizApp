package com.caycon.controller;

import com.caycon.model.Answer;
import com.caycon.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionController {
    /**
     * Lấy danh sách đáp án đã xáo trộn của một câu hỏi.
     *
     * @param question Câu hỏi cần lấy đáp án
     * @return Danh sách đáp án đã xáo trộn
     */
    public List<Answer> getShuffledAnswers(Question question) {
        if (question == null || question.getAnswers() == null) {
            return new ArrayList<>();
        }
        // Tạo bản sao của danh sách đáp án để không thay đổi danh sách gốc
        List<Answer> shuffledAnswers = new ArrayList<>(question.getAnswers());
        // Xáo trộn danh sách
        Collections.shuffle(shuffledAnswers);
        return shuffledAnswers;
    }
}