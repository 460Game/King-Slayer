package game.model.game.model.worldObject.entity.entities;

import util.Util;

import static java.lang.Math.PI;

/**
 * Class to keep track of the velocity of entities.
 */
public class Velocity {

    public static final Velocity NONE = new Velocity(0, 0);

    /**
     * X-component of the velocity.
     */
    private final double vx;

    /**
     * Y-component of the velocity.
     */
    private final double vy;

    /**
     * Default constructor for serialization.
     */
    public Velocity() {
        this(0, 0);
    }

    /**
     * Constructor given the components.
     * @param vx x-component of velocity
     * @param vy y-component of velocity
     */
    public Velocity(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }

    /**
     * Gets the x component of the velocity.
     * @return x component of the velocity
     */
    public double getVx() {
        return vx;
    }

    /**
     * Gets the y component of the velocity.
     * @return y component of the velocity
     */
    public double getVy() {
        return vy;
    }

    /**
     * Gets the magnitude of the velocity.
     * @return magnitude of the velocity
     */
    public double getMagnitude() {
        return Math.sqrt(vx * vx + vy * vy);
    }

    /**
     * Sets the magnitude of the velocity to the one specified.
     * Need to draw vx and vy as well.
     * @param magnitude new magnitude of the velocity
     */
    public Velocity withMagnitude(double magnitude) {
        if (Util.closeDouble(magnitude, 0, 0.00001)) {
            return new NonMovingVelocity(getAngle());
        }
        double angle = Math.atan2(vy, vx);
        return new Velocity(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    /**
     * Gets the angle of the velocity.
     * @return angle of the velocity
     */
    public double getAngle() {
        if (vy == 0 && vx == 0)
            return PI * 0.5;
        return Math.atan2(vy, vx);
    }

    /**
     * Sets the angle of the velocity to the one specified.
     * Need to draw vx and vy as well.
     * @param angle new magnitude of the velocity
     */
    public Velocity withAngle(double angle) {
        return new Velocity(getMagnitude() * Math.cos(angle), getMagnitude() * Math.sin(angle));
    }
}
