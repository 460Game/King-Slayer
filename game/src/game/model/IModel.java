package game.model;

import game.message.Message;

import java.util.Collection;

public interface IModel {
    public abstract void processMessage(Message m);

    public abstract long nanoTime();
}
