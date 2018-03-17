package game.view;

import game.message.toClient.NewEntityCommand;
import game.message.toServer.*;
import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntitySpawner;
import game.model.game.model.worldObject.entity.entities.Entities;
import javafx.geometry.Point2D;
import javafx.scene.ImageCursor;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import util.Const;
import util.Util;

//import static images.Images.DELETE_CURSOR_IMAGE;
//import static images.Images.UPGRADE_CURSOR_IMAGE;
import static images.Images.*;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.DIGIT3;
import static javafx.scene.input.KeyCode.DIGIT4;
import static javafx.scene.input.KeyCode.NUMPAD4;
import static util.Util.toDrawCoords;
import static util.Util.toWorldCoords;

/*
handles interacting with the game
 */
public class GameInteractionLayer extends Region {
    private ClientGameModel model;
    public WorldPanel world;

    public GameInteractionLayer() {}

    public GameInteractionLayer(ClientGameModel clientGameModel, WorldPanel worldPanel) {
        this.model = clientGameModel;
        world = worldPanel;
        world.prefHeightProperty().bind(this.heightProperty());
        world.prefWidthProperty().bind(this.widthProperty());
        this.getChildren().add(world);

        int[] dir = {0, 0};

        world.onKeyPress(CAPS, () -> {
            Const.DEBUG_DRAW = !Const.DEBUG_DRAW;
        });

        Runnable sendMovement = () -> {
            if (dir[0] == 0 && dir[1] == 0)
                model.processMessage(new StopRequest(model.getLocalPlayer().id));
            else
                model.processMessage(new GoDirectionRequest(model.getLocalPlayer().id, Math.atan2(dir[1], dir[0])));
        };

        world.onKeyPress(() -> {
            dir[1]--;
            sendMovement.run();
        }, W, UP);

        world.onKeyRelease(() -> {
            dir[1]++;
            sendMovement.run();
        }, W, UP);

        world.onKeyRelease(() -> {
            dir[1]--;
            sendMovement.run();
        }, S, DOWN);

        world.onKeyPress(() -> {
            dir[1]++;
            sendMovement.run();
        }, S, DOWN);

        world.onKeyRelease(() -> {
            dir[0]++;
            sendMovement.run();
        }, A, LEFT);
        world.onKeyPress(() -> {
            dir[0]--;
            sendMovement.run();
        }, A, LEFT);

        world.onKeyRelease(() -> {
            dir[0]--;
            sendMovement.run();
        }, D, RIGHT);


        world.onKeyPress(() -> {
            dir[0]++;
            sendMovement.run();
        }, D, RIGHT);

    }

    public void draw() {
        world.draw();
    }

    public void stop() {
        model = null;
        world.stop();
        world = null;
    }

}
