package game.model.Game.Grid;

import game.model.Game.GameModel;
import game.model.Game.Tile.Tile;
import game.model.Game.WorldObject.Blocker;
import game.model.Game.WorldObject.Entity;
import javafx.scene.canvas.GraphicsContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Defines an individual cell on the game grid. Knows the entities
 * that currently exist on the cell and the type of tile that it
 * currently is.
 */
public class GridCell {

    /**
     * Set of entities that currently reside on the cell.
     */
    private Set<Entity> contents = new HashSet<>();

    /**
     * X and y coordinates of the top left of this part of the grid.
     */
    private int x, y;

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
        return contents.stream().anyMatch(e -> e.getShape().blocksCell(x, y));
    }

    /**
     * Gets the contents of the cell.
     * @return the current contents of the cell
     */
    public Set<Entity> getContents() {
        return Collections.synchronizedSet(contents);
    }

    /**
     * Adds the specified entity to this cell.
     * @param o the entity to be added to this cell
     */
    public void add(Entity o) {
        contents.add(o);
    }

    /**
     * Removes the specified entity from this cell.
     * @param o the entity to be removed from this cell
     */
    public void remove(Entity o) {
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
        if(!isPassable())
            add(new Blocker(model, x, y));
    }

    /**
     *
     * @param gc
     */
    public void drawBackground(GraphicsContext gc) {
        tile.draw(gc, x, y);
    }

    /**
     *
     * @param model
     */
    public void collideContents(GameModel model) {
        for(Entity a : contents)
            for (Entity b : contents)
                if (a != b) // && a.getShape().testCollision(b.getShape()))
                    a.collision(model, b);
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
    public void removeByID(UUID entityID) {
        contents.removeIf(o -> o.getUuid().equals(entityID));
    }

    /**
     * Gets the x coordinate of the top left of this grid cell.
     * @return the x coordinate of the top left of this grid cell
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the x coordinate of the top left of this grid cell.
     * @return the x coordinate of the top left of this grid cell
     */
    public int getY() {
        return y;
    }
}
