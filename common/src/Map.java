public class Map {


    public static int GRID_X_SIZE = 10;
    public static int GRID_Y_SIZE = 10;

    private Tile[][] grid = new Tile[GRID_X_SIZE][GRID_Y_SIZE];


    public Tile get(int x, int y) {
        return grid[x][y];
    }

    public Map(Model model) {
        //test map of all "grass"
        for(int i = 0; i < GRID_X_SIZE; i++) {
            for(int j = 0; j < GRID_Y_SIZE; j++) {
                grid[i][j] = new Grass(i, j, model);
            }
        }
    }

    public void update() {
        for(Tile[] arr : grid)
            for(Tile tile : arr)
                tile.update();
    }
}
