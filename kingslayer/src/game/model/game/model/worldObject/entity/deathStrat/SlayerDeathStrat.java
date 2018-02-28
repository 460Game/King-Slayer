package game.model.game.model.worldObject.entity.deathStrat;

import game.message.toServer.RemoveEntityRequest;
import game.model.game.model.GameModel;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;

import java.util.function.Consumer;

public class SlayerDeathStrat extends DeathStrat {
    public final static SlayerDeathStrat SINGLETON = new SlayerDeathStrat();
    @Override
    public void handleDeath(GameModel model, Entity entity) {

        System.out.println("slayer dies");
        Consumer<ServerGameModel> serverConsumer = (server) -> {
            server.processMessage(new RemoveEntityRequest(entity));
//            server.getClients().forEach(client -> client.processMessage(new SetEntityCommand(b)));
        };
        model.execute(serverConsumer, (client) -> {
            client.slayerDead();
//            a.data.updateData.velocity.setMagnitude(0);
//            client.removeByID(a.id);
        });
    }
}
