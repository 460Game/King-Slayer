package game.view;

import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.EntitySpawner;
import images.Images;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

public class KingActionPanel extends ActionPanel {
  private ClientGameModel model;
  private KingGameInteractionLayer gameInteractionLayer;

  private ImageView resource;
  private ImageView wall;
  private ImageView arrowTower;
  private ImageView barracks;


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

    resource = new ImageView(Images.RESOURCE_COLLECTOR_UI_IMAGE);
    resource.setFitWidth(32 * 1.5);
    resource.setFitHeight(52 * 1.5);
    resource.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectResourceCollector();
      gameInteractionLayer.world.requestFocus();
    });
    grid.add(resource, 0, 0);

    wall = new ImageView(Images.WALL_BUILDABLE_UI_IMAGE);
    wall.setFitWidth(32 * 1.5);
    wall.setFitHeight(52 * 1.5);
    wall.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectWall();
      gameInteractionLayer.world.requestFocus();
    });
    grid.add(wall, 1, 0);

    arrowTower = new ImageView(Images.ARROW_TOWER_UI_IMAGE);
    arrowTower.setFitWidth(32 * 1.5);
    arrowTower.setFitHeight(52 * 1.5);
    arrowTower.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectArrowTower();
      gameInteractionLayer.world.requestFocus();
    });
    grid.add(arrowTower, 2, 0);

    barracks = new ImageView(Images.BARRACKS_UI_IMAGE);
    barracks.setFitWidth(32 * 1.5);
    barracks.setFitHeight(52 * 1.5);
    barracks.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectBarracks();
      gameInteractionLayer.world.requestFocus();
    });
    grid.add(barracks, 3, 0);

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
    grid.add(button4upgrade, 5, 0);

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
    grid.add(button4delete, 4, 0);

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
    ColorAdjust desaturate = new ColorAdjust();
    desaturate.setSaturation(-1);
    desaturate.setBrightness(-0.5);

    ColorAdjust saturate = new ColorAdjust();
    saturate.setSaturation(0);
    saturate.setBrightness(0);

    if (model.getResourceData().getResource(EntitySpawner.RESOURCE_COLLETOR_SPAWNER.resource) < -EntitySpawner.RESOURCE_COLLETOR_SPAWNER.cost) {
      resource.setEffect(desaturate);
    } else {
      resource.setEffect(saturate);
    }

    if (model.getResourceData().getResource(EntitySpawner.WALL_SPAWNER.resource) < -EntitySpawner.WALL_SPAWNER.cost) {
      wall.setEffect(desaturate);
    } else {
      wall.setEffect(saturate);
    }

    if (model.getResourceData().getResource(EntitySpawner.ARROW_TOWER_SPAWNER.resource) < -EntitySpawner.ARROW_TOWER_SPAWNER.cost) {
      arrowTower.setEffect(desaturate);
    } else {
      arrowTower.setEffect(saturate);
    }

    if (model.getResourceData().getResource(EntitySpawner.BARRACKS_SPAWNER.resource) < -EntitySpawner.BARRACKS_SPAWNER.cost) {
      barracks.setEffect(desaturate);
    } else {
      barracks.setEffect(saturate);
    }
  }
}
