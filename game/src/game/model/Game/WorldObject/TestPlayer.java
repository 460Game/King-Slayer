package game.model.Game.WorldObject;

import game.model.Game.GameModel;
import game.model.Game.WorldObject.Shape.CircleShape;
import game.model.Game.WorldObject.Shape.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TestPlayer extends Entity {

    private CircleShape shape;

    private double dx = 0;
    private double dy = 0;

    public TestPlayer(GameModel model, double x, double y) {
        super(model);
        this.shape = new CircleShape(x, y, 0.5);
    }

    @Override
    public void collision(GameModel model, Entity collidesWith) {
        double xdiff = this.getX() - collidesWith.getX();
        double ydiff = this.getY() - collidesWith.getY();

        while (this.shape.testCollision(collidesWith.getShape()))
            this.shape.shift(0.1 * xdiff, 0.1 * ydiff);
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void draw(GraphicsContext gc) {

        this.shape.draw(gc);
    }

    @Override
    public void update(long time, GameModel model) {
        this.shape.shift(dx * time * 1e-9 * 10, dy * time * 1e-9 * 10);

    }

    public void up() {
        dy = -1;
    }

    public void left() {
        dx = -1;
    }

    public void right() {
        dx = 1;
    }

    public void down() {
        dy = 1;
    }

    public void stopVert() {
        dy = 0;
    }

    public void stopHorz() {
        dx = 0;
    }
}
