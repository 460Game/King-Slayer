package Command;

import java.util.Map;

public class UpdateCommand<T extends WorldObject.WorldObjectData>  {
    private WorldObject<T> object;

    public void execute(Map<WorldObject, WorldObject> map) {
        map.putIfAbsent(object, object);
        map.get(object).set(object.data);
    }

    public void send(Model m) {
        m.receiveUpdateCommand(this);
    }

    public UpdateCommand(WorldObject<T> object) {this.object = object;}

}
