

import java.util.Map;

public class UpdateCommand<T extends WorldObject>  {
    private WorldObjectReference<T> toUpdate;
    private WorldObjectData<T> data;

    public void execute(Map<WorldObjectReference, WorldObject> map) {
        map.get(toUpdate).set(data);
    }

    public void send(Model m) {
        m.receiveUpdateCommand(this);
    }
}
