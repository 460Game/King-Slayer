//package network;
//
//import javafx.application.Application;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class Lobby extends Application {
//    LobbyClient lobbyClient = null;
//    LobbyServer lobbyServer = null;
//    Stage window;
//    public void serverStartGame() throws Exception {
//        //only server can call this function
////        lobbyServer.start();
//    }
//
//    public void joinOthersGame(String host) throws Exception {
//        lobbyClient = new LobbyClient(window);
//        lobbyClient.start();
//        lobbyClient.connectTo(host);
//    }
//
//    public void hostGame() throws Exception {
//        lobbyServer = new LobbyServer();
//        lobbyServer.start();
//        lobbyClient = new LobbyClient(window);
//        lobbyClient.start();
//        lobbyClient.connectTo("localhost");
//        Thread.sleep(2000); //(connection needs time)
//
//        lobbyServer.startGame();
//    }
//
//    public void ready() {
//        lobbyClient.lobbyClientReady();
//    }
//
//    public static void main(String[] args) {
//        Application.launch();
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        window = primaryStage;
//        hostGame();
//        Thread.sleep(5000);//simulate user click (connection needs time)
//        System.out.println("read?");
//
//        ready();
//    }
//}
