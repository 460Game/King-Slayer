package game.message.toClient;

import game.model.game.model.ClientGameModel;

public class SlayerDieCommand implements ToClientCommand {

    private long id;

    private SlayerDieCommand() {}
    public SlayerDieCommand(long id) {
        this.id = id;
    }

    @Override
    public void executeClient(ClientGameModel model) {
        if(model.getLocalPlayerId() == this.id)
            model.slayerDead();
    }
}
