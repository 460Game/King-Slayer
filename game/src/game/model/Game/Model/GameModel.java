package game.model.Game.Model;

import game.message.Message;
import game.model.Game.Grid.GridCell;
import game.model.Game.Map.MapGenerator;
import game.model.Game.Map.Tile;
import game.model.Game.WorldObject.Drawable;
import game.model.Game.WorldObject.Entity.Entity;
import game.model.Game.WorldObject.Entity.TestPlayer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class GameModel extends UpdateModel {

    /**
     * Grid of the game map. Each tile on the map is represented by a 1x1 cell.
     */
    private GridCell[][] grid = new GridCell[Util.Const.GRID_X_SIZE][Util.Const.GRID_Y_SIZE];

    private Collection<GridCell> allCells;

    private LinkedBlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();

    private Map<Long, Entity> entities;

    protected void queueMessage(Message message) {
        messageQueue.add(message);
    }

    /**
     * Constructor for the game model.
     * @param generator map generator for this game model
     */
    public GameModel(MapGenerator generator) {
        super();

        entities = new HashMap<>();
        allCells = new ArrayList<>();

        for (int i = 0; i < Util.Const.GRID_X_SIZE; i++)
            for (int j = 0; j < Util.Const.GRID_Y_SIZE; j++)
                grid[i][j] = new GridCell(this, i, j, generator.makeTile(i, j));

        for (int i = 0; i < Util.Const.GRID_X_SIZE; i++)
            for (int j = 0; j < Util.Const.GRID_Y_SIZE; j++)
                allCells.add(grid[i][j]);

        generator.makeStartingEntities().forEach(e -> entities.put(e.getId(),e));
    }

    /**
     * Gets the map width in terms of number of grid cells.
     * @return the number of grid cells in the width of the map
     */
    public int getMapWidth() {
        return Util.Const.GRID_X_SIZE;
    }

    /**
     * Gets the map height in terms of number of grid cells.
     * @return the number of grid cells in the height of the map
     */
    public int getMapHeight() {
        return Util.Const.GRID_Y_SIZE;
    }

    /**
     * Gets the cell at the specified coordinates. The coordinates represent
     * the upper left corner of the cell.
     * @param x x-coordinate
     * @param y y-coordinate
     * @return the cell with the given upper left coordinates
     */
    public GridCell getCell(int x, int y) {
        return grid[x][y];
    }

    /**
     * Gets the tile at the specified coordinates. The coordinates represent
     * the upper left corner of the cell.
     * @param x x-coordinate
     * @param y y-coordinate
     * @return tile of the cell with the given upper left coordinates
     */
    public Tile getTile(int x, int y) {
        return grid[x][y].getTile();
    }

    /**
     * Returns true if the cell at the given coordinates has been explored.
     * The coordinates represent the upper left corner of the cell.
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true if the cell at the given coordinates has been explored
     */
    public boolean explored(int x, int y) {
        return true; //TODO LOS
    }

    /**
     * Removes the entity with the given ID from every tile on the game map.
     *
     * @param entityID ID of the entity to be removed
     */
    public void removeByID(long entityID) {
        for (GridCell[] arr : grid)
            for (GridCell tile : arr)
                tile.removeByID(entityID);
        //TODO why doesnt this remove it from the entity map and players list
    }

    /**
     * returns approximately all the entities inside of the box centered at x,y with width, height
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public Set<Entity> inBox(int x, int y, int w, int h) {
        Set<Entity> objects = new HashSet<>();
        for (int i = x - w / 2; i < x + w / 2 + w; i++)
            for (int j = y - h / 2; j < y + h / 2; j++)
                objects.addAll(grid[i][j].getContents());
        return objects;
    }

    public void setTile(int x, int y, Tile tile) {
        grid[x][y].setTile(tile);
    }

    @Override
    public void update() {
        ArrayList<Message> list = new ArrayList<>();
        messageQueue.drainTo(list);
        list.forEach(m -> m.execute(this));

        for (GridCell cell : allCells) {// TODO collisions broken
            cell.collideContents(this);
        }

        entities.values().forEach(e -> e.update(this));
    }

    public Collection<GridCell> getAllCells() {
        return allCells;
    }

    public void setEntity(Entity entity) {
        if(entities.containsKey(entity.getId())) {
            Entity e = entities.get(entity.getId());
            e.copyOf(entity);
        } else {
            entities.put(entity.getId(),entity);
        }
    }

    /**
     * Decorator around drable to memozie z value so can sort even while another thread mutates z values
     */
   private static class DrawableZ implements Comparable<DrawableZ>{
        private Drawable drawable;
        private double z;

        DrawableZ(Drawable drawable) {
            this.drawable = drawable;
            this.z = drawable.getDrawZ();
        }

       public void draw(GraphicsContext gc) {
           drawable.draw(gc);
       }

        @Override
        public int compareTo(DrawableZ o) {
            return Double.compare(this.z, o.z);
        }
    }

    //    /**
//     * @param x top left x
//     * @param y top lefy y
//     * @param w width in game space
//     * @param h height in game space
//     * @param gc
//     * @param i
//     * @param i1
//     * @param i2
//     * @param i3
//     */
    public void draw(GraphicsContext gc, double cx, double cy, double w, double h) {
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(-100000, -100000, 100000000, 10000000);

        ArrayList<Drawable> drawEntities = new ArrayList<>();

        for (int y = Math.max(0, (int) (cy - h / 2)); y < Math.min(cy + h / 2, getMapHeight()); y++) {
            for (int x = Math.max(0, (int) (cx - w / 2)); x < Math.min(cx + w / 2, getMapWidth()); x++) {
                GridCell cell = getCell(x, y);
                drawEntities.add(cell);
                drawEntities.addAll(cell.getContents());
            }
        }

        drawEntities.stream().map(DrawableZ::new).sorted().forEach(a -> a.draw(gc));
    }


    public Collection<Entity> getAllEntities() {
        return entities.values();
    }

    public Entity getEntityById(long entity) {
        return entities.get(entity);
    }
}
