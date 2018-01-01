import java.util.UUID;

/*
 contains the data needed to call T.set(this)
 */
public class WorldObjectData<T extends WorldObject> {
    WorldObjectReference<T> id;
}
