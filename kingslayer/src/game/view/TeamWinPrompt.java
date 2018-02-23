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
    TeamWinPrompt(ClientGameModel model, GameView view) {
        this.model = model;
        this.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));
        this.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(3), null)));
        Text text = new Text("Winner Winner Chicken Dinner!");
        text.setFont(new Font(20));
        text.setFill(Color.WHITE);
        text.setLayoutX(10);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutY(50);

        Button back_to_lobby = new Button("Back To Lobby");
        Button rematch = new Button("Rematch");
        back_to_lobby.setTranslateX(100);
        back_to_lobby.setTranslateY(100);
        rematch.setTranslateX(400);
        rematch.setTranslateY(100);
        back_to_lobby.setOnAction(l -> {
            //TODO: change this to be another class
            Platform.runLater(() -> view.goBackToMain());
        });
        rematch.setOnAction(l-> {
            Platform.runLater(() -> view.restart());
        });

        this.getChildren().addAll(text, back_to_lobby, rematch);
    }
}
