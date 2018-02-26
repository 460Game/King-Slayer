package game.model.game.model.worldObject.entity.aiStrat;

import com.esotericsoftware.minlog.Log;
import game.message.toServer.MakeEntityRequest;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.*;
import util.Util;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.AI_DATA;
import static game.model.game.model.worldObject.entity.Entity.EntityProperty.HEALTH;
import static java.lang.Math.*;

public abstract class BuildingSpawnerStrat extends AIStrat {

    public enum BuildingType {
        COLLECTOR,
        TOWER,
        EXPLORER,
        BARRACKS
    }

    public static class MeleeBarracksBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final MeleeBarracksBuildingSpawnerStrat SINGLETON = new MeleeBarracksBuildingSpawnerStrat();

        @Override
        double timeBetweenSpawns(Entity entity) {
            return 2.5;
        }

        @Override
        int maxActive(Entity entity) {
            return 10 + 5 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0);
        }

        @Override
        Entity makeEntity(double x, double y, Team team, Entity entity) {
            Entity minion = Minions.makeMeleeMinion(x, y + 1, team);
           // minion.set(HEALTH, 100.0 + 10 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0));
            return minion;
        }
    }

    public static class RangedBarracksBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final RangedBarracksBuildingSpawnerStrat SINGLETON = new RangedBarracksBuildingSpawnerStrat();

        @Override
        double timeBetweenSpawns(Entity entity) {
            return 2.5;
        }

        @Override
        int maxActive(Entity entity) {
            return 10 + 5 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0);
        }

        @Override
        Entity makeEntity(double x, double y, Team team, Entity entity) {
            Entity minion = Minions.makeRangedMinion(x, y + 1, team);
         //   minion.set(HEALTH, 100.0 + 10 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0));
            return minion;
        }
    }

    public static class SiegeBarracksBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final SiegeBarracksBuildingSpawnerStrat SINGLETON = new SiegeBarracksBuildingSpawnerStrat();

        @Override
        double timeBetweenSpawns(Entity entity) {
            return 2.5;
        }

        @Override
        int maxActive(Entity entity) {
            return 10 + 5 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0);
        }

        @Override
        Entity makeEntity(double x, double y, Team team, Entity entity) {
            Entity minion = Minions.makeSiegeMinion(x, y + 1, team);
          //  minion.set(HEALTH, 100.0 + 10 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0));
            return minion;
        }
    }

    public static class ExplorationBarracksBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final ExplorationBarracksBuildingSpawnerStrat SINGLETON = new ExplorationBarracksBuildingSpawnerStrat();

        @Override
        double timeBetweenSpawns(Entity entity) {
            return 2.5;
        }

        @Override
        int maxActive(Entity entity) {
            return 10 + 5 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0);
        }

        @Override
        Entity makeEntity(double x, double y, Team team, Entity entity) {
            Entity minion = Minions.makeExplorationMinion(x, y + 1, team);
           // minion.set(HEALTH, 100.0 + 10 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0));
            return minion;
        }
    }

    public static class ResourceCollectorBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final ResourceCollectorBuildingSpawnerStrat SINGLETON = new ResourceCollectorBuildingSpawnerStrat();

        @Override
        double timeBetweenSpawns(Entity entity) {
            return 2.5;
        }

        @Override
        int maxActive(Entity entity) {
//            System.out.println("level: " + entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 10));
//            return 10 + 5 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0); // TODO change back later
            return 1;
        }

        @Override
        Entity makeEntity(double x, double y, Team team, Entity entity) {
            Entity minion = Minions.makeResourceMinion(x, y + 1, team);
         //   minion.set(HEALTH, 100.0 + 10 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0));
            return minion;
        }
    }


    public static class TowerBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final TowerBuildingSpawnerStrat SINGLETON = new TowerBuildingSpawnerStrat();

        @Override
        double timeBetweenSpawns(Entity entity) {
            return 0.1 - 0.02 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0);
        }

        @Override
        int maxActive(Entity entity) {
            return 100;
        }

        @Override
        Entity makeEntity(double x, double y, Team team, Entity entity) {
            double dir = Util.random.nextDouble() * 2 * Math.PI;
            return Entities.makeArrow(x + cos(dir), y + sin(dir), dir, team);
        }
    }

    abstract double timeBetweenSpawns(Entity entity);

    abstract int maxActive(Entity entity);

    abstract Entity makeEntity(double x, double y, Team team, Entity entity);

    private BuildingSpawnerStrat() {

    }

    public static class BuildingSpawnerStratAIData extends AIData {
        double elapsedTime;
        int spawnCounter;

        BuildingSpawnerStratAIData() {
            this.elapsedTime = 0;
            this.spawnCounter = 0;
        }
    }

    @Override
    public void init(Entity entity) {
//        Log.info("INIT AI " + entity);
        entity.add(AI_DATA, new BuildingSpawnerStratAIData());
    }

    @Override
    public void updateAI(Entity entity, ServerGameModel model, double seconds) {
//        Log.info("UPDATE AI " + entity);
        BuildingSpawnerStratAIData data = entity.<BuildingSpawnerStratAIData>get(AI_DATA);
        data.elapsedTime += seconds;
        while (data.elapsedTime > timeBetweenSpawns(entity)) {
            data.elapsedTime -= timeBetweenSpawns(entity);
            if (data.spawnCounter < maxActive(entity)) {
                Entity newEntity = makeEntity(entity.getX(), entity.getY(), entity.getTeam(), entity);
                model.processMessage(new MakeEntityRequest(newEntity));
                newEntity.onDeath((e, serverGameModel) -> {
                    data.spawnCounter--;
                });
                data.spawnCounter++;
            }
        }
    }
}
