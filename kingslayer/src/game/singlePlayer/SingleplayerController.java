package game.singlePlayer;

import game.model.game.model.ClientGameModel;
import game.model.game.model.Model;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.view.GameView;
import javafx.stage.Stage;
import lobby.GameView2MainAdaptor;
import lobby.Main;
import lobby.PlayerInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that allows for single player running of the game. This is
 * mainly used for testing and debugging.
 */
public class SingleplayerController {
    Main mainApp;
    Role role;
    public SingleplayerController(Main mainApp, Role role) {
        this.mainApp = mainApp;
        this.role = role;
    }

    public void start(Stage primaryStage) throws Exception {
        ServerGameModel serverModel = new ServerGameModel();
        ClientGameModel clientModel = new ClientGameModel(new CopyingModelWrapper(serverModel));
        Map<Model, PlayerInfo> testingMap = new HashMap<>();
        Model clientGameModel2 = new CopyingModelWrapper(clientModel);
        testingMap.put(clientGameModel2, new PlayerInfo(Team.ONE, role, "tester"));

        serverModel.init(Collections.singleton(clientGameModel2), testingMap);
        serverModel.start();

        GameView gameView = new GameView(clientModel, new GameView2MainAdaptor() {
            @Override
            public int closeServer() {
                return mainApp.closeServer();
            }

            @Override
            public int rematch() {
                return mainApp.rematch();
            }
        });
        gameView.start(primaryStage);

        serverModel.changeResource(Team.ONE, TeamResourceData.Resource.WOOD, 10000000);
        serverModel.changeResource(Team.ONE, TeamResourceData.Resource.STONE, 10000000);
        serverModel.changeResource(Team.ONE, TeamResourceData.Resource.METAL, 10000000);
    }

//    public static void main(String args[]) {
//        Application.launch();
//    }

}
