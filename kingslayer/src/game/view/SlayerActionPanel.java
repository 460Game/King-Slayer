package game.view;

import com.sun.deploy.panel.AdvancedPanel;
import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Team;
import images.Images;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SlayerActionPanel extends ActionPanel {

    public SlayerActionPanel(ClientGameModel model, SlayerGameInteractionLayer gameInteractionLayer) {
        super(model); //supermodel

//        Text text = new Text("Left click to charge\nRight click to shoot");
//        text.setLayoutX(15);
//        text.setLayoutY(25);
//        text.setFont(new Font(20));
//        text.setFill(Color.WHITE);
//        this.getChildren().add(text);

        GridPane grid = new GridPane();
        grid.setLayoutX(10);
        grid.setLayoutY(10);
        grid.setHgap(0);
        grid.setVgap(5);

        Text controls = new Text("Controls:");
        //if (model.getLocalPlayer().getTeam() == Team.BLUE_TEAM)
        controls.setFill(Color.WHITE);
        grid.add(controls, 0, 0);

        Label shoot = new Label("Shoot arrow   ");
        shoot.setGraphic(new ImageView(Images.SHOOTING_SYMBOL_IMAGE));
        shoot.setTextFill(Color.WHITE);
        //shoot.setStyle("-fx-background-color: #ffffffaa;");
        //shoot.setStyle("-fx-border-color: #aaaaaa; -fx-border-width: 2px;");
        grid.add(shoot, 1, 1);

        Label charge = new Label("Charge           ");
        charge.setGraphic(new ImageView(Images.CHARGING_SYMBOL_IMAGE));
        charge.setTextFill(Color.WHITE);
        //charge.setStyle("-fx-background-color: #ffffffaa;");
        //charge.setStyle("-fx-border-color: #ffffffaa; -fx-border-width: 2px;");
        grid.add(charge, 0, 1);

        this.getChildren().add(grid);

    }
}
