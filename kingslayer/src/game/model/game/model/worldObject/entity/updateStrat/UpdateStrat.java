package game.model.game.model.worldObject.entity.updateStrat;

import game.model.game.grid.GridCell;
import game.model.game.model.GameModel;
import game.model.game.model.Model;
import game.model.game.model.worldObject.entity.Entity;

import java.util.Collection;
import java.util.Set;

public abstract class UpdateStrat {

    /**
     * Time of the last update this entity performed.
     */
    private long last_update;

    /**
     * Updates the entity in the game model.
     * @param model current game model
     */
    public void update(Entity entity, GameModel model) {
        long current_time = model.nanoTime();
        update(current_time - last_update);
        last_update = current_time;

        if(entity.data.shape.moved()) {
            Collection<GridCell> afterSet = entity.data.shape.getCells(model);

            if(entity.containedIn != null)
                for (GridCell cell : entity.containedIn)
                    if(!afterSet.contains(cell))
                        cell.removeContents(entity);
            for (GridCell cell : afterSet)
                cell.addContents(entity);
        }
    }

    protected abstract void update(long l);

    UpdateStrat(Model model) {
        last_update = model.nanoTime();
    }
}
