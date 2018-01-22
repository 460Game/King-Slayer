package game.SinglePlayer;

import game.model.Game.Model.ClientGameModel;
import game.model.Game.Model.ServerGameModel;
import game.view.ClientView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.Collections;

/**
 * Class that allows for single player running of the game. This is
 * mainly used for testing and debugging.
 */
public class SingleplayerController extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ServerGameModel serverModel = new ServerGameModel();
        ClientGameModel clientModel = new ClientGameModel(new CopyingModelWrapper(serverModel));
        serverModel.init(Collections.singleton(new CopyingModelWrapper(clientModel)));
        serverModel.start();
        clientModel.start();
        ClientView clientView = new ClientView(clientModel);
        clientView.start(primaryStage);
    }

    public static void main(String args[]) {
        Application.launch();
    }

}
