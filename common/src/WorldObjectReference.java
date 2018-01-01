
import java.util.UUID;

/*
basicly a pointer to some object in the game world
dosn't hold that object itself so can be serialized
 */
public class WorldObjectReference<T extends WorldObject> {
    private UUID id = UUID.randomUUID();

    @Override
    public boolean equals(Object obj) {
        assert(obj instanceof WorldObjectReference);
        return id.equals(((WorldObjectReference) obj).id);
    }
}
