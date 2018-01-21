package game.model.Game.Grid;

import game.model.Game.Model.GameModel;
import game.model.Game.Map.Tile;
import game.model.Game.WorldObject.Entity.Blocker;
import game.model.Game.WorldObject.Drawable;
import game.model.Game.WorldObject.Entity.Entity;
import javafx.scene.canvas.GraphicsContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Defines an individual cell on the game grid. Knows the entities
 * that currently exist on the cell and the type of tile that it
 * currently is.w
 */
public class GridCell implements Drawable {

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
        if(!isPassable()) //TODO tempoary - long term this will be genrated by the map gernator itself
            addContents(new Blocker(model, x, y));
    }

    /**
     * Renders the cell with the background determined by the current
     * tile type.
     * @param gc context used to draw the cell background
     */
    public void draw(GraphicsContext gc) {
        tile.draw(gc, x, y);
    }

    @Override
    public double getDrawZ() {
        if(!tile.aboveGround)
            return 0;
        return y;
    }

    /**
     * Perform collisions between the current contents of the cell.
     * The collisions are handled based on the entities involved.
     * @param model current model of the game
     */
    public void collideContents(GameModel model) {
        for(Entity a : contents)
            contents.stream().filter(b -> a != b && a.getShape().testCollision(b.getShape())).forEach(
                    b -> a.collision(model, b));

        // Problem: collide in two cells
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
        contents.removeIf(o -> o.getId() == entityID);
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
