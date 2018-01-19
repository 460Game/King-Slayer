package Util;

/**
 * Class to represent location on the game map as points with x and
 * y-coordinates.
 */
public class Loc {

    /**
     * X-coordinate.
     */
    private int x;

    /**
     * Y-coordinate.
     */
    private int y;

    /**
     * Constructor for a location with given coordinates.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Loc(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate.
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate to the one specified.
     * @param x new x-coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate.
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate to the one specified.
     * @param y new y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        Loc ot = (Loc) o;
        return x == ot.getX() && y == ot.getY();
    }

    @Override
    public int hashCode() { return (int) (0.5 * (x + y) * (x + y + 1)) + y; }
}