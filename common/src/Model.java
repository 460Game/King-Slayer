

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Model {

    private Map<WorldObjectReference, WorldObject> objectMap = new HashMap<>();

    private Set<Model> updateListeners = new HashSet<>();
    private Set<Model> actionListeners = new HashSet<>();

    public void update() {
        for(WorldObject u : objectMap.values())
            u.update(this);
    }
    
    public void receiveUpdateCommand(UpdateCommand cmd) {
        cmd.execute(objectMap);

        for(Model l : updateListeners)
            l.receiveUpdateCommand(cmd);
    }
    
    public void receiveActionCommand(ActionCommand cmd) {
        cmd.execute(this);

        for(Model l : actionListeners)
            l.receiveActionCommand(cmd);
    }

    public void addUpdateListeners(Model m) {
        updateListeners.add(m);
    }

    public void addActionListeners(Model m) {
        actionListeners.add(m);
    }

    /*
    how you get time depends on if client or server
     */

    private static WorldClock timer = null;

    public long nanoTime() {
        if(timer == null)
            timer = getTimer();
        return timer.nanoTime();
    }

    public long miliTime() {
        return nanoTime() / 1000000;
    }

    /**
     * get the local systems timer
     * @return
     */
    abstract WorldClock getTimer();
}
