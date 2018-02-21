package game.model.game.model.worldObject.entity.deathStrat;

import game.message.toClient.RemoveEntityCommand;
import game.message.toClient.SetEntityCommand;
import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.collideStrat.CollisionStrat;

public class RemoveOnDeathStrat extends DeathStrat {
    public static final DeathStrat SINGLETON = new RemoveOnDeathStrat();

    @Override
    public void handleDeath(GameModel model, Entity entity) {
        model.execute(serverGameModel ->
            serverGameModel.processMessage(new RemoveEntityCommand(entity)), clientGameModel -> {});
            model.execute(serverGameModel -> serverGameModel.removeByID(entity.id), clientGameModel ->  clientGameModel.removeByID(entity.id));
//        if (model.getAllEntities().contains(entity))
//            System.out.println("Still not removed");
//        else
//            System.out.println("REMOVED!!!!");
    }
}
