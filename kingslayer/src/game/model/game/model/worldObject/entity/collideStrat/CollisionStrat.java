package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.collideStrat.collideData.CollisionData;

public abstract class CollisionStrat {


    public abstract CollisionData initCollisionData();

    public abstract void collision(GameModel model, Entity b);

    public static enum CollideType{
        HARD, //Blocks cells, can not be pushed or overlap (like a building), does not do any collision resolution logic itself
        //it is an error if two hard objects overlap
        SOFT, //Does not block cells, can be pushed (like a minion or hero)
        GHOST; //Does not block cells, does not effect movement normaly (like an arrow) - will collide with other things, but things do not collide with it
    };

    public abstract CollideType getCollideType();

}
