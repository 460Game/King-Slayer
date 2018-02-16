package game.model.game.model.worldObject.entity.collideStrat;

import game.message.toClient.SetEntityCommand;
import game.model.game.model.*;
import game.model.game.model.worldObject.entity.Entity;

import java.util.function.Consumer;

import static util.Util.checkBounds;
import static util.Const.*;

/**
 * Defines how an arrow collides with other entities in the world.
 */
public class ArrowCollisionStrat extends ProjectileCollisionStrat {

    /**
     * Only one instance of a arrow collision strategy is created. All arrows
     * use this strategy to collide.
     */
    public static ArrowCollisionStrat SINGLETON = new ArrowCollisionStrat();

    @Override
    public void collisionSoft(GameModel model, Entity a, Entity b) {
        // Server stops the arrow and removes it from the game. Entity b loses an
        // appropriate amount of health. All servers update entity b's health.
        Consumer<ServerGameModel> serverConsumer = (server) -> {
            a.data.updateData.velocity.setMagnitude(0);
            server.removeByID(a.id);
            b.decreaseHealthBy(model, 5);
            server.getClients().forEach(client -> client.processMessage(new SetEntityCommand(b)));
        };

        // As a precaution, on the client side, stop the arrow and remove it from the game.
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

        // Normally an arrow should go over water fine. This check only stops the arrow
        // from going out of the bounds of the map and causing an error.
        if (!checkBounds(a.data.x - a.data.hitbox.getRadius(ANGLE_LEFT), a.data.y - a.data.hitbox.getRadius(ANGLE_UP)) ||
                !checkBounds(a.data.x + a.data.hitbox.getRadius(ANGLE_RIGHT), a.data.y + a.data.hitbox.getRadius(ANGLE_DOWN)))
            model.execute(serverConsumer, clientConsumer);
    }

    @Override
    public void collisionHard(GameModel model, Entity a, Entity b) {
        // Both the client and server stops the arrow and removes it from the game.
        model.execute((server) -> {
            if (b.data.getHealth() != Double.POSITIVE_INFINITY)
                b.decreaseHealthBy(model, 100); // TODO CHANGE THIS
            a.data.updateData.velocity.setMagnitude(0);
            server.removeByID(a.id);
        }, (client) -> {
            if (b.data.getHealth() != Double.POSITIVE_INFINITY)
                b.decreaseHealthBy(model, 100);
            a.data.updateData.velocity.setMagnitude(0);
            client.removeByID(a.id);
            System.out.println(b.data.getHealth());
        });
    }
}
