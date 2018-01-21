package game.model.Game.Model;

import game.message.Message;

import java.util.Collection;

/**
 * Interface for a generic game model.
 */
public interface Model {

    /**
     * Process the message sent to this model.
     * @param m message to be processed
     */
    void processMessage(Message m);


    long nanoTime();
}
