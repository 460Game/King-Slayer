package game.message.toClient;

import game.model.game.model.ClientGameModel;
import game.model.game.model.team.TeamResourceData;

public class UpdateResourcesMessage implements ToClientMessage {

    private TeamResourceData data;

    public UpdateResourcesMessage(TeamResourceData teamResourceData) {
        this.data = teamResourceData;
    }

    @Override
    public void executeClient(ClientGameModel model) {
        model.setResourceData(data);
    }

    private UpdateResourcesMessage() {}
}
