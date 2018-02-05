package game.model.game.model.worldObject.entity.Entities;

/**
 * Class to keep track of the velocity of entities.
 */
public class Velocity {

    /**
     * X-component of the velocity.
     */
    private double vx;

    /**
     * Y-component of the velocity.
     */
    private double vy;

    /**
     * Magnitude of the velocity.
     */
    private double magnitude;

    /**
     * Angle of the velocity.
     */
    private double angle;

    /**
     * Default constructor for serialization.
     */
    public Velocity() {
        this(0,0);
    }

    /**
     * Constructor given the components.
     * @param vx x-component of velocity
     * @param vy y-component of velocity
     */
    public Velocity(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
        magnitude = Math.sqrt(vx * vx + vy * vy);
        angle = Math.atan2(vy, vx);
    }

    /**
     * Constructor given magnitude and angle.
     * @param magnitude magnitude of velocity
     * @param angle angle of velocity
     * @param notComponent used to distinguish constructors (not used)
     */
    public Velocity(double magnitude, double angle, boolean notComponent) {
        this.magnitude = magnitude;
        this.angle = angle;
        vx = magnitude * Math.cos(angle);
        vy = magnitude * Math.sin(angle);
    }

    /**
     * Gets the x component of the velocity.
     * @return x component of the velocity
     */
    public double getVx() {
        return vx;
    }

    /**
     * Sets the x component of the velocity to the one specified.
     * Need to update magnitude and angle as well.
     * @param vx new x component of the velocity
     */
    public void setVx(double vx) {
        this.vx = vx;
        magnitude = Math.sqrt(vx * vx + vy * vy);
        angle = Math.atan2(vy, vx);
    }

    /**
     * Gets the y component of the velocity.
     * @return y component of the velocity
     */
    public double getVy() {
        return vy;
    }

    /**
     * Sets the y component of the velocity to the one specified.
     * Need to update magnitude and angle as well.
     * @param vy new y component of the velocity
     */
    public void setVy(double vy) {
        this.vy = vy;
        magnitude = Math.sqrt(vx * vx + vy * vy);
        angle = Math.atan2(vy, vx);
    }

    /**
     * Gets the magnitude of the velocity.
     * @return magnitude of the velocity
     */
    public double getMagnitude() {
        return magnitude;
    }

    /**
     * Sets the magnitude of the velocity to the one specified.
     * Need to update vx and vy as well.
     * @param magnitude new magnitude of the velocity
     */
    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
        vx = magnitude * Math.cos(angle);
        vy = magnitude * Math.sin(angle);
    }

    /**
     * Gets the angle of the velocity.
     * @return angle of the velocity
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Sets the angle of the velocity to the one specified.
     * Need to update vx and vy as well.
     * @param angle new magnitude of the velocity
     */
    public void setAngle(double angle) {
        this.angle = angle;
        vx = magnitude * Math.cos(angle);
        vy = magnitude * Math.sin(angle);
    }
}
