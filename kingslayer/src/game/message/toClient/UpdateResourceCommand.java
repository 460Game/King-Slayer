package game.message.toClient;

import game.model.game.model.ClientGameModel;
import game.model.game.model.team.TeamResourceData;

public class UpdateResourceCommand implements ToClientCommand {

    private TeamResourceData data;

    public UpdateResourceCommand(TeamResourceData teamResourceData) {
        this.data = teamResourceData;
    }

    @Override
    public void executeClient(ClientGameModel model) {
        model.setResourceData(data);
    }

    private UpdateResourceCommand() {}
}
