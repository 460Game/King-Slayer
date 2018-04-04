package game.model.game.map;

import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Entities;
import game.model.game.model.worldObject.entity.entities.Players;
import util.Util;
import util.Loc;

import java.util.*;
import java.util.function.BiFunction;

/**
 * map generator for the server side of the game.
 */
public class ServerMapGenerator implements MapGenerator {

    private final int mapW;
    private final int mapH;
    private final int distMax;
    private final int FEATURE_SIZE = 4;
    private final int edgeWidth = 4;
    private final int NUM_RIVER = 4;

    final static int NUM_STARTS_ROOM = 2;
    final static int NUM_METAL_ROOM = 2;
    final static int NUM_STONE_ROOM = 8;
    final static int NUM_TRESURE_ROOM = 4;

    private Random random;
    private TS[][] grid;

    private List<Loc> startingLocations;

    public ServerMapGenerator(int mapW, int mapH) {
        this.mapW = mapW;
        this.mapH = mapH;
        this.distMax = (int) (0.3 * Math.sqrt(mapW * mapW + mapH * mapH));

        this.grid = new TS[this.mapW][this.mapH];

        for (int i = 0; i < this.mapW; i++)
            for (int j = 0; j < this.mapH; j++)
                grid[i][j] = TS.unset;

        makeMap();
    }

    @Override
    public Tile makeTile(int i, int j) {
        return grid[i][j].make();
    }

    @Override
    public Collection<Entity> makeStartingEntities() {
        Set<Entity> entities = new HashSet<>();
        for (int i = 0; i < this.mapW; i++)
            for (int j = 0; j < this.mapH; j++)
                grid[i][j].makeE(i, j).ifPresent(entities::add);
        return entities;
    }

    public List<Loc> getStartingLocations() {
        return startingLocations;
    }

    public static enum TS {
        river(Tile.DEEP_WATER, Entities::makeWater),
        edgeWater(Tile.DEEP_WATER, Entities::makeWater),
        treasure(Tile.PATH, Entities::makeTreasure),
        metal(Tile.GRASS_0, Entities::makeMetal),
        stone(Tile.GRASS_0, Entities::makeStone),
        tree(Tile.GRASS_0, Entities::makeTree),
        wall(Tile.GRASS_0, Entities::makeWall),
        room(Tile.GRASS_0, null),
        grass0(Tile.GRASS_0, null),
        grass1(Tile.GRASS_1, null),
        grass2(Tile.GRASS_2, null),
        barrier(Tile.GRASS_0, Entities::makeBox),
        unset(null, null),
        startKingA(Tile.PATH, (x, y) -> Players.makeKing(x, y, Team.RED_TEAM)),
        startSlayerA(Tile.PATH, (x, y) -> Players.makeSlayer(x, y, Team.RED_TEAM)),
        startKingB(Tile.PATH, (x, y) -> Players.makeKing(x, y, Team.BLUE_TEAM)),
        startSlayerB(Tile.PATH, (x, y) -> Players.makeSlayer(x, y, Team.BLUE_TEAM)),
        bridge(Tile.SHALLOW_WATER, null),
        road(Tile.PATH, null);

        private Tile make;
        private BiFunction<Double, Double, Entity> spawner;

        TS(Tile make, BiFunction<Double, Double, Entity> spawner) {
            this.make = make;
            this.spawner = spawner;
        }

        public Tile make() {
            return this.make;
        }

        public Optional<Entity> makeE(int i, int j) {
            if (spawner == null)
                return Optional.empty();
            Entity e = spawner.apply(i + 0.5, j + 0.5);
            return Optional.of(e);
        }
    }

    public void makeMap() {
        makeMap((new Random()).nextLong());
   //     makeMap(Long.parseLong("-2142394214886197830")); // Good for path testing.
//        makeMap(Long.parseLong("-5713126425086333025"));
//        makeMap(Long.parseLong("-1609539064927447349"));
//        makeMap(Long.parseLong("6736756290173747940"));
        //   makeMap(Long.parseLong("-4733834012032569948"));
        // makeMap(Long.parseLong("8539555192000551887");       // FUNNNY
    }

    public void makeMap(long seed) {
        System.out.println("Generating gameMap with seed " + seed);
        this.random = new Random(seed);

        //Pick

        double[][] w = new double[this.mapW][mapH];

        /*
        fill the w matrix
        w matrix is the weight of each tile - higher w means more liekly to be picked for feature

        also fills the edges with edge water
         */
        for (int i = 0; i < this.mapW; i++)
            for (int j = 0; j < mapH; j++)
                if (i >= edgeWidth && i < this.mapW - edgeWidth && j >= edgeWidth && j < mapH - edgeWidth) {
                    w[i][j] = 1 - 0.8 * 2 * Util.dist(this.mapW / 2, mapH / 2, i, j) / distMax;
                } else {
                    w[i][j] = 0;
                    grid[i][j] = TS.edgeWater;
                }

        //Add river

        for (int i = 0; i < NUM_RIVER; i++) {
            int river_start_y = random.nextInt(mapH);
            int river_start_x = 0;

            int river_end_x;
            int river_end_y;

            if (random.nextDouble() < 0.3333) {
                river_end_x = mapW - 1;
                river_end_y = random.nextInt(mapH);
            } else if (random.nextBoolean()) {
                river_end_y = 0;
                river_end_x = random.nextInt(this.mapW);
            } else {
                river_end_y = mapH - 1;
                river_end_x = random.nextInt(this.mapW);
            }

            if (random.nextDouble() < 0.5) {
                river_start_x = this.mapW - river_start_x - 1;
                river_start_y = mapH - river_start_y - 1;
                river_end_x = this.mapW - river_end_x - 1;
                river_start_y = mapH - river_start_y - 1;
            }

            Set<Loc> river = walk(river_start_x, river_start_y, river_end_x, river_end_y, 0.65);

            //if any tile has 3 neighbors addContents it

            for (Loc t : river) {

                grid[t.x][t.y] = TS.river;

                for (int x = Math.max(0, t.x - FEATURE_SIZE / 2 - 1); x <= Math.min(t.x + FEATURE_SIZE / 2 + 1, this.mapW - 1); x++)
                    for (int y = Math.max(t.y - FEATURE_SIZE / 2 - 1, 0); y <= Math.min(t.y + FEATURE_SIZE / 2 + 1, mapH - 1); y++)
                        w[x][y] = 0;
            }
        }

        // For each room to place, select a position
        int NUM_ROOMS = NUM_STARTS_ROOM + NUM_METAL_ROOM + NUM_STONE_ROOM + NUM_TRESURE_ROOM;

        Queue<Loc> rooms = new LinkedList<>();
        while (rooms.size() < NUM_ROOMS) {

            double sum = 0;
            for (int i = 0; i < this.mapW; i++)
                for (int j = 0; j < mapH; j++)
                    sum += w[i][j];

            loop:
            while (true) {
                int i = random.nextInt(this.mapW);
                int j = random.nextInt(this.mapH);

                if (random.nextDouble() < w[i][j] / sum) {

                    //set mapW of everything within 4 to 0
                    for (int x = i - FEATURE_SIZE / 2; x <= i + FEATURE_SIZE / 2; x++)
                        for (int y = j - FEATURE_SIZE / 2; y <= j + FEATURE_SIZE / 2; y++)
                            if (grid[x][y] != TS.edgeWater)
                                grid[x][y] = TS.room;


                    for (int x = i - FEATURE_SIZE; x <= i + FEATURE_SIZE; x++)
                        for (int y = j - FEATURE_SIZE; y <= j + FEATURE_SIZE; y++)
                            w[x][y] = 0;

                    //lower mapW of everything farther proportional to distance


                    for (int x = 0; x < this.mapW; x++)
                        for (int y = 0; y < mapH; y++)
                            if (Util.dist(x, y, i, j) < 0.1 * distMax)
                                w[x][y] = w[x][y] * (Util.dist(x, y, i, j) / distMax);

                    rooms.add(new Loc(i, j));

                    break loop;
                }
            }
        }

        List<Loc> points = new LinkedList<>();
        points.addAll(rooms);

        for (Loc t1 : points)
            for (Loc t2 : points) {

                if (Util.dist(t1.x, t1.y, t2.x, t2.y) < distMax)

                    for (Loc loc : walk(t1.x, t1.y, t2.x, t2.y, 0.9)) {
                        if (grid[loc.x][loc.y] == TS.river)
                            grid[loc.x][loc.y] = TS.bridge;
                        if (grid[loc.x][loc.y] == TS.unset) //alert this changed
                            if (random.nextDouble() < 0.99)
                                grid[loc.x][loc.y] = TS.road;
                            else
                                grid[loc.x][loc.y] = TS.treasure;
                        if (grid[loc.x][loc.y] == TS.room)
                            grid[loc.x][loc.y] = TS.road;

                    }

            }


        // Add paths between them

        Loc t;

        startingLocations = new ArrayList<>();


        t = rooms.poll();
        grid[t.x - 1][t.y] = TS.startKingA;
        grid[t.x + 1][t.y] = TS.startSlayerA;
        startingLocations.add(t);

        t = rooms.poll();
        grid[t.x - 1][t.y] = TS.startKingB;
        grid[t.x + 1][t.y] = TS.startSlayerB;
        startingLocations.add(t);

        for (int i = 0; i < NUM_METAL_ROOM; i++) {
            t = rooms.poll();
            for (int x = t.x - 1; x <= t.x + 1; x++)
                for (int y = t.y - 1; y <= t.y + 1; y++)
                    if (random.nextDouble() < 0.8)
                        if (grid[x][y] != TS.edgeWater)
                            grid[x][y] = TS.metal;
        }

        for (int i = 0; i < NUM_STONE_ROOM; i++) {
            t = rooms.poll();
            for (int x = t.x - 1; x <= t.x + 1; x++)
                for (int y = t.y - 1; y <= t.y + 1; y++)
                    if (random.nextDouble() < 0.5)
                        if (grid[x][y] != TS.edgeWater)
                            grid[x][y] = TS.stone;
        }

        for (int i = 0; i < NUM_TRESURE_ROOM; i++) {
            t = rooms.poll();
            for (int x = t.x - 2; x <= t.x + 2; x++)
                for (int y = t.y - 2; y <= t.y + 2; y++)
                    if (random.nextDouble() < 0.1)
                        if (grid[x][y] != TS.edgeWater)
                            grid[x][y] = TS.treasure;
        }

        for (int x = 0; x < mapW; x++) {
            for (int y = 0; y < mapH; y++) {
                Set<Loc> set = new HashSet<>();
                flood(set, TS.unset, x, y);
                if (set.size() > 80) {

                    //gen forest
                    for (Loc t2 : set) {
                        if (random.nextDouble() < 0.3)
                            grid[t2.x][t2.y] = TS.tree;
                        else if (random.nextDouble() < 0.55) {
                            grid[t2.x][t2.y] = TS.grass0;
                        } else {
                            if (random.nextBoolean()) {
                                grid[t2.x][t2.y] = TS.grass1;
                            } else {
                                grid[t2.x][t2.y] = TS.grass2;
                            }
                        }
                    }

                } else if (set.size() > 3) {

                    //gen walls
                    for (Loc t2 : set) {
                        grid[t2.x][t2.y] = TS.wall;
                    }
                } else {
                    for (Loc t2 : set) {
                        grid[t2.x][t2.y] = TS.barrier;
                    }
                }

            }
        }

    }

    /**
     * returns the set of all connected tiles of the same type
     *
     * @param x the tiles x
     * @param y the tiles y
     */
    private Set<Loc> flood(TS type, int x, int y) {
        Set<Loc> set = new HashSet<>();
        flood(set, type, x, y);
        return set;
    }

    /**
     * returns the set of all connected tiles of the same type
     *
     * @param set the set to addContents too
     * @param x   the tiles x
     * @param y   the tiles y
     */
    private void flood(Set<Loc> set, TS type, int x, int y) {
        if (x >= 0 && y >= 0 && x < this.mapW && y < mapH && grid[x][y] == type && !set.contains(new Loc(x, y))) {
            set.add(new Loc(x, y));
            flood(set, type, x + 1, y);
            flood(set, type, x - 1, y);
            flood(set, type, x, y + 1);
            flood(set, type, x, y - 1);
        }
    }

    /**
     * drunkards walk algorithm
     *
     * @param sx start x
     * @param sy start y
     * @param tx target x
     * @param ty target y
     * @param c  value in range (0.5, 1) - probabitiy that any segment in path will go towards target
     * @return returns set of tiles from (sx, sy) to (tx, ty) with random variation
     */
    private Set<Loc> walk(int sx, int sy, int tx, int ty, double c) {
        if (tx < edgeWidth)
            tx = edgeWidth;
        if (tx > mapW - edgeWidth)
            tx = mapW - edgeWidth;
        if (ty < edgeWidth)
            ty = edgeWidth;
        if (ty > mapH - edgeWidth)
            ty = mapH - edgeWidth;

        Set<Loc> set = new HashSet<>();
        set.add(new Loc(sx, sy));
        while (sx != tx || sy != ty) {

            //randomly pick up down left or right
            int xdiff = sx - tx;
            int ydiff = sy - ty;


            if ((Math.abs(xdiff) >= Math.abs(ydiff) && random.nextDouble() < c) || (Math.abs(xdiff) <= Math.abs(ydiff) && random.nextDouble() < 1 - c)) {
                if ((xdiff >= 0 && random.nextDouble() < c) || (xdiff <= 0 && random.nextDouble() < 1 - c))
                    sx--;
                else
                    sx++;

            } else {
                if ((ydiff >= 0 && random.nextDouble() < c) || (ydiff <= 0 && random.nextDouble() < 1 - c))
                    sy--;
                else
                    sy++;
            }

            if (sx < edgeWidth)
                sx = edgeWidth;
            if (sx > mapW - edgeWidth)
                sx = mapW - edgeWidth;
            if (sy < edgeWidth)
                sy = edgeWidth;
            if (sy > mapH - edgeWidth)
                sy = mapH - edgeWidth;

            set.add(new Loc(sx, sy));
        }

        boolean change = true;
        while (change) {
            change = false;
            for (int x = 1; x < mapW - 1; x++) {
                for (int y = 1; y < mapH - 1; y++) {
                    if (!set.contains(new Loc(x, y))) {
                        int count = 0;
                        if (set.contains(new Loc(x + 1, y)))
                            count++;
                        if (set.contains(new Loc(x - 1, y)))
                            count++;
                        if (set.contains(new Loc(x, y + 1)))
                            count++;
                        if (set.contains(new Loc(x, y - 1)))
                            count++;
                        if (count >= 3) {
                            set.add(new Loc(x, y));
                            change = true;
                        }
                    }
                }
            }

            Set<Loc> toRemove = new HashSet<>();
            for (Loc l : set) {
                // if not at end and onlk has one neighbor, removeContents it
                if (l.x != sx || l.y != sy) {

                    int count = 0;
                    if (set.contains(new Loc(l.x + 1, l.y)))
                        count++;
                    if (set.contains(new Loc(l.x - 1, l.y)))
                        count++;
                    if (set.contains(new Loc(l.x, l.y + 1)))
                        count++;
                    if (set.contains(new Loc(l.x, l.y - 1)))
                        count++;
                    if (count <= 1)
                        toRemove.add(l);
                }
            }
            if (!toRemove.isEmpty())
                change = true;
            set.removeAll(toRemove);
        }

        return set;
    }


}