package game.model.game.model.worldObject.entity.deathStrat;

import game.message.toClient.RemoveEntityCommand;
import game.message.toClient.SetEntityCommand;
import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

public class RemoveOnDeathStrat extends DeathStrat {
    public static final DeathStrat SINGLETON = new RemoveOnDeathStrat();

    @Override
    public void handleDeath(GameModel model, Entity entity) {
        model.remove(entity);
        model.execute(serverGameModel ->
                serverGameModel.getClients().forEach(client -> client.processMessage(new RemoveEntityCommand(entity))), clientGameModel -> {
        });
    }
}
