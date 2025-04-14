package com.caycon.view;

import javax.swing.*;
import java.awt.*;

public class ResultFrame extends JFrame {
    public ResultFrame(double score, int totalQuestions) {
        setTitle("Kết Quả Bài Thi");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(new JLabel("Tổng điểm: " + score));
        panel.add(new JLabel("Số câu đúng: " + (int) (score / 10))); // Giả sử mỗi câu 10 điểm
        panel.add(new JLabel("Tổng số câu: " + totalQuestions));

        JButton btnReview = new JButton("Xem lại bài làm");
        panel.add(btnReview);

        add(panel);
    }
}