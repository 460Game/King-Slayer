package game.view;

import game.model.game.model.ClientGameModel;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SlayerActionPanel extends ActionPanel {

    public SlayerActionPanel(ClientGameModel model, SlayerGameInteractionLayer gameInteractionLayer) {
        super(model); //supermodel

        Text text = new Text("Left click to charge\nRight click to shoot");
        text.setLayoutX(15);
        text.setLayoutY(25);
        text.setFont(new Font(20));
        text.setFill(Color.WHITE);
        this.getChildren().add(text);
    }
}
