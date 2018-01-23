package game.model.Game.WorldObject.Entity;

import game.model.Game.Map.Tile;
import game.model.Game.Model.GameModel;
import game.model.Game.WorldObject.Shape.CircleShape;
import game.model.Game.WorldObject.Shape.Shape;
import game.model.Game.WorldObject.Team;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.IOException;

import static Util.Const.TILE_PIXELS;

public class TestPlayer extends Entity {

    @Override
    public void copyOf(Entity other) {
        assert (other instanceof TestPlayer);
        TestPlayer o = (TestPlayer) other;
        this.shape = o.shape;
        this.dx = o.dx;
        this.dy = o.dy;
        super.copyOf(other);
    }

    private CircleShape shape;
    private double dx = 0;
    private double dy = 0;

    public TestPlayer() {
        super();
        shape = new CircleShape(0.0, 0.0, 0.3);
    }

    public TestPlayer(GameModel model, double x, double y) {
        super(model);
        shape = new CircleShape(x, y, 0.3);
    }

    @Override
    public void collision(GameModel model, Entity collidesWith) {
        this.setPos(x,y);
        // double xdiff = Math.abs(this.getX() - collidesWith.getX());
        // double ydiff = Math.abs(this.getY() - collidesWith.getY());

        // System.out.println("X: " + (shape.getX()) + ", Y: " + (shape.getY()) + ", radius: " + shape.getRadius());

        // for(Shape.GridCellReference g : shape.getCellsReference())
        //    System.out.println("Cell X: " + g.x + ", cell Y: " + g.y);

        // System.out.println("BLocker X, y: " + collidesWith.getX() + ", " + collidesWith.getY());

        // TODO issue with both directions?
        //    while (shape.testCollision(collidesWith.getShape()))
//            shape.shift(-0.05 * shape.getRadius() * Math.cos(getMovementAngle()), 0.05 * shape.getRadius() * Math.sin(getMovementAngle()));
       // shape.shift(5*this.getSpeed() * Math.cos(Math.PI + getMovementAngle()), 5*this.getSpeed() * Math.sin(Math.PI + getMovementAngle()));
       // this.setSpeed(0);
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    static Image imageRedKing;
    static Image imageBlueKing;

    static {
        try {
            imageRedKing = new Image(Tile.class.getResource("king_red_1.png").openStream());
            imageBlueKing = new Image(Tile.class.getResource("king_blue_1.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        //gc.setFill(this.getTeam().color);
        //shape.draw(gc);
        if (this.getTeam() == Team.ONE) {
            gc.drawImage(imageRedKing,
                this.getX() * TILE_PIXELS - TILE_PIXELS / 2,
                this.getY() * TILE_PIXELS - TILE_PIXELS + 40,
                TILE_PIXELS,
                1.5 * TILE_PIXELS);
        } else {
            gc.drawImage(imageBlueKing, this.getX() * TILE_PIXELS - TILE_PIXELS / 2,
                this.getY() * TILE_PIXELS - TILE_PIXELS + 40, TILE_PIXELS, 1.5 * TILE_PIXELS);
        }
    }

    @Override
    public double getDrawZ() {
        return getY();
    }

    private double x;
    private double y;

    @Override
    public void update(long time, GameModel model) {
        this.x = shape.getX();
        this.y = shape.getY();
        // shape.shift(dx * time * 1e-9 * 10, dy * time * 1e-9 * 10); TODO use the delta
        shape.shift(Math.cos(this.getMovementAngle()) * this.getSpeed(), Math.sin(this.getMovementAngle()) * this.getSpeed());
    }

    private void change() {
        if (!up && !left && !right && !down)
            this.setSpeed(0);
        else {
            this.setSpeed(0.1);

            int dx = 0;
            int dy = 0;
            if (up)
                dy = -1;
            if (down)
                dy = 1;
            if (right)
                dx = 1;
            if (left)
                dx = -1;
            if (up && down)
                dy = 0;
            if (left && right)
                dx = 0;
            this.setMovementAngle(Math.atan2(dy, dx));
        }
    }

    private boolean up = false, left = false, right = false, down = false;

    public void up() {
        if(!up) {
            up = true;
            change();
        }
    }

    public void left() {
        if(!left) {
            left = true;
            change();
        }
    }

    public void right() {
        if(!right) {
            right = true;
            change();
        }
    }

    public void down() {
        if(!down) {
            down = true;
            change();
        }
    }

    public void stopVert() {
        up = false;
        down = false;
        change();
    }

    public void stopHorz() {
        right = false;
        left = false;
        change();
    }
}
