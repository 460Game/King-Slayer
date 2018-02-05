package game.model.game.model.worldObject.entity;

import javafx.scene.canvas.GraphicsContext;

/**
 * Interface used to draw objects on the map.
 */
public interface Drawable {

    /**
     * Draw the object on the game map.
     * @param gc context used to draw the objects
     */
    void draw(GraphicsContext gc);

    /**
     * Everything is drawn in order of least Z to highest Z.
     * Something on bottom can have z = 0, and something on top can have a
     * large Z. Anything in middle should have approximately their y value in Z.
     * @return the Z value of the object being drawn
     */
    double getDrawZ();
}
