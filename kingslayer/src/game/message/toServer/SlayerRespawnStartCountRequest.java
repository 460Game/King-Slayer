package game.message.toServer;

import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;

import java.util.Timer;
import java.util.TimerTask;

public class SlayerRespawnStartCountRequest extends ActionRequest {
    Team team;
    String name;
    long id;

    public SlayerRespawnStartCountRequest() {}

    public SlayerRespawnStartCountRequest(Team team, String name, long id) {
        this.team = team;
        this.name = name;
        this.id = id;
    }
    @Override
    public void executeServer(ServerGameModel model) {
        Timer timer;
        TimerTask respawn = new TimerTask() {
            public void run() {
                model.processMessage(new RespawnSlayerRequest(team, name, id));
            }
        };

        Timer t = new Timer();
        t.schedule(respawn, 10000);
    }
}
