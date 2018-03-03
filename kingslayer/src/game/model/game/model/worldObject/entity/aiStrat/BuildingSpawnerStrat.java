package game.model.game.model.worldObject.entity.aiStrat;

import game.message.toServer.MakeEntityRequest;
import game.model.game.grid.GridCell;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.*;
import util.Util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.*;
import static java.lang.Math.*;

public abstract class BuildingSpawnerStrat extends AIStrat {

    public enum BuildingType {
        WALL,
        COLLECTOR,
        TOWER,
        EXPLORER,
        BARRACKS
    }

    public static class BarracksBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final BarracksBuildingSpawnerStrat SINGLETON = new BarracksBuildingSpawnerStrat();

        @Override
        boolean canAttack() { return false; }

        @Override
        double timeBetweenSpawns(Entity entity) {
            return 1;
        } // TODO CHANGE BACK

        @Override
        int maxActive(Entity entity) {
            return 10;
           /* switch (entity.<Integer>get(LEVEL)) {
                case 0:
                    return 15;
                case 1:
                    return 10;
                default:
                    return 3;
            }*/
        }

        @Override
        Entity makeEntity(double x, double y, Team team, Entity entity, ServerGameModel model) {
            switch (entity.<Integer>get(LEVEL)) {
                case 0:
                    return Minions.makeMeleeMinion(x, y, team, entity.get(LEVEL), entity);
                case 1:
                    return Minions.makeRangedMinion(x, y, team, entity.get(LEVEL), entity);
                default:
                    return Minions.makeExplorationMinion(x, y, team, entity.get(LEVEL), entity);
            }
        }
    }

    public static class RangedBarracksBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final RangedBarracksBuildingSpawnerStrat SINGLETON = new RangedBarracksBuildingSpawnerStrat();

        @Override
        boolean canAttack() { return false; }

        @Override
        double timeBetweenSpawns(Entity entity) {
            return 2.5;
        }

        @Override
        int maxActive(Entity entity) {
            return 10 + 5 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0);
        }

        @Override
        Entity makeEntity(double x, double y, Team team, Entity entity, ServerGameModel model) {
            Entity minion = Minions.makeRangedMinion(x, y, team, entity.get(LEVEL), entity);
         //   minion.set(HEALTH, 100.0 + 10 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0));
            return minion;
        }
    }

    public static class SiegeBarracksBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final SiegeBarracksBuildingSpawnerStrat SINGLETON = new SiegeBarracksBuildingSpawnerStrat();

        @Override
        boolean canAttack() { return false; }

        @Override
        double timeBetweenSpawns(Entity entity) {
            return 2.5;
        }

        @Override
        int maxActive(Entity entity) {
            return 10 + 5 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0);
        }

        @Override
        Entity makeEntity(double x, double y, Team team, Entity entity, ServerGameModel model) {
            Entity minion = Minions.makeSiegeMinion(x, y, team, entity.get(LEVEL), entity);
          //  minion.set(HEALTH, 100.0 + 10 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0));
            return minion;
        }
    }

    public static class ExplorationBarracksBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final ExplorationBarracksBuildingSpawnerStrat SINGLETON = new ExplorationBarracksBuildingSpawnerStrat();

        @Override
        boolean canAttack() { return false; }

        @Override
        double timeBetweenSpawns(Entity entity) {
            return 2.5;
        }

        @Override
        int maxActive(Entity entity) {
            return 10 + 5 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0);
        }

        @Override
        Entity makeEntity(double x, double y, Team team, Entity entity, ServerGameModel model) {
            Entity minion = Minions.makeExplorationMinion(x, y, team, entity.get(LEVEL), entity);
           // minion.set(HEALTH, 100.0 + 10 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0));
            return minion;
        }
    }

    public static class ResourceCollectorBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final ResourceCollectorBuildingSpawnerStrat SINGLETON = new ResourceCollectorBuildingSpawnerStrat();

        @Override
        boolean canAttack() { return false; }

        @Override
        double timeBetweenSpawns(Entity entity) {
            return 2.5;
        }

        @Override
        int maxActive(Entity entity) {
//            System.out.println("level: " + entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 10));
//            return 10 + 5 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0); // TODO change back later
            return 1;
        }

        @Override
        Entity makeEntity(double x, double y, Team team, Entity entity, ServerGameModel model) {

            Entity minion = Minions.makeResourceMinion(x, y, team, entity.get(Entity.EntityProperty.LEVEL), entity);
         //   minion.set(HEALTH, 100.0 + 10 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0));
            return minion;
        }
    }


    public static class TowerBuildingSpawnerStrat extends BuildingSpawnerStrat {

        public static final TowerBuildingSpawnerStrat SINGLETON = new TowerBuildingSpawnerStrat();

        @Override
        boolean canAttack() {
            return true;
        }

        @Override
        double timeBetweenSpawns(Entity entity) {
            return 1 - 0.02 * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0);
        }

        @Override
        int maxActive(Entity entity) {
            return 100;
        }

        @Override
        Entity makeEntity(double x, double y, Team team, Entity entity, ServerGameModel model) {
            double dir = Util.angle2Points(entity.getX(), entity.getY(), x, y);
            return Entities.makeArrow(entity.getX(), entity.getY(), dir, team, entity, 2.5, -1);
        }
    }

    abstract boolean canAttack();

    abstract double timeBetweenSpawns(Entity entity);

    abstract int maxActive(Entity entity);

    abstract Entity makeEntity(double x, double y, Team team, Entity entity, ServerGameModel model);

    /**
     * Returns all the enemy entities this arrow tower can attack. These are all the
     * enemies that are in the line of the sight of the tower within the attack
     * range.
     * @return all the enemy entities this tower can attack
     */
    Collection<Entity> attackableEnemies(Entity entity, ServerGameModel model) {
        // Minions shouldn't count enemies around a wall as attackable.
        // TODO ignore arrows

        Collection<Entity> enemies = new HashSet<>();
        double x = entity.getX();
        double y = entity.getY();
        double range = 5; // TODO change

        // Add enemies within the range of this entity and in the line of sight of this enemy.
        for (int i = (int) (x - range); i <= (int) (x + range); i++) {
            for (int j = (int) (y - range); j <= (int) (y + range); j++) {
                enemies.addAll(model.getCell(i, j).getContents().stream().filter(e ->
                        util.Util.dist(x, y, e.getX(), e.getY()) <= range
                                && e.has(Entity.EntityProperty.TEAM)
                                && e.getTeam() != entity.getTeam()
                                && !e.has(PROJECTILE)
                                && checkLineOfSight(entity, e, model)).collect(Collectors.toSet()));
            }
        }

        return enemies;
    }

    /**
     * Gets the closest enemy to this tower. First checks for any enemy
     * that this minion can detect. TODO
     * @param attackable attackable entities
     * @param entity this tower
     * @param model current model of the game
     * @return the closest enemy to this tower
     */
    Entity getClosestEnemy(Collection<Entity> attackable, Entity entity, ServerGameModel model) {
        return attackable.stream().min((e1, e2) ->
                Double.compare(util.Util.dist(e1.getX(), e1.getY(), entity.getX(), entity.getY()),
                        util.Util.dist(e2.getX(), e2.getY(), entity.getX(), entity.getY()))).get();
    }

    /**
     * Check if entity b is in the line of sight of entity a. Line of sight
     * would be a circle of a set radius around the entity not blocked by any
     * walls or hard objects. Entity b should be given as an enemy of entity
     * a. Return true if entity b is in the line of sight of entity a;
     * return false otherwise.
     * http://playtechs.blogspot.com/2007/03/raytracing-on-grid.html
     * @param a entity looking
     * @param b entity to check for line of sight
     * @return true if b is in the line of sight of a
     */
    boolean checkLineOfSight(Entity a, Entity b, ServerGameModel model) {
        // Get x and y coordinate of both entities.
        double ax = a.getX();
        double ay = a.getY();
        double bx = b.getX();
        double by = b.getY();

        // Find how far in each component the two entities are.
        double dy = Math.abs(by - ay);
        double dx = Math.abs(bx - ax);

        int x = (int) Math.floor(a.getX());
        int y = (int) Math.floor(a.getY());

        // Number of grid cells to check.
        int n = 0;

        // How much to increment x, y after each grid cell check. Should be -1, 0, or 1.
        int xinc, yinc;
        double error;

        if (dx == 0) {
            xinc = 0;
            error = Double.POSITIVE_INFINITY;
        } else if (bx > ax) {
            xinc = 1;
            n += (int) Math.floor(bx) - x;
            error = (Math.floor(ax) + 1 - ax) * dy;
        } else {
            xinc = -1;
            n += x - (int) Math.floor(bx);
            error = (ax - Math.floor(ax)) * dy;
        }

        if (dy == 0) {
            yinc = 0;
            error -= Double.POSITIVE_INFINITY;
        } else if (by > ay) {
            yinc = 1;
            n += (int) Math.floor(by) - y;
            error -= (Math.floor(ay) + 1 - ay) * dx;
        } else {
            yinc = -1;
            n += y - (int) Math.floor(by);
            error -= (ay - Math.floor(ay)) * dx;
        }

        while (true) {

            // Check if cell x, y has a wall.
            if (!a.containedIn.contains(model.getCell(x, y)) && !model.getCell(x, y).isPassable())
                return false;

            if (--n == 0)
                break;

            if (error > 0) {
                y += yinc;
                error -= dx;
            } else if (error < 0) {
                x += xinc;
                error += dy;
            } else {
                // Error = 0 case. Intersects the corner of a grid cell. Need to check
                // all of the 3 other corners.
                // May need to check this case.
                if (!model.getCell(x - 1, y - 1).isPassable() || !model.getCell(x - 1, y).isPassable() ||
                        !model.getCell(x, y - 1).isPassable())
                    return false;
                x += xinc;
                y += yinc;
                error += (dy - dx);
                n--;
            }

        }
        return true;
    }

    private BuildingSpawnerStrat() {

    }

    public static class BuildingSpawnerStratAIData extends AIData {
        double elapsedTime;
        int spawnCounter;
        HashSet<Entity> spawned;

        BuildingSpawnerStratAIData() {
            this.elapsedTime = 0;
            this.spawnCounter = 0;
            spawned = new HashSet<>();
        }
    }

    @Override
    public void init(Entity entity) {
//        Log.info("INIT AI " + entity);
        entity.add(AI_DATA, new BuildingSpawnerStratAIData());
    }

    @Override
    public void updateAI(Entity entity, ServerGameModel model, double seconds) {
//        Log.info("UPDATE AI " + entity);
        BuildingSpawnerStratAIData data = entity.<BuildingSpawnerStratAIData>get(AI_DATA);
        data.elapsedTime += seconds;
        while (data.elapsedTime > timeBetweenSpawns(entity)) {
            data.elapsedTime -= timeBetweenSpawns(entity);
            if (data.spawnCounter < maxActive(entity)) {

                if (!canAttack()) {

                    // Find possible spawn points: cells not blocked by hard entities.
                    Set<GridCell> neighbors = model.getNeighbors(model.getCell((int) (double) entity.getX(), (int) (double) entity.getY()));
                    Set<GridCell> passable = neighbors.stream().filter(GridCell::isPassable).collect(Collectors.toSet());

                    // If cell directly below it is clear, spawn it there. Otherwise, find another
                    // open neighboring cell to spawn the entity.
                    if (passable.contains(model.getCell((int) (double) entity.getX(), (int) (double) entity.getY() + 1))) {
                        Entity newEntity = makeEntity(entity.getX(), entity.getY() + 1, entity.getTeam(), entity, model);
                        model.processMessage(new MakeEntityRequest(newEntity));
                        newEntity.onDeath((e, serverGameModel) -> {
                            data.spawnCounter--;
                            data.spawned.remove(e);
                        });
                        data.spawnCounter++;
                        data.spawned.add(newEntity);
                    } else if (!passable.isEmpty()) {
                        Entity newEntity = makeEntity(passable.iterator().next().getCenterX(),
                                passable.iterator().next().getCenterY(), entity.getTeam(), entity, model);
                        model.processMessage(new MakeEntityRequest(newEntity));
                        newEntity.onDeath((e, serverGameModel) -> {
                            data.spawnCounter--;
                            data.spawned.remove(e);
                        });
                        data.spawnCounter++;
                        data.spawned.add(newEntity);
                    }
                } else {
                    Collection<Entity> attackable = attackableEnemies(entity, model);
                    if (!attackable.isEmpty()) {
                        Entity closest = getClosestEnemy(attackable, entity, model);
                        Entity newEntity = makeEntity(closest.getX(), closest.getY(), entity.getTeam(), entity, model);
                        model.processMessage(new MakeEntityRequest(newEntity));
                        newEntity.onDeath((e, serverGameModel) -> {
                            data.spawnCounter--;
                            data.spawned.remove(e);
                        });
                        data.spawnCounter++;
                        data.spawned.add(newEntity);
                    }
                }
            }
            if (entity.getUpgraded()) {
                for (Entity e : data.spawned) {
                    e.setUpgraded(false);
                    e.set(LEVEL, entity.get(LEVEL));
                    // TODO UP grade other stuff
                }
            }
        }
    }
}
