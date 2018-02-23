package util;

import com.esotericsoftware.minlog.Log;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.*;
import java.util.*;

import static java.lang.Math.PI;
import static util.Const.*;

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
        for(T t : a)
            if(b.contains(t))
                return true;
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
     * @param precision how close the two numbers have to be
     * @return true if the two numbers are closer than a set distance
     */
    public static boolean closeDouble(double a, double b, double precision) {
        return Math.abs(a - b) < precision;
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

    /**
     * Converts the coordinate on the game world to a drawing coordinate.
     * @param worldPos world coordinate of an entity
     * @return the drawing coordinate of the corresponding world coordinate
     */
    public static int toDrawCoords(double worldPos) {
        return (int) (worldPos * TILE_PIXELS);
    }

    /**
     * Converts the drawing coordinate to a coordinate on the game world.
     * @param drawCords drawing coordinate of an entity
     * @return the world coordinate of the corresponding drawing coordinate
     */
    public static double toWorldCoords(int drawCords) {
        return ((double) drawCords) / TILE_PIXELS;
    }

    /**
     * Converts the drawing coordinates to a coordinate on the game world.
     * @param drawCords drawing coordinate of an entity
     * @return the world coordinate of the corresponding drawing coordinate
     */
    public static double toWorldCoords(double drawCords) {
        return drawCords / TILE_PIXELS;
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws an image on a graphics context.
     *
     * The image is drawn at (tlpx, tlpy) rotated by angle pivoted around the point:
     *   (tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2)
     *https://stackoverflow.com/questions/18260421/how-to-draw-image-rotated-on-javafx-canvas
     *
     *
     * @param gc the graphics context the image is to be drawn on.
     * @param angle the angle of rotation.
     * @param tlpx the top left x co-ordinate where the image will be plotted (in canvas co-ordinates).
     * @param tlpy the top left y co-ordinate where the image will be plotted (in canvas co-ordinates).
     */
    public static void drawRotatedImage(GraphicsContext gc, Image image, double angle,
                                        double tlpx, double tlpy, double destw, double desth) {
      //  gc.save(); // saves the current state on stack, including the current transform
        angle = 180 * angle/ PI;
        gc.transform(new Affine(Transform.rotate(angle, tlpx + destw / 2, tlpy + desth / 2)));
        gc.drawImage(image, tlpx, tlpy, destw, desth);
        gc.transform(new Affine(Transform.rotate(-angle, tlpx + destw / 2, tlpy + desth / 2)));
      //  gc.restore(); // back to original state (before rotation)
    }

    /**
     * Check if the coordinates given are within the bounds of the game map. Return
     * true if they are; otherwise, return false.
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true if the coordinates are within the bounds
     */
    public static boolean checkBounds(double x, double y) {
        return x > 1 && y > 1 && x < GRID_X_SIZE - 1 && x < GRID_Y_SIZE - 1;
    }
}
