package game.model.Game.WorldObject.Entity;

import Util.Util;
import game.model.Game.Model.GameModel;
import game.model.Game.WorldObject.Shape.*;
import javafx.scene.canvas.GraphicsContext;

/**
 * Class that defines an abstract player in the game world.
 */
public abstract class Player extends MovingEntity {

    @Override
    public void copyOf(Entity other) {
        assert (other instanceof Player);
        Player o = (Player) other;
        this.shape = o.shape;
        this.dx = o.dx;
        this.dy = o.dy;
        this.up = o.up;
        this.left = o.left;
        this.right = o.right;
        this.down = o.down;
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
            } else if (angle > 3 * Math.PI / 4 || angle < - 3 * Math.PI / 4) {
                if (Util.closeDouble(this.getMovementAngle(), 3 * Math.PI / 4))
                    setPos(collidesWith.getX() + 0.5 + this.shape.getRadius(), y + 0.1);
                else
                    setPos(collidesWith.getX() + 0.5 + this.shape.getRadius(), y - 0.1);
            } else if (angle < - Math.PI / 4 && angle > -3 * Math.PI / 4) {
                if (Util.closeDouble(this.getMovementAngle(), - Math.PI / 4))
                    setPos(x + 0.1, collidesWith.getY() + 0.5 + this.shape.getRadius());
                else
                    setPos(x - 0.1, collidesWith.getY() + 0.5 + this.shape.getRadius());
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
     * Move the player based on the given direction.
     * @param dir direction the player wants to move
     */
    public void move(String dir) {
//        System.out.println("Movement Angle: " + getMovementAngle());
        boolean dirUpdate = false;  // Check if a direction has changed.
        switch (dir.toLowerCase()) {
            case "up" : // Upward movement.
                if(!up) {
                    up = true;
                    dy -= 1;
                    dirUpdate = true;
                }
                break;
            case "down" : // Downward movement.
                if (!down) {
                    down = true;
                    dy += 1;
                    dirUpdate = true;
                }
                break;
            case "left" : // Leftward movement.
                if(!left) {
                    left = true;
                    dx -= 1;
                    dirUpdate = true;
                }
                break;
            case "right" : // Rightward movement.
                if(!right) {
                    right = true;
                    dx += 1;
                    dirUpdate = true;
                }
                break;
            default :
                throw new RuntimeException("Unknown direction to move.");
        }
        if (dirUpdate) {    // Only update extra stuff if any direction now has movement in that direction.
            setSpeed(0.1);
            double oldAngle = getMovementAngle();
            setMovementAngle(Math.atan2(dy, dx));     // Update new movement angle after a direction has changed.
            if (up && down || left && right) {   // Directions cancelling out results in no speed, maintain angle.
                setSpeed(0);
                setMovementAngle(oldAngle);
            }
        }
    }

    /**
     * Stops the player based on the given direction.
     * @param dir direction the player wants to stop moving
     */
    public void stop(String dir) {
        switch (dir.toLowerCase()) {
            case "up" : // Stop upward movement.
                up = false;
                dy += 1;
                break;
            case "down" : // Stop downward movement.
                down = false;
                dy -= 1;
                break;
            case "left" : // Stop leftward movement.
                left = false;
                dx += 1;
                break;
            case "right" : // Stop rightward movement.
                right = false;
                dx -= 1;
                break;
            default :
                throw new RuntimeException("Unknown direction to stop.");
        }
        if (!up && !left && !right && !down)   // No movement - set speed to 0.
            this.setSpeed(0);
        else {
            this.setSpeed(0.1);
            double oldAngle = this.getMovementAngle();
            this.setMovementAngle(Math.atan2(dy, dx)); // Update new movement angle after a direction has changed.
            if (up && down || left && right) {  // Directions cancelling out results in no speed, preserve angle.
                setSpeed(0);
                setMovementAngle(oldAngle);
            }
        }
    }
}
