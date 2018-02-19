package game.model.game.model.worldObject.entity.aiStrat;

import com.esotericsoftware.minlog.Log;
import game.message.toServer.EntityBuildRequest;
import game.message.toServer.MakeEntityRequest;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Minions;

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
        Entity makeEntity(double x, double y, Team team) {
            return Minions.makeRangedMinion(x, y, team);
        }
    }

    abstract double timeBetweenSpawns();
    abstract int maxActive();
    abstract Entity makeEntity(double x, double y, Team team);

    private BuildingSpawnerStrat() { }

    static class BuildingSpanwerStratAIData extends AIData {
        double elapsedTime;
        int spawnCounter;

        BuildingSpanwerStratAIData() {
            this.elapsedTime = 0;
            this.spawnCounter = 0;
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
        Log.info("BUilding spawner strat " + data.elapsedTime);
        if (data.elapsedTime > timeBetweenSpawns()) {
            data.elapsedTime -= timeBetweenSpawns();
            if (data.spawnCounter < maxActive()) {
                Entity newEntity = makeEntity(entity.data.x, entity.data.y, entity.team);
                model.processMessage(new MakeEntityRequest(newEntity));
                newEntity.onServerDeath((e, serverGameModel) -> {
                    data.spawnCounter--;
                });
                data.spawnCounter++;
            }
        }
    }
}
