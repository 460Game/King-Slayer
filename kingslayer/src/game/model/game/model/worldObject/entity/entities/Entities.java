package game.model.game.model.worldObject.entity.entities;

import game.model.game.model.worldObject.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.aiStrat.AIDoNothingStrat;
import game.model.game.model.worldObject.entity.collideStrat.HardCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.SoftCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CellHitbox;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.Hitbox;
import game.model.game.model.worldObject.entity.drawStrat.ImageDrawStrat;
import game.model.game.model.worldObject.entity.drawStrat.NoDrawStrat;
import game.model.game.model.worldObject.entity.updateStrat.StillStrat;
import images.Images;

public class Entities {


    public static Entity makeBlocker(double x, double y) {
        return new Entity(x, y, Team.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.make(),
            CellHitbox.SINGLETON,
            NoDrawStrat.SINGLETON,
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeMetal(Double x, Double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.make(),
            CellHitbox.SINGLETON,
            ImageDrawStrat.make(Images.METAL_IMAGES[0]),
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeStone(Double x, Double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.make(),
            CellHitbox.SINGLETON,
            ImageDrawStrat.make(Images.STONE_IMAGES[0]),
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeTree(Double x, Double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.make(),
            CellHitbox.SINGLETON,
            ImageDrawStrat.make(Images.TREE_IMAGES[0]),
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeWall(Double x, Double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.make(),
            CellHitbox.SINGLETON,
            ImageDrawStrat.make(Images.WALL_IMAGE),
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeBox(Double x, Double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.make(),
            CellHitbox.SINGLETON,
            ImageDrawStrat.make(Images.BOX_IMAGE),
            AIDoNothingStrat.SINGLETON);
    }
}
