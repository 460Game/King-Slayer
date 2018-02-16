package game.view;

import game.model.game.model.ClientGameModel;
import game.model.game.model.GameModel;
import game.model.game.model.team.TeamResourceData;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static images.Images.CURSOR_IMAGE;

public class ResourcePanel extends Region {
    private ClientGameModel model;
    private Text text;

    public ResourcePanel(ClientGameModel model) {
        this.model = model;
        this.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));
        this.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(3), null)));
        text = new Text();
        text.setFont(new Font(20));
        text.setFill(Color.WHITE);
        text.setLayoutX(10);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutY(30);
        this.getChildren().add(text);
    }

    public void update() {
        text.setText("WOOD " + model.getResourceData().getResource(TeamResourceData.Resource.WOOD)+ "   STONE " +
            model.getResourceData().getResource(TeamResourceData.Resource.STONE) + "   METAL " +
            model.getResourceData().getResource(TeamResourceData.Resource.METAL));
    }
}
