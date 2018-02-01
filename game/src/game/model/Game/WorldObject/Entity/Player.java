package game.model.Game.WorldObject.Entity;

import Util.Util;
import game.model.Game.Model.GameModel;
import game.model.Game.WorldObject.Shape.*;
import javafx.scene.canvas.GraphicsContext;

/**
 * Class that defines an abstract player in the game world.
 */
public abstract class Player extends MovingEntity {

    /**
     * Shape that represents a player in the game.
     */
    private CircleShape shape;

    /**
     * Keeps track of change in x direction. Used for
     * calculating movement angle.
     */
    private int dx;

    /**
     * Keeps track of change in y direction. Used for
     * calculating movement angle.
     */
    private int dy;

    /**
     * X-coordinate of the center of the player.
     */
    private double x;

    /**
     * Y-coordinate of the center of the player.
     */
    private double y;

    /**
     * Flag that says if the player is currently moving up.
     */
    private boolean up = false;

    /**
     * Flag that says if the player is currently moving left.
     */
    private boolean left = false;

    /**
     * Flag that says if the player is currently moving right.
     */
    private boolean right = false;

    /**
     * Flag that says if the player is currently moving down.
     */
    private boolean down = false;

    /**
     * Index to get the image for a certain frame.
     */
    int imageNum = 0;

    /**
     * Index to get the images for movement in a certain direction.
     */
    char direction = SOUTH;

    /**
     * Counter to help create animation.
     */
    int count = 0;

    /**
     * Static variables for the four cardinal directions.
     */
    private static char NORTH = 'N';
    private static char EAST = 'E';
    private static char SOUTH = 'S';
    private static char WEST = 'W';

    /**
     * Default constructor of a player.
     */
    public Player() {
        super();
        shape = new CircleShape(0.0, 0.0, 0.3);
        this.x = shape.getX();
        this.y = shape.getY();
        this.setMovementAngle(0.5 * Math.PI);
        // TODO set health, team
    }

    /**
     * Constructor of a player given coordinates.
     * @param model current model of the game
     * @param x x-coordinate to spawn the player
     * @param y y-coordinate to spawn the player
     * @param king flag that determines whether this player is a king TODO is this correct?
     */
    public Player(GameModel model, double x, double y, boolean king) {
        super(model);
        shape = new CircleShape(x, y, 0.3);
        this.x = x;
        this.y = y;
        this.setMovementAngle(0.5 * Math.PI);
        // TODO set health and team
        // TODO deal with king boolean
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public double getDrawZ() {
        return getY();
    }

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

    @Override
    public void collision(GameModel model, MovingEntity collidesWith) {
        if (collidesWith.getVelocity() == 0) {
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
            if (Util.closeDouble(this.getMovementAngle(), Math.PI)) {//collidesWith.getVelocity() == 0 ) {
                setVelocity(0);
                setPos(collidesWith.getX() + 0.5 + this.shape.getRadius(), y); // Center of entity + 0.5 = right edge +
                // shape radius to get new center
            } else if (Util.closeDouble(this.getMovementAngle(), 0)) {//collidesWith.getVelocity() == 0) {
                setVelocity(0);
                setPos(collidesWith.getX() - 0.5 - this.shape.getRadius(), y); // Center of entity - 0.5 = left edge -
                // shape radius to get new center
            } else if (Util.closeDouble(this.getMovementAngle(), Math.PI / 2)) {//collidesWith.getVelocity() == 0) {
                setVelocity(0);
                setPos(x, collidesWith.getY() - 0.5 - this.shape.getRadius());
            } else if (Util.closeDouble(this.getMovementAngle(), -Math.PI / 2)) {//collidesWith.getVelocity() == 0) {
                setVelocity(0);
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
            setVelocity(0.1);
            double oldAngle = getMovementAngle();
            setMovementAngle(Math.atan2(dy, dx));     // Update new movement angle after a direction has changed.
            if (up && down || left && right) {   // Directions cancelling out results in no speed, maintain angle.
                setVelocity(0);
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
            this.setVelocity(0);
        else {
            this.setVelocity(0.1);
            double oldAngle = this.getMovementAngle();
            this.setMovementAngle(Math.atan2(dy, dx)); // Update new movement angle after a direction has changed.
            if (up && down || left && right) {  // Directions cancelling out results in no speed, preserve angle.
                setVelocity(0);
                setMovementAngle(oldAngle);
            }
        }
    }

    @Override
    public void update(long time, GameModel model) {
        this.x = shape.getX();
        this.y = shape.getY();
        // shape.shift(dx * time * 1e-9 * 10, dy * time * 1e-9 * 10); TODO use the delta
        shape.shift(getVelocityX(), getVelocityY());

        // Update direction of image
        double angle = getMovementAngle();
        if (angle >= -0.75 * Math.PI && angle < -0.25 * Math.PI) {
            direction = NORTH;
        } else if (angle >= -0.25 * Math.PI && angle < 0.25 * Math.PI) {
            direction = EAST;
        } else if (angle >= 0.25 * Math.PI && angle < 0.75 * Math.PI) {
            direction = SOUTH;
        } else if (angle >= 0.75 * Math.PI || angle < -0.75 * Math.PI) {
            direction = WEST;
        }

        // Update image being used
        if (this.getVelocity() != 0) {
            count++;
            if (count > 11) {
                count = 0;
                imageNum = (imageNum + 1) % 3;
            }
        } else {
            imageNum = 0;
        }
    }

    @Override
    public void draw(GraphicsContext gc, GameModel model) {

    }
}
