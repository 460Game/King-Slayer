package Command;

import Entity.WorldObject;
import Model.Model;

import java.util.Map;

public class UpdateMessage<T extends WorldObject.WorldObjectData> extends Message {
    private WorldObject<T> object;

    public void execute(Map<WorldObject, WorldObject> map) {
        map.putIfAbsent(object, object);
        map.get(object).set(object.data);
    }

    public void send(Model m) {
        m.receiveUpdateCommand(this);
    }

    public UpdateMessage(WorldObject<T> object) {this.object = object;}

}
