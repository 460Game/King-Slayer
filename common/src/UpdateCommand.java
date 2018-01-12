

import java.util.Map;

public class UpdateCommand<T extends WorldObject.WorldObjectData>  {
    private WorldObject<T> object;

    public void execute(Model model) {
        Map<WorldObject, WorldObject> map = model.model;
        map.putIfAbsent(object, object);
        map.get(object).set(object.data);
    }

    public void send(Model m) {
        m.receiveUpdateCommand(this);
    }

    public UpdateCommand(WorldObject<T> object) {this.object = object;}

}
