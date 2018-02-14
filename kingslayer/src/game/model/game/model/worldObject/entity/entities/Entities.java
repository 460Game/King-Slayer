package game.model.game.model.worldObject.entity.entities;

import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.aiStrat.AIDoNothingStrat;
import game.model.game.model.worldObject.entity.collideStrat.ArrowGhostCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.GhostCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.HardCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.TreasureGhostCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CellHitbox;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CircleHitbox;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.Hitbox;
import game.model.game.model.worldObject.entity.drawStrat.*;
import game.model.game.model.worldObject.entity.updateStrat.MovingStrat;
import game.model.game.model.worldObject.entity.updateStrat.StillStrat;

public class Entities {

    public static Entity makeBlocker(double x, double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            NoDrawStrat.SINGLETON,
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeTreasure(double x, double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            TreasureGhostCollisionStrat.SINGLETON,
            new CircleHitbox(0.3),
            ImageDrawStrat.TREASURE_IMAGE_DRAW_STRAT,
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeTree(double x, double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            ImageDrawStrat.TREE_IMAGE_DRAW_STRAT,
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeStone(double x, double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            ImageDrawStrat.STONE_IMAGE_DRAW_STRAT,
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeMetal(double x, double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            ImageDrawStrat.METAL_IMAGE_DRAW_STRAT,
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeWall(double x, double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            ImageDrawStrat.WALL_IMAGE_DRAW_STRAT,
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeBox(double x, double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            ImageDrawStrat.BOX_IMAGE_DRAW_STRAT,
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeBuiltWall(double x, double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            ImageDrawStrat.WALL_BUILDABLE_IMAGE_DRAW_STRAT,
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeGhostWall(double x, double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            GhostCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            GhostDrawStrat.GHOSTWALL,//ImageDrawStrat.WALL_BUILDABLE_IMAGE_DRAW_STRAT,//GhostDrawStrat.GHOSTWALL,
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeResourceCollectorRed(double x, double y) {
        return new Entity(x, y,
            Team.ONE,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            UpgradableImageDrawStrat.RED_RESOURCE_COLLECTOR_IMAGE_DRAW_STRAT,
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeResourceCollectorRedGhost(double x, double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            GhostCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            GhostDrawStrat.GHOST_RED_COLLECTOR,
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeResourceCollectorBlue(double x, double y) {
        return new Entity(x, y,
            Team.TWO,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            UpgradableImageDrawStrat.BLUE_RESOURCE_COLLECTOR_IMAGE_DRAW_STRAT,
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeResourceCollectorBlueGhost(double x, double y) {
        return new Entity(x, y,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            GhostCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            GhostDrawStrat.GHOST_BLUE_COLLECTOR,
            AIDoNothingStrat.SINGLETON);
    }

    static Hitbox ARROW_HITBOX =  new CircleHitbox(0.2);
    public static Entity makeArrow(double x, double y, double angle) {
        Entity arrow = new Entity(x, y,
                Team.NEUTRAL,
                Role.NEUTRAL,
                MovingStrat.SINGLETON,
                ArrowGhostCollisionStrat.SINGLETON,
            ARROW_HITBOX,
            RotatingImageDrawStrat.ARROW_IMAGE_DRAW_STRAT,
//                ImageDrawStrat.ARROW_IMAGE_DRAW_STRAT,
                AIDoNothingStrat.SINGLETON);
        arrow.data.updateData.maxSpeed = 7;
        arrow.data.updateData.velocity.setMagnitude(7);
        arrow.data.updateData.velocity.setAngle(angle);
        return arrow;
    }
}
