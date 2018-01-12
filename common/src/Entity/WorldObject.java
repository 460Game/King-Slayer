package Entity;

import Model.GameModel;
import client.Drawable;

import java.io.Serializable;
import java.util.UUID;

/*
a world object
paramterized with the data type that it should accept for its set method
 */
public abstract class WorldObject<T extends WorldObject.WorldObjectData> implements Drawable, Serializable {

    /*
    this exists so we can overwrite the state of an object without overwritting references to it
     */
    public static class WorldObjectData<T extends WorldObject> {
        long last_update;
        UUID uuid = UUID.randomUUID();
    }

    public T data;

    /**
     * the most important method!
     * should be additive in the sense that calling it twice with delta X and calling it once with delta 2X result in the same thing.
     */
    public abstract void update(long time, GameModel model);

    public void update(GameModel model) {
        long current_time = model.nanoTime();
        update(this.data.last_update - current_time, model);
        data.last_update = current_time;
    }

    public void set(T t) {
        this.data = t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        return this.data.uuid.equals(((WorldObject) o).data.uuid);
    }

    @Override
    public int hashCode()
    {
        return data.uuid.hashCode();
    }

}
