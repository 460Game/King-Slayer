package game.view;

import game.model.game.model.ClientGameModel;
import game.model.game.model.GameModel;
import game.model.game.model.team.Role;
import game.model.game.model.worldObject.entity.Entity;
import javafx.scene.ImageCursor;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static images.Images.CURSOR_IMAGE;

/**
 * panel in bottem right for displaying teams health, xp and ??
 */
public class InfoPanel extends Region {
    private GameModel model;

    public InfoPanel(ClientGameModel model) {
        this.model = model;
        this.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));
        this.setBorder(new Border(new BorderStroke(Color.WHITE,
            BorderStrokeStyle.SOLID,
            new CornerRadii(3),
            new BorderWidths(10))));
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(3), null)));
        double kingHealth = 10;
        double slayerHealth = 10;
        for(Entity e: model.getAllEntities()) {
            if (e.team == model.getLocalPlayer().team) {
                if (e.role == Role.KING)
                    kingHealth = e.data.health;
                else if (e.role == Role.SLAYER)
                    slayerHealth = e.data.health;
            }
        }
        Text text = new Text("KING HP: " + kingHealth + "     SLAYER HP: " + slayerHealth + "   SLAYER XP: 0    SLAYER LEVEL: 1");
        text.setFont(new Font(20));
        text.setFill(Color.WHITE);
        text.setLayoutX(10);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutY(50);
        this.getChildren().add(text);
    }
}
