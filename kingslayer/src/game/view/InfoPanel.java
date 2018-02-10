package game.view;

import game.model.game.model.ClientGameModel;
import game.model.game.model.GameModel;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * panel in bottem right for displaying teams health, xp and ??
 */
public class InfoPanel extends Region  {
    private GameModel model;
public InfoPanel(ClientGameModel model){
    this.model = model;
    this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));

}
}
