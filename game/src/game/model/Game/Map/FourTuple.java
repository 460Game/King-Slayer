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

    public static int hashToFourTuple(int cur,int n, int e, int s, int w) {
        return cur * 10000 + n * 1000 + e * 100 + s * 10 + w;
    }

    public static int hashToFourTuple(int cur,int n, int e, int s, int w, int shallow) {
        return cur * 10000 + n * 1000 + e * 100 + s * 10 + w + shallow;
    }

    static {
        map.put(hashToFourTuple(GRASS, WATER, GRASS, GRASS, WATER), new Point(0, 32));
        map.put(hashToFourTuple(GRASS, WATER, GRASS, GRASS, GRASS), new Point(32, 32));
        map.put(hashToFourTuple(GRASS, WATER, WATER, GRASS, GRASS), new Point(64, 32));
        map.put(hashToFourTuple(GRASS, GRASS, GRASS, GRASS, WATER), new Point(0, 64));
        map.put(hashToFourTuple(GRASS, GRASS, GRASS, GRASS, GRASS), new Point(32, 64));
        map.put(hashToFourTuple(GRASS, GRASS, WATER, GRASS, GRASS), new Point(64, 64));
        map.put(hashToFourTuple(GRASS, GRASS, GRASS, WATER, WATER), new Point(0, 96));
        map.put(hashToFourTuple(GRASS, GRASS, GRASS, WATER, GRASS), new Point(32, 96));
        map.put(hashToFourTuple(GRASS, GRASS, WATER, WATER, GRASS), new Point(64, 96));

//        map.put(hashToFourTuple(GRASS, DIRT, GRASS, GRASS, DIRT), new Point(32, 64));
//        map.put(hashToFourTuple(GRASS, DIRT, GRASS, GRASS, GRASS), new Point(32, 64));
//        map.put(hashToFourTuple(GRASS, DIRT, DIRT, GRASS, GRASS), new Point(32, 64));
//        map.put(hashToFourTuple(GRASS, GRASS, GRASS, GRASS, DIRT), new Point(32, 64));
//        map.put(hashToFourTuple(GRASS, GRASS, DIRT, GRASS, GRASS), new Point(32, 64));
//        map.put(hashToFourTuple(GRASS, GRASS, GRASS, DIRT, DIRT), new Point(32, 64));
//        map.put(hashToFourTuple(GRASS, GRASS, GRASS, DIRT, GRASS), new Point(32, 64));
//        map.put(hashToFourTuple(GRASS, GRASS, DIRT, DIRT, GRASS), new Point(32, 64));

        map.put(hashToFourTuple(DIRT, GRASS, DIRT, DIRT, GRASS), new Point(96, 160));
        map.put(hashToFourTuple(DIRT, GRASS, DIRT, DIRT, DIRT), new Point(128, 160));
        map.put(hashToFourTuple(DIRT, GRASS, GRASS, DIRT, DIRT), new Point(160, 160));
        map.put(hashToFourTuple(DIRT, DIRT, DIRT, DIRT, GRASS), new Point(96, 192));
        map.put(hashToFourTuple(DIRT, DIRT, DIRT, DIRT, DIRT), new Point(128, 192));
        map.put(hashToFourTuple(DIRT, DIRT, GRASS, DIRT, DIRT), new Point(160, 192));
        map.put(hashToFourTuple(DIRT, DIRT, DIRT, GRASS, GRASS), new Point(96, 224));
        map.put(hashToFourTuple(DIRT, DIRT, DIRT, GRASS, DIRT), new Point(128, 224));
        map.put(hashToFourTuple(DIRT, DIRT, GRASS, GRASS, DIRT), new Point(160, 224));

        map.put(hashToFourTuple(WATER, GRASS, WATER, WATER, WATER), new Point(32, 640));
        map.put(hashToFourTuple(WATER, GRASS, GRASS, WATER, WATER), new Point(32, 640));
        map.put(hashToFourTuple(WATER, GRASS, WATER, GRASS, WATER), new Point(32, 640));
        map.put(hashToFourTuple(WATER, GRASS, WATER, WATER, GRASS), new Point(32, 640));
        map.put(hashToFourTuple(WATER, GRASS, DIRT, WATER, WATER), new Point(32, 640));
        map.put(hashToFourTuple(WATER, GRASS, WATER, DIRT, WATER), new Point(32, 640));
        map.put(hashToFourTuple(WATER, GRASS, WATER, WATER, DIRT), new Point(32, 640));

        map.put(hashToFourTuple(WATER, GRASS, WATER, WATER, WATER, 1), new Point(32, 544));
        map.put(hashToFourTuple(WATER, GRASS, GRASS, WATER, WATER, 1), new Point(32, 544));
        map.put(hashToFourTuple(WATER, GRASS, WATER, GRASS, WATER, 1), new Point(32, 544));
        map.put(hashToFourTuple(WATER, GRASS, WATER, WATER, GRASS, 1), new Point(32, 544));
        map.put(hashToFourTuple(WATER, GRASS, DIRT, WATER, WATER, 1), new Point(32, 544));
        map.put(hashToFourTuple(WATER, GRASS, WATER, DIRT, WATER, 1), new Point(32, 544));
        map.put(hashToFourTuple(WATER, GRASS, WATER, WATER, DIRT, 1), new Point(32, 544));

    }
}
