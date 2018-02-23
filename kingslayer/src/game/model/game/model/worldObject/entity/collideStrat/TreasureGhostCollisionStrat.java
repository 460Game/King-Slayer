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
 * Defines how treasure collides with other entities in the world.
 */
public class TreasureGhostCollisionStrat extends GhostCollisionStrat{

    /**
     * Only one instance of a treasure collision strategy is created. All treasure
     * use this strategy to collide.
     */
    public static TreasureGhostCollisionStrat SINGLETON = new TreasureGhostCollisionStrat();

    @Override
    public void collisionSoft(GameModel model, Entity a, Entity b) {
        // Server should remove the treasure and draw the resource counts of the team that
        // collected the treasure by a certain amount.
        Consumer<ServerGameModel> serverConsumer = (server) -> {
            server.removeByID(a.id);
            server.changeResource(b.getTeam(), TeamResourceData.Resource.WOOD, (int) (Math.random() * 30 + 10));
            // TODO determine what resource is given and how much
        };
        model.execute(serverConsumer, (client) -> {});
//        System.out.println("Colliding with treasure!");
    }
}
