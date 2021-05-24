package com.company.game.level;

import com.company.game.Game;
import com.company.graphics.TextureAtlas;
import com.company.utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Level {

    public static final int TILE_SCALE = 8;
    public static final int TILE_IN_GAME_SCALE = 3;
    public static final int SCALED_TILE_SIZE = TILE_SCALE * TILE_IN_GAME_SCALE;
    public static final int TILES_IN_WIDTH = Game.WIDTH / SCALED_TILE_SIZE;
    public static final int TILES_IN_HEIGHT = Game.HEIGHT / SCALED_TILE_SIZE;


    private Integer[][] tileMap;
    private Map<TileType, Tile> tiles;
    private List<Point> grassCords;
    private int	count;
    private TextureAtlas atlas;

    public Level(TextureAtlas atlas, int stage){
        tiles = new HashMap<TileType, Tile>();
        count = 0;
        this.atlas = atlas;
        tileMap = new Integer[TILES_IN_WIDTH][TILES_IN_HEIGHT];

        tiles.put(TileType.BRICK, new Tile(atlas.cut(32 * TILE_SCALE, 0 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.BRICK));
        tiles.put(TileType.METAL, new Tile(atlas.cut(32 * TILE_SCALE, 2 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.METAL));
        tiles.put(TileType.WATER, new Tile(atlas.cut(34 * TILE_SCALE, 6 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.WATER));
        tiles.put(TileType.GRASS, new Tile(atlas.cut(34 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.GRASS));
        tiles.put(TileType.ICE, new Tile(atlas.cut(32 * TILE_SCALE, 6 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.ICE));
        tiles.put(TileType.EMPTY, new Tile(atlas.cut(36 * TILE_SCALE, 6 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.EMPTY));

        tileMap = Utils.levelParser("res/lvl" + stage + ".lvl");
        grassCords = new ArrayList<Point>();
//        for(int i = 0; i < tileMap.length; i++){
//            for(int j = 0; j < tileMap[i].length; j++){
//                Tile tile = tiles.get(TileType.fromNumeric(tileMap[i][j]));
//                if(tile.type() == TileType.GRASS)
//                    grassCords.add(new Point(j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE));
//            }
//        }
        for (int i = 0; i < tileMap.length; i++)
            for (int j = 0; j < tileMap[i].length; j++) {
                if (tileMap[i][j] == TileType.GRASS.numeric())
                    grassCords.add(new Point(j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE));
            }
    }

    public void update(int tileX, int tileY) {
        tileMap[tileY][tileX] = TileType.EMPTY.numeric();
    }

    public void render(Graphics2D g){
        count = ++count % 20;

        for (int i = 0; i < tileMap.length; i++)
            for (int j = 0; j < tileMap[i].length; j++) {
                Tile tile = tiles.get(TileType.fromNumeric(tileMap[i][j]));
                if (tile.type() == TileType.WATER && count < 10) {
                    tiles.get(TileType.fromNumeric(5)).render(g, j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE);
                } else {
                    if (tile.type() != TileType.GRASS) {

                        tile.render(g, j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE);

                    }
                }
            }
    }

    public void renderGrass(Graphics2D g){
        for(Point p : grassCords){
            tiles.get(TileType.GRASS).render(g, p.x, p.y);
        }
    }

    public Integer[][] getTileMap() {
        return tileMap;
    }
}
