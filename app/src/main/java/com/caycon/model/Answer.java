package com.caycon.model;

public class Answer {
    private int id; // ID đáp án
    private int questionId; // ID câu hỏi liên kết
    private String content; // Nội dung đáp án
    private boolean isCorrect; // Trạng thái đúng/sai

    // Constructor đầy đủ
    public Answer(int id, int questionId, String content, boolean isCorrect) {
        this.id = id;
        this.questionId = questionId;
        this.content = content;
        this.isCorrect = isCorrect;
    }

    // Constructor không có ID (dùng khi tạo mới đáp án trước khi lưu vào DB)
    public Answer(int questionId, String content, boolean isCorrect) {
        this.questionId = questionId;
        this.content = content;
        this.isCorrect = isCorrect;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", questionId=" + questionId +
                ", content='" + content + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
