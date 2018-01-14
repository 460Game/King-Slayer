package game;

import game.message.Message;
import game.model.ClientGameModel;
import game.model.Game.GameModel;
import game.model.Game.WorldObject.TestPlayer;
import game.model.IModel;
import game.model.ServerGameModel;
import game.view.AIView;
import game.view.ClientView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Collections;

public class SingleplayerController extends Application {

    private GameModel serverModel;
    private GameModel clientModel;

    private ClientView clientView;
    private AIView aiView;

    public SingleplayerController() {

        clientModel = new ClientGameModel(new IModel() {
            @Override
            public void processMessage(Message m) {
                serverModel.processMessage(m);
            }

            @Override
            public long nanoTime() {
                return serverModel.nanoTime();
            }
        });
        serverModel = new ServerGameModel(Collections.singleton(new IModel() {

            @Override
            public void processMessage(Message m) {
                clientModel.processMessage(m);
            }

            @Override
            public long nanoTime() {
                return clientModel.nanoTime();
            }
        }));

        clientView = new ClientView(clientModel);
        aiView = new AIView(clientModel);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        aiView.start();
        clientView.start(primaryStage);
    }

    public static void main(String args[]) {
        Application.launch();
    }

}
