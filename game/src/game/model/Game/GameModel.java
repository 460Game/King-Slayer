package game.model.Game;

import game.model.Game.Grid.GridCell;
import game.model.Game.Tile.Tile;
import game.model.Game.WorldObject.Entity;
import game.model.Game.WorldObject.TestPlayer;
import game.model.IModel;
import game.model.ProcessorForwarderModel;
import javafx.scene.canvas.GraphicsContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GameModel extends ProcessorForwarderModel implements IGameModel {

    public static int GRID_X_SIZE = 150;
    public static int GRID_Y_SIZE = 100;

    private GridCell[][] grid = new GridCell[GRID_X_SIZE][GRID_Y_SIZE];

    //TODO temp test
   public TestPlayer playerA = null;
   public TestPlayer playerB = null;

    public int getMapWidth() {
        return GRID_X_SIZE;
    }

    public int getMapHeight() {
        return GRID_Y_SIZE;
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
        for (GridCell[] arr : grid)
            for (GridCell tile : arr)
                objects.addAll(tile.getContains());
        return objects;
    }

    public GameModel(boolean isServer, Collection<IModel> others) {
        super(isServer, others);

        MapGenerator generator = new MapGenerator(getMapWidth(), getMapWidth());

        generator.makeMap();

        for (int i = 0; i < GRID_X_SIZE; i++)
            for (int j = 0; j < GRID_Y_SIZE; j++)
                grid[i][j] = new GridCell(this, i, j, generator.makeTile(i,j));

        playerA = new TestPlayer(this, generator.getStartingLocations().get(0).x, generator.getStartingLocations().get(0).y);
        playerB = new TestPlayer(this, generator.getStartingLocations().get(1).x, generator.getStartingLocations().get(1).y);
        grid[0][0].add(playerA);
        grid[0][0].add(playerB);
    }

    @Override
    public GameModel getGameModel() {
        return this;
    }

    @Override
    public long nanoTime() {
        return System.nanoTime();
    }


    @Override
    public void setTile(int x, int y, Tile tile) {
        grid[x][y].setType(tile);
    }

    @Override
    public void update() {
        for (GridCell[] arr : grid)
            for (GridCell tile : arr)
                tile.update(this);
    }

    @Override
    public void draw(GraphicsContext gc) {
        for (GridCell[] arr : grid)
            for (GridCell tile : arr)
                tile.drawBackground(gc);
        for (GridCell[] arr : grid)
            for (GridCell tile : arr)
                tile.draw(gc);
    }
}
