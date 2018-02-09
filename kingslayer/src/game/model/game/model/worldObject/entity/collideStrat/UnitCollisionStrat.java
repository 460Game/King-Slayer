package game.model.game.model.worldObject.entity.collideStrat;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import util.Util;

import static java.lang.Math.PI;

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

          /*  if() { //Hit right

            } else if() { //Hit

            } else if() {

            } else {

            }

            boolean hitleft = !(collisionAngle < -3*PI/4 || collisionAngle > 3*PI/4);
            boolean hittop = !(collisionAngle > -3*PI/4 && collisionAngle < -PI/4);

            // Hit from left/right.
            boolean hitVerticalWall = collisionAngle > -PI / 4 && collisionAngle < PI / 4 ||
                collisionAngle > 3 * PI / 4 || collisionAngle < -3 * PI / 4;

            // Hit from top/bottom.
            boolean hitHorizontalWall = collisionAngle < -PI / 4 && collisionAngle > -3 * PI / 4
                || collisionAngle > PI / 4 && collisionAngle < 3 * PI / 4;

            if (hitVerticalWall)
                a.data.x = b.data.x - (hitleft ? 1 : -1) * (0.5 + a.data.hitbox.getRadius(collisionAngle + PI/2));
            if (hitHorizontalWall)
                a.data.y = b.data.y - (hittop ? 1 : -1) * (0.5 + a.data.hitbox.getRadius(collisionAngle)); //TODO is this angle right? @ryan
   */
    }
}
