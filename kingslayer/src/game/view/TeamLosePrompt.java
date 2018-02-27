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

public class TeamLosePrompt extends Region {
    private ClientGameModel model;
    TeamLosePrompt(ClientGameModel model, GameView view) {
        this.model = model;
        this.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));
        this.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(3), null)));

        Text text = new Text("Better Luck Next Time!");
        text.setFont(new Font(20));
        text.setFill(Color.WHITE);
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
            Platform.runLater(() -> view.goBackToMain());
        });
        rematch.setOnAction(l-> {
            Platform.runLater(() -> view.restart());
        });

        this.getChildren().addAll(text, confirm, rematch);
    }
}
