package com.caycon.model;

import java.util.List;

public class Exam {
    private int id;
    private String name;
    private String category;
    private List<Question> questions;

    // Constructor đầy đủ
    public Exam(int id, String name, String category, List<Question> questions) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.questions = questions;
    }

    // Constructor không có ID (dùng khi tạo mới trước khi lưu vào DB)
    public Exam(String name, String category, List<Question> questions) {
        this.name = name;
        this.category = category;
        this.questions = questions;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", questions=" + questions.size() +
                '}';
    }
}