package game.model.game.model.worldObject.entity.deathStrat;

import com.esotericsoftware.kryonet.Server;
import game.model.game.model.GameModel;
import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;

import java.util.function.Consumer;


public class BuiltWallDeathStrat extends DeathStrat {

    public static BuiltWallDeathStrat SINGLETON = new BuiltWallDeathStrat();

    @Override
    public void handleDeath(GameModel model, Entity entity) {
        model.execute((server) -> server.removeByID(entity.id), (client) -> client.removeByID(entity.id));
    }
}
