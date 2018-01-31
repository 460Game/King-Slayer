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
    private int dx;
    private int dy;
    private boolean up = false, left = false, right = false, down = false;

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
        System.out.println("X: " + (shape.getX()) + ", Y: " + (shape.getY()) + ", radius: " + shape.getRadius());

//        for(Shape.GridCellReference g : shape.getCellsReference())
//            System.out.println("Cell X: " + g.x + ", cell Y: " + g.y);
        System.out.println("BLocker X, y: " + collidesWith.getX() + ", " + collidesWith.getY());
        if (collidesWith.getShape() instanceof CellShape) {
            double angle = Util.angle2Points(getX(), getY(), collidesWith.getX(), collidesWith.getY());
            System.out.println("Collide angle: " + angle);
            System.out.println("Movement angle: " + getMovementAngle());
            if (Util.closeDouble(this.getMovementAngle(), Math.PI)) {//collidesWith.getSpeed() == 0 ) {
                setSpeed(0);
                setPos(collidesWith.getX() + 0.5 + this.shape.getRadius(), y); // Center of entity + 0.5 = right edge +
                // shape radius to get new center
            } else if (Util.closeDouble(this.getMovementAngle(), 0)) {//collidesWith.getSpeed() == 0) {
                setSpeed(0);
                setPos(collidesWith.getX() - 0.5 - this.shape.getRadius(), y); // Center of entity - 0.5 = left edge -
                // shape radius to get new center
            } else if (Util.closeDouble(this.getMovementAngle(), Math.PI / 2)) {//collidesWith.getSpeed() == 0) {
                setSpeed(0);
                setPos(x, collidesWith.getY() - 0.5 - this.shape.getRadius());
            } else if (Util.closeDouble(this.getMovementAngle(), -Math.PI / 2)) {//collidesWith.getSpeed() == 0) {
                setSpeed(0);
                setPos(x, collidesWith.getY() + 0.5 + this.shape.getRadius());
            } else if (angle > Math.PI / 4 && angle < 3 * Math.PI / 4) {
                if (Util.closeDouble(this.getMovementAngle(), Math.PI / 4))
                    setPos(x + 0.1, collidesWith.getY() - 0.5 - this.shape.getRadius());
                else
                    setPos(x - 0.1, collidesWith.getY() - 0.5 - this.shape.getRadius());
            } else if (angle > -Math.PI / 4 && angle < Math.PI / 4) {
                if (Util.closeDouble(this.getMovementAngle(), Math.PI / 4))
                    setPos(collidesWith.getX() - 0.5 - this.shape.getRadius(), y + 0.1);
                else
                    setPos(collidesWith.getX() - 0.5 - this.shape.getRadius(), y - 0.1);
            }
            else
                setPos(x, y);
        }
        else
            setPos(x, y);

        // TODO problem running into object from another direction but still hiting object

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
    public void draw(GraphicsContext gc, GameModel model) {

    }

    /**
     * Move the player based on updated inputs.
     * @param dx new change in x movement
     * @param dy new change in y movement
     */
    public void move(int dx, int dy) {
        System.out.println("Movement Angle: " + getMovementAngle());
        boolean dirUpdate = false;  // Check if a direction has changed.
        if (dx == 0 && dy == -1) { // Up case.
            if(!up) {
                up = true;
                this.dy -= 1;
                dirUpdate = true;
            }
        } else if (dx == 0 && dy == 1) { // Down case.
            if (!down) {
                down = true;
                this.dy += 1;
                dirUpdate = true;
            }
        } else if (dx == -1 && dy == 0) { // Left case.
            if(!left) {
                left = true;
                this.dx -= 1;
                dirUpdate = true;
            }
        } else { // Right case.
            if(!right) {
                right = true;
                this.dx += 1;
                dirUpdate = true;
            }
        }
        if (dirUpdate) {    // Only update extra stuff if any direction now has movement in that direction.
            setSpeed(0.1);
            double oldAngle = getMovementAngle();
            setMovementAngle(Math.atan2(this.dy, this.dx));       // Get new movement angle.
            if (up && down || left && right) {   // Directions cancelling out results in no speed, maintain angle
                this.setSpeed(0);
                this.setMovementAngle(oldAngle);
            }
        }
    }

    private void change() {
        if (!up && !left && !right && !down)
            this.setSpeed(0);
        else {
            this.setSpeed(0.1);
//            System.out.println("NEW CHANGE!!!!!!1");
//            System.out.println("UP: " + up);
//            System.out.println("LEFT: " + left);
//            System.out.println("RIGHT: " + right);
//            System.out.println("DOWN: " + down);
            double oldAngle = this.getMovementAngle();

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
            this.setMovementAngle(Math.atan2(dy, dx));
            System.out.println(this.getMovementAngle());
            if (up && down) {
                dy = 0;
                this.setSpeed(0);
                this.setMovementAngle(oldAngle);
            }
            if (left && right) {
                dx = 0;
                this.setSpeed(0);
                this.setMovementAngle(oldAngle);
            }

        }
    }

    public void stopUp() {
        up = false;
        this.dy += 1;
        change();
    }

    public void stopDown() {
        down = false;
        this.dy -= 1;
        change();
    }

    public void stopLeft() {
        left = false;
        this.dx += 1;
        change();
    }

    public void stopRight() {
        right = false;
        this.dx -= 1;
        change();
    }
}
