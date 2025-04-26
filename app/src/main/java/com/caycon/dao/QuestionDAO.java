package com.caycon.dao;

import com.caycon.model.Answer;
import com.caycon.model.Question;
import com.caycon.util.DBConnection;

import java.sql.*;
import java.util.*;

public class QuestionDAO {
    public List<Question> getAllQuestions() throws SQLException {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT q.id, q.content, q.category, q.point, q.exam_id, " +
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
                question.setExamId(rs.getInt("exam_id"));
                int answerId = rs.getInt("answer_id");
                if (!rs.wasNull()) {
                    question.getAnswers().add(new Answer(
                            answerId,
                            qId,
                            rs.getString("answer_content"),
                            rs.getBoolean("is_correct")));
                }
                questionMap.put(qId, question);
            }
            questions.addAll(questionMap.values());
        }
        return questions;
    }

    public List<Question> getQuestionsByExamId(int examId) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT q.id, q.content, q.category, q.point, q.exam_id, " +
                "a.id AS answer_id, a.content AS answer_content, a.is_correct " +
                "FROM QUESTION q LEFT JOIN ANSWER a ON q.id = a.question_id " +
                "WHERE q.exam_id = ? " +
                "ORDER BY q.id, a.id";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, examId);
            try (ResultSet rs = stmt.executeQuery()) {
                Map<Integer, Question> questionMap = new HashMap<>();
                while (rs.next()) {
                    int qId = rs.getInt("id");
                    Question question = questionMap.getOrDefault(qId, new Question(
                            qId,
                            rs.getString("content"),
                            rs.getString("category"),
                            rs.getDouble("point"),
                            new ArrayList<>()));
                    question.setExamId(rs.getInt("exam_id"));
                    int answerId = rs.getInt("answer_id");
                    if (!rs.wasNull()) {
                        question.getAnswers().add(new Answer(
                                answerId,
                                qId,
                                rs.getString("answer_content"),
                                rs.getBoolean("is_correct")));
                    }
                    questionMap.put(qId, question);
                }
                questions.addAll(questionMap.values());
            }
        }
        return questions;
    }
}