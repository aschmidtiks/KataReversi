package gui;

import javax.swing.*;

public class Frame extends JFrame {

    Panel mainPanel;

    public Frame(int columns, int rows) {
        this.setTitle("Kata: Reversi");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocation(0,0);

        mainPanel = new Panel(columns, rows);
        this.add(mainPanel);
        this.pack();
        this.setVisible(true);
    }
}
