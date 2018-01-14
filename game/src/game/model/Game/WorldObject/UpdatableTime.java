package game.model.Game.WorldObject;

import game.model.Game.GameModel;

public interface UpdatableTime {

    public abstract void update(long time, GameModel model);

    public abstract long getLastUpdate();
    public abstract void setLastUpdate(long time);

    public default void update(GameModel model) {
        long current_time = model.nanoTime();
        update(current_time - getLastUpdate(), model);
        setLastUpdate(current_time);
    }
}
