package game.message.toClient;

import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Command sent by the server to a client to ensure that an entity's
 * data is updated on that client.
 */
public class SyncEntityFieldCommand implements ToClientCommand {

    /**
     * Data corresponding to the entity's field.
     */
    private Object data;

    /**
     * Entity property field to be updated.
     */
    private Entity.EntityProperty field;

    /**
     * Id of the entity that needs to update its data.
     */
    private long id;

    /**
     * Constructor of a command, given an entity and field to be updated
     * @param e entity whose data needs to be updated
     * @param syncField property to be updated
     */
    public SyncEntityFieldCommand(Entity e, Entity.EntityProperty syncField) {
        this.data = e.get(syncField);
        this.field = syncField;
        this.id = e.id;
    }

    /**
     * Default constructor needed for serialization.
     */
    private SyncEntityFieldCommand() {}

    @Override
    public void executeClient(ClientGameModel model) {
        if(model.getEntity(id) != null)
            model.getEntity(id).setOrAdd(this.field, data);
    }
}
