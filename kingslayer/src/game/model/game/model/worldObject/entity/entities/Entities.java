package game.model.game.model.worldObject.entity.entities;

import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.aiStrat.BuildingSpawnerStrat;
import game.model.game.model.worldObject.entity.collideStrat.*;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CellHitbox;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CircleHitbox;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.Hitbox;
import game.model.game.model.worldObject.entity.deathStrat.RemoveOnDeathStrat;
import game.model.game.model.worldObject.entity.drawStrat.*;
import game.model.game.model.worldObject.entity.updateStrat.MovingStrat;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.*;
import static util.Pair.pair;

public class Entities {

    public static Entity makeWater(double x, double y) {
        return new Entity(x, y, CellHitbox.SINGLETON,
                WaterCollisionStrat.SINGLETON
        );
    }

    public static Entity makeTreasure(double x, double y) {
        return new Entity(x, y, new CircleHitbox(0.3),
                TreasureGhostCollisionStrat.SINGLETON,
                pair(DRAW_STRAT, ImageDrawStrat.TREASURE_IMAGE_DRAW_STRAT));
    }

    public static Entity makeTree(double x, double y) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(DRAW_STRAT, ImageDrawStrat.TREE_IMAGE_DRAW_STRAT)); // TODO death
    }

    public static Entity makeStone(double x, double y) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(DRAW_STRAT, ImageDrawStrat.STONE_IMAGE_DRAW_STRAT)); // TODO death
    }

    public static Entity makeMetal(double x, double y) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(DRAW_STRAT, ImageDrawStrat.METAL_IMAGE_DRAW_STRAT)); // TODO death
    }

    public static Entity makeWall(double x, double y) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(DRAW_STRAT, ImageDrawStrat.WALL_IMAGE_DRAW_STRAT));
    }

    public static Entity makeBox(double x, double y) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(DRAW_STRAT, ImageDrawStrat.BOX_IMAGE_DRAW_STRAT));
    }

    public static Entity makeBuiltWall(double x, double y) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(HEALTH, 100.0),
                pair(DRAW_STRAT, UpgradableImageDrawStrat.BUILDABLE_WOOD_WALL),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
    }

    public static Entity makeGhostWall(double x, double y) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                GhostCollisionStrat.SINGLETON,
                pair(DRAW_STRAT, GhostDrawStrat.GHOSTWALL));//ImageDrawStrat.WALL_BUILDABLE_IMAGE_DRAW_STRAT,//GhostDrawStrat.GHOSTWALL);
    }

    public static Entity makeResourceCollector(double x, double y, Team team) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(HEALTH, 100.0),
                pair(TEAM, team),
                pair(DRAW_STRAT, UpgradableImageDrawStrat.WOOD_RESOURCE_COLLECTOR),
                pair(AI_STRAT, BuildingSpawnerStrat.ResourceCollectorBuildingSpawnerStrat.SINGLETON),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
    }

    public static Entity makeResourceCollectorGhost(double x, double y, Team team) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                GhostCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(DRAW_STRAT, GhostDrawStrat.GHOST_COLLECTOR));
    }

    public static Entity makeMeleeBarracks(double x, double y, Team team) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(HEALTH, 100.0),
                pair(DRAW_STRAT, UpgradableImageDrawStrat.WOOD_BARRACKS),
                pair(AI_STRAT, BuildingSpawnerStrat.MeleeBarracksBuildingSpawnerStrat.SINGLETON),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
    }

    public static Entity makeRangedBarracks(double x, double y, Team team) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(HEALTH, 100.0),
                pair(DRAW_STRAT, UpgradableImageDrawStrat.WOOD_BARRACKS),
                pair(AI_STRAT, BuildingSpawnerStrat.RangedBarracksBuildingSpawnerStrat.SINGLETON),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
    }

    public static Entity makeSiegeBarracks(double x, double y, Team team) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(HEALTH, 100.0),
                pair(DRAW_STRAT, UpgradableImageDrawStrat.WOOD_BARRACKS),
                pair(AI_STRAT, BuildingSpawnerStrat.SiegeBarracksBuildingSpawnerStrat.SINGLETON),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
    }

    public static Entity makeBarracksGhost(double x, double y, Team team) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                GhostCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(DRAW_STRAT, GhostDrawStrat.GHOST_BARRACKS),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
    }

    public static Entity makeArrowTower(double x, double y, Team team) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(HEALTH, 100.0),
                pair(DRAW_STRAT, UpgradableImageDrawStrat.WOOD_ARROW_TOWER),
                pair(AI_STRAT, BuildingSpawnerStrat.TowerBuildingSpawnerStrat.SINGLETON),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
    }

    public static Entity makeArrowTowerGhost(double x, double y, Team team) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                GhostCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(DRAW_STRAT, UpgradableImageDrawStrat.WOOD_ARROW_TOWER),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
    }

    private static final Hitbox ARROW_HITBOX = new CircleHitbox(0.2);

    public static Entity makeArrow(double x, double y, double angle, Team team) {
        Entity arrow = new Entity(x, y,
                ARROW_HITBOX,
                ArrowCollisionStrat.SINGLETON,
                pair(UPDATE_STRAT, MovingStrat.SINGLETON),
                pair(TEAM, team),
                pair(DRAW_STRAT, RotatingImageDrawStrat.ARROW_IMAGE_DRAW_STRAT),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
        arrow.setVelocity(arrow.getVelocity().withMagnitude(7).withAngle(angle));
        return arrow;
    }
}
