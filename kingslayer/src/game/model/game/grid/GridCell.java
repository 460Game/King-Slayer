package game.model.game.grid;

import game.model.game.model.GameModel;
import game.model.game.map.Tile;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.collideStrat.CollisionStrat;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;

/**
 * Defines an individual cell on the game grid. Knows the entities
 * that currently exist on the cell and the type of tile that it
 * currently is.
 */
public class GridCell {

    /**
     * Set of entities that currently reside on the cell.
     */
    private ArrayList<Entity> contents = new ArrayList<>();

    /**
     * X-coordinate of the top left of this cell.
     */
    private int x;

    /**
     * Y-coordinate of the top left of this cell.
     */
    private int y;

    /**
     * Type of tile that the grid is currently.
     */
    private Tile tile;

    /**
     * Returns true if the cell is able to be passed through, or equivalently,
     * if a pathing enemy should try to go through this tile. A cell is
     * considered unpassable f it has a cell shape occupying it.
     * @return true if the cell is able to be walked through
     */
    public boolean isPassable() {
        return contents.stream().noneMatch(e -> e.getCollideType() == CollisionStrat.CollideType.HARD && e.data.shape.blocksCell(x, y));
    }

    /**
     * Gets the contents of the cell.
     * @return the current contents of the cell
     */
    public Collection<Entity> getContents() {
        return contents;
    }

    /**
     * Adds the specified entity to this cell.
     * @param o the entity to be added to this cell
     */
    public void addContents(Entity o) {
        contents.add(o);
    }

    /**
     * Removes the specified entity from this cell.
     * @param o the entity to be removed from this cell
     */
    public void removeContents(Entity o) {
        contents.remove(o);
    }

    /**
     * Constructor for a grid cell.
     * @param model current model of the game
     * @param x x-coordinate of the top left of the cell
     * @param y y-coordinate of the top left of the cell
     * @param tile the type of tile
     */
    public GridCell(GameModel model, int x, int y, Tile tile) {
        this.x = x;
        this.y = y;
        this.tile = tile;
    }

    /**
     * Default constrcuctor needed for serialization.
     */
    public GridCell() {

    }

    /**
     * Renders the cell with the background determined by the current
     * tile type.
     * @param gc context used to draw the cell background
     */
    public void draw(GraphicsContext gc, GameModel model) {
        tile.draw(gc, x, y, model, true);
    }

    /**
     * Perform collisions between the current contents of the cell.
     * The collisions are handled based on the entities involved.
     * @param model current model of the game
     */
    public void collideContents(GameModel model) {
        for(int i = 0; i < contents.size(); i++) {
            for (int j = 0; j < i; j++) {
                Entity a = contents.get(i);
                Entity b = contents.get(j);
                if(a.data.shape.testCollision(b.data.shape)) {
                    a.collision(model, b);
                    b.collision(model, a);
                }
            }
        }
    }

    /**
     * Gets the current tile type of the cell.
     * @return the current tile type of the cell
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Sets the tile of the cell to the specified tile
     * @param tile the new tile type of the cell
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    /**
     * Remove all entities that have the specified id from the
     * cell.
     * @param entityID the id of the entity to be removed
     */
    public void removeByID(long entityID) {
        contents.removeIf(o -> o.id == entityID);
    }

    /**
     * Gets the x coordinate of the top left of this grid cell.
     * @return the x coordinate of the top left of this grid cell
     */
    public int getTopLeftX() {
        return x;
    }

    /**
     * Gets the x coordinate of the top left of this grid cell.
     * @return the x coordinate of the top left of this grid cell
     */
    public int getTopLeftY() {
        return y;
    }

    public double getCenterX() {
        return x + 0.5;
    }

    public double getCenterY() {
        return y + 0.5;
    }
}
