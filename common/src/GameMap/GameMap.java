package GameMap;

import Entity.WorldObject;
import Tile.Tile;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;

public class GameMap {


    public static int GRID_X_SIZE = 150;
    public static int GRID_Y_SIZE = 100;

    private Tile[][] grid = new Tile[GRID_X_SIZE][GRID_Y_SIZE];


    public Tile get(int x, int y) {
        return grid[x][y];
    }

    public GameMap() {

        MapGenerator generator = new MapGenerator();
//243614137484947419L
        generator.makeMap(); //Optional: add a seed here

        for(int i = 0; i < GRID_X_SIZE; i++)
            for(int j = 0; j < GRID_Y_SIZE; j++)
                grid[i][j] = generator.makeTile(i,j);

    }

    public void update() {
        for (Tile[] arr : grid)
            for (Tile tile : arr)
                tile.update();
    }

    public Set<WorldObject> inBox(int x, int y, int w, int h) {
        Set<WorldObject> objects = new HashSet<>();
        for (int i = 0; i < GRID_X_SIZE; i++) {
            for (int j = 0; j < GRID_Y_SIZE; j++) { //TODO make this actualy only return in the box not everything
                objects.addAll(grid[i][j].getContains());
            }
        }
        return objects;
    }

    public void draw(GraphicsContext gc) {
        for (Tile[] arr : grid)
            for (Tile tile : arr)
                tile.draw(gc);
    }
}