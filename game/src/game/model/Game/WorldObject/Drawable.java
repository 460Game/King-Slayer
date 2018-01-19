package game.model.Game.WorldObject;

import javafx.scene.canvas.GraphicsContext;

public interface Drawable {

    public void draw(GraphicsContext gc);

    /**
     * everything is drawn in order of least Z to highest Z
     * so something on bottem can have -1 z
     * something on top can hava a large Z
     * anything in middle should have approxibly their y value in Z
     * @return
     */
    public double getDrawZ();

}
