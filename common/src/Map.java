import javafx.scene.canvas.GraphicsContext;

import java.util.HashSet;
import java.util.Set;

public class Map {


    public static int GRID_X_SIZE = 100;
    public static int GRID_Y_SIZE = 100;

    private Tile[][] grid = new Tile[GRID_X_SIZE][GRID_Y_SIZE];


    public Tile get(int x, int y) {
        return grid[x][y];
    }

    public Map() {
        //test map of all "grass"
        for (int i = 0; i < GRID_X_SIZE; i++) {
            for (int j = 0; j < GRID_Y_SIZE; j++) {

                if (Math.random() > 0.1)
                    if (Math.random() > 0.2)
                        grid[i][j] = new Grass(i, j);
                    else
                        grid[i][j] = new Wall(i, j);
                else if (Math.random() > 0.3)
                    grid[i][j] = new MetalTile(i, j);
                else
                    grid[i][j] = new Water(i, j);
            }
        }
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
