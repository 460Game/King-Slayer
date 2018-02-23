package game.model.game.model.worldObject.entity.aiStrat;


import game.ai.Astar;
import game.message.toClient.NewEntityCommand;
import game.message.toClient.SetEntityCommand;
import game.model.game.grid.GridCell;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Entities;
import game.model.game.model.worldObject.entity.entities.Velocity;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class MinionStrat extends AIStrat {

    private MinionStrat() {

    }

    public static class RangedMinionStrat extends MinionStrat {

        public static final RangedMinionStrat SINGLETON = new RangedMinionStrat();

        @Override
        public void updateAI(Entity entity, ServerGameModel model, double seconds) {
            // get closest king?

            MinionStratAIData data = entity.get(Entity.EntityProperty.AI_DATA);
            Astar astar = new Astar(model);
            Entity king = model.getAllEntities().parallelStream().filter((e) -> e.getTeam() != entity.getTeam() && e.has(Entity.EntityProperty.ROLE) && e.<Role>get(Entity.EntityProperty.ROLE) == Role.KING).findFirst().get();
//        System.out.println(king.id);
            int entityx = (int) (double) entity.getX();
            int entityy = (int) (double) entity.getY();
            int x = (int) (double) model.getEntity(king.id).getX();
            int y = (int) (double) model.getEntity(king.id).getY();

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

            if (entityx == x && entityy == y)
                entity.setVelocity(entity.getVelocity().withMagnitude(0));
            else if (entityx == data.path.get(0).getTopLeftX() && entityy == data.path.get(0).getTopLeftY())
                data.path.remove(0);

            else {
                // Keep moving if cells are in path.
                astar.moveToCell(entity, data.path.get(0));
                entity.setVelocity(entity.getVelocity().withMagnitude(1));
            }

            model.processMessage(new SetEntityCommand(entity));

            // TODO have Ryan make this smarter
            Random rand = new Random();
            if (rand.nextDouble() < 0.05)
                model.processMessage(new NewEntityCommand(Entities.makeArrow(entity.getX(), entity.getY(),
                    entity.<Velocity>get(Entity.EntityProperty.VELOCITY).getAngle(), entity.getTeam())));
        }

    }

    public static class MeleeMinionStrat extends MinionStrat {

        public static final MeleeMinionStrat SINGLETON = new MeleeMinionStrat();

    }

    public static class ResourceMinionStrat extends MinionStrat {

        public static final ResourceMinionStrat SINGLETON = new ResourceMinionStrat();

        @Override
        public void updateAI(Entity entity, ServerGameModel model, double seconds) {
            // TODO make better
            Random rand = new Random();
            if (rand.nextDouble() < 0.1)
                model.changeResource(entity.getTeam(), TeamResourceData.Resource.WOOD, 1);
        }

    }

    static class MinionStratAIData extends AIData {
        private List<GridCell> path;

        MinionStratAIData() {
            path = new LinkedList<>();
        }
    }

    @Override
    public void init(Entity entity) {
        entity.add(Entity.EntityProperty.AI_DATA, new MinionStratAIData());
    }

    @Override
    public void updateAI(Entity entity, ServerGameModel model, double seconds) {
        // get closest king?

        MinionStratAIData data = entity.get(Entity.EntityProperty.AI_DATA);
        Astar astar = new Astar(model);
        Entity king = model.getAllEntities().parallelStream().filter((e) -> e.getTeam() != entity.getTeam() && e.has(Entity.EntityProperty.ROLE) && e.<Role>get(Entity.EntityProperty.ROLE) == Role.KING).findFirst().get();
//        System.out.println(king.id);
        int entityx = (int) (double) entity.getX();
        int entityy = (int) (double) entity.getY();
        int x = (int) (double) model.getEntity(king.id).getX();
        int y = (int) (double) model.getEntity(king.id).getY();

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

        if (entityx == x && entityy == y)
            entity.setVelocity(entity.getVelocity().withMagnitude(0));
        else if (entityx == data.path.get(0).getTopLeftX() && entityy == data.path.get(0).getTopLeftY())
            data.path.remove(0);

        else {
            // Keep moving if cells are in path.
            astar.moveToCell(entity, data.path.get(0));
            entity.setVelocity(entity.getVelocity().withMagnitude(1));
        }

        model.processMessage(new SetEntityCommand(entity));
    }
}
