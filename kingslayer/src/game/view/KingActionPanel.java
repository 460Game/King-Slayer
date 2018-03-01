package game.view;

import game.model.game.model.ClientGameModel;
import game.model.game.model.GameModel;
import game.model.game.model.team.Team;
import images.Images;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

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

    Button resource = new Button("1", new ImageView((model.getLocalPlayer().getTeam() == Team.ONE) ?
        Images.RED_RESOURCE_COLLECTOR_IMAGE : Images.BLUE_RESOURCE_COLLECTOR_IMAGE));
    resource.setTooltip(new Tooltip("Resource collector\nCost: 10 wood"));
    grid.add(resource, 0, 0);
    resource.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectResourceCollector();
      gameInteractionLayer.world.requestFocus();
    });

    Button wall = new Button("2", new ImageView(Images.WALL_BUILDABLE_IMAGE));
    wall.setTooltip(new Tooltip("Wall\nCost: 5 wood"));
    grid.add(wall, 1, 0);
    wall.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectWall();
      gameInteractionLayer.world.requestFocus();
    });

    Button arrowTower = new Button("3", new ImageView((model.getLocalPlayer().getTeam() == Team.ONE) ?
        Images.RED_WOOD_ARROW_TOWER_IMAGE : Images.BLUE_WOOD_ARROW_TOWER_IMAGE));
    arrowTower.setTooltip(new Tooltip("Arrow tower\nCost: 50 wood"));
    grid.add(arrowTower, 2, 0);
    arrowTower.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectArrowTower();
      gameInteractionLayer.world.requestFocus();
    });

    Button selectBarracks = new Button("4", new ImageView((model.getLocalPlayer().getTeam() == Team.ONE) ?
        Images.RED_WOOD_BARRACKS_IMAGE : Images.BLUE_WOOD_BARRACKS_IMAGE));
    selectBarracks.setTooltip(new Tooltip("Barracks\nMultiple options"));
    grid.add(selectBarracks, 3, 0);
    selectBarracks.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectBarracks();
      gameInteractionLayer.world.requestFocus();
    });

//    Button trap = new Button("TODO 5");//, new ImageView((model.getLocalPlayer().getTeam() == Team.ONE) ?
////        Images.RED_WOOD_ARROW_TOWER_IMAGE : Images.BLUE_WOOD_ARROW_TOWER_IMAGE));
//    grid.add(trap, 4, 0);
//    trap.setOnAction(e -> {
//      gameInteractionLayer.clearSelection();
//      gameInteractionLayer.world.requestFocus();
//    });

    Button upgrade = new Button("E", new ImageView(Images.UPGRADE_CURSOR_IMAGE));
    upgrade.setTooltip(new Tooltip("Upgrade building\nWood->Stone: 10 Stone\nStone->Metal: 10 Metal"));
    grid.add(upgrade, 5, 0);
    upgrade.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectUpgrade();
      gameInteractionLayer.world.requestFocus();
    });

    Button delete = new Button("Q", new ImageView(Images.DELETE_CURSOR_IMAGE));
    delete.setTooltip(new Tooltip("Sell building"));
    grid.add(delete, 6, 0);
    delete.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectDelete();
      gameInteractionLayer.world.requestFocus();
    });

    this.getChildren().addAll(grid);
  }

  public void draw() {
  }
}
