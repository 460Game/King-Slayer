package game;

import game.model.Game.Model.ClientGameModel;
import game.model.Game.Model.ServerGameModel;
import game.view.ClientView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.Collections;

public class SingleplayerController extends Application {

    private ServerGameModel serverModel;
    private ClientGameModel clientModel;
    private ClientView clientView;

    public SingleplayerController() {
        serverModel = new ServerGameModel();
        clientModel = new ClientGameModel(null);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        serverModel.init(Collections.EMPTY_SET);
//        clientModel.init(serverModel);
        clientView = new ClientView(clientModel);
        clientView.start(new Stage());
    }

    public static void main(String args[]) {
        Application.launch();
    }

}
