package util;

/*
like loc but uses doubles
 */
public class Pos {

    /**
     * X-coordinate.
     */
    public double x;

    /**
     * Y-coordinate.
     */
    public double y;

    /**
     * Constructor for a location with given coordinates.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Pos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        Pos ot = (Pos) o;
        return Util.closeDouble(x, ot.x) && Util.closeDouble(y, ot.y);
    }

    @Override
    public int hashCode() {
        int xh = Double.hashCode(x);
        int yh = Double.hashCode(y);
        return (int) ((0.5 * (xh + yh) * (xh + yh + 1)) + yh); }
}
