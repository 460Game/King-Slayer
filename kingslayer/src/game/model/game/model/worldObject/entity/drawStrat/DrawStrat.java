package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.ClientGameModel;
import game.model.game.model.Model;
import game.model.game.model.worldObject.entity.Entity;
import javafx.scene.canvas.GraphicsContext;

public abstract class DrawStrat {

    public abstract DrawData initDrawData();

    /**
     * Draw the object on the game map.
     * @param gc context used to drawFG the objects
     */
    public abstract void draw(Entity entity, ClientGameModel model, GraphicsContext gc);

    /**
     * Everything is drawn in order of least Z to highest Z.
     * Something on bottom can have z = 0, and something on top can have a
     * large Z. Anything in middle should have approximately their y value in Z.
     * @return the Z value of the object being drawn
     * @param entity
     */
    public abstract double getDrawZ(Entity entity);
}
