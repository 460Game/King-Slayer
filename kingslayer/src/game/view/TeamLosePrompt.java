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

public class TeamLosePrompt extends Region {
    private ClientGameModel model;
    private GameView view;
    TeamLosePrompt(ClientGameModel gameModel, GameView gameView) {
        this.model = gameModel;
        this.view = gameView;

        this.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));
        this.setBackground(model.getTeam().getPanelBG());

        Text text = new Text("Better Luck Next Time!");
        text.setFont(new Font(20));
        text.setFill(Color.WHITE);
        text.setLayoutX(90);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutY(50);

        Button confirm = new Button("Back To Lobby");
        Button rematch = new Button("   Rematch   ");
        confirm.setStyle(CssSheet.YELLO_BUTTON_CSS);
        rematch.setStyle(CssSheet.YELLO_BUTTON_CSS);
//        confirm.setTranslateX(100);
//        confirm.setTranslateY(100);
//        rematch.setTranslateX(280);
//        rematch.setTranslateY(100);
        confirm.setTranslateX(50);
        confirm.setTranslateY(220);
        rematch.setTranslateX(350);
        rematch.setTranslateY(220);

        confirm.setOnAction(l -> {
            MusicPlayer.stopDangerSound();
            view.goBackToMain();
        });
        rematch.setOnAction(l-> {
//            Platform.runLater(() -> view.restart());
            Platform.runLater(() -> {
                text.setText("Waiting for rematch...");
                view.rematch();
            });
        });

        this.getChildren().addAll(text, confirm, rematch);
    }

    public void stop() {
        view = null;
        model = null;
    }
}
