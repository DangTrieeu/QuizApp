package com.caycon;

import javax.swing.SwingUtilities;

import com.caycon.view.LoginFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

}