package org.vulpes.games;

public class GameElement {
    public String imageName;
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
}
