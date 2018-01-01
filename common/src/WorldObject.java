import java.util.UUID;

/*
a world object
paramterized with the data type that it should accept for its set method
 */
public abstract class WorldObject<T extends WorldObject.WorldObjectData> implements Drawable {

    public static class WorldObjectData<T extends WorldObject> {

        public class WorldObjectReference<T> {
            private UUID id = UUID.randomUUID();

            @Override
            public boolean equals(Object obj) {
                assert(obj instanceof WorldObjectReference);
                return id.equals(((WorldObjectReference) obj).id);
            }
        }

        WorldObjectReference<T> id;
    }

    /**
     * the most important method!
     * should be additive in the sense that calling it twice with delta X and calling it once with delta 2X result in the same thing.
     */
    public abstract void update(long time, GameModel model);

    private long last_update;

    public void update(GameModel model) {
        long current_time = model.nanoTime();
        update(last_update - current_time, model);
        last_update = current_time;
    }

    T data;

    public void set(T t) {
        this.data = t;
    }
}
