package game.model.Game.WorldObject;

import game.model.Drawable;
import game.model.Game.GameModel;
import game.model.Game.Grid.GridCell;

import java.util.UUID;

public abstract class Entity implements UpdatableTime, Drawable {
    public abstract void collision(Entity b);

    private UUID uuid = UUID.randomUUID();
    private long last_update;

    public UUID getUuid() {
        return uuid;
    }

    Entity(GameModel model) {
        last_update = model.nanoTime();
    }

    @Override
    public long getLastUpdate() {
        return last_update;
    }

    @Override
    public void setLastUpdate(long t) {
        last_update = t;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        Entity entity = (Entity) o;
        // field comparison
        return this.uuid.equals(entity.uuid);
    }

}
