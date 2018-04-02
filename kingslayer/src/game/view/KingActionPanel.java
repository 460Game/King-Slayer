package game.view;

import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.EntitySpawner;
import images.Images;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;

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

    DescriptionPanel descriptionPanel = new DescriptionPanel(model, new Text(""));
    descriptionPanel.setPrefSize(335, 150);
    descriptionPanel.layoutXProperty().bind(this.widthProperty().divide(2).subtract(168));
    descriptionPanel.layoutYProperty().bind(this.heightProperty().subtract(300));
    descriptionPanel.setVisible(false);
    this.getChildren().add(descriptionPanel);

    /* Resource collector button */
    resource = new ImageView(Images.RESOURCE_COLLECTOR_UI_IMAGE);
    resource.setFitWidth(32 * 1.5);
    resource.setFitHeight(52 * 1.5);
    resource.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectResourceCollector();
    });
    resource.setOnMouseEntered(e -> {
      descriptionPanel.setText(new Text("Resource collector\nCost: " + EntitySpawner.RESOURCE_COLLETOR_SPAWNER.finalCost(model) +
          " wood\nUpgrade to stone: 10 stone\nUpgrade to metal: 10 metal\n*Hold 1 or Shift to place multiple"));
      descriptionPanel.setVisible(true);
    });
    resource.setOnMouseExited(e -> {
      descriptionPanel.setVisible(false);
    });
    grid.add(resource, 0, 0);

    /* Wall button */
    wall = new ImageView(Images.WALL_BUILDABLE_UI_IMAGE);
    wall.setFitWidth(32 * 1.5);
    wall.setFitHeight(52 * 1.5);
    wall.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectWall();
      gameInteractionLayer.world.requestFocus();
    });
    wall.setOnMouseEntered(e -> {
      descriptionPanel.setText(new Text("Wall\nCost: " + EntitySpawner.WALL_SPAWNER.finalCost(model) + " wood\nUpgrade to stone: 5 stone\n" +
          "Upgrade to metal: 5 metal\n*Hold 2 or Shift to place multiple"));
      descriptionPanel.setVisible(true);
    });
    wall.setOnMouseExited(e -> {
      descriptionPanel.setVisible(false);
    });
    grid.add(wall, 1, 0);

    /* Arrow tower button */
    arrowTower = new ImageView(Images.ARROW_TOWER_UI_IMAGE);
    arrowTower.setFitWidth(32 * 1.5);
    arrowTower.setFitHeight(52 * 1.5);
    arrowTower.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectArrowTower();
      gameInteractionLayer.world.requestFocus();
    });
    arrowTower.setOnMouseEntered(e -> {
      descriptionPanel.setText(new Text("Arrow Tower\nCost: " + EntitySpawner.ARROW_TOWER_SPAWNER.finalCost(model) + " wood\nUpgrade to stone: 20 stone\n" +
          "Upgrade to metal: 20 metal\n*Hold 3 or Shift to place multiple"));
      descriptionPanel.setVisible(true);
    });
    arrowTower.setOnMouseExited(e -> {
      descriptionPanel.setVisible(false);
    });
    grid.add(arrowTower, 2, 0);

    /* Barracks button */
    barracks = new ImageView(Images.BARRACKS_UI_IMAGE);
    barracks.setFitWidth(32 * 1.5);
    barracks.setFitHeight(52 * 1.5);
    barracks.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectBarracks();
      gameInteractionLayer.world.requestFocus();
    });
    barracks.setOnMouseEntered(e -> {
      descriptionPanel.setText(new Text("Barracks\nCost: " + EntitySpawner.BARRACKS_SPAWNER.finalCost(model) + " wood\nUpgrade to stone: 15 stone\n" +
          "Upgrade to metal: 20 metal\n*Hold 4 or Shift to place multiple"));
      descriptionPanel.setVisible(true);
    });
    barracks.setOnMouseExited(e -> {
      descriptionPanel.setVisible(false);
    });
    grid.add(barracks, 3, 0);

    /* Upgrade button */
    ImageView upgrade = new ImageView(Images.UPGRADE_CURSOR_UI_IMAGE);
    upgrade.setFitWidth(32 * 1.5);
    upgrade.setFitHeight(52 * 1.5);
    upgrade.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectUpgrade();
      gameInteractionLayer.world.requestFocus();
    });
    upgrade.setOnMouseEntered(e -> {
      descriptionPanel.setText(new Text("Upgrade\nDifferent costs for different\n   buildings\nHold E or Shift to upgrade multiple"));
      descriptionPanel.setVisible(true);
    });
    upgrade.setOnMouseExited(e -> {
      descriptionPanel.setVisible(false);
    });
    grid.add(upgrade, 5, 0);

    /* Sell button */
    ImageView delete = new ImageView(Images.DELETE_CURSOR_UI_IMAGE);
    delete.setFitWidth(32 * 1.5);
    delete.setFitHeight(52 * 1.5);
    delete.setOnMouseClicked(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectDelete();
      gameInteractionLayer.world.requestFocus();
    });
    delete.setOnMouseEntered(e -> {
      descriptionPanel.setText(new Text("Sell\nDifferent prices for different\n   buildings\nHold Q or Shift to sell multiple"));
      descriptionPanel.setVisible(true);
    });
    delete.setOnMouseExited(e -> {
      descriptionPanel.setVisible(false);
    });
    grid.add(delete, 4, 0);

    // Add all buttons
    this.getChildren().addAll(grid);
  }

  public void draw() {
    ColorAdjust desaturate = new ColorAdjust();
    desaturate.setSaturation(-1);
    desaturate.setBrightness(-0.5);

    ColorAdjust saturate = new ColorAdjust();
    saturate.setSaturation(0);
    saturate.setBrightness(0);

    if (model.getResourceData().getResource(EntitySpawner.RESOURCE_COLLETOR_SPAWNER.resource) < EntitySpawner.RESOURCE_COLLETOR_SPAWNER.finalCost(model)) {
      resource.setEffect(desaturate);
    } else {
      resource.setEffect(saturate);
    }

    if (model.getResourceData().getResource(EntitySpawner.WALL_SPAWNER.resource) < EntitySpawner.WALL_SPAWNER.finalCost(model)) {
      wall.setEffect(desaturate);
    } else {
      wall.setEffect(saturate);
    }

    if (model.getResourceData().getResource(EntitySpawner.ARROW_TOWER_SPAWNER.resource) < EntitySpawner.ARROW_TOWER_SPAWNER.finalCost(model)) {
      arrowTower.setEffect(desaturate);
    } else {
      arrowTower.setEffect(saturate);
    }

    if (model.getResourceData().getResource(EntitySpawner.BARRACKS_SPAWNER.resource) < EntitySpawner.BARRACKS_SPAWNER.finalCost(model)) {
      barracks.setEffect(desaturate);
    } else {
      barracks.setEffect(saturate);
    }
  }
}
