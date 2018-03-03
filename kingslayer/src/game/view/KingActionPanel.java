package game.view;

import game.model.game.model.ClientGameModel;
import game.model.game.model.GameModel;
import game.model.game.model.team.Team;
import images.Images;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class KingActionPanel extends ActionPanel {
  private GameModel model;
  private KingGameInteractionLayer gameInteractionLayer;

  public KingActionPanel(ClientGameModel model, KingGameInteractionLayer gameInteractionLayer) {
    super(model);
    this.model = model;
    this.gameInteractionLayer = gameInteractionLayer;

    this.setOnKeyPressed(kc -> {
      if (kc.getCode() == KeyCode.TAB) {
        gameInteractionLayer.world.requestFocus();
        System.out.println("in action");
      }
    });

    GridPane grid = new GridPane();
    grid.setLayoutX(10);
    grid.setLayoutY(10);
    grid.setHgap(5);
    grid.setVgap(5);

    ImageView button4resource = new ImageView(Images.RESOURCE_COLLECTOR_UI_IMAGE);
    button4resource.setFitWidth(32 * 1.5);
    button4resource.setFitHeight(52 * 1.5);
    button4resource.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectResourceCollector();
      gameInteractionLayer.world.requestFocus();
    });
    grid.add(button4resource, 0, 0);
//    Button resource = new Button("", button4resource); //new ImageView((model.getLocalPlayer().getTeam() == Team.ONE) ?
////        Images.RED_RESOURCE_COLLECTOR_IMAGE : Images.BLUE_RESOURCE_COLLECTOR_IMAGE));
//    resource.setTooltip(new Tooltip("Resource collector\nCost: 10 wood"));
//    System.out.println(javafx.scene.text.Font.getFamilies());
////    resource.setFont(new Font("Candara", 15));
//    grid.add(resource, 0, 0);
//    resource.setOnAction(e -> {
//      gameInteractionLayer.clearSelection();
//      gameInteractionLayer.selectResourceCollector();
//      gameInteractionLayer.world.requestFocus();
//    });

    ImageView button4wall = new ImageView(Images.WALL_BUILDABLE_UI_IMAGE);
    button4wall.setFitWidth(32 * 1.5);
    button4wall.setFitHeight(52 * 1.5);
    button4wall.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectWall();
      gameInteractionLayer.world.requestFocus();
    });
    grid.add(button4wall, 1, 0);
//    Button wall = new Button("", button4wall); //new ImageView(Images.WALL_BUILDABLE_IMAGE));
//    wall.setTooltip(new Tooltip("Wall\nCost: 5 wood"));
//    grid.add(wall, 1, 0);
//    wall.setOnAction(e -> {
//      gameInteractionLayer.clearSelection();
//      gameInteractionLayer.selectWall();
//      gameInteractionLayer.world.requestFocus();
//    });

    ImageView button4arrowTower = new ImageView(Images.ARROW_TOWER_UI_IMAGE);
    button4arrowTower.setFitWidth(32 * 1.5);
    button4arrowTower.setFitHeight(52 * 1.5);
    button4arrowTower.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectArrowTower();
      gameInteractionLayer.world.requestFocus();
    });
    grid.add(button4arrowTower, 2, 0);

//    Button arrowTower = new Button("", button4arrowTower);//new ImageView((model.getLocalPlayer().getTeam() == Team.ONE) ?
////        Images.RED_WOOD_ARROW_TOWER_IMAGE : Images.BLUE_WOOD_ARROW_TOWER_IMAGE));
//    arrowTower.setTooltip(new Tooltip("Arrow tower\nCost: 50 wood"));
//    grid.add(arrowTower, 2, 0);
//    arrowTower.setOnAction(e -> {
//      gameInteractionLayer.clearSelection();
//      gameInteractionLayer.selectArrowTower();
//      gameInteractionLayer.world.requestFocus();
//    });

    ImageView button4barracks = new ImageView(Images.BARRACKS_UI_IMAGE);
    button4barracks.setFitWidth(32 * 1.5);
    button4barracks.setFitHeight(52 * 1.5);
    button4barracks.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectBarracks();
      gameInteractionLayer.world.requestFocus();
    });
    grid.add(button4barracks, 3, 0);

//    Button selectBarracks = new Button("", button4barracks); //new ImageView((model.getLocalPlayer().getTeam() == Team.ONE) ?
////        Images.RED_WOOD_BARRACKS_IMAGE : Images.BLUE_WOOD_BARRACKS_IMAGE));
//    selectBarracks.setTooltip(new Tooltip("Barracks\nMultiple options"));
//    grid.add(selectBarracks, 3, 0);
//    selectBarracks.setOnAction(e -> {
//      gameInteractionLayer.clearSelection();
//      gameInteractionLayer.selectBarracks();
//      gameInteractionLayer.world.requestFocus();
//    });

//    Button trap = new Button("TODO 5");//, new ImageView((model.getLocalPlayer().getTeam() == Team.ONE) ?
////        Images.RED_WOOD_ARROW_TOWER_IMAGE : Images.BLUE_WOOD_ARROW_TOWER_IMAGE));
//    grid.add(trap, 4, 0);
//    trap.setVisible(false);
//    trap.setOnAction(e -> {
//      gameInteractionLayer.clearSelection();
//      gameInteractionLayer.world.requestFocus();
//    });

    ImageView button4upgrade = new ImageView(Images.UPGRADE_CURSOR_UI_IMAGE);
    button4upgrade.setFitWidth(32 * 1.5);
    button4upgrade.setFitHeight(52 * 1.5);
    button4upgrade.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectUpgrade();
      gameInteractionLayer.world.requestFocus();
    });
    grid.add(button4upgrade, 4, 0);

//    Button upgrade = new Button("", button4upgrade); //new ImageView(Images.UPGRADE_CURSOR_IMAGE));
////    upgrade.setPrefSize(32 * 1.5, 52 * 1.5);
//    upgrade.setTooltip(new Tooltip("Upgrade building\nWood->Stone: 10 Stone\nStone->Metal: 10 Metal"));
//    grid.add(upgrade, 5, 0);
//    upgrade.setOnAction(e -> {
//      gameInteractionLayer.clearSelection();
//      gameInteractionLayer.selectUpgrade();
//      gameInteractionLayer.world.requestFocus();
//    });

    ImageView button4delete = new ImageView(Images.DELETE_CURSOR_UI_IMAGE);
    button4delete.setFitWidth(32 * 1.5);
    button4delete.setFitHeight(52 * 1.5);
    button4delete.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectDelete();
      gameInteractionLayer.world.requestFocus();
    });
    grid.add(button4delete, 5, 0);

//    Button delete = new Button("", button4delete); //new ImageView(Images.DELETE_CURSOR_IMAGE));
////    delete.setPrefSize(32 * 1.5, 52 * 1.5);
//    delete.setTooltip(new Tooltip("Sell building"));
//    grid.add(delete, 6, 0);
//    delete.setOnAction(e -> {
//      gameInteractionLayer.clearSelection();
//      gameInteractionLayer.selectDelete();
//      gameInteractionLayer.world.requestFocus();
//    });

    this.getChildren().addAll(grid);
  }

  public void draw() {
  }
}
