package network;

import game.message.Message;

public interface NetWork2LobbyAdaptor {
    public void init();
    public void makeModel();
    public void getMsg(Message obj);

    public void showLobbyTeamChoice();
}
