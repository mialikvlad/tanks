package com.company.graphics;

import com.company.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite {
    private BufferedImage image;
    private SpriteSheet sheet;
    private float scale;

    public Sprite(SpriteSheet sheet, float scale){
        this.sheet = sheet;
        this.scale = scale;
        image = sheet.getSprite(0);
        image = Utils.resize(image, (int)(image.getWidth() * scale), (int)(image.getHeight() * scale));
    }

    public void render(Graphics2D g, float x, float y){
        g.drawImage(image, (int)x, (int)y, null);
    }
}
