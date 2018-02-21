package game.model.game.model.worldObject.entity.aiStrat;


import game.ai.Astar;
import game.message.toClient.SetEntityCommand;
import game.model.game.grid.GridCell;
import game.model.game.model.ClientGameModel;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.worldObject.entity.Entity;

import java.util.LinkedList;
import java.util.List;

public abstract class MinionStrat extends AIStrat {

    private MinionStrat() {

    }

    public static class RangedMinionStrat extends MinionStrat {

        public static final RangedMinionStrat SINGLETON = new RangedMinionStrat();


    }

    public static class MeleeMinionStrat extends MinionStrat {

        public static final MeleeMinionStrat SINGLETON = new MeleeMinionStrat();

    }

    public static class ResourceMinionStrat extends MinionStrat {

        public static final ResourceMinionStrat SINGLETON = new ResourceMinionStrat();

    }

    static class MinionStratAIData extends AIData {
        private List<GridCell> path;

        MinionStratAIData() {
            path = new LinkedList<>();
        }
    }

    @Override
    public AIData makeAIData() {
        return new MinionStratAIData();
    }

    @Override
    public void updateAI(Entity entity, ServerGameModel model, double seconds) {
        // get closest king?

        MinionStratAIData data = (MinionStratAIData) entity.data.aiData;
        Astar astar = new Astar(model);
        Entity king = model.getAllEntities().parallelStream().filter((e) -> e.team != entity.team && e.role == Role.KING).findFirst().get();

        int entityx = (int) entity.data.x;
        int entityy = (int) entity.data.y;
        int x = (int) model.getEntity(king.id).data.x;
        int y = (int) model.getEntity(king.id).data.y;

        // Check if path exists and king has moved, then generate a new path.
        if (data.path.size() > 0 && data.path.get(data.path.size() - 1).getTopLeftX() != x &&
                data.path.get(data.path.size() - 1).getTopLeftY() != y) {
            data.path.clear();
            data.path = astar.astar(model.getCell(entityx, entityy), model.getCell(x, y));
        }

        // If nothing in path and not at destination, generate a path.
        if (data.path.size() == 0 && entityx != x && entityy != y) {
            data.path = astar.astar(model.getCell(entityx, entityy), model.getCell(x, y));
            System.out.println("Found path");
        }

        // might need to check for empty path
        // If reached destination, stop.
        if (entityx == x && entityy == y) {
            entity.data.updateData.velocity.setMagnitude(0);
            data.path.clear();
        } else if (entityx == data.path.get(0).getTopLeftX() && entityy == data.path.get(0).getTopLeftY())
            data.path.remove(0); // Remove the first cell in path if it has been reached.
        else {
            // Keep moving if cells are in path.
            astar.moveToCell(entity, data.path.get(0));
            entity.data.updateData.velocity.setMagnitude(1);
        }

        model.processMessage(new SetEntityCommand(entity));
    }
}
