package game.model.Game;

import game.model.Game.Grid.GridCell;
import game.model.Game.Tile.Tile;
import game.model.Game.WorldObject.Entity;
import game.model.Game.WorldObject.TestPlayer;
import game.model.IModel;
import game.model.ProcessorForwarderModel;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;

public class GameModel extends ProcessorForwarderModel implements IGameModel {

    public static int GRID_X_SIZE = 100;
    public static int GRID_Y_SIZE = 100;

    private GridCell[][] grid = new GridCell[GRID_X_SIZE][GRID_Y_SIZE];

    Collection<GridCell> allCells;

    //TODO temp test
    public TestPlayer playerA = null;
    public TestPlayer playerB = null;

    private Collection<Entity> entities;

    public int getMapWidth() {
        return GRID_X_SIZE;
    }

    public int getMapHeight() {
        return GRID_Y_SIZE;
    }

    public GridCell getCell(int x, int y) {
        return grid[x][y];
    }

    public Tile getTile(int x, int y) {
        return grid[x][y].getTile();
    }

    public boolean explored(int x, int y) {
        return true; //TODO LOS
    }

    @Override
    public void removeByID(UUID entityID) {
        for (GridCell[] arr : grid)
            for (GridCell tile : arr)
                tile.removeByID(entityID);
    }


    public Set<Entity> inBox(int x, int y, int w, int h) {
        Set<Entity> objects = new HashSet<>();
        for (int i = x; i < x + w; i++)
            for (int j = y; j < y + +w; j++)
                objects.addAll(grid[i][j].getContains());
        return objects;
    }

    public GameModel(boolean isServer, Collection<IModel> others) {
        super(isServer, others);

        entities = new ArrayList<>();
        allCells = new ArrayList<>();
        MapGenerator generator = new MapGenerator(getMapWidth(), getMapWidth());

        generator.makeMap();

        for (int i = 0; i < GRID_X_SIZE; i++)
            for (int j = 0; j < GRID_Y_SIZE; j++)
                grid[i][j] = new GridCell(this, i, j, generator.makeTile(i, j));

        for (int i = 0; i < GRID_X_SIZE; i++)
            for (int j = 0; j < GRID_Y_SIZE; j++)
                allCells.add(grid[i][j]);

        playerA = new TestPlayer(this, generator.getStartingLocations().get(0).x, generator.getStartingLocations().get(0).y);
        playerB = new TestPlayer(this, generator.getStartingLocations().get(1).x, generator.getStartingLocations().get(1).y);
        entities.add(playerA);
        entities.add(playerB);
    }

    @Override
    public GameModel getGameModel() {
        return this;
    }

    @Override
    public long nanoTime() {
        return System.nanoTime(); //TODO hm
    }


    @Override
    public void setTile(int x, int y, Tile tile) {
        grid[x][y].setType(tile);
    }

    @Override
    public void update() {
        for (GridCell tile : allCells)
            tile.collideContents(this);

        for (Entity e : entities)
            e.update(this);
    }

    @Override
    public void draw(GraphicsContext gc) {
        for (GridCell tile : allCells)
            tile.drawBackground(gc);
        for (Entity e : entities)
            e.draw(gc);
    }

    public Collection<GridCell> getAllCells() {
        return allCells;
    }

}
