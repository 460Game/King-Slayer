package Util;

/**
 * Class to represent location on the game map as points with x and
 * y-coordinates.
 */
public class Loc {

    /**
     * X-coordinate.
     */
    public int x;

    /**
     * Y-coordinate.
     */
    public int y;

    /**
     * Constructor for a location with given coordinates.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Loc(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        Loc ot = (Loc) o;
        return x == ot.x && y == ot.y;
    }

    @Override
    public int hashCode() { return (int) (0.5 * (x + y) * (x + y + 1)) + y; }
}