import java.util.Set;

public class Util {
    static <T> boolean setsIntersect(Set<T> a, Set<T> b) {
        for(T t : a) {
            if(b.contains(t))
                return true;
        }
        return false;
    }

    static <T> boolean setsDisjoint(Set<T> a, Set<T> b) {
        return !setsIntersect(a,b);
    }

    static double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt( (x1 - x2) + (y1 - y2));
    }
}
