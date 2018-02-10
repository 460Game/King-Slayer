package game.view;

import game.model.game.model.ClientGameModel;
import game.model.game.model.GameModel;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ResourcePanel extends Region {
    private GameModel model;
    public ResourcePanel(ClientGameModel model) {
        this.model = model;
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));

    }
}
