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

public class ExitPrompt extends Region {
    private ClientGameModel model;
    private GameView view;
    ExitPrompt(ClientGameModel model, GameView view) {
        this.model = model;
        this.view = view;
        this.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));
        this.setBackground(model.getTeam().getPanelBG());
        Text text = new Text("Exit?");
        text.setFont(new Font("Goudy Old Style", 60));
        Font.getFamilies().forEach(System.out::println);
        text.setFill(Color.WHITE);
        text.setLayoutX(200);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutY(100);

        Button confirm = new Button("Back To Lobby");
        Button cancle = new Button("Cancel");
        confirm.setTranslateX(50);
        confirm.setTranslateY(240);
        cancle.setTranslateX(450);
        cancle.setTranslateY(240);
        confirm.setOnAction(l -> {
//            Platform.exit();
            Platform.runLater(() -> view.goBackToMain());
        });
        cancle.setOnAction(l-> {
            ExitPrompt.this.setVisible(false);
        });

        this.getChildren().addAll(text, confirm, cancle);
    }
    public void stop() {
        view = null;
        model = null;
    }
}
