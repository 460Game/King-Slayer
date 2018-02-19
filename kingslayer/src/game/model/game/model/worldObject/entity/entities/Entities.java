package game.model.game.model.worldObject.entity.entities;

import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.aiStrat.AIDoNothingStrat;
import game.model.game.model.worldObject.entity.aiStrat.BuildingSpawnerStrat;
import game.model.game.model.worldObject.entity.collideStrat.*;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CellHitbox;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CircleHitbox;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.Hitbox;
import game.model.game.model.worldObject.entity.deathStrat.NopDeathStrat;
import game.model.game.model.worldObject.entity.deathStrat.RemoveOnDeathStrat;
import game.model.game.model.worldObject.entity.drawStrat.*;
import game.model.game.model.worldObject.entity.updateStrat.MovingStrat;
import game.model.game.model.worldObject.entity.updateStrat.StillStrat;

public class Entities {

    public static Entity makeWater(double x, double y) {
        return new Entity(x, y, Double.POSITIVE_INFINITY,
                Team.NEUTRAL,
                Role.NEUTRAL,
                StillStrat.SINGLETON,
                WaterCollisionStrat.SINGLETON,
                CellHitbox.SINGLETON,
                NoDrawStrat.SINGLETON,
                AIDoNothingStrat.SINGLETON,
                NopDeathStrat.SINGLETON);
    }

    public static Entity makeTreasure(double x, double y) {
        return new Entity(x, y, Double.POSITIVE_INFINITY,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            TreasureGhostCollisionStrat.SINGLETON,
            new CircleHitbox(0.3),
            ImageDrawStrat.TREASURE_IMAGE_DRAW_STRAT,
            AIDoNothingStrat.SINGLETON,
                NopDeathStrat.SINGLETON);
    }

    public static Entity makeTree(double x, double y) {
        return new Entity(x, y, Double.POSITIVE_INFINITY,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            ImageDrawStrat.TREE_IMAGE_DRAW_STRAT,
            AIDoNothingStrat.SINGLETON,
                NopDeathStrat.SINGLETON); // TODO death
    }

    public static Entity makeStone(double x, double y) {
        return new Entity(x, y, Double.POSITIVE_INFINITY,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            ImageDrawStrat.STONE_IMAGE_DRAW_STRAT,
            AIDoNothingStrat.SINGLETON,
                NopDeathStrat.SINGLETON); // TODO death
    }

    public static Entity makeMetal(double x, double y) {
        return new Entity(x, y, Double.POSITIVE_INFINITY,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            ImageDrawStrat.METAL_IMAGE_DRAW_STRAT,
            AIDoNothingStrat.SINGLETON,
                NopDeathStrat.SINGLETON); // TODO death
    }

    public static Entity makeWall(double x, double y) {
        return new Entity(x, y, Double.POSITIVE_INFINITY,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            ImageDrawStrat.WALL_IMAGE_DRAW_STRAT,
            AIDoNothingStrat.SINGLETON,
                NopDeathStrat.SINGLETON);
    }

    public static Entity makeBox(double x, double y) {
        return new Entity(x, y, Double.POSITIVE_INFINITY,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            ImageDrawStrat.BOX_IMAGE_DRAW_STRAT,
            AIDoNothingStrat.SINGLETON,
                NopDeathStrat.SINGLETON);
    }

    public static Entity makeBuiltWall(double x, double y) {
        return new Entity(x, y, 100,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            UpgradableImageDrawStrat.BUILDABLE_WOOD_WALL,
            AIDoNothingStrat.SINGLETON,
                RemoveOnDeathStrat.SINGLETON);
    }

    public static Entity makeGhostWall(double x, double y) {
        return new Entity(x, y, Double.POSITIVE_INFINITY,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            GhostCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            GhostDrawStrat.GHOSTWALL,//ImageDrawStrat.WALL_BUILDABLE_IMAGE_DRAW_STRAT,//GhostDrawStrat.GHOSTWALL,
            AIDoNothingStrat.SINGLETON,
                NopDeathStrat.SINGLETON);
    }

    public static Entity makeResourceCollectorRed(double x, double y) {
        return new Entity(x, y, 100,
            Team.ONE,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            UpgradableImageDrawStrat.RED_WOOD_RESOURCE_COLLECTOR,
            AIDoNothingStrat.SINGLETON,
                RemoveOnDeathStrat.SINGLETON); // TODO DEATH
    }

    public static Entity makeResourceCollectorRedGhost(double x, double y) {
        return new Entity(x, y, Double.POSITIVE_INFINITY,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            GhostCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            GhostDrawStrat.GHOST_RED_COLLECTOR,
            AIDoNothingStrat.SINGLETON,
                NopDeathStrat.SINGLETON);
    }

    public static Entity makeResourceCollectorBlue(double x, double y) {
        return new Entity(x, y, 100,
            Team.TWO,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            UpgradableImageDrawStrat.BLUE_WOOD_RESOURCE_COLLECTOR,
            AIDoNothingStrat.SINGLETON,
                RemoveOnDeathStrat.SINGLETON); // TODO death
    }

    public static Entity makeResourceCollectorBlueGhost(double x, double y) {
        return new Entity(x, y, Double.POSITIVE_INFINITY,
            Team.NEUTRAL,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            GhostCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            GhostDrawStrat.GHOST_BLUE_COLLECTOR,
            AIDoNothingStrat.SINGLETON,
                NopDeathStrat.SINGLETON);
    }

    public static Entity makeRedBarracks(double x, double y) {
        return new Entity(x, y, 100,
            Team.ONE,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            UpgradableImageDrawStrat.RED_WOOD_RANGED_BARRACKS,
            BuildingSpawnerStrat.RangedBarracksBuildingSpawnerStrat.SINGLETON,
                RemoveOnDeathStrat.SINGLETON);
    }

    public static Entity makeRedBarracksGhost(double x, double y) {
        return new Entity(x, y, 100,
            Team.ONE,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            GhostCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            UpgradableImageDrawStrat.RED_WOOD_RANGED_BARRACKS,
            BuildingSpawnerStrat.RangedBarracksBuildingSpawnerStrat.SINGLETON,
            RemoveOnDeathStrat.SINGLETON);
    }

    public static Entity makeBlueBarracks(double x, double y) {
        return new Entity(x, y, 100,
            Team.TWO,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
                HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            UpgradableImageDrawStrat.BLUE_WOOD_RANGED_BARRACKS,
            BuildingSpawnerStrat.RangedBarracksBuildingSpawnerStrat.SINGLETON,
                RemoveOnDeathStrat.SINGLETON);
    }
    public static Entity makeBlueBarracksGhost(double x, double y) {
        return new Entity(x, y, 100,
            Team.TWO,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            GhostCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            UpgradableImageDrawStrat.BLUE_WOOD_RANGED_BARRACKS,
            BuildingSpawnerStrat.RangedBarracksBuildingSpawnerStrat.SINGLETON,
            RemoveOnDeathStrat.SINGLETON);
    }

    public static Entity makeRedArrowTower(double x, double y) {
        return new Entity(x, y, 100,
            Team.ONE,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
                HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            UpgradableImageDrawStrat.RED_WOOD_ARROW_TOWER,
                BuildingSpawnerStrat.TowerBuildingSpawnerStrat.SINGLETON,
                RemoveOnDeathStrat.SINGLETON);
    }

    public static Entity makeRedArrowTowerGhost(double x, double y) {
        return new Entity(x, y, 100,
            Team.ONE,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            GhostCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            UpgradableImageDrawStrat.RED_WOOD_ARROW_TOWER,
            BuildingSpawnerStrat.TowerBuildingSpawnerStrat.SINGLETON,
            RemoveOnDeathStrat.SINGLETON);
    }

    public static Entity makeBlueArrowTower(double x, double y) {
        return new Entity(x, y, 100,
            Team.TWO,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
                HardCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            UpgradableImageDrawStrat.BLUE_WOOD_ARROW_TOWER,
                BuildingSpawnerStrat.TowerBuildingSpawnerStrat.SINGLETON,
                RemoveOnDeathStrat.SINGLETON);
    }

    public static Entity makeBlueArrowTowerGhost(double x, double y) {
        return new Entity(x, y, 100,
            Team.TWO,
            Role.NEUTRAL,
            StillStrat.SINGLETON,
            GhostCollisionStrat.SINGLETON,
            CellHitbox.SINGLETON,
            UpgradableImageDrawStrat.BLUE_WOOD_ARROW_TOWER,
            BuildingSpawnerStrat.TowerBuildingSpawnerStrat.SINGLETON,
            RemoveOnDeathStrat.SINGLETON);
    }

    private static final Hitbox ARROW_HITBOX = new CircleHitbox(0.2);

    public static Entity makeArrow(double x, double y, double angle) {
        Entity arrow = new Entity(x, y, Double.POSITIVE_INFINITY,
                Team.NEUTRAL,
                Role.NEUTRAL,
                MovingStrat.SINGLETON,
                ArrowCollisionStrat.SINGLETON,
            ARROW_HITBOX,
            RotatingImageDrawStrat.ARROW_IMAGE_DRAW_STRAT,
//                ImageDrawStrat.ARROW_IMAGE_DRAW_STRAT,
                AIDoNothingStrat.SINGLETON,
                RemoveOnDeathStrat.SINGLETON);
        arrow.data.updateData.maxSpeed = 7;
        arrow.data.updateData.velocity.setMagnitude(arrow.data.updateData.maxSpeed);
        arrow.data.updateData.velocity.setAngle(angle);
        return arrow;
    }
}
