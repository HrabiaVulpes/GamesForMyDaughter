package org.vulpes;

import com.sun.javafx.application.PlatformImpl;
import org.vulpes.games.CarDriving;
import org.vulpes.games.Circles;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        PlatformImpl.startup(() -> {});
        JFrame frame = new JFrame("My First GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        JPanel gamePanel = new JPanel();
        Circles game = new Circles();
        gamePanel.add(game);
        gamePanel.addMouseListener(game);
        gamePanel.addMouseMotionListener(game);
        frame.getContentPane().add(gamePanel);
        frame.pack();
        frame.setVisible(true);
    }
}