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

public class GridCell {

    private Set<Entity> contents = new HashSet<>();

    //x,y coordiante of top left of this part of the grid
    private int x, y;

    private Tile tile;

    /**
     * @return true if a pathing enemy should try to go through this tile
     * considerd unpassible if it has a cell shape occupying it
     */
    public boolean isPassable() {
        return contents.stream().anyMatch(e -> e.getShape().blocksCell(x,y));
    }

    public Set<Entity> getContents() {
        return Collections.synchronizedSet(contents);
    }

    public void add(Entity o) {
        contents.add(o);
    }

    public void remove(Entity o) {
        contents.remove(o);
    }

    public GridCell(GameModel model, int x, int y, Tile tile) {
        this.x = x;
        this.y = y;
        this.tile = tile;
        if(!isPassable())
            add(new Blocker(model, x,y));
    }

    public void drawBackground(GraphicsContext gc) {
        tile.draw(gc, x, y);
    }

    public void collideContents(GameModel model) {
        for(Entity a : contents)
            for (Entity b : contents)
                if (a != b ) // && a.getShape().testCollision(b.getShape()))
                    a.collision(model, b);
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void removeByID(UUID entityID) {
        contents.removeIf(o -> o.getUuid().equals(entityID));
    }
}
