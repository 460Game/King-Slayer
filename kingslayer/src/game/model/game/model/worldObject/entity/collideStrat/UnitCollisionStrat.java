package game.model.game.model.worldObject.entity.collideStrat;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import util.Const;
import util.Util;

import static java.lang.Math.PI;
import static util.Const.*;

public class UnitCollisionStrat extends SoftCollisionStrat {

    public static final UnitCollisionStrat SINGLETON = new UnitCollisionStrat();

    @Override
    public void collisionSoft(GameModel model, Entity a, Entity b) {
        //TODO should push away from other entity
    }

    @Override
    protected void collisionHard(GameModel model, Entity a, Entity b) {

        // Check side of collision based on angle between entity and collided object.
        double collisionAngle = Util.angle2Points(a.data.x, a.data.y, b.data.x, b.data.y);
        //  Log.info("" + (collisionAngle/ Math.PI));

//           if() { //Hit right
//
//            } else if() { //Hit
//
//            } else if() {
//
//            } else {
//
//            }


//        boolean hitleft = !(collisionAngle < -3*PI/4 || collisionAngle > 3*PI/4);
//        boolean hittop = !(collisionAngle > -3*PI/4 && collisionAngle < -PI/4);

        boolean hitleft = collisionAngle > ANGLE_UP_RIGHT && collisionAngle < ANGLE_DOWN_RIGHT;
        boolean hitright = collisionAngle < ANGLE_UP_LEFT || collisionAngle > ANGLE_DOWN_LEFT;
        boolean hittop = collisionAngle > ANGLE_DOWN_RIGHT && collisionAngle < ANGLE_DOWN_LEFT;
        boolean hitbottom = collisionAngle > ANGLE_UP_LEFT && collisionAngle < ANGLE_UP_RIGHT;

        // Hit from left/right.
//        boolean hitVerticalWall = collisionAngle > -PI / 4 && collisionAngle < PI / 4 ||
//                collisionAngle > 3 * PI / 4 || collisionAngle < -3 * PI / 4;
//
//        // Hit from top/bottom.
//        boolean hitHorizontalWall = collisionAngle < -PI / 4 && collisionAngle > -3 * PI / 4
//                || collisionAngle > PI / 4 && collisionAngle < 3 * PI / 4;

        boolean hitVerticalWall = hitleft || hitright;
        boolean hitHorizontalWall = hittop || hitbottom;
//        System.out.println("COLLIDING....");
//        System.out.println("Collision angle: " + collisionAngle);
//        System.out.println("Hit left: " + hitleft);
//        System.out.println("Hit right: " + hitright);
//        System.out.println("Hit top: " + hittop);
//        System.out.println("Hit bottom: " + hitbottom);

        if (hitVerticalWall)
            a.data.x = b.data.x - (hitleft ? 1 : -1) * (0.5 + a.data.hitbox.getRadius(collisionAngle + PI/2));
        if (hitHorizontalWall)
            a.data.y = b.data.y - (hittop ? 1 : -1) * (0.5 + a.data.hitbox.getRadius(collisionAngle)); //TODO is this angle right? @ryan

//        System.out.println("X: " + b.data.x);
//        System.out.println("Y: "+ b.data.y);
//        a.update(model);
    }
}
