package game.model.Game.WorldObject.Entity;

import Util.Util;
import game.model.Game.Model.GameModel;
import game.model.Game.WorldObject.Shape.CellShape;
import game.model.Game.WorldObject.Shape.CircleShape;
import game.model.Game.WorldObject.Shape.Shape;
import javafx.scene.canvas.GraphicsContext;

public abstract class Player extends MovingEntity {

    @Override
    public void copyOf(Entity other) {
        assert (other instanceof Player);
        Player o = (Player) other;
        this.shape = o.shape;
        this.dx = o.dx;
        this.dy = o.dy;
        super.copyOf(other);
    }

    private CircleShape shape;
    private double dx = 0;
    private double dy = 0;

    public Player() {
        super();
        shape = new CircleShape(0.0, 0.0, 0.3);
        setMovementAngle(0.5 * Math.PI);
    }

    public Player(GameModel model, double x, double y, boolean king) {
        super(model);
        shape = new CircleShape(x, y, 0.3);
        setMovementAngle(0.5 * Math.PI);
    }

    @Override
    public void collision(GameModel model, MovingEntity collidesWith) {
        if (collidesWith.getSpeed() == 0) {
            // TODO treat like stationary entity collision
        } else {
            // TODO other cases
        }
    }

    @Override
    public void collision(GameModel model, StationaryEntity collidesWith) {
//        System.out.println("X: " + (shape.getX()) + ", Y: " + (shape.getY()) + ", radius: " + shape.getRadius());
//
//        for(Shape.GridCellReference g : shape.getCellsReference())
//            System.out.println("Cell X: " + g.x + ", cell Y: " + g.y);
//        System.out.println("BLocker X, y: " + collidesWith.getX() + ", " + collidesWith.getY());
        if (Util.closeDouble(this.getMovementAngle(), Math.PI) && collidesWith.getShape() instanceof CellShape) {//collidesWith.getSpeed() == 0 ) {
            setSpeed(0);
            setPos(collidesWith.getX() + 0.5 + this.shape.getRadius(), y); // Center of entity + 0.5 = right edge +
            // shape radius to get new center
        } else if (Util.closeDouble(this.getMovementAngle(), 0) && collidesWith.getShape() instanceof CellShape) {//collidesWith.getSpeed() == 0) {
            setSpeed(0);
            setPos(collidesWith.getX() - 0.5 - this.shape.getRadius(), y); // Center of entity - 0.5 = left edge -
                                                                           // shape radius to get new center
        } else if (Util.closeDouble(this.getMovementAngle(), Math.PI / 2) && collidesWith.getShape() instanceof CellShape) {//collidesWith.getSpeed() == 0) {
            setSpeed(0);
            setPos(x, collidesWith.getY() - 0.5 - this.shape.getRadius());
        } else if (Util.closeDouble(this.getMovementAngle(), - Math.PI / 2) && collidesWith.getShape() instanceof CellShape) {//collidesWith.getSpeed() == 0) {
            setSpeed(0);
            setPos(x, collidesWith.getY() + 0.5 + this.shape.getRadius());
        } else
            setPos(x, y);

        // TODO problem running into object from another direction but still hiting object
        // double xdiff = Math.abs(this.getX() - collidesWith.getX());
        // double ydiff = Math.abs(this.getY() - collidesWith.getY());

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

    @Override
    public void draw(GraphicsContext gc) {

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

    protected boolean up = false, left = false, right = false, down = false;

    public void up() {
        if(!up) {
            up = true;
            change();
        }
//        if (!up) {
//            up = true;
//            setSpeed(getSpeed() + 0.1);
//            dy = -1;
//            setMovementAngle(Math.atan2(dy, dx));
//        }
    }

    public void left() {
        if(!left) {
            left = true;
            change();
        }
//        if (!left) {
//            left = true;
//            setSpeed(getSpeed() + 0.1);
//            dx = -1;
//            setMovementAngle(Math.atan2(dy, dx));
//        }
    }

    public void right() {
        if(!right) {
            right = true;
            change();
        }
//        if (!right) {
//            right = true;
//            setSpeed(getSpeed() + 0.1);
//            dx = 1;
//            setMovementAngle(Math.atan2(dy, dx));
//        }
    }

    public void down() {
        if(!down) {
            down = true;
            change();
        }
//        if (!down) {
//            down = true;
//            setSpeed(getSpeed() + 0.1);
//            dy = 1;
//            setMovementAngle(Math.atan2(dy, dx));
//        }
    }

    public void stopVert() {
        up = false;
        down = false;
        change();
//        setSpeed(0);
//        dy = 0;
//        setMovementAngle(Math.atan2(dy, dx));
    }

    public void stopHorz() {
        right = false;
        left = false;
        change();
//        setSpeed(0);
//        dx = 0;
//        setMovementAngle(Math.atan2(dy, dx));
    }
}
