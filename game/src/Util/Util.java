package Util;

import java.util.Random;
import java.util.Set;
import com.esotericsoftware.kryo.Kryo;

public class Util {

    /**
     * Helper random class to generate random seeds.
     */
    public static Random random = new Random();

    public static <T> boolean setsIntersect(Set<T> a, Set<T> b) {
        for(T t : a) {
            if(b.contains(t))
                return true;
        }
        return false;
    }

    public static <T> boolean setsDisjoint(Set<T> a, Set<T> b) {
        return !setsIntersect(a,b);
    }

    public static double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt( (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}
