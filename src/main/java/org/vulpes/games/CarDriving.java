package org.vulpes.games;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class CarDriving extends JComponent implements ActionListener, KeyListener {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 1000;

    public static int PLAYER_CAR_LOC = 200;

    public static int SPEED = 3;
    public static int OBSTACLE_TIMER = 0;

    Map<String, Image> images = new HashMap<>();
    List<GameElement> gameElements = new ArrayList<>();
    Media happySound;
    Media carHorn;
    Timer timer = new Timer(5, this);

    public CarDriving() throws IOException {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        images.put("PlayerCar", ImageIO.read(new File("src/main/resources", "RedCar.png")));

        images.put("Obstacle1", ImageIO.read(new File("src/main/resources", "hole.png")));
        images.put("Obstacle2", ImageIO.read(new File("src/main/resources", "obstacle1.jpeg")));
        images.put("Obstacle3", ImageIO.read(new File("src/main/resources", "obstacle2.jpeg")));

        images.put("Drop", ImageIO.read(new File("src/main/resources", "drop.jpg")));
        images.put("Lamp", ImageIO.read(new File("src/main/resources", "Lamp.jpg")));

        images.put("Tree1", ImageIO.read(new File("src/main/resources", "Tree1.jpg")));
        images.put("Tree2", ImageIO.read(new File("src/main/resources", "Tree2.jpg")));
        images.put("Tree3", ImageIO.read(new File("src/main/resources", "Tree3.jpg")));
        images.put("Tree4", ImageIO.read(new File("src/main/resources", "Tree4.jpg")));
        images.put("Tree5", ImageIO.read(new File("src/main/resources", "Tree5.jpg")));
        images.put("Tree6", ImageIO.read(new File("src/main/resources", "Tree6.jpg")));
        images.put("Tree7", ImageIO.read(new File("src/main/resources", "Tree7.jpg")));

        images.keySet().forEach(
                key -> images.put(key, makeWhiteTransparent(images.get(key)))
        );

        happySound = new Media(new File("src/main/resources", "bip.mp3").toURI().toString());
        carHorn = new Media(new File("src/main/resources", "carHorn.mp3").toURI().toString());

        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(58, 120, 48));
        g.fillRect(0, 0, 200, HEIGHT);

        g.setColor(Color.GRAY);
        g.fillRect(200, 0, 300, HEIGHT);

        g.setColor(new Color(58, 120, 48));
        g.fillRect(500, 0, 200, HEIGHT);

        gameElements.stream()
                .sorted(Comparator.comparing(gameElement -> gameElement.locY + gameElement.height))
                .forEach(element -> g.drawImage(images.get(element.imageName), element.locX, element.locY, element.width, element.height, new Color(0, 0, 0, 0), null));

        g.drawImage(images.get("PlayerCar"), PLAYER_CAR_LOC, HEIGHT - 300, 150, 300, new Color(0, 0, 0, 0), null);
    }

    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == timer && !isCarBlocked()) {
            if (ThreadLocalRandom.current().nextInt(0, 300) == 0) addTreeLeft();
            if (ThreadLocalRandom.current().nextInt(0, 300) == 0) addTreeRight();
            if (ThreadLocalRandom.current().nextInt(0, 300) == 0) addLampLeft();
            if (ThreadLocalRandom.current().nextInt(0, 300) == 0) addLampRight();
            if (ThreadLocalRandom.current().nextInt(0, 300) == 0 && OBSTACLE_TIMER <= 0) addObstacle();
            if (ThreadLocalRandom.current().nextInt(0, 300) == 0) addDrop();
            OBSTACLE_TIMER--;
            moveDown();
            repaint();
        }
    }

    private void addTreeLeft() {
        int treeNum = ThreadLocalRandom.current().nextInt(1, 8);
        gameElements.add(new GameElement("Tree" + treeNum, ThreadLocalRandom.current().nextInt(-20, 20), -500, 200, ThreadLocalRandom.current().nextInt(150, 500)));
    }

    private void addTreeRight() {
        int treeNum = ThreadLocalRandom.current().nextInt(1, 8);
        gameElements.add(new GameElement("Tree" + treeNum, 500 + ThreadLocalRandom.current().nextInt(-20, 20), -500, 200, ThreadLocalRandom.current().nextInt(150, 500)));
    }

    private void addLampLeft() {
        gameElements.add(new GameElement("Lamp", 150, -200, 50, 200));
    }

    private void addLampRight() {
        gameElements.add(new GameElement("Lamp", 500, -200, 50, 200));
    }

    private void addObstacle() {
        int obNum = ThreadLocalRandom.current().nextInt(1, 4);
        gameElements.add(new GameElement("Obstacle" + obNum, 200 + 150 * ThreadLocalRandom.current().nextInt(0, 2), -200, 150, 50));
        OBSTACLE_TIMER = 500;
    }


    private void addDrop() {
        gameElements.add(new GameElement("Drop", 225 + 175 * ThreadLocalRandom.current().nextInt(0, 2), -200, 50, 100));
    }


    private void moveDown() {
        gameElements.forEach(
                gameElement -> {
                    gameElement.locY = gameElement.locY + SPEED;
                }
        );
        gameElements = gameElements.stream()
                .filter(gameElement -> gameElement.locY < HEIGHT)
                .collect(Collectors.toList());

        List<GameElement> dropsToKill = gameElements.stream()
                .filter(gameElement -> gameElement.imageName.equals("Drop"))
                .filter(gameElement -> gameElement.locX > PLAYER_CAR_LOC && gameElement.locX + gameElement.width < PLAYER_CAR_LOC + 150)
                .filter(gameElement -> gameElement.locY + gameElement.height > HEIGHT - 300)
                .toList();

        if (!dropsToKill.isEmpty()) {
            new MediaPlayer(happySound).play();
            gameElements.removeAll(dropsToKill);
        }


    }

    private boolean isCarBlocked() {
        return gameElements.stream()
                .filter(gameElement -> gameElement.imageName.contains("Obstacle"))
                .filter(gameElement -> gameElement.locX + gameElement.width / 2 > PLAYER_CAR_LOC && gameElement.locX + gameElement.width / 2 < PLAYER_CAR_LOC + 150)
                .anyMatch(gameElement -> gameElement.locY + gameElement.height >= HEIGHT - 300);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            CarDriving.PLAYER_CAR_LOC = 200;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            CarDriving.PLAYER_CAR_LOC = 350;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            SPEED = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (isCarBlocked())
                new MediaPlayer(carHorn).play();
            else
                SPEED = 5;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP) {
            SPEED = 3;
        }
    }

    private Image makeWhiteTransparent(Image im) {
        ImageFilter filter = new RGBImageFilter() {

            public int filterRGB(int x, int y, int rgb) {
                int b = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int r = (rgb >> 16) & 0xFF;

                if (r + g + b > 700) {
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
}
