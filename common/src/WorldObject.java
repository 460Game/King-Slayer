
/*
a world object
paramterized with the data type that it should accept for its set method
 */
public abstract class WorldObject<T extends WorldObjectData> implements Drawable {

    /**
     * the most important method!
     * should be additive in the sense that calling it twice with delta X and calling it once with delta 2X result in the same thing.
     */
    public abstract void update(long time, Model model);

    private long last_update;

    public void update(Model model) {
        long current_time = model.nanoTime();
        update(last_update - current_time, model);
        last_update = current_time;
    }

    public abstract void set(T t);
}
