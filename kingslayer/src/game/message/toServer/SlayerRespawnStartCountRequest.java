package game.message.toServer;

import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;

import java.util.Timer;
import java.util.TimerTask;

public class SlayerRespawnStartCountRequest extends ActionRequest {
    Team team;
    String name;

    public SlayerRespawnStartCountRequest() {}

    public SlayerRespawnStartCountRequest(Team team, String name) {
        this.team = team;
        this.name = name;
    }
    @Override
    public void executeServer(ServerGameModel model) {
        Timer timer;
        TimerTask respawn = new TimerTask() {
            public void run() {
                model.processMessage(new RespawnSlayerRequest(team, name));
            }
        };

        Timer t = new Timer();
        t.schedule(respawn, 1000);
    }
}
