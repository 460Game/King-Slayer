package images;

import javafx.scene.image.Image;

public abstract class Images {
    public static final Image BOX_IMAGE = getImage("barrier.png");
    public static final Image WALL_IMAGE = getImage("wall.png");
    public static final Image LOGO_IMAGE = getImage("logo.png");
    public static final Image LOGO_TEXT_IMAGE = getImage("logotext.png");
    public static final Image CURSOR_IMAGE = getImage("cursor.png");
    public static final Image GAME_CURSOR_IMAGE = getImage("gamecursor.png");

    public static final Image WALL_BUILDABLE_IMAGE = getImage("wall_buildable.png");
    public static final Image RED_RESOURCE_COLLECTOR_IMAGE = getImage("resource_collector_red.png");
    public static final Image BLUE_RESOURCE_COLLECTOR_IMAGE = getImage("resource_collector_blue.png");

    public static final Image TREASURE_IMAGE = getImage("treasure.png");

    public static Image TILE_IMAGE = getImage("tile_map.png");
    public static Image MENU_SPASH_BG_IMAGE = getImage("castleSplash.jpg");
    public static final Image[] METAL_IMAGES = getImages("iron_ingots.png",
        "iron_ingots_1.png","iron_ingots_3.png","iron_ingots_4.png","iron_ingots_5.png", "iron_ingots_6.png",
        "iron_ingots_7.png", "iron_ingots_8.png", "iron_ingots_9.png");
    public static final Image[] STONE_IMAGES = getImages("boulder_tall.png","boulder_1.png","boulder_2.png","boulder_3.png",
        "boulder_4.png","boulder_5.png","boulder_6.png","boulder_7.png");
    public static final Image[] TREE_IMAGES = getImages("tree.png", "tree_1.png", "tree_2.png", "tree_3.png", "tree_4.png");

    public static final Image RED_KING_IMAGE_SHEET = getImage("king_red_sheet_new.png");
    public static final Image BLUE_KING_IMAGE_SHEET = getImage("king_blue_sheet_new.png");
    public static final Image RED_SLAYER_IMAGE_SHEET = getImage("slayer_red_sheet.png");
    public static final Image BLUE_SLAYER_IMAGE_SHEET = getImage("slayer_blue_sheet.png");

    private static Image getImage(String s) {
        return new Image(Images.class.getResourceAsStream(s));
    }

    private static Image[] getImages(String... s) {
        Image[] arr = new Image[s.length];
        for(int i = 0; i < s.length; i++)
            arr[i] = getImage(s[i]);
        return arr;
    }
}
