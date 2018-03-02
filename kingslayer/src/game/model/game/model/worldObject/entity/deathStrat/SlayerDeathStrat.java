package game.model.game.model.worldObject.entity.deathStrat;

import game.message.toClient.RemoveEntityCommand;
import game.message.toClient.SlayerDieCommand;
import game.message.toServer.RemoveEntityRequest;
import game.message.toServer.SlayerRespawnStartCountRequest;
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
        Team team = entity.getTeam();
        String name = entity.get(Entity.EntityProperty.PLAYER_NAME);
        Consumer<ServerGameModel> serverConsumer = (server) -> {
            server.remove(entity);
          //  server.processMessage(new SlayerD);
            server.processMessage(new SlayerDieCommand(entity.id));
            server.processMessage(new RemoveEntityCommand(entity));
            server.processMessage(new SlayerRespawnStartCountRequest(team, name));
//            server.getClients().forEach(client -> client.processMessage(new SetEntityCommand(b)));
        };
        model.execute(serverConsumer, (client) -> {
  //      client.slayerDead();
//            a.data.updateData.velocity.setMagnitude(0);
//            client.removeByID(a.id);
        });
    }
}
