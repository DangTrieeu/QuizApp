package com.caycon.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ExamFrame extends JFrame {
    private JLabel lblQuestion;
    private JRadioButton[] rbAnswers;
    private JButton btnNext, btnPrev;
    private JLabel lblTimer;
    private Timer timer;
    private int timeLeft = 600; // 10 phút

    public ExamFrame() {
        setTitle("Bài Thi");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        lblQuestion = new JLabel("Câu hỏi sẽ hiển thị ở đây");
        panel.add(lblQuestion, BorderLayout.NORTH);

        JPanel answerPanel = new JPanel(new GridLayout(4, 1));
        rbAnswers = new JRadioButton[4];
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            rbAnswers[i] = new JRadioButton("Đáp án " + (i + 1));
            group.add(rbAnswers[i]);
            answerPanel.add(rbAnswers[i]);
        }
        panel.add(answerPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        btnPrev = new JButton("Trước");
        btnNext = new JButton("Tiếp");
        lblTimer = new JLabel("Thời gian: 10:00");
        controlPanel.add(btnPrev);
        controlPanel.add(btnNext);
        controlPanel.add(lblTimer);
        panel.add(controlPanel, BorderLayout.SOUTH);

        add(panel);

        // Timer
        timer = new Timer(1000, e -> {
            timeLeft--;
            int minutes = timeLeft / 60;
            int seconds = timeLeft % 60;
            lblTimer.setText(String.format("Thời gian: %02d:%02d", minutes, seconds));
            if (timeLeft <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Hết thời gian!");
                // Xử lý nộp bài
            }
        });
        timer.start();
    }
}
