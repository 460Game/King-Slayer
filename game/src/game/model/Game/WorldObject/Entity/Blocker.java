package game.model.Game.WorldObject.Entity;

import game.model.Game.Model.GameModel;
import game.model.Game.WorldObject.Shape.CellShape;
import game.model.Game.WorldObject.Shape.Shape;
import game.model.Game.WorldObject.Team;
import javafx.scene.canvas.GraphicsContext;

/**
 * Defines a blocking entity that covers an entire cell and
 * cannot be passed through.
 */
public class Blocker extends StationaryEntity {

    /**
     * Shape of a blocker is a cell shape.
     */
    private CellShape shape;

    @Override
    public void copyOf(Entity other) {
        assert(other instanceof Blocker);
        Blocker o = (Blocker) other;
        this.shape = o.shape;
        super.copyOf(other);
    }

    /**
     * Constructor for a blocker. It has infinite health and
     * has no team affiliation.
     */
    public Blocker() {
        super();
        this.shape = new CellShape();
        this.setTeam(Team.NEUTRAL);
        this.setHealth(Double.POSITIVE_INFINITY);
    }

    /**
     * Constructor for a blocker, given a position and game model.
     * @param model current model of the game
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Blocker(GameModel model, int x, int y) {
        super(model);
        this.shape = new CellShape(x, y);
        this.setTeam(Team.NEUTRAL);
        this.setHealth(Double.POSITIVE_INFINITY);
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public double getDrawZ() {
        return 0;
    }

    @Override
    public void update(long time, GameModel model) {
        //Do nothing
    }

    @Override
    public void draw(GraphicsContext gc, GameModel model) {
        //Do nothing
    }
}
