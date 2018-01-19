package game;

import game.model.Game.GameModel;
import game.model.ServerGameModel;
import game.view.AIView;
import game.view.ClientView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Collections;

public class SingleplayerController extends Application {

    private ServerGameModel serverModel;
    private ClientView clientView;

    public SingleplayerController() {
        serverModel = new ServerGameModel(Collections.EMPTY_SET);
       // clientView = new ClientView(serverModel); TODO
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        clientView.start(new Stage());
    }

    public static void main(String args[]) {
        Application.launch();
    }

}
