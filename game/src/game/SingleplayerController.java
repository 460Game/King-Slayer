package game;

import Util.Util;
import game.message.Message;
import game.model.ClientGameModel;
import game.model.Game.GameModel;
import game.model.Game.WorldObject.Entity;
import game.model.Game.WorldObject.TestPlayer;
import game.model.IModel;
import game.model.ServerGameModel;
import game.view.AIView;
import game.view.ClientView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.Collections;

public class SingleplayerController extends Application {

    private GameModel serverModel;
    private GameModel clientModel;

    private ClientView clientView1;
    private ClientView clientView2;
    private AIView aiView;

    public SingleplayerController() {

        serverModel = new ServerGameModel(Collections.EMPTY_SET);/*Collections.singleton(new IModel() {

            @Override
            public void processMessage(Message m) {
                Message copy = Util.KYRO.copy(m); //to simulate going thoruhg network - make a copy
                clientModel.processMessage(copy);
            }

            @Override
            public long nanoTime() {
                return clientModel.nanoTime();
            }
        }));
        clientModel = new ClientGameModel(new IModel() {
            @Override
            public void processMessage(Message m) {
                Message copy = Util.KYRO.copy(m); //to simulate going thoruhg network - make a copy
                serverModel.processMessage(copy);
            }

            @Override
            public long nanoTime() {
                return serverModel.nanoTime();
            }
        }, serverModel.getGenerator());

        clientView1 = new ClientView(clientModel);*/
        clientView2 = new ClientView(serverModel);
       // aiView = new AIView(serverModel);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        aiView.start();
      //  clientView1.start(primaryStage);
        clientView2.start(new Stage());
    }

    public static void main(String args[]) {
        Application.launch();
    }

}
