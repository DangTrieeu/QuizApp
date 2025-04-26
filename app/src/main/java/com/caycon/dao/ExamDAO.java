package com.caycon.dao;

import java.sql.*;
import java.util.*;

import com.caycon.model.Answer;
import com.caycon.model.Exam;
import com.caycon.model.Question;
import com.caycon.util.DBConnection;
import com.caycon.util.ExcelReader;

public class ExamDAO {
    public List<Exam> getAllExams() throws SQLException {
        List<Exam> exams = new ArrayList<>();
        String examQuery = "SELECT id, name, category FROM EXAM";
        String questionQuery = "SELECT q.id, q.content, q.category, q.point, q.exam_id, " +
                "a.id AS answer_id, a.content AS answer_content, a.is_correct " +
                "FROM QUESTION q " +
                "LEFT JOIN ANSWER a ON q.id = a.question_id " +
                "WHERE q.exam_id = ? " +
                "ORDER BY q.id, a.id";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement examStmt = conn.prepareStatement(examQuery);
                ResultSet examRs = examStmt.executeQuery()) {

            while (examRs.next()) {
                int examId = examRs.getInt("id");
                String name = examRs.getString("name");
                String category = examRs.getString("category");

                // Lấy danh sách câu hỏi cho bài thi
                List<Question> questions = new ArrayList<>();
                try (PreparedStatement questionStmt = conn.prepareStatement(questionQuery)) {
                    questionStmt.setInt(1, examId);
                    try (ResultSet questionRs = questionStmt.executeQuery()) {
                        Map<Integer, Question> questionMap = new HashMap<>();
                        while (questionRs.next()) {
                            int qId = questionRs.getInt("id");
                            Question question = questionMap.getOrDefault(qId, new Question(
                                    qId,
                                    questionRs.getString("content"),
                                    questionRs.getString("category"),
                                    questionRs.getDouble("point"),
                                    new ArrayList<>()));
                            question.setExamId(questionRs.getInt("exam_id"));

                            int answerId = questionRs.getInt("answer_id");
                            if (!questionRs.wasNull()) {
                                question.getAnswers().add(new Answer(
                                        answerId,
                                        qId,
                                        questionRs.getString("answer_content"),
                                        questionRs.getBoolean("is_correct")));
                            }
                            questionMap.put(qId, question);
                        }
                        questions.addAll(questionMap.values());
                    }
                }

                exams.add(new Exam(examId, name, category, questions));
            }
        }

        return exams;
    }

    public void addExam(String name, String category) throws SQLException {
        String query = "INSERT INTO EXAM (name, category) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.executeUpdate();
        }
    }

    public void addExamWithQuestions(String name, String category, String excelFilePath) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Thêm bài thi
            String examQuery = "INSERT INTO EXAM (name, category) VALUES (?, ?)";
            int examId;
            try (PreparedStatement examStmt = conn.prepareStatement(examQuery, Statement.RETURN_GENERATED_KEYS)) {
                examStmt.setString(1, name);
                examStmt.setString(2, category);
                examStmt.executeUpdate();
                try (ResultSet rs = examStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        examId = rs.getInt(1);
                    } else {
                        throw new SQLException("Không lấy được examId.");
                    }
                }
            }

            // Đọc câu hỏi từ Excel
            ExcelReader excelReader = new ExcelReader();
            List<Question> questions = excelReader.readQuestions(excelFilePath);
            if (questions.isEmpty()) {
                conn.rollback();
                throw new Exception("File Excel không chứa câu hỏi hợp lệ.");
            }

            // Thêm câu hỏi và đáp án
            String questionQuery = "INSERT INTO QUESTION (content, category, point, exam_id) VALUES (?, ?, ?, ?)";
            String answerQuery = "INSERT INTO ANSWER (question_id, content, is_correct) VALUES (?, ?, ?)";
            for (Question question : questions) {
                // Thêm câu hỏi
                int questionId;
                try (PreparedStatement questionStmt = conn.prepareStatement(questionQuery,
                        Statement.RETURN_GENERATED_KEYS)) {
                    questionStmt.setString(1, question.getContent());
                    questionStmt.setString(2, category); // Dùng category từ bài thi
                    questionStmt.setDouble(3, question.getPoint());
                    questionStmt.setInt(4, examId);
                    questionStmt.executeUpdate();
                    try (ResultSet rs = questionStmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            questionId = rs.getInt(1);
                        } else {
                            throw new SQLException("Không lấy được questionId.");
                        }
                    }
                }

                // Thêm đáp án
                try (PreparedStatement answerStmt = conn.prepareStatement(answerQuery)) {
                    for (Answer answer : question.getAnswers()) {
                        answerStmt.setInt(1, questionId);
                        answerStmt.setString(2, answer.getContent());
                        answerStmt.setBoolean(3, answer.isCorrect());
                        answerStmt.executeUpdate();
                    }
                }
            }

            conn.commit(); // Commit transaction
        } catch (Exception e) {
            try (Connection conn = DBConnection.getConnection()) {
                conn.rollback(); // Rollback nếu có lỗi
            }
            throw e;
        }
    }

    public void deleteExam(int examId) throws SQLException {
        String query = "DELETE FROM EXAM WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, examId);
            stmt.executeUpdate();
        }
    }
}