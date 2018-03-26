package game.model.game.model.worldObject.entity.aiStrat;

import com.esotericsoftware.minlog.Log;
import game.ai.Astar;
import game.message.toClient.SetEntityCommand;
import game.message.toServer.MakeEntityRequest;
import game.model.game.grid.GridCell;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Entities;
import util.Const;
import util.Util;

import java.util.*;
import java.util.stream.Collectors;

public abstract class MinionStrat extends AIStrat {

    // Moving target, update only every few frames

    private MinionStrat() {

    }

    // TODO balance these ranges

    public static class RangedMinionStrat extends MinionStrat {

        public static final RangedMinionStrat SINGLETON = new RangedMinionStrat();

        @Override
        double attackRange() {
            return 2.0;
        }

        @Override
        double detectRange() {
            return 5.0;
        }

        @Override
        void handleEnemyDetected(MinionStratAIData data, Entity entity, ServerGameModel model, double seconds) {

        }

        @Override
        void handleEnemyAttackable(MinionStratAIData data, Entity entity, ServerGameModel model, double seconds) {
            waitCounter += seconds;
            data.path.clear();
            data.nextDestination = null;
            entity.setVelocity(entity.getVelocity().withMagnitude(0));
            if (waitCounter >= 1) {
                Entity enemy = getClosestEnemy(data, entity, model);
                double dir = Util.angle2Points(entity.getX(), entity.getY(), enemy.getX(), enemy.getY());
                model.processMessage(new MakeEntityRequest(Entities.makeArrow(entity.getX(), entity.getY(), dir, entity.getTeam(), entity, 1, -1)));
                waitCounter = 0;
            }
        }

        private double waitCounter = 2;

        @Override
        void wander(MinionStratAIData data, Entity entity, ServerGameModel model, double seconds) {

            Astar astar = new Astar(model);
//            Astar astar = model.getAstar();

            // Get current position.
            double entityx = entity.getX();
            double entityy = entity.getY();

            // Get destination.
            Entity king = getClosestEnemy(data, entity, model);
            int x = (int) (double) model.getEntity(king.id).getX();
            int y = (int) (double) model.getEntity(king.id).getY();

            // Check if path exists and king has moved, then generate a new path.
            if (data.path.size() > 0 && !king.containedIn.contains(data.path.get(data.path.size() - 1))) {
                data.path.clear();
                data.path = astar.astar(model.getCell((int) entityx, (int) entityy), model.getCell(x, y));
            }

            // If nothing in path and not at destination, generate a path. TODO might need better check
            if (data.path.size() == 0 && !entity.checkCollision(king.getHitbox(), king.getX(), king.getY())) {//entityx != x && entityy != y) {
                data.path = astar.astar(model.getCell((int) entityx, (int) entityy), model.getCell(x, y));
            }

            if (data.nextDestination != null && !data.nextDestination.isPassable() && !data.path.isEmpty() && data.path.get(0).getTopLeftX() != x &&
                    data.path.get(0).getTopLeftY() != y) {
                data.path.clear();
                data.path = astar.astar(model.getCell((int) entityx, (int) entityy), model.getCell(x, y));
            }

            // Check if reached destination.
            if (entity.containedIn.contains(model.getCell(x, y))) {
                entity.setVelocity(entity.getVelocity().withMagnitude(0));
                data.path.clear();
                data.nextDestination = null;
            } else if (!data.path.isEmpty()) {
                if ((int) entityx == data.path.get(0).getTopLeftX() && (int) entityy == data.path.get(0).getTopLeftY()) {
                    data.path.remove(0);
                    data.nextDestination = null;
                }
                else {
                    // Keep moving if cells are in path.
                    data.nextDestination = data.path.get(0);
                    astar.moveToCell(entity, data.nextDestination);
                    if (entity.getVelocity().getMagnitude() == 0)
                        entity.setVelocity(entity.getVelocity().withMagnitude(entity.get(Entity.EntityProperty.MAX_SPEED)));
                }
            }

            model.processMessage(new SetEntityCommand(entity));
        }
    }

    public static class MeleeMinionStrat extends MinionStrat {

        public static final MeleeMinionStrat SINGLETON = new MeleeMinionStrat();

        @Override
        double attackRange() {
            return 0.25;
        }

        @Override
        double detectRange() {
            return 5.0;
        }

        @Override
        void handleEnemyDetected(MinionStratAIData data, Entity entity, ServerGameModel model, double seconds) {
            wander(data, entity, model, seconds);
        }

        @Override
        void handleEnemyAttackable(MinionStratAIData data, Entity entity, ServerGameModel model, double seconds) {
            wander(data, entity, model, seconds); // TODO TEMP
        }

        @Override
        void wander(MinionStratAIData data, Entity entity, ServerGameModel model, double seconds) {

            // TODO figure out why model.getAstar doesnt work
//            Astar astar = model.getAstar();
            Astar astar = new Astar(model);

            // Get current position.
            double entityx = entity.getX();
            double entityy = entity.getY();

            int destx = 0;
            int desty = 0;

            Team team = entity.getTeam();
            TeamResourceData teamData = model.getTeamData(team);

            if (teamData.getEnemyKingInSight()) {
                // If team sees enemy king, all minions should go to enemy king.
                Entity king = getClosestKing(entity, model);
                destx = (int) (double) king.getX();
                desty = (int) (double) king.getY();

                // Check if path exists and king has moved, generate a new path.
                if (data.path.size() > 0 && !king.containedIn.contains(data.path.get(data.path.size() - 1))) {
                    data.path.clear();
                    data.path = astar.astar(model.getCell((int) entityx, (int) entityy), model.getCell(destx, desty));
                }

                // If nothing in path and not at destination, generate a path. TODO might need better check
                if (data.path.size() == 0 && !entity.checkCollision(king.getHitbox(), king.getX(), king.getY())) {//entityx != x && entityy != y) {
                    data.path = astar.astar(model.getCell((int) entityx, (int) entityy), model.getCell(destx, desty));
                }

                if (data.nextDestination != null && !data.nextDestination.isPassable() && data.path.get(0).getTopLeftX() != destx &&
                        data.path.get(0).getTopLeftY() != desty) {
                    data.path.clear();
                    data.path = astar.astar(model.getCell((int) entityx, (int) entityy), model.getCell(destx, desty));
                }

                // Check if reached destination.
                if (entity.containedIn.contains(model.getCell(destx, desty))) {
                    entity.setVelocity(entity.getVelocity().withMagnitude(0));
                    data.path.clear();
                    data.nextDestination = null;
                } else if (!data.path.isEmpty()) {
                    if ((int) entityx == data.path.get(0).getTopLeftX() && (int) entityy == data.path.get(0).getTopLeftY()) {
                        data.path.remove(0);
                        data.nextDestination = null;
                    } else {
                        // Keep moving if cells are in path.
                        data.nextDestination = data.path.get(0);
                        astar.moveToCell(entity, data.nextDestination);
                        if (entity.getVelocity().getMagnitude() == 0)
                            entity.setVelocity(entity.getVelocity().withMagnitude(entity.get(Entity.EntityProperty.MAX_SPEED)));
                    }
                }
            } else {
                if (data.reachedDestination) {
                    GridCell dest = model.getNextCell(entity.getTeam().team);
                    while (!dest.isPassable() || !astar.isReachable(model.getCell((int) entityx, (int) entityy), dest))
                        dest = model.getNextCell(entity.getTeam().team);


                    destx = dest.getTopLeftX();
                    desty = dest.getTopLeftY();
                    data.finalDestination = dest;
                    data.reachedDestination = false;
                }

                if (data.path.size() == 0 && !data.reachedDestination) {//entityx != destx && entityy != desty) {
                    data.path = astar.astar(model.getCell((int) entityx, (int) entityy), data.finalDestination);
                }

                if (data.nextDestination != null && !data.nextDestination.isPassable() && data.path.get(0).getTopLeftX() != destx &&
                        data.path.get(0).getTopLeftY() != desty) {
                    data.path.clear();
                    data.path = astar.astar(model.getCell((int) entityx, (int) entityy), data.finalDestination);
                    data.reachedDestination = false;
                }

                if (data.finalDestination.getContents().contains(entity))
                    data.reachedDestination = true;

                // Check if reached destination.
                if (data.reachedDestination) {
                    entity.setVelocity(entity.getVelocity().withMagnitude(0));
                    data.path.clear();
                    data.nextDestination = null;
                    data.finalDestination = null;
                } else if (!data.path.isEmpty()) {
                    if ((int) entityx == data.path.get(0).getTopLeftX() && (int) entityy == data.path.get(0).getTopLeftY()) {
                        data.path.remove(0);
                        data.nextDestination = null;
                    } else {
                        // Keep moving if cells are in path.
                        data.nextDestination = data.path.get(0);
                        astar.moveToCell(entity, data.nextDestination);
                        if (entity.getVelocity().getMagnitude() == 0)
                            entity.setVelocity(entity.getVelocity().withMagnitude(entity.get(Entity.EntityProperty.MAX_SPEED)));
                    }
                }
            }

            model.processMessage(new SetEntityCommand(entity));
        }
    }

    public static class ResourceMinionStrat extends MinionStrat {

        public static final ResourceMinionStrat SINGLETON = new ResourceMinionStrat();

        @Override
        double attackRange() {
            return -1;
        }

        @Override
        double detectRange() {
            return 5.0;
        }

        @Override
        void handleEnemyDetected(MinionStratAIData data, Entity entity, ServerGameModel model, double seconds) {
            // Clear current path and run away
            wander(data, entity, model, seconds); // TODO TEMP
        }

        @Override
        void handleEnemyAttackable(MinionStratAIData data, Entity entity, ServerGameModel model, double seconds) {
            handleEnemyDetected(data, entity, model, seconds);
        }

        private double waitCounter = -1; // TODO think of better way to do the waiting

        @Override
        void wander(MinionStratAIData data, Entity entity, ServerGameModel model, double seconds) {

            if (waitCounter >= 3)
                waitCounter = -1;
            if (waitCounter >= 0) {
                waitCounter += seconds;
                return;
            }
//            Astar astar = model.getAstar();
            Astar astar = new Astar(model);

            // Get current position.
            double entityx = entity.getX();
            double entityy = entity.getY();

            // Holds the final destination.
            int x, y;

            // Check if the minion should go to a resource or back to a collector.
            if (!data.hasResource) {
                entity.set(Entity.EntityProperty.MAX_SPEED, 1.0);
                if ((int) entity.get(Entity.EntityProperty.LEVEL) == 0) {
                    GridCell wood = astar.getClosestWood(model.getCell((int) entityx, (int) entityy));
                    if (wood == null)
                        return;
                    x = wood.getTopLeftX();
                    y = wood.getTopLeftY();
                    data.resourceType = 0;
                } else if ((int) entity.get(Entity.EntityProperty.LEVEL) == 1) {
                    GridCell stone = astar.getClosestStone(model.getCell((int) entityx, (int) entityy));
                    if (stone == null)
                        return;
                    x = stone.getTopLeftX();
                    y = stone.getTopLeftY();
                    data.resourceType = 1;
                } else {
                    GridCell metal = astar.getClosestMetal(model.getCell((int) entityx, (int) entityy));
                    if (metal == null)
                        return;
                    x = metal.getTopLeftX();
                    y = metal.getTopLeftY();
                    data.resourceType = 2;
                }
            } else {
                entity.set(Entity.EntityProperty.MAX_SPEED, 0.5);
                GridCell collector = astar.getClosestCollector(model.getCell((int) entityx, (int) entityy), entity.getTeam());
                if (collector == null) {
                    entity.setVelocity(entity.getVelocity().withMagnitude(0));
                    data.path.clear();
                    model.processMessage(new SetEntityCommand(entity));
                    return;
                }
                x = collector.getTopLeftX();
                y = collector.getTopLeftY();
            }

            // Check if path exists and resource disappeared, then generate a new path.
            if (data.path.size() > 0 && (data.path.get(data.path.size() - 1).getTopLeftX() != x ||
                    data.path.get(data.path.size() - 1).getTopLeftY() != y)) {
                data.path.clear();
                data.path = astar.astar(model.getCell((int) entityx, (int) entityy), model.getCell(x, y));
            }

            // If nothing in path and not at destination, generate a path. TODO might need better check
            if (data.path.size() == 0 && entityx != x && entityy != y) {
                data.path = astar.astar(model.getCell((int) entityx, (int) entityy), model.getCell(x, y));
            }

            if (data.nextDestination != null && !data.nextDestination.isPassable() && data.path.get(0).getTopLeftX() != x &&
                    data.path.get(0).getTopLeftY() != y) {
                data.path.clear();
                data.path = astar.astar(model.getCell((int) entityx, (int) entityy), model.getCell(x, y));
            }

            // Check if reached destination.
            if (entity.containedIn.contains(model.getCell(x, y))) {
                waitCounter = 2;
                // Stop movement and clear path.
                entity.setVelocity(entity.getVelocity().withMagnitude(0));
                data.path.clear();
                data.nextDestination = null;

                // Update resource counts if applicable, and change path destination.
                data.hasResource = !data.hasResource;
                if (data.hasResource) {
                    waitCounter = 0;
                    Entity res = model.getEntitiesAt(x, y).stream().filter(e ->
                            e.has(Entity.EntityProperty.RESOURCE_AMOUNT)).findFirst().get();
                    if ((int) entity.get(Entity.EntityProperty.LEVEL) == 0)
                        data.resourceHeld += Math.min(Const.FIRST_LEVEL_WOOD_COLLECTED, res.get(Entity.EntityProperty.RESOURCE_AMOUNT));
                    else if ((int) entity.get(Entity.EntityProperty.LEVEL) == 1)
                        data.resourceHeld += Math.min(Const.SECOND_LEVEL_STONE_COLLECTED, res.get(Entity.EntityProperty.RESOURCE_AMOUNT));
                    else
                        data.resourceHeld += Math.min(Const.THIRD_LEVEL_METAL_COLLECTED, res.get(Entity.EntityProperty.RESOURCE_AMOUNT));
                    res.decreaseResourceAmount(model, data.resourceHeld);
                } else {
                    if (data.resourceType == 0)
                        model.changeResource(entity.getTeam(), TeamResourceData.Resource.WOOD, data.resourceHeld);
                    else if (data.resourceType == 1)
                        model.changeResource(entity.getTeam(), TeamResourceData.Resource.STONE, data.resourceHeld);
                    else
                        model.changeResource(entity.getTeam(), TeamResourceData.Resource.METAL, data.resourceHeld);
                    data.resourceHeld = 0;
                }
            } else if (data.path.size() > 0) {
                if ((int) entityx == data.path.get(0).getTopLeftX() && (int) entityy == data.path.get(0).getTopLeftY()) {
                    data.path.remove(0);
                    data.nextDestination = null;
                }
                else {
                    // Keep moving if cells are in path.
                    data.nextDestination = data.path.get(0);
                    astar.moveToCell(entity, data.nextDestination);
                    if (entity.getVelocity().getMagnitude() == 0)
                        entity.setVelocity(entity.getVelocity().withMagnitude(entity.get(Entity.EntityProperty.MAX_SPEED)));
                }
            }

            model.processMessage(new SetEntityCommand(entity));
        }
    }

    public static class MinionStratAIData extends AIData {
        private List<GridCell> path;
        private Collection<Entity> detected;
        private Collection<Entity> attackable;
        private boolean hasResource;
        private int resourceType;
        private int resourceHeld;
        private GridCell nextDestination;
        private GridCell finalDestination;
        private boolean reachedDestination;
        public boolean foundKing;

        MinionStratAIData() {
            path = new ArrayList<>();
            detected = new HashSet<>();
            attackable = new HashSet<>();
            hasResource = false;
            resourceType = 0;
            resourceHeld = 0;
            nextDestination = null;
            finalDestination = null;
            foundKing = false;
            reachedDestination = true;
        }
    }

    /**
     * Furthest distance that a minion is able to attack an
     * enemy. This distance is -1 if the minion cannot attack.
     * @return furthest distance a minion can attack
     */
    abstract double attackRange();

    /**
     * Furthest range that a minion is able to detect an enemy.
     * @return furthest distance a minion can detect an enemy
     */
    abstract double detectRange();

    /**
     * Handles the action to perform when the minion detects
     * an enemy.
     */
    abstract void handleEnemyDetected(MinionStratAIData data, Entity entity, ServerGameModel model, double seconds);
    // TODO attacking minions can chase closest enemy and collectors should run away

    /**
     * Handles the action to perform when the minion detects
     * an attackable enemy.
     */
    abstract void handleEnemyAttackable(MinionStratAIData data, Entity entity, ServerGameModel model, double seconds);
    // TODO attacking minions should attack closest enemy and collectors should run away (shouldnt even get to this position)

    /**
     * Returns all the enemy entities this minion can detect. These are all the
     * enemies that are in the line of sight of the minion as far as the
     * detect range or around a solid entity with in a closer range.
     * @return the enemy entities that this minion can detect
     */
    Collection<Entity> detectedEnemies(MinionStratAIData data, Entity entity, ServerGameModel model) {
        // TODO If there are any around a wall with a closer range (maybe)
        // TODO ignore arrows or try to dodge

        Collection<Entity> enemies = new HashSet<>();
        double x = entity.getX();
        double y = entity.getY();
        double range = ((MinionStrat) entity.get(Entity.EntityProperty.AI_STRAT)).detectRange();

        // Add enemies within the range of this entity and in the line of sight of this enemy.
        for (int i = (int) (x - range); i <= (int) (x + range); i++) {
            for (int j = (int) (y - range); j <= (int) (y + range); j++) {
                enemies.addAll(model.getCell(i, j).getContents().stream().filter(e ->
                        util.Util.dist(x, y, e.getX(), e.getY()) <= range
                                && e.has(Entity.EntityProperty.TEAM)
                                && e.getTeam() != entity.getTeam()
                                && checkLineOfSight(entity, e, model)).collect(Collectors.toSet()));
            }
        }

        if (enemies.parallelStream().filter(e -> e.has(Entity.EntityProperty.ROLE) &&
                e.<Role>get(Entity.EntityProperty.ROLE) == Role.KING).count() > 0) {
            model.getTeamData(entity.getTeam()).setEnemyKingInSight(true);
            data.foundKing = true;
        }

        return enemies;
    }

    /**
     * Returns all the enemy entities this minion can attack. These are all the
     * enemies that are in the line of the sight of the minion within the attack
     * range. If a minion does not attack, this should return an empty collection.
     * @return all the enemy entities this minion can attack
     */
    Collection<Entity> attackableEnemies(Entity entity, ServerGameModel model) {
        if (attackRange() == -1)
            return new HashSet<>();

        // Minions shouldn't count enemies around a wall as attackable.

        Collection<Entity> enemies = new HashSet<>();
        double x = entity.getX();
        double y = entity.getY();
        double range = ((MinionStrat) entity.get(Entity.EntityProperty.AI_STRAT)).attackRange();

        // Add enemies within the range of this entity and in the line of sight of this enemy.
        for (int i = (int) (x - range); i <= (int) (x + range); i++) {
            for (int j = (int) (y - range); j <= (int) (y + range); j++) {
                if (Util.checkBounds(i, j))
                    enemies.addAll(model.getCell(i, j).getContents().stream().filter(e ->
                        util.Util.dist(x, y, e.getX(), e.getY()) <= range
                                && e.has(Entity.EntityProperty.TEAM)
                                && e.getTeam() != entity.getTeam()
                                && !e.has(Entity.EntityProperty.PROJECTILE)
                                && checkLineOfSight(entity, e, model)).collect(Collectors.toSet()));
            }
        }

        return enemies;
    }

    /**
     * Gets the closest enemy to this minion. First checks for any enemy
     * that this minion can detect. If none are found, find the location
     * of the last sighted enemy (or closest sighted enemy?). TODO
     * @param data minion data
     * @param entity this minion
     * @param model current model of the game
     * @return the closest enemy to this minion
     */
    Entity getClosestEnemy(MinionStratAIData data, Entity entity, ServerGameModel model) {
        if (!data.attackable.isEmpty()) {
            return data.attackable.stream().min((e1, e2) ->
                    Double.compare(util.Util.dist(e1.getX(), e1.getY(), entity.getX(), entity.getY()),
                    util.Util.dist(e2.getX(), e2.getY(), entity.getX(), entity.getY()))).get();
        } else if (!data.detected.isEmpty()) {
            return data.detected.stream().min((e1, e2) ->
                    Double.compare(util.Util.dist(e1.getX(), e1.getY(), entity.getX(), entity.getY()),
                            util.Util.dist(e2.getX(), e2.getY(), entity.getX(), entity.getY()))).get();
        } else {
            // TODO location of last enemy seen?
            // Returns the enemy king, assuming no fog of war. Could return closest king. Temporary for now? TODO
            return model.getAllEntities().parallelStream().filter(e -> e.has(Entity.EntityProperty.TEAM) && e.getTeam() != entity.getTeam() &&
                    e.has(Entity.EntityProperty.ROLE) && e.<Role>get(Entity.EntityProperty.ROLE) == Role.KING).findFirst().get();
        }
    }

    /**
     * Returns the closest enemy king to this entity.
     * @param entity entity for which to ind closest enemy king
     * @param model current state of game on server
     * @return closest enemy king to this entity
     */
    Entity getClosestKing(Entity entity, ServerGameModel model) {
        // Returns the closest enemy king.
        return model.getAllEntities().parallelStream().filter(e -> e.has(Entity.EntityProperty.TEAM) && e.getTeam() != entity.getTeam() &&
                e.has(Entity.EntityProperty.ROLE) && e.<Role>get(Entity.EntityProperty.ROLE) == Role.KING).findFirst().get();
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
        int n = 1;

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
            if (!b.containedIn.contains(model.getCell(x, y)) && !model.getCell(x, y).isPassable())
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

    // Look at for fov/los: http://www.adammil.net/blog/v125_Roguelike_Vision_Algorithms.html

    /**
     * Makes the minion wander around. This is performed as a last resort, if there are no
     * enemies detected or attackable. Wandering will depend on the state of the game (fog of
     * war, last seen enemy) and the tasks that the minion is assigned to do.
     * @param entity this minion
     * @param model current model of the game
     */
    abstract void wander(MinionStratAIData data, Entity entity, ServerGameModel model, double seconds);

    @Override
    public void init(Entity entity) {
        entity.add(Entity.EntityProperty.AI_DATA, new MinionStratAIData());
    }

    @Override
    public void updateAI(Entity entity, ServerGameModel model, double seconds) {

        MinionStratAIData data = entity.get(Entity.EntityProperty.AI_DATA);

        // First, scan for any attackable enemies if the minion can attack.
        // If there are any attackable enemies, perform the appropriate action.
        data.attackable = attackableEnemies(entity, model);
        if (data.attackable.size() > 0) {
            handleEnemyAttackable(data, entity, model, seconds);
            return;
        }
//
//        // Next, scan for any enemies in the area. If there are enemies in the range,
//        // perform the appropriate action.
        data.detected = detectedEnemies(data, entity, model);
        if (data.detected.size() > 0) {
            handleEnemyDetected(data, entity, model, seconds);
            return;
        }

        // If the minion has no other tasks to do, attack or handle detected enemies, it
        // should "wander." This is either moving on a set path to perform a task or
        // actually wandering so it can perform a task.
        wander(data, entity, model, seconds);
    }
}
