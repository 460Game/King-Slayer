package game.message;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.ServerGameModel;

public class RequestEntityMessage implements ToServerMessage {

    private long id;

    public RequestEntityMessage(long id) {
        this.id = id;
    }

    private RequestEntityMessage() {}

    @Override
    public void executeServer(ServerGameModel model) {
        Log.info("Client request entity " + id);
        model.processMessage(new NewEntityMessage(model.getEntityById(id)));
    }
}
