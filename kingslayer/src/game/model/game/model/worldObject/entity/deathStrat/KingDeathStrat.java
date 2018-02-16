package game.model.game.model.worldObject.entity.deathStrat;

import game.message.toClient.SetEntityCommand;
import game.model.game.model.GameModel;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;

import java.util.function.Consumer;

public class KingDeathStrat extends DeathStrat {
    public final static KingDeathStrat SINGLETON = new KingDeathStrat();
    @Override
    public void handleDeath(GameModel model, Entity entity) {

        System.out.println("A king dies!");
        Consumer<ServerGameModel> serverConsumer = (server) -> {
            server.teamWin((entity.team == Team.ONE) ? Team.TWO : Team.ONE);
//            server.getClients().forEach(client -> client.processMessage(new SetEntityCommand(b)));
        };
        model.execute(serverConsumer, (client) -> {
//            a.data.updateData.velocity.setMagnitude(0);
//            client.removeByID(a.id);
        });
    }

}
