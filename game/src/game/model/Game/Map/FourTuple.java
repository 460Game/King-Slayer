package game.model.Game.Map;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FourTuple {
    public static int WATER = 1;
    public static int GROUND = 2;
    public static int DIRT = 3;
    public static int GRASS = 4;
    public static int BUILDING = 5;

    public static Map<Integer, Point> map = new HashMap<>();

    public static int hashToFourTuple(int n, int s, int e, int w) {
        return n * 1000 + s * 100 + e * 10 + w;
    }

    static {
        map.put(hashToFourTuple(1,1,1,1), new Point(1,1));
    }
}
