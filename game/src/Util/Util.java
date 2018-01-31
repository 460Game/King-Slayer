package Util;

import java.util.Random;
import java.util.Set;
import com.esotericsoftware.kryo.Kryo;

/**
 * Class used for various helper functions.
 */
public class Util {

    /**
     * Helper random class to generate random seeds.
     */
    public static Random random = new Random();

    /**
     * Returns true if the two sets have a non-empty intersection.
     * Returns false otherwise.
     * @param a first set
     * @param b second set
     * @param <T> item type in the set
     * @return true if the two sets have a non-empty intersection
     */
    public static <T> boolean setsIntersect(Set<T> a, Set<T> b) {
        for(T t : a) {
            if(b.contains(t))
                return true;
        }
        return false;
    }

    /**
     * Returns true if the two sets are disjoint. Returns false otherwise.
     * @param a first set
     * @param b second set
     * @param <T> T item type in the set
     * @return true if the two sets are disjoint
     */
    public static <T> boolean setsDisjoint(Set<T> a, Set<T> b) {
        return !setsIntersect(a,b);
    }

    /**
     * Determines the distance between two points.
     * @param x1 x-coordinate of first point
     * @param y1 y-coordinate of first point
     * @param x2 x-coordinate of second point
     * @param y2 y-coordinate of second point
     * @return distance between the two points
     */
    public static double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt( (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * Check if the two doubles are closer than a set distance. Return true
     * if the two numbers are within that distance. Return false otherwise.
     * @param a first number to compare
     * @param b second number to compare.
     * @return true if the two numbers are closer than a set distance
     */
    public static boolean closeDouble(double a, double b) {
        return Math.abs(a - b) < 10e-5;
    }

    /**
     * Returns the angle between two points. This angle ranges from
     * -pi to pi.
     * @param x1 x-coordinate of first point
     * @param y1 y-coordinate of first point
     * @param x2 x-coordinate of second point
     * @param y2 y-coordinate of second point
     * @return angle between the two points
     */
    public static double angle2Points(double x1, double y1, double x2, double y2) {
        return Math.atan2(y2 - y1, x2 - x1);
    }
}
