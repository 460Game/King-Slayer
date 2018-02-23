package game.singlePlayer;

import game.model.game.model.ClientGameModel;
import game.model.game.model.Model;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.view.GameView;
import javafx.stage.Stage;
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
    public SingleplayerController(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void start(Stage primaryStage) throws Exception {
        ServerGameModel serverModel = new ServerGameModel();
        ClientGameModel clientModel = new ClientGameModel(new CopyingModelWrapper(serverModel));
        Map<Model, PlayerInfo> testingMap = new HashMap<>();
        Model clientGameModel2 = new CopyingModelWrapper(clientModel);
        testingMap.put(clientGameModel2, new PlayerInfo(Team.TWO, Role.KING, "testName"));
        serverModel.init(Collections.singleton(clientGameModel2), testingMap);
        serverModel.start();

        GameView gameView = new GameView(clientModel, mainApp);
        gameView.start(primaryStage);
    }

//    public static void main(String args[]) {
//        Application.launch();
//    }

}
