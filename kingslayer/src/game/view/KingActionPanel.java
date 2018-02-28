package game.view;

import game.model.game.model.ClientGameModel;
import game.model.game.model.GameModel;
import game.model.game.model.team.Team;
import images.Images;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


import static images.Images.CURSOR_IMAGE;

public class KingActionPanel extends ActionPanel {
  private GameModel model;
  private KingGameInteractionLayer gameInteractionLayer;

  public KingActionPanel(ClientGameModel model, KingGameInteractionLayer gameInteractionLayer) {
    super(model);
    this.model = model;
    this.gameInteractionLayer = gameInteractionLayer;

    GridPane grid = new GridPane();
    grid.setLayoutX(15);
    grid.setLayoutY(15);

    GridPane grid2 = new GridPane();
    grid2.setLayoutX(15);
    grid2.setLayoutY(15);

    Button wall = new Button("1", new ImageView(Images.WALL_BUILDABLE_IMAGE));
    wall.setTooltip(new Tooltip("Wall\nCost: 5 wood"));
    grid.add(wall, 0, 0);
    wall.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectWall();
    });

    Button resource = new Button("2", new ImageView((model.getLocalPlayer().getTeam() == Team.ONE) ?
        Images.RED_RESOURCE_COLLECTOR_IMAGE : Images.BLUE_RESOURCE_COLLECTOR_IMAGE));
    resource.setTooltip(new Tooltip("Resource collector\nCost: 10 wood"));
    grid.add(resource, 1, 0);
    resource.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectResourceCollector();
    });

    Button selectBarracks = new Button("3", new ImageView((model.getLocalPlayer().getTeam() == Team.ONE) ?
        Images.RED_WOOD_BARRACKS_IMAGE : Images.BLUE_WOOD_BARRACKS_IMAGE));
    selectBarracks.setTooltip(new Tooltip("Barracks\nMultiple options"));
    grid.add(selectBarracks, 2, 0);
    selectBarracks.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectingBarracks = true;
      this.getChildren().removeAll(grid);
      this.getChildren().add(grid2);
    });

    Button arrowTower = new Button("4", new ImageView((model.getLocalPlayer().getTeam() == Team.ONE) ?
        Images.RED_WOOD_ARROW_TOWER_IMAGE : Images.BLUE_WOOD_ARROW_TOWER_IMAGE));
    arrowTower.setTooltip(new Tooltip("Arrow tower\nCost: 50 wood"));
    grid.add(arrowTower, 3, 0);
    arrowTower.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectArrowTower();
    });

    Button trap = new Button("TODO 5");//, new ImageView((model.getLocalPlayer().getTeam() == Team.ONE) ?
//        Images.RED_WOOD_ARROW_TOWER_IMAGE : Images.BLUE_WOOD_ARROW_TOWER_IMAGE));
    grid.add(trap, 4, 0);
    trap.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
    });

    Button upgrade = new Button("E", new ImageView(Images.UPGRADE_CURSOR_IMAGE));
    upgrade.setTooltip(new Tooltip("Upgrade building"));
    grid.add(upgrade, 5, 0);
    upgrade.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectUpgrade();
    });

    Button delete = new Button("Q", new ImageView(Images.DELETE_CURSOR_IMAGE));
    delete.setTooltip(new Tooltip("Sell building"));
    grid.add(delete, 6, 0);
    delete.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectDelete();
    });


    Button melee = new Button("1", new ImageView(Images.CURSOR_IMAGE));
    melee.setTooltip(new Tooltip("Melee soldier barracks\nCost: 15 wood"));
    grid2.add(melee, 0, 0);
    melee.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectMelee();
      this.getChildren().removeAll(grid2);
      this.getChildren().add(grid);
    });

    Button ranged = new Button("2", new ImageView(Images.RANGED_SYMBOL_IMAGE));
    ranged.setTooltip(new Tooltip("Ranged soldier barracks\nCost: 15 wood"));
    grid2.add(ranged, 1, 0);
    ranged.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectRanged();
      this.getChildren().removeAll(grid2);
      this.getChildren().add(grid);
    });

    Button siege = new Button("3", new ImageView(Images.SIEGE_SYMBOL_IMAGE));
    siege.setTooltip(new Tooltip("Siege soldier barracks\nCost: 15 wood"));
    grid2.add(siege, 2, 0);
    siege.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectSiege();
      this.getChildren().removeAll(grid2);
      this.getChildren().add(grid);
    });

    Button exploration = new Button("4", new ImageView(Images.EXPLORATION_SYMBOL_IMAGE));
    exploration.setTooltip(new Tooltip("Exploration soldier barracks\nCost: 15 wood"));
    grid2.add(exploration, 3, 0);
    exploration.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectExploration();
      this.getChildren().removeAll(grid2);
      this.getChildren().add(grid);
    });

    this.getChildren().addAll(grid);
  }

  public void draw() {
  }
}
