package org.vulpes;

import com.sun.javafx.application.PlatformImpl;
import org.vulpes.games.CarDriving;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        PlatformImpl.startup(() -> {});
        JFrame frame = new JFrame("My First GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        JPanel gamePanel = new JPanel();
        CarDriving game = new CarDriving();
        gamePanel.add(game);
        frame.getContentPane().add(gamePanel);
        frame.addKeyListener(game);
        frame.pack();
        frame.setVisible(true);
    }
}