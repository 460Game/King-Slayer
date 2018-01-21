package game.model.Game.Model;

import game.message.Message;

import java.util.Collection;

public interface Model {
    public abstract void processMessage(Message m);
    public abstract long nanoTime();
}
