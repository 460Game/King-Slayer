package game.model.game.model.worldObject.entity.aiStrat;

import game.ai.Astar;
import game.message.toClient.SetEntityCommand;
import game.model.game.grid.GridCell;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.Entity;

import java.util.*;

public abstract class MinionStrat extends AIStrat {

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
        void handleEnemyDetected() {

        }

        @Override
        void handleEnemyAttackable() {

        }

        @Override
        void wander(MinionStratAIData data, Entity entity, ServerGameModel model) {
            // TODO make this work with only one initialized astar in the model.
            Astar astar = new Astar(model);
//            Astar astar = model.getAstar();

            Entity king = getClosestEnemy(data, entity, model);
            int entityx = (int) (double) entity.getX();
            int entityy = (int) (double) entity.getY();
            int x = (int) (double) model.getEntity(king.id).getX();
            int y = (int) (double) model.getEntity(king.id).getY();
//            int x = astar.getClosestWood(model.getCell(entityx, entityy)).getTopLeftX();
//            int y = astar.getClosestWood(model.getCell(entityx, entityy)).getTopLeftY();

            // Check if path exists and king has moved, then generate a new path.
            if (data.path.size() > 0 && data.path.get(data.path.size() - 1).getTopLeftX() != x &&
                    data.path.get(data.path.size() - 1).getTopLeftY() != y) {
                data.path.clear();
                data.path = astar.astar(model.getCell(entityx, entityy), model.getCell(x, y));
            }

            // If nothing in path and not at destination, generate a path.

            if (data.path.size() == 0 && entityx != x && entityy != y) {
                data.path = astar.astar(model.getCell(entityx, entityy), model.getCell(x, y));
            }

            // might need to check for empty path

            if (entityx == x && entityy == y)
                entity.setVelocity(entity.getVelocity().withMagnitude(0));
            else if (data.path.size() > 0) {
                if (entityx == data.path.get(0).getTopLeftX() && entityy == data.path.get(0).getTopLeftY())
                    data.path.remove(0);
                else {
                    // Keep moving if cells are in path.
                    astar.moveToCell(entity, data.path.get(0));
                    if (entity.getVelocity().getMagnitude() == 0)
                        entity.setVelocity(entity.getVelocity().withMagnitude(1));
                }
            }

            model.processMessage(new SetEntityCommand(entity));

//            Random rand = new Random();
//            if (rand.nextDouble() < 0.05)
//                model.processMessage(new NewEntityCommand(Entities.makeArrow(entity.getX(), entity.getY(),
//                        entity.<Velocity>get(Entity.EntityProperty.VELOCITY).getAngle(), entity.getTeam())));
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
        void handleEnemyDetected() {

        }

        @Override
        void handleEnemyAttackable() {

        }

        @Override
        void wander(MinionStratAIData data, Entity entity, ServerGameModel model) {

            // TODO make this work with only one initialized astar in the model.
            Astar astar = new Astar(model);
//            Astar astar = model.getAstar();

            Entity enemy = getClosestEnemy(data, entity, model);
            int entityx = (int) (double) entity.getX();
            int entityy = (int) (double) entity.getY();
            int x = (int) (double) model.getEntity(enemy.id).getX();
            int y = (int) (double) model.getEntity(enemy.id).getY();

            // Check if path exists and enemy has moved, then generate a new path.
            if (data.path.size() > 0 && data.path.get(data.path.size() - 1).getTopLeftX() != x &&
                    data.path.get(data.path.size() - 1).getTopLeftY() != y) {
                data.path.clear();
                data.path = astar.astar(model.getCell(entityx, entityy), model.getCell(x, y));
            }

            // If nothing in path and not at destination, generate a path.

            if (data.path.size() == 0 && entityx != x && entityy != y) {
                data.path = astar.astar(model.getCell(entityx, entityy), model.getCell(x, y));
            }

            // might need to check for empty path

            if (entityx == x && entityy == y)
                entity.setVelocity(entity.getVelocity().withMagnitude(0));
            else if (data.path.size() > 0) {
                if (entityx == data.path.get(0).getTopLeftX() && entityy == data.path.get(0).getTopLeftY())
                    data.path.remove(0);
                else {
                    // Keep moving if cells are in path.
                    astar.moveToCell(entity, data.path.get(0));
                    if (entity.getVelocity().getMagnitude() == 0)
                        entity.setVelocity(entity.getVelocity().withMagnitude(1));
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
        void handleEnemyDetected() {

        }

        @Override
        void handleEnemyAttackable() {

        }

        @Override
        void wander(MinionStratAIData data, Entity entity, ServerGameModel model) {

            // TODO make this work with only one initialized astar in the model.
            Astar astar = new Astar(model);
//            Astar astar = model.getAstar();

            Entity king = getClosestEnemy(data, entity, model);
            int entityx = (int) (double) entity.getX();
            int entityy = (int) (double) entity.getY();
            int x = (int) (double) model.getEntity(king.id).getX();
            int y = (int) (double) model.getEntity(king.id).getY();
//            int x = astar.getClosestWood(model.getCell(entityx, entityy)).getTopLeftX();
//            int y = astar.getClosestWood(model.getCell(entityx, entityy)).getTopLeftY();

            // Check if path exists and king has moved, then generate a new path.
            if (data.path.size() > 0 && data.path.get(data.path.size() - 1).getTopLeftX() != x &&
                    data.path.get(data.path.size() - 1).getTopLeftY() != y) {
                data.path.clear();
                data.path = astar.astar(model.getCell(entityx, entityy), model.getCell(x, y));
            }


            // If nothing in path and not at destination, generate a path.
            if (data.path.size() == 0 && entityx != x && entityy != y) {
                data.path = astar.astar(model.getCell(entityx, entityy), model.getCell(x, y));
            }

            // might need to check for empty path

            if (entityx == x && entityy == y)
                entity.setVelocity(entity.getVelocity().withMagnitude(0));
            else if (data.path.size() > 0) {
                if (entityx == data.path.get(0).getTopLeftX() && entityy == data.path.get(0).getTopLeftY())
                    data.path.remove(0);
                else {
                    // Keep moving if cells are in path.
                    astar.moveToCell(entity, data.path.get(0));
                    if (entity.getVelocity().getMagnitude() == 0)
                        entity.setVelocity(entity.getVelocity().withMagnitude(1));
                }
            }

            model.processMessage(new SetEntityCommand(entity));

            System.out.println(checkLineOfSight(entity, king, model));
//
//            Random rand = new Random();
//            if (rand.nextDouble() < 0.1)
//                model.changeResource(entity.getTeam(), TeamResourceData.Resource.WOOD, 1);
        }
    }

    public static class MinionStratAIData extends AIData {
        private List<GridCell> path;
        private Collection<Entity> detected;
        private Collection<Entity> attackable;

        MinionStratAIData() {
            path = new ArrayList<>();
            detected = new HashSet<>();
            attackable = new HashSet<>();
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
    abstract void handleEnemyDetected();
    // TODO attacking minions can chase closest enemy and collectors should run away

    /**
     * Handles the ation to perform when the minion detects
     * an attackable enemy.
     */
    abstract void handleEnemyAttackable();
    // TODO attacking minions should attack closest enemy and collectors should run away (shouldnt even get to this position)

    /**
     * Returns all the enemy entities this minion can detect. These are all the
     * enemies that are in the line of sight of the minion as far as the
     * detect range or around a solid entity with in a closer range.
     * @return the enemy entities that this minion can detect
     */
    Collection<Entity> detectedEnemies(Entity entity, ServerGameModel model) {
        // TODO look at all enemies within the detect Range
        // If there are any in LOS within detect range or around a wall with a closer range (maybe)
        // put that in the collection of entities that minion can detect.
        // ignore arrows or try to dodge
        Collection<Entity> enemies = new HashSet<>();

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
        else
            return new HashSet<>();
        // TODO look at all enemies within attack range
        // IF there are any in LOS within attack range, put in collection of entities that minion can detect.
        // Minions shouldn't count enemies around a wall as attackable.
        // ignore arrows
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

        // TODO find the closest enemy in detected range or location of last enemy?

        // Returns the enemy king, assuming no fog of war. Could return closest king. Temporary for now? TODO
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
        double ax = a.getX();
        double ay = a.getY();
        double bx = b.getX();
        double by = b.getY();

        double dy = Math.abs(by - ay);
        double dx = Math.abs(bx - ax);

        int x = (int) Math.floor(a.getX());
        int y = (int) Math.floor(a.getY());

        int n = 1;
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

            // check for los here on x,y
            if (!model.getCell(x, y).isPassable())
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
                // error == 0 case
                if (!model.getCell(x - 1, y - 1).isPassable())
                    return false;
                x += xinc;
                y += yinc;
                error += (dy - dx);
                n--;
                // need to check if goes directly between 2 impassable cells
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
    abstract void wander(MinionStratAIData data, Entity entity, ServerGameModel model);

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
            handleEnemyAttackable();
            return;
        }

        // Next, scan for any enemies in the area. If there are enemies in the range,
        // perform the appropriate action.
        data.detected = detectedEnemies(entity, model);
        if (data.detected.size() > 0) {
            handleEnemyDetected();
            return;
        }

        // If the minion has no other tasks to do, attack or handle detected enemies, it
        // should "wander." This is either moving on a set path to perform a task or
        // actually wandering so it can perform a task.
        wander(data, entity, model);
    }
}
