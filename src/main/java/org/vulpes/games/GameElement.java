package org.vulpes.games;

import java.awt.*;

public class GameElement {
    public String imageName;
    public Color color;
    public int locX;
    public int locY;
    public int width;
    public int height;

    public GameElement(String imageName, int locX, int locY, int width, int height) {
        this.imageName = imageName;
        this.locX = locX;
        this.locY = locY;
        this.width = width;
        this.height = height;
    }

    public GameElement(String imageName, int locX, int locY, int width, int height, Color color) {
        this.imageName = imageName;
        this.locX = locX;
        this.locY = locY;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public boolean isInside(int x, int y){
        if (x < locX) return false;
        if (x > locX+width) return false;
        if (y < locY) return false;
        if (y > locY+height) return false;

        return true;
    }
}
