package game.model.game.model;

import game.message.Message;

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
