package game.view;


import game.model.game.model.ClientGameModel;
import game.model.game.model.GameModel;
import javafx.beans.binding.Bindings;
import javafx.scene.ImageCursor;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static images.Images.CURSOR_IMAGE;

public class DescriptionPanel extends Region {
  private GameModel model;
  private Text description;

  public DescriptionPanel() {}

  public DescriptionPanel(ClientGameModel model, Text description) {
    this.model = model;
    this.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));
    this.setBackground(model.getTeam().getPanelBG());
    this.description = description;
    description.setTranslateX(10);
    description.setTranslateY(25);
    description.setFill(Color.WHITE);
    description.setFont(new Font(20));
    this.getChildren().add(description);
  }

  public void setText(Text text) {
    this.getChildren().removeAll(description);
    description = text;
    description.setTranslateX(10);
    description.setTranslateY(25);
    description.setFill(Color.WHITE);
    description.setFont(new Font(20));
    this.getChildren().add(description);
  }
}
