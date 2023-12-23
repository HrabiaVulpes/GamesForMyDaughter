package org.vulpes.games;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Circles extends JComponent implements ActionListener, MouseListener, MouseMotionListener {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 1000;

    Media carHorn;
    MediaPlayer carHornPlayer;
    Media happySound;
    MediaPlayer happySoundPlayer;
    Map<String, Image> images = new HashMap<>();
    java.util.List<GameElement> gameElements = new ArrayList<>();
    Timer timer = new Timer(5, this);

    public Circles() throws IOException {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        carHorn = new Media(new File("src/main/resources", "carHorn.mp3").toURI().toString());
        carHornPlayer = new MediaPlayer(carHorn);
        carHornPlayer.setOnEndOfMedia(() -> {
            carHornPlayer.seek(Duration.ZERO);
            carHornPlayer.pause();
        });

        happySound = new Media(new File("src/main/resources", "bip.mp3").toURI().toString());
        happySoundPlayer = new MediaPlayer(happySound);
        happySoundPlayer.setOnEndOfMedia(() -> {
            happySoundPlayer.seek(Duration.ZERO);
            happySoundPlayer.pause();
        });

        timer.start();
        addCircle();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        gameElements.stream()
                .sorted(Comparator.comparing(gameElement -> gameElement.locY + gameElement.height))
                .forEach(element -> {
                    graphics.setColor(element.color);
                    graphics.fillOval(element.locX, element.locY, element.width, element.height);
                });
    }

    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == timer) {
            repaint();
        }
    }

    private void addCircle() {
        int posX = ThreadLocalRandom.current().nextInt(1, WIDTH);
        int posY = ThreadLocalRandom.current().nextInt(1, HEIGHT);
        int radi = ThreadLocalRandom.current().nextInt(30, 100);

        int r = ThreadLocalRandom.current().nextInt(30, 255);
        int g = ThreadLocalRandom.current().nextInt(30, 255);
        int b = ThreadLocalRandom.current().nextInt(30, 255);

        gameElements.add(new GameElement("Circle", posX - radi, posY - radi, 2 * radi, 2 * radi, new Color(r,g,b)));
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gameElements.stream().anyMatch(element -> element.isInside(e.getX(), e.getY()))) {
            carHornPlayer.play();
            gameElements.clear();
            addCircle();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameElements.stream().anyMatch(element -> element.isInside(e.getX(), e.getY()))) {
            happySoundPlayer.play();
        }
    }
}
