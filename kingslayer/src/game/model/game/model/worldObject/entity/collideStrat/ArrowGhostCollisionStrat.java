package game.model.game.model.worldObject.entity.collideStrat;

import game.message.toClient.SetEntityCommand;
import game.model.game.model.GameModel;
import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;

import java.util.function.Consumer;

/**
 * Created by ryanhan on 2/14/18.
 */
public class ArrowGhostCollisionStrat extends GhostCollisionStrat {

    public static ArrowGhostCollisionStrat SINGLETON = new ArrowGhostCollisionStrat();

    @Override
    public void collisionSoft(GameModel model, Entity a, Entity b) {
        Consumer<ServerGameModel> serverConsumer = (server) -> {
            a.data.updateData.velocity.setMagnitude(0);
            server.removeByID(a.id);
            server.getEntityById(b.id).data.health -= 10;
            server.getClients().forEach(client -> client.processMessage(new SetEntityCommand(server.getEntityById(b.id))));
        };
        model.execute(serverConsumer, (client) -> {
            a.data.updateData.velocity.setMagnitude(0);
            client.removeByID(a.id);
        });
    }

    @Override
    public void collisionHard(GameModel model, Entity a, Entity b) {
        model.execute((server) -> {
            a.data.updateData.velocity.setMagnitude(0);
            server.removeByID(a.id);
        }, (client) -> {
            a.data.updateData.velocity.setMagnitude(0);
            client.removeByID(a.id);
        });
    }
}
