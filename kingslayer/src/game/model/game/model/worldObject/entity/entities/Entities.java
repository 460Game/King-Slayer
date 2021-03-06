package game.model.game.model.worldObject.entity.entities;

import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
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
import static util.Const.*;
import static util.Pair.pair;

public class Entities {

    public static Entity makeWater(double x, double y) {
        return new Entity(x, y, CellHitbox.SINGLETON,
                WaterCollisionStrat.SINGLETON);
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
                pair(RESOURCE_TYPE, TeamResourceData.Resource.WOOD),
                pair(RESOURCE_AMOUNT, STARTING_WOOD),
                pair(DRAW_STRAT, ImageDrawStrat.TREE_IMAGE_DRAW_STRAT),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
    }

    public static Entity makeStone(double x, double y) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(RESOURCE_TYPE, TeamResourceData.Resource.STONE),
                pair(RESOURCE_AMOUNT, STARTING_STONE),
                pair(DRAW_STRAT, ImageDrawStrat.STONE_IMAGE_DRAW_STRAT),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
    }

    public static Entity makeMetal(double x, double y) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(RESOURCE_TYPE, TeamResourceData.Resource.METAL),
                pair(RESOURCE_AMOUNT, STARTING_METAL),
                pair(DRAW_STRAT, ImageDrawStrat.METAL_IMAGE_DRAW_STRAT),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
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

    public static Entity makeBuiltWall(double x, double y, Team team) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(HEALTH, 50.0),
                pair(DRAW_STRAT, UpgradableImageDrawStrat.BUILDABLE_WOOD_WALL),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON),
                pair(TEAM, team),
                pair(LEVEL, 0),
                pair(MAX_HEALTH, 50.0),
                pair(BUILDING_TYPE, BuildingSpawnerStrat.BuildingType.WALL),
                pair(Entity.EntityProperty.SIGHT_RADIUS, 2));
    }

    public static Entity makeGhostWall(double x, double y) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                GhostCollisionStrat.SINGLETON,
                pair(DRAW_STRAT, GhostDrawStrat.GHOSTWALL),
                pair(SIGHT_RADIUS, 2));//ImageDrawStrat.WALL_BUILDABLE_IMAGE_DRAW_STRAT,//GhostDrawStrat.GHOSTWALL);
    }

    public static Entity makeResourceCollector(double x, double y, Team team) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(HEALTH, 25.0),
                pair(TEAM, team),
                pair(BUILDING_TYPE, BuildingSpawnerStrat.BuildingType.COLLECTOR),
                pair(DRAW_STRAT, UpgradableImageDrawStrat.RESOURCE_COLLECTOR),
                pair(AI_STRAT, BuildingSpawnerStrat.ResourceCollectorBuildingSpawnerStrat.SINGLETON),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON),
                pair(LEVEL, 0),
                pair(MAX_HEALTH, 25.0),
                pair(SIGHT_RADIUS, 2));
    }

    public static Entity makeResourceCollectorGhost(double x, double y, Team team) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                GhostCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(DRAW_STRAT, GhostDrawStrat.GHOST_COLLECTOR));
    }

    public static Entity makeBarracks(double x, double y, Team team) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(HEALTH, 25.0),
                pair(BUILDING_TYPE, BuildingSpawnerStrat.BuildingType.BARRACKS),
                pair(DRAW_STRAT, UpgradableImageDrawStrat.BARRACKS),
                pair(AI_STRAT, BuildingSpawnerStrat.BarracksBuildingSpawnerStrat.SINGLETON),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON),
                pair(LEVEL, 0),
                pair(MAX_HEALTH, 25.0),
                pair(SIGHT_RADIUS, 2));
    }

    public static Entity makeRangedBarracks(double x, double y, Team team) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(HEALTH, 25.0),
                pair(BUILDING_TYPE, BuildingSpawnerStrat.BuildingType.BARRACKS),
                pair(DRAW_STRAT, UpgradableImageDrawStrat.BARRACKS),
                pair(AI_STRAT, BuildingSpawnerStrat.RangedBarracksBuildingSpawnerStrat.SINGLETON),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON),
                pair(LEVEL, 0),
                pair(MAX_HEALTH, 25.0),
                pair(SIGHT_RADIUS, 2));
    }

    public static Entity makeSiegeBarracks(double x, double y, Team team) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                HardCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(HEALTH, 15.0),
                pair(BUILDING_TYPE, BuildingSpawnerStrat.BuildingType.BARRACKS),
                pair(DRAW_STRAT, UpgradableImageDrawStrat.BARRACKS),
                pair(AI_STRAT, BuildingSpawnerStrat.SiegeBarracksBuildingSpawnerStrat.SINGLETON),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON),
                pair(LEVEL, 0),
                pair(MAX_HEALTH, 15.0),
                pair(SIGHT_RADIUS, 2));
    }

    public static Entity makeExplorationBarracks(double x, double y, Team team) {
        return new Entity(x, y,
            CellHitbox.SINGLETON,
            HardCollisionStrat.SINGLETON,
            pair(TEAM, team),
            pair(HEALTH, 15.0),
            pair(BUILDING_TYPE, BuildingSpawnerStrat.BuildingType.EXPLORER),
            pair(DRAW_STRAT, UpgradableImageDrawStrat.BARRACKS),
            pair(AI_STRAT, BuildingSpawnerStrat.ExplorationBarracksBuildingSpawnerStrat.SINGLETON),
            pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON),
            pair(LEVEL, 0),
                pair(MAX_HEALTH, 15.0),
                pair(SIGHT_RADIUS, 2));
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
                pair(HEALTH, 50.0),
                pair(BUILDING_TYPE, BuildingSpawnerStrat.BuildingType.TOWER),
                pair(DRAW_STRAT, UpgradableImageDrawStrat.ARROW_TOWER),
                pair(AI_STRAT, BuildingSpawnerStrat.TowerBuildingSpawnerStrat.SINGLETON),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON),
                pair(LEVEL, 0),
                pair(MAX_HEALTH, 50.0),
                pair(SIGHT_RADIUS, 4));
    }

    public static Entity makeArrowTowerGhost(double x, double y, Team team) {
        return new Entity(x, y,
                CellHitbox.SINGLETON,
                GhostCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(DRAW_STRAT, GhostDrawStrat.GHOST_ARROW_TOWER),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
    }

    private static final Hitbox ARROW_HITBOX = new CircleHitbox(0.2);

    public static Entity makeArrow(double x, double y, double angle, Team team, Entity spawner, double damage, int level) {
        Entity arrow = new Entity(x, y,
                ARROW_HITBOX,
                ArrowCollisionStrat.SINGLETON,
                pair(UPDATE_STRAT, MovingStrat.SINGLETON),
                pair(LEVEL, 0),
                pair(DAMAGE_DEALT, damage),
                pair(TEAM, team),
                pair(SPAWNED_ID, spawner.id),
                pair(PROJECTILE, ProjectileCollisionStrat.Projectile.ARROW),
                pair(DRAW_STRAT, RotatingImageDrawStrat.ARROW_IMAGE_DRAW_STRAT),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
        arrow.setVelocity(arrow.getVelocity().withMagnitude(7).withAngle(angle));
        return arrow;
    }
}
