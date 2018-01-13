package Model;

import Command.ActionCommand;
import Command.UpdateCommand;
import Entity.WorldObject;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Model {

    private Map<WorldObject, WorldObject> objectMap = new HashMap<>();

    private Set<Model> updateListeners = new HashSet<>();
    private Set<Model> actionListeners = new HashSet<>();

    private WorldClock timer = new WorldClock() {
        @Override
        public long nanoTime() {
            return System.nanoTime();
        }
    };

    public void updateAll() {
        for(WorldObject u : objectMap.values())
            this.reqUpdate(u);
    }
    public Map<WorldObject, WorldObject> getMap () {
        return objectMap;
    }
    public abstract void draw(GraphicsContext gc);

    public abstract void reqUpdate(WorldObject u);
    
    public void receiveUpdateCommand(UpdateCommand cmd) {
        cmd.execute(this);

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
    public abstract WorldClock getTimer();

    public void drawAll(GraphicsContext gc) {
        for(WorldObject u : objectMap.values())
            u.draw(gc);
    }
}
