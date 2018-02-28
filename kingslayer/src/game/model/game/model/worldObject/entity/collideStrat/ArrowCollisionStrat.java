package game.model.game.model.worldObject.entity.collideStrat;

import game.message.toClient.SetEntityCommand;
import game.model.game.model.*;
import game.model.game.model.worldObject.entity.Entity;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.TEAM;
import static util.Util.checkBoundsForArrows;
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
        // appropriate amount of health. All servers draw entity b's health.
        // As a precaution, on the client side, stop the arrow and remove it from the game.
        if (a.has(TEAM) && b.has(TEAM) && a.get(TEAM) != b.get(TEAM)) {
            model.execute((server) -> {
                a.setVelocity(a.getVelocity().withMagnitude(0));
                a.entityDie(server);
                b.decreaseHealthBy(model, 5);
                server.processMessage(new SetEntityCommand(b));
            }, (client) -> {
                a.setVelocity(a.getVelocity().withMagnitude(0));
                a.entityDie(client);
            });
        }
    }

    @Override
    public void collisionWater(GameModel model, Entity a, Entity b) {
        // Normally an arrow should go over water fine. This check only stops the arrow
        // from going out of the bounds of the map and causing an error.
        if (!checkBoundsForArrows(a.getX() - a.getHitbox().getRadius(ANGLE_LEFT), a.getY() - a.getHitbox().getRadius(ANGLE_UP)) ||
                !checkBoundsForArrows(a.getX() + a.getHitbox().getRadius(ANGLE_RIGHT), a.getY() + a.getHitbox().getRadius(ANGLE_DOWN))) {
            a.setVelocity(a.getVelocity().withMagnitude(0));
            a.entityDie(model);
        }
    }

    @Override
    public void collisionHard(GameModel model, Entity a, Entity b) {
        // Both the client and server stops the arrow and removes it from the game.
        if (a.has(TEAM) && b.has(TEAM) && a.get(TEAM) != b.get(TEAM)) {
            if (b.getHealth() != Double.POSITIVE_INFINITY)
                b.decreaseHealthBy(model, 5); // TODO CHANGE THIS
        }
        a.setVelocity(a.getVelocity().withMagnitude(0));
        a.entityDie(model);
    }
}
