package game.view;

import game.model.game.model.ClientGameModel;
import game.model.game.model.team.TeamResourceData;
import javafx.scene.ImageCursor;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import util.Const;

import static images.Images.CURSOR_IMAGE;

public class FPSPanel extends Region {
    private Text text;

    public FPSPanel() {
        this.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));
        this.setBackground(Const.PANEL_BG);
        text = new Text();
        text.setFont(new Font(20));
        text.setFill(Color.WHITE);
        text.setLayoutX(15);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutY(30);
        this.getChildren().add(text);
    }

    public void setFPS(int fps) {
        text.setText(fps + "");
    }
}
