package game.model.game.model.worldObject.entity.collideStrat;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.GameModel;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import util.Const;
import util.Util;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.TEAM;
import static java.lang.Math.PI;
import static util.Const.*;

/**
 * Defines how a unit collides with other entities in the world.
 */
public class UnitCollisionStrat extends SoftCollisionStrat {

    /**
     * Only one instance of a unit collision strategy is created. All units
     * use this strategy to collide.
     */
    public static final UnitCollisionStrat SINGLETON = new UnitCollisionStrat();

    @Override
    public void collisionSoft(GameModel model, Entity a, Entity b) {
        //TODO should push away from other entity
        double collisionAngle = Util.angle2Points(a.getX(), a.getY(), b.getX(), b.getY());
        a.translateX(-1.5 * Math.cos(collisionAngle) * a.timeDelta);
        a.translateY(-1.5 * Math.sin(collisionAngle) * a.timeDelta);

        if (a.has(TEAM) && b.has(TEAM) && a.get(TEAM) != b.get(TEAM)) {
            a.decreaseHealthBy(model,  a.timeDelta);
            b.decreaseHealthBy(model, b.timeDelta);
        }
    }

    @Override
    protected void collisionHard(GameModel model, Entity a, Entity b) {

        // Check side of collision based on angle between entity and collided object.
        double collisionAngle = Util.angle2Points(a.getX(), a.getY(), b.getX(), b.getY());
        //  Log.info("" + (collisionAngle/ Math.PI));

        // Get booleans about where entity a hit entity b.
        boolean hitleft = collisionAngle > ANGLE_UP_RIGHT && collisionAngle < ANGLE_DOWN_RIGHT;
        boolean hitright = collisionAngle < ANGLE_UP_LEFT || collisionAngle > ANGLE_DOWN_LEFT;
        boolean hittop = collisionAngle > ANGLE_DOWN_RIGHT && collisionAngle < ANGLE_DOWN_LEFT;
        boolean hitbottom = collisionAngle > ANGLE_UP_LEFT && collisionAngle < ANGLE_UP_RIGHT;

        boolean hitVerticalWall = hitleft || hitright;
        boolean hitHorizontalWall = hittop || hitbottom;
//        System.out.println("COLLIDING....");
//        System.out.println("Collision angle: " + collisionAngle);
//        System.out.println("Hit left: " + hitleft);
//        System.out.println("Hit right: " + hitright);
//        System.out.println("Hit top: " + hittop);
//        System.out.println("Hit bottom: " + hitbottom);

//        System.out.println("Player x: " + a.data.x);
//        System.out.println("Player y: " + a.data.y);
//        System.out.println("X: " + b.data.x);
//        System.out.println("Y: "+ b.data.y);

        // Move the unit to the right spot depending on which side of the entity it hits.
        if (hitVerticalWall)
            a.setX(b.getX() - (hitleft ? 1 : -1) * (0.5 + a.getHitbox().getRadius(collisionAngle + PI/2)));
        else if (hitHorizontalWall)
            a.setY(b.getY() - (hittop ? 1 : -1) * (0.5 + a.getHitbox().getRadius(collisionAngle))); //TODO is this angle right? @ryan
//        else {
//            a.data.x = b.data.x - Math.cos(collisionAngle) * (a.data.hitbox.getRadius(collisionAngle) + 0.5 * Math.sqrt(2));
//            a.data.y = b.data.y - Math.sin(collisionAngle) * (a.data.hitbox.getRadius(collisionAngle) + 0.5 * Math.sqrt(2));
//        }

//        System.out.println("New Player x: " + a.data.x);
//        System.out.println("New Player y: " + a.data.y);
//        a.draw(model);
    }
}
