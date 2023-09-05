package org.vulpes.games;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CarDriving extends JComponent {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 1000;

    Map<String, Image> images;

    public CarDriving() throws IOException {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        images = new HashMap<>();
        images.put("PlayerCar", ImageIO.read(new File("src/main/resources", "RedCar.png")));


        images.keySet().forEach(
                key -> images.put(key, makeWhiteTransparent(images.get(key)))
        );
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

        g.drawImage(images.get("PlayerCar"), 200, HEIGHT-300,150,300, new Color(0, 0, 0, 0), null);
    }

    private Image makeWhiteTransparent(Image im) {
        ImageFilter filter = new RGBImageFilter() {
            public final int markerRGB = Color.WHITE.getRGB() | 0xFF000000;

            public int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
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
