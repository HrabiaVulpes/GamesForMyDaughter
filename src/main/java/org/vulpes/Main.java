package org.vulpes;

import org.vulpes.games.CarDriving;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("My First GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        JPanel gamePanel = new JPanel();
        gamePanel.add(new CarDriving());
        frame.getContentPane().add(gamePanel);
        frame.pack();
        frame.setVisible(true);
    }
}