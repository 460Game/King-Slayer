package game.singlePlayer;

import com.esotericsoftware.kryo.Kryo;
import game.message.Message;
import game.model.game.model.Model;
import network.NetworkCommon;

public class CopyingModelWrapper implements Model {

    private Kryo kryo;

    private Model model;

    public CopyingModelWrapper(Model model) {
        kryo = new Kryo();
        NetworkCommon.KyroRegister(kryo);
        this.model = model;
    }

    @Override
    public void processMessage(Message m) {
        model.processMessage(kryo.copy(m));
    }

    @Override
    public long nanoTime() {
        return model.nanoTime();
    }
}
