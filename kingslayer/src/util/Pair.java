package util;

public class Pair extends javafx.util.Pair{
    /**
     * Creates a new pair
     *
     * @param key   The key for this pair
     * @param value The value to use for this pair
     */
    public Pair(Object key, Object value) {
        super(key, value);
    }

    public static Pair pair(Object key, Object value) {
        return new Pair(key, value);
    }
}
