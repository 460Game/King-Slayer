package game.model.game.model.worldObject.entity.collideStrat;

import game.message.toClient.SetEntityCommand;
import game.model.game.model.ClientGameModel;
import game.model.game.model.GameModel;
import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;

import java.util.function.Consumer;

import static util.Util.checkBounds;
import static util.Const.*;

public class ArrowCollisionStrat extends ProjectileCollisionStrat {

    public static ArrowCollisionStrat SINGLETON = new ArrowCollisionStrat();

    @Override
    public void collisionSoft(GameModel model, Entity a, Entity b) {
        Consumer<ServerGameModel> serverConsumer = (server) -> {
            a.data.updateData.velocity.setMagnitude(0);
            server.removeByID(a.id);
            b.decreaseHealthBy(model, 5);
            server.getClients().forEach(client -> client.processMessage(new SetEntityCommand(b)));
        };
        model.execute(serverConsumer, (client) -> {
            a.data.updateData.velocity.setMagnitude(0);
            client.removeByID(a.id);
        });
    }

    @Override
    public void collisionWater(GameModel model, Entity a, Entity b) {
        Consumer<ServerGameModel> serverConsumer = (server) -> {
            a.data.updateData.velocity.setMagnitude(0);
            server.removeByID(a.id);
        };
        Consumer<ClientGameModel> clientConsumer = (client) -> {
            a.data.updateData.velocity.setMagnitude(0);
            client.removeByID(a.id);
        };
        if (!checkBounds(a.data.x - a.data.hitbox.getRadius(ANGLE_LEFT), a.data.y - a.data.hitbox.getRadius(ANGLE_UP)) ||
                !checkBounds(a.data.x + a.data.hitbox.getRadius(ANGLE_RIGHT), a.data.y + a.data.hitbox.getRadius(ANGLE_DOWN)))
            model.execute(serverConsumer, clientConsumer);
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
