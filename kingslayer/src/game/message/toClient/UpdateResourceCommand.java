package game.message.toClient;

import game.model.game.model.ClientGameModel;
import game.model.game.model.team.TeamResourceData;

/**
 * Message sent to a client to update the team resources.
 */
public class UpdateResourceCommand implements ToClientCommand {

    /**
     * Data holding the resource counts of the team.
     */
    private TeamResourceData data;

    /**
     * Default constructor needed for serialization.
     */
    private UpdateResourceCommand() {}

    /**
     * Constructor of a message, given a team's resource data.
     * @param teamResourceData team's resource data
     */
    public UpdateResourceCommand(TeamResourceData teamResourceData) {
        this.data = teamResourceData;
    }

    /**
     * Updates a team's resource data.
     * @param model the game model on the client
     */
    @Override
    public void executeClient(ClientGameModel model) {
        model.setResourceData(data);
    }

}
