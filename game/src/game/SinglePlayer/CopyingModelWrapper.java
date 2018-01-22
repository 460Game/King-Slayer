package game.SinglePlayer;

import com.esotericsoftware.kryo.Kryo;
import game.message.Message;
import game.model.Game.Model.Model;
import game.network.NetworkCommon;

public class CopyingModelWrapper implements Model {

    static Kryo kryo;
    static {
        kryo = new Kryo();
        NetworkCommon.KyroRegister(kryo);
    }

    private Model model;

    public CopyingModelWrapper(Model model) {
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
