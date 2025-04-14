package com.caycon.dao;

import java.sql.*;
import java.util.*;

import com.caycon.model.Answer;
import com.caycon.model.Question;
import com.caycon.util.DBConnection;

public class QuestionDAO {
    // Phương thức lấy tất cả câu hỏi và đáp án
    public List<Question> getAllQuestions() throws SQLException {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT q.id, q.content, q.category, q.point, " +
                "a.id AS answer_id, a.content AS answer_content, a.is_correct " +
                "FROM QUESTION q LEFT JOIN ANSWER a ON q.id = a.question_id " +
                "ORDER BY q.id, a.id";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            Map<Integer, Question> questionMap = new HashMap<>();

            while (rs.next()) {
                int qId = rs.getInt("id");
                Question question = questionMap.getOrDefault(qId, new Question(
                        qId,
                        rs.getString("content"),
                        rs.getString("category"),
                        rs.getDouble("point"),
                        new ArrayList<>()));

                int answerId = rs.getInt("answer_id");
                if (!rs.wasNull()) { // Nếu có đáp án
                    Answer answer = new Answer(
                            answerId,
                            qId,
                            rs.getString("answer_content"),
                            rs.getBoolean("is_correct"));
                    question.getAnswers().add(answer);
                }

                questionMap.put(qId, question);
            }

            questions.addAll(questionMap.values());
        }

        return questions;
    }

    // Phương thức lưu câu hỏi (đã có từ trước, giữ nguyên để tham khảo)
    public void saveQuestion(Question question) throws SQLException {
        String insertQuestion = "INSERT INTO QUESTION (content, category, point) VALUES (?, ?, ?)";
        String insertAnswer = "INSERT INTO ANSWER (question_id, content, is_correct) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(insertQuestion, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, question.getContent());
                stmt.setString(2, question.getCategory());
                stmt.setDouble(3, question.getPoint());
                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Thêm câu hỏi thất bại.");
                }

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int questionId = rs.getInt(1);
                        try (PreparedStatement ansStmt = conn.prepareStatement(insertAnswer)) {
                            for (Answer answer : question.getAnswers()) {
                                ansStmt.setInt(1, questionId);
                                ansStmt.setString(2, answer.getContent());
                                ansStmt.setBoolean(3, answer.isCorrect());
                                ansStmt.executeUpdate();
                            }
                        }
                    } else {
                        throw new SQLException("Không lấy được ID câu hỏi.");
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}