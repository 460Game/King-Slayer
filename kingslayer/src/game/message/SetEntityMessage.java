package game.message;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntityData;

/**
 * Message sent to set an entity on a client's game model. This message
 * is sent by the server.
 */
public class SetEntityMessage implements ToClientMessage {

    private EntityData data;
    private long id;

    /**
     * Default constructor needed for serialization.
     */
    public SetEntityMessage() {

    }

    /**
     * Constructor for a message, given an entity.
     * @param entity entity to be set
     */
    public SetEntityMessage(Entity entity) {
        this.data = entity.data;
        this.id = entity.id;
    }

    /**
     * This guy is a tricky one!
     *
     * If entity with same UUID already exists in this model, should copy the new one into it.
     * If it doesn't exist copy the whole thing!
     * TODO
     */
    @Override
    public void executeClient(ClientGameModel model) {
        Log.info("Client setting entity " + id);
        if(!model.setEntityData(id, data)) {
            model.requestEntityFromServer(id);
        }
    }
}
