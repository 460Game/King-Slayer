package game.message.toClient;

import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;

public class SyncEntityFeildCommand implements ToClientCommand {

    private Object data;
    private Entity.EntityProperty feild;
    private long id;

    private SyncEntityFeildCommand() {}

    public SyncEntityFeildCommand(Entity e, Entity.EntityProperty syncFeild) {
        this.data = e.get(syncFeild);
        this.feild = syncFeild;
        this.id = e.id;
    }

    @Override
    public void executeClient(ClientGameModel model) {
        if(model.getEntity(id) != null)
            model.getEntity(id).setOrAdd(this.feild, data);
    }
}
