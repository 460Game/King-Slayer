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
import music.MusicPlayer;
import util.CssSheet;

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
        text.setFill(Color.WHITE);
        text.setLayoutX(90);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutY(50);

        Button back_to_lobby = new Button("Back To Lobby");
        Button rematch = new Button("   Rematch   ");

//        back_to_lobby.setTranslateX(100);
//        back_to_lobby.setTranslateY(100);
//        rematch.setTranslateX(280);
//        rematch.setTranslateY(100);

        back_to_lobby.setTranslateX(50);
        back_to_lobby.setTranslateY(220);
        rematch.setTranslateX(350);
        rematch.setTranslateY(220);
        back_to_lobby.setStyle(CssSheet.YELLO_BUTTON_CSS);
        rematch.setStyle(CssSheet.YELLO_BUTTON_CSS);
        back_to_lobby.setOnAction(l -> {
            MusicPlayer.stopDangerSound();
            //TODO: change this to be another class
            Platform.runLater(() -> view.goBackToMain());
        });
        rematch.setOnAction(l-> {
//            Platform.runLater(() -> view.restart());
            Platform.runLater(() -> {
                text.setText("Waiting for rematch...");
                view.rematch();
            });
            stop();
        });

        this.getChildren().addAll(text, back_to_lobby, rematch);
    }

    public void stop() {
        model = null;
        view = null;
    }
}
