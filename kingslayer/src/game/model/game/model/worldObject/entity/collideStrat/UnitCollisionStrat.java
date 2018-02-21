package game.model.game.model.worldObject.entity.collideStrat;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.GameModel;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import util.Const;
import util.Util;

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
        if (a.team != b.team && !(a.team == Team.NEUTRAL || b.team == Team.NEUTRAL)) {
            a.decreaseHealthBy(model, 0.2 * NANOS_TO_SECONDS * a.timeDelta);
            b.decreaseHealthBy(model, 0.2 * NANOS_TO_SECONDS * b.timeDelta);
        }
    }

    @Override
    protected void collisionHard(GameModel model, Entity a, Entity b) {

        // Check side of collision based on angle between entity and collided object.
        double collisionAngle = Util.angle2Points(a.data.x, a.data.y, b.data.x, b.data.y);
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
            a.data.x = b.data.x - (hitleft ? 1 : -1) * (0.5 + a.data.hitbox.getRadius(collisionAngle + PI/2));
        if (hitHorizontalWall)
            a.data.y = b.data.y - (hittop ? 1 : -1) * (0.5 + a.data.hitbox.getRadius(collisionAngle)); //TODO is this angle right? @ryan

//        System.out.println("New Player x: " + a.data.x);
//        System.out.println("New Player y: " + a.data.y);
//        a.update(model);
    }
}
