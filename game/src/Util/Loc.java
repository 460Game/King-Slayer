package Util;

public class Loc {
    public int x;
    public int y;

    public Loc(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        Loc ot = (Loc) o;
        return this.x == ot.x && this.y == ot.y;
    }

    @Override
    public int hashCode() { return (int) (0.5 * (x + y) * (x + y + 1)) + y; }
}