package game.model.game.model.worldObject.entity.aiStrat;

import game.message.toServer.MakeEntityRequest;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Entities;
import game.model.game.model.worldObject.entity.entities.Minions;
import util.Util;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.AI_DATA;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public abstract class BuildingSpawnerStrat extends AIStrat {

    public static class MeleeBarracksBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final MeleeBarracksBuildingSpawnerStrat SINGLETON = new MeleeBarracksBuildingSpawnerStrat();

        @Override
        double timeBetweenSpawns() {
            return 5;
        }

        @Override
        int maxActive() {
            return 5;
        }

        @Override
        Entity makeEntity(double x, double y, Team team) {
            return Minions.makeMeleeMinion(x, y, team);
        }
    }

    public static class RangedBarracksBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final RangedBarracksBuildingSpawnerStrat SINGLETON = new RangedBarracksBuildingSpawnerStrat();

        @Override
        double timeBetweenSpawns() {
            return 5;
        }

        @Override
        int maxActive() {
            return 5;
        }

        @Override
        Entity makeEntity(double x, double y, Team team) {
            return Minions.makeRangedMinion(x, y, team);
        }
    }

    public static class SiegeBarracksBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final SiegeBarracksBuildingSpawnerStrat SINGLETON = new SiegeBarracksBuildingSpawnerStrat();

        @Override
        double timeBetweenSpawns() {
            return 5;
        }

        @Override
        int maxActive() {
            return 5;
        }

        @Override
        Entity makeEntity(double x, double y, Team team) {
            return Minions.makeSiegeMinion(x, y, team);
        }
    }

    public static class ResourceCollectorBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final ResourceCollectorBuildingSpawnerStrat SINGLETON = new ResourceCollectorBuildingSpawnerStrat();

        @Override
        double timeBetweenSpawns() {
            return 5;
        }

        @Override
        int maxActive() {
            return 5;
        }

        @Override
        Entity makeEntity(double x, double y, Team team) {
            return Minions.makeResourceMinion(x, y, team);
        }
    }


    public static class TowerBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final TowerBuildingSpawnerStrat SINGLETON = new TowerBuildingSpawnerStrat();

        @Override
        double timeBetweenSpawns() {
            return 0.1;
        }

        @Override
        int maxActive() {
            return 100;
        }

        @Override
        Entity makeEntity(double x, double y, Team team) {
            double dir = Util.random.nextDouble() * 2 * Math.PI;
            return Entities.makeArrow(x + cos(dir), y + sin(dir), dir, team);
        }
    }

    abstract double timeBetweenSpawns();

    abstract int maxActive();

    abstract Entity makeEntity(double x, double y, Team team);

    private BuildingSpawnerStrat() {
    }

    static class BuildingSpanwerStratAIData extends AIData {
        double elapsedTime;
        int spawnCounter;

        BuildingSpanwerStratAIData() {
            this.elapsedTime = 0;
            this.spawnCounter = 0;
        }
    }

    @Override
    public void init(Entity entity) {
        entity.add(AI_DATA, new BuildingSpanwerStratAIData());
    }

    @Override
    public void updateAI(Entity entity, ServerGameModel model, double seconds) {
        BuildingSpanwerStratAIData data = entity.<BuildingSpanwerStratAIData>get(AI_DATA);
        data.elapsedTime += seconds;
        while (data.elapsedTime > timeBetweenSpawns()) {
            data.elapsedTime -= timeBetweenSpawns();
            if (data.spawnCounter < maxActive()) {
                Entity newEntity = makeEntity(entity.getX(), entity.getY(), entity.getTeam());
                model.processMessage(new MakeEntityRequest(newEntity));
                newEntity.onDeath((e, serverGameModel) -> {
                    data.spawnCounter--;
                });
                data.spawnCounter++;
            }
        }
    }
}
