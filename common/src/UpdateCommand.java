

import java.util.Map;

public class UpdateCommand<T extends WorldObject>  {
    private WorldObject.WorldObjectData<T> data;

    public void execute(Map<WorldObject.WorldObjectData.WorldObjectReference, WorldObject> map) {
        map.get(data.id).set(data);
    }

    public void send(Model m) {
        m.receiveUpdateCommand(this);
    }
}
