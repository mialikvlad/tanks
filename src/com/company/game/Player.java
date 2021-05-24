package com.company.game;

import com.company.IO.Input;
import com.company.game.level.Level;
import com.company.graphics.Sprite;
import com.company.graphics.SpriteSheet;
import com.company.graphics.TextureAtlas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.company.game.Game.reset;

public class Player extends Entity{

//    public static final int SPRITE_SCALE = 16;
//    public static final int SPRITES_PER_HEADING = 1;
    private static final int	PROTECTION_TIME	= 4000;
    private static final float	APPEARANCE_X	= Entity.SPRITE_SCALE * Game.SCALE * 4;
    private static final float	APPEARANCE_Y	= Entity.SPRITE_SCALE * Game.SCALE * 12;

    private enum Heading{
        NORTH(0 * SPRITE_SCALE, 7 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE),
        EAST(6 * SPRITE_SCALE, 7 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE),
        SOUTH(4 * SPRITE_SCALE, 7 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE),
        WEST(2 * SPRITE_SCALE, 7 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE);

        private int x, y, h, w;

        Heading(int x, int y, int h, int w){
            this.x = x;
            this.y = y;
            this.h = h;
            this.w = w;
        }

        protected BufferedImage texture(TextureAtlas atlas){
            return atlas.cut(x, y, w, h);
        }
    }

    private static int				lives;
    private static int				strength;

    private Heading					heading;
    private Map<Heading, Sprite>	spriteMap;
    private float					speed;
    private float					bulletSpeed;
    private Bullet					bullet;
    private boolean					isProtected;
    private List<Sprite> protectionList;

    public Player(float scale, float speed, TextureAtlas atlas, Level lvl) {
        super(EntityType.Player, APPEARANCE_X, APPEARANCE_Y, scale, atlas, lvl);

        heading = Heading.NORTH;
        spriteMap = new HashMap<Heading, Sprite>();
        this.speed = speed;
        bulletSpeed = 8;
        lives = 2;
        strength = 1;


        for(Heading h : Heading.values()){
            SpriteSheet sheet = new SpriteSheet(h.texture(atlas), SPRITES_PER_HEADING, SPRITE_SCALE);
            Sprite sprite = new Sprite(sheet, scale);
            spriteMap.put(h, sprite);
        }
    }

    @Override
    public void update(Input input) {
//        if (evolving)
//            return;
        float newX = x;
        float newY = y;

        if(input.getKey(KeyEvent.VK_UP)){
            newY -= speed;
            x = newX = (Math.round(newX / Level.SCALED_TILE_SIZE)) * Level.SCALED_TILE_SIZE;
            heading = Heading.NORTH;
        }else if(input.getKey(KeyEvent.VK_DOWN)){
            newY += speed;
            x = newX = (Math.round(newX / Level.SCALED_TILE_SIZE)) * Level.SCALED_TILE_SIZE;
            heading = Heading.SOUTH;
        }else if(input.getKey(KeyEvent.VK_RIGHT)){
            newX += speed;
            y = newY = (Math.round(newY / Level.SCALED_TILE_SIZE)) * Level.SCALED_TILE_SIZE;
            heading = Heading.EAST;
        }else if(input.getKey(KeyEvent.VK_LEFT)){
            newX -= speed;
            y = newY = (Math.round(newY / Level.SCALED_TILE_SIZE)) * Level.SCALED_TILE_SIZE;
            heading = Heading.WEST;
        }

        if(newX < 0){
            newX = 0;
        }else if(newX >= Game.WIDTH - SPRITE_SCALE * scale){
            newX = Game.WIDTH - SPRITE_SCALE * scale;
        }

        if(newY < 0){
            newY = 0;
        }else if(newY >= Game.HEIGHT - SPRITE_SCALE * scale){
            newY = Game.HEIGHT - SPRITE_SCALE * scale;
        }

//        x = newX;
//        y = newY;

        switch (heading){
            case NORTH:
                if (canMove(newX, newY, newX + (SPRITE_SCALE * scale / 2), newY, newX + (SPRITE_SCALE * scale), newY)
                        && !intersectsEnemy(newX, newY)) {
                    x = newX;
                    y = newY;
                }
                break;
            case SOUTH:
                if(canMove(newX, newY + (SPRITE_SCALE * scale), newX + (SPRITE_SCALE * scale / 2), newY + (SPRITE_SCALE * scale), newX + (SPRITE_SCALE * scale), newY + (SPRITE_SCALE * scale))
                        && !intersectsEnemy(newX, newY)){
                    x = newX;
                    y = newY;
                }
                break;
            case EAST:
                if (canMove(newX + (SPRITE_SCALE * scale), newY, newX + (SPRITE_SCALE * scale),
                        newY + (SPRITE_SCALE * scale / 2), newX + (SPRITE_SCALE * scale), newY + (SPRITE_SCALE * scale))
                        && !intersectsEnemy(newX, newY)) {
                    x = newX;
                    y = newY;
                }
                break;
            case WEST:
                if (canMove(newX, newY, newX, newY + (SPRITE_SCALE * scale / 2), newX, newY + (SPRITE_SCALE * scale))
                        && !intersectsEnemy(newX, newY)) {
                    x = newX;
                    y = newY;
                }
                break;
        }

        List<Bullet> bullets = Game.getBullets(EntityType.Enemy);
        if (bullets != null) {
            for (Bullet enemyBullet : bullets) {
                if (getRectangle().intersects(enemyBullet.getRectangle()) && enemyBullet.isActive()) {
                    if (!isProtected)
                        isAlive = false;
                    enemyBullet.setInactive();
                }

            }

        }

        if (input.getKey(KeyEvent.VK_SPACE)) {
            if (bullet == null || !bullet.isActive()) {
                if (Game.getBullets(EntityType.Player).size() == 0) {
                    bullet = new Bullet(x, y, scale, bulletSpeed, heading.toString().substring(0, 4), atlas, lvl,
                            EntityType.Player);
                }
            }
        }
    }

    private boolean intersectsEnemy(float newX, float newY) {
        List<Enemy> enemyList = Game.getEnemies();
        Rectangle2D.Float rect = getRectangle(newX, newY);
        for (Enemy enemy : enemyList) {
            if (rect.intersects(enemy.getRectangle()))
                return true;
        }
        return false;
    }

    @Override
    public void render(Graphics2D g) {
        if (evolving) {
            drawEvolving(g);
            return;
        }
        spriteMap.get(heading).render(g, x, y);
    }

    @Override
    public void drawExplosion(Graphics2D g) {
        super.drawExplosion(g);
        if (--lives >= 0)
            reset();
        else
            Game.setGameOver();
    }

    public void reset() {
        this.x = APPEARANCE_X;
        this.y = APPEARANCE_Y;
        isAlive = true;
        evolving = true;
        isProtected = true;
        createdTime = System.currentTimeMillis();
        strength = 1;
        heading = Heading.NORTH;

    }

    public boolean hasMoreLives() {
        return lives >= 0;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    public static int getPlayerLives() {
        return lives;
    }

    public static int getPlayerStrength() {
        return strength;
    }
}
