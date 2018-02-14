package game.model.game.model.worldObject.entity.collideStrat;

import com.sun.deploy.util.SessionState;
import game.message.toClient.RemoveEntityCommand;
import game.message.toClient.UpdateResourceCommand;
import game.model.game.model.ClientGameModel;
import game.model.game.model.GameModel;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.Entity;

import java.util.function.Consumer;

/**
 * Created by ryanhan on 2/13/18.
 */
public class TreasureGhostCollisionStrat extends GhostCollisionStrat{

    public void collisionSoft(GameModel model, Entity a, Entity b) {
        Consumer<ServerGameModel> serverConsumer = (server) -> {
            server.removeByID(a.id);
            server.changeResource(b.team, TeamResourceData.Resource.WOOD, (int) (Math.random() * 30 + 10)); // TODO determine what resource is given and how much
        };
        Consumer<ClientGameModel> clientConsumer = (client) -> {};
        model.execute(serverConsumer, clientConsumer);
    }
}
