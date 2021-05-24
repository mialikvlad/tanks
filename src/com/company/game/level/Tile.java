package com.company.game.level;

import com.company.graphics.Sprite;
import com.company.graphics.SpriteSheet;
import com.company.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;

//public class Tile {
//
//    private BufferedImage image;
//    private TileType type;
//
//    protected Tile(BufferedImage image, int scale, TileType type){
//        this.type = type;
//        this.image = Utils.resize(image, image.getWidth() * scale, image.getHeight() * scale);
//    }
//
//    protected void render(Graphics2D g, int x, int y){
//        g.drawImage(image, x, y, null);
//    }
//
//    protected TileType type(){
//        return type;
//    }
//}

public class Tile {

    private BufferedImage	image;
    private TileType		type;

    public static final int	SCALE	= 8;

    Tile(BufferedImage image, int scale, TileType type) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration config = device.getDefaultConfiguration();
        BufferedImage buffy = config.createCompatibleImage(image.getWidth() * scale, image.getHeight() * scale,
                Transparency.TRANSLUCENT);
        Graphics g = buffy.getGraphics();
        g.drawImage(image, 0, 0, image.getWidth() * scale, image.getHeight() * scale, null);
        this.image = buffy;
        g.dispose();
        this.type = type;
        if (type == TileType.GRASS)
            for (int i = 0; i < this.image.getHeight(); i++)
                for (int j = 0; j < this.image.getWidth(); j++) {
                    int pixel = this.image.getRGB(j, i);
                    if ((pixel & 0x00FFFFFF) < 10)
                        this.image.setRGB(j, i, (pixel & 0x00FFFFFF));
                }
    }

    protected void render(Graphics2D g, int x, int y) {
        g.drawImage(image, x, y, null);
    }

    protected TileType type() {
        return type;
    }
}