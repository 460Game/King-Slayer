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
        // As a precaution, on the client side, stop the arrow and remove it from the game.
        model.execute((server) -> {
            a.data.updateData.velocity.setMagnitude(0);
            a.entityDie(server);
            b.decreaseHealthBy(model, 5);
            server.getClients().forEach(client -> client.processMessage(new SetEntityCommand(b)));
        }, (client) -> {
            a.data.updateData.velocity.setMagnitude(0);
            a.entityDie(client);
        });
    }

    @Override
    public void collisionWater(GameModel model, Entity a, Entity b) {
        // Normally an arrow should go over water fine. This check only stops the arrow
        // from going out of the bounds of the map and causing an error.
        if (!checkBounds(a.data.x - a.data.hitbox.getRadius(ANGLE_LEFT), a.data.y - a.data.hitbox.getRadius(ANGLE_UP)) ||
                !checkBounds(a.data.x + a.data.hitbox.getRadius(ANGLE_RIGHT), a.data.y + a.data.hitbox.getRadius(ANGLE_DOWN))) {
            a.data.updateData.velocity.setMagnitude(0);
            a.entityDie(model);
        }
    }

    @Override
    public void collisionHard(GameModel model, Entity a, Entity b) {
        // Both the client and server stops the arrow and removes it from the game.

        if (b.getHealth() != Double.POSITIVE_INFINITY)
            b.decreaseHealthBy(model, 5); // TODO CHANGE THIS
        a.data.updateData.velocity.setMagnitude(0);
        a.entityDie(model);
    }
}
