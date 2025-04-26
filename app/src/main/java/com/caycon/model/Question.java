package com.caycon.model;

import java.util.List;

public class Question {
    private int id; // ID câu hỏi
    private String content; // Nội dung câu hỏi
    private String category; // Danh mục (Toán, Văn, Anh)
    private double point; // Điểm số của câu hỏi
    private int examId; // ID bài thi liên kết
    private List<Answer> answers; // Danh sách các đáp án

    public Question() {
    }

    public Question(int id, String content, String category, double point, List<Answer> answers) {
        this.id = id;
        this.content = content;
        this.category = category;
        this.point = point;
        this.answers = answers;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}