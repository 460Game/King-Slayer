package game.model.game.model.worldObject.entity.aiStrat;

import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Minions;
import util.Pos;

import java.util.function.Function;

public abstract class BuildingSpawnerStrat extends AIStrat {

    public static class BarracksBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final BarracksBuildingSpawnerStrat SINGLETON = new BarracksBuildingSpawnerStrat();

        @Override
        double timeBetweenSpawns() {
            return 5;
        }

        @Override
        int maxActive() {
            return 5;
        }

        @Override
        Entity function(double x, double y) {
            return Minions.makeRangedMinionTwo(x, y);
        }
    }

    abstract double timeBetweenSpawns();
    abstract int maxActive();
    abstract Entity function(double x, double y);

    public BuildingSpawnerStrat() { }

    static class BuildingSpanwerStratAIData extends AIData {
        int elapsedTime;
        int maxSpawns;
        int spawnCounter;
        int timeBetweenSpawns;
        Function<Pos, Entity> function;

        BuildingSpanwerStratAIData() {
            this.elapsedTime = 0;
            this.timeBetweenSpawns = timeBetweenSpawns;
            this.maxSpawns = maxSpawns;
            this.spawnCounter = 0;
            this.function = function;
        }
    }

    @Override
    public AIData makeAIData() {
        return new BuildingSpanwerStratAIData();
    }

    @Override
    public void updateAI(Entity entity, ServerGameModel model, double seconds) {
        BuildingSpanwerStratAIData data = (BuildingSpanwerStratAIData) entity.data.aiData;
        data.elapsedTime += seconds;
        if (data.elapsedTime > timeBetweenSpawns()) {
            data.elapsedTime -= timeBetweenSpawns();
            if (data.spawnCounter < maxActive()) {
                Entity newEntity = function(entity.data.x, entity.data.y);
                model.makeEntity(newEntity);
                newEntity.onServerDeath((e, serverGameModel) -> {
                    data.spawnCounter--;
                });
                data.spawnCounter++;
            }
        }
    }
}
