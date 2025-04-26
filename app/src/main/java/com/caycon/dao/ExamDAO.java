package com.caycon.dao;

import java.sql.*;
import java.util.*;

import com.caycon.model.Answer;
import com.caycon.model.Exam;
import com.caycon.model.Question;
import com.caycon.util.DBConnection;

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
}