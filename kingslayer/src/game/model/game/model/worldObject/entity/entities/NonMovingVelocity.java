package game.model.game.model.worldObject.entity.entities;

/**
 * Defines a non-moving velocity class. This is used to preserve
 * the angle after an entity has stopped, to maintain the correct
 * drawing image.
 */
public class NonMovingVelocity extends Velocity {

    /**
     * X-component of the velocity that is always 0.
     */
    private final double vx = 0;

    /**
     * Y-component of the velocity that is always 0.
     */
    private final double vy = 0;

    /**
     * Angle with which the entity was last moving.
     */
    private final double angle;

    /**
     * Default constructor for serialization.
     */
    public NonMovingVelocity() {
        this(Math.PI / 2);
    }

    /**
     * Constructor given the components.
     * @param vx x-component of velocity
     * @param vy y-component of velocity
     */
    public NonMovingVelocity(double angle) {
        this.angle = angle;
    }

    /**
     * Gets the x component of the velocity. This is always
     * 0.
     * @return x component of the velocity
     */
    public double getVx() {
        return 0;
    }

    /**
     * Gets the y component of the velocity. This is always
     * 0.
     * @return y component of the velocity
     */
    public double getVy() {
        return 0;
    }

    /**
     * Gets the magnitude of the velocity. This is always
     * 0.
     * @return magnitude of the velocity
     */
    public double getMagnitude() {
        return 0;
    }

    @Override
    public double getAngle() {
        return angle;
    }

}
