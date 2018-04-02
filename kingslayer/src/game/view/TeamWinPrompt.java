package game.view;

import game.model.game.model.ClientGameModel;
import javafx.application.Platform;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static images.Images.CURSOR_IMAGE;

public class TeamWinPrompt extends Region {
    private ClientGameModel model;
    private GameView view;
    TeamWinPrompt(ClientGameModel gameModel, GameView gameView) {
        this.model = gameModel;
        this.view = gameView;

        this.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));
        this.setBackground(model.getTeam().getPanelBG());
        Text text = new Text("Chicken Dinner Winner Winner!");
        text.setFont(new Font(20));
        text.setFill(Color.BLACK);
        text.setLayoutX(10);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutY(50);

        Button back_to_lobby = new Button("Back To Lobby");
        Button rematch = new Button("Rematch");
        back_to_lobby.setTranslateX(100);
        back_to_lobby.setTranslateY(100);
        rematch.setTranslateX(280);
        rematch.setTranslateY(100);
        back_to_lobby.setOnAction(l -> {
            //TODO: change this to be another class
            Platform.runLater(() -> view.goBackToMain());
        });
        rematch.setOnAction(l-> {
//            Platform.runLater(() -> view.restart());
            Platform.runLater(() -> view.rematch());
        });

        this.getChildren().addAll(text, back_to_lobby, rematch);
    }

    public void stop() {
        model = null;
        view = null;
    }
}
