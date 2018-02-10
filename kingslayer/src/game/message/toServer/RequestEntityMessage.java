package game.message.toServer;

import game.message.toClient.NewEntityMessage;
import game.model.game.model.ServerGameModel;

public class RequestEntityMessage implements ToServerMessage {

    private long id;

    public RequestEntityMessage(long id) {
        this.id = id;
    }

    private RequestEntityMessage() {}

    @Override
    public void executeServer(ServerGameModel model) {
        model.processMessage(new NewEntityMessage(model.getEntityById(id)));
    }
}
