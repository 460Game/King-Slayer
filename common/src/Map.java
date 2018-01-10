import javafx.scene.canvas.GraphicsContext;

import java.util.*;

public class Map {


    public static int GRID_X_SIZE = 100;
    public static int GRID_Y_SIZE = 100;
    public static int DIST_MAX = 143;;
    public static int FEATURE_SIZE = 6;

    private Tile[][] grid = new Tile[GRID_X_SIZE][GRID_Y_SIZE];


    public Tile get(int x, int y) {
        return grid[x][y];
    }

    public Map() {

        for (int i = 0; i < GRID_X_SIZE; i++) {
            for (int j = 0; j < GRID_Y_SIZE; j++) {
                grid[i][j] = new TileWall(i, j);
            }
        }

        //Pick

        double[][] w = new double[GRID_X_SIZE][GRID_Y_SIZE];
        for (int i = 0; i < GRID_X_SIZE; i++)
            for (int j = 0; j < GRID_Y_SIZE; j++) {
                if (i > FEATURE_SIZE/2 && i < GRID_X_SIZE - FEATURE_SIZE/2 && j > FEATURE_SIZE/2 && j < GRID_Y_SIZE - FEATURE_SIZE/2) {
                    w[i][j] = 1 - 0.4 * 2 * Util.dist(GRID_X_SIZE/2, GRID_Y_SIZE/2, i, j) / DIST_MAX;
                } else {
                    w[i][j] = 0;
                }
            }

        // For each room to place, select a position

        Queue<Tile> rooms = new LinkedList<Tile>();
        while(rooms.size() < 10) {

            double sum = 0;
            for (int i = 0; i < GRID_X_SIZE; i++)
                for (int j = 0; j < GRID_Y_SIZE; j++)
                    sum += w[i][j];

            loop:
            while(true)
            for (int i = 0; i < GRID_X_SIZE; i++)
                for (int j = 0; j < GRID_Y_SIZE; j++)
                    if(Math.random() < w[i][j]/sum) {

                        //set w of everything within 4 to 0
                        for(int x = i - FEATURE_SIZE/2; x <= i + FEATURE_SIZE/2; x++)
                            for(int y = j - FEATURE_SIZE/2; y <= j + FEATURE_SIZE/2; y++)
                                w[x][y] = 0;

                                //lower w of everything farther proportional to distance


                        for(int x = 0; x < GRID_X_SIZE; x++)
                            for(int y = 0; y < GRID_Y_SIZE; y++)
                                w[x][y] = w[x][y] * ( 10 * Util.dist(x,y,i,j)/DIST_MAX);

                        grid[i][j] = new TileStart(i, j); //temp for test
                        System.out.println("Set " + i + " " + j + " to tile start");
                        rooms.add(grid[i][j]);

                        break loop;
                    }

        }


        // Add paths between them

        Tile t;

        for(int i = 0; i < 2; i++) {
            t = rooms.poll();
            grid[t.x][t.y] = new TileStart(t.x, t.y);
        }

        for(int i = 0; i < 3; i++) {
            t = rooms.poll();
            grid[t.x][t.y] = new TileMetal(t.x, t.y);
        }

        for(int i = 0; i < 5; i++) {
            t = rooms.poll();
            grid[t.x][t.y] = new TileStone(t.x, t.y);
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
