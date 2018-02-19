package game.model.game.model.worldObject.entity;

import game.model.game.model.ServerGameModel;

import java.util.function.BiConsumer;

public interface ServerCallBack extends BiConsumer<Entity, ServerGameModel> {
}
