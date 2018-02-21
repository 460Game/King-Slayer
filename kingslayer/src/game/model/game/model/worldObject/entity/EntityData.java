package game.model.game.model.worldObject.entity;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

//TODO use EnumMap
public class EntityData extends HashMap<Entity.EntityProperty, Object> {
    public EntityData(Class<Entity.EntityProperty> keyType) {
        //super(keyType);
    }

    private EntityData(){
       // this(null);
    }
}
