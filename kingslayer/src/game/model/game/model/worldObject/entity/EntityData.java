package game.model.game.model.worldObject.entity;

import java.util.EnumMap;
import java.util.Map;

public class EntityData extends EnumMap<Entity.EntityProperty, Object> {
    public EntityData(Class<Entity.EntityProperty> keyType) {
        super(keyType);
    }
}
