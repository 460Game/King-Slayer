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
import static util.Const.PANEL_BG;
import static util.Const.PROMPT_BG;

public class TeamLosePrompt extends Region {
    private ClientGameModel model;
    TeamLosePrompt(ClientGameModel model, GameView view) {
        this.model = model;
        this.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));
        this.setBackground(PROMPT_BG);

        Text text = new Text("Better Luck Next Time!");
        text.setFont(new Font(20));
        text.setFill(Color.BLACK);
        text.setLayoutX(10);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutY(50);

        Button confirm = new Button("Back To Lobby");
        Button rematch = new Button("Rematch");
        confirm.setTranslateX(100);
        confirm.setTranslateY(100);
        rematch.setTranslateX(280);
        rematch.setTranslateY(100);

        confirm.setOnAction(l -> {
            view.goBackToMain();
        });
        rematch.setOnAction(l-> {
            view.restart();
        });

        this.getChildren().addAll(text, confirm, rematch);
    }
}
