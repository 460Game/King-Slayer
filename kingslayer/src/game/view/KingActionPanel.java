package game.view;

import game.model.game.model.ClientGameModel;
import game.model.game.model.GameModel;
import game.model.game.model.team.Team;
import images.Images;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


import static images.Images.CURSOR_IMAGE;

public class KingActionPanel extends ActionPanel {
  private GameModel model;
  private KingGameInteractionLayer gameInteractionLayer;

  public KingActionPanel(ClientGameModel model, KingGameInteractionLayer gameInteractionLayer) {
    this.model = model;
    this.gameInteractionLayer = gameInteractionLayer;

    this.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));
    this.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));
    this.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(3), null)));

    GridPane grid = new GridPane();
    grid.setLayoutX(15);
    grid.setLayoutY(15);

    Button wall = new Button("1", new ImageView(Images.WALL_BUILDABLE_IMAGE));
    grid.add(wall, 0, 0);
    wall.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectWall();
    });

    Button resource = new Button("2", new ImageView((model.getLocalPlayer().getTeam() == Team.ONE) ?
        Images.RED_RESOURCE_COLLECTOR_IMAGE : Images.BLUE_RESOURCE_COLLECTOR_IMAGE));
    grid.add(resource, 1, 0);
    resource.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectResourceCollector();
    });

    Button selectBarracks = new Button("3", new ImageView((model.getLocalPlayer().getTeam() == Team.ONE) ?
        Images.RED_WOOD_BARRACKS_IMAGE : Images.BLUE_WOOD_BARRACKS_IMAGE));
    grid.add(selectBarracks, 2, 0);
    selectBarracks.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectingBarracks = true;
    });

    Button arrowTower = new Button("4", new ImageView((model.getLocalPlayer().getTeam() == Team.ONE) ?
        Images.RED_WOOD_ARROW_TOWER_IMAGE : Images.BLUE_WOOD_ARROW_TOWER_IMAGE));
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
    grid.add(upgrade, 5, 0);
    upgrade.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectUpgrade();
    });

    Button delete = new Button("Q", new ImageView(Images.DELETE_CURSOR_IMAGE));
    grid.add(delete, 6, 0);
    delete.setOnAction(e -> {
      gameInteractionLayer.clearSelection();
      gameInteractionLayer.selectDelete();
    });

    this.getChildren().addAll(grid);


//    Text text = new Text("Kings:\nBuild Wall (1)\nBuild Collector(2)\n\nSlayers:\nClick to shoot");
//    text.setFont(new Font(20));
//    text.setFill(Color.WHITE);
//    text.setLayoutX(10);
//    text.setLayoutY(28);
//    this.getChildren().add(text);
  }

  public void draw() {
  }
}
